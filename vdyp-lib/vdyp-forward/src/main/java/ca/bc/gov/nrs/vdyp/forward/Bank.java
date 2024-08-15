package ca.bc.gov.nrs.vdyp.forward;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.vdyp.application.ProcessingException;
import ca.bc.gov.nrs.vdyp.model.BecDefinition;
import ca.bc.gov.nrs.vdyp.model.Sp64DistributionSet;
import ca.bc.gov.nrs.vdyp.model.UtilizationClass;
import ca.bc.gov.nrs.vdyp.model.VdypEntity;
import ca.bc.gov.nrs.vdyp.model.VdypLayer;
import ca.bc.gov.nrs.vdyp.model.VdypSite;
import ca.bc.gov.nrs.vdyp.model.VdypSpecies;
import ca.bc.gov.nrs.vdyp.model.VdypUtilizationHolder;

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
	public final Sp64DistributionSet[/* nSpecies + 1 */] sp64Distributions; // BANK2 SP64DISTB
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

	public Bank(VdypLayer layer, BecDefinition becZone, Predicate<VdypSpecies> retainCriteria) throws ProcessingException {

		this.layer = layer;
		this.becZone = becZone;
		
		List<VdypSpecies> speciesToRetain = layer.getSpecies().values().stream().filter(s -> retainCriteria.test(s))
				.sorted((s1, s2) -> s1.getGenusIndex() - s2.getGenusIndex()).toList();

		this.nSpecies = speciesToRetain.size();
		this.indices = IntStream.range(1, nSpecies + 1).toArray();

		// In the following, index 0 is unused
		speciesNames = new String[nSpecies + 1];
		sp64Distributions = new Sp64DistributionSet[getNSpecies() + 1];
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

		transferUtilizationsIntoBank(0, layer);

		int nextSlot = 1;
		for (VdypSpecies s : speciesToRetain) {
			transferSpeciesIntoBank(nextSlot++, s);
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

	int[] getIndices() {
		return indices;
	}

	BecDefinition getBecZone() {
		return becZone;
	}
	
	/**
	 * This method copies the Bank contents out to the VdypLayer instance used to create it
	 * and returns that. It is a relatively expensive operation and should not be called 
	 * without due consideration.
	 * 
	 * @return as described
	 */
	VdypLayer getUpdatedLayer() {
		transferLayerFromBank();
		return layer;
	}

	private void transferSpeciesIntoBank(int index, VdypSpecies species) throws ProcessingException {

		VdypSite site = species.getSite().orElseThrow(() -> new ProcessingException(MessageFormat.format(
				"Species {0} of Polygon {1} must contain a Site definition but does not.", 
				species.getGenus(), species.getPolygonIdentifier().toStringCompact())));
		
		speciesNames[index] = species.getGenus();
		sp64Distributions[index] = species.getSp64DistributionSet();
		siteIndices[index] = site.getSiteIndex().orElse(VdypEntity.MISSING_FLOAT_VALUE);
		dominantHeights[index] = site.getHeight().orElse(VdypEntity.MISSING_FLOAT_VALUE);
		ageTotals[index] = site.getAgeTotal().orElse(VdypEntity.MISSING_FLOAT_VALUE);
		yearsToBreastHeight[index] = site.getYearsToBreastHeight().orElse(VdypEntity.MISSING_FLOAT_VALUE);
		if (ageTotals[index] != VdypEntity.MISSING_FLOAT_VALUE && yearsToBreastHeight[index] != VdypEntity.MISSING_FLOAT_VALUE) {
			yearsAtBreastHeight[index] = ageTotals[index] - yearsToBreastHeight[index];
		} else {
			yearsAtBreastHeight[index] = VdypEntity.MISSING_FLOAT_VALUE;
		}
		siteCurveNumbers[index] = site.getSiteCurveNumber().orElse(VdypEntity.MISSING_INTEGER_VALUE);
		speciesIndices[index] = species.getGenusIndex();
		// percentForestedLand is output-only and so not assigned here.

		transferUtilizationsIntoBank(index, species);
	}

	private void transferUtilizationsIntoBank(int index, VdypUtilizationHolder uh) {

		for (UtilizationClass uc: UtilizationClass.values()) {
			int ucIndex = uc.ordinal();
			basalAreas[index][ucIndex] = uh.getBaseAreaByUtilization().get(uc);
			closeUtilizationVolumes[index][ucIndex] = uh.getCloseUtilizationVolumeByUtilization().get(uc);
			cuVolumesMinusDecay[index][ucIndex] = uh.getCloseUtilizationVolumeNetOfDecayByUtilization().get(uc);
			cuVolumesMinusDecayAndWastage[index][ucIndex] = uh.getCloseUtilizationVolumeNetOfDecayAndWasteByUtilization().get(uc);
			if (ucIndex < 2 /* only uc 0 and 1 have a lorey height */) {
				loreyHeights[index][ucIndex] = uh.getLoreyHeightByUtilization().get(uc);
			}
			quadMeanDiameters[index][ucIndex] = uh.getQuadraticMeanDiameterByUtilization().get(uc);
			treesPerHectare[index][ucIndex] = uh.getTreesPerHectareByUtilization().get(uc);
			wholeStemVolumes[index][ucIndex] = uh.getWholeStemVolumeByUtilization().get(uc);
		}
	}

	private void transferDefaultUtilizationsIntoBank(int index) {

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

	private void transferLayerFromBank() {

		transferUtilizationsFromBank(0, layer);
		
		for (int i: indices) {
			transferSpeciesFromBank(i, layer.getSpecies().get(speciesNames[i]));
		}
	}

	private void transferSpeciesFromBank(int index, VdypSpecies species) {

		VdypSite site = species.getSite().get();
		
		VdypSite updatedSite = new VdypSite.Builder()
				.adapt(site)
				.siteIndex(siteIndices[index])
				.ageTotal(ageTotals[index])
				.height(dominantHeights[index])
				.yearsToBreastHeight(yearsToBreastHeight[index])
				.build();
		
		VdypSpecies speciesBuilder = new VdypSpecies.Builder()
				.addSite(updatedSite)
				.adapt(species).build();

		transferUtilizationsIntoBank(index, species);
	}

	private void transferUtilizationsFromBank(int index, VdypUtilizationHolder uh) {

		for (UtilizationClass uc: UtilizationClass.values()) {
			int ucIndex = uc.ordinal();
			uh.getBaseAreaByUtilization().set(uc, basalAreas[index][ucIndex]);
			uh.getCloseUtilizationVolumeByUtilization().set(uc, closeUtilizationVolumes[index][ucIndex]);
			uh.getCloseUtilizationVolumeNetOfDecayByUtilization().set(uc, cuVolumesMinusDecay[index][ucIndex]);
			uh.getCloseUtilizationVolumeNetOfDecayAndWasteByUtilization().set(uc, cuVolumesMinusDecayAndWastage[index][ucIndex]);
			if (ucIndex < 2 /* only uc 0 and 1 have a lorey height */) {
				uh.getLoreyHeightByUtilization().set(uc, loreyHeights[index][ucIndex]);
			}
			uh.getQuadraticMeanDiameterByUtilization().set(uc, quadMeanDiameters[index][ucIndex]);
			uh.getTreesPerHectareByUtilization().set(uc, treesPerHectare[index][ucIndex]);
			uh.getWholeStemVolumeByUtilization().set(uc, wholeStemVolumes[index][ucIndex]);
		}
	}

	public Bank copy() {
		return new Bank(this, CopyMode.CopyAll);
	
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

	private Sp64DistributionSet[] copy(Sp64DistributionSet[] sp64Distributions) {
		return Arrays.stream(sp64Distributions).map(Sp64DistributionSet::copy).toArray(Sp64DistributionSet[]::new);
	}
}