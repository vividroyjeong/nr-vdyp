package ca.bc.gov.nrs.vdyp.common_calculators;

import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

import org.junit.jupiter.api.*;

public class SiteIndexYears2BreastHeightTest {

	// Taken from sindex.h
	/* define species and equation indices */
	private static final int SI_ACB_HUANGAC = 97;
	private static final int SI_ACB_HUANG = 0;
	private static final int SI_ACT_THROWERAC = 103;
	private static final int SI_ACT_THROWER = 1;
	private static final int SI_AT_CHEN = 74;
	private static final int SI_AT_CIESZEWSKI = 3;
	private static final int SI_AT_GOUDIE = 4;
	private static final int SI_AT_HUANG = 2;
	private static final int SI_AT_NIGH = 92;
	private static final int SI_BA_DILUCCA = 5;
	private static final int SI_BA_KURUCZ82AC = 102;
	private static final int SI_BA_KURUCZ82 = 8;
	private static final int SI_BA_KURUCZ86 = 7;
	private static final int SI_BA_NIGHGI = 117;
	private static final int SI_BA_NIGH = 118;
	private static final int SI_BL_CHENAC = 93;
	private static final int SI_BL_CHEN = 73;
	private static final int SI_BL_KURUCZ82 = 10;
	private static final int SI_BL_THROWERGI = 9;
	private static final int SI_BP_CURTISAC = 94;
	private static final int SI_BP_CURTIS = 78;
	private static final int SI_CWC_BARKER = 12;
	private static final int SI_CWC_KURUCZAC = 101;
	private static final int SI_CWC_KURUCZ = 11;
	private static final int SI_CWC_NIGH = 122;
	private static final int SI_CWI_NIGH = 77;
	private static final int SI_CWI_NIGHGI = 84;
	private static final int SI_DR_HARRING = 14;
	private static final int SI_DR_NIGH = 13;
	private static final int SI_EP_NIGH = 116;
	private static final int SI_FDC_BRUCEAC = 100;
	private static final int SI_FDC_BRUCE = 16;
	private static final int SI_FDC_BRUCENIGH = 89;
	private static final int SI_FDC_COCHRAN = 17;
	private static final int SI_FDC_KING = 18;
	private static final int SI_FDC_NIGHGI = 15;
	private static final int SI_FDC_NIGHTA = 88;
	private static final int SI_FDI_HUANG_NAT = 21;
	private static final int SI_FDI_HUANG_PLA = 20;
	private static final int SI_FDI_MILNER = 22;
	private static final int SI_FDI_MONS_DF = 26;
	private static final int SI_FDI_MONS_GF = 27;
	private static final int SI_FDI_MONS_SAF = 30;
	private static final int SI_FDI_MONS_WH = 29;
	private static final int SI_FDI_MONS_WRC = 28;
	private static final int SI_FDI_NIGHGI = 19;
	private static final int SI_FDI_THROWERAC = 96;
	private static final int SI_FDI_THROWER = 23;
	private static final int SI_FDI_VDP_MONT = 24;
	private static final int SI_FDI_VDP_WASH = 25;
	private static final int SI_HM_MEANSAC = 95;
	private static final int SI_HM_MEANS = 86;
	private static final int SI_HWC_BARKER = 33;
	private static final int SI_HWC_FARR = 32;
	private static final int SI_HWC_NIGHGI = 31;
	private static final int SI_HWC_NIGHGI99 = 79;
	private static final int SI_HWC_WILEYAC = 99;
	private static final int SI_HWC_WILEY = 34;
	private static final int SI_HWC_WILEY_BC = 35;
	private static final int SI_HWC_WILEY_MB = 36;
	private static final int SI_HWI_NIGH = 37;
	private static final int SI_HWI_NIGHGI = 38;
	private static final int SI_LW_MILNER = 39;
	private static final int SI_LW_NIGH = 90;
	private static final int SI_LW_NIGHGI = 82;
	private static final int SI_PJ_HUANG = 113;
	private static final int SI_PJ_HUANGAC = 114;
	private static final int SI_PLI_CIESZEWSKI = 47;
	private static final int SI_PLI_DEMPSTER = 50;
	private static final int SI_PLI_GOUDIE_DRY = 48;
	private static final int SI_PLI_GOUDIE_WET = 49;
	private static final int SI_PLI_HUANG_NAT = 44;
	private static final int SI_PLI_HUANG_PLA = 43;
	private static final int SI_PLI_MILNER = 46;
	private static final int SI_PLI_NIGHGI97 = 42;
	private static final int SI_PLI_NIGHTA98 = 41;
	private static final int SI_PLI_THROWER = 45;
	private static final int SI_PLI_THROWNIGH = 40;
	private static final int SI_PL_CHEN = 76;
	private static final int SI_PW_CURTISAC = 98;
	private static final int SI_PW_CURTIS = 51;
	private static final int SI_PY_HANNAC = 104;
	private static final int SI_PY_HANN = 53;
	private static final int SI_PY_MILNER = 52;
	private static final int SI_PY_NIGH = 107;
	private static final int SI_PY_NIGHGI = 108;
	private static final int SI_SB_CIESZEWSKI = 55;
	private static final int SI_SB_DEMPSTER = 57;
	private static final int SI_SB_HUANG = 54;
	private static final int SI_SB_KER = 56;
	private static final int SI_SB_NIGH = 91;
	private static final int SI_SE_CHENAC = 105;
	private static final int SI_SE_CHEN = 87;
	private static final int SI_SE_NIGHGI = 120;
	private static final int SI_SE_NIGH = 121;
	private static final int SI_SS_BARKER = 62;
	private static final int SI_SS_FARR = 61;
	private static final int SI_SS_GOUDIE = 60;
	private static final int SI_SS_NIGH = 59;
	private static final int SI_SS_NIGHGI = 58;
	private static final int SI_SS_NIGHGI99 = 80;
	private static final int SI_SW_CIESZEWSKI = 67;
	private static final int SI_SW_DEMPSTER = 72;
	private static final int SI_SW_GOUDIE_NAT = 71;
	private static final int SI_SW_GOUDIE_NATAC = 106;
	private static final int SI_SW_GOUDIE_PLA = 70;
	private static final int SI_SW_GOUDIE_PLAAC = 112;
	private static final int SI_SW_GOUDNIGH = 85;
	private static final int SI_SW_HU_GARCIA = 119;
	private static final int SI_SW_HUANG_NAT = 65;
	private static final int SI_SW_HUANG_PLA = 64;
	private static final int SI_SW_KER_NAT = 69;
	private static final int SI_SW_KER_PLA = 68;
	private static final int SI_SW_NIGHGI = 63;
	private static final int SI_SW_NIGHGI99 = 81;
	private static final int SI_SW_NIGHGI2004 = 115;
	private static final int SI_SW_NIGHTA = 83;
	private static final int SI_SW_THROWER = 66;

