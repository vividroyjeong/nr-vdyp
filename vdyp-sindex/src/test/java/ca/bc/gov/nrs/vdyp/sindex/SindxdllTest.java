package ca.bc.gov.nrs.vdyp.sindex;

import ca.bc.gov.nrs.vdyp.common_calculators.Height2SiteIndex;
import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

import org.junit.jupiter.api.*;

class SindxdllTest {
	/*
	 * establishment types
	 */
	private static final int SI_ESTAB_NAT = 0;
	private static final int SI_ESTAB_PLA = 1;

	/*
	 * site index estimation (from height and age) types
	 */
// private static final int SI_EST_ITERATE = 0; Unused
// private static final int SI_EST_DIRECT  = 1; Unused

	/*
	 * error codes as return values from functions
	 */
	private static final int SI_ERR_LT13 = -1;
	private static final int SI_ERR_GI_MIN = -2;
	private static final int SI_ERR_GI_MAX = -3;
	private static final int SI_ERR_NO_ANS = -4;
	private static final int SI_ERR_CURVE = -5;
	private static final int SI_ERR_CLASS = -6;
	private static final int SI_ERR_FIZ = -7;
	private static final int SI_ERR_CODE = -8;
	private static final int SI_ERR_GI_TOT = -9;
	private static final int SI_ERR_SPEC = -10;
	private static final int SI_ERR_AGE_TYPE = -11;
// private static final int SI_ERR_ESTAB     = -12; Replaced with Java exception

//These are taken from sindex.h (since it was missing everywhere else). These were not defined in the orginal sindxdll.c

	/* define species and equation indices */
	private static final int SI_SPEC_A = 0;
	private static final int SI_SPEC_ABAL = 1;
	private static final int SI_SPEC_ABCO = 2;
	private static final int SI_SPEC_AC = 3;
	private static final int SI_SPEC_ACB = 4;
	private static final int SI_SPEC_ACT = 5;
	private static final int SI_SPEC_AD = 6;
	private static final int SI_SPEC_AH = 7;
	private static final int SI_SPEC_AT = 8;
	private static final int SI_SPEC_AX = 9;
	private static final int SI_SPEC_B = 10;
	private static final int SI_SPEC_BA = 11;
	private static final int SI_SPEC_BB = 12;
	private static final int SI_SPEC_BC = 13;
	private static final int SI_SPEC_BG = 14;
	private static final int SI_SPEC_BI = 15;
	private static final int SI_SPEC_BL = 16;
	private static final int SI_SPEC_BM = 17;
	private static final int SI_SPEC_BP = 18;
	private static final int SI_SPEC_C = 19;
	private static final int SI_SPEC_CI = 20;
	private static final int SI_SPEC_CP = 21;
	private static final int SI_SPEC_CW = 22;
	private static final int SI_SPEC_CWC = 23;
	private static final int SI_SPEC_CWI = 24;
	private static final int SI_SPEC_CY = 25;
	private static final int SI_SPEC_D = 26;
	private static final int SI_SPEC_DG = 27;
	private static final int SI_SPEC_DM = 28;
	private static final int SI_SPEC_DR = 29;
	private static final int SI_SPEC_E = 30;
	private static final int SI_SPEC_EA = 31;
	private static final int SI_SPEC_EB = 32;
	private static final int SI_SPEC_EE = 33;
	private static final int SI_SPEC_EP = 34;
	private static final int SI_SPEC_ES = 35;
	private static final int SI_SPEC_EW = 36;
	private static final int SI_SPEC_EXP = 37;
	private static final int SI_SPEC_FD = 38;
	private static final int SI_SPEC_FDC = 39;
	private static final int SI_SPEC_FDI = 40;
	private static final int SI_SPEC_G = 41;
	private static final int SI_SPEC_GP = 42;
	private static final int SI_SPEC_GR = 43;
	private static final int SI_SPEC_H = 44;
	private static final int SI_SPEC_HM = 45;
	private static final int SI_SPEC_HW = 46;
	private static final int SI_SPEC_HWC = 47;
	private static final int SI_SPEC_HWI = 48;
	private static final int SI_SPEC_HXM = 49;
	private static final int SI_SPEC_IG = 50;
	private static final int SI_SPEC_IS = 51;
	private static final int SI_SPEC_J = 52;
	private static final int SI_SPEC_JR = 53;
	private static final int SI_SPEC_K = 54;
	private static final int SI_SPEC_KC = 55;
	private static final int SI_SPEC_L = 56;
	private static final int SI_SPEC_LA = 57;
	private static final int SI_SPEC_LE = 58;
	private static final int SI_SPEC_LT = 59;
	private static final int SI_SPEC_LW = 60;
	private static final int SI_SPEC_M = 61;
	private static final int SI_SPEC_MB = 62;
	private static final int SI_SPEC_ME = 63;
	private static final int SI_SPEC_MN = 64;
	private static final int SI_SPEC_MR = 65;
	private static final int SI_SPEC_MS = 66;
	private static final int SI_SPEC_MV = 67;
	private static final int SI_SPEC_OA = 68;
	private static final int SI_SPEC_OB = 69;
	private static final int SI_SPEC_OC = 70;
	private static final int SI_SPEC_OD = 71;
	private static final int SI_SPEC_OE = 72;
	private static final int SI_SPEC_OF = 73;
	private static final int SI_SPEC_OG = 74;
	private static final int SI_SPEC_P = 75;
	private static final int SI_SPEC_PA = 76;
	private static final int SI_SPEC_PF = 77;
	private static final int SI_SPEC_PJ = 78;
	private static final int SI_SPEC_PL = 79;
	private static final int SI_SPEC_PLC = 80;
	private static final int SI_SPEC_PLI = 81;
	private static final int SI_SPEC_PM = 82;
	private static final int SI_SPEC_PR = 83;
	private static final int SI_SPEC_PS = 84;
	private static final int SI_SPEC_PW = 85;
	private static final int SI_SPEC_PXJ = 86;
	private static final int SI_SPEC_PY = 87;
	private static final int SI_SPEC_Q = 88;
	private static final int SI_SPEC_QE = 89;
	private static final int SI_SPEC_QG = 90;
	private static final int SI_SPEC_R = 91;
	private static final int SI_SPEC_RA = 92;
	private static final int SI_SPEC_S = 93;
	private static final int SI_SPEC_SA = 94;
	private static final int SI_SPEC_SB = 95;
	private static final int SI_SPEC_SE = 96;
	private static final int SI_SPEC_SI = 97;
	private static final int SI_SPEC_SN = 98;
	private static final int SI_SPEC_SS = 99;
	private static final int SI_SPEC_SW = 100;
	private static final int SI_SPEC_SX = 101;
	private static final int SI_SPEC_SXB = 102;
	private static final int SI_SPEC_SXE = 103;
	private static final int SI_SPEC_SXL = 104;
	private static final int SI_SPEC_SXS = 105;
	private static final int SI_SPEC_SXW = 106;
	private static final int SI_SPEC_SXX = 107;
	private static final int SI_SPEC_T = 108;
	private static final int SI_SPEC_TW = 109;
	private static final int SI_SPEC_U = 110;
	private static final int SI_SPEC_UA = 111;
	private static final int SI_SPEC_UP = 112;
	private static final int SI_SPEC_V = 113;
	private static final int SI_SPEC_VB = 114;
	private static final int SI_SPEC_VP = 115;
	private static final int SI_SPEC_VS = 116;
	private static final int SI_SPEC_VV = 117;
	private static final int SI_SPEC_W = 118;
	private static final int SI_SPEC_WA = 119;
	private static final int SI_SPEC_WB = 120;
	private static final int SI_SPEC_WD = 121;
	private static final int SI_SPEC_WI = 122;
	private static final int SI_SPEC_WP = 123;
	private static final int SI_SPEC_WS = 124;
	private static final int SI_SPEC_WT = 125;
	private static final int SI_SPEC_X = 126;
	private static final int SI_SPEC_XC = 127;
	private static final int SI_SPEC_XH = 128;
	private static final int SI_SPEC_Y = 129;
	private static final int SI_SPEC_YC = 130;
	private static final int SI_SPEC_YP = 131;
	private static final int SI_SPEC_Z = 132;
	private static final int SI_SPEC_ZC = 133;
	private static final int SI_SPEC_ZH = 134;
	private static final int SI_MAX_SPECIES = 135;

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
	private static final int SI_SW_NIGHGI2004 = 115;
	private static final int SI_SW_NIGHTA = 83;
	private static final int SI_SW_THROWER = 66;
	private static final int SI_MAX_CURVES = 123;

