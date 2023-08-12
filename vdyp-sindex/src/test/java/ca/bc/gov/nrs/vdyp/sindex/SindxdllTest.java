package ca.bc.gov.nrs.vdyp.sindex;

import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class SindxdllTest {
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

	@Test
	void testNextSpeciesValidIndex() {
		short inputIndex = 2; // Choose a valid index for testing
		short expectedOutput = (short) (inputIndex + 1);

		short actualOutput = Sindxdll.NextSpecies(inputIndex);

		assertEquals(expectedOutput, actualOutput, "NextSpecies should return the next species index");
	}

	@Test
	void testNextSpeciesTooSmallIndex() {
		short invalidIndex = -1; // Choose an invalid index for testing
		assertThrows(
				SpeciesErrorException.class, () -> Sindxdll.NextSpecies(invalidIndex),
				"NextSpecies should throw SpeciesErrorException for invalid index"
		);
	}

	@Test
	void testNextSpeciesTooBigIndex() {
		short invalidIndex = 135; // Choose an invalid index for testing
		assertThrows(
				SpeciesErrorException.class, () -> Sindxdll.NextSpecies(invalidIndex),
				"NextSpecies should throw SpeciesErrorException for invalid index"
		);
	}

	@Test
	void testNextSpeciesLastIndex() {
		short lastIndex = 134; // Use the value of SI_SPEC_END for testing
		assertThrows(
				NoAnswerException.class, () -> Sindxdll.NextSpecies(lastIndex),
				"NextSpecies should throw NoAnswerException for last defined species index"
		);
	}

	@Test
	void testSpecCodeTooSmallIndex() {
		short invalidIndex = -1; // Choose an invalid index for testing
		assertThrows(
				IllegalArgumentException.class, () -> Sindxdll.SpecCode(invalidIndex),
				"SpecCode should throw IllegalArgumentException for invalid index"
		);
	}

	@Test
	void testSpecCodeTooBigIndex() {
		short invalidIndex = 135; // Choose an invalid index for testing
		assertThrows(
				IllegalArgumentException.class, () -> Sindxdll.SpecCode(invalidIndex),
				"SpecCode should throw IllegalArgumentException for invalid index"
		);
	}

	@Test
	void testSpecUseTooSmallIndex() {
		short invalidIndex = -1; // Choose an invalid index for testing
		assertThrows(
				SpeciesErrorException.class, () -> Sindxdll.SpecUse(invalidIndex),
				"SpecUse should throw SpeciesErrorException for invalid index"
		);
	}

	@Test
	void testSpecUseTooBigIndex() {
		short invalidIndex = 135; // Choose an invalid index for testing
		assertThrows(
				SpeciesErrorException.class, () -> Sindxdll.SpecUse(invalidIndex),
				"SpecUse should throw SpeciesErrorException for invalid index"
		);
	}

	private void testSpecUseHelper(int inputIndex, int expectedValue) { // helper function to reduce repetive code
		short actualValue = Sindxdll.SpecUse((short) inputIndex);
		assertEquals((int) actualValue, expectedValue);
	}

	@Test
	void testSpecUseSI_SPEC_A() {
		testSpecUseHelper(SI_SPEC_A, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_ABAL() {
		testSpecUseHelper(SI_SPEC_ABAL, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_ABCO() {
		testSpecUseHelper(SI_SPEC_ABCO, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_AC() {
		testSpecUseHelper(SI_SPEC_AC, 0x04);
	}

	@Test
	void testSpecUseSI_SPEC_ACB() {
		testSpecUseHelper(SI_SPEC_ACB, 0x07);
	}

	@Test
	void testSpecUseSI_SPEC_ACT() {
		testSpecUseHelper(SI_SPEC_ACT, 0x04);
	}

	@Test
	void testSpecUseSI_SPEC_AD() {
		testSpecUseHelper(SI_SPEC_AD, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_AH() {
		testSpecUseHelper(SI_SPEC_AH, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_AT() {
		testSpecUseHelper(SI_SPEC_AT, 0x06);
	}

	@Test
	void testSpecUseSI_SPEC_AX() {
		testSpecUseHelper(SI_SPEC_AX, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_B() {
		testSpecUseHelper(SI_SPEC_B, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_BA() {
		testSpecUseHelper(SI_SPEC_BA, 0x05);
	}

	@Test
	void testSpecUseSI_SPEC_BB() {
		testSpecUseHelper(SI_SPEC_BB, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_BC() {
		testSpecUseHelper(SI_SPEC_BC, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_BG() {
		testSpecUseHelper(SI_SPEC_BG, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_BI() {
		testSpecUseHelper(SI_SPEC_BI, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_BL() {
		testSpecUseHelper(SI_SPEC_BL, 0x06);
	}

	@Test
	void testSpecUseSI_SPEC_BM() {
		testSpecUseHelper(SI_SPEC_BM, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_BP() {
		testSpecUseHelper(SI_SPEC_BP, 0x05);
	}

	@Test
	void testSpecUseSI_SPEC_C() {
		testSpecUseHelper(SI_SPEC_C, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_CI() {
		testSpecUseHelper(SI_SPEC_CI, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_CP() {
		testSpecUseHelper(SI_SPEC_CP, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_CW() {
		testSpecUseHelper(SI_SPEC_CW, 0x05);
	}

	@Test
	void testSpecUseSI_SPEC_CWC() {
		testSpecUseHelper(SI_SPEC_CWC, 0x05);
	}

	@Test
	void testSpecUseSI_SPEC_CWI() {
		testSpecUseHelper(SI_SPEC_CWI, 0x06);
	}

	@Test
	void testSpecUseSI_SPEC_CY() {
		testSpecUseHelper(SI_SPEC_CY, 0x01);
	}

	@Test
	void testSpecUseSI_SPEC_D() {
		testSpecUseHelper(SI_SPEC_D, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_DG() {
		testSpecUseHelper(SI_SPEC_DG, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_DM() {
		testSpecUseHelper(SI_SPEC_DM, 0x02);
	}

	@Test
	void testSpecUseSI_SPEC_DR() {
		testSpecUseHelper(SI_SPEC_DR, 0x05);
	}

	@Test
	void testSpecUseSI_SPEC_E() {
		testSpecUseHelper(SI_SPEC_E, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_EA() {
		testSpecUseHelper(SI_SPEC_EA, 0x02);
	}

	@Test
	void testSpecUseSI_SPEC_EB() {
		testSpecUseHelper(SI_SPEC_EB, 0x02);
	}

	@Test
	void testSpecUseSI_SPEC_EE() {
		testSpecUseHelper(SI_SPEC_EE, 0x02);
	}

	@Test
	void testSpecUseSI_SPEC_EP() {
		testSpecUseHelper(SI_SPEC_EP, 0x06);
	}

	@Test
	void testSpecUseSI_SPEC_ES() {
		testSpecUseHelper(SI_SPEC_ES, 0x02);
	}

	@Test
	void testSpecUseSI_SPEC_EW() {
		testSpecUseHelper(SI_SPEC_EW, 0x02);
	}

	@Test
	void testSpecUseSI_SPEC_EXP() {
		testSpecUseHelper(SI_SPEC_EXP, 0x02);
	}

	@Test
	void testSpecUseSI_SPEC_FD() {
		testSpecUseHelper(SI_SPEC_FD, 0x05);
	}

	@Test
	void testSpecUseSI_SPEC_FDC() {
		testSpecUseHelper(SI_SPEC_FDC, 0x05);
	}

	@Test
	void testSpecUseSI_SPEC_FDI() {
		testSpecUseHelper(SI_SPEC_FDI, 0x06);
	}

	@Test
	void testSpecUseSI_SPEC_G() {
		testSpecUseHelper(SI_SPEC_G, 0x01);
	}

	@Test
	void testSpecUseSI_SPEC_GP() {
		testSpecUseHelper(SI_SPEC_GP, 0x01);
	}

	@Test
	void testSpecUseSI_SPEC_GR() {
		testSpecUseHelper(SI_SPEC_GR, 0x01);
	}

	@Test
	void testSpecUseSI_SPEC_H() { // TODO: shorten these like others
		testSpecUseHelper(SI_SPEC_H, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_HM() {
		testSpecUseHelper(SI_SPEC_HM, 0x05);
	}

	@Test
	void testSpecUseSI_SPEC_HW() {
		testSpecUseHelper(SI_SPEC_HW, 0x05);
	}

	@Test
	void testSpecUseSI_SPEC_HWC() {
		testSpecUseHelper(SI_SPEC_HWC, 0x05);
	}

	@Test
	void testSpecUseSI_SPEC_HWI() {
		testSpecUseHelper(SI_SPEC_HWI, 0x06);
	}

	@Test
	void testSpecUseSI_SPEC_HXM() {
		testSpecUseHelper(SI_SPEC_HXM, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_IG() {
		testSpecUseHelper(SI_SPEC_IG, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_IS() {
		testSpecUseHelper(SI_SPEC_IS, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_J() {
		testSpecUseHelper(SI_SPEC_J, 0x02);
	}

	@Test
	void testSpecUseSI_SPEC_JR() {
		testSpecUseHelper(SI_SPEC_JR, 0x02);
	}

	@Test
	void testSpecUseSI_SPEC_K() {
		testSpecUseHelper(SI_SPEC_K, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_KC() {
		testSpecUseHelper(SI_SPEC_KC, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_L() {
		testSpecUseHelper(SI_SPEC_L, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_LA() {
		testSpecUseHelper(SI_SPEC_LA, 0x02);
	}

	@Test
	void testSpecUseSI_SPEC_LE() {
		testSpecUseHelper(SI_SPEC_LE, 0x02);
	}

	@Test
	void testSpecUseSI_SPEC_LT() {
		testSpecUseHelper(SI_SPEC_LT, 0x02);
	}

	@Test
	void testSpecUseSI_SPEC_LW() {
		testSpecUseHelper(SI_SPEC_LW, 0x06);
	}

	@Test
	void testSpecUseSI_SPEC_M() {
		testSpecUseHelper(SI_SPEC_M, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_MB() {
		testSpecUseHelper(SI_SPEC_MB, 0x01);
	}

	@Test
	void testSpecUseSI_SPEC_ME() {
		testSpecUseHelper(SI_SPEC_ME, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_MN() {
		testSpecUseHelper(SI_SPEC_MN, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_MR() {
		testSpecUseHelper(SI_SPEC_MR, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_MS() {
		testSpecUseHelper(SI_SPEC_MS, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_MV() {
		testSpecUseHelper(SI_SPEC_MV, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_OA() {
		testSpecUseHelper(SI_SPEC_OA, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_OB() {
		testSpecUseHelper(SI_SPEC_OB, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_OC() {
		testSpecUseHelper(SI_SPEC_OC, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_OD() {
		testSpecUseHelper(SI_SPEC_OD, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_OE() {
		testSpecUseHelper(SI_SPEC_OE, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_OF() {
		testSpecUseHelper(SI_SPEC_OF, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_OG() {
		testSpecUseHelper(SI_SPEC_OG, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_P() {
		testSpecUseHelper(SI_SPEC_P, 0x02);
	}

	@Test
	void testSpecUseSI_SPEC_PA() {
		testSpecUseHelper(SI_SPEC_PA, 0x02);
	}

	@Test
	void testSpecUseSI_SPEC_PF() {
		testSpecUseHelper(SI_SPEC_PF, 0x02);
	}

	@Test
	void testSpecUseSI_SPEC_PJ() {
		testSpecUseHelper(SI_SPEC_PJ, 0x02);
	}

	@Test
	void testSpecUseSI_SPEC_PL() {
		testSpecUseHelper(SI_SPEC_PL, 0x06);
	}

	@Test
	void testSpecUseSI_SPEC_PLC() {
		testSpecUseHelper(SI_SPEC_PLC, 0x01);
	}

	@Test
	void testSpecUseSI_SPEC_PLI() {
		testSpecUseHelper(SI_SPEC_PLI, 0x06);
	}

	@Test
	void testSpecUseSI_SPEC_PM() {
		testSpecUseHelper(SI_SPEC_PM, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_PR() {
		testSpecUseHelper(SI_SPEC_PR, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_PS() {
		testSpecUseHelper(SI_SPEC_PS, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_PW() {
		testSpecUseHelper(SI_SPEC_PW, 0x04);
	}

	@Test
	void testSpecUseSI_SPEC_PXJ() {
		testSpecUseHelper(SI_SPEC_PXJ, 0x02);
	}

	@Test
	void testSpecUseSI_SPEC_PY() {
		testSpecUseHelper(SI_SPEC_PY, 0x06);
	}

	@Test
	void testSpecUseSI_SPEC_Q() {
		testSpecUseHelper(SI_SPEC_Q, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_QE() {
		testSpecUseHelper(SI_SPEC_QE, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_QG() {
		testSpecUseHelper(SI_SPEC_QG, 0x01);
	}

	@Test
	void testSpecUseSI_SPEC_R() {
		testSpecUseHelper(SI_SPEC_R, 0x01);
	}

	@Test
	void testSpecUseSI_SPEC_RA() {
		testSpecUseHelper(SI_SPEC_RA, 0x01);
	}

	@Test
	void testSpecUseSI_SPEC_S() {
		testSpecUseHelper(SI_SPEC_S, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_SA() {
		testSpecUseHelper(SI_SPEC_SA, 0x02);
	}

	@Test
	void testSpecUseSI_SPEC_SB() {
		testSpecUseHelper(SI_SPEC_SB, 0x06);
	}

	@Test
	void testSpecUseSI_SPEC_SE() {
		testSpecUseHelper(SI_SPEC_SE, 0x06);
	}

	@Test
	void testSpecUseSI_SPEC_SI() {
		testSpecUseHelper(SI_SPEC_SI, 0x02);
	}

	@Test
	void testSpecUseSI_SPEC_SN() {
		testSpecUseHelper(SI_SPEC_SN, 0x02);
	}

	@Test
	void testSpecUseSI_SPEC_SS() {
		testSpecUseHelper(SI_SPEC_SS, 0x05);
	}

	@Test
	void testSpecUseSI_SPEC_SW() {
		testSpecUseHelper(SI_SPEC_SW, 0x06);
	}

	@Test
	void testSpecUseSI_SPEC_SX() {
		testSpecUseHelper(SI_SPEC_SX, 0x06);
	}

	@Test
	void testSpecUseSI_SPEC_SXB() {
		testSpecUseHelper(SI_SPEC_SXB, 0x02);
	}

	@Test
	void testSpecUseSI_SPEC_SXE() {
		testSpecUseHelper(SI_SPEC_SXE, 0x01);
	}

	@Test
	void testSpecUseSI_SPEC_SXL() {
		testSpecUseHelper(SI_SPEC_SXL, 0x01);
	}

	@Test
	void testSpecUseSI_SPEC_SXS() {
		testSpecUseHelper(SI_SPEC_SXS, 0x01);
	}

	@Test
	void testSpecUseSI_SPEC_SXW() {
		testSpecUseHelper(SI_SPEC_SXW, 0x02);
	}

	@Test
	void testSpecUseSI_SPEC_SXX() {
		testSpecUseHelper(SI_SPEC_SXX, 0x02);
	}

	@Test
	void testSpecUseSI_SPEC_T() {
		testSpecUseHelper(SI_SPEC_T, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_TW() {
		testSpecUseHelper(SI_SPEC_TW, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_U() {
		testSpecUseHelper(SI_SPEC_U, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_UA() {
		testSpecUseHelper(SI_SPEC_UA, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_UP() {
		testSpecUseHelper(SI_SPEC_UP, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_V() {
		testSpecUseHelper(SI_SPEC_V, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_VB() {
		testSpecUseHelper(SI_SPEC_VB, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_VP() {
		testSpecUseHelper(SI_SPEC_VP, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_VS() {
		testSpecUseHelper(SI_SPEC_VS, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_VV() {
		testSpecUseHelper(SI_SPEC_VV, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_W() {
		testSpecUseHelper(SI_SPEC_W, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_WA() {
		testSpecUseHelper(SI_SPEC_WA, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_WB() {
		testSpecUseHelper(SI_SPEC_WB, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_WD() {
		testSpecUseHelper(SI_SPEC_WD, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_WI() {
		testSpecUseHelper(SI_SPEC_WI, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_WP() {
		testSpecUseHelper(SI_SPEC_WP, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_WS() {
		testSpecUseHelper(SI_SPEC_WS, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_WT() {
		testSpecUseHelper(SI_SPEC_WT, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_X() {
		testSpecUseHelper(SI_SPEC_X, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_XC() {
		testSpecUseHelper(SI_SPEC_XC, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_XH() {
		testSpecUseHelper(SI_SPEC_XH, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_Y() {
		testSpecUseHelper(SI_SPEC_Y, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_YC() {
		testSpecUseHelper(SI_SPEC_YC, 0x01);
	}

	@Test
	void testSpecUseSI_SPEC_YP() {
		testSpecUseHelper(SI_SPEC_YP, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_Z() {
		testSpecUseHelper(SI_SPEC_Z, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_ZC() {
		testSpecUseHelper(SI_SPEC_ZC, 0x00);
	}

	@Test
	void testSpecUseSI_SPEC_ZH() {
		testSpecUseHelper(SI_SPEC_ZH, 0x00);
	}

	@Test
	void testDefCurveTooSmallIndex() {
		short invalidIndex = -1; // Choose an invalid index for testing
		assertThrows(
				SpeciesErrorException.class, () -> Sindxdll.DefCurve(invalidIndex),
				"DefCurve should throw SpeciesErrorException for invalid index"
		);
	}

	@Test
	void testDefCurveTooBigIndex() {
		short invalidIndex = 135; // Choose an invalid index for testing
		assertThrows(
				SpeciesErrorException.class, () -> Sindxdll.DefCurve(invalidIndex),
				"DefCurve should throw SpeciesErrorException for invalid index"
		);
	}

	@Test
	void testDefCurveLastSpeciesIndex() {
		short lastIndex = 134; // Choose the last index for testing
		assertThrows(
				NoAnswerException.class, () -> Sindxdll.DefCurve(lastIndex),
				"DefCurve should throw NoAnswerException for last index"
		);
	}

	@Test
	void testDefCurveValidIndex() {
		short validIndex = 0;
		short expectValue = -4; // SI_ERR_NO_ANS
		short actualOutput = Sindxdll.DefCurve(validIndex);

		assertEquals(actualOutput, expectValue);
	}

	@Test
	void testDefGICurveTooSmallIndex() {
		short invalidIndex = -1; // Choose an invalid index for testing
		assertThrows(
				SpeciesErrorException.class, () -> Sindxdll.DefGICurve(invalidIndex),
				"DefGICurve should throw SpeciesErrorException for invalid index"
		);
	}

	@Test
	void testDefGICurveTooBigIndex() {
		short invalidIndex = 135; // Choose an invalid index for testing
		assertThrows(
				SpeciesErrorException.class, () -> Sindxdll.DefGICurve(invalidIndex),
				"DefGICurve should throw SpeciesErrorException for invalid index"
		);
	}

	private void testDefGICurveHelper(int inputIndex, int expectedValue) { // helper function to reduce repetive
																			// code
		short actualValue = Sindxdll.DefGICurve((short) inputIndex);
		assertEquals(actualValue, expectedValue);
	}

	@Test
	void testDefGICurveSI_SPEC_BA() {
		testDefGICurveHelper(SI_SPEC_BA, SI_BA_NIGHGI);
	}

	@Test
	void testDefGICurveBL() {
		testDefGICurveHelper(SI_SPEC_BL, SI_BL_THROWERGI);
	}

	@Test
	void testDefGICurveCWI() {
		testDefGICurveHelper(SI_SPEC_CWI, SI_CWI_NIGHGI);
	}

	@Test
	void testDefGICurveFDC() {
		testDefGICurveHelper(SI_SPEC_FDC, SI_FDC_NIGHGI);
	}

	@Test
	void testDefGICurveFDI() {
		testDefGICurveHelper(SI_SPEC_FDI, SI_FDI_NIGHGI);
	}

	@Test
	void testDefGICurveHWC() {
		testDefGICurveHelper(SI_SPEC_HWC, SI_HWC_NIGHGI99);
	}

	@Test
	void testDefGICurveHWI() {
		testDefGICurveHelper(SI_SPEC_HWI, SI_HWI_NIGHGI);
	}

	@Test
	void testDefGICurveLW() {
		testDefGICurveHelper(SI_SPEC_LW, SI_LW_NIGHGI);
	}

	@Test
	void testDefGICurvePLI() {
		testDefGICurveHelper(SI_SPEC_PLI, SI_PLI_NIGHGI97);
	}

	@Test
	void testDefGICurvePY() {
		testDefGICurveHelper(SI_SPEC_PY, SI_PY_NIGHGI);
	}

	@Test
	void testDefGICurveSE() {
		testDefGICurveHelper(SI_SPEC_SE, SI_SE_NIGHGI);
	}

	@Test
	void testDefGICurveSS() {
		testDefGICurveHelper(SI_SPEC_SS, SI_SS_NIGHGI99);
	}

	@Test
	void testDefGICurveSW() {
		testDefGICurveHelper(SI_SPEC_SW, SI_SW_NIGHGI2004);
	}

	@Test
	void testDefGICurveIndexNotInSwitch() {
		short invalidIndex = 10; // Choose an index that could feasibly exist but isn't in the switch for testing
		assertThrows(
				NoAnswerException.class, () -> Sindxdll.DefGICurve(invalidIndex),
				"DefGICurve should throw NoAnswerException for invalid index"
		);
	}

	public static class DefCurveEstTest {

		@Test
		public void testValidSpeciesIndexAndEstab() {
			short result = Sindxdll.DefCurveEst((short) 0, (short) SI_ESTAB_NAT);
			assertEquals(SI_SW_GOUDIE_NATAC, result);
		}

		@Test
		public void testInvalidSpeciesIndex() {
			assertThrows(SpeciesErrorException.class, () -> {
				Sindxdll.DefCurveEst((short) SI_MAX_SPECIES, (short) SI_ESTAB_NAT);
			});
		}

		@Test
		public void testInvalidEstablishment() {
			assertThrows(EstablishmentErrorException.class, () -> {
				Sindxdll.DefCurveEst((short) SI_SPEC_SW, (short) -1);
			});
		}

		@Test
		public void testNoCurvesDefined() {
			assertThrows(NoAnswerException.class, () -> {
				Sindxdll.DefCurveEst((short) 1, (short) SI_ESTAB_NAT);
			});
		}

		@Test
		public void testDefaultCase() {
			short result = Sindxdll.DefCurveEst((short) 0, (short) SI_ESTAB_NAT);
			assertEquals(SI_ERR_NO_ANS, result);
		}
	}
}