package ca.bc.gov.nrs.vdyp.io.parse;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ValueParserTest {

	@Test
	void rangeParserTest() throws Exception {
		var exclusiveParser = ValueParser.range(ValueParser.INTEGER, 10, false, 20, false, "Test");

		var result = exclusiveParser.parse("11");
		assertThat(result, is(11));

		result = exclusiveParser.parse("19");
		assertThat(result, is(19));

		var ex = assertThrows(ValueParseException.class, () -> exclusiveParser.parse("10"));
		assertThat(ex, hasProperty("message", is("Test must be greater than 10.")));

		ex = assertThrows(ValueParseException.class, () -> exclusiveParser.parse("9"));
		assertThat(ex, hasProperty("message", is("Test must be greater than 10.")));

		ex = assertThrows(ValueParseException.class, () -> exclusiveParser.parse("20"));
		assertThat(ex, hasProperty("message", is("Test must be less than 20.")));

		ex = assertThrows(ValueParseException.class, () -> exclusiveParser.parse("21"));
		assertThat(ex, hasProperty("message", is("Test must be less than 20.")));

		var inclusiveParser = ValueParser.range(ValueParser.INTEGER, 10, true, 20, true, "Test");

		result = inclusiveParser.parse("11");
		assertThat(result, is(11));

		result = inclusiveParser.parse("19");
		assertThat(result, is(19));

		result = inclusiveParser.parse("10");
		assertThat(result, is(10));

		result = inclusiveParser.parse("20");
		assertThat(result, is(20));

		ex = assertThrows(ValueParseException.class, () -> inclusiveParser.parse("9"));
		assertThat(ex, hasProperty("message", is("Test must be greater than or equal to 10.")));

		ex = assertThrows(ValueParseException.class, () -> inclusiveParser.parse("21"));
		assertThat(ex, hasProperty("message", is("Test must be less than or equal to 20.")));

	}

	static enum TestEnum {
		VALUE1,
		VALUE2
	}
	
	@Test
	void enumParserTest() throws Exception {
		var parser = ValueParser.enumParser(TestEnum.class);
		
		assertThat(parser.parse("VALUE1"), is(TestEnum.VALUE1));
		assertThat(parser.parse("VALUE2"), is(TestEnum.VALUE2));
		
		var ex = assertThrows(ValueParseException.class, ()->parser.parse("FAKE"));
		assertThat(ex.getMessage(), is("\"FAKE\" is not a valid TestEnum"));
		
		ex = assertThrows(ValueParseException.class, ()->parser.parse(""));
		assertThat(ex.getMessage(), is("\"\" is not a valid TestEnum"));
		
		ex = assertThrows(ValueParseException.class, ()->parser.parse(" "));
		assertThat(ex.getMessage(), is("\"\" is not a valid TestEnum"));
	}
}
