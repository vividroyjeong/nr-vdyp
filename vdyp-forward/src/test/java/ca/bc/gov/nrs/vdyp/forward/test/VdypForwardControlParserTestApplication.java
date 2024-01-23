package ca.bc.gov.nrs.vdyp.forward.test;

import ca.bc.gov.nrs.vdyp.application.VdypApplication;
import ca.bc.gov.nrs.vdyp.application.VdypApplicationIdentifier;

public class VdypForwardControlParserTestApplication extends VdypApplication {
	@Override
	public VdypApplicationIdentifier getIdentifier() {
		return VdypApplicationIdentifier.VDYPForward;
	}
}