	private static final double ERROR_TOLERANCE = 0.00001;

	@Test
	void testVersionNumber() {
		short expectedValue = 151;
		short actualValue = Sindxdll.VersionNumber();
		assertEquals(expectedValue, actualValue);
	}

	@Test
	void testFirstSpecies() {
		short expectedValue = 0;
		short actualValue = Sindxdll.FirstSpecies();
		assertEquals(expectedValue, actualValue);
	}

	@Nested
	class NextSpeciesTest {
		@Test
		void testValidIndex() {
			short inputIndex = 2; // Choose a valid index for testing
			short expectedOutput = (short) (inputIndex + 1);

			short actualOutput = Sindxdll.NextSpecies(inputIndex);

			assertEquals(expectedOutput, actualOutput, "NextSpecies should return the next species index");
		}

		@Test
		void testTooSmallIndex() {
			short invalidIndex = -1; // Choose an invalid index for testing
			assertThrows(
					SpeciesErrorException.class, () -> Sindxdll.NextSpecies(invalidIndex),
					"NextSpecies should throw SpeciesErrorException for invalid index"
			);
		}

		@Test
		void testTooBigIndex() {
			short invalidIndex = 135; // Choose an invalid index for testing
			assertThrows(
					SpeciesErrorException.class, () -> Sindxdll.NextSpecies(invalidIndex),
					"NextSpecies should throw SpeciesErrorException for invalid index"
			);
		}

		@Test
		void testLastIndex() {
			short lastIndex = 134; // Use the value of SI_SPEC_END for testing
			assertThrows(
					NoAnswerException.class, () -> Sindxdll.NextSpecies(lastIndex),
					"NextSpecies should throw NoAnswerException for last defined species index"
			);
		}
	}

	@Nested
	@DisplayName("Tests for SpecCode method")
	class SpecCodeTest {
		@Test
		void testTooSmallIndex() {
			short invalidIndex = -1; // Choose an invalid index for testing
			assertThrows(
					IllegalArgumentException.class, () -> Sindxdll.SpecCode(invalidIndex),
					"SpecCode should throw IllegalArgumentException for invalid index"
			);
		}

		@Test
		void testTooBigIndex() {
			short invalidIndex = 135; // Choose an invalid index for testing
			assertThrows(
					IllegalArgumentException.class, () -> Sindxdll.SpecCode(invalidIndex),
					"SpecCode should throw IllegalArgumentException for invalid index"
			);
		}
	}

	@Nested
	@DisplayName("Tests for SpecUse method")
	class SpecUseTest {
		@Test
		void testTooSmallIndex() {
			short invalidIndex = -1; // Choose an invalid index for testing
			assertThrows(
					SpeciesErrorException.class, () -> Sindxdll.SpecUse(invalidIndex),
					"SpecUse should throw SpeciesErrorException for invalid index"
			);
		}

		@Test
		void testTooBigIndex() {
			short invalidIndex = 135; // Choose an invalid index for testing
			assertThrows(
					SpeciesErrorException.class, () -> Sindxdll.SpecUse(invalidIndex),
					"SpecUse should throw SpeciesErrorException for invalid index"
			);
		}

		private void testHelper(int inputIndex, int expectedValue) { // helper function to reduce repetive code
			short actualValue = Sindxdll.SpecUse((short) inputIndex);
			assertEquals((int) actualValue, expectedValue);
		}

		@Test
		void testSI_SPEC_A() {
			testHelper(SI_SPEC_A, 0x00);
		}

		@Test
		void testSI_SPEC_ABAL() {
			testHelper(SI_SPEC_ABAL, 0x00);
		}

		@Test
		void testSI_SPEC_ABCO() {
			testHelper(SI_SPEC_ABCO, 0x00);
		}

		@Test
		void testSI_SPEC_AC() {
			testHelper(SI_SPEC_AC, 0x04);
		}

		@Test
		void testSI_SPEC_ACB() {
			testHelper(SI_SPEC_ACB, 0x07);
		}

		@Test
		void testSI_SPEC_ACT() {
			testHelper(SI_SPEC_ACT, 0x04);
		}

		@Test
		void testSI_SPEC_AD() {
			testHelper(SI_SPEC_AD, 0x00);
		}

		@Test
		void testSI_SPEC_AH() {
			testHelper(SI_SPEC_AH, 0x00);
		}

		@Test
		void testSI_SPEC_AT() {
			testHelper(SI_SPEC_AT, 0x06);
		}

		@Test
		void testSI_SPEC_AX() {
			testHelper(SI_SPEC_AX, 0x00);
		}

		@Test
		void testSI_SPEC_B() {
			testHelper(SI_SPEC_B, 0x00);
		}

		@Test
		void testSI_SPEC_BA() {
			testHelper(SI_SPEC_BA, 0x05);
		}

		@Test
		void testSI_SPEC_BB() {
			testHelper(SI_SPEC_BB, 0x00);
		}

		@Test
		void testSI_SPEC_BC() {
			testHelper(SI_SPEC_BC, 0x00);
		}

		@Test
		void testSI_SPEC_BG() {
			testHelper(SI_SPEC_BG, 0x00);
		}

		@Test
		void testSI_SPEC_BI() {
			testHelper(SI_SPEC_BI, 0x00);
		}

		@Test
		void testSI_SPEC_BL() {
			testHelper(SI_SPEC_BL, 0x06);
		}

		@Test
		void testSI_SPEC_BM() {
			testHelper(SI_SPEC_BM, 0x00);
		}

		@Test
		void testSI_SPEC_BP() {
			testHelper(SI_SPEC_BP, 0x05);
		}

		@Test
		void testSI_SPEC_C() {
			testHelper(SI_SPEC_C, 0x00);
		}

		@Test
		void testSI_SPEC_CI() {
			testHelper(SI_SPEC_CI, 0x00);
		}

