package ca.bc.gov.nrs.vdyp.common_calculators;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.LessThan13Exception;
import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.NoAnswerException;

class SiteIndex2HeightSmoothedTest {
	// Taken from sindex.h
	/*
	 * age types
	 */
	private static final short SI_AT_TOTAL = 0;
	private static final short SI_AT_BREAST = 1;

	/* define species and equation indices */
	private static final short SI_PLI_THROWER = 45;

	private static final double ERROR_TOLERANCE = 0.00001;

	@Test
	void testPpowPositive() {
		assertThat(8.0, closeTo(SiteIndex2HeightSmoothed.ppow(2.0, 3.0), ERROR_TOLERANCE));
		assertThat(1.0, closeTo(SiteIndex2HeightSmoothed.ppow(5.0, 0.0), ERROR_TOLERANCE));
	}

	@Test
	void testPpowZero() {
		assertThat(0.0, closeTo(SiteIndex2HeightSmoothed.ppow(0.0, 3.0), ERROR_TOLERANCE));
	}

	@Test
	void testLlogPositive() {
		assertThat(1.60943, closeTo(SiteIndex2HeightSmoothed.llog(5.0), ERROR_TOLERANCE));
		assertThat(11.51293, closeTo(SiteIndex2HeightSmoothed.llog(100000.0), ERROR_TOLERANCE));
	}

	@Test
	void testLlogZero() {
		assertThat(-11.51293, closeTo(SiteIndex2HeightSmoothed.llog(0.0), ERROR_TOLERANCE));
	}

	@Nested
	class IndexToHeightSmoothedTest {
		@Test
		void testInvalidSiteIndex() {
			assertThrows(
					LessThan13Exception.class,
					() -> SiteIndex2HeightSmoothed
							.index_to_height_smoothed((short) 0, 0.0, (short) 0, 1.2, 0.0, 0.0, 0.0)
			);
		}

		@Test
		void testInvalidY2BH() {
			assertThrows(
					NoAnswerException.class,
					() -> SiteIndex2HeightSmoothed
							.index_to_height_smoothed((short) 0, 0.0, (short) 0, 1.31, -1.0, 0.0, 0.0)
			);
		}

		@Test
		void testItageInvalid() {
			assertThrows(
					NoAnswerException.class,
					() -> SiteIndex2HeightSmoothed
							.index_to_height_smoothed((short) 0, -1.0, SI_AT_BREAST, 1.31, 0.0, 0.0, 0.0)
			);

			double actualResult = SiteIndex2HeightSmoothed
					.index_to_height_smoothed((short) 0, 0.0, SI_AT_BREAST, 1.31, 0.0, 0.0, 0.0);
			double expectedResult = 0;
			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

		@Test
		void testIterationCannotConverge() {
			assertThrows(
					NoAnswerException.class,
					() -> SiteIndex2HeightSmoothed
							.index_to_height_smoothed(SI_PLI_THROWER, 0.0, SI_AT_BREAST, 1.31, 1.0, 0, 0)
			);
		}

		@Test
		void testValidInput() {
			double actualResult = SiteIndex2HeightSmoothed
					.index_to_height_smoothed((short) 21, 3.0, SI_AT_TOTAL, 16.0, 4.0, 3.1, 1.3);
			double expectedResult = 1.3 / 3.1 * 3;
			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			actualResult = SiteIndex2HeightSmoothed
					.index_to_height_smoothed((short) 21, 3.0, SI_AT_TOTAL, 16.0, 4.0, 0.0, 1.0);

			double k1 = 2.6120353509515746; // based on calculation with traced values
			double k0 = (.3) / Math.pow(4, k1);
			expectedResult = k0 * Math.pow(3, k1);
			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));

			actualResult = SiteIndex2HeightSmoothed
					.index_to_height_smoothed((short) 21, 3.0, SI_AT_TOTAL, 16.0, 4.0, 3.0, 1.0);

			expectedResult = 1; // since k0 * Math.pow(0, k1) should be 0
			assertThat(actualResult, closeTo(expectedResult, ERROR_TOLERANCE));
		}

	}

}
