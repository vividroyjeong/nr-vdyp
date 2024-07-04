package ca.bc.gov.nrs.vdyp.forward;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.common.GenusDefinitionMap;
import ca.bc.gov.nrs.vdyp.common.Utils;
import ca.bc.gov.nrs.vdyp.common_calculators.enumerations.SiteIndexEquation;
import ca.bc.gov.nrs.vdyp.forward.model.ForwardControlVariables;
import ca.bc.gov.nrs.vdyp.forward.model.ForwardDebugSettings;
import ca.bc.gov.nrs.vdyp.forward.model.VdypPolygon;
import ca.bc.gov.nrs.vdyp.io.parse.coe.ModifierParser;
import ca.bc.gov.nrs.vdyp.model.BecLookup;
import ca.bc.gov.nrs.vdyp.model.Coefficients;
import ca.bc.gov.nrs.vdyp.model.CompVarAdjustments;
import ca.bc.gov.nrs.vdyp.model.DebugSettings;
import ca.bc.gov.nrs.vdyp.model.GenusDefinition;
import ca.bc.gov.nrs.vdyp.model.LayerType;
import ca.bc.gov.nrs.vdyp.model.MatrixMap2;
import ca.bc.gov.nrs.vdyp.model.MatrixMap3;
import ca.bc.gov.nrs.vdyp.model.Region;
import ca.bc.gov.nrs.vdyp.model.SiteCurveAgeMaximum;
import ca.bc.gov.nrs.vdyp.model.UtilizationClass;

class ForwardProcessingState {

	/** An "instance" is the processing state of one polygon */
	private static final int LAYERS_PER_INSTANCE = 2;

	/** An "instance" is the processing state of one polygon */
	@SuppressWarnings("unused")
	private static final int MAX_INSTANCES = 3;

	/** The control map defining the context of the execution */
	private final Map<String, Object> controlMap;

	// Cached values from the controlMap

	final GenusDefinitionMap genusDefinitionMap;
	final ForwardControlVariables forwardGrowthDetails;
	final ForwardDebugSettings debugSettings;
	final Map<String, Coefficients> netDecayWasteCoeMap;
	final MatrixMap2<Integer, Integer, Optional<Coefficients>> netDecayCoeMap;
	final MatrixMap2<String, Region, Float> wasteModifierMap;
	final MatrixMap2<String, Region, Float> decayModifierMap;
	final MatrixMap2<Integer, Integer, Optional<Coefficients>> closeUtilizationCoeMap;
	final Map<Integer, Coefficients> totalStandWholeStepVolumeCoeMap;
	final MatrixMap2<Integer, Integer, Optional<Coefficients>> wholeStemUtilizationComponentMap;
	final MatrixMap3<Integer, String, String, Coefficients> quadMeanDiameterUtilizationComponentMap;
	final MatrixMap3<Integer, String, String, Coefficients> basalAreaDiameterUtilizationComponentMap;
	final Map<String, Coefficients> smallComponentWholeStemVolumeCoefficients;
	final Map<String, Coefficients> smallComponentLoreyHeightCoefficients;
	final Map<String, Coefficients> smallComponentQuadMeanDiameterCoefficients;
	final Map<String, Coefficients> smallComponentBasalAreaCoefficients;
	final Map<String, Coefficients> smallComponentProbabilityCoefficients;
	final Map<Integer, SiteCurveAgeMaximum> maximumAgeBySiteCurveNumber;

	/** The storage banks */
	private final Bank[/* instances */][/* layers of instance */] banks;

	/** The active state */
	private PolygonProcessingState pps;

