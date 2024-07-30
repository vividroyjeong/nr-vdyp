package ca.bc.gov.nrs.vdyp.forward;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.vdyp.model.BecDefinition;
import ca.bc.gov.nrs.vdyp.model.GenusDistributionSet;
import ca.bc.gov.nrs.vdyp.model.UtilizationClass;
import ca.bc.gov.nrs.vdyp.model.VdypLayer;
import ca.bc.gov.nrs.vdyp.model.VdypSpecies;
import ca.bc.gov.nrs.vdyp.model.VdypUtilization;

class Bank {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(Bank.class);

	private final VdypLayer layer;
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

	public final String[/* nSpecies + 1 */] speciesNames; // BANK2 SP0B
	public final GenusDistributionSet[/* nSpecies + 1 */] sp64Distributions; // BANK2 SP64DISTB
	public final float[/* nSpecies + 1 */] siteIndices; // BANK3 SIB
	public final float[/* nSpecies + 1 */] dominantHeights; // BANK3 HDB
	public final float[/* nSpecies + 1 */] ageTotals; // BANK3 AGETOTB
	public final float[/* nSpecies + 1 */] yearsAtBreastHeight; // BANK3 AGEBHB
	public final float[/* nSpecies + 1 */] yearsToBreastHeight; // BANK3 YTBHB
	public final int[/* nSpecies + 1 */] siteCurveNumbers; // BANK3 SCNB
	public final int[/* nSpecies + 1 */] speciesIndices; // BANK1 ISPB
	public final float[/* nSpecies + 1 */] percentagesOfForestedLand; // BANK1 PCTB

	// Utilization information, per Species

	public final float[/* nSpecies + 1, including 0 */][/* all ucs */] basalAreas; // BANK1 BAB. Units: m^2/hectare
	public final float[/* nSpecies + 1, including 0 */][/* all ucs */] closeUtilizationVolumes; // BANK1 VOLCUB
	public final float[/* nSpecies + 1, including 0 */][/* all ucs */] cuVolumesMinusDecay; // BANK1 VOL_DB
	public final float[/* nSpecies + 1, including 0 */][/* all ucs */] cuVolumesMinusDecayAndWastage; // BANK1 VOL_DW_B
	public final float[/* nSpecies + 1, including 0 */][/* uc -1 and 0 only */] loreyHeights; // BANK1 HLB
	public final float[/* nSpecies + 1, including 0 */][/* all ucs */] quadMeanDiameters; // BANK1 DQB
	public final float[/* nSpecies + 1, including 0 */][/* all ucs */] treesPerHectare; // BANK1 TPHB
	public final float[/* nSpecies + 1, including 0 */][/* all ucs */] wholeStemVolumes; // BANK1 VOLWSB

	public Bank(VdypLayer layer, BecDefinition becZone, Predicate<VdypSpecies> retainCriteria) {

		this.layer = layer;
		this.becZone = becZone;
		
		this.indices = IntStream.range(1, nSpecies + 1).toArray();

		List<VdypSpecies> speciesToRetain = new ArrayList<>();

		for (VdypSpecies s : layer.getSpecies().values()) {
			if (retainCriteria.test(s)) {
				speciesToRetain.add(s);
			}
		}
				
		speciesToRetain.sort((o1, o2) -> o1.getGenusIndex().compareTo(o2.getGenusIndex()));

		this.nSpecies = speciesToRetain.size();
		this.indices = IntStream.range(1, nSpecies + 1).toArray();

		// In the following, index 0 is unused
		speciesNames = new String[nSpecies + 1];
		sp64Distributions = new GenusDistributionSet[nSpecies + 1];
		siteIndices = new float[nSpecies + 1];
		dominantHeights = new float[nSpecies + 1];
		ageTotals = new float[nSpecies + 1];
		yearsAtBreastHeight = new float[nSpecies + 1];
		yearsToBreastHeight = new float[nSpecies + 1];
		siteCurveNumbers = new int[nSpecies + 1];
		speciesIndices = new int[nSpecies + 1];
		percentagesOfForestedLand = new float[nSpecies + 1];

		int nUtilizationClasses = UtilizationClass.values().length;

		// In the following, index 0 is used for the default species utilization
		basalAreas = new float[nSpecies + 1][nUtilizationClasses];
		closeUtilizationVolumes = new float[nSpecies + 1][nUtilizationClasses];
		cuVolumesMinusDecay = new float[nSpecies + 1][nUtilizationClasses];
		cuVolumesMinusDecayAndWastage = new float[nSpecies + 1][nUtilizationClasses];
		loreyHeights = new float[nSpecies + 1][2];
		quadMeanDiameters = new float[nSpecies + 1][nUtilizationClasses];
		treesPerHectare = new float[nSpecies + 1][nUtilizationClasses];
		wholeStemVolumes = new float[nSpecies + 1][nUtilizationClasses];

		if (layer.getDefaultUtilizationMap().isPresent()) {
			recordUtilizations(0, layer.getDefaultUtilizationMap().get());
		}

		int nextSlot = 1;
		for (VdypSpecies s : speciesToRetain) {
			recordSpecies(nextSlot++, s);
		}
	}

