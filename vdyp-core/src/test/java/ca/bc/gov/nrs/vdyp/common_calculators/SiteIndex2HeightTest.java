package ca.bc.gov.nrs.vdyp.common_calculators;

import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

import org.junit.jupiter.api.*;

class SiteIndex2HeightTest {
	// Taken from sindex.h
	/*
	 * age types
	 */
	private static final short SI_AT_TOTAL = 0;
	private static final short SI_AT_BREAST = 1;

	/*
	 * site index estimation (from height and age) types
	 */
	private static final short SI_EST_DIRECT = 1;

	/*
	 * error codes as return values from functions
	 */
	private static final short SI_ERR_NO_ANS = -4;

	/* define species and equation indices */
	private static final short SI_ACB_HUANGAC = 97;
	private static final short SI_ACB_HUANG = 0;
	private static final short SI_ACT_THROWERAC = 103;
	private static final short SI_ACT_THROWER = 1;
	private static final short SI_AT_CHEN = 74;
	private static final short SI_AT_CIESZEWSKI = 3;
	private static final short SI_AT_GOUDIE = 4;
	private static final short SI_AT_HUANG = 2;
	private static final short SI_AT_NIGH = 92;
	private static final short SI_BA_DILUCCA = 5;
	private static final short SI_BA_KURUCZ82AC = 102;
	private static final short SI_BA_KURUCZ82 = 8;
	private static final short SI_BA_KURUCZ86 = 7;
	private static final short SI_BA_NIGHGI = 117;
	private static final short SI_BA_NIGH = 118;
	private static final short SI_BL_CHENAC = 93;
	private static final short SI_BL_CHEN = 73;
	private static final short SI_BL_KURUCZ82 = 10;
	private static final short SI_BL_THROWERGI = 9;
	private static final short SI_BP_CURTISAC = 94;
	private static final short SI_BP_CURTIS = 78;
	private static final short SI_CWC_BARKER = 12;
	private static final short SI_CWC_KURUCZAC = 101;
	private static final short SI_CWC_KURUCZ = 11;
	private static final short SI_CWC_NIGH = 122;
	private static final short SI_CWI_NIGH = 77;
	private static final short SI_CWI_NIGHGI = 84;
	private static final short SI_DR_HARRING = 14;
	private static final short SI_DR_NIGH = 13;
	private static final short SI_EP_NIGH = 116;
	private static final short SI_FDC_BRUCEAC = 100;
	private static final short SI_FDC_BRUCE = 16;
	private static final short SI_FDC_BRUCENIGH = 89;
	private static final short SI_FDC_COCHRAN = 17;
	private static final short SI_FDC_KING = 18;
	private static final short SI_FDC_NIGHGI = 15;
	private static final short SI_FDC_NIGHTA = 88;
	private static final short SI_FDI_HUANG_NAT = 21;
	private static final short SI_FDI_HUANG_PLA = 20;
	private static final short SI_FDI_MILNER = 22;
	private static final short SI_FDI_MONS_DF = 26;
	private static final short SI_FDI_MONS_GF = 27;
	private static final short SI_FDI_MONS_SAF = 30;
	private static final short SI_FDI_MONS_WH = 29;
	private static final short SI_FDI_MONS_WRC = 28;
	private static final short SI_FDI_NIGHGI = 19;
	private static final short SI_FDI_THROWERAC = 96;
	private static final short SI_FDI_THROWER = 23;
	private static final short SI_FDI_VDP_MONT = 24;
	private static final short SI_FDI_VDP_WASH = 25;
	private static final short SI_HM_MEANSAC = 95;
	private static final short SI_HM_MEANS = 86;
	private static final short SI_HWC_BARKER = 33;
	private static final short SI_HWC_FARR = 32;
	private static final short SI_HWC_NIGHGI = 31;
	private static final short SI_HWC_NIGHGI99 = 79;
	private static final short SI_HWC_WILEYAC = 99;
	private static final short SI_HWC_WILEY = 34;
	private static final short SI_HWC_WILEY_BC = 35;
	private static final short SI_HWC_WILEY_MB = 36;
	private static final short SI_HWI_NIGH = 37;
	private static final short SI_HWI_NIGHGI = 38;
	private static final short SI_LW_MILNER = 39;
	private static final short SI_LW_NIGH = 90;
	private static final short SI_LW_NIGHGI = 82;
	private static final short SI_PJ_HUANG = 113;
	private static final short SI_PJ_HUANGAC = 114;
	private static final short SI_PLI_CIESZEWSKI = 47;
	private static final short SI_PLI_DEMPSTER = 50;
	private static final short SI_PLI_GOUDIE_DRY = 48;
	private static final short SI_PLI_GOUDIE_WET = 49;
	private static final short SI_PLI_HUANG_NAT = 44;
	private static final short SI_PLI_HUANG_PLA = 43;
	private static final short SI_PLI_MILNER = 46;
	private static final short SI_PLI_NIGHGI97 = 42;
	private static final short SI_PLI_NIGHTA98 = 41;
	private static final short SI_PLI_THROWER = 45;
	private static final short SI_PLI_THROWNIGH = 40;
	private static final short SI_PL_CHEN = 76;
	private static final short SI_PW_CURTISAC = 98;
	private static final short SI_PW_CURTIS = 51;
	private static final short SI_PY_HANNAC = 104;
	private static final short SI_PY_HANN = 53;
	private static final short SI_PY_MILNER = 52;
	private static final short SI_PY_NIGH = 107;
	private static final short SI_PY_NIGHGI = 108;
	private static final short SI_SB_CIESZEWSKI = 55;
	private static final short SI_SB_DEMPSTER = 57;
	private static final short SI_SB_HUANG = 54;
	private static final short SI_SB_KER = 56;
	private static final short SI_SB_NIGH = 91;
	private static final short SI_SE_CHENAC = 105;
	private static final short SI_SE_CHEN = 87;
	private static final short SI_SE_NIGHGI = 120;
	private static final short SI_SE_NIGH = 121;
	private static final short SI_SS_BARKER = 62;
	private static final short SI_SS_FARR = 61;
	private static final short SI_SS_GOUDIE = 60;
	private static final short SI_SS_NIGH = 59;
	private static final short SI_SS_NIGHGI = 58;
	private static final short SI_SS_NIGHGI99 = 80;
	private static final short SI_SW_CIESZEWSKI = 67;
	private static final short SI_SW_DEMPSTER = 72;
	private static final short SI_SW_GOUDIE_NAT = 71;
	private static final short SI_SW_GOUDIE_NATAC = 106;
	private static final short SI_SW_GOUDIE_PLA = 70;
	private static final short SI_SW_GOUDIE_PLAAC = 112;
	private static final short SI_SW_GOUDNIGH = 85;
	private static final short SI_SW_HU_GARCIA = 119;
	private static final short SI_SW_HUANG_NAT = 65;
	private static final short SI_SW_HUANG_PLA = 64;
	private static final short SI_SW_KER_NAT = 69;
	private static final short SI_SW_KER_PLA = 68;
	private static final short SI_SW_NIGHGI = 63;
	private static final short SI_SW_NIGHGI99 = 81;
	private static final short SI_SW_NIGHGI2004 = 115;
	private static final short SI_SW_NIGHTA = 83;
	private static final short SI_SW_THROWER = 66;

