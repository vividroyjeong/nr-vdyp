package ca.bc.gov.nrs.vdyp.io.parse;

import org.junit.jupiter.api.Test;

public class BecDefinitionParserTest {
	
	@Test
	public void testParse() throws Exception {
		var parser = new BecDefinitionParser();
		
		parser.parse(ControlFileParserTest.class, "coe/Becdef.dat");
	}
	
}
