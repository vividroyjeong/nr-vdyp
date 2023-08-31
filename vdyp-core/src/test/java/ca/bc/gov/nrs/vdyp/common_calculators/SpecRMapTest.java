package ca.bc.gov.nrs.vdyp.common_calculators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.CodeErrorException;

public class SpecRMapTest {
	// Taken from sindex.h
	/* define species and equation indices */
	private static final int SI_SPEC_ABAL = 1;
	private static final int SI_SPEC_ABCO = 2;
	private static final int SI_SPEC_ACB = 4;
	private static final int SI_SPEC_ACT = 5;
	private static final int SI_SPEC_AT = 8;
	private static final int SI_SPEC_BA = 11;
	private static final int SI_SPEC_BL = 16;
	private static final int SI_SPEC_BP = 18;
	private static final int SI_SPEC_CWC = 23;
	private static final int SI_SPEC_CWI = 24;
	private static final int SI_SPEC_DR = 29;
	private static final int SI_SPEC_EP = 34;
	private static final int SI_SPEC_FDC = 39;
	private static final int SI_SPEC_FDI = 40;
	private static final int SI_SPEC_HM = 45;
	private static final int SI_SPEC_HWC = 47;
	private static final int SI_SPEC_HWI = 48;
	private static final int SI_SPEC_LW = 60;
	private static final int SI_SPEC_PJ = 78;
	private static final int SI_SPEC_PLI = 81;
	private static final int SI_SPEC_PW = 85;
	private static final int SI_SPEC_PY = 87;
	private static final int SI_SPEC_SB = 95;
	private static final int SI_SPEC_SE = 96;
	private static final int SI_SPEC_SS = 99;
	private static final int SI_SPEC_SW = 100;

	@Nested
	class SpeciesMapTest {
		@Test
		void testAllSpeciesUppercase() { // loop through each case
			for (short i = 0; i < SiteIndexNames.si_spec_code.length; i++) { // test all normal cases
				short expectedResult = i;
				short actualResult = SpecRMap.species_map(SiteIndexNames.si_spec_code[i]);
				assertEquals(expectedResult, actualResult);
			}

			assertThrows(CodeErrorException.class, () -> SpecRMap.species_map("Error causer"));
		}

		@Test
		void testSpeciesLowercase() {
			short actualResult = SpecRMap.species_map("abal");
			short expectedResult = SI_SPEC_ABAL;

			assertEquals(expectedResult, actualResult);
		}

		@Test
		void testSpeciesMixedcase() {
			short actualResult = SpecRMap.species_map("AbCo");
			short expectedResult = SI_SPEC_ABCO;

			assertEquals(expectedResult, actualResult);
		}

		@Test
		void testSpecieSpaces() {
			short actualResult = SpecRMap.species_map("    c    W   I   ");
			short expectedResult = SI_SPEC_CWI;

			assertEquals(expectedResult, actualResult);
		}
	}

	@Nested
	class SpeciesRemapTest {
		// Spaces and lowe/uppercase is tested throughout but mainly in testReturnERROR
		@Test
		void testReturnSI_SPEC_BA() {
			short expectedResult = SI_SPEC_BA;
			String[] cases = { "ABCO", "bA", "BaC", "BAi", "BG", "BM", "B", "BC" };

			for (int i = 0; i < cases.length; i++) {
				short actualResult = SpecRMap.species_remap(cases[i], 'A');
				assertEquals(actualResult, expectedResult, "Error with this " + cases[i] + " case");
			}
		}

		@Test
		void testReturnSI_SPEC_ACB() {
			short expectedResult = SI_SPEC_ACB;
			String[] cases = { "AC", "ACB", "AX" };

			for (int i = 0; i < cases.length; i++) {
				short actualResult = SpecRMap.species_remap(cases[i], 'X');
				assertEquals(actualResult, expectedResult, "Error with this " + cases[i] + " case");
			}
		}

		@Test
		void testReturnSI_SPEC_ACT() {
			short expectedResult = SI_SPEC_ACT;
			String[] cases = { "ACT", "AD", "AH", "COT", "CT" };

			for (int i = 0; i < cases.length; i++) {
				short actualResult = SpecRMap.species_remap(cases[i], 'X');
				assertEquals(actualResult, expectedResult, "Error with this " + cases[i] + " case");
			}
		}

