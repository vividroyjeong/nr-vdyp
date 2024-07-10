package ca.bc.gov.nrs.vdyp.forward.controlmap;

import java.util.Map;

import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.common.Utils;
import ca.bc.gov.nrs.vdyp.common_calculators.enumerations.SiteIndexEquation;
import ca.bc.gov.nrs.vdyp.controlmap.CachingResolvedControlMapImpl;
import ca.bc.gov.nrs.vdyp.forward.model.ForwardControlVariables;
import ca.bc.gov.nrs.vdyp.forward.model.ForwardDebugSettings;
import ca.bc.gov.nrs.vdyp.model.Coefficients;
import ca.bc.gov.nrs.vdyp.model.CompVarAdjustments;
import ca.bc.gov.nrs.vdyp.model.DebugSettings;
import ca.bc.gov.nrs.vdyp.model.GrowthFiatDetails;
import ca.bc.gov.nrs.vdyp.model.MatrixMap2;
import ca.bc.gov.nrs.vdyp.model.Region;

public class ForwardResolvedControlMapImpl extends CachingResolvedControlMapImpl implements ForwardResolvedControlMap {
	
	final ForwardDebugSettings debugSettings;
	final ForwardControlVariables forwardControlVariables;
	final MatrixMap2<String, Region, SiteIndexEquation> siteCurveMap;
	final CompVarAdjustments compVarAdjustments;
	final MatrixMap2<String, String, Coefficients> basalAreaYieldCoefficients;
	final Map<Region, GrowthFiatDetails> basalAreaGrowthFiatDetails;

	public ForwardResolvedControlMapImpl(Map<String, Object> controlMap) {

		super(controlMap);
		
		this.debugSettings = new ForwardDebugSettings(get(ControlKey.DEBUG_SWITCHES, DebugSettings.class));
		this.forwardControlVariables = get(ControlKey.VTROL, ForwardControlVariables.class);
		this.siteCurveMap = Utils.expectParsedControl(controlMap, ControlKey.SITE_CURVE_NUMBERS, MatrixMap2.class);
		this.compVarAdjustments = this.get(ControlKey.PARAM_ADJUSTMENTS, CompVarAdjustments.class);
		this.basalAreaYieldCoefficients = this.get(ControlKey.BA_YIELD, MatrixMap2.class);
		this.basalAreaGrowthFiatDetails = this.get(ControlKey.BA_GROWTH_FIAT, Map.class);
	}
	
	@Override
	public ForwardDebugSettings getDebugSettings() {
		return debugSettings;
	}
	
	@Override
	public ForwardControlVariables getForwardControlVariables() {
		return forwardControlVariables;
	}

	@Override
	public MatrixMap2<String, Region, SiteIndexEquation> getSiteCurveMap() {
		return siteCurveMap;
	}

	@Override
	public CompVarAdjustments getCompVarAdjustments() {
		return compVarAdjustments;
	}
	
	@Override
	public MatrixMap2<String, String, Coefficients> getBasalAreaYieldCoefficients() {
		return basalAreaYieldCoefficients;
	}

	@Override
	public Map<Region, GrowthFiatDetails> getBasalAreaGrowthFiatDetails() {
		return basalAreaGrowthFiatDetails;
	}
}