	public ForwardProcessingState(Map<String, Object> controlMap) {
		this.controlMap = controlMap;

		banks = new Bank[][] { new Bank[LAYERS_PER_INSTANCE], new Bank[LAYERS_PER_INSTANCE],
				new Bank[LAYERS_PER_INSTANCE] };

		this.forwardGrowthDetails = Utils
				.expectParsedControl(controlMap, ControlKey.VTROL, ForwardControlVariables.class);
		this.debugSettings = new ForwardDebugSettings(
				Utils.expectParsedControl(controlMap, ControlKey.DEBUG_SWITCHES, DebugSettings.class)
		);

		List<GenusDefinition> genusDefinitions = Utils
				.<List<GenusDefinition>>expectParsedControl(controlMap, ControlKey.SP0_DEF, List.class);
		this.genusDefinitionMap = new GenusDefinitionMap(genusDefinitions);

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
		this.closeUtilizationCoeMap = Utils.<MatrixMap2<Integer, Integer, Optional<Coefficients>>>expectParsedControl(
				controlMap, ControlKey.CLOSE_UTIL_VOLUME, MatrixMap2.class
		);
		this.totalStandWholeStepVolumeCoeMap = Utils.<Map<Integer, Coefficients>>expectParsedControl(
				controlMap, ControlKey.TOTAL_STAND_WHOLE_STEM_VOL, Map.class
		);
		this.wholeStemUtilizationComponentMap = Utils
				.<MatrixMap2<Integer, Integer, Optional<Coefficients>>>expectParsedControl(
						controlMap, ControlKey.UTIL_COMP_WS_VOLUME, MatrixMap2.class
				);
		this.quadMeanDiameterUtilizationComponentMap = Utils
				.<MatrixMap3<Integer, String, String, Coefficients>>expectParsedControl(
						controlMap, ControlKey.UTIL_COMP_DQ, MatrixMap3.class
				);
		this.basalAreaDiameterUtilizationComponentMap = Utils
				.<MatrixMap3<Integer, String, String, Coefficients>>expectParsedControl(
						controlMap, ControlKey.UTIL_COMP_BA, MatrixMap3.class
				);
		this.smallComponentWholeStemVolumeCoefficients = Utils
				.<Map<String, Coefficients>>expectParsedControl(controlMap, ControlKey.SMALL_COMP_WS_VOLUME, Map.class);
		this.smallComponentLoreyHeightCoefficients = Utils
				.<Map<String, Coefficients>>expectParsedControl(controlMap, ControlKey.SMALL_COMP_HL, Map.class);
		this.smallComponentQuadMeanDiameterCoefficients = Utils
				.<Map<String, Coefficients>>expectParsedControl(controlMap, ControlKey.SMALL_COMP_DQ, Map.class);
		this.smallComponentBasalAreaCoefficients = Utils
				.<Map<String, Coefficients>>expectParsedControl(controlMap, ControlKey.SMALL_COMP_BA, Map.class);
		this.smallComponentProbabilityCoefficients = Utils.<Map<String, Coefficients>>expectParsedControl(
				controlMap, ControlKey.SMALL_COMP_PROBABILITY, Map.class
		);
		this.maximumAgeBySiteCurveNumber = Utils.<Map<Integer, SiteCurveAgeMaximum>>expectParsedControl(
				controlMap, ControlKey.SITE_CURVE_AGE_MAX, Map.class
		);
	}

	public GenusDefinitionMap getGenusDefinitionMap() {
		return genusDefinitionMap;
	}

	public BecLookup getBecLookup() {
		return Utils.expectParsedControl(controlMap, ControlKey.BEC_DEF, BecLookup.class);
	}

	public MatrixMap2<String, Region, SiteIndexEquation> getSiteCurveMap() {
		return Utils.expectParsedControl(controlMap, ControlKey.SITE_CURVE_NUMBERS, MatrixMap2.class);
	}

	public MatrixMap2<String, Region, Coefficients> getHl1Coefficients() {
		return Utils.expectParsedControl(controlMap, ControlKey.HL_PRIMARY_SP_EQN_P1, MatrixMap2.class);
	}

	public CompVarAdjustments getCompVarAdjustments() {
		return Utils.expectParsedControl(controlMap, ControlKey.PARAM_ADJUSTMENTS, CompVarAdjustments.class);
	}

	public ForwardControlVariables getForwardControlVariables() {
		return Utils.expectParsedControl(controlMap, ControlKey.VTROL, ForwardControlVariables.class);
	}

	private static final float MIN_BASAL_AREA = 0.001f;

	public void setPolygon(VdypPolygon polygon) {
		// Move the primary layer of the given polygon to bank zero.
		banks[0][0] = new Bank(
				polygon.getPrimaryLayer(), polygon.getBiogeoclimaticZone(),
				s -> s.getUtilizations().isPresent()
						? s.getUtilizations().get().get(UtilizationClass.ALL).getBasalArea() >= MIN_BASAL_AREA
						: true
		);
		pps = new PolygonProcessingState(this, polygon, banks[0][0], controlMap);
	}

	public PolygonProcessingState getPolygonProcessingState() {
		return pps;
	}

	public Map<String, Object> getControlMap() {
		return controlMap;
	}

	public void storeActive(int instanceNumber, LayerType layerType) {
		banks[instanceNumber][layerType.ordinal()] = pps.wallet.copy();
	}

	public void transfer(int fromInstanceNumber, int toInstanceNumber, LayerType layerType) {
		banks[toInstanceNumber][layerType.ordinal()] = banks[fromInstanceNumber][layerType.ordinal()].copy();
	}

	public Bank getBank(int instanceNumber, LayerType layerType) {
		return banks[instanceNumber][layerType.ordinal()];
	}
}