		@Test
		void testReturnSI_SPEC_AT() {
			short expectedResult = SI_SPEC_AT;
			String[] cases = { "AT", "BI", "K", "KC", "OD", "OE", "OF", "OG", "Q", "QE", "QG", "U", "UA", "UP", "V",
					"VB", "VP", "VS", "VV", "W", "WA", "WB", "WD", "WI", "WP", "WS", "WT", "XH", "ZH" };

			for (int i = 0; i < cases.length; i++) {
				short actualResult = SpecRMap.species_remap(cases[i], 'X');
				assertEquals(actualResult, expectedResult, "Error with this " + cases[i] + " case");
			}
		}

		@Test
		void testReturnSI_SPEC_BL() {
			short expectedResult = SI_SPEC_BL;
			String[] cases = { "BB", "BL", "B", "BC" };

			for (int i = 0; i < cases.length; i++) {
				short actualResult = SpecRMap.species_remap(cases[i], 'E');
				assertEquals(actualResult, expectedResult, "Error with this " + cases[i] + " case");
			}
		}

		@Test
		void testReturnSI_SPEC_BP() {
			short expectedResult = SI_SPEC_BP;
			String[] cases = { "BN", "BP" };

			for (int i = 0; i < cases.length; i++) {
				short actualResult = SpecRMap.species_remap(cases[i], 'X');
				assertEquals(actualResult, expectedResult, "Error with this " + cases[i] + " case");
			}
		}

		@Test
		void testReturnSI_SPEC_CWC() {
			short expectedResult = SI_SPEC_CWC;
			String[] cases = { "C", "CI", "CP", "CW", "CWC", "CY", "IG", "IS", "J", "JR", "OA", "OB", "OC", "Y", "YC",
					"YP" };

			for (int i = 0; i < cases.length; i++) {
				short actualResult = SpecRMap.species_remap(cases[i], 'A');
				assertEquals(actualResult, expectedResult, "Error with this " + cases[i] + " case");
			}
		}

		@Test
		void testReturnSI_SPEC_CWI() {
			short expectedResult = SI_SPEC_CWI;
			String[] cases = { "C", "CI", "CP", "CW", "CWI", "CY", "IG", "IS", "J", "JR", "OA", "OB", "OC", "Y", "YC",
					"YP" };

			for (int i = 0; i < cases.length; i++) {
				short actualResult = SpecRMap.species_remap(cases[i], 'E');
				assertEquals(actualResult, expectedResult, "Error with this " + cases[i] + " case");
			}
		}

		@Test
		void testReturnSI_SPEC_DR() {
			short expectedResult = SI_SPEC_DR;
			String[] cases = { "D", "DG", "DM", "DR", "G", "GP", "GR", "M", "MB", "ME", "MN", "MR", "MS", "MV", "R",
					"RA" };

			for (int i = 0; i < cases.length; i++) {
				short actualResult = SpecRMap.species_remap(cases[i], 'E');
				assertEquals(actualResult, expectedResult, "Error with this " + cases[i] + " case");
			}
		}

		@Test
		void testReturnSI_SPEC_FDC() {
			short expectedResult = SI_SPEC_FDC;
			String[] cases = { "DF", "F", "FD", "FDC", "X", "XC", "Z", "ZC", };

			for (int i = 0; i < cases.length; i++) {
				short actualResult = SpecRMap.species_remap(cases[i], 'A');
				assertEquals(actualResult, expectedResult, "Error with this " + cases[i] + " case");
			}
		}

		@Test
		void testReturnSI_SPEC_FDI() {
			short expectedResult = SI_SPEC_FDI;
			String[] cases = { "DF", "F", "FD", "FDI", "X", "XC", "Z", "ZC", };

			for (int i = 0; i < cases.length; i++) {
				short actualResult = SpecRMap.species_remap(cases[i], 'E');
				assertEquals(actualResult, expectedResult, "Error with this " + cases[i] + " case");
			}
		}

		@Test
		void testReturnSI_SPEC_EP() {
			short expectedResult = SI_SPEC_EP;
			String[] cases = { "E", "EA", "EB", "EE", "EP", "ES", "EW", "EXP" };

			for (int i = 0; i < cases.length; i++) {
				short actualResult = SpecRMap.species_remap(cases[i], 'E');
				assertEquals(actualResult, expectedResult, "Error with this " + cases[i] + " case");
			}
		}

		@Test
		void testReturnSI_SPEC_HWC() {
			short expectedResult = SI_SPEC_HWC;
			String[] cases = { "H", "HW", "HWC", "HXM", "T", "TW", };

			for (int i = 0; i < cases.length; i++) {
				short actualResult = SpecRMap.species_remap(cases[i], 'A');
				assertEquals(actualResult, expectedResult, "Error with this " + cases[i] + " case");
			}
		}

