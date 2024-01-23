package ca.bc.gov.nrs.vdyp.application;

/**
 * Identifies the VDYP applications. The PH_ values are placeholders, included
 * so that the enum values agree with the original values used for the JPROGRAM
 * COMMON variable.
 * 
 * @author Michael Junkin, Vivid Solutions
 */
public enum VDYPApplicationIdentifier
{
	PH_Zero, 
	FIPStart, // == 1
	PH_One, 
	PH_Two, 
	VRIStart, // == 3
	PH_Four, 
	PH_Five, 
	VDYPForward, // == 6
	VDYPBack, // == 7
	VRIAdjust // == 8
}
