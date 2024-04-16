package ca.bc.gov.nrs.vdyp.common_calculators;

import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.ClassErrorException;
import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.CommonCalculatorException;
import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.ForestInventoryZoneException;
import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.SpeciesErrorException;

/**
 * SiteClassCode2SiteIndex.java 
 * - translates site class code to site index (height in metres) for a given species, 
 *   site class, and FIZ. 
 * - the translation is intended to be used where total age is small (under 30 years), 
 *   where site index based on height may not be reliable. 
 * - primarily used by VDYP and FredTab. 
 * - the origin of the values used here is Inventory Branch.
 */
public class SiteClassCode2SiteIndex {
	// Taken from sindex.h

	/* define species and equation indices */
	private static final int SI_SPEC_ACT = 5;
	private static final int SI_SPEC_AT = 8;
	private static final int SI_SPEC_BA = 11;
	private static final int SI_SPEC_BL = 16;
	private static final int SI_SPEC_CWC = 23;
	private static final int SI_SPEC_CWI = 24;
	private static final int SI_SPEC_DR = 29;
	private static final int SI_SPEC_EA = 31;
	private static final int SI_SPEC_EP = 34;
	private static final int SI_SPEC_FDC = 39;
	private static final int SI_SPEC_FDI = 40;
	private static final int SI_SPEC_HWC = 47;
	private static final int SI_SPEC_HWI = 48;
	private static final int SI_SPEC_LA = 57;
	private static final int SI_SPEC_LT = 59;
	private static final int SI_SPEC_LW = 60;
	private static final int SI_SPEC_MB = 62;
	private static final int SI_SPEC_PA = 76;
	private static final int SI_SPEC_PF = 77;
	private static final int SI_SPEC_PLI = 81;
	private static final int SI_SPEC_PW = 85;
	private static final int SI_SPEC_PY = 87;
	private static final int SI_SPEC_SB = 95;
	private static final int SI_SPEC_SE = 96;
	private static final int SI_SPEC_SS = 99;
	private static final int SI_SPEC_SW = 100;
	private static final int SI_SPEC_YC = 130;

	public static double class_to_index(int sp_index, char sitecl, char fiz) throws CommonCalculatorException {

		if (sitecl != 'G' && sitecl != 'M' && sitecl != 'P' && sitecl != 'L') {
			throw new ClassErrorException("Unknown site class code: " + sitecl);
		}

		switch (sp_index) {
		case SI_SPEC_ACT, SI_SPEC_MB:
			switch (sitecl) {
			case 'G':
				return 26;
			case 'M':
				return 18;
			case 'P':
				return 9;
			case 'L':
				return 3;
			default:
				break;
			}
			break;
		case SI_SPEC_AT, SI_SPEC_EA, SI_SPEC_EP:
			switch (sitecl) {
			case 'G':
				return 27;
			case 'M':
				return 20;
			case 'P':
				return 12;
			case 'L':
				return 4;
			default:
				break;
			}
			break;
		case SI_SPEC_BA:
			switch (sitecl) {
			case 'G':
				return 29;
			case 'M':
				return 23;
			case 'P':
				return 14;
			case 'L':
				return 5;
			default:
				break;
			}
			break;
		case SI_SPEC_BL:
			switch (sitecl) {
			case 'G':
				return 18;
			case 'M':
				return 15;
			case 'P':
				return 11;
			case 'L':
				return 5;
			default:
				break;
			}
			break;
		case SI_SPEC_CWC, SI_SPEC_YC:
			switch (sitecl) {
			case 'G':
				return 29;
			case 'M':
				return 23;
			case 'P':
				return 15;
			case 'L':
				return 6;
			default:
				break;
			}
			break;
		case SI_SPEC_CWI:
			switch (sitecl) {
			case 'G':
				return 22;
			case 'M':
				return 19;
			case 'P':
				return 13;
			case 'L':
				return 5;
			default:
				break;
			}
			break;
		case SI_SPEC_DR:
			switch (sitecl) {
			case 'G':
				return 33;
			case 'M':
				return 23;
			case 'P':
				return 13;
			case 'L':
				return 6;
			default:
				break;
			}
			break;
		case SI_SPEC_FDC:
			switch (sitecl) {
			case 'G':
				return 32;
			case 'M':
				return 27;
			case 'P':
				return 18;
			case 'L':
				return 7;
			default:
				break;
			}
			break;
		case SI_SPEC_FDI:
			switch (sitecl) {
			case 'G':
				return 20;
			case 'M':
				return 17;
			case 'P':
				return 12;
			case 'L':
				return 5;
			default:
				break;
			}
			break;
		case SI_SPEC_HWC:
			switch (FizCheck.fiz_check(fiz)) {
			case FizCheck.FIZ_COAST:
				switch (sitecl) {
				case 'G':
					return 28;
				case 'M':
					return 22;
				case 'P':
					return 14;
				case 'L':
					return 5;
				default:
					break;
				}
				break;
			case FizCheck.FIZ_INTERIOR:
				switch (sitecl) {
				case 'G':
					return 21;
				case 'M':
					return 18;
				case 'P':
					return 12;
				case 'L':
					return 4;
				}

			default:
				throw new ForestInventoryZoneException("Unknown FIZ code: " + fiz);

			}

		case SI_SPEC_HWI:
			switch (sitecl) {
			case 'G':
				return 21;
			case 'M':
				return 18;
			case 'P':
				return 12;
			case 'L':
				return 4;
			default:
				break;
			}
			break;
		case SI_SPEC_LA, SI_SPEC_LT, SI_SPEC_LW:
			switch (sitecl) {
			case 'G':
				return 20;
			case 'M':
				return 16;
			case 'P':
				return 10;
			case 'L':
				return 3;
			default:
				break;
			}
			break;
		case SI_SPEC_PLI, SI_SPEC_PA, SI_SPEC_PF:
			switch (sitecl) {
			case 'G':
				return 20;
			case 'M':
				return 16;
			case 'P':
				return 11;
			case 'L':
				return 4;
			default:
				break;
			}
			break;
		case SI_SPEC_PY:
			switch (sitecl) {
			case 'G':
				return 17;
			case 'M':
				return 14;
			case 'P':
				return 10;
			case 'L':
				return 4;
			default:
				break;
			}
			break;
		case SI_SPEC_PW:
			switch (sitecl) {
			case 'G':
				return 28;
			case 'M':
				return 22;
			case 'P':
				return 12;
			case 'L':
				return 4;
			default:
				break;
			}
			break;
		case SI_SPEC_SS:
			switch (sitecl) {
			case 'G':
				return 28;
			case 'M':
				return 21;
			case 'P':
				return 11;
			case 'L':
				return 4;
			default:
				break;
			}
			break;

		case SI_SPEC_SB, SI_SPEC_SW, SI_SPEC_SE:
			switch (sitecl) {
			case 'G':
				return 19;
			case 'M':
				return 15;
			case 'P':
				return 10;
			case 'L':
				return 5;
			default:
				break;
			}
			break;

		}
		throw new SpeciesErrorException("Unknown species index: " + sitecl);
	}

}
