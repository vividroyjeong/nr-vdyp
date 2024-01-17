package ca.bc.gov.nrs.vdyp.common_calculators;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.AgeTypeErrorException;

class Age2AgeTest {
	/*
	 * age types
	 */
	private static final short SI_AT_TOTAL = 0;
	private static final short SI_AT_BREAST = 1;

	/* define species and equation indices */
	private static final short SI_ACB_HUANGAC = 97;
	private static final short SI_ACT_THROWERAC = 103;
	private static final short SI_AT_NIGH = 92;
	private static final short SI_BA_KURUCZ82AC = 102;
	private static final short SI_BA_NIGH = 118;
	private static final short SI_BL_CHENAC = 93;
	private static final short SI_CWI_NIGH = 77;
	private static final short SI_DR_NIGH = 13;
	private static final short SI_EP_NIGH = 116;
	private static final short SI_FDC_BRUCEAC = 100;
	private static final short SI_FDC_NIGHTA = 88;
	private static final short SI_FDI_THROWERAC = 96;
	private static final short SI_HWI_NIGH = 37;
	private static final short SI_LW_NIGH = 90;
	private static final short SI_PJ_HUANG = 113;
	private static final short SI_PJ_HUANGAC = 114;
	private static final short SI_PLI_NIGHTA98 = 41;
	private static final short SI_PLI_THROWNIGH = 40;
	private static final short SI_PW_CURTISAC = 98;
	private static final short SI_PY_HANNAC = 104;
	private static final short SI_PY_NIGH = 107;
	private static final short SI_SB_NIGH = 91;
	private static final short SI_SE_CHENAC = 105;
	private static final short SI_SS_NIGH = 59;
	private static final short SI_FDC_BRUCENIGH = 89;
	private static final short SI_SW_GOUDIE_NATAC = 106;
	private static final short SI_SW_GOUDIE_PLAAC = 112;
	private static final short SI_SW_NIGHTA = 83;
	/* not used, but must be defined for array positioning */
	private static final short SI_SE_NIGHTA = 110;
	private static final short SI_SW_NIGHTA2004 = 111;
	private static final short SI_PLI_NIGHTA2004 = 109;

	private static final double ERROR_TOLERANCE = 0.00001;

	@Test
	void testShouldBranchInvalid() {
		assertThrows(
				AgeTypeErrorException.class, () -> Age2Age.age_to_age(SI_ACB_HUANGAC, 0.0, SI_AT_BREAST, (short) 1, 0.0)
		);

		assertThrows(
				AgeTypeErrorException.class,
				() -> Age2Age.age_to_age(SI_ACT_THROWERAC, 0.0, SI_AT_TOTAL, (short) 0, 0.0)
		);
	}

	@Test
	void testShouldNotBranchInvalid() {
		assertThrows(
				AgeTypeErrorException.class, () -> Age2Age.age_to_age((short) 600, 0.0, SI_AT_BREAST, (short) 1, 0.0)
		);

		assertThrows(
				AgeTypeErrorException.class, () -> Age2Age.age_to_age((short) 600, 0.0, SI_AT_TOTAL, (short) 0, 0.0)
		);
	}

	@Test
	void testShouldBranchReturnValue() {
		double expectedResult = 3.0; // normal calculated test
		double actualResult = Age2Age.age_to_age(SI_AT_NIGH, 1.5, SI_AT_BREAST, SI_AT_TOTAL, 2);

		assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

		expectedResult = 0; // returnValue < 0
		actualResult = Age2Age.age_to_age(SI_BA_KURUCZ82AC, 0, SI_AT_BREAST, SI_AT_TOTAL, 0);

		expectedResult = 1.0; // normal calculated test
		actualResult = Age2Age.age_to_age(SI_BA_NIGH, 1.5, SI_AT_TOTAL, SI_AT_BREAST, 1);

		assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

		expectedResult = 0; // returnValue < 0
		actualResult = Age2Age.age_to_age(SI_BL_CHENAC, 0, SI_AT_TOTAL, SI_AT_BREAST, 1);

		assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
	}

	@Test
	void testShouldNotBranchReturnValue() {
		double expectedResult = 3.0; // normal calculated test
		double actualResult = Age2Age.age_to_age((short) 600, 1, SI_AT_BREAST, SI_AT_TOTAL, 2);

		assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

		expectedResult = 0; // returnValue < 0
		actualResult = Age2Age.age_to_age((short) 600, 0, SI_AT_BREAST, SI_AT_TOTAL, -2);

		expectedResult = 1.0; // normal calculated test
		actualResult = Age2Age.age_to_age((short) 600, 2, SI_AT_TOTAL, SI_AT_BREAST, 1);

		assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

		expectedResult = 0; // returnValue < 0
		actualResult = Age2Age.age_to_age((short) 600, 0, SI_AT_TOTAL, SI_AT_BREAST, 1);

		assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
	}

	@Test
	void testShouldBranchNoIfMatch() {
		assertThrows(AgeTypeErrorException.class, () -> Age2Age.age_to_age(SI_SS_NIGH, 0.0, (short) 5, (short) 6, 0.0));
	}

	@Test
	void testUncheckedCasesWithShouldBranch() {
		double expectedResult = 3.0; // normal calculated test
		short[] cases = { SI_CWI_NIGH, SI_DR_NIGH, SI_EP_NIGH, SI_FDC_BRUCENIGH, SI_FDC_BRUCEAC, SI_FDC_NIGHTA,
				SI_FDI_THROWERAC, SI_HWI_NIGH, SI_LW_NIGH, SI_PJ_HUANG, SI_PJ_HUANGAC, SI_PLI_NIGHTA2004,
				SI_PLI_NIGHTA98, SI_PLI_THROWNIGH, SI_PW_CURTISAC, SI_PY_HANNAC, SI_PY_NIGH, SI_SB_NIGH, SI_SE_CHENAC,
				SI_SE_NIGHTA, SI_SW_GOUDIE_NATAC, SI_SW_GOUDIE_PLAAC, SI_SW_NIGHTA2004, SI_SW_NIGHTA };

		for (short caseValue : cases) {
			double actualResult = Age2Age.age_to_age(caseValue, 1.5, SI_AT_BREAST, SI_AT_TOTAL, 2);
			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

	}
}
