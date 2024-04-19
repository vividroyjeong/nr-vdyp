package ca.bc.gov.nrs.vdyp.forward;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import ca.bc.gov.nrs.vdyp.common.GenusDefinitionMap;
import ca.bc.gov.nrs.vdyp.forward.model.VdypLayerSpecies;
import ca.bc.gov.nrs.vdyp.forward.model.VdypPolygonLayer;
import ca.bc.gov.nrs.vdyp.forward.model.VdypSpeciesUtilization;
import ca.bc.gov.nrs.vdyp.model.GenusDefinition;
import ca.bc.gov.nrs.vdyp.model.SpeciesDistributionSet;
import ca.bc.gov.nrs.vdyp.model.UtilizationClass;

class PolygonProcessingState {
	
	/** 
	 * The number of species in the state. Note that all arrays have one more element in them; the
	 * element at index 0 is unused for the species values and contains the default utilization
	 * in the Utilization values.
	 */
	private int nSpecies; // BANK1 NSPB

	// Species information
	
	public String speciesName[/* nSpecies + 1 */]; // BANK2 SP0B
	public SpeciesDistributionSet sp64Distribution[/* nSpecies + 1 */]; // BANK2 SP64DISTB
	public float siteIndex[/* nSpecies + 1 */]; // BANK3 SIB
	public float dominantHeight[/* nSpecies + 1 */]; // BANK3 HDB
	public float ageTotal[/* nSpecies + 1 */]; // BANK3 AGETOTB
	public float ageBreastHeight[/* nSpecies + 1 */]; // BANK3 AGEBHB
	public float yearsToBreastHeight[/* nSpecies + 1 */]; // BANK3 YTBHB
	public Optional<Integer> siteCurveNumber[/* nSpecies + 1 */]; // BANK3 SCNB
	public int speciesIndex[/* nSpecies + 1 */]; // BANK1 ISPB
	public float percentForestedLand[/* nSpecies + 1 */]; // BANK1 PCTB

	// Utilization information, per Species
	
	public float basalArea[/* nSpecies + 1, including 0 */][/* all ucs */]; // BANK1 BAB
	public float closeUtilizationVolume[/* nSpecies + 1, including 0 */][/* all ucs */]; // BANK1 VOLCUB
	public float cuVolumeMinusDecay[/* nSpecies + 1, including 0 */][/* all ucs */]; // BANK1 VOL_DB
	public float cuVolumeMinusDecayWastage[/* nSpecies + 1, including 0 */][/* all ucs */]; // BANK1 VOL_DW_B
	public float loreyHeight[/* nSpecies + 1, including 0 */][/* uc -1 and 0 only */]; // BANK1 HLB
	public float quadMeanDiameter[/* nSpecies + 1, including 0 */][/* all ucs */]; // BANK1 DQB
	public float treesPerHectare[/* nSpecies + 1, including 0 */][/* all ucs */]; // BANK1 TPHB
	public float wholeStemVolume[/* nSpecies + 1, including 0 */][/* all ucs */]; // BANK1 VOLWSB

	private final GenusDefinitionMap genera;

	@SuppressWarnings("unchecked")
	public PolygonProcessingState(GenusDefinitionMap genera) {

		this.genera = genera;

		this.nSpecies = genera.getMaxIndex();

		// In the following, index 0 is unused
		speciesName = new String[getNSpecies() + 1];
		sp64Distribution = new SpeciesDistributionSet[getNSpecies() + 1];
		siteIndex = new float[getNSpecies() + 1];
		dominantHeight = new float[getNSpecies() + 1];
		ageTotal = new float[getNSpecies() + 1];
		ageBreastHeight = new float[getNSpecies() + 1];
		yearsToBreastHeight = new float[getNSpecies() + 1];
		siteCurveNumber = new Optional[getNSpecies() + 1];
		speciesIndex = new int[getNSpecies() + 1];
		percentForestedLand = new float[getNSpecies() + 1];

		int nUtilizationClasses = UtilizationClass.values().length;

		// In the following, index 0 is used for the default species utilization
		basalArea = new float[getNSpecies() + 1][nUtilizationClasses];
		closeUtilizationVolume = new float[getNSpecies() + 1][nUtilizationClasses];
		cuVolumeMinusDecay = new float[getNSpecies() + 1][nUtilizationClasses];
		cuVolumeMinusDecayWastage = new float[getNSpecies() + 1][nUtilizationClasses];
		loreyHeight = new float[getNSpecies() + 1][2];
		quadMeanDiameter = new float[getNSpecies() + 1][nUtilizationClasses];
		treesPerHectare = new float[getNSpecies() + 1][nUtilizationClasses];
		wholeStemVolume = new float[getNSpecies() + 1][nUtilizationClasses];
	}

