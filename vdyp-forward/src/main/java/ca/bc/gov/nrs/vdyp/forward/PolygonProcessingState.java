package ca.bc.gov.nrs.vdyp.forward;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.common.Utils;
import ca.bc.gov.nrs.vdyp.forward.model.VdypEntity;
import ca.bc.gov.nrs.vdyp.forward.model.VdypGrowthDetails;
import ca.bc.gov.nrs.vdyp.forward.model.VdypPolygonLayer;
import ca.bc.gov.nrs.vdyp.io.parse.coe.ModifierParser;
import ca.bc.gov.nrs.vdyp.model.BecDefinition;
import ca.bc.gov.nrs.vdyp.model.Coefficients;
import ca.bc.gov.nrs.vdyp.model.LayerType;
import ca.bc.gov.nrs.vdyp.model.MatrixMap2;
import ca.bc.gov.nrs.vdyp.model.MatrixMap3;
import ca.bc.gov.nrs.vdyp.model.Region;
import ca.bc.gov.nrs.vdyp.model.UtilizationClass;
import ca.bc.gov.nrs.vdyp.model.VolumeVariable;

class PolygonProcessingState {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(PolygonProcessingState.class);

	// L1COM1, L1COM4 and L1COM5 - these common blocks mirror BANK1, BANK2 and BANK3 and are initialized 
	// when copied to "active" in ForwardProcessingEngine.
	Bank wallet;

	// L1COM3 - just shadows of fields of L1COM5
	//     AGETOTL1 = wallet.ageTotals[primarySpeciesIndex]
	//     AGEBHL1 = wallet.yearsAtBreastHeight[primarySpeciesIndex]
	//     YTBHL1 = wallet.yearsToBreastHeight[primarySpeciesIndex]
	//     HDL1 = wallet.dominantHeights[primarySpeciesIndex]

	// L1COM2 - equation groups. From the configuration, narrowed to the 
	// polygon's BEC zone.

	int[] volumeEquationGroups;
	int[] decayEquationGroups;
	int[] breakageEquationGroups;

	// Cached values from the controlMap

	private VdypGrowthDetails vdypGrowthDetails;
	private Map<String, Coefficients> netDecayWasteCoeMap;
	private MatrixMap2<Integer, Integer, Optional<Coefficients>> netDecayCoeMap;
	private MatrixMap2<String, Region, Float> wasteModifierMap;
	private MatrixMap2<String, Region, Float> decayModifierMap;
	private MatrixMap2<Integer, Integer, Optional<Coefficients>> closeUtilizationCoeMap;
	private Map<Integer, Coefficients> totalStandWholeStepVolumeCoeMap;
	private MatrixMap2<Integer, Integer, Optional<Coefficients>> wholeStemUtilizationComponentMap;

	// Calculated data - this data is calculated after construction during processing.

	// Ranking Details - encompasses INXXL1 and INXL1
	private boolean areRankingDetailsSet = false;

	// INXXL1
	private int primarySpeciesIndex; // IPOSP

	// INXL1
	//     ISPP = wallet.speciesIndices[primarySpeciesIndex]
	//     PCTP = wallet.percentagesOfForestedLand[primarySpeciesIndex]
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
	private Map<VolumeVariable, Float>[] cvSmall;

	// FRBASP0 - FR
	// TODO

	// MNSP - MSPL1, MSPLV
	// TODO

