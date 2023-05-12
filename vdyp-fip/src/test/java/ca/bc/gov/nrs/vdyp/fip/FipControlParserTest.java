package ca.bc.gov.nrs.vdyp.fip;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.util.Map;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import ca.bc.gov.nrs.vdyp.io.parse.ControlFileParserTest;
import ca.bc.gov.nrs.vdyp.model.BecDefinition;

public class FipControlParserTest {

	@Test
	public void testParse() throws Exception {
		var parser = new FipControlParser();
		var result = parser.parse(ControlFileParserTest.class, "FIPSTART.CTR");
		
	}
	
	@Test
	public void testParseBec() throws Exception {
		var parser = new FipControlParser();
		var result = parser.parse(ControlFileParserTest.class, "FIPSTART.CTR");
		assertThat(result, (Matcher) hasEntry(is(FipControlParser.BEC_DEF), 
				allOf(
						instanceOf(Map.class),
						hasEntry(
								is("AT"), 
								instanceOf(BecDefinition.class)))));
	}
}
