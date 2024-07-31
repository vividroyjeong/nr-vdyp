package ca.bc.gov.nrs.vdyp.controlmap;

import java.util.Map;
import java.util.Optional;

import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.common.GenusDefinitionMap;
import ca.bc.gov.nrs.vdyp.model.BecLookup;
import ca.bc.gov.nrs.vdyp.model.Coefficients;
import ca.bc.gov.nrs.vdyp.model.MatrixMap2;
import ca.bc.gov.nrs.vdyp.model.MatrixMap3;
import ca.bc.gov.nrs.vdyp.model.Region;
import ca.bc.gov.nrs.vdyp.model.SiteCurveAgeMaximum;

public interface ResolvedControlMap {
	<U> U get(ControlKey key, Class<? super U> clazz);

	/** 9 - BEC_DEF */
	BecLookup getBecLookup();

	/** 10 - SP0_DEF */
	GenusDefinitionMap getGenusDefinitionMap();

	/** 26 - SITE_CURVE_AGE_MAX */
	Map<Integer, SiteCurveAgeMaximum> getMaximumAgeBySiteCurveNumber();

	/** 30 - DEFAULT_EQ_NUM */
	MatrixMap2<String, String, Integer> getDefaultEquationGroup();

	/** 31 - EQN_MODIFIERS */
	MatrixMap2<Integer, Integer, Optional<Integer>> getEquationModifierGroup();

	/** 50 - HL_PRIMARY_SP_EQN_P1 */
	MatrixMap2<String, Region, Coefficients> getHl1Coefficients();

	/** 70 - UTIL_COMP_BA */
	MatrixMap3<Integer, String, String, Coefficients> getBasalAreaDiameterUtilizationComponentMap();

	/** 71 - UTIL_COMP_DQ */
	MatrixMap3<Integer, String, String, Coefficients> getQuadMeanDiameterUtilizationComponentMap();

	/** 80 - SMALL_COMP_PROBABILITY */
	Map<String, Coefficients> getSmallComponentProbabilityCoefficients();

	/** 81 - SMALL_COMP_BA */
	Map<String, Coefficients> getSmallComponentBasalAreaCoefficients();

	/** 82 - SMALL_COMP_DQ */
	Map<String, Coefficients> getSmallComponentQuadMeanDiameterCoefficients();

	/** 85 - SMALL_COMP_HL */
	Map<String, Coefficients> getSmallComponentLoreyHeightCoefficients();

	/** 86 - SMALL_COMP_WS_VOLUME */
	Map<String, Coefficients> getSmallComponentWholeStemVolumeCoefficients();

	/** 90 - TOTAL_STAND_WHOLE_STEM_VOL */
	Map<Integer, Coefficients> getTotalStandWholeStepVolumeCoeMap();

	/** 91 - UTIL_COMP_WS_VOLUME */
	MatrixMap2<Integer, Integer, Optional<Coefficients>> getWholeStemUtilizationComponentMap();

	/** 92 - CLOSE_UTIL_VOLUME */
	MatrixMap2<Integer, Integer, Optional<Coefficients>> getCloseUtilizationCoeMap();

	/** 93 - VOLUME_NET_DECAY */
	MatrixMap2<Integer, Integer, Optional<Coefficients>> getNetDecayCoeMap();

	/** 94 - VOLUME_NET_DECAY_WASTE */
	Map<String, Coefficients> getNetDecayWasteCoeMap();

	/** 108 - BA_DQ_UPPER_BOUNDS */
	Map<Integer, Coefficients> getUpperBounds();

	/** 198 - MODIFIER_FILE */
	MatrixMap2<String, Region, Float> getWasteModifierMap();

	/** 198 - MODIFIER_FILE */
	MatrixMap2<String, Region, Float> getDecayModifierMap();
}
