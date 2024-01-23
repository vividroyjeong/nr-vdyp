package ca.bc.gov.nrs.vdyp.application;

/**
 * Identifies the VDYP applications. The PH_ values are placeholders, included
 * so that the enum values agree with the original values used for the JPROGRAM
 * COMMON variable.
 *
 * @author Michael Junkin, Vivid Solutions
 */
public enum VdypApplicationIdentifier {
	
	FIPStart(1),
	VRIStart(3),
	VDYPForward(6),
	VDYPBack(7),
	VRIAdjust(8);
	
	private final int jProgramNumber;

	VdypApplicationIdentifier(int jProgramNumber)
	{
		this.jProgramNumber = jProgramNumber;
	}
	
	public int getId()
	{
		return jProgramNumber;
	}
}
