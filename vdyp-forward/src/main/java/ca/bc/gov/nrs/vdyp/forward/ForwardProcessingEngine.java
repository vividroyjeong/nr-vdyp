package ca.bc.gov.nrs.vdyp.forward;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.vdyp.application.ProcessingException;
import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.common.GenusDefinitionMap;
import ca.bc.gov.nrs.vdyp.common_calculators.enumerations.SiteIndexEquation;
import ca.bc.gov.nrs.vdyp.forward.model.VdypEntity;
import ca.bc.gov.nrs.vdyp.forward.model.VdypPolygon;
import ca.bc.gov.nrs.vdyp.model.BecDefinition;
import ca.bc.gov.nrs.vdyp.model.BecLookup;
import ca.bc.gov.nrs.vdyp.model.CommonData;
import ca.bc.gov.nrs.vdyp.model.GenusDefinition;
import ca.bc.gov.nrs.vdyp.model.GenusDistribution;
import ca.bc.gov.nrs.vdyp.model.LayerType;
import ca.bc.gov.nrs.vdyp.model.Region;
import ca.bc.gov.nrs.vdyp.model.SiteCurve;
import ca.bc.gov.nrs.vdyp.model.UtilizationClass;
import ca.bc.gov.nrs.vdyp.si32.site.SiteTool;

public class ForwardProcessingEngine {

	private static final Logger logger = LoggerFactory.getLogger(ForwardProcessor.class);

	private static final float MIN_BASAL_AREA = 0.001f;

	private final ForwardProcessingState fps;
	private final GenusDefinitionMap genusDefinitionMap;
	private final BecLookup becLookup;
	private final Map<String, SiteCurve> siteCurveMap;

	@SuppressWarnings("unchecked")
	public ForwardProcessingEngine(Map<String, Object> controlMap) {

		genusDefinitionMap = new GenusDefinitionMap((List<GenusDefinition>) controlMap.get(ControlKey.SP0_DEF.name()));
		becLookup = (BecLookup) controlMap.get(ControlKey.BEC_DEF.name());
		siteCurveMap = (Map<String, SiteCurve>) controlMap.get(ControlKey.SITE_CURVE_NUMBERS.name());

		fps = new ForwardProcessingState();
	}

	public GenusDefinitionMap getGenusDefinitionMap() {
		return genusDefinitionMap;
	}

	public BecLookup getBecLookup() {
		return becLookup;
	}

	public Map<String, SiteCurve> getSiteCurveMap() {
		return Collections.unmodifiableMap(siteCurveMap);
	}

	public void processPolygon(VdypPolygon polygon) throws ProcessingException {

		logger.info("Starting processing of polygon {}", polygon.getDescription());

		fps.setStartingState(polygon);

		// All of BANKCHK1 that we need
		validatePolygon(polygon);

		PolygonProcessingState workingBank = fps.getBank(LayerType.PRIMARY, 0).copy();

		// SCINXSET
		completeSiteCurveMap(polygon.getBiogeoclimaticZone(), workingBank);

		// VPRIME, method == 1
		setSpecies(workingBank);

		fps.setActive(LayerType.PRIMARY, 0);
	}

	private static void setSpecies(PolygonProcessingState bank) {

		setPercentages(bank);
		
		setPolygonRankingDetails(bank, CommonData.PRIMARY_SPECIES_TO_COMBINE);
	}

	public static void setPercentages(PolygonProcessingState bank) {
		logger.atDebug().addArgument(bank.getNSpecies()).addArgument(bank.basalAreas[0][0])
				.log(
						"Calculating percents as a ratio of Species BA over Total BA. # species: {}; Layer total 7.5cm+ basal area: {}"
				);

		int ucIndex = UtilizationClass.ALL.ordinal();
		for (int i = 1; i <= bank.getNSpecies(); i++) {
			bank.percentagesOfForestedLand[i] = bank.basalAreas[i][ucIndex] / bank.basalAreas[0][ucIndex] * 100.0f;

			logger.atDebug().addArgument(i).addArgument(bank.speciesIndices[i]).addArgument(bank.speciesNames[i])
					.addArgument(bank.basalAreas[i][0]).addArgument(bank.percentagesOfForestedLand[i])
					.log("Species {}: SP0 {}, Name {}, Species 7.5cm+ BA {}, Calculated Percent {}");
		}
	}