	/* not used, but must be defined for array positioning */
	private static final short SI_BB_KER = 6;
	private static final short SI_DR_CHEN = 75;
	private static final short SI_PLI_NIGHTA2004 = 109;
	private static final short SI_SE_NIGHTA = 110;
	private static final short SI_SW_NIGHTA2004 = 111;

	private static final double ERROR_TOLERANCE = 0.00001;

	@Test
	void testPpowPositive() {
		assertThat(8.0, closeTo(SiteIndex2Height.ppow(2.0, 3.0), ERROR_TOLERANCE));
		assertThat(1.0, closeTo(SiteIndex2Height.ppow(5.0, 0.0), ERROR_TOLERANCE));
	}

	@Test
	void testPpowZero() {
		assertThat(0.0, closeTo(SiteIndex2Height.ppow(0.0, 3.0), ERROR_TOLERANCE));
	}

	@Test
	void testLlogPositive() {
		assertThat(1.60943, closeTo(SiteIndex2Height.llog(5.0), ERROR_TOLERANCE));
		assertThat(11.51293, closeTo(SiteIndex2Height.llog(100000.0), ERROR_TOLERANCE));
	}

	@Test
	void testLlogZero() {
		assertThat(-11.51293, closeTo(SiteIndex2Height.llog(0.0), ERROR_TOLERANCE));
	}

