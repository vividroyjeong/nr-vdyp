package ca.bc.gov.nrs.vdyp.forward;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.vdyp.application.ProcessingException;
import ca.bc.gov.nrs.vdyp.forward.Bank.CopyMode;
import ca.bc.gov.nrs.vdyp.forward.model.ControlVariable;
import ca.bc.gov.nrs.vdyp.model.BecDefinition;
import ca.bc.gov.nrs.vdyp.model.LayerType;
import ca.bc.gov.nrs.vdyp.model.MatrixMap2;
import ca.bc.gov.nrs.vdyp.model.MatrixMap3;
import ca.bc.gov.nrs.vdyp.model.UtilizationClass;
import ca.bc.gov.nrs.vdyp.model.UtilizationClassVariable;
import ca.bc.gov.nrs.vdyp.model.VdypEntity;
import ca.bc.gov.nrs.vdyp.model.VdypLayer;
import ca.bc.gov.nrs.vdyp.model.VdypPolygon;
import ca.bc.gov.nrs.vdyp.model.VdypSpecies;
import ca.bc.gov.nrs.vdyp.model.VolumeVariable;

class LayerProcessingState {

	enum BankType {
		Start, End
	};

	private static final String COMPATIBILITY_VARIABLES_SET_CAN_BE_SET_ONCE_ONLY = "CompatibilityVariablesSet can be set once only";
	private static final String PRIMARY_SPECIES_DETAILS_CAN_BE_SET_ONCE_ONLY = "PrimarySpeciesDetails can be set once only";
	private static final String SITE_CURVE_NUMBERS_CAN_BE_SET_ONCE_ONLY = "SiteCurveNumbers can be set once only";
	private static final String SPECIES_RANKING_DETAILS_CAN_BE_SET_ONCE_ONLY = "SpeciesRankingDetails can be set once only";

	private static final String UNSET_PRIMARY_SPECIES_AGE_TO_BREAST_HEIGHT = "unset primarySpeciesAgeToBreastHeight";
	private static final String UNSET_PRIMARY_SPECIES_AGE_AT_BREAST_HEIGHT = "unset primarySpeciesAgeAtBreastHeight";
	private static final String UNSET_PRIMARY_SPECIES_DOMINANT_HEIGHT = "unset primarySpeciesDominantHeight";
	private static final String UNSET_CV_VOLUMES = "unset cvVolumes";
	private static final String UNSET_CV_BASAL_AREAS = "unset cvBasalAreas";
	private static final String UNSET_RANKING_DETAILS = "unset rankingDetails";
	private static final String UNSET_SITE_CURVE_NUMBERS = "unset siteCurveNumbers";
	private static final String UNSET_INVENTORY_TYPE_GROUP = "unset inventoryTypeGroup";

	private static final Logger logger = LoggerFactory.getLogger(LayerProcessingState.class);

	/** The containing ForwardProcessingState */
	private final ForwardProcessingState fps;

	/** The containing polygon of the layer on which the Processor is operating */
	private final VdypPolygon polygon;
	
	/** The type of Layer being processed */
	private final LayerType layerType;

	// L1COM1, L1COM4 and L1COM5 - these common blocks mirror BANK1, BANK2 and BANK3 and are initialized
	// when copied to "active" in ForwardProcessingEngine.
	
	/** 
	 * State of the layer at the start of processing; read-write during preparation for grow 
	 * and read-only after that.
	 */
	private Bank startBank;
	
	/** 
	 * State of the layer at the end of processing. The information comprising this is calculated
	 * during the execution of <code>grow()</code>.
	 */
	private Bank endBank;

	// L1COM2 - equation groups. From the configuration, narrowed to the
	// polygon's BEC zone.

	int[] volumeEquationGroups;
	int[] decayEquationGroups;
	int[] breakageEquationGroups;