		@Test
		void testSI_SPEC_CP() {
			testHelper(SI_SPEC_CP, 0x00);
		}

		@Test
		void testSI_SPEC_CW() {
			testHelper(SI_SPEC_CW, 0x05);
		}

		@Test
		void testSI_SPEC_CWC() {
			testHelper(SI_SPEC_CWC, 0x05);
		}

		@Test
		void testSI_SPEC_CWI() {
			testHelper(SI_SPEC_CWI, 0x06);
		}

		@Test
		void testSI_SPEC_CY() {
			testHelper(SI_SPEC_CY, 0x01);
		}

		@Test
		void testSI_SPEC_D() {
			testHelper(SI_SPEC_D, 0x00);
		}

		@Test
		void testSI_SPEC_DG() {
			testHelper(SI_SPEC_DG, 0x00);
		}

		@Test
		void testSI_SPEC_DM() {
			testHelper(SI_SPEC_DM, 0x02);
		}

		@Test
		void testSI_SPEC_DR() {
			testHelper(SI_SPEC_DR, 0x05);
		}

		@Test
		void testSI_SPEC_E() {
			testHelper(SI_SPEC_E, 0x00);
		}

		@Test
		void testSI_SPEC_EA() {
			testHelper(SI_SPEC_EA, 0x02);
		}

		@Test
		void testSI_SPEC_EB() {
			testHelper(SI_SPEC_EB, 0x02);
		}

		@Test
		void testSI_SPEC_EE() {
			testHelper(SI_SPEC_EE, 0x02);
		}

		@Test
		void testSI_SPEC_EP() {
			testHelper(SI_SPEC_EP, 0x06);
		}

		@Test
		void testSI_SPEC_ES() {
			testHelper(SI_SPEC_ES, 0x02);
		}

		@Test
		void testSI_SPEC_EW() {
			testHelper(SI_SPEC_EW, 0x02);
		}

		@Test
		void testSI_SPEC_EXP() {
			testHelper(SI_SPEC_EXP, 0x02);
		}

		@Test
		void testSI_SPEC_FD() {
			testHelper(SI_SPEC_FD, 0x05);
		}

		@Test
		void testSI_SPEC_FDC() {
			testHelper(SI_SPEC_FDC, 0x05);
		}

		@Test
		void testSI_SPEC_FDI() {
			testHelper(SI_SPEC_FDI, 0x06);
		}

		@Test
		void testSI_SPEC_G() {
			testHelper(SI_SPEC_G, 0x01);
		}

		@Test
		void testSI_SPEC_GP() {
			testHelper(SI_SPEC_GP, 0x01);
		}

		@Test
		void testSI_SPEC_GR() {
			testHelper(SI_SPEC_GR, 0x01);
		}

		@Test
		void testSI_SPEC_H() {
			testHelper(SI_SPEC_H, 0x00);
		}

		@Test
		void testSI_SPEC_HM() {
			testHelper(SI_SPEC_HM, 0x05);
		}

		@Test
		void testSI_SPEC_HW() {
			testHelper(SI_SPEC_HW, 0x05);
		}

		@Test
		void testSI_SPEC_HWC() {
			testHelper(SI_SPEC_HWC, 0x05);
		}

		@Test
		void testSI_SPEC_HWI() {
			testHelper(SI_SPEC_HWI, 0x06);
		}

		@Test
		void testSI_SPEC_HXM() {
			testHelper(SI_SPEC_HXM, 0x00);
		}

		@Test
		void testSI_SPEC_IG() {
			testHelper(SI_SPEC_IG, 0x00);
		}

		@Test
		void testSI_SPEC_IS() {
			testHelper(SI_SPEC_IS, 0x00);
		}

		@Test
		void testSI_SPEC_J() {
			testHelper(SI_SPEC_J, 0x02);
		}

		@Test
		void testSI_SPEC_JR() {
			testHelper(SI_SPEC_JR, 0x02);
		}

		@Test
		void testSI_SPEC_K() {
			testHelper(SI_SPEC_K, 0x00);
		}

		@Test
		void testSI_SPEC_KC() {
			testHelper(SI_SPEC_KC, 0x00);
		}

		@Test
		void testSI_SPEC_L() {
			testHelper(SI_SPEC_L, 0x00);
		}

		@Test
		void testSI_SPEC_LA() {
			testHelper(SI_SPEC_LA, 0x02);
		}

		@Test
		void testSI_SPEC_LE() {
			testHelper(SI_SPEC_LE, 0x02);
		}

		@Test
		void testSI_SPEC_LT() {
			testHelper(SI_SPEC_LT, 0x02);
		}

		@Test
		void testSI_SPEC_LW() {
			testHelper(SI_SPEC_LW, 0x06);
		}

		@Test
		void testSI_SPEC_M() {
			testHelper(SI_SPEC_M, 0x00);
		}

		@Test
		void testSI_SPEC_MB() {
			testHelper(SI_SPEC_MB, 0x01);
		}

		@Test
		void testSI_SPEC_ME() {
			testHelper(SI_SPEC_ME, 0x00);
		}

		@Test
		void testSI_SPEC_MN() {
			testHelper(SI_SPEC_MN, 0x00);
		}

		@Test
		void testSI_SPEC_MR() {
			testHelper(SI_SPEC_MR, 0x00);
		}

		@Test
		void testSI_SPEC_MS() {
			testHelper(SI_SPEC_MS, 0x00);
		}

		@Test
		void testSI_SPEC_MV() {
			testHelper(SI_SPEC_MV, 0x00);
		}

		@Test
		void testSI_SPEC_OA() {
			testHelper(SI_SPEC_OA, 0x00);
		}

		@Test
		void testSI_SPEC_OB() {
			testHelper(SI_SPEC_OB, 0x00);
		}

		@Test
		void testSI_SPEC_OC() {
			testHelper(SI_SPEC_OC, 0x00);
		}

		@Test
		void testSI_SPEC_OD() {
			testHelper(SI_SPEC_OD, 0x00);
		}

		@Test
		void testSI_SPEC_OE() {
			testHelper(SI_SPEC_OE, 0x00);
		}

		@Test
		void testSI_SPEC_OF() {
			testHelper(SI_SPEC_OF, 0x00);
		}

		@Test
		void testSI_SPEC_OG() {
			testHelper(SI_SPEC_OG, 0x00);
		}

		@Test
		void testSI_SPEC_P() {
			testHelper(SI_SPEC_P, 0x02);
		}

		@Test
		void testSI_SPEC_PA() {
			testHelper(SI_SPEC_PA, 0x02);
		}

		@Test
		void testSI_SPEC_PF() {
			testHelper(SI_SPEC_PF, 0x02);
		}

		@Test
		void testSI_SPEC_PJ() {
			testHelper(SI_SPEC_PJ, 0x02);
		}

		@Test
		void testSI_SPEC_PL() {
			testHelper(SI_SPEC_PL, 0x06);
		}

		@Test
		void testSI_SPEC_PLC() {
			testHelper(SI_SPEC_PLC, 0x01);
		}

		@Test
		void testSI_SPEC_PLI() {
			testHelper(SI_SPEC_PLI, 0x06);
		}

		@Test
		void testSI_SPEC_PM() {
			testHelper(SI_SPEC_PM, 0x00);
		}

