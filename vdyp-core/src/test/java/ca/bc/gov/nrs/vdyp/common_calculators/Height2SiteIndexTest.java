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

	@Test
	void testInvalidHeightForBreastHeightAge() {
		assertThrows(
				LessThan13Exception.class,
				() -> Height2SiteIndex.height_to_index((short) 0, 0.0, SI_AT_BREAST, 1.2, (short) 0)
		);

	}

}
