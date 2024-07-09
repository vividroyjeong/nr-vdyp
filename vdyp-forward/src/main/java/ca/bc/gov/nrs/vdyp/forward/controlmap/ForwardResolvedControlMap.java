package ca.bc.gov.nrs.vdyp.forward.controlmap;

import ca.bc.gov.nrs.vdyp.common_calculators.enumerations.SiteIndexEquation;
import ca.bc.gov.nrs.vdyp.controlmap.ResolvedControlMap;
import ca.bc.gov.nrs.vdyp.forward.model.ForwardControlVariables;
import ca.bc.gov.nrs.vdyp.forward.model.ForwardDebugSettings;
import ca.bc.gov.nrs.vdyp.model.BecLookup;
import ca.bc.gov.nrs.vdyp.model.MatrixMap2;
import ca.bc.gov.nrs.vdyp.model.Region;

public interface ForwardResolvedControlMap extends ResolvedControlMap {
	
	ForwardDebugSettings getDebugSettings();

	ForwardControlVariables getForwardControlVariables();
	
	BecLookup getBecLookup();
	
	MatrixMap2<String, Region, SiteIndexEquation> getSiteCurveMap();
}
