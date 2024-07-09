package ca.bc.gov.nrs.vdyp.controlmap;

import java.util.Map;
import java.util.Optional;

import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.common.GenusDefinitionMap;
import ca.bc.gov.nrs.vdyp.model.Coefficients;
import ca.bc.gov.nrs.vdyp.model.CompVarAdjustments;
import ca.bc.gov.nrs.vdyp.model.MatrixMap2;
import ca.bc.gov.nrs.vdyp.model.MatrixMap3;
import ca.bc.gov.nrs.vdyp.model.Region;
import ca.bc.gov.nrs.vdyp.model.SiteCurveAgeMaximum;

public interface ResolvedControlMap {	
	<U> U get(ControlKey key, Class<? super U> clazz);

	GenusDefinitionMap getGenusDefinitionMap();

	Map<String, Coefficients> getNetDecayWasteCoeMap();

	MatrixMap2<Integer, Integer, Optional<Coefficients>> getNetDecayCoeMap();

	MatrixMap2<String, Region, Float> getWasteModifierMap();

	MatrixMap2<String, Region, Float> getDecayModifierMap();

	MatrixMap2<Integer, Integer, Optional<Coefficients>> getCloseUtilizationCoeMap();

	Map<Integer, Coefficients> getTotalStandWholeStepVolumeCoeMap();

	MatrixMap2<Integer, Integer, Optional<Coefficients>> getWholeStemUtilizationComponentMap();

	MatrixMap3<Integer, String, String, Coefficients> getQuadMeanDiameterUtilizationComponentMap();

	MatrixMap3<Integer, String, String, Coefficients> getBasalAreaDiameterUtilizationComponentMap();

	Map<String, Coefficients> getSmallComponentWholeStemVolumeCoefficients();

	Map<String, Coefficients> getSmallComponentLoreyHeightCoefficients();

	Map<String, Coefficients> getSmallComponentQuadMeanDiameterCoefficients();

	Map<String, Coefficients> getSmallComponentBasalAreaCoefficients();

	Map<String, Coefficients> getSmallComponentProbabilityCoefficients();

	Map<Integer, SiteCurveAgeMaximum> getMaximumAgeBySiteCurveNumber();

	Map<Integer, Coefficients> getUpperBounds();

	MatrixMap2<String, String, Integer> getDefaultEquationGroup();

	MatrixMap2<Integer, Integer, Optional<Integer>> getEquationModifierGroup();

	MatrixMap2<String, Region, Coefficients> getHl1Coefficients();

	CompVarAdjustments getCompVarAdjustments();
}