	// L1COM3 - just shadows of fields of L1COM5
	// AGETOTL1 = wallet.ageTotals[primarySpeciesIndex]
	// AGEBHL1 = wallet.yearsAtBreastHeight[primarySpeciesIndex]
	// YTBHL1 = wallet.yearsToBreastHeight[primarySpeciesIndex]
	// HDL1 = wallet.dominantHeights[primarySpeciesIndex]

	// Calculated data - this data is calculated after construction during processing.

	// Ranking Details - encompasses INXXL1 and INXL1
	private boolean areRankingDetailsSet = false;

	// INXXL1
	private int primarySpeciesIndex; // IPOSP

	// INXL1
	// ISPP = wallet.speciesIndices[primarySpeciesIndex]
	// PCTP = wallet.percentagesOfForestedLand[primarySpeciesIndex]
	private Optional<Integer> secondarySpeciesIndex; // => ISPS (species name) and PCTS (percentage)
	private int inventoryTypeGroup; // ITG
	private int primarySpeciesGroupNumber; // GRPBA1
	private int primarySpeciesStratumNumber; // GRPBA3

	// Site Curve Numbers - encompasses INXSCV
	private boolean areSiteCurveNumbersSet = false;

	// INXSC
	private int[] siteCurveNumbers; // INXSCV

	// Primary Species Details - encompasses L1COM6
	private boolean arePrimarySpeciesDetailsSet = false;

	// L1COM6
	private float primarySpeciesDominantHeight; // HD
	private float primarySpeciesSiteIndex; // SI
	private float primarySpeciesTotalAge; // AGETOTP
	private float primarySpeciesAgeAtBreastHeight; // AGEBHP
	private float primarySpeciesAgeToBreastHeight; // YTBHP

	// Compatibility Variables - LCV1 & LCVS
	private boolean areCompatibilityVariablesSet = false;

	private MatrixMap3<UtilizationClass, VolumeVariable, LayerType, Float>[] cvVolume;
	private MatrixMap2<UtilizationClass, LayerType, Float>[] cvBasalArea;
	private MatrixMap2<UtilizationClass, LayerType, Float>[] cvQuadraticMeanDiameter;
	private Map<UtilizationClassVariable, Float>[] cvPrimaryLayerSmall;

	// FRBASP0 - FR
	// TODO

	// MNSP - MSPL1, MSPLV
	// TODO

	public LayerProcessingState(ForwardProcessingState fps, VdypPolygon polygon, LayerType subjectLayerType) throws ProcessingException {

		this.fps = fps;
		this.polygon = polygon;
		this.layerType = subjectLayerType;
		
		BecDefinition becZone = polygon.getBiogeoclimaticZone();
		
		this.startBank = new Bank(
				polygon.getLayers().get(subjectLayerType), becZone,
				s -> s.getBaseAreaByUtilization().get(UtilizationClass.ALL) >= ForwardProcessingEngine.MIN_BASAL_AREA);
		
		this.endBank = new Bank(this.startBank, CopyMode.CopyStructure);
		
		var volumeEquationGroupMatrix = this.fps.fcm.getVolumeEquationGroups();
		var decayEquationGroupMatrix = this.fps.fcm.getDecayEquationGroups();
		var breakageEquationGroupMatrix = this.fps.fcm.getBreakageEquationGroups();

		this.volumeEquationGroups = new int[this.startBank.getNSpecies() + 1];
		this.decayEquationGroups = new int[this.startBank.getNSpecies() + 1];
		this.breakageEquationGroups = new int[this.startBank.getNSpecies() + 1];

		this.volumeEquationGroups[0] = VdypEntity.MISSING_INTEGER_VALUE;
		this.decayEquationGroups[0] = VdypEntity.MISSING_INTEGER_VALUE;
		this.breakageEquationGroups[0] = VdypEntity.MISSING_INTEGER_VALUE;

		String becZoneAlias = this.getBecZone().getAlias();
		for (int i = 1; i < this.startBank.getNSpecies() + 1; i++) {
			String speciesName = this.startBank.speciesNames[i];
			this.volumeEquationGroups[i] = volumeEquationGroupMatrix.get(speciesName, becZoneAlias);
			// From VGRPFIND, volumeEquationGroup 10 is mapped to 11.
			if (this.volumeEquationGroups[i] == 10) {
				this.volumeEquationGroups[i] = 11;
			}
			this.decayEquationGroups[i] = decayEquationGroupMatrix.get(speciesName, becZoneAlias);
			this.breakageEquationGroups[i] = breakageEquationGroupMatrix.get(speciesName, becZoneAlias);
		}
	}
	