	public PolygonProcessingState(PolygonProcessingState s) {
		this.genera = s.genera;
		this.nSpecies = s.getNSpecies();

		this.ageBreastHeight = copy(s.ageBreastHeight);
		this.ageTotal = copy(s.ageTotal);
		this.basalArea = copy(s.basalArea);
		this.closeUtilizationVolume = copy(s.closeUtilizationVolume);
		this.cuVolumeMinusDecay = copy(s.cuVolumeMinusDecay);
		this.cuVolumeMinusDecayWastage = copy(s.cuVolumeMinusDecayWastage);
		this.dominantHeight = copy(s.dominantHeight);
		this.loreyHeight = copy(s.loreyHeight);
		this.percentForestedLand = copy(s.percentForestedLand);
		this.quadMeanDiameter = copy(s.quadMeanDiameter);
		this.siteIndex = copy(s.siteIndex);
		this.siteCurveNumber = copy(s.siteCurveNumber);
		this.sp64Distribution = copy(s.sp64Distribution);
		this.speciesIndex = copy(s.speciesIndex);
		this.speciesName = copy(s.speciesName);
		this.treesPerHectare = copy(s.treesPerHectare);
		this.wholeStemVolume = copy(s.wholeStemVolume);
		this.yearsToBreastHeight = copy(s.yearsToBreastHeight);
	}

	public int getNSpecies() {
		return nSpecies;
	}

	public GenusDefinitionMap getGenera() {
		return genera;
	}

	/**
	 * Set this State to that of the given layer.
	 * 
	 * @param layer the layer in question
	 */
	public void set(VdypPolygonLayer layer) {

		if (layer.getDefaultUtilizationMap().isPresent()) {
			recordUtilizations(0, layer.getDefaultUtilizationMap().get());
		}

		for (var ge: layer.getGenus().entrySet()) {
			recordSpecies(ge.getKey(), ge.getValue());
		}
	}

	private void recordSpecies(GenusDefinition key, VdypLayerSpecies species) {

		int spIndex = genera.getIndex(key.getAlias());

		GenusDefinition genus = genera.get(key.getAlias());

		speciesName[spIndex] = genus.getName();
		sp64Distribution[spIndex] = species.getSpeciesDistributions();
		siteIndex[spIndex] = species.getSiteIndex();
		dominantHeight[spIndex] = species.getDominantHeight();
		ageTotal[spIndex] = species.getAgeTotal();
		ageBreastHeight[spIndex] = species.getAgeAtBreastHeight();
		yearsToBreastHeight[spIndex] = species.getYearsToBreastHeight();
		siteCurveNumber[spIndex] = species.getSiteCurveNumber();
		speciesIndex[spIndex] = species.getGenusIndex();
		// percentForestedLand is output-only and so not assigned here.

		if (species.getUtilizations().isPresent()) {
			recordUtilizations(spIndex, species.getUtilizations().get());
		}
	}

	private void recordUtilizations(int speciesIndex, Map<UtilizationClass, VdypSpeciesUtilization> m) {

		for (var su : m.entrySet()) {
			int ucIndex = su.getKey().ordinal();
			basalArea[speciesIndex][ucIndex] = su.getValue().getBasalArea();
			closeUtilizationVolume[speciesIndex][ucIndex] = su.getValue().getCloseUtilizationVolume();
			cuVolumeMinusDecay[speciesIndex][ucIndex] = su.getValue().getCuVolumeMinusDecay();
			cuVolumeMinusDecayWastage[speciesIndex][ucIndex] = su.getValue().getCuVolumeMinusDecayWastage();
			if (ucIndex < 2 /* only uc 0 and 1 have a lorey height */) {
				loreyHeight[speciesIndex][ucIndex] = su.getValue().getLoreyHeight();
			}
			quadMeanDiameter[speciesIndex][ucIndex] = su.getValue().getQuadraticMeanDiameterAtBH();
			treesPerHectare[speciesIndex][ucIndex] = su.getValue().getLiveTreesPerHectare();
			wholeStemVolume[speciesIndex][ucIndex] = su.getValue().getWholeStemVolume();
		}
	}

	public PolygonProcessingState copy() {

		return new PolygonProcessingState(this);
	}

