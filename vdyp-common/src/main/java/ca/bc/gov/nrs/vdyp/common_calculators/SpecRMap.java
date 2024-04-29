package ca.bc.gov.nrs.vdyp.common_calculators;

import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.CodeErrorException;
import static ca.bc.gov.nrs.vdyp.common_calculators.SiteIndexConstants.*;

/**
 * SpecRMap.java - determines the default species/curve index for a given species code. - initial species code
 * remappings provided by Inventory Branch. - species codes can be 1-3 letters, in upper or lower case.
 */
public class SpecRMap {

	public static int species_map(String sc) throws CodeErrorException {

		// This can be done more elegantly with Java
		String sc2 = sc.replaceAll(" ", "").toUpperCase();

		// Just in case here is a like for like recreation
		/*
		 * int i, i2; char[] sc2 = new char[10];
		 *
		 * i2 = 0; for (i = 0; i < sc.length && i < 10; i++) { if (sc[i] != ' ') { sc2[i2] =
		 * Character.toUpperCase(sc[i]); i2++; } } sc2[i2] = '\0';
		 */

		if (sc2.equals("A")) {
			return SI_SPEC_A;
		}
		if (sc2.equals("ABAL")) {
			return SI_SPEC_ABAL;
		}
		if (sc2.equals("ABCO")) {
			return SI_SPEC_ABCO;
		}
		if (sc2.equals("AC")) {
			return SI_SPEC_AC;
		}
		if (sc2.equals("ACB")) {
			return SI_SPEC_ACB;
		}
		if (sc2.equals("ACT")) {
			return SI_SPEC_ACT;
		}
		if (sc2.equals("AD")) {
			return SI_SPEC_AD;
		}
		if (sc2.equals("AH")) {
			return SI_SPEC_AH;
		}
		if (sc2.equals("AT")) {
			return SI_SPEC_AT;
		}
		if (sc2.equals("AX")) {
			return SI_SPEC_AX;
		}
		if (sc2.equals("B")) {
			return SI_SPEC_B;
		}
		if (sc2.equals("BA")) {
			return SI_SPEC_BA;
		}
		if (sc2.equals("BB")) {
			return SI_SPEC_BB;
		}
		if (sc2.equals("BC")) {
			return SI_SPEC_BC;
		}
		if (sc2.equals("BG")) {
			return SI_SPEC_BG;
		}
		if (sc2.equals("BI")) {
			return SI_SPEC_BI;
		}
		if (sc2.equals("BL")) {
			return SI_SPEC_BL;
		}
		if (sc2.equals("BM")) {
			return SI_SPEC_BM;
		}
		if (sc2.equals("BP")) {
			return SI_SPEC_BP;
		}
		// if (sc2.equals("BV")){ return SI_SPEC_BV;}
		if (sc2.equals("C")) {
			return SI_SPEC_C;
		}
		if (sc2.equals("CI")) {
			return SI_SPEC_CI;
		}
		if (sc2.equals("CP")) {
			return SI_SPEC_CP;
		}
		if (sc2.equals("CW")) {
			return SI_SPEC_CW;
		}
		if (sc2.equals("CWC")) {
			return SI_SPEC_CWC;
		}
		if (sc2.equals("CWI")) {
			return SI_SPEC_CWI;
		}
		if (sc2.equals("CY")) {
			return SI_SPEC_CY;
		}
		if (sc2.equals("D")) {
			return SI_SPEC_D;
		}
		if (sc2.equals("DG")) {
			return SI_SPEC_DG;
		}
		if (sc2.equals("DM")) {
			return SI_SPEC_DM;
		}
		if (sc2.equals("DR")) {
			return SI_SPEC_DR;
		}
		if (sc2.equals("E")) {
			return SI_SPEC_E;
		}
		if (sc2.equals("EA")) {
			return SI_SPEC_EA;
		}
		if (sc2.equals("EB")) {
			return SI_SPEC_EB;
		}
		if (sc2.equals("EE")) {
			return SI_SPEC_EE;
		}
		if (sc2.equals("EP")) {
			return SI_SPEC_EP;
		}
		if (sc2.equals("ES")) {
			return SI_SPEC_ES;
		}
		if (sc2.equals("EW")) {
			return SI_SPEC_EW;
		}
		if (sc2.equals("EXP")) {
			return SI_SPEC_EXP;
		}
		if (sc2.equals("FD")) {
			return SI_SPEC_FD;
		}
		if (sc2.equals("FDC")) {
			return SI_SPEC_FDC;
		}
		if (sc2.equals("FDI")) {
			return SI_SPEC_FDI;
		}
		if (sc2.equals("G")) {
			return SI_SPEC_G;
		}
		if (sc2.equals("GP")) {
			return SI_SPEC_GP;
		}
		if (sc2.equals("GR")) {
			return SI_SPEC_GR;
		}
		if (sc2.equals("H")) {
			return SI_SPEC_H;
		}
		if (sc2.equals("HM")) {
			return SI_SPEC_HM;
		}
		if (sc2.equals("HW")) {
			return SI_SPEC_HW;
		}
		if (sc2.equals("HWC")) {
			return SI_SPEC_HWC;
		}
		if (sc2.equals("HWI")) {
			return SI_SPEC_HWI;
		}
		if (sc2.equals("HXM")) {
			return SI_SPEC_HXM;
		}
		if (sc2.equals("IG")) {
			return SI_SPEC_IG;
		}
		if (sc2.equals("IS")) {
			return SI_SPEC_IS;
		}
		if (sc2.equals("J")) {
			return SI_SPEC_J;
		}
		if (sc2.equals("JR")) {
			return SI_SPEC_JR;
		}
		if (sc2.equals("K")) {
			return SI_SPEC_K;
		}
		if (sc2.equals("KC")) {
			return SI_SPEC_KC;
		}
		if (sc2.equals("L")) {
			return SI_SPEC_L;
		}
		if (sc2.equals("LA")) {
			return SI_SPEC_LA;
		}
		if (sc2.equals("LE")) {
			return SI_SPEC_LE;
		}
		if (sc2.equals("LT")) {
			return SI_SPEC_LT;
		}
		if (sc2.equals("LW")) {
			return SI_SPEC_LW;
		}
		if (sc2.equals("M")) {
			return SI_SPEC_M;
		}
		if (sc2.equals("MB")) {
			return SI_SPEC_MB;
		}
		if (sc2.equals("ME")) {
			return SI_SPEC_ME;
		}
		if (sc2.equals("MN")) {
			return SI_SPEC_MN;
		}
		if (sc2.equals("MR")) {
			return SI_SPEC_MR;
		}
		if (sc2.equals("MS")) {
			return SI_SPEC_MS;
		}
		if (sc2.equals("MV")) {
			return SI_SPEC_MV;
		}
		if (sc2.equals("OA")) {
			return SI_SPEC_OA;
		}
		if (sc2.equals("OB")) {
			return SI_SPEC_OB;
		}
		if (sc2.equals("OC")) {
			return SI_SPEC_OC;
		}
		if (sc2.equals("OD")) {
			return SI_SPEC_OD;
		}
		if (sc2.equals("OE")) {
			return SI_SPEC_OE;
		}
		if (sc2.equals("OF")) {
			return SI_SPEC_OF;
		}
		if (sc2.equals("OG")) {
			return SI_SPEC_OG;
		}
		if (sc2.equals("P")) {
			return SI_SPEC_P;
		}
		if (sc2.equals("PA")) {
			return SI_SPEC_PA;
		}
		if (sc2.equals("PF")) {
			return SI_SPEC_PF;
		}
		if (sc2.equals("PJ")) {
			return SI_SPEC_PJ;
		}
		if (sc2.equals("PL")) {
			return SI_SPEC_PL;
		}
		if (sc2.equals("PLC")) {
			return SI_SPEC_PLC;
		}
		if (sc2.equals("PLI")) {
			return SI_SPEC_PLI;
		}
		if (sc2.equals("PM")) {
			return SI_SPEC_PM;
		}
		if (sc2.equals("PR")) {
			return SI_SPEC_PR;
		}
		if (sc2.equals("PS")) {
			return SI_SPEC_PS;
		}
		if (sc2.equals("PW")) {
			return SI_SPEC_PW;
		}
		if (sc2.equals("PXJ")) {
			return SI_SPEC_PXJ;
		}
		if (sc2.equals("PY")) {
			return SI_SPEC_PY;
		}
		if (sc2.equals("Q")) {
			return SI_SPEC_Q;
		}
		if (sc2.equals("QE")) {
			return SI_SPEC_QE;
		}
		if (sc2.equals("QG")) {
			return SI_SPEC_QG;
		}
		if (sc2.equals("R")) {
			return SI_SPEC_R;
		}
		if (sc2.equals("RA")) {
			return SI_SPEC_RA;
		}
		if (sc2.equals("S")) {
			return SI_SPEC_S;
		}
		if (sc2.equals("SA")) {
			return SI_SPEC_SA;
		}
		if (sc2.equals("SB")) {
			return SI_SPEC_SB;
		}
		if (sc2.equals("SE")) {
			return SI_SPEC_SE;
		}
		if (sc2.equals("SI")) {
			return SI_SPEC_SI;
		}
		if (sc2.equals("SN")) {
			return SI_SPEC_SN;
		}
		if (sc2.equals("SS")) {
			return SI_SPEC_SS;
		}
		if (sc2.equals("SW")) {
			return SI_SPEC_SW;
		}
		if (sc2.equals("SX")) {
			return SI_SPEC_SX;
		}
		if (sc2.equals("SXB")) {
			return SI_SPEC_SXB;
		}
		if (sc2.equals("SXE")) {
			return SI_SPEC_SXE;
		}
		if (sc2.equals("SXL")) {
			return SI_SPEC_SXL;
		}
		if (sc2.equals("SXS")) {
			return SI_SPEC_SXS;
		}
		if (sc2.equals("SXW")) {
			return SI_SPEC_SXW;
		}
		if (sc2.equals("SXX")) {
			return SI_SPEC_SXX;
		}
		if (sc2.equals("T")) {
			return SI_SPEC_T;
		}
		if (sc2.equals("TW")) {
			return SI_SPEC_TW;
		}
		if (sc2.equals("U")) {
			return SI_SPEC_U;
		}
		if (sc2.equals("UA")) {
			return SI_SPEC_UA;
		}
		if (sc2.equals("UP")) {
			return SI_SPEC_UP;
		}
		if (sc2.equals("V")) {
			return SI_SPEC_V;
		}
		if (sc2.equals("VB")) {
			return SI_SPEC_VB;
		}
		if (sc2.equals("VP")) {
			return SI_SPEC_VP;
		}
		if (sc2.equals("VS")) {
			return SI_SPEC_VS;
		}
		if (sc2.equals("VV")) {
			return SI_SPEC_VV;
		}
		if (sc2.equals("W")) {
			return SI_SPEC_W;
		}
		if (sc2.equals("WA")) {
			return SI_SPEC_WA;
		}
		if (sc2.equals("WB")) {
			return SI_SPEC_WB;
		}
		if (sc2.equals("WD")) {
			return SI_SPEC_WD;
		}
		if (sc2.equals("WI")) {
			return SI_SPEC_WI;
		}
		if (sc2.equals("WP")) {
			return SI_SPEC_WP;
		}
		if (sc2.equals("WS")) {
			return SI_SPEC_WS;
		}
		if (sc2.equals("WT")) {
			return SI_SPEC_WT;
		}
		if (sc2.equals("X")) {
			return SI_SPEC_X;
		}
		if (sc2.equals("XC")) {
			return SI_SPEC_XC;
		}
		if (sc2.equals("XH")) {
			return SI_SPEC_XH;
		}
		if (sc2.equals("Y")) {
			return SI_SPEC_Y;
		}
		if (sc2.equals("YC")) {
			return SI_SPEC_YC;
		}
		if (sc2.equals("YP")) {
			return SI_SPEC_YP;
		}
		if (sc2.equals("Z")) {
			return SI_SPEC_Z;
		}
		if (sc2.equals("ZC")) {
			return SI_SPEC_ZC;
		}
		if (sc2.equals("ZH")) {
			return SI_SPEC_ZH;
		}

		throw new CodeErrorException("Unknown species code: " + sc2);
	}

