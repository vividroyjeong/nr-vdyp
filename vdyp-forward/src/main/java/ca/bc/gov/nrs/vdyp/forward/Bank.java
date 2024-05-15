package ca.bc.gov.nrs.vdyp.forward;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.vdyp.forward.model.VdypLayerSpecies;
import ca.bc.gov.nrs.vdyp.forward.model.VdypPolygonLayer;
import ca.bc.gov.nrs.vdyp.forward.model.VdypSpeciesUtilization;
import ca.bc.gov.nrs.vdyp.model.BecDefinition;
import ca.bc.gov.nrs.vdyp.model.GenusDistributionSet;
import ca.bc.gov.nrs.vdyp.model.UtilizationClass;

class Bank {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(PolygonProcessingState.class);

	private final VdypPolygonLayer layer;
	private final BecDefinition becZone;

	/**
	 * The number of species in the state. Note that all arrays have this value plus one elements in them; the element
	 * at index 0 is unused for the species values* and contains the default utilization in the Utilization values.
	 *
	 * (*) except: siteCurveNumbers[0] is used to store the site curve of the primary species.
	 */
	private int nSpecies; // BANK1 NSPB
	private int[] indices;

	// Species information

	public String speciesNames[/* nSpecies + 1 */]; // BANK2 SP0B
	public GenusDistributionSet sp64Distributions[/* nSpecies + 1 */]; // BANK2 SP64DISTB
	public float siteIndices[/* nSpecies + 1 */]; // BANK3 SIB
	public float dominantHeights[/* nSpecies + 1 */]; // BANK3 HDB
	public float ageTotals[/* nSpecies + 1 */]; // BANK3 AGETOTB
	public float yearsAtBreastHeight[/* nSpecies + 1 */]; // BANK3 AGEBHB
	public float yearsToBreastHeight[/* nSpecies + 1 */]; // BANK3 YTBHB
	public int siteCurveNumbers[/* nSpecies + 1 */]; // BANK3 SCNB
	public int speciesIndices[/* nSpecies + 1 */]; // BANK1 ISPB
	public float percentagesOfForestedLand[/* nSpecies + 1 */]; // BANK1 PCTB

	// Utilization information, per Species

	public float basalAreas[/* nSpecies + 1, including 0 */][/* all ucs */]; // BANK1 BAB
	public float closeUtilizationVolumes[/* nSpecies + 1, including 0 */][/* all ucs */]; // BANK1 VOLCUB
	public float cuVolumesMinusDecay[/* nSpecies + 1, including 0 */][/* all ucs */]; // BANK1 VOL_DB
	public float cuVolumesMinusDecayAndWastage[/* nSpecies + 1, including 0 */][/* all ucs */]; // BANK1 VOL_DW_B
	public float loreyHeights[/* nSpecies + 1, including 0 */][/* uc -1 and 0 only */]; // BANK1 HLB
	public float quadMeanDiameters[/* nSpecies + 1, including 0 */][/* all ucs */]; // BANK1 DQB
	public float treesPerHectares[/* nSpecies + 1, including 0 */][/* all ucs */]; // BANK1 TPHB
	public float wholeStemVolumes[/* nSpecies + 1, including 0 */][/* all ucs */]; // BANK1 VOLWSB
	
