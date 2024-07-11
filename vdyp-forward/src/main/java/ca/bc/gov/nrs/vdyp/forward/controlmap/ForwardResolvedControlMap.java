package ca.bc.gov.nrs.vdyp.forward.controlmap;

import java.util.Map;

import ca.bc.gov.nrs.vdyp.common_calculators.enumerations.SiteIndexEquation;
import ca.bc.gov.nrs.vdyp.controlmap.ResolvedControlMap;
import ca.bc.gov.nrs.vdyp.forward.model.ForwardControlVariables;
import ca.bc.gov.nrs.vdyp.forward.model.ForwardDebugSettings;
import ca.bc.gov.nrs.vdyp.model.Coefficients;
import ca.bc.gov.nrs.vdyp.model.CompVarAdjustments;
import ca.bc.gov.nrs.vdyp.model.GrowthFiatDetails;
import ca.bc.gov.nrs.vdyp.model.MatrixMap2;
import ca.bc.gov.nrs.vdyp.model.MatrixMap3;
import ca.bc.gov.nrs.vdyp.model.Region;

public interface ForwardResolvedControlMap extends ResolvedControlMap {
	
	/** 25 - SITE_CURVE_NUMBERS */
	MatrixMap2<String, Region, SiteIndexEquation> getSiteCurveMap();
	
	/** 28 - PARAM_ADJUSTMENTS */
	CompVarAdjustments getCompVarAdjustments();
	
	/** 43 - UPPER_BA_BY_CI_S0_P */
	MatrixMap3<Region, String, Integer, Float> getUpperBoundsCoefficients();
	
	/** 101 - VTROL */
	ForwardControlVariables getForwardControlVariables();
	
	/** 106 - BA_YIELD */
	MatrixMap2<String, String, Coefficients> getBasalAreaYieldCoefficients();
	
	/** 111 - BA_GROWTH_FIAT */
	Map<Region, GrowthFiatDetails> getBasalAreaGrowthFiatDetails();
	
	/** 121 - BA_GROWTH_EMPIRICAL */
	MatrixMap2<String, String, Coefficients> getBasalAreaGrowthEmpiricalCoefficients();
	
	/** 199 - DEBUG_SWITCHES */
	ForwardDebugSettings getDebugSettings();
}
