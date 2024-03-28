package ca.bc.gov.nrs.vdyp.common_calculators;

import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.AgeTypeErrorException;

/**
 * Age2Age.java
 * given age and type, converts to other type of age.
 *
 * @throws AgeTypeErrorException if unnkown age type is provided
 */
public class Age2Age {
	// Taken from sindex.h

	/*
	 * age types
	 */
	private static final int SI_AT_TOTAL = 0;
	private static final int SI_AT_BREAST = 1;

	/* define species and equation indices */
	private static final int SI_ACB_HUANGAC = 97;
	private static final int SI_ACT_THROWERAC = 103;
	private static final int SI_AT_NIGH = 92;
	private static final int SI_BA_KURUCZ82AC = 102;
	private static final int SI_BA_NIGH = 118;
	private static final int SI_BL_CHENAC = 93;
	private static final int SI_BP_CURTISAC = 94;
	private static final int SI_CWC_KURUCZAC = 101;
	private static final int SI_CWI_NIGH = 77;
	private static final int SI_DR_NIGH = 13;
	private static final int SI_EP_NIGH = 116;
	private static final int SI_FDC_BRUCEAC = 100;
	private static final int SI_FDC_NIGHTA = 88;
	private static final int SI_FDI_THROWERAC = 96;
	private static final int SI_HM_MEANSAC = 95;
	private static final int SI_HWC_WILEYAC = 99;
	private static final int SI_HWI_NIGH = 37;
	private static final int SI_LW_NIGH = 90;
	private static final int SI_PJ_HUANG = 113;
	private static final int SI_PJ_HUANGAC = 114;
	private static final int SI_PLI_NIGHTA98 = 41;
	private static final int SI_PLI_THROWER = 45;
	private static final int SI_PLI_THROWNIGH = 40;
	private static final int SI_PW_CURTISAC = 98;
	private static final int SI_PY_HANNAC = 104;
	private static final int SI_PY_NIGH = 107;
	private static final int SI_SB_NIGH = 91;
	private static final int SI_SE_CHENAC = 105;
	private static final int SI_SS_NIGH = 59;
	private static final int SI_FDC_BRUCENIGH = 89;
	private static final int SI_SW_GOUDIE_NATAC = 106;
	private static final int SI_SW_GOUDIE_PLAAC = 112;
	private static final int SI_SW_GOUDNIGH = 85;
	private static final int SI_SW_NIGHTA = 83;
	/* not used, but must be defined for array positioning */
	private static final int SI_SE_NIGHTA = 110;
	private static final int SI_SW_NIGHTA2004 = 111;
	private static final int SI_PLI_NIGHTA2004 = 109;

	public static double age_to_age(int cu_index, double age1, int age1_type, int age2_type, double y2bh)
			throws AgeTypeErrorException {
		double returnValue;

		boolean shouldBranch;
		switch (cu_index) {
		case SI_ACB_HUANGAC:
			shouldBranch = true;
			break;
		case SI_ACT_THROWERAC:
			shouldBranch = true;
			break;
		case SI_AT_NIGH:
			shouldBranch = true;
			break;
		case SI_BA_KURUCZ82AC:
			shouldBranch = true;
			break;
		case SI_BA_NIGH:
			shouldBranch = true;
			break;
		case SI_BL_CHENAC:
			shouldBranch = true;
			break;
		case SI_BP_CURTISAC:
			shouldBranch = true;
			break;
		case SI_CWC_KURUCZAC:
			shouldBranch = true;
			break;
		case SI_CWI_NIGH:
			shouldBranch = true;
			break;
		case SI_DR_NIGH:
			shouldBranch = true;
			break;
		case SI_EP_NIGH:
			shouldBranch = true;
			break;
		case SI_FDC_BRUCENIGH:
			shouldBranch = true;
			break;
		case SI_FDC_BRUCEAC:
			shouldBranch = true;
			break;
		case SI_FDC_NIGHTA:
			shouldBranch = true;
			break;
		case SI_FDI_THROWERAC:
			shouldBranch = true;
			break;
		case SI_HM_MEANSAC:
			shouldBranch = true;
			break;
		case SI_HWC_WILEYAC:
			shouldBranch = true;
			break;
		case SI_HWI_NIGH:
			shouldBranch = true;
			break;
		case SI_LW_NIGH:
			shouldBranch = true;
			break;
		case SI_PJ_HUANG:
			shouldBranch = true;
			break;
		case SI_PJ_HUANGAC:
			shouldBranch = true;
			break;
		case SI_PLI_NIGHTA2004:
			shouldBranch = true;
			break;
		case SI_PLI_NIGHTA98:
			shouldBranch = true;
			break;
		case SI_PLI_THROWNIGH:
			shouldBranch = true;
			break;
		case SI_PLI_THROWER:
			shouldBranch = true;
			break;
		case SI_PW_CURTISAC:
			shouldBranch = true;
			break;
		case SI_PY_HANNAC:
			shouldBranch = true;
			break;
		case SI_PY_NIGH:
			shouldBranch = true;
			break;
		case SI_SB_NIGH:
			shouldBranch = true;
			break;
		case SI_SE_CHENAC:
			shouldBranch = true;
			break;
		case SI_SE_NIGHTA:
			shouldBranch = true;
			break;
		case SI_SW_GOUDIE_NATAC:
			shouldBranch = true;
			break;
		case SI_SW_GOUDIE_PLAAC:
			shouldBranch = true;
			break;
		case SI_SW_GOUDNIGH:
			shouldBranch = true;
			break;
		case SI_SW_NIGHTA2004:
			shouldBranch = true;
			break;
		case SI_SW_NIGHTA:
			shouldBranch = true;
			break;
		case SI_SS_NIGH:
			shouldBranch = true;
			break;
		default:
			shouldBranch = false;
		}

		if (shouldBranch) {
			if (age1_type == SI_AT_BREAST) {
				if (age2_type == SI_AT_TOTAL) {
					/* convert to total age */
					returnValue = age1 + y2bh - 0.5;
					if (returnValue < 0) {
						returnValue = 0;
					}
					return returnValue;
				}
				throw new AgeTypeErrorException("Unkown age type provided");
			}

			if (age1_type == SI_AT_TOTAL) {
				if (age2_type == SI_AT_BREAST) {
					/* convert to breast-height age */
					returnValue = age1 - y2bh + 0.5;
					if (returnValue < 0) {
						returnValue = 0;
					}
					return returnValue;
				}
				throw new AgeTypeErrorException("Unkown age type provided");
			}
		} else {
			if (age1_type == SI_AT_BREAST) {
				if (age2_type == SI_AT_TOTAL) {
					/* convert to total age */
					returnValue = age1 + y2bh;
					if (returnValue < 0) {
						returnValue = 0;
					}
					return returnValue;
				}
				throw new AgeTypeErrorException("Unkown age type provided");
			}

			if (age1_type == SI_AT_TOTAL) {
				if (age2_type == SI_AT_BREAST) {
					/* convert to breast-height age */
					returnValue = age1 - y2bh;
					if (returnValue < 0) {
						returnValue = 0;
					}
					return returnValue;
				}
				throw new AgeTypeErrorException("Unkown age type provided");
			}
		}
		throw new AgeTypeErrorException("Unkown age type provided");
	}

}