	public PolygonProcessingState(Bank bank, Map<String, Object> controlMap) {
		this.wallet = bank.copy();

		var volumeEquationGroupMatrix = Utils.<MatrixMap2<String, String, Integer>>expectParsedControl(
				controlMap, ControlKey.VOLUME_EQN_GROUPS, MatrixMap2.class
		);
		var decayEquationGroupMatrix = Utils.<MatrixMap2<String, String, Integer>>expectParsedControl(
				controlMap, ControlKey.DECAY_GROUPS, MatrixMap2.class
		);
		var breakageEquationGroupMatrix = Utils.<MatrixMap2<String, String, Integer>>expectParsedControl(
				controlMap, ControlKey.BREAKAGE_GROUPS, MatrixMap2.class
		);

		this.netDecayWasteCoeMap = Utils.<Map<String, Coefficients>>expectParsedControl(
				controlMap, ControlKey.VOLUME_NET_DECAY_WASTE, Map.class
		);
		this.netDecayCoeMap = Utils.<MatrixMap2<Integer, Integer, Optional<Coefficients>>>expectParsedControl(
				controlMap, ControlKey.VOLUME_NET_DECAY, MatrixMap2.class
		);
		this.wasteModifierMap = Utils.<MatrixMap2<String, Region, Float>>expectParsedControl(
				controlMap, ControlKey.WASTE_MODIFIERS, MatrixMap2.class
		);
		this.decayModifierMap = Utils.<MatrixMap2<String, Region, Float>>expectParsedControl(
				controlMap, ModifierParser.CONTROL_KEY_MOD301_DECAY, MatrixMap2.class
		);
		this.closeUtilizationCoeMap = Utils
				.<MatrixMap2<Integer, Integer, Optional<Coefficients>>>expectParsedControl(
						controlMap, ControlKey.CLOSE_UTIL_VOLUME, MatrixMap2.class
		);
		this.vdypGrowthDetails = Utils
				.expectParsedControl(controlMap, ControlKey.VTROL, VdypGrowthDetails.class
		);
		this.totalStandWholeStepVolumeCoeMap = Utils
				.<Map<Integer, Coefficients>>expectParsedControl(
						controlMap, ControlKey.TOTAL_STAND_WHOLE_STEM_VOL, Map.class
		);
		this.wholeStemUtilizationComponentMap = Utils
				.<MatrixMap2<Integer, Integer, Optional<Coefficients>>>expectParsedControl(
						controlMap, ControlKey.UTIL_COMP_WS_VOLUME, MatrixMap2.class
				);

		this.volumeEquationGroups = new int[this.wallet.getNSpecies() + 1];
		this.decayEquationGroups = new int[this.wallet.getNSpecies() + 1];
		this.breakageEquationGroups = new int[this.wallet.getNSpecies() + 1];

		this.volumeEquationGroups[0] = VdypEntity.MISSING_INTEGER_VALUE;
		this.decayEquationGroups[0] = VdypEntity.MISSING_INTEGER_VALUE;
		this.breakageEquationGroups[0] = VdypEntity.MISSING_INTEGER_VALUE;

		String becZoneAlias = this.getBecZone().getAlias();
		for (int i = 1; i < this.wallet.getNSpecies() + 1; i++) {
			String speciesName = this.wallet.speciesNames[i];
			this.volumeEquationGroups[i] = volumeEquationGroupMatrix.get(speciesName, becZoneAlias);
			// From VGRPFIND, volumeEquationGroup 10 is mapped to 11.
			if (this.volumeEquationGroups[i] == 10) {
				this.volumeEquationGroups[i] = 11;
			}
			this.decayEquationGroups[i] = decayEquationGroupMatrix.get(speciesName, becZoneAlias);
			this.breakageEquationGroups[i] = breakageEquationGroupMatrix.get(speciesName, becZoneAlias);
		}
	}

	public int getNSpecies() {
		return wallet.getNSpecies();
	}

	public int[] getIndices() {
		return wallet.getIndices();
	}

	public BecDefinition getBecZone() {
		return wallet.getBecZone();
	}

	public VdypPolygonLayer getLayer() {
		return wallet.getLayer();
	}

	public VdypGrowthDetails getVdypGrowthDetails() {
		return vdypGrowthDetails;
	}

	public MatrixMap2<Integer, Integer, Optional<Coefficients>> getNetDecayCoeMap() {
		return netDecayCoeMap;
	}

	public Map<String, Coefficients> getNetDecayWasteCoeMap() {
		return netDecayWasteCoeMap;
	}

	public MatrixMap2<String, Region, Float> getWasteModifierMap() {
		return wasteModifierMap;
	}

