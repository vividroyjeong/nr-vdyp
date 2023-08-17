package ca.bc.gov.nrs.vdyp.common_calculators;

import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.*;

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

			double expectedResult = 446.6 * SiteIndexYears2BreastHeight.ppow(site_index, -1.432);
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
			double expectedResult = 21.6623 * SiteIndexYears2BreastHeight.ppow(site_index - 9.05671, -0.550762);
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_PLI_NIGHTA98() {
			short cu_index = SI_PLI_NIGHTA98;

			assertThrows(NoAnswerException.class, () -> SiteIndexYears2BreastHeight.si_y2bh(cu_index, 8.0));

			double site_index = 12.0;
			double expectedResult = 21.6623 * SiteIndexYears2BreastHeight.ppow(site_index - 9.05671, -0.550762);
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_SW_GOUDNIGH() {
			short cu_index = SI_SW_GOUDNIGH;
			double site_index = 2;

			double expectedResult = 2.0 + 2.1578 + 110.76 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 19.4;
			expectedResult = 10.45;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 20;
			expectedResult = 35.87 * SiteIndexYears2BreastHeight.ppow(site_index - 9.726, -0.5409);
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_SW_NIGHTA2004() {
			short cu_index = SI_SW_NIGHTA2004;
			double site_index = 2;

			double expectedResult = 2.0 + 2.1578 + 110.76 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 20;
			expectedResult = 35.87 * SiteIndexYears2BreastHeight.ppow(site_index - 9.726, -0.5409);
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_SW_HU_GARCIA() {
			short cu_index = SI_SW_HU_GARCIA;
			double site_index = 2;

			double expectedResult = 2.0 + 2.1578 + 110.76 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 20;
			expectedResult = 35.87 * SiteIndexYears2BreastHeight.ppow(site_index - 9.726, -0.5409);
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_SW_NIGHTA() {
			short cu_index = SI_SW_NIGHTA;
			double site_index = 2;

			double expectedResult = 2.0 + 2.1578 + 110.76 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 20;
			expectedResult = 35.87 * SiteIndexYears2BreastHeight.ppow(site_index - 9.726, -0.5409);
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_SE_NIGH() {
			short cu_index = SI_SE_NIGH;
			double site_index = 2;

			double expectedResult = 6.0 + 2.1578 + 110.76 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_SE_NIGHTA() {
			short cu_index = SI_SE_NIGHTA;
			double site_index = 2;

			double expectedResult = 2.0 + 2.1578 + 110.76 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 20;
			expectedResult = 35.87 * SiteIndexYears2BreastHeight.ppow(site_index - 9.726, -0.5409);
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_SE_NIGHGI() {
			short cu_index = SI_SE_NIGHGI;

			assertThrows(GrowthInterceptTotalException.class, () -> SiteIndexYears2BreastHeight.si_y2bh(cu_index, 2.0));
		}

		@Test
		void testSI_PLI_THROWNIGH() {
			short cu_index = SI_PLI_THROWNIGH;
			double site_index = 2;

			double expectedResult = 2 + 0.55 + 69.4 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 20;
			expectedResult = 21.6623 * SiteIndexYears2BreastHeight.ppow(site_index - 9.05671, -0.550762);
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_PLI_THROWER() {
			short cu_index = SI_PLI_THROWER;
			double site_index = 2;

			double expectedResult = 2 + 0.55 + 69.4 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_PLI_MILNER() {
			short cu_index = SI_PLI_MILNER;
			double site_index = 2;

			double expectedResult = 2 + 3.6 + 42.64 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_PLI_CIESZEWSKI() {
			short cu_index = SI_PLI_CIESZEWSKI;
			double site_index = 2;

			double expectedResult = 2 + 3.6 + 42.64 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_PLI_GOUDIE_DRY() {
			short cu_index = SI_PLI_GOUDIE_DRY;
			double site_index = 2;

			double expectedResult = 2 + 3.6 + 42.64 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_PLI_GOUDIE_WET() {
			short cu_index = SI_PLI_GOUDIE_WET;
			double site_index = 2;

			double expectedResult = 2 + 3.6 + 42.64 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_PLI_DEMPSTER() {
			short cu_index = SI_PLI_DEMPSTER;
			double site_index = 2;

			double expectedResult = 2 + 3.6 + 42.64 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_PL_CHEN() {
			short cu_index = SI_PL_CHEN;
			double site_index = 2;

			double expectedResult = 2 + 3.6 + 42.64 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_SE_CHEN() {
			short cu_index = SI_SE_CHEN;
			double site_index = 2;

			double expectedResult = 6.0 + 2.1578 + 110.76 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_SE_CHENAC() {
			short cu_index = SI_SE_CHENAC;
			double site_index = 2;

			double expectedResult = 6.0 + 2.1578 + 110.76 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_SW_NIGHGI() {
			short cu_index = SI_SW_NIGHGI;

			assertThrows(GrowthInterceptTotalException.class, () -> SiteIndexYears2BreastHeight.si_y2bh(cu_index, 2.0));
		}

		@Test
		void testSI_SW_HUANG_PLA() {
			short cu_index = SI_SW_HUANG_PLA;
			double site_index = 2;

			double expectedResult = 4.5 + 4.3473 + 59.908359 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_SW_HUANG_NAT() {
			short cu_index = SI_SW_HUANG_NAT;
			double site_index = 2;

			double expectedResult = 8 + 4.3473 + 59.908359 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_SW_THROWER() {
			short cu_index = SI_SW_THROWER;
			double site_index = 2;

			double expectedResult = 4 + 0.38 + 117.34 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_SW_KER_PLA() {
			short cu_index = SI_SW_KER_PLA;
			double site_index = 2;

			double expectedResult = 2.0 + 2.1578 + 110.76 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_SW_KER_NAT() {
			short cu_index = SI_SW_KER_NAT;
			double site_index = 2;

			double expectedResult = 6.0 + 2.1578 + 110.76 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_SW_GOUDIE_PLA() {
			short cu_index = SI_SW_GOUDIE_PLA;
			double site_index = 2;

			double expectedResult = 2.0 + 2.1578 + 110.76 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_SW_GOUDIE_NAT() {
			short cu_index = SI_SW_GOUDIE_NAT;
			double site_index = 2;

			double expectedResult = 6.0 + 2.1578 + 110.76 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_SW_GOUDIE_PLAAC() {
			short cu_index = SI_SW_GOUDIE_PLAAC;
			double site_index = 3.0;

			double expectedResult = 2.0 + 2.1578 + 110.76 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_SW_GOUDIE_NATAC() {
			short cu_index = SI_SW_GOUDIE_NATAC;
			double site_index = 4.0;

			double expectedResult = 6.0 + 2.1578 + 110.76 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_SW_DEMPSTER() {
			short cu_index = SI_SW_DEMPSTER;
			double site_index = 5.0;

			double expectedResult = 2.1578 + 110.76 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_SW_CIESZEWSKI() {
			short cu_index = SI_SW_CIESZEWSKI;
			double site_index = 6.0;

			double expectedResult = 2.0 + 2.1578 + 110.76 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_SB_HUANG() {
			short cu_index = SI_SB_HUANG;
			double site_index = 7.0;

			double expectedResult = 8 + 2.288325 + 80.774008 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_SB_KER() {
			short cu_index = SI_SB_KER;
			double site_index = 8.0;

			double expectedResult = 7.0 + 4.0427 + 61.08 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_SB_DEMPSTER() {
			short cu_index = SI_SB_DEMPSTER;
			double site_index = 9.0;

			double expectedResult = 7.0 + 4.0427 + 61.08 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_SB_NIGH() {
			short cu_index = SI_SB_NIGH;
			double site_index = 2;

			double expectedResult = 7.0 + 4.0427 + 61.08 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_SB_CIESZEWSKI() {
			short cu_index = SI_SB_CIESZEWSKI;
			double site_index = 2;

			double expectedResult = 7.0 + 4.0427 + 61.08 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_SS_GOUDIE() {
			short cu_index = SI_SS_GOUDIE;
			double site_index = 2;

			double expectedResult = 11.7 - site_index / 5.4054;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 100;
			expectedResult = 1;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_SS_NIGHGI() {
			short cu_index = SI_SS_NIGHGI;

			assertThrows(GrowthInterceptTotalException.class, () -> SiteIndexYears2BreastHeight.si_y2bh(cu_index, 2.0));
		}

		@Test
		void testSI_SS_NIGH() {
			short cu_index = SI_SS_NIGH;
			double site_index = 2;

			double expectedResult = 11.7 - site_index / 5.4054;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 100;
			expectedResult = 1;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_SS_FARR() {
			short cu_index = SI_SS_FARR;
			double site_index = 2;

			double expectedResult = 13.25 - site_index / 6.096;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 100;
			expectedResult = 1;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_SS_BARKER() {
			short cu_index = SI_SS_BARKER;
			double site_index = 2;

			double expectedResult = -5.13 + 450.00 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 600;
			expectedResult = 1;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_CWI_NIGHGI() {
			short cu_index = SI_CWI_NIGHGI;

			assertThrows(GrowthInterceptTotalException.class, () -> SiteIndexYears2BreastHeight.si_y2bh(cu_index, 2.0));
		}

		@Test
		void testSI_CWI_NIGH() {
			short cu_index = SI_CWI_NIGH;
			double site_index = 2;

			double expectedResult = 18.18 - 0.5526 * site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 600;
			expectedResult = 1;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_CWC_KURUCZ() {
			short cu_index = SI_CWC_KURUCZ;
			double site_index = 2;

			double expectedResult = 13.25 - site_index / 6.096;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 600;
			expectedResult = 1;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_CWC_KURUCZAC() {
			short cu_index = SI_CWC_KURUCZAC;
			double site_index = 2;

			double expectedResult = 13.25 - site_index / 6.096;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 600;
			expectedResult = 1;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_CWC_BARKER() {
			short cu_index = SI_CWC_BARKER;
			double site_index = 2;

			double expectedResult = -3.46 + 285.00 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 600;
			expectedResult = 1;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_CWC_NIGH() {
			short cu_index = SI_CWC_NIGH;
			double site_index = 2;

			double expectedResult = 13.25 - site_index / 6.096;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 600;
			expectedResult = 1;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_BA_DILUCCA() {
			short cu_index = SI_BA_DILUCCA;
			double site_index = 2;

			double expectedResult = 18.47373 - 0.4086 * site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 600;
			expectedResult = 5;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_BB_KER() {
			short cu_index = SI_BB_KER;
			double site_index = 2;

			double expectedResult = 18.47373 - site_index / 2.447;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 600;
			expectedResult = 5;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_BP_CURTIS() {
			short cu_index = SI_BP_CURTIS;
			double site_index = 2;

			double expectedResult = 18.47373 - 0.4086 * site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 600;
			expectedResult = 5;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_BP_CURTISAC() {
			short cu_index = SI_BP_CURTISAC;
			double site_index = 2;

			double expectedResult = 18.47373 - 0.4086 * site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 600;
			expectedResult = 5;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_BA_NIGHGI() {
			short cu_index = SI_BA_NIGHGI;

			assertThrows(GrowthInterceptTotalException.class, () -> SiteIndexYears2BreastHeight.si_y2bh(cu_index, 2.0));
		}

		@Test
		void testSI_BA_NIGH() {
			short cu_index = SI_BA_NIGH;
			double site_index = 2;

			double expectedResult = 18.47373 - 0.4086 * site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 600;
			expectedResult = 5;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_BA_KURUCZ86() {
			short cu_index = SI_BA_KURUCZ86;
			double site_index = 2;

			double expectedResult = 18.47373 - 0.4086 * site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 600;
			expectedResult = 5;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_BA_KURUCZ82() {
			short cu_index = SI_BA_KURUCZ82;
			double site_index = 2;

			double expectedResult = 18.47373 - 0.4086 * site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 600;
			expectedResult = 5;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_BA_KURUCZ82AC() {
			short cu_index = SI_BA_KURUCZ82AC;
			double site_index = 2;

			double expectedResult = 18.47373 - 0.4086 * site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 600;
			expectedResult = 5;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_BL_CHEN() {
			short cu_index = SI_BL_CHEN;
			double site_index = 2;

			double expectedResult = 42.25 - 10.66 * SiteIndexYears2BreastHeight.llog(site_index);
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 600;
			expectedResult = 5;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_BL_CHENAC() {
			short cu_index = SI_BL_CHENAC;
			double site_index = 2;

			double expectedResult = 42.25 - 10.66 * SiteIndexYears2BreastHeight.llog(site_index);
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 600;
			expectedResult = 5;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_BL_THROWERGI() {
			short cu_index = SI_BL_THROWERGI;

			assertThrows(GrowthInterceptTotalException.class, () -> SiteIndexYears2BreastHeight.si_y2bh(cu_index, 2.0));
		}

		@Test
		void testSI_BL_KURUCZ82() {
			short cu_index = SI_BL_KURUCZ82;
			double site_index = 2;

			double expectedResult = 42.25 - 10.66 * SiteIndexYears2BreastHeight.llog(site_index);
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 600;
			expectedResult = 5;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_FDI_NIGHGI() {
			short cu_index = SI_FDI_NIGHGI;

			assertThrows(GrowthInterceptTotalException.class, () -> SiteIndexYears2BreastHeight.si_y2bh(cu_index, 2.0));
		}

		@Test
		void testSI_FDI_HUANG_PLA() {
			short cu_index = SI_FDI_HUANG_PLA;
			double site_index = 2;

			double expectedResult = 6.5 + 5.276585 + 38.968242 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_FDI_HUANG_NAT() {
			short cu_index = SI_FDI_HUANG_NAT;
			double site_index = 2;

			double expectedResult = 8.0 + 5.276585 + 38.968242 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_FDI_MILNER() {
			short cu_index = SI_FDI_MILNER;
			double site_index = 2;

			double expectedResult = 4.0 + 99.0 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_FDI_THROWER() {
			short cu_index = SI_FDI_THROWER;
			double site_index = 2;

			double expectedResult = 4.0 + 99.0 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_FDI_THROWERAC() {
			short cu_index = SI_FDI_THROWERAC;
			double site_index = 2;

			double expectedResult = 4.0 + 99.0 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_FDI_VDP_MONT() {
			short cu_index = SI_FDI_VDP_MONT;
			double site_index = 2;

			double expectedResult = 4.0 + 99.0 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_FDI_VDP_WASH() {
			short cu_index = SI_FDI_VDP_WASH;
			double site_index = 2;

			double expectedResult = 4.0 + 99.0 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_FDI_MONS_DF() {
			short cu_index = SI_FDI_MONS_DF;
			double site_index = 2;

			double expectedResult = 16.0 - site_index / 3.0;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 600;
			expectedResult = 8;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_FDI_MONS_GF() {
			short cu_index = SI_FDI_MONS_GF;
			double site_index = 2;

			double expectedResult = 16.0 - site_index / 3.0;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 600;
			expectedResult = 8;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_FDI_MONS_WRC() {
			short cu_index = SI_FDI_MONS_WRC;
			double site_index = 2;

			double expectedResult = 16.0 - site_index / 3.0;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 600;
			expectedResult = 8;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_FDI_MONS_WH() {
			short cu_index = SI_FDI_MONS_WH;
			double site_index = 2;

			double expectedResult = 16.0 - site_index / 3.0;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 600;
			expectedResult = 8;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_FDI_MONS_SAF() {
			short cu_index = SI_FDI_MONS_SAF;
			double site_index = 2;

			double expectedResult = 16.0 - site_index / 3.0;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 600;
			expectedResult = 8;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_AT_NIGH() {
			short cu_index = SI_AT_NIGH;
			double site_index = 3.5;

			double expectedResult = 1.331 + 38.56 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_AT_CHEN() {
			short cu_index = SI_AT_CHEN;
			double site_index = 3.5;

			double expectedResult = 1.331 + 38.56 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_AT_HUANG() {
			short cu_index = SI_AT_HUANG;
			double site_index = 5.0;

			double expectedResult = 1 + 2.184066 + 50.788746 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_AT_GOUDIE() {
			short cu_index = SI_AT_GOUDIE;
			double site_index = 4.0;

			double expectedResult = 1.331 + 38.56 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_ACB_HUANGAC() {
			short cu_index = SI_ACB_HUANGAC;
			double site_index = 2;

			double expectedResult = 1 - 1.196472 + 104.124205 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 600;
			expectedResult = 1;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_ACB_HUANG() {
			short cu_index = SI_ACB_HUANG;
			double site_index = 2;

			double expectedResult = 1 - 1.196472 + 104.124205 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 600;
			expectedResult = 1;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_ACT_THROWER() {
			short cu_index = SI_ACT_THROWER;
			double site_index = 4.0;

			double expectedResult = 2;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_ACT_THROWERAC() {
			short cu_index = SI_ACT_THROWERAC;
			double site_index = 4.0;

			double expectedResult = 2;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_AT_CIESZEWSKI() {
			short cu_index = SI_AT_CIESZEWSKI;
			double site_index = 4.0;

			double expectedResult = 1.331 + 38.56 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_DR_HARRING() {
			short cu_index = SI_DR_HARRING;
			double site_index = 2;

			double expectedResult = 2;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 600;
			expectedResult = 1;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_DR_CHEN() {
			short cu_index = SI_DR_CHEN;
			double site_index = 2;

			double expectedResult = 2;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 600;
			expectedResult = 1;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_DR_NIGH() {
			short cu_index = SI_DR_NIGH;
			double site_index = 2;

			double expectedResult = 5.494 - 0.1789 * (0.3094 + 0.7616 * site_index);
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			site_index = 600;
			expectedResult = 1;
			actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_PY_NIGHGI() {
			short cu_index = SI_PY_NIGHGI;

			assertThrows(GrowthInterceptTotalException.class, () -> SiteIndexYears2BreastHeight.si_y2bh(cu_index, 2.0));
		}

		@Test
		void testSI_PY_NIGH() {
			short cu_index = SI_PY_NIGH;
			double site_index = 3.5;

			double expectedResult = 36.35 * Math.pow(0.9318, site_index);
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_PY_HANN() {
			short cu_index = SI_PY_HANN;
			double site_index = 3.5;

			double expectedResult = 2 + 3.6 + 42.64 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_PY_HANNAC() {
			short cu_index = SI_PY_HANNAC;
			double site_index = 3.5;

			double expectedResult = 2 + 3.6 + 42.64 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_PY_MILNER() {
			short cu_index = SI_PY_MILNER;
			double site_index = 3.5;

			double expectedResult = 2 + 3.6 + 42.64 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_LW_MILNER() {
			short cu_index = SI_LW_MILNER;
			double site_index = 3.5;

			double expectedResult = 3.36 + 87.18 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_LW_NIGH() {
			short cu_index = SI_LW_NIGH;
			double site_index = 3.5;

			double expectedResult = 3.36 + 87.18 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_EP_NIGH() {
			short cu_index = SI_EP_NIGH;
			double site_index = 3.5;

			double expectedResult = 1.331 + 38.56 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_PW_CURTIS() {
			short cu_index = SI_PW_CURTIS;
			double site_index = 3.5;

			double expectedResult = 2.0 + 2.1578 + 110.76 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSI_PW_CURTISAC() {
			short cu_index = SI_PW_CURTISAC;
			double site_index = 2;

			double expectedResult = 2.0 + 2.1578 + 110.76 / site_index;
			double actualResult = SiteIndexYears2BreastHeight.si_y2bh(cu_index, site_index);

			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testSwitchDefault() {
			short cu_index = 200;
			assertThrows(CurveErrorException.class, () -> SiteIndexYears2BreastHeight.si_y2bh(cu_index, 2.0));
		}
	}

	@Test
	void testSi_y2bh05() {
		short cu_index = SI_PW_CURTIS;
		double site_index = 3.5;

		double expectedResult = ((int) (2.0 + 2.1578 + 110.76 / site_index)) + 0.5;
		double actualResult = SiteIndexYears2BreastHeight.si_y2bh05(cu_index, site_index);

		assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
	}
}
