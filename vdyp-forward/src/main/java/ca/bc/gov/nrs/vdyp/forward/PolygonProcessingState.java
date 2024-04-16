package ca.bc.gov.nrs.vdyp.forward;

import java.util.Map;
import java.util.Optional;

import ca.bc.gov.nrs.vdyp.common.GenusDefinitionMap;
import ca.bc.gov.nrs.vdyp.forward.model.VdypLayerSpecies;
import ca.bc.gov.nrs.vdyp.forward.model.VdypPolygonLayer;
import ca.bc.gov.nrs.vdyp.forward.model.VdypSpeciesUtilization;
import ca.bc.gov.nrs.vdyp.model.GenusDefinition;
import ca.bc.gov.nrs.vdyp.model.SpeciesDistributionSet;
import ca.bc.gov.nrs.vdyp.model.UtilizationClass;

class PolygonProcessingState {
	public int nSpecies; // BANK1 NSPB

	public String speciesName[/* species */]; // BANK2 SP0B
	public SpeciesDistributionSet sp64Distribution[/* species */]; // BANK2 SP64DISTB
	public float siteIndex[/* species */]; // BANK3 SIB
	public float dominantHeight[/* species */]; // BANK3 HDB
	public float ageTotal[/* species */]; // BANK3 AGETOTB
	public float ageBreastHeight[/* species */]; // BANK3 AGEBHB
	public float yearsToBreastHeight[/* species */]; // BANK3 YTBHB
	public Optional<Integer> siteCurveNumber[/* species */]; // BANK3 SCNB
	public int speciesIndex[/* species */]; // BANK1 ISPB
	public float percentForestedLand[/* species */]; // BANK1 PCTB

	public float basalArea[/* species, including 0 */][/* all ucs */]; // BANK1 BAB
	public float closeUtilizationVolume[/* species, including 0 */][/* all ucs */]; // BANK1 VOLCUB
	public float cuVolumeMinusDecay[/* species, including 0 */][/* all ucs */]; // BANK1 VOL_DB
	public float cuVolumeMinusDecayWastage[/* species, including 0 */][/* all ucs */]; // BANK1 VOL_DW_B
	public float loreyHeight[/* species, including 0 */][/* uc -1 and 0 only */]; // BANK1 HLB
	public float quadMeanDiameter[/* species, including 0 */][/* all ucs */]; // BANK1 DQB
	public float treesPerHectare[/* species, including 0 */][/* all ucs */]; // BANK1 TPHB
	public float wholeStemVolume[/* species, including 0 */][/* all ucs */]; // BANK1 VOLWSB

	private final GenusDefinitionMap genera;

	@SuppressWarnings("unchecked")
	public PolygonProcessingState(GenusDefinitionMap genera) {

		this.genera = genera;

		this.nSpecies = genera.getMaxIndex();

		// In the following, index 0 is unused
		speciesName = new String[nSpecies + 1];
		sp64Distribution = new SpeciesDistributionSet[nSpecies + 1];
		siteIndex = new float[nSpecies + 1];
		dominantHeight = new float[nSpecies + 1];
		ageTotal = new float[nSpecies + 1];
		ageBreastHeight = new float[nSpecies + 1];
		yearsToBreastHeight = new float[nSpecies + 1];
		siteCurveNumber = new Optional[nSpecies + 1];
		speciesIndex = new int[nSpecies + 1];
		percentForestedLand = new float[nSpecies + 1];

		int nUtilizationClasses = UtilizationClass.values().length;

		// In the following, index 0 is used for the default species utilization
		basalArea = new float[nSpecies + 1][nUtilizationClasses];
		closeUtilizationVolume = new float[nSpecies + 1][nUtilizationClasses];
		cuVolumeMinusDecay = new float[nSpecies + 1][nUtilizationClasses];
		cuVolumeMinusDecayWastage = new float[nSpecies + 1][nUtilizationClasses];
		loreyHeight = new float[nSpecies + 1][2];
		quadMeanDiameter = new float[nSpecies + 1][nUtilizationClasses];
		treesPerHectare = new float[nSpecies + 1][nUtilizationClasses];
		wholeStemVolume = new float[nSpecies + 1][nUtilizationClasses];
	}

	public PolygonProcessingState(PolygonProcessingState s) {
		this.genera = s.genera;
		this.nSpecies = s.nSpecies;

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

	public void set(VdypPolygonLayer layer) {

		if (layer.getDefaultUtilizationMap().isPresent()) {
			recordUtilizations(0, layer.getDefaultUtilizationMap().get());
		}

		for (var ge : layer.getGenus().entrySet()) {
			recordSpecies(ge.getKey(), ge.getValue());
		}
	}

	private void recordSpecies(GenusDefinition key, VdypLayerSpecies layer) {

		int spIndex = genera.getIndex(key.getAlias());
		GenusDefinition genus = genera.get(key.getAlias());

		speciesName[spIndex] = genus.getName();
		sp64Distribution[spIndex] = layer.getSpeciesDistributions();
		siteIndex[spIndex] = layer.getSiteIndex();
		dominantHeight[spIndex] = layer.getDominantHeight();
		ageTotal[spIndex] = layer.getAgeTotal();
		ageBreastHeight[spIndex] = layer.getAgeAtBreastHeight();
		yearsToBreastHeight[spIndex] = layer.getYearsToBreastHeight();
		siteCurveNumber[spIndex] = layer.getSiteCurveNumber();
		speciesIndex[spIndex] = layer.getGenusIndex();
		// percentForestedLand is output-only and so not assigned here.

		if (layer.getUtilizations().isPresent()) {
			recordUtilizations(spIndex, layer.getUtilizations().get());
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

		PolygonProcessingState s = new PolygonProcessingState(this);

		return s;
	}

	private SpeciesDistributionSet[] copy(SpeciesDistributionSet[] a) {
		SpeciesDistributionSet[] t = new SpeciesDistributionSet[a.length];

		for (int i = 0; i < a.length; i++)
			t[i] = a[i].copy();

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
			t[i] = new String(a[i]);

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
	 * @param j the index of the species to replace it with, j > i
	 */
	public void replace(int i, int j) {
		if (i >= j || i < 1) {
			throw new IllegalArgumentException(
					"PolygonProcessingState.replace - illegal arguments i = " + i + " j = " + j
			);
		}

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
	}
}