		@Test
		void testSI_SPEC_PR() {
			testHelper(SI_SPEC_PR, 0x00);
		}

		@Test
		void testSI_SPEC_PS() {
			testHelper(SI_SPEC_PS, 0x00);
		}

		@Test
		void testSI_SPEC_PW() {
			testHelper(SI_SPEC_PW, 0x04);
		}

		@Test
		void testSI_SPEC_PXJ() {
			testHelper(SI_SPEC_PXJ, 0x02);
		}

		@Test
		void testSI_SPEC_PY() {
			testHelper(SI_SPEC_PY, 0x06);
		}

		@Test
		void testSI_SPEC_Q() {
			testHelper(SI_SPEC_Q, 0x00);
		}

		@Test
		void testSI_SPEC_QE() {
			testHelper(SI_SPEC_QE, 0x00);
		}

		@Test
		void testSI_SPEC_QG() {
			testHelper(SI_SPEC_QG, 0x01);
		}

		@Test
		void testSI_SPEC_R() {
			testHelper(SI_SPEC_R, 0x01);
		}

		@Test
		void testSI_SPEC_RA() {
			testHelper(SI_SPEC_RA, 0x01);
		}

		@Test
		void testSI_SPEC_S() {
			testHelper(SI_SPEC_S, 0x00);
		}

		@Test
		void testSI_SPEC_SA() {
			testHelper(SI_SPEC_SA, 0x02);
		}

		@Test
		void testSI_SPEC_SB() {
			testHelper(SI_SPEC_SB, 0x06);
		}

		@Test
		void testSI_SPEC_SE() {
			testHelper(SI_SPEC_SE, 0x06);
		}

		@Test
		void testSI_SPEC_SI() {
			testHelper(SI_SPEC_SI, 0x02);
		}

		@Test
		void testSI_SPEC_SN() {
			testHelper(SI_SPEC_SN, 0x02);
		}

		@Test
		void testSI_SPEC_SS() {
			testHelper(SI_SPEC_SS, 0x05);
		}

		@Test
		void testSI_SPEC_SW() {
			testHelper(SI_SPEC_SW, 0x06);
		}

		@Test
		void testSI_SPEC_SX() {
			testHelper(SI_SPEC_SX, 0x06);
		}

		@Test
		void testSI_SPEC_SXB() {
			testHelper(SI_SPEC_SXB, 0x02);
		}

		@Test
		void testSI_SPEC_SXE() {
			testHelper(SI_SPEC_SXE, 0x01);
		}

		@Test
		void testSI_SPEC_SXL() {
			testHelper(SI_SPEC_SXL, 0x01);
		}

		@Test
		void testSI_SPEC_SXS() {
			testHelper(SI_SPEC_SXS, 0x01);
		}

		@Test
		void testSI_SPEC_SXW() {
			testHelper(SI_SPEC_SXW, 0x02);
		}

		@Test
		void testSI_SPEC_SXX() {
			testHelper(SI_SPEC_SXX, 0x02);
		}

		@Test
		void testSI_SPEC_T() {
			testHelper(SI_SPEC_T, 0x00);
		}

		@Test
		void testSI_SPEC_TW() {
			testHelper(SI_SPEC_TW, 0x00);
		}

		@Test
		void testSI_SPEC_U() {
			testHelper(SI_SPEC_U, 0x00);
		}

		@Test
		void testSI_SPEC_UA() {
			testHelper(SI_SPEC_UA, 0x00);
		}

		@Test
		void testSI_SPEC_UP() {
			testHelper(SI_SPEC_UP, 0x00);
		}

		@Test
		void testSI_SPEC_V() {
			testHelper(SI_SPEC_V, 0x00);
		}

		@Test
		void testSI_SPEC_VB() {
			testHelper(SI_SPEC_VB, 0x00);
		}

		@Test
		void testSI_SPEC_VP() {
			testHelper(SI_SPEC_VP, 0x00);
		}

		@Test
		void testSI_SPEC_VS() {
			testHelper(SI_SPEC_VS, 0x00);
		}

		@Test
		void testSI_SPEC_VV() {
			testHelper(SI_SPEC_VV, 0x00);
		}

		@Test
		void testSI_SPEC_W() {
			testHelper(SI_SPEC_W, 0x00);
		}

		@Test
		void testSI_SPEC_WA() {
			testHelper(SI_SPEC_WA, 0x00);
		}

		@Test
		void testSI_SPEC_WB() {
			testHelper(SI_SPEC_WB, 0x00);
		}

		@Test
		void testSI_SPEC_WD() {
			testHelper(SI_SPEC_WD, 0x00);
		}

		@Test
		void testSI_SPEC_WI() {
			testHelper(SI_SPEC_WI, 0x00);
		}

		@Test
		void testSI_SPEC_WP() {
			testHelper(SI_SPEC_WP, 0x00);
		}

		@Test
		void testSI_SPEC_WS() {
			testHelper(SI_SPEC_WS, 0x00);
		}

		@Test
		void testSI_SPEC_WT() {
			testHelper(SI_SPEC_WT, 0x00);
		}

		@Test
		void testSI_SPEC_X() {
			testHelper(SI_SPEC_X, 0x00);
		}

		@Test
		void testSI_SPEC_XC() {
			testHelper(SI_SPEC_XC, 0x00);
		}

		@Test
		void testSI_SPEC_XH() {
			testHelper(SI_SPEC_XH, 0x00);
		}

		@Test
		void testSI_SPEC_Y() {
			testHelper(SI_SPEC_Y, 0x00);
		}

		@Test
		void testSI_SPEC_YC() {
			testHelper(SI_SPEC_YC, 0x01);
		}

		@Test
		void testSI_SPEC_YP() {
			testHelper(SI_SPEC_YP, 0x00);
		}

		@Test
		void testSI_SPEC_Z() {
			testHelper(SI_SPEC_Z, 0x00);
		}

		@Test
		void testSI_SPEC_ZC() {
			testHelper(SI_SPEC_ZC, 0x00);
		}

		@Test
		void testSI_SPEC_ZH() {
			testHelper(SI_SPEC_ZH, 0x00);
		}
	}

	@Nested
	@DisplayName("Tests for DefCurve method")
	class DefCurveTest {
		@Test
		void testTooSmallIndex() {
			short invalidIndex = -1; // Choose an invalid index for testing
			assertThrows(
					SpeciesErrorException.class, () -> Sindxdll.DefCurve(invalidIndex),
					"DefCurve should throw SpeciesErrorException for invalid index"
			);
		}

		@Test
		void testTooBigIndex() {
			short invalidIndex = 135; // Choose an invalid index for testing
			assertThrows(
					SpeciesErrorException.class, () -> Sindxdll.DefCurve(invalidIndex),
					"DefCurve should throw SpeciesErrorException for invalid index"
			);
		}

		@Test
		void testLastSpeciesIndex() {
			short lastIndex = 134; // Choose the last index for testing
			assertThrows(
					NoAnswerException.class, () -> Sindxdll.DefCurve(lastIndex),
					"DefCurve should throw NoAnswerException for last index"
			);
		}

		@Test
		void testValidIndex() {
			short validIndex = 0;
			short expectValue = -4; // SI_ERR_NO_ANS
			short actualOutput = Sindxdll.DefCurve(validIndex);

			assertEquals(actualOutput, expectValue);
		}
	}

