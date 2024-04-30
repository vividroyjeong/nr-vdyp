package ca.bc.gov.nrs.vdyp.common_calculators;

import static ca.bc.gov.nrs.vdyp.common_calculators.SiteIndexConstants.*;

/**
 * Determines whether a given FIZ code represents the coast or interior.
 */
public class FizCheck {

	/**
	 * @param fiz a character identifying the forest inventory zone (A - L)
	 * @return the region containing the zone
	 */
	public static short fiz2Region(char fiz) {
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