	private void completeSiteCurveMap(BecDefinition becZone, PolygonProcessingState bank) {

		for (int i = 0; i < bank.getNSpecies(); i++) {

			if (bank.siteCurveNumbers[i] == VdypEntity.MISSING_INTEGER_VALUE) {

				Optional<SiteIndexEquation> scIndex = Optional.empty();

				Optional<GenusDistribution> sp0Dist = bank.sp64Distributions[i].getSpeciesDistribution(0);

				if (sp0Dist.isPresent()) {
					if (siteCurveMap.size() > 0) {
						SiteCurve sc = siteCurveMap.get(sp0Dist.get().getGenus().getAlias());
						scIndex = Optional.of(sc.getValue(becZone.getRegion()));
					} else {
						scIndex = Optional.of(
								SiteTool.getSICurve(bank.speciesNames[i], becZone.getRegion().equals(Region.COASTAL))
						);
					}
				}

				if (scIndex.isEmpty()) {
					if (siteCurveMap.size() > 0) {
						SiteCurve sc = siteCurveMap.get(bank.speciesNames[i]);
						scIndex = Optional.of(sc.getValue(becZone.getRegion()));
					} else {
						scIndex = Optional.of(
								SiteTool.getSICurve(bank.speciesNames[i], becZone.getRegion().equals(Region.COASTAL))
						);
					}
				}
			}
		}
	}

	private void validatePolygon(VdypPolygon polygon) throws ProcessingException {

		if (polygon.getDescription().getYear() < 1900) {

			throw new ProcessingException(
					MessageFormat.format(
							"Polygon {}'s year value {} is < 1900", 101, polygon.getDescription().getName(), polygon
									.getDescription().getYear()
					)
			);
		}

		// The following is extracted from BANKCHK1, simplified for the parameters METH_CHK = 4,
		// LayerI = 1, and INSTANCE = 1. So IR = 1, which is the first bank, numbered 0.

		// => all that is done is that species with basal area < MIN_BASAL_AREA are removed.

		PolygonProcessingState pps = fps.getBank(LayerType.PRIMARY, 0);

		pps.removeSpecies(i -> pps.basalAreas[i][UtilizationClass.ALL.ordinal()] < MIN_BASAL_AREA);

		if (pps.getNSpecies() == 0) {
			throw new ProcessingException(
					MessageFormat.format(
							"Polygon {0} layer 0 has no species with basal area above {1}", polygon.getDescription()
									.getName(), MIN_BASAL_AREA
					)
			);
		}
	}

	// PRIMFIND
	/**
	 * Returns a {@code SpeciesRankingDetails} instance giving:
	 * <ul>
	 * <li>the index in {@code bank} of the primary species
	 * <li>the index in {@code bank} of the secondary species, or Optional.empty() if none, and
	 * <li>the percentage of forested land occupied by the primary species
	 * </ul>
	 * @param bank the bank on which to operate
	 * @return as described
	 */
	static void setPolygonRankingDetails(PolygonProcessingState bank, Collection<List<String>> speciesToCombine) {

		if (bank.getNSpecies() == 0) {
			throw new IllegalArgumentException("Can not find primary species as there are no species");
		}

		float percentages[] = Arrays.copyOf(bank.percentagesOfForestedLand, bank.percentagesOfForestedLand.length);

		for (var speciesPair : speciesToCombine) {
			combinePercentages(bank.speciesNames, speciesPair, percentages);
		}

		float highestPercentage = 0.0f;
		int highestPercentageIndex = -1;
		float secondHighestPercentage = 0.0f;
		int secondHighestPercentageIndex = -1;
		for (int i = 0; i < percentages.length; i++) {

			if (percentages[i] > highestPercentage) {

				secondHighestPercentageIndex = highestPercentageIndex;
				secondHighestPercentage = highestPercentage;
				highestPercentageIndex = i;
				highestPercentage = percentages[i];
				
			} else if (percentages[i] > secondHighestPercentage) {

				secondHighestPercentageIndex = i;
				secondHighestPercentage = percentages[i];
			}

			// TODO: implement NDEBUG22 = 1 logic
		}

		if (highestPercentageIndex == -1) {
			throw new IllegalStateException("There are no species with covering percentage > 0");
		}

		String primaryGenusName = bank.speciesNames[highestPercentageIndex];
		Optional<String> secondaryGenusName = secondHighestPercentageIndex != -1
				? Optional.of(bank.speciesNames[secondHighestPercentageIndex])
				: Optional.empty();

		try {
			int inventoryTypeGroup = findInventoryTypeGroup(primaryGenusName, secondaryGenusName, highestPercentage);

			bank.setSpeciesRankingDetails(new SpeciesRankingDetails(
					highestPercentageIndex, secondHighestPercentageIndex != -1
							? Optional.of(secondHighestPercentageIndex)
							: Optional.empty(),
					inventoryTypeGroup
			));
		} catch (ProcessingException e) {
			// This should never fail because the bank has already been validated and hence the genera
			// are known to be valid.

			throw new IllegalStateException(e);
		}
	}