	@Nested
	@DisplayName("Tests for DefGICurve method")
	class DefGICurveTest {
		@Test
		void testTooSmallIndex() {
			short invalidIndex = -1; // Choose an invalid index for testing
			assertThrows(
					SpeciesErrorException.class, () -> Sindxdll.DefGICurve(invalidIndex),
					"DefGICurve should throw SpeciesErrorException for invalid index"
			);
		}

		@Test
		void testTooBigIndex() {
			short invalidIndex = 135; // Choose an invalid index for testing
			assertThrows(
					SpeciesErrorException.class, () -> Sindxdll.DefGICurve(invalidIndex),
					"DefGICurve should throw SpeciesErrorException for invalid index"
			);
		}

		private void testHelper(int inputIndex, int expectedValue) { // helper function to reduce repetive
																		// code
			short actualValue = Sindxdll.DefGICurve((short) inputIndex);
			assertEquals(actualValue, expectedValue);
		}

		@Test
		void testSI_SPEC_BA() {
			testHelper(SI_SPEC_BA, SI_BA_NIGHGI);
		}

		@Test
		void testBL() {
			testHelper(SI_SPEC_BL, SI_BL_THROWERGI);
		}

		@Test
		void testCWI() {
			testHelper(SI_SPEC_CWI, SI_CWI_NIGHGI);
		}

		@Test
		void testFDC() {
			testHelper(SI_SPEC_FDC, SI_FDC_NIGHGI);
		}

		@Test
		void testFDI() {
			testHelper(SI_SPEC_FDI, SI_FDI_NIGHGI);
		}

		@Test
		void testHWC() {
			testHelper(SI_SPEC_HWC, SI_HWC_NIGHGI99);
		}

		@Test
		void testHWI() {
			testHelper(SI_SPEC_HWI, SI_HWI_NIGHGI);
		}

		@Test
		void testLW() {
			testHelper(SI_SPEC_LW, SI_LW_NIGHGI);
		}

		@Test
		void testPLI() {
			testHelper(SI_SPEC_PLI, SI_PLI_NIGHGI97);
		}

		@Test
		void testPY() {
			testHelper(SI_SPEC_PY, SI_PY_NIGHGI);
		}

		@Test
		void testSE() {
			testHelper(SI_SPEC_SE, SI_SE_NIGHGI);
		}

		@Test
		void testSS() {
			testHelper(SI_SPEC_SS, SI_SS_NIGHGI99);
		}

		@Test
		void testSW() {
			testHelper(SI_SPEC_SW, SI_SW_NIGHGI2004);
		}

		@Test
		void testIndexNotInSwitch() {
			short invalidIndex = 10; // Choose an index that could feasibly exist but isn't in the switch for testing
			assertThrows(
					NoAnswerException.class, () -> Sindxdll.DefGICurve(invalidIndex),
					"DefGICurve should throw NoAnswerException for invalid index"
			);
		}
	}

	@Nested
	@DisplayName("Tests for DefCurveEst method")
	class DefCurveEstTest {
		@BeforeAll
		static void beforeAll() {
			System.out.println("Before all tests of DefCurveEst");
		}

		@AfterAll
		static void afterAll() {
			System.out.println("After all tests of DefCurveEst");
		}

		@Test
		void testEstTooSmallIndex() {
			short invalidIndex = -1; // Choose an invalid index for testing
			assertThrows(
					SpeciesErrorException.class, () -> Sindxdll.DefCurveEst(invalidIndex, (short) 0),
					"DefCurveEst should throw SpeciesErrorException for invalid index"
			);
		}

		@Test
		void testTooBigIndex() {
			short invalidIndex = 135; // Choose an invalid index for testing
			assertThrows(
					SpeciesErrorException.class, () -> Sindxdll.DefCurveEst(invalidIndex, (short) 0),
					"DefGICurve should throw SpeciesErrorException for invalid index"
			);
		}

		@Test
		void testInvalidSpeciesIndex() {
			assertThrows(SpeciesErrorException.class, () -> {
				Sindxdll.DefCurveEst((short) SI_MAX_SPECIES, (short) SI_ESTAB_NAT);
			});
		}

		@Test
		void testValidSpeciesIndexAndEstabSI_ESTAB_NAT() {
			short result = Sindxdll.DefCurveEst((short) 100, (short) SI_ESTAB_NAT);
			assertEquals(SI_SW_GOUDIE_NATAC, result);
		}

		@Test
		void testValidSpeciesIndexAndEstabSI_ESTAB_PLA() {
			short result = Sindxdll.DefCurveEst((short) 100, (short) SI_ESTAB_PLA);
			assertEquals(SI_SW_GOUDIE_PLAAC, result);
		}

		@Test
		void testInvalidEstablishment() {
			assertThrows(EstablishmentErrorException.class, () -> {
				Sindxdll.DefCurveEst((short) SI_SPEC_SW, (short) -1);
			});
		}

		@Test
		void testNoCurvesDefined() {
			assertThrows(NoAnswerException.class, () -> {
				Sindxdll.DefCurveEst((short) 1, (short) SI_ESTAB_NAT);
			});
		}

		@Test
		void testDefaultCase() {
			short result = Sindxdll.DefCurveEst((short) 4, (short) 0);
			assertEquals(SI_ACB_HUANGAC, result);
		}
	}

	@Nested
	@DisplayName("Tests for FirstCurves method")
	class FirstCurveTest {
		@Test
		void testTooSmallIndex() {
			short invalidIndex = -1; // Choose an invalid index for testing
			assertThrows(
					SpeciesErrorException.class, () -> Sindxdll.FirstCurve(invalidIndex),
					"FirstCurve should throw SpeciesErrorException for invalid index"
			);
		}

		@Test
		void testTooBigIndex() {
			short invalidIndex = 135; // Choose an invalid index for testing
			assertThrows(
					SpeciesErrorException.class, () -> Sindxdll.FirstCurve(invalidIndex),
					"FirstCurve should throw SpeciesErrorException for invalid index"
			);
		}

		@Test
		void testNoCurvesDefined() {
			short invalidIndex = 0;
			assertThrows(
					NoAnswerException.class, () -> Sindxdll.FirstCurve(invalidIndex),
					"FirstCurve should throw NoAnswerException for invalid index"
			);
		}

		@Test
		void testDefaultCase() {
			short validIndex = 4;
			short result = Sindxdll.FirstCurve(validIndex);
			assertEquals(result, 97); // SI_ACB_START
		}
	}

	@Nested
	@DisplayName("Tests for NextCurve method")
	class NextCurveTest {
		@Test
		void testTooSmallSPIndex() {
			short invalidIndex = -1; // Choose an invalid index for testing
			assertThrows(
					SpeciesErrorException.class, () -> Sindxdll.NextCurve(invalidIndex, (short) 0),
					"NextCurve should throw SpeciesErrorException for invalid index"
			);
		}

		@Test
		void testTooBigSPIndex() {
			short invalidIndex = 135; // Choose an invalid index for testing
			assertThrows(
					SpeciesErrorException.class, () -> Sindxdll.NextCurve(invalidIndex, (short) 0),
					"NextCurve should throw SpeciesErrorException for invalid index"
			);
		}

