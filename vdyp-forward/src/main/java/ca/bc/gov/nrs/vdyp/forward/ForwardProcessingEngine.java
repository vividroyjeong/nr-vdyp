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
import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.CommonCalculatorException;
import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.CurveErrorException;
import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.NoAnswerException;
import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.SpeciesErrorException;
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

	// Read-only
	private final GenusDefinitionMap genusDefinitionMap;
	private final BecLookup becLookup;
	private final Map<String, SiteCurve> siteCurveMap;

	private final ForwardProcessingState fps;

	public ForwardProcessingEngine(Map<String, Object> controlMap) {

		this(controlMap, new ForwardProcessingState());
	}

	@SuppressWarnings("unchecked")
	/* pp */ ForwardProcessingEngine(Map<String, Object> controlMap, ForwardProcessingState fps) {

		// Reference, since read-only
		genusDefinitionMap = new GenusDefinitionMap((List<GenusDefinition>) controlMap.get(ControlKey.SP0_DEF.name()));
		becLookup = (BecLookup) controlMap.get(ControlKey.BEC_DEF.name());
		siteCurveMap = (Map<String, SiteCurve>) controlMap.get(ControlKey.SITE_CURVE_NUMBERS.name());

		this.fps = fps;
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
		
		executeForwardAlgorithm(workingBank, siteCurveMap, Integer.MAX_VALUE);
	}
	
	static void executeForwardAlgorithm(PolygonProcessingState bank, Map<String, SiteCurve> siteCurveMap, int lastStepToExecute) {
		
		// SCINXSET
		if (lastStepToExecute >= 0) {
			calculateMissingSiteCurves(bank, siteCurveMap);
		}
		
		// VPRIME, method == 1
		if (lastStepToExecute >= 1) {
			calculateCoverages(bank);
		}

		if (lastStepToExecute >= 2) {
			determinePolygonRankings(bank, CommonData.PRIMARY_SPECIES_TO_COMBINE);
		}
		
		// ADDSITE
		if (lastStepToExecute >= 3) {
			estimateMissingSiteIndices(bank);
		}

		if (lastStepToExecute >= 4) {
			estimateMissingYearsToBreastHeightValues(bank);
		}
	}

	/**
	 * For each species for which a years-to-breast-height value was not supplied, calculate it
	 * from the given years-at-breast-height and age-total values if given or otherwise 
	 * estimate it from the site curve and site index values for the species.
	 * 
	 * @param bank the bank in which calculations are performed
	 */
	private static void estimateMissingYearsToBreastHeightValues(PolygonProcessingState bank) {

		int primarySpeciesIndex = bank.getSpeciesRankingDetails().primarySpeciesIndex();
		float primarySpeciesSiteIndex = bank.siteIndices[primarySpeciesIndex];
		
		// Determine the default site index by using the site index of the primary species unless 
		// it hasn't been set in which case pick any. Note that there may still not be a 
		// meaningful value after this for example when the value is not available for the primary \
		// species (see estimateMissingSiteIndices) and it's the only one.
		
		float defaultSiteIndex = primarySpeciesSiteIndex;
		
		if (defaultSiteIndex <= 0.0) {
			for (int i: bank.getIndices()) {
				if (bank.siteIndices[i] > 0.0) {
					defaultSiteIndex = bank.siteIndices[i];
					break;
				}
			}
		}
		
		for (int i: bank.getIndices()) {
			if (bank.yearsToBreastHeight[i] > 0.0) {
				// was supplied
				continue;
			}
			
			if (bank.yearsAtBreastHeight[i] > 0.0 && bank.ageTotals[i] > bank.yearsAtBreastHeight[i]) {
				bank.yearsToBreastHeight[i] = bank.ageTotals[i] - bank.yearsAtBreastHeight[i];
				continue;
			}
			
			float siteIndex = bank.siteIndices[i] > 0.0 ? bank.siteIndices[i] : defaultSiteIndex;
			try {
				SiteIndexEquation curve = SiteIndexEquation.getByIndex(bank.siteCurveNumbers[i]);
				double yearsToBreastHeight = SiteTool.yearsToBreastHeight(curve, siteIndex);
				bank.yearsToBreastHeight[i] = (float)yearsToBreastHeight;
			} catch (CommonCalculatorException e) {
				logger.warn(MessageFormat.format("Unable to determine yearsToBreastHeight of species {0}", i) , e);
			}
		}
	}

	/**
	 * (1) If the site index of the primary species has not been set, calculate it as the average 
	 * of the site indices of the other species that -do- have one, after converting it between 
	 * the site curve of the other species and that of the primary species.
	 * <p>
	 * (2) 
	 * If the site index of the primary species has (now) been set, calculate that of the other 
	 * species whose site index has not been set from the primary site index after converting it 
	 * between the site curve of the other species and that of the primary species.
	 *
	 * @param bank the bank in which the calculations are done.
	 */
	static void estimateMissingSiteIndices(PolygonProcessingState bank) {
		
		int primarySpeciesIndex = bank.getSpeciesRankingDetails().primarySpeciesIndex();
		SiteIndexEquation primarySiteCurve = SiteIndexEquation.getByIndex(bank.siteCurveNumbers[primarySpeciesIndex]);
		
		// (1)
		
		if (bank.siteIndices[primarySpeciesIndex] <= 0.0 && bank.getNSpecies() > 1) {
			
			double otherSiteIndicesSum = 0.0f;
			int nOtherSiteIndices = 0;
			
			for (int i: bank.getIndices()) {
				
				if (i == primarySpeciesIndex) {
					continue;
				}
					
				double siteIndexI = bank.siteIndices[i];
				
				if (siteIndexI > 0.0) {
					SiteIndexEquation siteCurveI = SiteIndexEquation.getByIndex(bank.siteCurveNumbers[i]);
					
					Double mappedSiteIndex = null;
					try {
						mappedSiteIndex = SiteTool.convertSiteIndexBetweenCurves(siteCurveI, siteIndexI, primarySiteCurve);
					} catch (CurveErrorException | SpeciesErrorException | NoAnswerException e) {
						logger.error(MessageFormat.format("convertSiteIndexBetweenCurves on {0}, {1} and {2} failed. Skipping species {3}", siteCurveI, siteIndexI, primarySiteCurve, i), e);
					}
					
					if (mappedSiteIndex != null) {
						otherSiteIndicesSum += siteIndexI;
						nOtherSiteIndices += 1;
					}
				}
			}
			
			if (nOtherSiteIndices > 0) {
				bank.siteIndices[primarySpeciesIndex] = (float)(otherSiteIndicesSum / nOtherSiteIndices);
			}
		}
			
		// (2)
		
		double primarySpeciesSiteIndex = bank.siteIndices[primarySpeciesIndex];
		if (primarySpeciesSiteIndex > 0.0f) {
			
			for (int i: bank.getIndices()) {
				
				if (i == primarySpeciesIndex) {
					continue;
				}

				double siteIndexI = bank.siteIndices[i];
				if (siteIndexI == 0.0) {
					SiteIndexEquation siteCurveI = SiteIndexEquation.getByIndex(bank.siteCurveNumbers[i]);
					
					try {
						double mappedSiteIndex = SiteTool.convertSiteIndexBetweenCurves(primarySiteCurve, primarySpeciesSiteIndex, siteCurveI);
						bank.siteIndices[i] = (float)mappedSiteIndex;
					} catch (CurveErrorException | SpeciesErrorException | NoAnswerException e) {
						logger.error(MessageFormat.format("convertSiteIndexBetweenCurves on {0}, {1} and {2} failed. Skipping species {3}", primarySiteCurve, primarySpeciesSiteIndex, siteCurveI, i), e);
					}
				}
			}
		}
		
		// Finally, set bank.siteIndices[0] to that of the primary species.
		bank.siteIndices[0] = (float)primarySpeciesSiteIndex;
	}

	/**
	 * Calculate the percentage of forested land covered by each species by dividing the basal area 
	 * of each given species with the basal area of the polygon covered by forest.
	 * 
	 * @param bank the bank in which the calculations are performed
	 */
	static void calculateCoverages(PolygonProcessingState bank) {
		
		logger.atDebug().addArgument(bank.getNSpecies()).addArgument(bank.basalAreas[0][0]).log(
				"Calculating coverages as a ratio of Species BA over Total BA. # species: {}; Layer total 7.5cm+ basal area: {}"
		);

		int ucIndex = UtilizationClass.ALL.ordinal();
		for (int i: bank.getIndices()) {
			bank.percentagesOfForestedLand[i] = bank.basalAreas[i][ucIndex] / bank.basalAreas[0][ucIndex] * 100.0f;

			logger.atDebug().addArgument(i).addArgument(bank.speciesIndices[i]).addArgument(bank.speciesNames[i])
					.addArgument(bank.basalAreas[i][0]).addArgument(bank.percentagesOfForestedLand[i])
					.log("Species {}: SP0 {}, Name {}, Species 7.5cm+ BA {}, Calculated Percent {}");
		}
	}

	/**
	 * Calculate the siteCurve number of all species for which one was not supplied.
	 * 
	 * @param bank the bank in which the calculations are done.
	 * @param becZone the BEC zone definitions.
	 * @param siteCurveMap the Site Curve definitions.
	 */
	static void calculateMissingSiteCurves(
			PolygonProcessingState bank, Map<String, SiteCurve> siteCurveMap
	) {
		BecDefinition becZone = bank.getBecZone();
		
		for (int i: bank.getIndices()) {

			if (bank.siteCurveNumbers[i] == VdypEntity.MISSING_INTEGER_VALUE) {

				Optional<SiteIndexEquation> scIndex = Optional.empty();

				Optional<GenusDistribution> sp0Dist = bank.sp64Distributions[i].getSpeciesDistribution(0);

				if (sp0Dist.isPresent()) {
					if (siteCurveMap.size() > 0) {
						SiteCurve sc = siteCurveMap.get(sp0Dist.get().getGenus().getAlias());
						scIndex = sc == null ? Optional.empty() : Optional.of(sc.getValue(becZone.getRegion()));
					} else {
						SiteIndexEquation siCurve = SiteTool.getSICurve(bank.speciesNames[i], becZone.getRegion().equals(Region.COASTAL));
						scIndex = siCurve == SiteIndexEquation.SI_NO_EQUATION ? Optional.empty() : Optional.of(siCurve);
					}
				}

				if (scIndex.isEmpty()) {
					if (siteCurveMap.size() > 0) {
						SiteCurve sc = siteCurveMap.get(bank.speciesNames[i]);
						scIndex = sc == null ? Optional.empty() : Optional.of(sc.getValue(becZone.getRegion()));
					} else {
						SiteIndexEquation siCurve = SiteTool.getSICurve(bank.speciesNames[i], becZone.getRegion().equals(Region.COASTAL));
						scIndex = siCurve == SiteIndexEquation.SI_NO_EQUATION ? Optional.empty() : Optional.of(siCurve);
					}
				}

				bank.siteCurveNumbers[i] = scIndex.orElseThrow().n();
			}
		}
	}

	/**
	 * Validate that the given polygon is in good order for processing.
	 * 
	 * @param polygon the subject polygon.
	 * @returns if this method doesn't throw, all is good.
	 * @throws ProcessingException if the polygon does not pass validation.
	 */
	private void validatePolygon(VdypPolygon polygon) throws ProcessingException {

		if (polygon.getDescription().getYear() < 1900) {

			throw new ProcessingException(
					MessageFormat.format(
							"Polygon {}'s year value {} is < 1900", 101, polygon.getDescription().getName(), polygon
									.getDescription().getYear()
					)
			);
		}

		// The following is extracted from BANKCHK1, simplified for the parameters
		// METH_CHK = 4, LayerI = 1, and INSTANCE = 1. So IR = 1, which is the first
		// bank, numbered 0.

		// => all that is done is that species with basal area < MIN_BASAL_AREA are
		// removed.

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
	 *
	 * @param bank the bank on which to operate
	 * @return as described
	 */
	static void determinePolygonRankings(PolygonProcessingState bank, Collection<List<String>> speciesToCombine) {

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
		for (int i: bank.getIndices()) {

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

			bank.setSpeciesRankingDetails(
					new SpeciesRankingDetails(
							highestPercentageIndex,
							secondHighestPercentageIndex != -1 ? Optional.of(secondHighestPercentageIndex)
									: Optional.empty(),
							inventoryTypeGroup
					)
			);
		} catch (ProcessingException e) {
			// This should never fail because the bank has already been validated and hence the genera
			// are known to be valid.

			throw new IllegalStateException(e);
		}
	}

	/**
	 * <code>combinationGroup</code> is a list of precisely two species names. This method determines
	 * the indices within <code>speciesNames</code> that match the two given names. If two do, say i and j, 
	 * the <code>percentages</code> is modified as follows. Assuming percentages[i] > percentages[j], 
	 * percentages[i] is set to percentages[i] + percentages[j] and percentages[j] is set to 0.0. If fewer 
	 * than two indices match, nothing is done.
	 *  
	 * @param speciesNames an array of (possibly null) distinct Strings.
	 * @param combinationGroup a pair of (not null) Strings.
	 * @param percentages an array with one entry for each entry in <code>speciesName</code>.
	 */
	static void combinePercentages(String[] speciesNames, List<String> combinationGroup, float[] percentages) {

		if (combinationGroup.size() != 2) {
			throw new IllegalArgumentException(
					MessageFormat.format("combinationGroup must have size 2; it has size", combinationGroup.size())
			);
		}

		if (combinationGroup.get(0) == null || combinationGroup.get(1) == null) {
			throw new IllegalArgumentException("combinationGroup must not contain null values");
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
	 * @param primaryGenus           the genus of the primary species
	 * @param optionalSecondaryGenus the genus of the primary species, which may be empty
	 * @param primaryPercentage      the percentage covered by the primary species
	 * @return as described
	 * @throws ProcessingException if primaryGenus is not a known genus
	 */
	static int findInventoryTypeGroup(
			String primaryGenus, Optional<String> optionalSecondaryGenus, float primaryPercentage
	) throws ProcessingException {

		if (primaryPercentage > 79.999 /* Copied from VDYP7 */) {

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
