package ca.bc.gov.nrs.vdyp.forward.test;

import ca.bc.gov.nrs.vdyp.application.VDYPApplication;
import ca.bc.gov.nrs.vdyp.application.VDYPApplicationIdentifier;

public class VDYPForwardControlParserTestApplication extends VDYPApplication
{
	@Override
	public VDYPApplicationIdentifier getIdentifier()
	{
		return VDYPApplicationIdentifier.VDYPForward;
	}
}