package ca.bc.gov.nrs.vdyp.fip;

import org.junit.jupiter.api.Test;

import ca.bc.gov.nrs.vdyp.io.parse.ControlFileParserTest;

public class FipControlParserTest {

	@Test
	public void testParse() throws Exception {
		var parser = new FipControlParser();
		var result = parser.parse(ControlFileParserTest.class, "FIPSTART.CTR");
		
	}
}