	/** 
	 * Set the layer processing state to the given Bank. This is normally done at the conclusion of
	 * the growth of a layer for one growth period; the Bank resulting from that calculation is
	 * assigned as the start bank for the next growth period.
	 */
	public void swapBanks() {
		this.startBank = endBank;
		this.endBank = new Bank(this.startBank, CopyMode.CopyStructure);
	}

	public VdypPolygon getPolygon() {
		return polygon;
	}
	
	public LayerType getLayerType() {
		return layerType;
	}

	public int getNSpecies() {
		return startBank.getNSpecies();
	}

	public int[] getIndices() {
		return startBank.getIndices();
	}

	public BecDefinition getBecZone() {
		return startBank.getBecZone();
	}

	public int getPrimarySpeciesIndex() {
		if (!areRankingDetailsSet) {
			throw new IllegalStateException("unset primarySpeciesIndex");
		}
		return primarySpeciesIndex;
	}
	
	public String getPrimarySpeciesAlias() {
		if (!areRankingDetailsSet) {
			throw new IllegalStateException("unset primarySpeciesIndex");
		}
		return startBank.speciesNames[primarySpeciesIndex];
	}

	public boolean hasSecondarySpeciesIndex() {
		return secondarySpeciesIndex.isPresent();
	}

	public int getSecondarySpeciesIndex() {
		return secondarySpeciesIndex.orElseThrow(() -> new IllegalStateException("unset secondarySpeciesIndex"));
	}

	public int getInventoryTypeGroup() {
		if (!areRankingDetailsSet) {
			throw new IllegalStateException(UNSET_INVENTORY_TYPE_GROUP);
		}
		return inventoryTypeGroup;
	}

	public static String getCompatibilityVariablesSetCanBeSetOnceOnly() {
		return COMPATIBILITY_VARIABLES_SET_CAN_BE_SET_ONCE_ONLY;
	}

	public static String getPrimarySpeciesDetailsCanBeSetOnceOnly() {
		return PRIMARY_SPECIES_DETAILS_CAN_BE_SET_ONCE_ONLY;
	}

	public static String getSiteCurveNumbersCanBeSetOnceOnly() {
		return SITE_CURVE_NUMBERS_CAN_BE_SET_ONCE_ONLY;
	}

	public static String getSpeciesRankingDetailsCanBeSetOnceOnly() {
		return SPECIES_RANKING_DETAILS_CAN_BE_SET_ONCE_ONLY;
	}

	public static String getUnsetPrimarySpeciesAgeToBreastHeight() {
		return UNSET_PRIMARY_SPECIES_AGE_TO_BREAST_HEIGHT;
	}

	public static String getUnsetPrimarySpeciesAgeAtBreastHeight() {
		return UNSET_PRIMARY_SPECIES_AGE_AT_BREAST_HEIGHT;
	}

	public static String getUnsetPrimarySpeciesDominantHeight() {
		return UNSET_PRIMARY_SPECIES_DOMINANT_HEIGHT;
	}

	public static String getUnsetCvVolumes() {
		return UNSET_CV_VOLUMES;
	}

	public static String getUnsetCvBasalAreas() {
		return UNSET_CV_BASAL_AREAS;
	}

	public static String getUnsetRankingDetails() {
		return UNSET_RANKING_DETAILS;
	}

	public static String getUnsetSiteCurveNumbers() {
		return UNSET_SITE_CURVE_NUMBERS;
	}

	public static String getUnsetInventoryTypeGroup() {
		return UNSET_INVENTORY_TYPE_GROUP;
	}

