package ca.bc.gov.nrs.vdyp.common_calculators;

import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.AgeTypeErrorException;
import static ca.bc.gov.nrs.vdyp.common_calculators.SiteIndexConstants.*;

/**
 * Given age and type, converts to other type of age.
 *
 * @throws AgeTypeErrorException if unnkown age type is provided
 */
public class Age2Age {

	@SuppressWarnings("java:S3776, java:S6541, java:S1479")
	public static double ageToAge(SiteIndexEquation cuIndex, double sourceAge, int sourceAgeType, int targetAgeType, double years2BreastHeight)
			throws AgeTypeErrorException {
		
		double returnValue;

		boolean shouldBranch;
		
		if (cuIndex == null) {
			shouldBranch = true;
		} else {
			switch (cuIndex) {
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
		}
		
		if (shouldBranch) {
			if (sourceAgeType == SI_AT_BREAST) {
				if (targetAgeType == SI_AT_TOTAL) {
					/* convert to total age */
					returnValue = sourceAge + years2BreastHeight - 0.5;
					if (returnValue < 0) {
						returnValue = 0;
					}
					return returnValue;
				}
				throw new AgeTypeErrorException("Unkown age type provided");
			}

			if (sourceAgeType == SI_AT_TOTAL) {
				if (targetAgeType == SI_AT_BREAST) {
					/* convert to breast-height age */
					returnValue = sourceAge - years2BreastHeight + 0.5;
					if (returnValue < 0) {
						returnValue = 0;
					}
					return returnValue;
				}
				throw new AgeTypeErrorException("Unkown age type provided");
			}
		} else {
			if (sourceAgeType == SI_AT_BREAST) {
				if (targetAgeType == SI_AT_TOTAL) {
					/* convert to total age */
					returnValue = sourceAge + years2BreastHeight;
					if (returnValue < 0) {
						returnValue = 0;
					}
					return returnValue;
				}
				throw new AgeTypeErrorException("Unkown age type provided");
			}

			if (sourceAgeType == SI_AT_TOTAL) {
				if (targetAgeType == SI_AT_BREAST) {
					/* convert to breast-height age */
					returnValue = sourceAge - years2BreastHeight;
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