	@Nested
	class Index_to_heightTest {

		@Test
		void testInvalidSiteIndex() {
			assertThrows(
					LessThan13Exception.class,
					() -> SiteIndex2Height.index_to_height((short) 0, 0.0, (short) 0, 1.2, 0.0, 0.0)
			);
		}

		@Test
		void testInvalidTage() {
			double expectedResult = 0;
			double actualResult = SiteIndex2Height.index_to_height(SI_BP_CURTISAC, 0.0, SI_AT_TOTAL, 1.31, 0.0, 0.0);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			assertThrows(
					NoAnswerException.class,
					() -> SiteIndex2Height.index_to_height(SI_CWC_KURUCZAC, (short) -1, SI_AT_TOTAL, 1.31, 0.0, 0.0)
			);
		}

		@Test
		void testSI_FDC_COCHRAN() {
			double actualResult = SiteIndex2Height.index_to_height(SI_FDC_COCHRAN, 0.0, SI_AT_BREAST, 1.31, 1.0, 0.0);
			double expectedResult = 1.37;
			double site_index = 1.31;

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			actualResult = SiteIndex2Height.index_to_height(SI_FDC_COCHRAN, 1.0, SI_AT_BREAST, site_index, 1.0, 0.0);
			site_index /= 0.3048;
			double x1 = Math.exp(-0.37496 + 1.36164 * Math.log(1) - 0.00243434 * SiteIndex2Height.ppow(Math.log(1), 4));
			double x2 = -0.2828 + 1.87947 * SiteIndex2Height.ppow(1 - Math.exp(-0.022399 * 1), 0.966998);
			expectedResult = 4.5 + x1 - x2 * (79.97 - (site_index - 4.5));
			expectedResult *= 0.3048;

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_FDC_KINGBhageLessThanZero() {
			double actualResult = SiteIndex2Height.index_to_height(SI_FDC_KING, 0.0, SI_AT_BREAST, 1.31, 1.0, 0.0);
			double expectedResult = 1.37;

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE)); // bhage <= 0
		}

