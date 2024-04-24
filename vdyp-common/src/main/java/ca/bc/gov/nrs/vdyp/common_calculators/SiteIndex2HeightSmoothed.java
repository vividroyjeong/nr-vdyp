package ca.bc.gov.nrs.vdyp.common_calculators;

import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.CommonCalculatorException;
import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.LessThan13Exception;
import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.NoAnswerException;

/**
 * SiteIndex2HeightSmoothed
 *
 * Defines {@code index_to_height_smoothed}
 */
public class SiteIndex2HeightSmoothed {
	// Taken from sindex

	/*
	 * age types
	 */
	private static final int SI_AT_TOTAL = 0;
	private static final int SI_AT_BREAST = 1;

	/* define species and equation indices */
	private static final int SI_FDC_BRUCEAC = 100;
	private static final int SI_HWC_WILEYAC = 99;
	private static final int SI_PLI_THROWER = 45;
	private static final int SI_SW_GOUDIE_NATAC = 106;
	private static final int SI_SW_GOUDIE_PLAAC = 112;

	/**
	 * @param cu_index
	 * @param iage
	 * @param age_type
	 * @param site_index
	 * @param y2bh
	 * @param seedling_age
	 * @param seedling_ht
	 * @return
	 * @throws LessThan13Exception when {@code site_index} is less than 1.3
	 * @throws NoAnswerException   when the iteration will not converge
	 */
	public static double indexToHeightSmoothed(
			int cu_index, double iage, int age_type, double site_index, double y2bh, double seedling_age,
			double seedling_ht
	) throws CommonCalculatorException {

		double result; // return value

		double tage; // total age
		double bhage; // breast-height age
		double pi; // proportion of height growth between breast height
					// ages 0 and 1 that occurs below breast height

		if (site_index < 1.3) {
			throw new LessThan13Exception("Site index < 1.3m: " + site_index);
		}

		if (y2bh < 0) {
			throw new NoAnswerException("Iteration could not converge (projected height > 999), y2bh: " + y2bh);
		}

		double itage = iage;
		if (age_type == SI_AT_BREAST) {
			itage = iage + y2bh;
		}
		if (itage < 0.0) {
			throw new NoAnswerException("Iteration could not converge (projected height > 999), itage: " + itage);
		}
		if (itage < 0.00001) {
			return 0.0;
		}

		if (cu_index == SI_PLI_THROWER || cu_index == SI_SW_GOUDIE_PLAAC || cu_index == SI_SW_GOUDIE_NATAC
				|| cu_index == SI_FDC_BRUCEAC || cu_index == SI_HWC_WILEYAC) {
			pi = y2bh - (int) y2bh;
		} else {
			pi = 0.5;
		}

		double k0;
		double k1;

		bhage = 2;
		do {
			result = SiteIndex2Height.indexToHeight(cu_index, bhage, SI_AT_BREAST, site_index, y2bh, pi);
			if (result < 0) {
				return result;
			}
			tage = bhage + (int) y2bh;
			k1 = Math.log( (1.3 - seedling_ht) / (result - seedling_ht))
					/ Math.log( (y2bh - seedling_age) / (tage - seedling_age));
			if (k1 >= 1) {
				k0 = (1.3 - seedling_ht) / Math.pow(y2bh - seedling_age, k1);
				break;
			}
			bhage++;
			if (bhage >= 25) {
				throw new NoAnswerException(
						"Iteration could not converge (projected height > 999), bhage >= 25: " + bhage
				);
			}
		} while (true);

		if (seedling_age == 0) {
			if (itage <= tage) {
				result = k0 * Math.pow(itage, k1);
			} else {
				result = SiteIndex2Height.indexToHeight(cu_index, itage, SI_AT_TOTAL, site_index, y2bh, pi);
			}
		} else {
			if (itage < seedling_age) {
				result = seedling_ht / seedling_age * itage;
			} else if (itage < tage) {
				result = seedling_ht + k0 * Math.pow(itage - seedling_age, k1);
			} else {
				result = SiteIndex2Height.indexToHeight(cu_index, itage, SI_AT_TOTAL, site_index, y2bh, pi);
			}
		}

		return result;
	}
}