	/* not used, but must be defined for array positioning */
	private static final int SI_BB_KER = 6;
	private static final int SI_DR_CHEN = 75;
	private static final int SI_PLI_NIGHTA2004 = 109;
	private static final int SI_SE_NIGHTA = 110;
	private static final int SI_SW_NIGHTA2004 = 111;

	private static final double ERROR_TOLERANCE = 0.00001;

	@Test
	void testPpowPositive() {
		assertThat(8.0, closeTo(SiteIndexYears2BreastHeight.ppow(2.0, 3.0), ERROR_TOLERANCE));
		assertThat(1.0, closeTo(SiteIndexYears2BreastHeight.ppow(5.0, 0.0), ERROR_TOLERANCE));
	}

	@Test
	public void testPpowZero() {
		assertThat(0.0, closeTo(SiteIndexYears2BreastHeight.ppow(0.0, 3.0), ERROR_TOLERANCE));
	}

	@Test
	public void testLlogPositive() {
		assertThat(1.60943, closeTo(SiteIndexYears2BreastHeight.llog(5.0), ERROR_TOLERANCE));
		assertThat(11.51293, closeTo(SiteIndexYears2BreastHeight.llog(100000.0), ERROR_TOLERANCE));
	}

	@Test
	public void testLlogZero() {
		assertThat(-11.51293, closeTo(SiteIndexYears2BreastHeight.llog(0.0), ERROR_TOLERANCE));
	}

