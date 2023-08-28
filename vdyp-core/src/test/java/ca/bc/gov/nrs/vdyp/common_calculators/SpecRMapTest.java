package ca.bc.gov.nrs.vdyp.common_calculators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.ClassErrorException;
import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.CodeErrorException;

public class SpecRMapTest {
	// Taken from sindex.h
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

	@Test
	void testSpeciesMap() {
		for (short i = 0; i < SiteIndexNames.si_spec_code.length; i++) { // test all normal cases
			short expectedResult = i;
			short actualResult = SpecRMap.species_map(SiteIndexNames.si_spec_code[i]);
			assertEquals(expectedResult, actualResult);
		}

		assertThrows(CodeErrorException.class, () -> SpecRMap.species_map("Error causer"));
	}

	@Test
	void testSpeciesRemap() {

	}
}