	public static Logger getLogger() {
		return logger;
	}

	public ForwardProcessingState getFps() {
		return fps;
	}

	public Bank getStartBank() {
		return startBank;
	}

	public Bank getEndBank() {
		return endBank;
	}

	public int[] getVolumeEquationGroups() {
		return volumeEquationGroups;
	}

	public int[] getDecayEquationGroups() {
		return decayEquationGroups;
	}

	public int[] getBreakageEquationGroups() {
		return breakageEquationGroups;
	}

	public boolean isAreRankingDetailsSet() {
		return areRankingDetailsSet;
	}

	public int getPrimarySpeciesGroupNumber() {
		return primarySpeciesGroupNumber;
	}

	public int getPrimarySpeciesStratumNumber() {
		return primarySpeciesStratumNumber;
	}

	public int[] getSiteCurveNumbers() {
		return siteCurveNumbers;
	}
	
	public MatrixMap3<UtilizationClass, VolumeVariable, LayerType, Float>[] getCvVolume() {
		return cvVolume;
	}

	public MatrixMap2<UtilizationClass, LayerType, Float>[] getCvBasalArea() {
		return cvBasalArea;
	}

	public MatrixMap2<UtilizationClass, LayerType, Float>[] getCvQuadraticMeanDiameter() {
		return cvQuadraticMeanDiameter;
	}

	public Map<UtilizationClassVariable, Float>[] getCvPrimaryLayerSmall() {
		return cvPrimaryLayerSmall;
	}

	/** 
	 * @param n index of species for whom the site curve number is to be returned.
	 * @return the site curve number of the given species.
	 */
	public int getSiteCurveNumber(int n) {
		if (!areSiteCurveNumbersSet) {
			throw new IllegalStateException(UNSET_SITE_CURVE_NUMBERS);
		}
		if (n == 0) {
			// Take this opportunity to initialize siteCurveNumbers[0] from that of the primary species.
			if (!areRankingDetailsSet) {
				throw new IllegalStateException(UNSET_RANKING_DETAILS);
			}
			siteCurveNumbers[0] = siteCurveNumbers[primarySpeciesIndex];
		}
		return siteCurveNumbers[n];
	}

	public float getPrimarySpeciesDominantHeight() {
		if (!arePrimarySpeciesDetailsSet) {
			throw new IllegalStateException(UNSET_PRIMARY_SPECIES_DOMINANT_HEIGHT);
		}
		return primarySpeciesDominantHeight;
	}

	public float getPrimarySpeciesSiteIndex() {
		if (!arePrimarySpeciesDetailsSet) {
			throw new IllegalStateException(UNSET_PRIMARY_SPECIES_DOMINANT_HEIGHT);
		}
		return primarySpeciesSiteIndex;
	}

	public float getPrimarySpeciesTotalAge() {
		if (!arePrimarySpeciesDetailsSet) {
			throw new IllegalStateException(UNSET_PRIMARY_SPECIES_DOMINANT_HEIGHT);
		}
		return primarySpeciesTotalAge;
	}

	public float getPrimarySpeciesAgeAtBreastHeight() {
		if (!arePrimarySpeciesDetailsSet) {
			throw new IllegalStateException(UNSET_PRIMARY_SPECIES_AGE_AT_BREAST_HEIGHT);
		}
		return primarySpeciesAgeAtBreastHeight;
	}

	public float getPrimarySpeciesAgeToBreastHeight() {
		if (!arePrimarySpeciesDetailsSet) {
			throw new IllegalStateException(UNSET_PRIMARY_SPECIES_AGE_TO_BREAST_HEIGHT);
		}
		return primarySpeciesAgeToBreastHeight;
	}

