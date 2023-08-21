package ca.bc.gov.nrs.vdyp.common_calculators;

import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

import org.junit.jupiter.api.*;

public class SiteIndex2HeightTest {
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
	public void testPpowZero() {
		assertThat(0.0, closeTo(SiteIndex2Height.ppow(0.0, 3.0), ERROR_TOLERANCE));
	}

	@Test
	public void testLlogPositive() {
		assertThat(1.60943, closeTo(SiteIndex2Height.llog(5.0), ERROR_TOLERANCE));
		assertThat(11.51293, closeTo(SiteIndex2Height.llog(100000.0), ERROR_TOLERANCE));
	}

	@Test
	public void testLlogZero() {
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
	}

	@Test
	public void testHuGarciaQ() { // the way I've done these tests is to validate them with the orginal C code and
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
