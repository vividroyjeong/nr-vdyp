package ca.bc.gov.nrs.vdyp.fip;

import org.junit.jupiter.api.Test;

public class FipControlParserTest {

	@Test
	public void testParse() throws Exception {
		var parser = new FipControlParser();
		var result = parser.parse(FipControlParserTest.class, "FIPSTART.CTR");
		
	}
}