	public void setSpeciesRankingDetails(SpeciesRankingDetails rankingDetails) {
		if (this.areRankingDetailsSet) {
			throw new IllegalStateException(SPECIES_RANKING_DETAILS_CAN_BE_SET_ONCE_ONLY);
		}

		this.primarySpeciesIndex = rankingDetails.primarySpeciesIndex();
		this.secondarySpeciesIndex = rankingDetails.secondarySpeciesIndex();
		this.inventoryTypeGroup = rankingDetails.inventoryTypeGroup();
		this.primarySpeciesGroupNumber = rankingDetails.basalAreaGroup1();
		this.primarySpeciesStratumNumber = rankingDetails.basalAreaGroup3();

		this.areRankingDetailsSet = true;
	}

	public void setSiteCurveNumbers(int[] siteCurveNumbers) {
		if (this.areSiteCurveNumbersSet) {
			throw new IllegalStateException(SITE_CURVE_NUMBERS_CAN_BE_SET_ONCE_ONLY);
		}

		this.siteCurveNumbers = Arrays.copyOf(siteCurveNumbers, siteCurveNumbers.length);

		areSiteCurveNumbersSet = true;
	}

	public void setPrimarySpeciesDetails(PrimarySpeciesDetails details) {
		
		// Normally, these values may only be set only once. However, during grow(), if the 
		// control variable UPDATE_DURING_GROWTH_6 has value "1" then updates are allowed.
		if (this.arePrimarySpeciesDetailsSet && fps.fcm.getForwardControlVariables().getControlVariable(ControlVariable.UPDATE_DURING_GROWTH_6) != 1) {
			throw new IllegalStateException(PRIMARY_SPECIES_DETAILS_CAN_BE_SET_ONCE_ONLY);
		}

		this.primarySpeciesDominantHeight = details.primarySpeciesDominantHeight();
		this.primarySpeciesSiteIndex = details.primarySpeciesSiteIndex();
		this.primarySpeciesTotalAge = details.primarySpeciesTotalAge();
		this.primarySpeciesAgeAtBreastHeight = details.primarySpeciesAgeAtBreastHeight();
		this.primarySpeciesAgeToBreastHeight = details.primarySpeciesAgeToBreastHeight();

		// Store these values into start - VHDOM1 lines 182 - 186
		if (startBank.dominantHeights[primarySpeciesIndex] <= 0.0) {
			startBank.dominantHeights[primarySpeciesIndex] = this.primarySpeciesDominantHeight;
		}
		if (startBank.siteIndices[primarySpeciesIndex] <= 0.0) {
			startBank.siteIndices[primarySpeciesIndex] = this.primarySpeciesSiteIndex;
		}
		if (startBank.ageTotals[primarySpeciesIndex] <= 0.0) {
			startBank.ageTotals[primarySpeciesIndex] = this.primarySpeciesTotalAge;
		}
		if (startBank.yearsAtBreastHeight[primarySpeciesIndex] <= 0.0) {
			startBank.yearsAtBreastHeight[primarySpeciesIndex] = this.primarySpeciesAgeAtBreastHeight;
		}
		if (startBank.yearsAtBreastHeight[primarySpeciesIndex] <= 0.0) {
			startBank.yearsAtBreastHeight[primarySpeciesIndex] = this.primarySpeciesAgeToBreastHeight;
		}

		this.arePrimarySpeciesDetailsSet = true;
	}
	
	public void updatePrimarySpeciesDetailsAfterGrowth(float newPrimarySpeciesDominantHeight) {
		
		this.primarySpeciesDominantHeight = newPrimarySpeciesDominantHeight;
		this.primarySpeciesTotalAge += 1;
		this.primarySpeciesAgeAtBreastHeight += 1;
		
		// primarySpeciesSiteIndex - does this change?
		// primarySpeciesAgeToBreastHeight of course doesn't change.
	}

