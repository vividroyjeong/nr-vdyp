package ca.bc.gov.nrs.vdyp.sindex;

import ca.bc.gov.nrs.vdyp.common_calculators.*;
import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class SindxdllTest {
	@Test
	public void test_JUnit() {
		String str1 = "Let's check this string";
		assertEquals("Let's check this string", str1);
	}

	@Test
	public void testVersionNumber() {
		short expectedValue = 151;
		short actualValue = Sindxdll.VersionNumber();
		assertThat((double) actualValue, closeTo((double) expectedValue, 0.001));
	}

	@Test
	public void testFirstSpecies() {
		short expectedValue = 0;
		short actualValue = Sindxdll.FirstSpecies();
		assertThat((double) actualValue, closeTo((double) expectedValue, 0.001));
	}

	@Test
	public void testNextSpeciesValidIndex() {
		short inputIndex = 2; // Choose a valid index for testing
		short expectedOutput = (short) (inputIndex + 1);

		short actualOutput = Sindxdll.NextSpecies(inputIndex);

		assertEquals(expectedOutput, actualOutput, "NextSpecies should return the next species index");
	}

	@Test
	public void testNextSpeciesTooSmallIndex() {
		short invalidIndex = -1; // Choose an invalid index for testing
		assertThrows(
				SpeciesErrorException.class, () -> Sindxdll.NextSpecies(invalidIndex),
				"NextSpecies should throw SpeciesErrorException for invalid index"
		);
	}

	@Test
	public void testNextSpeciesTooBigIndex() {
		short invalidIndex = 135; // Choose an invalid index for testing
		assertThrows(
				SpeciesErrorException.class, () -> Sindxdll.NextSpecies(invalidIndex),
				"NextSpecies should throw SpeciesErrorException for invalid index"
		);
	}

	@Test
	public void testNextSpeciesLastIndex() {
		short lastIndex = 134; // Use the value of SI_SPEC_END for testing
		assertThrows(
				NoAnswerException.class, () -> Sindxdll.NextSpecies(lastIndex),
				"NextSpecies should throw NoAnswerException for last defined species index"
		);
	}

}