		@Test
		void testSI_FDC_KINGBhageLessThanFive() {
			double site_index = 1.31;
			double actualResult = SiteIndex2Height
					.index_to_height(SI_FDC_KING, 1.0, SI_AT_BREAST, site_index, 1.0, 0.0);

			site_index /= 0.3048;
			double x1 = 2500 / (site_index - 4.5);
			double x2 = -0.954038 + 0.109757 * x1;
			double x3 = 0.0558178 + 0.00792236 * x1;
			double x4 = -0.000733819 + 0.000197693 * x1;

			double expectedResult = 4.5 + 1 / (x2 + x3 + x4);
			expectedResult += 0.22;
			expectedResult *= 0.3048;

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE)); // bhage < 5
		}

		@Test
		void testSI_FDC_KINGBhageGreaterThanFive() {
			double site_index = 1.31;

			double actualResult = SiteIndex2Height
					.index_to_height(SI_FDC_KING, 5.0, SI_AT_BREAST, site_index, 1.0, 0.0);

			site_index /= 0.3048;
			double x1 = 2500 / (site_index - 4.5);
			double x2 = -0.954038 + 0.109757 * x1;
			double x3 = 0.0558178 + 0.00792236 * x1;
			double x4 = -0.000733819 + 0.000197693 * x1;

			double expectedResult = 4.5 + 25 / (x2 + x3 * 5 + x4 * 25);
			expectedResult += (2.2 - 0.22 * 5);
			expectedResult *= 0.3048;

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE)); // bhage >= 5
		}

		@Test
		void testSI_HWC_FARRBhageGreaterThanZero() {
			double site_index = 1.31;

			double actualResult = SiteIndex2Height
					.index_to_height(SI_HWC_FARR, 5.0, SI_AT_BREAST, site_index, 1.0, 0.0);

			site_index /= 0.3048;
			double x2 = 0.3621734 + 1.149181 * Math.log(5) - 0.005617852 * SiteIndex2Height.ppow(Math.log(5), 3.0)
					- 7.267547E-6 * SiteIndex2Height.ppow(Math.log(5), 7.0)
					+ 1.708195E-16 * SiteIndex2Height.ppow(Math.log(5), 22.0)
					- 2.482794E-22 * SiteIndex2Height.ppow(Math.log(5), 30.0);

			double x3 = -2.146617 - 0.109007 * Math.log(5) + 0.0994030 * SiteIndex2Height.ppow(Math.log(5), 3.0)
					- 0.003853396 * SiteIndex2Height.ppow(Math.log(5), 5.0)
					+ 1.193933E-8 * SiteIndex2Height.ppow(Math.log(5), 12.0)
					- 9.486544E-20 * SiteIndex2Height.ppow(Math.log(5), 27.0)
					+ 1.431925E-26 * SiteIndex2Height.ppow(Math.log(5), 36.0);

			double expectedResult = 4.5 + Math.exp(x2) - Math.exp(x3) * (83.20 - (site_index - 4.5));
			expectedResult *= 0.3048;

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_HWC_FARRBhageLessThanZero() { // LessThanOrEqual is meant but omitted
			double actualResult = SiteIndex2Height.index_to_height(SI_HWC_FARR, 0.0, SI_AT_BREAST, 1.31, 2.0, 0.0);
			double expectedResult = 1.37;

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE)); // bhage <= 0
		}

		@Test
		void testSI_HWC_BARKER() {
			double actualResult = SiteIndex2Height.index_to_height(SI_HWC_BARKER, 0.0, SI_AT_BREAST, 1.31, 1.0, 0.0);
			double expectedResult = Math.exp(4.35753) * SiteIndex2Height.ppow(
					(-10.45 + 1.30049 * 1.31 - 0.0022 * 1.7161) / Math.exp(4.35753),
					SiteIndex2Height.ppow(50.0, 0.756313)
			);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_HM_MEANSBhageGreaterThanZero() {
			double site_index = 1.31;
			double actualResult = SiteIndex2Height
					.index_to_height(SI_HM_MEANS, 5.0, SI_AT_BREAST, site_index, 1.0, 0.0);

			site_index = -1.73 + 3.149 * SiteIndex2Height.ppow(site_index, 0.8279);
			double expectedResult = 1.37 + (22.87 + 0.9502 * (site_index - 1.37)) * SiteIndex2Height.ppow(
					1 - Math.exp(-0.0020647 * SiteIndex2Height.ppow(site_index - 1.37, 0.5) * 5),
					1.3656 + 2.046 / (site_index - 1.37)
			);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_HM_MEANSBhageLessThanZero() {
			double actualResult = SiteIndex2Height.index_to_height(SI_HM_MEANS, 0.0, SI_AT_BREAST, 1.31, 1.0, 0.0);
			double expectedResult = 1.37;

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_HM_MEANSACBhageGreaterThanHalf() {
			double site_index = 1.31;

			double actualResult = SiteIndex2Height
					.index_to_height(SI_HM_MEANSAC, 5.0, SI_AT_BREAST, site_index, 1.0, 0.0);

			site_index = -1.73 + 3.149 * SiteIndex2Height.ppow(site_index, 0.8279);
			double expectedResult = 1.37 + (22.87 + 0.9502 * (site_index - 1.37)) * SiteIndex2Height.ppow(
					1 - Math.exp(-0.0020647 * SiteIndex2Height.ppow(site_index - 1.37, 0.5) * 5),
					1.3656 + 2.046 / (site_index - 1.37)
			);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_HM_MEANSACBhageLessThanHalf() {
			double actualResult = SiteIndex2Height.index_to_height(SI_HM_MEANSAC, 0.0, SI_AT_BREAST, 1.31, 1.5, 0.0);
			double expectedResult = 1.37 / 1.5 / 1.5;

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_HWC_WILEYBHAGEZero() {
			double site_index = 1.31;

			double actualResult = SiteIndex2Height
					.index_to_height(SI_HWC_WILEY, 0.0, SI_AT_BREAST, site_index, 1.0, 0.0);

			double expectedResult = 1.37;

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_HWC_WILEYBHAGELargeSite() {
			double site_index = 80;

			double actualResult = SiteIndex2Height
					.index_to_height(SI_HWC_WILEY, 1.0, SI_AT_BREAST, site_index, 1.0, 0.0);

			double x1 = 20 / 1.667 + 0.1;
			double expectedFunctionResult = SiteIndex2Height
					.index_to_height(SI_HWC_WILEY, x1, SI_AT_BREAST, site_index, 1.0, 0.0);

			double expectedResult = 1.37 + (expectedFunctionResult - 1.37) / x1;

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_HWC_WILEY_BCBHAGEZero() {
			double site_index = 1.31;

			double actualResult = SiteIndex2Height
					.index_to_height(SI_HWC_WILEY_BC, 0.0, SI_AT_BREAST, site_index, 1.0, 0.0);

			double expectedResult = 1.37;

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_HWC_WILEY_BCBHAGELargeSite() {
			double site_index = 80;

			double actualResult = SiteIndex2Height
					.index_to_height(SI_HWC_WILEY_BC, 1.0, SI_AT_BREAST, site_index, 1.0, 0.0);

			double x1 = 20 / 1.667 + 0.1;
			double expectedFunctionResult = SiteIndex2Height
					.index_to_height(SI_HWC_WILEY_BC, x1, SI_AT_BREAST, site_index, 1.0, 0.0);

			double expectedResult = 1.37 + (expectedFunctionResult - 1.37) / x1;

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_HWC_WILEY_MBBHAGEZero() {
			double site_index = 1.31;

			double actualResult = SiteIndex2Height
					.index_to_height(SI_HWC_WILEY_MB, 0.0, SI_AT_BREAST, site_index, 1.0, 0.0);

			double expectedResult = 1.37;

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_HWC_WILEY_MBBHAGELargeSite() {
			double site_index = 80;

			double actualResult = SiteIndex2Height
					.index_to_height(SI_HWC_WILEY_MB, 1.0, SI_AT_BREAST, site_index, 1.0, 0.0);

			double x1 = 20 / 1.667 + 0.1;
			double expectedFunctionResult = SiteIndex2Height
					.index_to_height(SI_HWC_WILEY_MB, x1, SI_AT_BREAST, site_index, 1.0, 0.0);

			double expectedResult = 1.37 + (expectedFunctionResult - 1.37) / x1;

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_HWC_WILEYACBHAGELessThanPI() {
			double site_index = 1.31;

			double actualResult = SiteIndex2Height
					.index_to_height(SI_HWC_WILEYAC, 0.5, SI_AT_BREAST, site_index, 1.0, 0.6);

			double expectedResult = 1.37;

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_HWC_WILEYACBHAGELargeSite() {
			double site_index = 80;

			double actualResult = SiteIndex2Height
					.index_to_height(SI_HWC_WILEYAC, 1.0, SI_AT_BREAST, site_index, 1.0, 0.0);

			double x1 = 20 / 1.667 + 0.1;
			double expectedFunctionResult = SiteIndex2Height
					.index_to_height(SI_HWC_WILEYAC, x1, SI_AT_BREAST, site_index, 1.0, 0.0);

			double expectedResult = 1.37 + (expectedFunctionResult - 1.37) / x1;

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_BP_CURTISBhageLessThanZero() {
			double actualResult = SiteIndex2Height.index_to_height(SI_BP_CURTIS, 0.0, SI_AT_BREAST, 1.31, 1.0, 0.0);
			double expectedResult = 1.37;

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_BP_CURTISBhageGreaterThanHalf() {
			double site_index = 6;
			double bhage = 5;

			double actualResult = SiteIndex2Height
					.index_to_height(SI_BP_CURTIS, bhage, SI_AT_BREAST, site_index, 1.0, 0.0);

			site_index /= 0.3048;
			double x1 = Math.log(site_index - 4.5) + 1.649871 * (Math.log(bhage) - Math.log(50))
					+ 0.147245 * Math.pow(Math.log(bhage) - Math.log(50), 2.0);

			double x2 = 1.0 + 0.164927 * (Math.log(bhage) - Math.log(50))
					+ 0.052467 * Math.pow(Math.log(bhage) - Math.log(50), 2.0);

			double expectedResult = (4.5 + Math.exp(x1 / x2));
			expectedResult *= 0.3048;

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_BP_CURTISACBhageLessThanZero() {
			double actualResult = SiteIndex2Height.index_to_height(SI_BP_CURTISAC, 0.5, SI_AT_BREAST, 1.31, 1.0, 0.6);
			double expectedResult = 1.37;

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_BP_CURTISACBhageGreaterThanHalf() {
			double site_index = 6;
			double bhage = 5;

			double actualResult = SiteIndex2Height
					.index_to_height(SI_BP_CURTISAC, bhage, SI_AT_BREAST, site_index, 1.0, 0.0);

			site_index /= 0.3048;
			double x1 = Math.log(site_index - 4.5) + 1.649871 * (Math.log(bhage - 0.5) - Math.log(49.5))
					+ 0.147245 * Math.pow(Math.log(bhage - 0.5) - Math.log(49.5), 2.0);

			double x2 = 1.0 + 0.164927 * (Math.log(bhage - 0.5) - Math.log(49.5))
					+ 0.052467 * Math.pow(Math.log(bhage - 0.5) - Math.log(49.5), 2.0);

			double expectedResult = (4.5 + Math.exp(x1 / x2));
			expectedResult *= 0.3048;

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_SW_GOUDNIGHBhageGreaterThanHalf() {
			double site_index = 18.0;
			double bhage = 1.0;
			double y2bh = 1.5;

			double expectedResult = (1.0
					+ Math.exp(9.7936 - 1.2866 * SiteIndex2Height.llog(site_index - 1.3) - 1.4661 * Math.log(49.5)))
					/ (1.0 + Math.exp(
							9.7936 - 1.2866 * SiteIndex2Height.llog(site_index - 1.3) - 1.4661 * Math.log(bhage - 0.5)
					));
			expectedResult = 1.3 + (site_index - 1.3) * expectedResult;

			double actualResult = SiteIndex2Height
					.index_to_height(SI_SW_GOUDNIGH, bhage, SI_AT_BREAST, site_index, y2bh, 0.0);
			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_BA_NIGHGI() {
			double site_index = 1.31;

			double expectedResult = SiteIndex2Height.gi_si2ht(SI_BA_NIGHGI, 5, site_index);

			double actualResult = SiteIndex2Height.index_to_height(SI_BA_NIGHGI, 5, SI_AT_BREAST, site_index, 1.5, 0.0);
			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testDefaultSwitchStatement() {
			assertThrows(
					CurveErrorException.class,
					() -> SiteIndex2Height.index_to_height((short) 300, 5, SI_AT_BREAST, 1.31, 1.5, 0.0)
			);
		}

	}

	@Nested
	class testGiSi2Ht {
		@Test
		void testBhageLessThanHalf() {
			assertThrows(GrowthInterceptMinimumException.class, () -> SiteIndex2Height.gi_si2ht((short) 0, 0.0, 0.0));
		}

		@Test
		void testValidInput() {
			double actualResult = SiteIndex2Height.gi_si2ht(SI_FDC_COCHRAN, 1, 1.31);

			double expectedResult = 1.965; 

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testConvergenceError() {
			assertThrows(NoAnswerException.class, () -> {
				SiteIndex2Height.gi_si2ht(SI_FDC_COCHRAN, 5.0, 2000.0);
			});
		}

		@Test
		void testNegativeSiteIndex() {
			double actualResult = SiteIndex2Height.gi_si2ht((short) 1, 3.0, -5.0);
			assertThat(actualResult, closeTo(1.3, ERROR_TOLERANCE));
		}

	}

	@Test
	void testHuGarciaQ() { // the way I've done these tests is to validate them with the original C code
							// and
							// compare them with the output of the java code
		// Test case 1
		double siteIndex1 = 20.0;
		double bhAge1 = 30.0;
		double expectedQ1 = 0.028228; // from running the C code
		double resultQ1 = SiteIndex2Height.hu_garcia_q(siteIndex1, bhAge1);

		assertThat(expectedQ1, closeTo(resultQ1, ERROR_TOLERANCE));

		// Test case 2
		double siteIndex2 = 25.0;
		double bhAge2 = 40.0;
		double expectedQ2 = 0.027830; // from running the C code
		double resultQ2 = SiteIndex2Height.hu_garcia_q(siteIndex2, bhAge2);

		assertThat(expectedQ2, closeTo(resultQ2, ERROR_TOLERANCE));
	}

	@Test
	void testHuGarciaH() {
		double q = 05;
		double bhage = 10.0;

		double expectedResult = (283.9 * Math.pow(q, 0.5137)) * Math.pow(
				1 - (1 - Math.pow(1.3 / (283.9 * Math.pow(q, 0.5137)), 0.5829)) * Math.exp(-q * (bhage - 0.5)), 1.71556
		);

		double actualResult = SiteIndex2Height.hu_garcia_h(q, bhage);

		assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
	}
}