	public void setCompatibilityVariableDetails(
			MatrixMap3<UtilizationClass, VolumeVariable, LayerType, Float>[] cvVolume,
			MatrixMap2<UtilizationClass, LayerType, Float>[] cvBasalArea,
			MatrixMap2<UtilizationClass, LayerType, Float>[] cvQuadraticMeanDiameter,
			Map<UtilizationClassVariable, Float>[] cvPrimaryLayerSmall
	) {
		if (this.areCompatibilityVariablesSet) {
			throw new IllegalStateException(COMPATIBILITY_VARIABLES_SET_CAN_BE_SET_ONCE_ONLY);
		}

		this.cvVolume = cvVolume;
		this.cvBasalArea = cvBasalArea;
		this.cvQuadraticMeanDiameter = cvQuadraticMeanDiameter;
		this.cvPrimaryLayerSmall = cvPrimaryLayerSmall;

		this.areCompatibilityVariablesSet = true;
	}

	/**
	 * CVADJ1 - adjust the values of the compatibility variables after one year of growth.
	 */
	public void updateCompatibilityVariablesAfterGrowth() {
		
		var compVarAdjustments = fps.fcm.getCompVarAdjustments();
		
		for (int i: getIndices()) {
			for (UtilizationClassVariable sucv: UtilizationClassVariable.values()) {
				cvPrimaryLayerSmall[i].put(sucv, cvPrimaryLayerSmall[i].get(sucv) * compVarAdjustments.getValue(UtilizationClass.SMALL, sucv));
			}
			for (UtilizationClass uc: UtilizationClass.UTIL_CLASSES) {
				cvBasalArea[i].put(uc, LayerType.PRIMARY, cvBasalArea[i].get(uc, LayerType.PRIMARY) 
						* compVarAdjustments.getValue(uc, UtilizationClassVariable.BASAL_AREA));
				cvQuadraticMeanDiameter[i].put(uc, LayerType.PRIMARY, cvQuadraticMeanDiameter[i].get(uc, LayerType.PRIMARY)
						* compVarAdjustments.getValue(uc, UtilizationClassVariable.QUAD_MEAN_DIAMETER));
				
				for (VolumeVariable vv: VolumeVariable.ALL) {
					cvVolume[i].put(uc, vv, LayerType.PRIMARY, cvVolume[i].get(uc, vv, LayerType.PRIMARY)
							* compVarAdjustments.getVolumeValue(uc, vv));
				}
			}
		}
	}

	public float
			getCVVolume(int speciesIndex, UtilizationClass uc, VolumeVariable volumeVariable, LayerType layerType) {
		if (!this.areCompatibilityVariablesSet) {
			throw new IllegalStateException(UNSET_CV_VOLUMES);
		}

		return cvVolume[speciesIndex].get(uc, volumeVariable, layerType);
	}

	public float getCVBasalArea(int speciesIndex, UtilizationClass uc, LayerType layerType) {
		if (!this.areCompatibilityVariablesSet) {
			throw new IllegalStateException(UNSET_CV_BASAL_AREAS);
		}

		return cvBasalArea[speciesIndex].get(uc, layerType);
	}

	public float getCVQuadraticMeanDiameter(int speciesIndex, UtilizationClass uc, LayerType layerType) {
		if (!this.areCompatibilityVariablesSet) {
			throw new IllegalStateException(UNSET_CV_BASAL_AREAS);
		}

		return cvQuadraticMeanDiameter[speciesIndex].get(uc, layerType);
	}

	public float getCVSmall(int speciesIndex, UtilizationClassVariable variable) {
		if (!this.areCompatibilityVariablesSet) {
			throw new IllegalStateException(UNSET_CV_BASAL_AREAS);
		}

		return cvPrimaryLayerSmall[speciesIndex].get(variable);
	}

	public VdypLayer getLayerFromBank(BankType bankType) {
		
		Bank sourceBank = bankType == BankType.Start ? startBank : endBank;
		
		VdypLayer updatedLayer = sourceBank.getUpdatedLayer();
		
		for (int i = 1; i < getNSpecies() + 1; i++) {
			VdypSpecies species = updatedLayer.getSpeciesBySp0(sourceBank.speciesNames[i]);
			species.setCompatibilityVariables(
					cvVolume[i], cvBasalArea[i], cvQuadraticMeanDiameter[i], cvPrimaryLayerSmall[i]);
		}
		
		return updatedLayer;
	}
}