	public MatrixMap2<String, Region, Float> getDecayModifierMap() {
		return decayModifierMap;
	}

	public MatrixMap2<Integer, Integer, Optional<Coefficients>> getCloseUtilizationCoeMap() {
		return this.closeUtilizationCoeMap;
	}

	public Map<Integer, Coefficients> getTotalStandWholeStepVolumeCoeMap() {
		return totalStandWholeStepVolumeCoeMap;
	}
	
	public MatrixMap2<Integer, Integer, Optional<Coefficients>> getWholeStemUtilizationComponentMap() {
		return wholeStemUtilizationComponentMap;
	}

	public int getPrimarySpeciesIndex() {
		if (!areRankingDetailsSet) {
			throw new IllegalStateException("unset primarySpeciesIndex");
		}
		return primarySpeciesIndex;
	}

	public boolean hasSecondarySpeciesIndex() {
		return secondarySpeciesIndex.isPresent();
	}

	public int getSecondarySpeciesIndex() {
		return secondarySpeciesIndex.orElseThrow(() -> new IllegalStateException("unset secondarySpeciesIndex"));
	}

	public int getInventoryTypeGroup() {
		if (!areRankingDetailsSet) {
			throw new IllegalStateException("unset inventoryTypeGroup");
		}
		return inventoryTypeGroup;
	}

	public int getSiteCurveNumber(int n) {
		if (!areSiteCurveNumbersSet) {
			throw new IllegalStateException("unset siteCurveNumbers");
		}
		if (n == 0) {
			// Take this opportunity to initialize siteCurveNumbers[0] from that of the primary species.
			if (!areRankingDetailsSet) {
				throw new IllegalStateException("unset rankingDetails");
			}
			siteCurveNumbers[0] = siteCurveNumbers[primarySpeciesIndex];
		}
		return siteCurveNumbers[n];
	}

	public float getPrimarySpeciesDominantHeight() {
		if (!arePrimarySpeciesDetailsSet) {
			throw new IllegalStateException("unset primarySpeciesDominantHeight");
		}
		return primarySpeciesDominantHeight;
	}

	public float getPrimarySpeciesSiteIndex() {
		if (!arePrimarySpeciesDetailsSet) {
			throw new IllegalStateException("unset primarySpeciesSiteIndex");
		}
		return primarySpeciesSiteIndex;
	}

	public float getPrimarySpeciesTotalAge() {
		if (!arePrimarySpeciesDetailsSet) {
			throw new IllegalStateException("unset primarySpeciesTotalAge");
		}
		return primarySpeciesTotalAge;
	}

	public float getPrimarySpeciesAgeAtBreastHeight() {
		if (!arePrimarySpeciesDetailsSet) {
			throw new IllegalStateException("unset primarySpeciesAgeAtBreastHeight");
		}
		return primarySpeciesAgeAtBreastHeight;
	}

	public float getPrimarySpeciesAgeToBreastHeight() {
		if (!arePrimarySpeciesDetailsSet) {
			throw new IllegalStateException("unset primarySpeciesAgeToBreastHeight");
		}
		return primarySpeciesAgeToBreastHeight;
	}

	public void setSpeciesRankingDetails(SpeciesRankingDetails rankingDetails) {
		if (this.areRankingDetailsSet) {
			throw new IllegalStateException("SpeciesRankingDetails can be set once only");
		}

		this.primarySpeciesIndex = rankingDetails.primarySpeciesIndex();
		this.secondarySpeciesIndex = rankingDetails.secondarySpeciesIndex();
		this.inventoryTypeGroup = rankingDetails.inventoryTypeGroup();

		this.areRankingDetailsSet = true;
	}

	public void setSiteCurveNumbers(int[] siteCurveNumbers) {
		if (this.areSiteCurveNumbersSet) {
			throw new IllegalStateException("SiteCurveNumbers can be set once only");
		}

		this.siteCurveNumbers = Arrays.copyOf(siteCurveNumbers, siteCurveNumbers.length);

		areSiteCurveNumbersSet = true;
	}

