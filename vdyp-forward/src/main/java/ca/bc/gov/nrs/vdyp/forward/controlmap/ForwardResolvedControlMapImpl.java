package ca.bc.gov.nrs.vdyp.forward.controlmap;

import java.util.Map;

import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.common.Utils;
import ca.bc.gov.nrs.vdyp.common_calculators.enumerations.SiteIndexEquation;
import ca.bc.gov.nrs.vdyp.controlmap.CachingResolvedControlMapImpl;
import ca.bc.gov.nrs.vdyp.forward.model.ForwardControlVariables;
import ca.bc.gov.nrs.vdyp.forward.model.ForwardDebugSettings;
import ca.bc.gov.nrs.vdyp.model.BecLookup;
import ca.bc.gov.nrs.vdyp.model.DebugSettings;
import ca.bc.gov.nrs.vdyp.model.MatrixMap2;
import ca.bc.gov.nrs.vdyp.model.Region;

public class ForwardResolvedControlMapImpl extends CachingResolvedControlMapImpl implements ForwardResolvedControlMap {
	
	final ForwardDebugSettings debugSettings;
	final ForwardControlVariables forwardControlVariables;
	final BecLookup becLookup;
	final MatrixMap2<String, Region, SiteIndexEquation> siteCurveMap;
	
	public ForwardResolvedControlMapImpl(Map<String, Object> controlMap) {

		super(controlMap);
		
		this.debugSettings = new ForwardDebugSettings(get(ControlKey.DEBUG_SWITCHES, DebugSettings.class));
		this.forwardControlVariables = get(ControlKey.VTROL, ForwardControlVariables.class);
		this.becLookup = Utils.expectParsedControl(controlMap, ControlKey.BEC_DEF, BecLookup.class);
		this.siteCurveMap = Utils.expectParsedControl(controlMap, ControlKey.SITE_CURVE_NUMBERS, MatrixMap2.class);
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
	public BecLookup getBecLookup() {
		return becLookup;
	}

	@Override
	public MatrixMap2<String, Region, SiteIndexEquation> getSiteCurveMap() {
		return siteCurveMap;
	}
}
