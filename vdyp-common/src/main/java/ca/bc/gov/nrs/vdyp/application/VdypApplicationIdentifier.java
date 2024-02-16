package ca.bc.gov.nrs.vdyp.application;

/**
 * Identifies the VDYP applications. Matches the values used for the FORTRAN
 * implementation's JPROGRAM COMMON variable.
 *
 * @author Michael Junkin, Vivid Solutions
 */
public enum VdypApplicationIdentifier {

	FIPStart(true, 1), //
	VRIStart(true, 3), //
	VDYPForward(false, 6), //
	VDYPBack(false, 7), //
	VRIAdjust(false, 8);

	private final int jProgramNumber;
	private final boolean isStart;

	VdypApplicationIdentifier(boolean isStart, int jProgramNumber) {
		this.jProgramNumber = jProgramNumber;
		this.isStart = isStart;
	}

	public int getJProgramNumber() {
		return jProgramNumber;
	}

	public boolean isStart() {
		return isStart;
	}
}