		@Test
		void testReturnSI_SPEC_HWI() {
			short expectedResult = SI_SPEC_HWI;
			String[] cases = { "H", "HW", "HWI", "HXM", "T", "TW", };

			for (int i = 0; i < cases.length; i++) {
				short actualResult = SpecRMap.species_remap(cases[i], 'E');
				assertEquals(actualResult, expectedResult, "Error with this " + cases[i] + " case");
			}
		}

		@Test
		void testReturnSI_SPEC_HM() {
			short expectedResult = SI_SPEC_HM;
			short actualResult = SpecRMap.species_remap("HM", 'A');
			assertEquals(expectedResult, actualResult);
		}

		@Test
		void testReturnSI_SPEC_LW() {
			short expectedResult = SI_SPEC_LW;
			String[] cases = { "L", "LA", "LE", "LT", "LW" };

			for (int i = 0; i < cases.length; i++) {
				short actualResult = SpecRMap.species_remap(cases[i], 'A');
				assertEquals(actualResult, expectedResult, "Error with this " + cases[i] + " case");
			}
		}

		@Test
		void testReturnSI_SPEC_PLI() {
			short expectedResult = SI_SPEC_PLI;
			String[] cases = { "P", "PA", "PF", "PL", "PLC", "PLI", "PM", "PR", "PS", "PXJ" };

			for (int i = 0; i < cases.length; i++) {
				short actualResult = SpecRMap.species_remap(cases[i], 'A');
				assertEquals(actualResult, expectedResult, "Error with this " + cases[i] + " case");
			}
		}

		@Test
		void testReturnSI_SPEC_PJ() {
			short expectedResult = SI_SPEC_PJ;
			short actualResult = SpecRMap.species_remap("PJ", 'A');
			assertEquals(expectedResult, actualResult);
		}

		@Test
		void testReturnSI_SPEC_PY() {
			short expectedResult = SI_SPEC_PY;
			String[] cases = { "PV", "PY" };

			for (int i = 0; i < cases.length; i++) {
				short actualResult = SpecRMap.species_remap(cases[i], 'A');
				assertEquals(actualResult, expectedResult, "Error with this " + cases[i] + " case");
			}
		}

		@Test
		void testReturnSI_SPEC_PW() {
			short expectedResult = SI_SPEC_PW;
			short actualResult = SpecRMap.species_remap("PW", 'A');
			assertEquals(expectedResult, actualResult);
		}

		@Test
		void testReturnSI_SPEC_SS() {
			short expectedResult = SI_SPEC_SS;
			String[] cases = { "S", "SS", "SX", "SXE", "SXL", "SXS", "SXX" };

			for (int i = 0; i < cases.length; i++) {
				short actualResult = SpecRMap.species_remap(cases[i], 'A');
				assertEquals(actualResult, expectedResult, "Error with this " + cases[i] + " case");
			}
		}

		@Test
		void testReturnSI_SPEC_SB() {
			short expectedResult = SI_SPEC_SB;
			short actualResult = SpecRMap.species_remap("SB", 'A');
			assertEquals(expectedResult, actualResult);
		}

		@Test
		void testReturnSI_SPEC_SE() {
			short expectedResult = SI_SPEC_SE;
			String[] cases = { "SXE", "SE" };

			for (int i = 0; i < cases.length; i++) {
				short actualResult = SpecRMap.species_remap(cases[i], 'E');
				assertEquals(actualResult, expectedResult, "Error with this " + cases[i] + " case");
			}
		}

		@Test
		void testReturnSI_SPEC_SW() {
			short expectedResult = SI_SPEC_SW;
			String[] cases = { "SW", "S", "SA", "SI", "SN", "SX", "SXL", "SXS", "SXX", "SXB", "SXW", };

			for (int i = 0; i < cases.length; i++) {
				short actualResult = SpecRMap.species_remap(cases[i], 'E');
				assertEquals(actualResult, expectedResult, "Error with this " + cases[i] + " case");
			}
		}

		@Test
		void testReturnERROR() {
			String[] cases = { "B", "BC", "C", "CI", "cp", "cw", "cy", "Df", "f", "f  d", "h", "hw", "hxm", "ig", "is",
					"j", "jr", "oa", "ob", "oc", "s", "sx", "sxe", "sxl", "sxs", "sxx", "t", "tw", "x", " X c", "y",
					"YC", "yp", "Z", "ZC", "ERIC" };

			for (String caseValue : cases) {
				assertThrows(CodeErrorException.class, () -> SpecRMap.species_remap(caseValue, 'X'));
			}
		}

	}

}