	@Nested
	class si_y2bhTest {
		@Test
		void testSiteIndexInvalid() {
			assertThrows(LessThan13Exception.class, () -> SiteIndexYears2BreastHeight.si_y2bh((short) 0, 1));
		}

		@Test
		void testSI_FDC_NIGHGI() {
			assertThrows(
					GrowthInterceptTotalException.class,
					() -> SiteIndexYears2BreastHeight.si_y2bh((short) SI_FDC_NIGHGI, 2)
			);
		}

		@Test
		void testSI_FDC_BRUCE() {
			short cu_index = SI_FDC_BRUCE;
			double site_index = 1.3;// check normal case

			double expectedResult = 13.25 - site_index / 6.096;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 13.25 * 6.096; // check y2bh < 1 case
			expectedResult = 1;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_FDC_BRUCEAC() {
			short cu_index = SI_FDC_BRUCEAC;
			double site_index = 1.3;// check normal case

			double expectedResult = 13.25 - site_index / 6.096;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 13.25 * 6.096; // check y2bh < 1 case
			expectedResult = 1;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_FDC_NIGHTA() {
			short cu_index = SI_FDC_NIGHTA;
			double site_index = 10;// check normal case

			double expectedResult = 24.44 * Math.pow(site_index - 9.051, -0.394);
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			// check site index <= 9.051 case
			assertThrows(NoAnswerException.class, () -> SiteIndexYears2BreastHeight.si_y2bh(cu_index, 2));

		}

		@Test
		void testSI_FDC_BRUCENIGH() {
			short cu_index = SI_FDC_BRUCENIGH;
			double site_index = 15;// check normal case

			double expectedResult = 13.25 - site_index / 6.096;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);
			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 16; // check site index <= 15 case
			expectedResult = 36.5818 * Math.pow(site_index - 6.6661, -0.5526);
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);
			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_FDC_COCHRAN() {
			short cu_index = SI_FDC_COCHRAN;
			double site_index = 1.3;// check normal case

			double expectedResult = 13.25 - site_index / 6.096;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 13.25 * 6.096; // check y2bh < 1 case
			expectedResult = 1;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_FDC_KING() {
			short cu_index = SI_FDC_KING;
			double site_index = 1.3;// check normal case

			double expectedResult = 13.25 - site_index / 6.096;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 13.25 * 6.096; // check y2bh < 1 case
			expectedResult = 1;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_HWC_FARR() {
			short cu_index = SI_HWC_FARR;
			double site_index = 1.3;// check normal case

			double expectedResult = 13.25 - site_index / 6.096;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 13.25 * 6.096; // check y2bh < 1 case
			expectedResult = 1;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_HWC_BARKER() {
			short cu_index = SI_HWC_BARKER;
			double site_index = 1.3;// check normal case

			double expectedResult = -5.2 + 410.00 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 410; // check y2bh < 1 case
			expectedResult = 1;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_HM_MEANS() {
			short cu_index = SI_HM_MEANS;
			double site_index = 1.3;// check normal case

			double expectedResult = 9.43 - site_index / 7.088;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 410; // check y2bh < 1 case
			expectedResult = 1;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_HM_MEANSAC() {
			short cu_index = SI_HM_MEANSAC;
			double site_index = 1.3;// check normal case

			double expectedResult = 9.43 - site_index / 7.088;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 410; // check y2bh < 1 case
			expectedResult = 1;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_HWI_NIGH() {
			short cu_index = SI_HWI_NIGH;
			double site_index = 1.3;// check normal case

			double expectedResult = 446.6 * Height2SiteIndex.ppow(site_index, -1.432);
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 410; // check y2bh < 1 case
			expectedResult = 1;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_HWI_NIGHGI() {
			assertThrows(
					GrowthInterceptTotalException.class,
					() -> SiteIndexYears2BreastHeight.si_y2bh((short) SI_HWI_NIGHGI, 2)
			);
		}

		@Test
		void testSI_HWC_NIGHGI() {
			assertThrows(
					GrowthInterceptTotalException.class,
					() -> SiteIndexYears2BreastHeight.si_y2bh((short) SI_HWC_NIGHGI, 2)
			);
		}

		@Test
		void testSI_HWC_NIGHGI99() {
			assertThrows(
					GrowthInterceptTotalException.class,
					() -> SiteIndexYears2BreastHeight.si_y2bh((short) SI_HWC_NIGHGI99, 2)
			);
		}

		@Test
		void testSI_SS_NIGHGI99() {
			assertThrows(
					GrowthInterceptTotalException.class,
					() -> SiteIndexYears2BreastHeight.si_y2bh((short) SI_SS_NIGHGI99, 2)
			);
		}

		@Test
		void testSI_SW_NIGHGI99() {
			assertThrows(
					GrowthInterceptTotalException.class,
					() -> SiteIndexYears2BreastHeight.si_y2bh((short) SI_SW_NIGHGI99, 2)
			);
		}

		@Test
		void testSI_SW_NIGHGI2004() {
			assertThrows(
					GrowthInterceptTotalException.class,
					() -> SiteIndexYears2BreastHeight.si_y2bh((short) SI_SW_NIGHGI2004, 2)
			);
		}

		@Test
		void testSI_LW_NIGHGI() {
			assertThrows(
					GrowthInterceptTotalException.class,
					() -> SiteIndexYears2BreastHeight.si_y2bh((short) SI_LW_NIGHGI, 2)
			);
		}

		@Test
		void testSI_HWC_WILEY() {
			short cu_index = SI_HWC_WILEY;
			double site_index = 1.3;// check normal case

			double expectedResult = 9.43 - site_index / 7.088;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 410; // check y2bh < 1 case
			expectedResult = 1;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_HWC_WILEYAC() {
			short cu_index = SI_HWC_WILEYAC;
			double site_index = 1.3;

			double expectedResult = 9.43 - site_index / 7.088;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 410;
			expectedResult = 1;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_HWC_WILEY_BC() {
			short cu_index = SI_HWC_WILEY_BC;
			double site_index = 1.3;

			double expectedResult = 9.43 - site_index / 7.088;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 410;
			expectedResult = 1;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_HWC_WILEY_MB() {
			short cu_index = SI_HWC_WILEY_MB;
			double site_index = 1.3;

			double expectedResult = 9.43 - site_index / 7.088;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 410;
			expectedResult = 1;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_PJ_HUANG() {
			short cu_index = SI_PJ_HUANG;
			double site_index = 2.5;

			double expectedResult = 5 + 1.872138 + 49.555513 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_PJ_HUANGAC() {
			short cu_index = SI_PJ_HUANGAC;
			double site_index = 2.5;

			double expectedResult = 5 + 1.872138 + 49.555513 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_PLI_NIGHGI97() {
			short cu_index = SI_PLI_NIGHGI97;

			assertThrows(GrowthInterceptTotalException.class, () -> SiteIndexYears2BreastHeight.si_y2bh(cu_index, 2.0));
		}

		@Test
		void testSI_PLI_HUANG_PLA() {
			short cu_index = SI_PLI_HUANG_PLA;
			double site_index = 2.5;

			double expectedResult = 3.5 + 1.740006 + 58.83891 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_PLI_HUANG_NAT() {
			short cu_index = SI_PLI_HUANG_NAT;
			double site_index = 2.5;

			double expectedResult = 5 + 1.740006 + 58.83891 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_PLI_NIGHTA2004() {
			short cu_index = SI_PLI_NIGHTA2004;

			assertThrows(NoAnswerException.class, () -> SiteIndexYears2BreastHeight.si_y2bh(cu_index, 8.0));

			double site_index = 12.0;
			double expectedResult = 21.6623 * Height2SiteIndex.ppow(site_index - 9.05671, -0.550762);
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_PLI_NIGHTA98() {
			short cu_index = SI_PLI_NIGHTA98;

			assertThrows(NoAnswerException.class, () -> SiteIndexYears2BreastHeight.si_y2bh(cu_index, 8.0));

			double site_index = 12.0;
			double expectedResult = 21.6623 * Height2SiteIndex.ppow(site_index - 9.05671, -0.550762);
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}
	}
}
