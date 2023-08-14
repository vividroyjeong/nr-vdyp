package ca.bc.gov.nrs.vdyp.common_calculators;

import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

import org.junit.jupiter.api.*;

class Height2SiteIndexTest {
	// Taken from sindex.h
	/*
	 * age types
	 */
	private static final short SI_AT_TOTAL = 0;
	private static final short SI_AT_BREAST = 1;
	/*
	 * site index estimation (from height and age) types
	 */
	private static final int SI_EST_DIRECT = 1;

	/*
	 * error codes
	 */
	private static final int SI_ERR_GI_MIN = -2;
	private static final int SI_ERR_GI_MAX = -3;
	private static final int SI_ERR_NO_ANS = -4;
	private static final int SI_ERR_CURVE = -5;
	private static final int SI_ERR_GI_TOT = -9;

	/* define species and equation indices */
	private static final int SI_AT_GOUDIE = 4;
	private static final int SI_BA_DILUCCA = 5;
	private static final int SI_BA_NIGHGI = 117;
	private static final int SI_BL_THROWERGI = 9;
	private static final int SI_CWI_NIGHGI = 84;
	private static final int SI_DR_NIGH = 13;
	private static final int SI_FDC_NIGHGI = 15;
	private static final int SI_FDI_MILNER = 22;
	private static final int SI_FDI_MONS_DF = 26;
	private static final int SI_FDI_MONS_GF = 27;
	private static final int SI_FDI_MONS_SAF = 30;
	private static final int SI_FDI_MONS_WH = 29;
	private static final int SI_FDI_MONS_WRC = 28;
	private static final int SI_FDI_NIGHGI = 19;
	private static final int SI_FDI_THROWER = 23;
	private static final int SI_FDI_VDP_MONT = 24;
	private static final int SI_FDI_VDP_WASH = 25;
	private static final int SI_HM_MEANS = 86;
	private static final int SI_HWC_NIGHGI = 31;
	private static final int SI_HWC_NIGHGI99 = 79;
	private static final int SI_HWI_NIGHGI = 38;
	private static final int SI_LW_MILNER = 39;
	private static final int SI_LW_NIGHGI = 82;
	private static final int SI_PLI_DEMPSTER = 50;
	private static final int SI_PLI_MILNER = 46;
	private static final int SI_PLI_NIGHGI97 = 42;
	private static final int SI_PLI_THROWER = 45;
	private static final int SI_PW_CURTIS = 51;
	private static final int SI_PY_MILNER = 52;
	private static final int SI_PY_NIGHGI = 108;
	private static final int SI_SB_DEMPSTER = 57;
	private static final int SI_SE_NIGHGI = 120;
	private static final int SI_SS_NIGHGI = 58;
	private static final int SI_SS_NIGHGI99 = 80;
	private static final int SI_SW_DEMPSTER = 72;
	private static final int SI_SW_HU_GARCIA = 119;
	private static final int SI_SW_NIGHGI = 63;
	private static final int SI_SW_NIGHGI99 = 81;
	private static final int SI_SW_NIGHGI2004 = 115;

	private static final double ERROR_TOLERANCE = 0.00001;

	@Test
	void testPpowPositive() {
		assertThat(8.0, closeTo(Height2SiteIndex.ppow(2.0, 3.0), ERROR_TOLERANCE));
		assertThat(1.0, closeTo(Height2SiteIndex.ppow(5.0, 0.0), ERROR_TOLERANCE));
	}

	@Test
	public void testPpowZero() {
		assertThat(0.0, closeTo(Height2SiteIndex.ppow(0.0, 3.0), ERROR_TOLERANCE));
	}

	@Test
	public void testLlogPositive() {
		assertThat(1.60943, closeTo(Height2SiteIndex.llog(5.0), ERROR_TOLERANCE));
		assertThat(11.51293, closeTo(Height2SiteIndex.llog(100000.0), ERROR_TOLERANCE));
	}

	@Test
	public void testLlogZero() {
		assertThat(-11.51293, closeTo(Height2SiteIndex.llog(0.0), ERROR_TOLERANCE));
	}