	static void combinePercentages(String[] speciesNames, List<String> combinationGroup, float[] percentages) {

		if (combinationGroup.size() != 2) {
			throw new IllegalArgumentException(
					MessageFormat.format("combinationGroup must have size 2; it has size", combinationGroup.size())
			);
		}

		if (speciesNames.length != percentages.length) {
			throw new IllegalArgumentException(
					MessageFormat.format(
							"the length of speciesNames ({}) must match that of percentages ({}) but it doesn't", speciesNames.length, percentages.length
					)
			);
		}

		Set<Integer> groupIndices = new HashSet<>();
		for (int i = 0; i < speciesNames.length; i++) {
			if (combinationGroup.contains(speciesNames[i]))
				groupIndices.add(i);
		}

		if (groupIndices.size() == 2) {
			Integer[] groupIndicesArray = new Integer[2];
			groupIndices.toArray(groupIndicesArray);

			int higherPercentageIndex;
			int lowerPercentageIndex;
			if (percentages[groupIndicesArray[0]] > percentages[groupIndicesArray[1]]) {
				higherPercentageIndex = groupIndicesArray[0];
				lowerPercentageIndex = groupIndicesArray[1];
			} else {
				higherPercentageIndex = groupIndicesArray[1];
				lowerPercentageIndex = groupIndicesArray[0];
			}
			percentages[higherPercentageIndex] = percentages[higherPercentageIndex] + percentages[lowerPercentageIndex];
			percentages[lowerPercentageIndex] = 0.0f;
		}
	}

	// ITGFIND
	/**
	 * Find Inventory type group (ITG) for the given primary and secondary (if given) genera.
	 * 
	 * @param primaryGenus the genus of the primary species
	 * @param optionalSecondaryGenus the genus of the primary species, which may be empty
	 * @param primaryPercentage the percentage covered by the primary species
	 * @return as described
	 * @throws ProcessingException if primaryGenus is not a known genus
	 */
	static int findInventoryTypeGroup(
			String primaryGenus, Optional<String> optionalSecondaryGenus, float primaryPercentage
	)
			throws ProcessingException {

		if (primaryPercentage > 79.999) { // Copied from VDYP7

			Integer recordedInventoryTypeGroup = CommonData.ITG_PURE.get(primaryGenus);
			if (recordedInventoryTypeGroup == null) {
				throw new ProcessingException("Unrecognized primary species: " + primaryGenus);
			}

			return recordedInventoryTypeGroup;
		}

		String secondaryGenus = optionalSecondaryGenus.isPresent() ? optionalSecondaryGenus.get() : "";

		if (primaryGenus.equals(secondaryGenus)) {
			throw new IllegalArgumentException("The primary and secondary genera are the same");
		}

		switch (primaryGenus) {
		case "F":
			switch (secondaryGenus) {
			case "C", "Y":
				return 2;
			case "B", "H":
				return 3;
			case "S":
				return 4;
			case "PL", "PA":
				return 5;
			case "PY":
				return 6;
			case "L", "PW":
				return 7;
			default:
				return 8;
			}
		case "C", "Y":
			switch (secondaryGenus) {
			case "H", "B", "S":
				return 11;
			default:
				return 10;
			}
		case "H":
			switch (secondaryGenus) {
			case "C", "Y":
				return 14;
			case "B":
				return 15;
			case "S":
				return 16;
			default:
				return 13;
			}
		case "B":
			switch (secondaryGenus) {
			case "C", "Y", "H":
				return 19;
			default:
				return 20;
			}
		case "S":
			switch (secondaryGenus) {
			case "C", "Y", "H":
				return 23;
			case "B":
				return 24;
			case "PL":
				return 25;
			default:
				if (CommonData.HARDWOODS.contains(secondaryGenus)) {
					return 26;
				}
				return 22;
			}
		case "PW":
			return 27;
		case "PL", "PA":
			switch (secondaryGenus) {
			case "PL", "PA":
				return 28;
			case "F", "PW", "L", "PY":
				return 29;
			default:
				if (CommonData.HARDWOODS.contains(secondaryGenus)) {
					return 31;
				}
				return 30;
			}
		case "PY":
			return 32;
		case "L":
			switch (secondaryGenus) {
			case "F":
				return 33;
			default:
				return 34;
			}
		case "AC":
			if (CommonData.HARDWOODS.contains(secondaryGenus)) {
				return 36;
			}
			return 35;
		case "D":
			if (CommonData.HARDWOODS.contains(secondaryGenus)) {
				return 38;
			}
			return 37;
		case "MB":
			return 39;
		case "E":
			return 40;
		case "AT":
			if (CommonData.HARDWOODS.contains(secondaryGenus)) {
				return 42;
			}
			return 41;
		default:
			throw new ProcessingException("Unrecognized primary species: " + primaryGenus);
		}
	}
}
