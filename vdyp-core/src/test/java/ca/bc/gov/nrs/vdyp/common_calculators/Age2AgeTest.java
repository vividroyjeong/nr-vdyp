package ca.bc.gov.nrs.vdyp.common_calculators;

import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

import org.junit.jupiter.api.*;

 class Age2AgeTest {
	// Taken from sindex.h
	/*
	 * age types
	 */
	private static final int SI_AT_TOTAL = 0;
	private static final int SI_AT_BREAST = 1;

	/* define species and equation indices */
	private static final int SI_ACB_HUANGAC = 97;
	private static final int SI_ACT_THROWERAC = 103;
	private static final int SI_AT_NIGH = 92;
	private static final int SI_BA_KURUCZ82AC = 102;
	private static final int SI_BA_NIGH = 118;
	private static final int SI_BL_CHENAC = 93;
	private static final int SI_SS_NIGH = 59;

	private static final double ERROR_TOLERANCE = 0.00001;

	@Test
	void testShouldBranchInvalid() {
		assertThrows(
				AgeTypeErrorException.class,
				() -> Age2Age.age_to_age((short) SI_ACB_HUANGAC, 0.0, (short) SI_AT_BREAST, (short) 1, 0.0)
		);

		assertThrows(
				AgeTypeErrorException.class,
				() -> Age2Age.age_to_age((short) SI_ACT_THROWERAC, 0.0, (short) SI_AT_TOTAL, (short) 0, 0.0)
		);
	}

	@Test
	void testShouldNotBranchInvalid() {
		assertThrows(
				AgeTypeErrorException.class,
				() -> Age2Age.age_to_age((short) 600, 0.0, (short) SI_AT_BREAST, (short) 1, 0.0)
		);

		assertThrows(
				AgeTypeErrorException.class,
				() -> Age2Age.age_to_age((short) 600, 0.0, (short) SI_AT_TOTAL, (short) 0, 0.0)
		);
	}

	@Test
	void testShouldBranchReturnValue() {
		double expectedResult = 3.0; // normal calculated test
		double actualResult = Age2Age.age_to_age((short) SI_AT_NIGH, 1.5, (short) SI_AT_BREAST, (short) SI_AT_TOTAL, 2);

		assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

		expectedResult = 0; // returnValue < 0
		actualResult = Age2Age.age_to_age((short) SI_BA_KURUCZ82AC, 0, (short) SI_AT_BREAST, (short) SI_AT_TOTAL, 0);

		expectedResult = 1.0; // normal calculated test
		actualResult = Age2Age.age_to_age((short) SI_BA_NIGH, 1.5, (short) SI_AT_TOTAL, (short) SI_AT_BREAST, 1);

		assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

		expectedResult = 0; // returnValue < 0
		actualResult = Age2Age.age_to_age((short) SI_BL_CHENAC, 0, (short) SI_AT_TOTAL, (short) SI_AT_BREAST, 1);

		assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
	}

	@Test
	void testShouldNotBranchReturnValue() {
		double expectedResult = 3.0; // normal calculated test
		double actualResult = Age2Age.age_to_age((short) 600, 1, (short) SI_AT_BREAST, (short) SI_AT_TOTAL, 2);

		assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

		expectedResult = 0; // returnValue < 0
		actualResult = Age2Age.age_to_age((short) 600, 0, (short) SI_AT_BREAST, (short) SI_AT_TOTAL, -2);

		expectedResult = 1.0; // normal calculated test
		actualResult = Age2Age.age_to_age((short) 600, 2, (short) SI_AT_TOTAL, (short) SI_AT_BREAST, 1);

		assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

		expectedResult = 0; // returnValue < 0
		actualResult = Age2Age.age_to_age((short) 600, 0, (short) SI_AT_TOTAL, (short) SI_AT_BREAST, 1);

		assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
	}

	@Test
	void testShouldBranchNoIfMatch() {
		assertThrows(
				AgeTypeErrorException.class,
				() -> Age2Age.age_to_age((short) SI_SS_NIGH, 0.0, (short) 5, (short) 6, 0.0)
		);
	}
}