	private SpeciesDistributionSet[] copy(SpeciesDistributionSet[] a) {
		SpeciesDistributionSet[] t = new SpeciesDistributionSet[a.length];

		for (int i = 0; i < a.length; i++)
			if (a[i] != null) {
				t[i] = a[i].copy();
			}

		return t;
	}

	private Optional<Integer>[] copy(Optional<Integer>[] a) {
		@SuppressWarnings("unchecked")
		Optional<Integer>[] t = new Optional[a.length];

		for (int i = 0; i < a.length; i++)
			t[i] = a[i];

		return t;
	}

	private String[] copy(String[] a) {
		String[] t = new String[a.length];

		for (int i = 0; i < a.length; i++)
			if (a[i] != null) {
				t[i] = new String(a[i]);
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
	 * @param i the index of the species to remove, i >= 1
	 * @param j the index of the species to replace it with, j >= i and j <= nSpecies
	 */
	private void replace(int i, int j) {
		if (i > j || i < 1 || j > getNSpecies()) {
			throw new IllegalArgumentException(
					MessageFormat.format("PolygonProcessingState.replace - illegal arguments i = {0}, j = {1}, nSpecies = {2}",
							i, j, getNSpecies()));
		}
		
		if (i < j) {
			
			speciesName[i] = speciesName[j];
			sp64Distribution[i] = sp64Distribution[j];
			siteIndex[i] = siteIndex[j];
			dominantHeight[i] = dominantHeight[j];
			ageTotal[i] = ageTotal[j];
			ageBreastHeight[i] = ageBreastHeight[j];
			yearsToBreastHeight[i] = yearsToBreastHeight[j];
			siteCurveNumber[i] = siteCurveNumber[j];
			speciesIndex[i] = speciesIndex[j];
			percentForestedLand[i] = percentForestedLand[j];
	
			basalArea[i] = basalArea[j];
			closeUtilizationVolume[i] = closeUtilizationVolume[j];
			cuVolumeMinusDecay[i] = cuVolumeMinusDecay[j];
			cuVolumeMinusDecayWastage[i] = cuVolumeMinusDecayWastage[j];
			loreyHeight[i] = loreyHeight[j];
			quadMeanDiameter[i] = quadMeanDiameter[j];
			treesPerHectare[i] = treesPerHectare[j];
			wholeStemVolume[i] = wholeStemVolume[j];

			genera.removeGenus(genera.getByIndex(i).getAlias());
		}
	}

	public void remove(Set<Integer> speciesToRemove) {
		
		int nRemoved = 0;
		for (int i = 1; i + nRemoved < (nSpecies + 1); i++) {
			
			if (speciesToRemove.contains(i)) {
				nRemoved += 1;
			}
		
			replace(i, i + nRemoved);
		}
		
		if (nRemoved > 0) {
			nSpecies = getNSpecies() - nRemoved;
			int nElements = nSpecies + 1;
			
			speciesName = Arrays.copyOf(speciesName, nElements);
			sp64Distribution = Arrays.copyOf(sp64Distribution, nElements);
			siteIndex = Arrays.copyOf(siteIndex, nElements);
			dominantHeight = Arrays.copyOf(dominantHeight, nElements);
			ageTotal = Arrays.copyOf(ageTotal, nElements);
			ageBreastHeight = Arrays.copyOf(ageBreastHeight, nElements);
			yearsToBreastHeight = Arrays.copyOf(yearsToBreastHeight, nElements);
			siteCurveNumber = Arrays.copyOf(siteCurveNumber, nElements);
			speciesIndex = Arrays.copyOf(speciesIndex, nElements);
			percentForestedLand = Arrays.copyOf(percentForestedLand, nElements);
	
			basalArea = Arrays.copyOf(basalArea, nElements);
			closeUtilizationVolume = Arrays.copyOf(closeUtilizationVolume, nElements);
			cuVolumeMinusDecay = Arrays.copyOf(cuVolumeMinusDecay, nElements);
			cuVolumeMinusDecayWastage = Arrays.copyOf(cuVolumeMinusDecayWastage, nElements);
			loreyHeight = Arrays.copyOf(loreyHeight, nElements);
			quadMeanDiameter = Arrays.copyOf(quadMeanDiameter, nElements);
			treesPerHectare = Arrays.copyOf(treesPerHectare, nElements);
			wholeStemVolume = Arrays.copyOf(wholeStemVolume, nElements);
		}
	}
}