	public Bank(VdypPolygonLayer layer, BecDefinition becZone) {

		this.layer = layer;
		this.becZone = becZone;

		this.nSpecies = layer.getGenera().size();
		this.indices = IntStream.range(1, nSpecies + 1).toArray();

		// In the following, index 0 is unused
		speciesNames = new String[getNSpecies() + 1];
		sp64Distributions = new GenusDistributionSet[getNSpecies() + 1];
		siteIndices = new float[getNSpecies() + 1];
		dominantHeights = new float[getNSpecies() + 1];
		ageTotals = new float[getNSpecies() + 1];
		yearsAtBreastHeight = new float[getNSpecies() + 1];
		yearsToBreastHeight = new float[getNSpecies() + 1];
		siteCurveNumbers = new int[getNSpecies() + 1];
		speciesIndices = new int[getNSpecies() + 1];
		percentagesOfForestedLand = new float[getNSpecies() + 1];

		int nUtilizationClasses = UtilizationClass.values().length;

		// In the following, index 0 is used for the default species utilization
		basalAreas = new float[getNSpecies() + 1][nUtilizationClasses];
		closeUtilizationVolumes = new float[getNSpecies() + 1][nUtilizationClasses];
		cuVolumesMinusDecay = new float[getNSpecies() + 1][nUtilizationClasses];
		cuVolumesMinusDecayAndWastage = new float[getNSpecies() + 1][nUtilizationClasses];
		loreyHeights = new float[getNSpecies() + 1][2];
		quadMeanDiameters = new float[getNSpecies() + 1][nUtilizationClasses];
		treesPerHectares = new float[getNSpecies() + 1][nUtilizationClasses];
		wholeStemVolumes = new float[getNSpecies() + 1][nUtilizationClasses];

		if (layer.getDefaultUtilizationMap().isPresent()) {
			recordUtilizations(0, layer.getDefaultUtilizationMap().get());
		}

		List<Integer> sortedSpIndices = layer.getGenera().keySet().stream().sorted(Integer::compareTo)
				.collect(Collectors.toList());
		for (int i = 0; i < sortedSpIndices.size(); i++) {
			recordSpecies(i + 1, layer.getGenera().get(sortedSpIndices.get(i)));
		}
	}

	public Bank(Bank s) {

		this.becZone = s.becZone;
		this.layer = s.layer;

		this.nSpecies = s.nSpecies;
		this.indices = copy(s.indices);

		this.yearsAtBreastHeight = copy(s.yearsAtBreastHeight);
		this.ageTotals = copy(s.ageTotals);
		this.basalAreas = copy(s.basalAreas);
		this.closeUtilizationVolumes = copy(s.closeUtilizationVolumes);
		this.cuVolumesMinusDecay = copy(s.cuVolumesMinusDecay);
		this.cuVolumesMinusDecayAndWastage = copy(s.cuVolumesMinusDecayAndWastage);
		this.dominantHeights = copy(s.dominantHeights);
		this.loreyHeights = copy(s.loreyHeights);
		this.percentagesOfForestedLand = copy(s.percentagesOfForestedLand);
		this.quadMeanDiameters = copy(s.quadMeanDiameters);
		this.siteIndices = copy(s.siteIndices);
		this.siteCurveNumbers = copy(s.siteCurveNumbers);
		this.sp64Distributions = copy(s.sp64Distributions);
		this.speciesIndices = copy(s.speciesIndices);
		this.speciesNames = copy(s.speciesNames);
		this.treesPerHectares = copy(s.treesPerHectares);
		this.wholeStemVolumes = copy(s.wholeStemVolumes);
		this.yearsToBreastHeight = copy(s.yearsToBreastHeight);
	}

	public int getNSpecies() {
		return nSpecies;
	}

	public int[] getIndices() {
		return indices;
	}

	public BecDefinition getBecZone() {
		return becZone;
	}

	public VdypPolygonLayer getLayer() {
		return layer;
	}

	private void recordSpecies(int index, VdypLayerSpecies species) {

		speciesNames[index] = species.getGenus();
		sp64Distributions[index] = species.getSpeciesDistributions();
		siteIndices[index] = species.getSiteIndex();
		dominantHeights[index] = species.getDominantHeight();
		ageTotals[index] = species.getAgeTotal();
		yearsAtBreastHeight[index] = species.getAgeAtBreastHeight();
		yearsToBreastHeight[index] = species.getYearsToBreastHeight();
		siteCurveNumbers[index] = species.getSiteCurveNumber();
		speciesIndices[index] = species.getGenusIndex();
		// percentForestedLand is output-only and so not assigned here.

		if (species.getUtilizations().isPresent()) {
			recordUtilizations(index, species.getUtilizations().get());
		} else {
			recordDefaultUtilizations(index);
		}
	}