	enum CopyMode { CopyAll, CopyStructure };
	
	public Bank(Bank s, CopyMode copyMode) {

		this.becZone = s.becZone;
		this.layer = s.layer;

		this.nSpecies = s.nSpecies;
		this.indices = copy(s.indices);
		this.speciesNames = copy(s.speciesNames);
		this.speciesIndices = copy(s.speciesIndices);

		this.siteCurveNumbers = copy(s.siteCurveNumbers);
		this.sp64Distributions = copy(s.sp64Distributions);

		if (copyMode == CopyMode.CopyAll) {
			this.ageTotals = copy(s.ageTotals);
			this.dominantHeights = copy(s.dominantHeights);
			this.percentagesOfForestedLand = copy(s.percentagesOfForestedLand);
			this.siteIndices = copy(s.siteIndices);
			this.yearsAtBreastHeight = copy(s.yearsAtBreastHeight);
			this.yearsToBreastHeight = copy(s.yearsToBreastHeight);
			
			this.basalAreas = copy(s.basalAreas);
			this.closeUtilizationVolumes = copy(s.closeUtilizationVolumes);
			this.cuVolumesMinusDecay = copy(s.cuVolumesMinusDecay);
			this.cuVolumesMinusDecayAndWastage = copy(s.cuVolumesMinusDecayAndWastage);
			this.loreyHeights = copy(s.loreyHeights);
			this.quadMeanDiameters = copy(s.quadMeanDiameters);
			this.treesPerHectare = copy(s.treesPerHectare);
			this.wholeStemVolumes = copy(s.wholeStemVolumes);
		} else {
			this.ageTotals = buildShell(nSpecies);
			this.dominantHeights = buildShell(nSpecies);
			this.percentagesOfForestedLand = buildShell(nSpecies);
			this.siteIndices = buildShell(nSpecies);
			this.yearsAtBreastHeight = buildShell(nSpecies);
			this.yearsToBreastHeight = buildShell(nSpecies);
			
			int nUtilizationClasses = UtilizationClass.values().length;
			this.basalAreas = buildShell(nSpecies, nUtilizationClasses);
			this.closeUtilizationVolumes = buildShell(nSpecies, nUtilizationClasses);
			this.cuVolumesMinusDecay = buildShell(nSpecies, nUtilizationClasses);
			this.cuVolumesMinusDecayAndWastage = buildShell(nSpecies, nUtilizationClasses);
			this.loreyHeights = buildShell(nSpecies, nUtilizationClasses);
			this.quadMeanDiameters = buildShell(nSpecies, nUtilizationClasses);
			this.treesPerHectare = buildShell(nSpecies, nUtilizationClasses);
			this.wholeStemVolumes = buildShell(nSpecies, nUtilizationClasses);
		}
	}
	
	private float[] buildShell(int n) {
		
		float[] result = new float[nSpecies + 1];
		Arrays.fill(result, Float.NaN);
		return result;
	}

	private float[][] buildShell(int n, int m) {
		
		float[][] result = new float[nSpecies + 1][];
		
		for (int i = 0; i < n; i++) {
			float[] row = new float[m];
			Arrays.fill(row, Float.NaN);
			result[i] = row;
		}
		
		return result;
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

	public VdypLayer getLayer() {
		return layer;
	}

	private void recordSpecies(int index, VdypSpecies species) {

		speciesNames[index] = species.getGenus();
		sp64Distributions[index] = species.getSpeciesPercent();
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

	private void recordUtilizations(int index, Map<UtilizationClass, VdypUtilization> suMap) {

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
			treesPerHectare[index][ucIndex] = su.getValue().getLiveTreesPerHectare();
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
			treesPerHectare[index][ucIndex] = Float.NaN;
			wholeStemVolumes[index][ucIndex] = Float.NaN;
		}
	}

	public Bank copy() {
		return new Bank(this, CopyMode.CopyAll);
	}

	private GenusDistributionSet[] copy(GenusDistributionSet[] a) {
		return Arrays.stream(a).map(g -> g == null ? null : g.copy()).toArray(GenusDistributionSet[]::new);
	}

	private String[] copy(String[] a) {
		return Arrays.stream(a).toArray(String[]::new);
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
		return Arrays.stream(a).map(float[]::clone).toArray(float[][]::new);
	}
}