		@Test
		void testTooSmallCUIndex() {
			short invalidIndex = -1; // Choose an invalid index for testing
			assertThrows(
					CurveErrorException.class, () -> Sindxdll.NextCurve((short) 0, invalidIndex),
					"NextCurve should throw CurveErrorException for invalid index"
			);
		}

		@Test
		void testTooBigCUIndex() {
			short invalidIndex = SI_MAX_CURVES; // Choose an invalid index for testing
			assertThrows(
					CurveErrorException.class, () -> Sindxdll.NextCurve((short) 0, invalidIndex),
					"NextCurve should throw CurveErrorException for invalid index"
			);
		}

		@Test
		void testCurveSpeciesMismatch() {
			short spIndex = 0;
			short cuIndex = 0;
			assertThrows(CurveErrorException.class, () -> Sindxdll.NextCurve(spIndex, cuIndex));
		}

		@Test
		void testSI_ACB_HUANGAC() {
			short sp_index = SI_SPEC_ACB;
			short cu_index = SI_ACB_HUANGAC;

			short expectResut = SI_ACB_HUANG;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_ACB_HUANG() {
			short cu_index = SI_ACB_HUANG;
			short sp_index = SI_SPEC_ACB;
			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_ACT_THROWERAC() {
			short sp_index = SI_SPEC_ACT;
			short cu_index = SI_ACT_THROWERAC;

			short expectResut = SI_ACT_THROWER;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_ACT_THROWER() {
			short sp_index = SI_SPEC_ACT;
			short cu_index = SI_ACT_THROWER;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_AT_NIGH() {
			short sp_index = SI_SPEC_AT;
			short cu_index = SI_AT_NIGH;

			short expectResut = SI_AT_CHEN;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_AT_CHEN() {
			short sp_index = SI_SPEC_AT;
			short cu_index = SI_AT_CHEN;

			short expectResut = SI_AT_HUANG;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_AT_HUANG() {
			short sp_index = SI_SPEC_AT;
			short cu_index = SI_AT_HUANG;

			short expectResut = SI_AT_CIESZEWSKI;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_AT_CIESZEWSKI() {
			short sp_index = SI_SPEC_AT;
			short cu_index = SI_AT_CIESZEWSKI;

			short expectResut = SI_AT_GOUDIE;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_AT_GOUDIE() {
			short sp_index = SI_SPEC_AT;
			short index = SI_AT_GOUDIE;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, index));
		}

		@Test
		void testSI_BA_NIGHGI() {
			short sp_index = SI_SPEC_BA;
			short cu_index = SI_BA_NIGHGI;

			short expectResut = SI_BA_NIGH;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_BA_NIGH() {
			short sp_index = SI_SPEC_BA;
			short cu_index = SI_BA_NIGH;

			short expectResut = SI_BA_KURUCZ82AC;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_BA_KURUCZ82AC() {
			short sp_index = SI_SPEC_BA;
			short cu_index = SI_BA_KURUCZ82AC;

			short expectResut = SI_BA_DILUCCA;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_BA_DILUCCA() {
			short sp_index = SI_SPEC_BA;
			short cu_index = SI_BA_DILUCCA;

			short expectResut = SI_BA_KURUCZ86;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_BA_KURUCZ86() {
			short sp_index = SI_SPEC_BA;
			short cu_index = SI_BA_KURUCZ86;

			short expectResut = SI_BA_KURUCZ82;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_BA_KURUCZ82() {
			short sp_index = SI_SPEC_BA;
			short cu_index = SI_BA_KURUCZ82;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_BL_CHENAC() {
			short sp_index = SI_SPEC_BL;
			short cu_index = SI_BL_CHENAC;

			short expectResut = SI_BL_CHEN;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_BL_CHEN() {
			short sp_index = SI_SPEC_BL;
			short cu_index = SI_BL_CHEN;

			short expectResut = SI_BL_THROWERGI;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_BL_THROWERGI() {
			short sp_index = SI_SPEC_BL;
			short cu_index = SI_BL_THROWERGI;

			short expectResut = SI_BL_KURUCZ82;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_BL_KURUCZ82() {
			short sp_index = SI_SPEC_BL;
			short cu_index = SI_BL_KURUCZ82;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_BP_CURTISAC() {
			short sp_index = SI_SPEC_BP;
			short cu_index = SI_BP_CURTISAC;

			short expectResut = SI_BP_CURTIS;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_BP_CURTIS() {
			short sp_index = SI_SPEC_BP;
			short cu_index = SI_BP_CURTIS;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_CWC_NIGH() {
			short sp_index = SI_SPEC_CWC;
			short cu_index = SI_CWC_NIGH;

			short expectResut = SI_CWC_KURUCZAC;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_CWC_KURUCZAC() {
			short sp_index = SI_SPEC_CWC;
			short cu_index = SI_CWC_KURUCZAC;

			short expectResut = SI_CWC_KURUCZ;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_CWC_KURUCZ() {
			short sp_index = SI_SPEC_CWC;
			short cu_index = SI_CWC_KURUCZ;

			short expectResut = SI_CWC_BARKER;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_CWC_BARKER() {
			short sp_index = SI_SPEC_CWC;
			short cu_index = SI_CWC_BARKER;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_CWI_NIGH() {
			short sp_index = SI_SPEC_CWI;
			short cu_index = SI_CWI_NIGH;

			short expectResut = SI_CWI_NIGHGI;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_CWI_NIGHGI() {
			short sp_index = SI_SPEC_CWI;
			short cu_index = SI_CWI_NIGHGI;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_DR_NIGH() {
			short sp_index = SI_SPEC_DR;
			short cu_index = SI_DR_NIGH;

			short expectResut = SI_DR_HARRING;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_DR_HARRING() {
			short sp_index = SI_SPEC_DR;
			short cu_index = SI_DR_HARRING;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_EP_NIGH() {
			short sp_index = SI_SPEC_EP;
			short cu_index = SI_EP_NIGH;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_FDC_BRUCEAC() {
			short sp_index = SI_SPEC_FDC;
			short cu_index = SI_FDC_BRUCEAC;

			short expectResut = SI_FDC_NIGHTA;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_FDC_NIGHTA() {
			short sp_index = SI_SPEC_FDC;
			short cu_index = SI_FDC_NIGHTA;

			short expectResut = SI_FDC_NIGHGI;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_FDC_NIGHGI() {
			short sp_index = SI_SPEC_FDC;
			short cu_index = SI_FDC_NIGHGI;

			short expectResut = SI_FDC_BRUCE;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_FDC_BRUCE() {
			short sp_index = SI_SPEC_FDC;
			short cu_index = SI_FDC_BRUCE;

			short expectResut = SI_FDC_COCHRAN;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_FDC_COCHRAN() {
			short sp_index = SI_SPEC_FDC;
			short cu_index = SI_FDC_COCHRAN;

			short expectResut = SI_FDC_KING;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_FDC_KING() {
			short sp_index = SI_SPEC_FDC;
			short cu_index = SI_FDC_KING;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_FDI_THROWERAC() {
			short sp_index = SI_SPEC_FDI;
			short cu_index = SI_FDI_THROWERAC;

			short expectResut = SI_FDI_NIGHGI;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_FDI_NIGHGI() {
			short sp_index = SI_SPEC_FDI;
			short cu_index = SI_FDI_NIGHGI;

			short expectResut = SI_FDI_HUANG_PLA;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_FDI_HUANG_PLA() {
			short sp_index = SI_SPEC_FDI;
			short cu_index = SI_FDI_HUANG_PLA;

			short expectResut = SI_FDI_HUANG_NAT;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_FDI_HUANG_NAT() {
			short sp_index = SI_SPEC_FDI;
			short cu_index = SI_FDI_HUANG_NAT;

			short expectResut = SI_FDI_MILNER;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_FDI_MILNER() {
			short sp_index = SI_SPEC_FDI;
			short cu_index = SI_FDI_MILNER;

			short expectResut = SI_FDI_THROWER;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_FDI_THROWER() {
			short sp_index = SI_SPEC_FDI;
			short cu_index = SI_FDI_THROWER;

			short expectResut = SI_FDI_VDP_MONT;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_FDI_VDP_MONT() {
			short sp_index = SI_SPEC_FDI;
			short cu_index = SI_FDI_VDP_MONT;

			short expectResut = SI_FDI_VDP_WASH;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_FDI_VDP_WASH() {
			short sp_index = SI_SPEC_FDI;
			short cu_index = SI_FDI_VDP_WASH;

			short expectResut = SI_FDI_MONS_DF;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_FDI_MONS_DF() {
			short sp_index = SI_SPEC_FDI;
			short cu_index = SI_FDI_MONS_DF;

			short expectResut = SI_FDI_MONS_GF;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_FDI_MONS_GF() {
			short sp_index = SI_SPEC_FDI;
			short cu_index = SI_FDI_MONS_GF;

			short expectResut = SI_FDI_MONS_WRC;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_FDI_MONS_WRC() {
			short sp_index = SI_SPEC_FDI;
			short cu_index = SI_FDI_MONS_WRC;

			short expectResut = SI_FDI_MONS_WH;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_FDI_MONS_WH() {
			short sp_index = SI_SPEC_FDI;
			short cu_index = SI_FDI_MONS_WH;

			short expectResut = SI_FDI_MONS_SAF;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_FDI_MONS_SAF() {
			short sp_index = SI_SPEC_FDI;
			short cu_index = SI_FDI_MONS_SAF;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_HM_MEANSAC() {
			short sp_index = SI_SPEC_HM;
			short cu_index = SI_HM_MEANSAC;

			short expectResut = SI_HM_MEANS;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_HM_MEANS() {
			short sp_index = SI_SPEC_HM;
			short cu_index = SI_HM_MEANS;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_HWC_WILEYAC() {
			short sp_index = SI_SPEC_HWC;
			short cu_index = SI_HWC_WILEYAC;

			short expectResut = SI_HWC_NIGHGI99;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_HWC_NIGHGI99() {
			short sp_index = SI_SPEC_HWC;
			short cu_index = SI_HWC_NIGHGI99;

			short expectResut = SI_HWC_FARR;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_HWC_FARR() {
			short sp_index = SI_SPEC_HWC;
			short cu_index = SI_HWC_FARR;

			short expectResut = SI_HWC_BARKER;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_HWC_BARKER() {
			short sp_index = SI_SPEC_HWC;
			short cu_index = SI_HWC_BARKER;

			short expectResut = SI_HWC_WILEY;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_HWC_WILEY() {
			short sp_index = SI_SPEC_HWC;
			short cu_index = SI_HWC_WILEY;

			short expectResut = SI_HWC_WILEY_BC;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_HWC_WILEY_BC() {
			short sp_index = SI_SPEC_HWC;
			short cu_index = SI_HWC_WILEY_BC;

			short expectResut = SI_HWC_WILEY_MB;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_HWC_WILEY_MB() {
			short sp_index = SI_SPEC_HWC;
			short cu_index = SI_HWC_WILEY_MB;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_HWI_NIGH() {
			short sp_index = SI_SPEC_HWI;
			short cu_index = SI_HWI_NIGH;

			short expectResut = SI_HWI_NIGHGI;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_HWI_NIGHGI() {
			short sp_index = SI_SPEC_HWI;
			short cu_index = SI_HWI_NIGHGI;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_LW_NIGH() {
			short sp_index = SI_SPEC_LW;
			short cu_index = SI_LW_NIGH;

			short expectResut = SI_LW_NIGHGI;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_LW_NIGHGI() {
			short sp_index = SI_SPEC_LW;
			short cu_index = SI_LW_NIGHGI;

			short expectResut = SI_LW_MILNER;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_LW_MILNER() {
			short sp_index = SI_SPEC_LW;
			short cu_index = SI_LW_MILNER;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_PJ_HUANG() {
			short sp_index = SI_SPEC_PJ;
			short cu_index = SI_PJ_HUANG;

			short expectResut = SI_PJ_HUANGAC;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_PJ_HUANGAC() {
			short sp_index = SI_SPEC_PJ;
			short cu_index = SI_PJ_HUANGAC;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_PL_CHEN() {
			short sp_index = SI_SPEC_PLI;
			short cu_index = SI_PL_CHEN;

			short expectResut = SI_PLI_THROWNIGH;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_PLI_THROWNIGH() {
			short sp_index = SI_SPEC_PLI;
			short cu_index = SI_PLI_THROWNIGH;

			short expectResut = SI_PLI_NIGHTA98;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_PLI_NIGHTA98() {
			short sp_index = SI_SPEC_PLI;
			short cu_index = SI_PLI_NIGHTA98;

			short expectResut = SI_PLI_NIGHGI97;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_PLI_NIGHGI97() {
			short sp_index = SI_SPEC_PLI;
			short cu_index = SI_PLI_NIGHGI97;

			short expectResut = SI_PLI_HUANG_PLA;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_PLI_HUANG_PLA() {
			short sp_index = SI_SPEC_PLI;
			short cu_index = SI_PLI_HUANG_PLA;

			short expectResut = SI_PLI_HUANG_NAT;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_PLI_HUANG_NAT() {
			short sp_index = SI_SPEC_PLI;
			short cu_index = SI_PLI_HUANG_NAT;

			short expectResut = SI_PLI_THROWER;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_PLI_THROWER() {
			short sp_index = SI_SPEC_PLI;
			short cu_index = SI_PLI_THROWER;

			short expectResut = SI_PLI_MILNER;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_PLI_MILNER() {
			short sp_index = SI_SPEC_PLI;
			short cu_index = SI_PLI_MILNER;

			short expectResut = SI_PLI_CIESZEWSKI;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_PLI_CIESZEWSKI() {
			short sp_index = SI_SPEC_PLI;
			short cu_index = SI_PLI_CIESZEWSKI;

			short expectResut = SI_PLI_GOUDIE_DRY;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_PLI_GOUDIE_DRY() {
			short sp_index = SI_SPEC_PLI;
			short cu_index = SI_PLI_GOUDIE_DRY;

			short expectResut = SI_PLI_GOUDIE_WET;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_PLI_GOUDIE_WET() {
			short sp_index = SI_SPEC_PLI;
			short cu_index = SI_PLI_GOUDIE_WET;

			short expectResut = SI_PLI_DEMPSTER;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_PLI_DEMPSTER() {
			short sp_index = SI_SPEC_PLI;
			short cu_index = SI_PLI_DEMPSTER;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_PW_CURTISAC() {
			short sp_index = SI_SPEC_PW;
			short cu_index = SI_PW_CURTISAC;

			short expectResut = SI_PW_CURTIS;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_PW_CURTIS() {
			short sp_index = SI_SPEC_PW;
			short cu_index = SI_PW_CURTIS;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_PY_NIGH() {
			short sp_index = SI_SPEC_PY;
			short cu_index = SI_PY_NIGH;

			short expectResut = SI_PY_NIGHGI;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_PY_NIGHGI() {
			short sp_index = SI_SPEC_PY;
			short cu_index = SI_PY_NIGHGI;

			short expectResut = SI_PY_HANNAC;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_PY_HANNAC() {
			short sp_index = SI_SPEC_PY;
			short cu_index = SI_PY_HANNAC;

			short expectResut = SI_PY_MILNER;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_PY_MILNER() {
			short sp_index = SI_SPEC_PY;
			short cu_index = SI_PY_MILNER;

			short expectResut = SI_PY_HANN;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_PY_HANN() {
			short sp_index = SI_SPEC_PY;
			short cu_index = SI_PY_HANN;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_SB_NIGH() {
			short sp_index = SI_SPEC_SB;
			short cu_index = SI_SB_NIGH;

			short expectResut = SI_SB_HUANG;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SB_HUANG() {
			short sp_index = SI_SPEC_SB;
			short cu_index = SI_SB_HUANG;

			short expectResut = SI_SB_CIESZEWSKI;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SB_CIESZEWSKI() {
			short sp_index = SI_SPEC_SB;
			short cu_index = SI_SB_CIESZEWSKI;

			short expectResut = SI_SB_KER;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SB_KER() {
			short sp_index = SI_SPEC_SB;
			short cu_index = SI_SB_KER;

			short expectResut = SI_SB_DEMPSTER;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SB_DEMPSTER() {
			short sp_index = SI_SPEC_SB;
			short cu_index = SI_SB_DEMPSTER;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_SE_CHENAC() {
			short sp_index = SI_SPEC_SE;
			short cu_index = SI_SE_CHENAC;

			short expectResut = SI_SE_CHEN;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SE_CHEN() {
			short sp_index = SI_SPEC_SE;
			short cu_index = SI_SE_CHEN;

			short expectResut = SI_SE_NIGHGI;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SE_NIGHGI() {
			short sp_index = SI_SPEC_SE;
			short cu_index = SI_SE_NIGHGI;

			short expectResut = SI_SE_NIGH;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SE_NIGH() {
			short sp_index = SI_SPEC_SE;
			short cu_index = SI_SE_NIGH;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_SS_NIGHGI99() {
			short sp_index = SI_SPEC_SS;
			short cu_index = SI_SS_NIGHGI99;

			short expectResut = SI_SS_NIGH;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SS_NIGH() {
			short sp_index = SI_SPEC_SS;
			short cu_index = SI_SS_NIGH;

			short expectResut = SI_SS_GOUDIE;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SS_GOUDIE() {
			short sp_index = SI_SPEC_SS;
			short cu_index = SI_SS_GOUDIE;

			short expectResut = SI_SS_FARR;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SS_FARR() {
			short sp_index = SI_SPEC_SS;
			short cu_index = SI_SS_FARR;

			short expectResut = SI_SS_BARKER;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SS_BARKER() {
			short sp_index = SI_SPEC_SS;
			short cu_index = SI_SS_BARKER;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_SW_GOUDNIGH() {
			short sp_index = SI_SPEC_SW;
			short cu_index = SI_SW_GOUDNIGH;

			short expectResut = SI_SW_HU_GARCIA;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SW_HU_GARCIA() {
			short sp_index = SI_SPEC_SW;
			short cu_index = SI_SW_HU_GARCIA;

			short expectResut = SI_SW_NIGHTA;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SW_NIGHTA() {
			short sp_index = SI_SPEC_SW;
			short cu_index = SI_SW_NIGHTA;

			short expectResut = SI_SW_NIGHGI2004;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SW_NIGHGI2004() {
			short sp_index = SI_SPEC_SW;
			short cu_index = SI_SW_NIGHGI2004;

			short expectResut = SI_SW_HUANG_PLA;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SW_HUANG_PLA() {
			short sp_index = SI_SPEC_SW;
			short cu_index = SI_SW_HUANG_PLA;

			short expectResut = SI_SW_HUANG_NAT;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SW_HUANG_NAT() {
			short sp_index = SI_SPEC_SW;
			short cu_index = SI_SW_HUANG_NAT;

			short expectResut = SI_SW_THROWER;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SW_THROWER() {
			short sp_index = SI_SPEC_SW;
			short cu_index = SI_SW_THROWER;

			short expectResut = SI_SW_CIESZEWSKI;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SW_CIESZEWSKI() {
			short sp_index = SI_SPEC_SW;
			short cu_index = SI_SW_CIESZEWSKI;

			short expectResut = SI_SW_KER_PLA;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SW_KER_PLA() {
			short sp_index = SI_SPEC_SW;
			short cu_index = SI_SW_KER_PLA;

			short expectResut = SI_SW_KER_NAT;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SW_KER_NAT() {
			short sp_index = SI_SPEC_SW;
			short cu_index = SI_SW_KER_NAT;

			short expectResut = SI_SW_GOUDIE_PLAAC;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SW_GOUDIE_PLAAC() {
			short sp_index = SI_SPEC_SW;
			short cu_index = SI_SW_GOUDIE_PLAAC;

			short expectResut = SI_SW_GOUDIE_PLA;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SW_GOUDIE_PLA() {
			short sp_index = SI_SPEC_SW;
			short cu_index = SI_SW_GOUDIE_PLA;

			short expectResut = SI_SW_GOUDIE_NATAC;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SW_GOUDIE_NATAC() {
			short sp_index = SI_SPEC_SW;
			short cu_index = SI_SW_GOUDIE_NATAC;

			short expectResut = SI_SW_GOUDIE_NAT;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SW_GOUDIE_NAT() {
			short sp_index = SI_SPEC_SW;
			short cu_index = SI_SW_GOUDIE_NAT;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}
	}

	@Nested
	@DisplayName("Tests for CurveName method")
	class CurveNameTest {
	}

	@Nested
	@DisplayName("Tests for CurveUse method")
	class CurveUseTest {
	}

	@Test
	void testHtAgeToSIError() {
		double[] site = new double[2];
		assertThrows(
				LessThan13Exception.class, () -> Sindxdll.HtAgeToSI((short) 0, 0.0, (short) 1, 1.2, (short) 0, site)
		);
	}

	@Test
	void testHtAgeToSIDefault() {
		double[] site = new double[2];
		double height = 2;
		double age = 1;

		double expectedResult = 0;
		double actualResult = Sindxdll.HtAgeToSI((short) SI_FDI_THROWER, age, (short) 1, height, (short) 1, site);
		double expectedSiteValue = 0.39 + 0.3104 * height + 33.3828 * height / age;

		assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		assertThat(site[0], closeTo(expectedSiteValue, ERROR_TOLERANCE));
	}

}