	private void recordUtilizations(int index, Map<UtilizationClass, VdypSpeciesUtilization> suMap) {

		for (var su : suMap.entrySet()) {
			int ucIndex = su.getKey().ordinal();
			basalAreas[index][ucIndex] = su.getValue().getBasalArea();
			closeUtilizationVolumes[index][ucIndex] = su.getValue().getCloseUtilizationVolume();
			cuVolumesMinusDecay[index][ucIndex] = su.getValue().getCuVolumeMinusDecay();
			cuVolumesMinusDecayAndWastage[index][ucIndex] = su.getValue().getCuVolumeMinusDecayWastage();
			if (ucIndex < 2 /* only uc 0 and 1 have a lorey height */) {
				loreyHeights[index][ucIndex] = su.getValue().getLoreyHeight();
			}
			quadMeanDiameters[index][ucIndex] = su.getValue().getQuadraticMeanDiameterAtBH();
			treesPerHectares[index][ucIndex] = su.getValue().getLiveTreesPerHectare();
			wholeStemVolumes[index][ucIndex] = su.getValue().getWholeStemVolume();
		}
	}

	private void recordDefaultUtilizations(int index) {

		for (var uc : UtilizationClass.values()) {
			int ucIndex = uc.ordinal();
			basalAreas[index][ucIndex] = Float.NaN;
			closeUtilizationVolumes[index][ucIndex] = Float.NaN;
			cuVolumesMinusDecay[index][ucIndex] = Float.NaN;
			cuVolumesMinusDecayAndWastage[index][ucIndex] = Float.NaN;
			if (ucIndex < 2 /* only uc 0 and 1 have a lorey height */) {
				loreyHeights[index][ucIndex] = Float.NaN;
			}
			quadMeanDiameters[index][ucIndex] = Float.NaN;
			treesPerHectares[index][ucIndex] = Float.NaN;
			wholeStemVolumes[index][ucIndex] = Float.NaN;
		}
	}

	public Bank copy() {
		return new Bank(this);
	}

	private GenusDistributionSet[] copy(GenusDistributionSet[] a) {
		GenusDistributionSet[] t = new GenusDistributionSet[a.length];

		for (int i = 0; i < a.length; i++)
			if (a[i] != null) {
				t[i] = a[i].copy();
			}

		return t;
	}

	private String[] copy(String[] a) {
		String[] t = new String[a.length];

		for (int i = 0; i < a.length; i++)
			if (a[i] != null) {
				t[i] = a[i];
			}
		return t;
	}

	private int[] copy(int[] a) {
		int[] t = new int[a.length];

		System.arraycopy(a, 0, t, 0, a.length);

		return t;
	}

	private float[] copy(float[] a) {
		float[] t = new float[a.length];

		System.arraycopy(a, 0, t, 0, a.length);

		return t;
	}

	private float[][] copy(float[][] a) {
		float[][] t = new float[a.length][];

		for (int i = 0; i < a.length; i++) {
			t[i] = new float[a[i].length];
			for (int j = 0; j < a[i].length; j++)
				t[i][j] = a[i][j];
		}

		return t;
	}

