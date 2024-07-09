package ca.bc.gov.nrs.vdyp.controlmap;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.common.GenusDefinitionMap;
import ca.bc.gov.nrs.vdyp.io.parse.coe.ModifierParser;
import ca.bc.gov.nrs.vdyp.model.Coefficients;
import ca.bc.gov.nrs.vdyp.model.CompVarAdjustments;
import ca.bc.gov.nrs.vdyp.model.GenusDefinition;
import ca.bc.gov.nrs.vdyp.model.MatrixMap2;
import ca.bc.gov.nrs.vdyp.model.MatrixMap3;
import ca.bc.gov.nrs.vdyp.model.Region;
import ca.bc.gov.nrs.vdyp.model.SiteCurveAgeMaximum;

public class CachingResolvedControlMapImpl extends ResolvedControlMapImpl implements ResolvedControlMap {

	final GenusDefinitionMap genusDefinitionMap;
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
	final Map<Integer, Coefficients> upperBounds;
	final MatrixMap2<String, String, Integer> defaultEquationGroup;
	final MatrixMap2<Integer, Integer, Optional<Integer>> equationModifierGroup;
	final MatrixMap2<String, Region, Coefficients> hl1Coefficients;
	public CachingResolvedControlMapImpl(Map<String, Object> controlMap) {

		super(controlMap);

		List<GenusDefinition> genusDefinitions = this.get(ControlKey.SP0_DEF, List.class);
		this.genusDefinitionMap = new GenusDefinitionMap(genusDefinitions);

		this.netDecayWasteCoeMap = this.get(ControlKey.VOLUME_NET_DECAY_WASTE, Map.class);		
		this.netDecayCoeMap = this.get(ControlKey.VOLUME_NET_DECAY, MatrixMap2.class);
		this.wasteModifierMap = this.get(ControlKey.WASTE_MODIFIERS, MatrixMap2.class);
		this.decayModifierMap = this.get(ModifierParser.CONTROL_KEY_MOD301_DECAY, MatrixMap2.class);
		this.closeUtilizationCoeMap = this.get(ControlKey.CLOSE_UTIL_VOLUME, MatrixMap2.class);
		this.totalStandWholeStepVolumeCoeMap = this.get(ControlKey.TOTAL_STAND_WHOLE_STEM_VOL, Map.class);
		this.wholeStemUtilizationComponentMap = this.get(ControlKey.UTIL_COMP_WS_VOLUME, MatrixMap2.class);
		this.quadMeanDiameterUtilizationComponentMap = this.get(ControlKey.UTIL_COMP_DQ, MatrixMap3.class);
		this.basalAreaDiameterUtilizationComponentMap = this.get(ControlKey.UTIL_COMP_BA, MatrixMap3.class);
		this.smallComponentWholeStemVolumeCoefficients = this.get(ControlKey.SMALL_COMP_WS_VOLUME, Map.class);
		this.smallComponentLoreyHeightCoefficients = this.get(ControlKey.SMALL_COMP_HL, Map.class);
		this.smallComponentQuadMeanDiameterCoefficients = this.get(ControlKey.SMALL_COMP_DQ, Map.class);
		this.smallComponentBasalAreaCoefficients = this.get(ControlKey.SMALL_COMP_BA, Map.class);
		this.smallComponentProbabilityCoefficients = this.get(ControlKey.SMALL_COMP_PROBABILITY, Map.class);
		this.maximumAgeBySiteCurveNumber = this.get(ControlKey.SITE_CURVE_AGE_MAX, Map.class);
		this.defaultEquationGroup = this.get(ControlKey.DEFAULT_EQ_NUM, MatrixMap2.class);
		this.hl1Coefficients = this.get(ControlKey.HL_PRIMARY_SP_EQN_P1, MatrixMap2.class);
		this.upperBounds = this.get(ControlKey.BA_DQ_UPPER_BOUNDS, Map.class);
		this.equationModifierGroup = this.get(ControlKey.EQN_MODIFIERS, MatrixMap2.class);
	}

	@Override
	public GenusDefinitionMap getGenusDefinitionMap() {
		return genusDefinitionMap;
	}

	@Override
	public Map<String, Coefficients> getNetDecayWasteCoeMap() {
		return netDecayWasteCoeMap;
	}

	@Override
	public MatrixMap2<Integer, Integer, Optional<Coefficients>> getNetDecayCoeMap() {
		return netDecayCoeMap;
	}

	@Override
	public MatrixMap2<String, Region, Float> getWasteModifierMap() {
		return wasteModifierMap;
	}

	@Override
	public MatrixMap2<String, Region, Float> getDecayModifierMap() {
		return decayModifierMap;
	}

	@Override
	public MatrixMap2<Integer, Integer, Optional<Coefficients>> getCloseUtilizationCoeMap() {
		return closeUtilizationCoeMap;
	}

	@Override
	public Map<Integer, Coefficients> getTotalStandWholeStepVolumeCoeMap() {
		return totalStandWholeStepVolumeCoeMap;
	}

	@Override
	public MatrixMap2<Integer, Integer, Optional<Coefficients>> getWholeStemUtilizationComponentMap() {
		return wholeStemUtilizationComponentMap;
	}

	@Override
	public MatrixMap3<Integer, String, String, Coefficients> getQuadMeanDiameterUtilizationComponentMap() {
		return quadMeanDiameterUtilizationComponentMap;
	}

	@Override
	public MatrixMap3<Integer, String, String, Coefficients> getBasalAreaDiameterUtilizationComponentMap() {
		return basalAreaDiameterUtilizationComponentMap;
	}

	@Override
	public Map<String, Coefficients> getSmallComponentWholeStemVolumeCoefficients() {
		return smallComponentWholeStemVolumeCoefficients;
	}

	@Override
	public Map<String, Coefficients> getSmallComponentLoreyHeightCoefficients() {
		return smallComponentLoreyHeightCoefficients;
	}

	@Override
	public Map<String, Coefficients> getSmallComponentQuadMeanDiameterCoefficients() {
		return smallComponentQuadMeanDiameterCoefficients;
	}

	@Override
	public Map<String, Coefficients> getSmallComponentBasalAreaCoefficients() {
		return smallComponentBasalAreaCoefficients;
	}

	@Override
	public Map<String, Coefficients> getSmallComponentProbabilityCoefficients() {
		return smallComponentProbabilityCoefficients;
	}

	@Override
	public Map<Integer, SiteCurveAgeMaximum> getMaximumAgeBySiteCurveNumber() {
		return maximumAgeBySiteCurveNumber;
	}

	@Override
	public Map<Integer, Coefficients> getUpperBounds() {
		return upperBounds;
	}

	@Override
	public MatrixMap2<String, String, Integer> getDefaultEquationGroup() {
		return defaultEquationGroup;
	}

	@Override
	public MatrixMap2<Integer, Integer, Optional<Integer>> getEquationModifierGroup() {
		return equationModifierGroup;
	}

	@Override
	public MatrixMap2<String, Region, Coefficients> getHl1Coefficients() {
		return hl1Coefficients;
	}
}