	public static int species_remap(String sc, char fiz) throws CodeErrorException {

		// This can be done more elegantly with Java
		String sc2 = sc.replaceAll(" ", "").toUpperCase();

		// Just incase here is a like for like recreation
		/*
		 * int i, i2; char[] sc2 = new char[10];
		 *
		 * i2 = 0; for (i = 0; i < sc.length && i < 10; i++) { if (sc[i] != ' ') { sc2[i2] =
		 * Character.toUpperCase(sc[i]); i2++; } } sc2[i2] = '\0';
		 */

		if (sc2.equals("A")) {
			return SI_SPEC_AT;
		}
		if (sc2.equals("ABAL")) {
			return SI_SPEC_BA;
		}
		if (sc2.equals("ABCO")) {
			return SI_SPEC_BA;
		}
		if (sc2.equals("AC")) {
			return SI_SPEC_ACB;
		}
		if (sc2.equals("ACB")) {
			return SI_SPEC_ACB;
		}
		if (sc2.equals("ACT")) {
			return SI_SPEC_ACT;
		}
		if (sc2.equals("AD")) {
			return SI_SPEC_ACT;
		}
		if (sc2.equals("AH")) {
			return SI_SPEC_ACT;
		}
		if (sc2.equals("AT")) {
			return SI_SPEC_AT;
		}
		if (sc2.equals("AX")) {
			return SI_SPEC_ACB;
		}
		if (sc2.equals("B")) {
			return speciesByFizCategory(fiz, SI_SPEC_BA, SI_SPEC_BL);
		}
		if (sc2.equals("BA")) {
			return SI_SPEC_BA;
		}
		if (sc2.equals("BAC")) {
			return SI_SPEC_BA;
		}
		if (sc2.equals("BAI")) {
			return SI_SPEC_BA;
		}
		if (sc2.equals("BB")) {
			return SI_SPEC_BL;
		}
		if (sc2.equals("BC")) {
			return speciesByFizCategory(fiz, SI_SPEC_BA, SI_SPEC_BL);
		}
		if (sc2.equals("BG")) {
			return SI_SPEC_BA;
		}
		if (sc2.equals("BI")) {
			return SI_SPEC_AT;
		}
		if (sc2.equals("BL")) {
			return SI_SPEC_BL;
		}
		if (sc2.equals("BM")) {
			return SI_SPEC_BA;
		}
		if (sc2.equals("BN")) {
			return SI_SPEC_BP;
		}
		if (sc2.equals("BP")) {
			return SI_SPEC_BP;
		}
		// if (sc2.equals("BV")) {
		// return SI_SPEC_AT;
		// }
		if (sc2.equals("C")) {
			return speciesByFizCategory(fiz, SI_SPEC_CWC, SI_SPEC_CWI);
		}
		if (sc2.equals("CI")) {
			return speciesByFizCategory(fiz, SI_SPEC_CWC, SI_SPEC_CWI);
		}
		if (sc2.equals("COT")) {
			return SI_SPEC_ACT;
		}
		if (sc2.equals("CP")) {
			return speciesByFizCategory(fiz, SI_SPEC_CWC, SI_SPEC_CWI);
		}
		if (sc2.equals("CT")) {
			return SI_SPEC_ACT;
		}
		if (sc2.equals("CW")) {
			return speciesByFizCategory(fiz, SI_SPEC_CWC, SI_SPEC_CWI);
		}
		if (sc2.equals("CWC")) {
			return SI_SPEC_CWC;
		}
		if (sc2.equals("CWI")) {
			return SI_SPEC_CWI;
		}
		if (sc2.equals("CY")) {
			return speciesByFizCategory(fiz, SI_SPEC_CWC, SI_SPEC_CWI);
		}
		if (sc2.equals("D")) {
			return SI_SPEC_DR;
		}
		if (sc2.equals("DF")) {
			return speciesByFizCategory(fiz, SI_SPEC_FDC, SI_SPEC_FDI);
		}
		if (sc2.equals("DG")) {
			return SI_SPEC_DR;
		}
		if (sc2.equals("DM")) {
			return SI_SPEC_DR;
		}
		if (sc2.equals("DR")) {
			return SI_SPEC_DR;
		}
		if (sc2.equals("E")) {
			return SI_SPEC_EP;
		}
		if (sc2.equals("EA")) {
			return SI_SPEC_EP;
		}
		if (sc2.equals("EB")) {
			return SI_SPEC_EP;
		}
		if (sc2.equals("EE")) {
			return SI_SPEC_EP;
		}
		if (sc2.equals("EP")) {
			return SI_SPEC_EP;
		}
		if (sc2.equals("ES")) {
			return SI_SPEC_EP;
		}
		if (sc2.equals("EW")) {
			return SI_SPEC_EP;
		}
		if (sc2.equals("EXP")) {
			return SI_SPEC_EP;
		}
		if (sc2.equals("F")) {
			return speciesByFizCategory(fiz, SI_SPEC_FDC, SI_SPEC_FDI);
		}
		if (sc2.equals("FD")) {
			return speciesByFizCategory(fiz, SI_SPEC_FDC, SI_SPEC_FDI);
		}
		if (sc2.equals("FDC")) {
			return SI_SPEC_FDC;
		}
		if (sc2.equals("FDI")) {
			return SI_SPEC_FDI;
		}
		if (sc2.equals("G")) {
			return SI_SPEC_DR;
		}
		if (sc2.equals("GP")) {
			return SI_SPEC_DR;
		}
		if (sc2.equals("GR")) {
			return SI_SPEC_DR;
		}
		if (sc2.equals("H")) {
			return speciesByFizCategory(fiz, SI_SPEC_HWC, SI_SPEC_HWI);
		}
		if (sc2.equals("HM")) {
			return SI_SPEC_HM;
		}
		if (sc2.equals("HW")) {
			return speciesByFizCategory(fiz, SI_SPEC_HWC, SI_SPEC_HWI);
		}
		if (sc2.equals("HWC")) {
			return SI_SPEC_HWC;
		}
		if (sc2.equals("HWI")) {
			return SI_SPEC_HWI;
		}
		if (sc2.equals("HXM")) {
			return speciesByFizCategory(fiz, SI_SPEC_HWC, SI_SPEC_HWI);
		}
		if (sc2.equals("IG")) {
			return speciesByFizCategory(fiz, SI_SPEC_CWC, SI_SPEC_CWI);
		}
		if (sc2.equals("IS")) {
			return speciesByFizCategory(fiz, SI_SPEC_CWC, SI_SPEC_CWI);
		}
		if (sc2.equals("J")) {
			return speciesByFizCategory(fiz, SI_SPEC_CWC, SI_SPEC_CWI);
		}
		if (sc2.equals("JR")) {
			return speciesByFizCategory(fiz, SI_SPEC_CWC, SI_SPEC_CWI);
		}
		if (sc2.equals("K")) {
			return SI_SPEC_AT;
		}
		if (sc2.equals("KC")) {
			return SI_SPEC_AT;
		}
		if (sc2.equals("L")) {
			return SI_SPEC_LW;
		}
		if (sc2.equals("LA")) {
			return SI_SPEC_LW;
		}
		if (sc2.equals("LE")) {
			return SI_SPEC_LW;
		}
		if (sc2.equals("LT")) {
			return SI_SPEC_LW;
		}
		if (sc2.equals("LW")) {
			return SI_SPEC_LW;
		}
		if (sc2.equals("M")) {
			return SI_SPEC_DR;
		}
		if (sc2.equals("MB")) {
			return SI_SPEC_DR;
		}
		if (sc2.equals("ME")) {
			return SI_SPEC_DR;
		}
		if (sc2.equals("MN")) {
			return SI_SPEC_DR;
		}
		if (sc2.equals("MR")) {
			return SI_SPEC_DR;
		}
		if (sc2.equals("MS")) {
			return SI_SPEC_DR;
		}
		if (sc2.equals("MV")) {
			return SI_SPEC_DR;
		}
		if (sc2.equals("OA")) {
			return speciesByFizCategory(fiz, SI_SPEC_CWC, SI_SPEC_CWI);
		}
		if (sc2.equals("OB")) {
			return speciesByFizCategory(fiz, SI_SPEC_CWC, SI_SPEC_CWI);
		}
		if (sc2.equals("OC")) {
			return speciesByFizCategory(fiz, SI_SPEC_CWC, SI_SPEC_CWI);
		}
		if (sc2.equals("OD")) {
			return SI_SPEC_AT;
		}
		if (sc2.equals("OE")) {
			return SI_SPEC_AT;
		}
		if (sc2.equals("OF")) {
			return SI_SPEC_AT;
		}
		if (sc2.equals("OG")) {
			return SI_SPEC_AT;
		}
		if (sc2.equals("P")) {
			return SI_SPEC_PLI;
		}
		if (sc2.equals("PA")) {
			return SI_SPEC_PLI;
		}
		if (sc2.equals("PF")) {
			return SI_SPEC_PLI;
		}
		if (sc2.equals("PJ")) {
			return SI_SPEC_PJ;
		}
		if (sc2.equals("PL")) {
			return SI_SPEC_PLI;
		}
		if (sc2.equals("PLC")) {
			return SI_SPEC_PLI;
		}
		if (sc2.equals("PLI")) {
			return SI_SPEC_PLI;
		}
		if (sc2.equals("PM")) {
			return SI_SPEC_PLI;
		}
		if (sc2.equals("PR")) {
			return SI_SPEC_PLI;
		}
		if (sc2.equals("PS")) {
			return SI_SPEC_PLI;
		}
		if (sc2.equals("PV")) {
			return SI_SPEC_PY;
		}
		if (sc2.equals("PW")) {
			return SI_SPEC_PW;
		}
		if (sc2.equals("PXJ")) {
			return SI_SPEC_PLI;
		}
		if (sc2.equals("PY")) {
			return SI_SPEC_PY;
		}
		if (sc2.equals("Q")) {
			return SI_SPEC_AT;
		}
		if (sc2.equals("QE")) {
			return SI_SPEC_AT;
		}
		if (sc2.equals("QG")) {
			return SI_SPEC_AT;
		}
		if (sc2.equals("R")) {
			return SI_SPEC_DR;
		}
		if (sc2.equals("RA")) {
			return SI_SPEC_DR;
		}
		if (sc2.equals("S")) { // Duplicate case? Unreachable
			return speciesByFizCategory(fiz, SI_SPEC_SS, SI_SPEC_SW);
		}
		if (sc2.equals("SA")) { // Duplicate case? Unreachable
			return SI_SPEC_SW;
		}
		if (sc2.equals("SB")) {
			return SI_SPEC_SB;
		}
		if (sc2.equals("SE")) {
			return SI_SPEC_SE;
		}
		if (sc2.equals("SI")) {
			return SI_SPEC_SW;
		}
		if (sc2.equals("SN")) {
			return SI_SPEC_SW;
		}
		if (sc2.equals("SS")) {
			return SI_SPEC_SS;
		}
		if (sc2.equals("SW")) {
			return SI_SPEC_SW;
		}
		if (sc2.equals("SX")) {
			return speciesByFizCategory(fiz, SI_SPEC_SS, SI_SPEC_SW);
		}
		if (sc2.equals("SXB")) {
			return SI_SPEC_SW;
		}
		if (sc2.equals("SXE")) {
			return speciesByFizCategory(fiz, SI_SPEC_SS, SI_SPEC_SE);
		}
		if (sc2.equals("SXL")) {
			return speciesByFizCategory(fiz, SI_SPEC_SS, SI_SPEC_SW);
		}
		if (sc2.equals("SXS")) {
			return speciesByFizCategory(fiz, SI_SPEC_SS, SI_SPEC_SW);
		}
		if (sc2.equals("SXW")) {
			return SI_SPEC_SW;
		}
		if (sc2.equals("SXX")) {
			return speciesByFizCategory(fiz, SI_SPEC_SS, SI_SPEC_SW);
		}
		if (sc2.equals("T")) {
			return speciesByFizCategory(fiz, SI_SPEC_HWC, SI_SPEC_HWI);
		}
		if (sc2.equals("TW")) {
			return speciesByFizCategory(fiz, SI_SPEC_HWC, SI_SPEC_HWI);
		}
		if (sc2.equals("U")) {
			return SI_SPEC_AT;
		}
		if (sc2.equals("UA")) {
			return SI_SPEC_AT;
		}
		if (sc2.equals("UP")) {
			return SI_SPEC_AT;
		}
		if (sc2.equals("V")) {
			return SI_SPEC_AT;
		}
		if (sc2.equals("VB")) {
			return SI_SPEC_AT;
		}
		if (sc2.equals("VP")) {
			return SI_SPEC_AT;
		}
		if (sc2.equals("VS")) {
			return SI_SPEC_AT;
		}
		if (sc2.equals("VV")) {
			return SI_SPEC_AT;
		}
		if (sc2.equals("W")) {
			return SI_SPEC_AT;
		}
		if (sc2.equals("WA")) {
			return SI_SPEC_AT;
		}
		if (sc2.equals("WB")) {
			return SI_SPEC_AT;
		}
		if (sc2.equals("WD")) {
			return SI_SPEC_AT;
		}
		if (sc2.equals("WI")) {
			return SI_SPEC_AT;
		}
		if (sc2.equals("WP")) {
			return SI_SPEC_AT;
		}
		if (sc2.equals("WS")) {
			return SI_SPEC_AT;
		}
		if (sc2.equals("WT")) {
			return SI_SPEC_AT;
		}
		if (sc2.equals("X")) {
			return speciesByFizCategory(fiz, SI_SPEC_FDC, SI_SPEC_FDI);
		}
		if (sc2.equals("XC")) {
			return speciesByFizCategory(fiz, SI_SPEC_FDC, SI_SPEC_FDI);
		}
		if (sc2.equals("XH")) {
			return SI_SPEC_AT;
		}
		if (sc2.equals("Y")) {
			return speciesByFizCategory(fiz, SI_SPEC_CWC, SI_SPEC_CWI);
		}
		if (sc2.equals("YC")) {
			return speciesByFizCategory(fiz, SI_SPEC_CWC, SI_SPEC_CWI);
		}
		if (sc2.equals("YP")) {
			return speciesByFizCategory(fiz, SI_SPEC_CWC, SI_SPEC_CWI);
		}
		if (sc2.equals("Z")) {
			return speciesByFizCategory(fiz, SI_SPEC_FDC, SI_SPEC_FDI);
		}
		if (sc2.equals("ZC")) {
			return speciesByFizCategory(fiz, SI_SPEC_FDC, SI_SPEC_FDI);
		}
		if (sc2.equals("ZH")) {
			return SI_SPEC_AT;
		}

		throw new CodeErrorException("Unknown species code: " + sc2);
	}

	private static int speciesByFizCategory(char fiz, int coastalSpecies, int interiorSpecies)
			throws CodeErrorException {
		switch (FizCheck.fiz2Region(fiz)) {
		case FIZ_COAST:
			return coastalSpecies;
		case FIZ_INTERIOR:
			return interiorSpecies;
		default:
			throw new CodeErrorException("Unknown forest inventory code: " + fiz);
		}
	}
}