	public void setPrimarySpeciesDetails(PrimarySpeciesDetails details) {
		if (this.arePrimarySpeciesDetailsSet) {
			throw new IllegalStateException("PrimarySpeciesDetails can be set once only");
		}

		this.primarySpeciesDominantHeight = details.primarySpeciesDominantHeight();
		this.primarySpeciesSiteIndex = details.primarySpeciesSiteIndex();
		this.primarySpeciesTotalAge = details.primarySpeciesTotalAge();
		this.primarySpeciesAgeAtBreastHeight = details.primarySpeciesAgeAtBreastHeight();
		this.primarySpeciesAgeToBreastHeight = details.primarySpeciesAgeToBreastHeight();

		// Store these values into the wallet - VHDOM1 lines 182 - 186
		if (wallet.dominantHeights[primarySpeciesIndex] <= 0.0) {
			wallet.dominantHeights[primarySpeciesIndex] = this.primarySpeciesDominantHeight;
		}
		if (wallet.siteIndices[primarySpeciesIndex] <= 0.0) {
			wallet.siteIndices[primarySpeciesIndex] = this.primarySpeciesSiteIndex;
		}
		if (wallet.ageTotals[primarySpeciesIndex] <= 0.0) {
			wallet.ageTotals[primarySpeciesIndex] = this.primarySpeciesTotalAge;
		}
		if (wallet.yearsAtBreastHeight[primarySpeciesIndex] <= 0.0) {
			wallet.yearsAtBreastHeight[primarySpeciesIndex] = this.primarySpeciesAgeAtBreastHeight;
		}
		if (wallet.yearsAtBreastHeight[primarySpeciesIndex] <= 0.0) {
			wallet.yearsAtBreastHeight[primarySpeciesIndex] = this.primarySpeciesAgeToBreastHeight;
		}

		this.arePrimarySpeciesDetailsSet = true;
	}

	public void setCompatibilityVariableDetails(
			MatrixMap3<UtilizationClass, VolumeVariable, LayerType, Float>[] cvVolume,
			MatrixMap2<UtilizationClass, LayerType, Float>[] cvBasalArea,
			MatrixMap2<UtilizationClass, LayerType, Float>[] cvQuadraticMeanDiameter,
			Map<VolumeVariable, Float>[] cvSmall
	) {
		if (this.areCompatibilityVariablesSet) {
			throw new IllegalStateException("CompatibilityVariablesSet can be set once only");
		}

		this.cvVolume = cvVolume;
		this.cvBasalArea = cvBasalArea;
		this.cvQuadraticMeanDiameter = cvQuadraticMeanDiameter;
		this.cvSmall = cvSmall;

		this.areCompatibilityVariablesSet = true;
	}

	public float
			getCVVolume(int speciesIndex, UtilizationClass uc, VolumeVariable volumeVariable, LayerType layerType) {
		if (!this.areCompatibilityVariablesSet) {
			throw new IllegalStateException("unset cvVolumes");
		}

		return cvVolume[speciesIndex].get(uc, volumeVariable, layerType);
	}

	public float getCVBasalArea(int speciesIndex, UtilizationClass uc, LayerType layerType) {
		if (!this.areCompatibilityVariablesSet) {
			throw new IllegalStateException("unset cvBasalAreas");
		}

		return cvBasalArea[speciesIndex].get(uc, layerType);
	}

	public float getCVQuadraticMeanDiameter(int speciesIndex, UtilizationClass uc, LayerType layerType) {
		if (!this.areCompatibilityVariablesSet) {
			throw new IllegalStateException("unset cvBasalAreas");
		}

		return cvQuadraticMeanDiameter[speciesIndex].get(uc, layerType);
	}

	public float getCVSmall(int speciesIndex, VolumeVariable volumeVariable) {
		if (!this.areCompatibilityVariablesSet) {
			throw new IllegalStateException("unset cvBasalAreas");
		}

		return cvSmall[speciesIndex].get(volumeVariable);
	}
}