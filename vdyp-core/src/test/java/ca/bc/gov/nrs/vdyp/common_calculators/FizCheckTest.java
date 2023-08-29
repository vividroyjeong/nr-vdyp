package ca.bc.gov.nrs.vdyp.common_calculators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.*;

public class FizCheckTest {
	// From sindex.h
	private static short FIZ_UNKNOWN = 0;
	private static short FIZ_COAST = 1;
	private static short FIZ_INTERIOR = 2;

	@Test
	public void testCoastalFiz() {
		for (char fiz = 'A'; fiz <= 'C'; fiz++) {
			short result = FizCheck.fiz_check(fiz);
			assertEquals(FIZ_COAST, result);
		}
	}

	@Test
	public void testInteriorFiz() {
		for (char fiz = 'D'; fiz <= 'L'; fiz++) {
			short result = FizCheck.fiz_check(fiz);
			assertEquals(FIZ_INTERIOR, result);
		}
	}

	@Test
	public void testUnknownFiz() {
		char fiz = 'X'; // Replace with any unknown fiz value
		short result = FizCheck.fiz_check(fiz);
		assertEquals(FIZ_UNKNOWN, result);
	}
}