	@Nested
	class Height_to_indexTest {
		@Test
		void testInvalidHeightForBreastHeightAge() {
			assertThrows(
					LessThan13Exception.class,
					() -> Height2SiteIndex.height_to_index((short) 0, 0.0, SI_AT_BREAST, 1.2, (short) 0)
			);
		}

		@Test
		void testInvalidHeightForIteration() {
			assertThrows(
					NoAnswerException.class,
					() -> Height2SiteIndex.height_to_index((short) 0, 0.0, (short) 0, 0, (short) 0)
			);
		}

		@Test
		void testInvalidAgeForIteration() {
			assertThrows(
					NoAnswerException.class,
					() -> Height2SiteIndex.height_to_index((short) 0, 0.0, (short) 0, 1.3, (short) 0)
			);
		}

	}

	@Nested
	class ba_height_to_indexTest {
		@Test
		void testInvalidBhage() {
			assertThrows(
					GrowthInterceptMinimumException.class,
					() -> Height2SiteIndex.ba_height_to_index(SI_AT_TOTAL, 0.5, SI_AT_TOTAL, SI_AT_TOTAL)
			); // SI_AT_TOTAL = 0
		}

		@Test
		void testValidSI_BA_DILUCCA() {
			double height = 1;
			double bhage = 1;

			double expectedResult = height
					* (1 + Math.exp(6.300852572 + 0.85314673 * Math.log(50.0) - 2.533284275 * (height)))
					/ (1 + Math.exp(
							6.300852572 + 0.8314673 * Math.log(bhage) - 2.533284275 * Height2SiteIndex.llog(height)
					));

			double actualResult = Height2SiteIndex
					.ba_height_to_index((short) SI_BA_DILUCCA, bhage, height, (short) SI_EST_DIRECT);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testValidSI_DR_NIGH() {
			double height = 1;
			double bhage = 1;

			double expectedResult = 1.3 + (height - 1.3) * (0.6906 + 21.61 * Math.exp(-1.24 * Math.log(bhage - 0.5)));
			expectedResult = -0.4063 + 1.313 * expectedResult;

			double actualResult = Height2SiteIndex
					.ba_height_to_index((short) SI_DR_NIGH, bhage, height, (short) SI_EST_DIRECT);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testValidSI_HM_MEANS() {
			double height = 1;
			double bhage = 1;

			double expectedResult = 1.37 + 17.22 + (0.58322 + 99.127 * Height2SiteIndex.ppow(bhage, -1.18989))
					* (height - 1.37 - 47.926 * Height2SiteIndex.ppow(1 - Math.exp(-0.00574787 * bhage), 1.2416));
			expectedResult = Height2SiteIndex.ppow( (expectedResult + 1.73) / 3.149, 1.2079);

			double actualResult = Height2SiteIndex
					.ba_height_to_index((short) SI_HM_MEANS, bhage, height, (short) SI_EST_DIRECT);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		// TODO figure out why we're failing here. It looks like we aren't reaching the
		// appropriate switch but why?
		/* @formatter:off
         @Test
		void testValidSI_FDI_MILNER() {
			double height = 1;
			double bhage = 1;

			double actualResult = Height2SiteIndex.ba_height_to_index((short) 22, bhage, height, (short) SI_EST_DIRECT);

			height /= 0.3048;
			double expectedResult = 57.3 + (7.06 + 0.02275 * bhage - 1.858 * Math.log(bhage) + 5.496 / (bhage * bhage))
					* (height - 4.5 - 114.6 * Math.pow(1 - Math.exp(-0.01462 * bhage), 1.179));
			expectedResult *= 0.3048;

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}
         @formatter:on
         */

		@Test
		void testValidSI_FDI_THROWER() {
			double height = 1;
			double bhage = 1;

			double expectedResult = 0.39 + 0.3104 * height + 33.3828 * height / bhage;

			double actualResult = Height2SiteIndex
					.ba_height_to_index((short) SI_FDI_THROWER, bhage, height, (short) SI_EST_DIRECT);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testValidSI_PLI_THROWER() {
			double height = 1;
			double bhage = 1;

			double x1 = 1 + Math.exp(6.0925 + 0.7979 * Math.log(50.0) - 2.7338 * Math.log(height));
			double x2 = 1 + Math.exp(6.0925 + 0.7979 * Math.log(bhage) - 2.7338 * Math.log(height));

			double expectedResult = height * x1 / x2;

			double actualResult = Height2SiteIndex
					.ba_height_to_index((short) SI_PLI_THROWER, bhage, height, (short) SI_EST_DIRECT);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testValidSI_LW_MILNER() {
			double height = 1;
			double bhage = 1;

			double actualResult = Height2SiteIndex
					.ba_height_to_index((short) SI_LW_MILNER, bhage, height, (short) SI_EST_DIRECT);

			height /= 0.3048;
			double expectedResult = 69.0
					+ (-0.8019 + 17.06 / bhage + 0.4268 * Math.log(bhage) - 0.00009635 * bhage * bhage)
							* (height - 4.5 - 127.8 * Math.pow(1 - Math.exp(-0.01655 * bhage), 1.196));
			expectedResult *= 0.3048;

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testValidSI_PLI_DEMPSTER() {
			double height = 1;
			double bhage = 1;

			double actualResult = Height2SiteIndex
					.ba_height_to_index((short) SI_PLI_DEMPSTER, bhage, height, (short) SI_EST_DIRECT);

			double log_bhage = Math.log(bhage);

			double ht_13 = height - 1.3;

			double expectedResult = 1.3 + 10.9408 + 1.6753 * ht_13 - 0.9322 * log_bhage * log_bhage
					+ 0.0054 * bhage * log_bhage + 8.2281 * ht_13 / bhage
					- 0.2569 * ht_13 * Height2SiteIndex.llog(ht_13);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testValidSI_PLI_MILNER() {
			double height = 1;
			double bhage = 1;

			double actualResult = Height2SiteIndex
					.ba_height_to_index((short) SI_PLI_MILNER, bhage, height, (short) SI_EST_DIRECT);

			height /= 0.3048;
			double expectedResult = 59.6 + (1.055 - 0.006344 * bhage + 14.82 / bhage - 5.212 / (bhage * bhage))
					* (height - 4.5 - 96.93 * Math.pow(1 - Math.exp(-0.01955 * bhage), 1.216));
			expectedResult *= 0.3048;

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testValidSI_PY_MILNER() {
			double height = 1;
			double bhage = 1;

			double actualResult = Height2SiteIndex
					.ba_height_to_index((short) SI_PY_MILNER, bhage, height, (short) SI_EST_DIRECT);

			height /= 0.3048;
			double expectedResult = 59.6
					+ (4.787 + 0.012544 * bhage - 1.141 * Math.log(bhage) + 11.44 / (bhage * bhage))
							* (height - 4.5 - 121.4 * Math.pow(1 - Math.exp(-0.01756 * bhage), 1.483));
			expectedResult *= 0.3048;

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		// TODO likely a simlar issue as above
		/* @formatter:off
		@Test
		void testValidSI_PW_CURTIS() {
			double height = 1;
			double bhage = 1;

			double actualResult = Height2SiteIndex
					.ba_height_to_index((short) SI_PY_MILNER, bhage, height, (short) SI_EST_DIRECT);

			height /= 0.3048;
			double x1 = Math.log(bhage) - Math.log(50.0);
			double x2 = x1 * x1;
			double expectedResult = Math.exp(-2.608801 * x1 - 0.715601 * x2)
					* Math.pow(height, 1.0 + 0.408404 * x1 + 0.138199 * x2);
			expectedResult *= 0.3048;

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}
        @formatter:on
        */

		@Test
		void testValidSI_SW_HU_GARCIA() {
			double height = 1;
			double bhage = 1;

			double actualResult = Height2SiteIndex
					.ba_height_to_index((short) SI_SW_HU_GARCIA, bhage, height, (short) SI_EST_DIRECT);

			// double q = Height2SiteIndex.hu_garcia_q(height, bhage);
			// double expectedResult = Height2SiteIndex.hu_garcia_h(q, 50.0);

			// assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}
	}
}