	/**
	 * Replace species at index <code>i</code> with that at index <code>j</code>, i < j.
	 *
	 * @param toIndex   the index of the species to remove, i >= 1
	 * @param fromIndex the index of the species to replace it with, j >= i and j <= nSpecies
	 */
	private void move(int toIndex, int fromIndex) {
		if (toIndex > fromIndex || toIndex < 1 || fromIndex > getNSpecies()) {
			throw new IllegalArgumentException(
					MessageFormat.format(
							"PolygonProcessingState.replace - illegal arguments i = {0}, j = {1}, nSpecies = {2}",
							toIndex, fromIndex, getNSpecies()
					)
			);
		}

		if (toIndex < fromIndex) {

			speciesNames[toIndex] = speciesNames[fromIndex];
			sp64Distributions[toIndex] = sp64Distributions[fromIndex];
			siteIndices[toIndex] = siteIndices[fromIndex];
			dominantHeights[toIndex] = dominantHeights[fromIndex];
			ageTotals[toIndex] = ageTotals[fromIndex];
			yearsAtBreastHeight[toIndex] = yearsAtBreastHeight[fromIndex];
			yearsToBreastHeight[toIndex] = yearsToBreastHeight[fromIndex];
			siteCurveNumbers[toIndex] = siteCurveNumbers[fromIndex];
			speciesIndices[toIndex] = speciesIndices[fromIndex];
			percentagesOfForestedLand[toIndex] = percentagesOfForestedLand[fromIndex];

			basalAreas[toIndex] = basalAreas[fromIndex];
			closeUtilizationVolumes[toIndex] = closeUtilizationVolumes[fromIndex];
			cuVolumesMinusDecay[toIndex] = cuVolumesMinusDecay[fromIndex];
			cuVolumesMinusDecayAndWastage[toIndex] = cuVolumesMinusDecayAndWastage[fromIndex];
			loreyHeights[toIndex] = loreyHeights[fromIndex];
			quadMeanDiameters[toIndex] = quadMeanDiameters[fromIndex];
			treesPerHectares[toIndex] = treesPerHectares[fromIndex];
			wholeStemVolumes[toIndex] = wholeStemVolumes[fromIndex];
		}
	}

	private void retainOnly(Set<Integer> speciesToRetainByIndexSet) {

		var speciesToRetainByIndex = new ArrayList<>(speciesToRetainByIndexSet);

		speciesToRetainByIndex.sort(Integer::compareTo);

		int nextAvailableSlot = 1;
		for (int index : speciesToRetainByIndex) {
			if (nextAvailableSlot != index) {
				move(nextAvailableSlot, index);
			}
			nextAvailableSlot += 1;
		}

		if (nextAvailableSlot > 1) {
			nSpecies = speciesToRetainByIndex.size();
			int nElements = nSpecies + 1;

			speciesNames = Arrays.copyOf(speciesNames, nElements);
			sp64Distributions = Arrays.copyOf(sp64Distributions, nElements);
			siteIndices = Arrays.copyOf(siteIndices, nElements);
			dominantHeights = Arrays.copyOf(dominantHeights, nElements);
			ageTotals = Arrays.copyOf(ageTotals, nElements);
			yearsAtBreastHeight = Arrays.copyOf(yearsAtBreastHeight, nElements);
			yearsToBreastHeight = Arrays.copyOf(yearsToBreastHeight, nElements);
			siteCurveNumbers = Arrays.copyOf(siteCurveNumbers, nElements);
			speciesIndices = Arrays.copyOf(speciesIndices, nElements);
			percentagesOfForestedLand = Arrays.copyOf(percentagesOfForestedLand, nElements);

			basalAreas = Arrays.copyOf(basalAreas, nElements);
			closeUtilizationVolumes = Arrays.copyOf(closeUtilizationVolumes, nElements);
			cuVolumesMinusDecay = Arrays.copyOf(cuVolumesMinusDecay, nElements);
			cuVolumesMinusDecayAndWastage = Arrays.copyOf(cuVolumesMinusDecayAndWastage, nElements);
			loreyHeights = Arrays.copyOf(loreyHeights, nElements);
			quadMeanDiameters = Arrays.copyOf(quadMeanDiameters, nElements);
			treesPerHectares = Arrays.copyOf(treesPerHectares, nElements);
			wholeStemVolumes = Arrays.copyOf(wholeStemVolumes, nElements);
		}
	}

	public void removeSpecies(Predicate<Integer> removeCriteria) {

		Set<Integer> speciesToRetainByIndex = new HashSet<>();
		for (int i = 1; i <= getNSpecies(); i++) {
			if (!removeCriteria.test(i)) {
				speciesToRetainByIndex.add(i);
			}
		}

		retainOnly(speciesToRetainByIndex);
	}
}