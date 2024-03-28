package ca.bc.gov.nrs.vdyp.common_calculators;

/**
 * Determines whether a given FIZ code represents the coast or interior.
 */
public class FizCheck {
	// From sindex.h
	public static final short FIZ_UNKNOWN = 0;
	public static final short FIZ_COAST = 1;
	public static final short FIZ_INTERIOR = 2;

	public static short fiz_check(char fiz) {
		switch (fiz) {
		case 'A', 'B', 'C':
			return FIZ_COAST;
		case 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L':
			return FIZ_INTERIOR;
		default:
			return FIZ_UNKNOWN;
		}
	}
}
