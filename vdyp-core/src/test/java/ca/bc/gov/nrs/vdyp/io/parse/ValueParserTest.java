package ca.bc.gov.nrs.vdyp.io.parse;

import static ca.bc.gov.nrs.vdyp.test.VdypMatchers.notPresent;
import static ca.bc.gov.nrs.vdyp.test.VdypMatchers.present;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import ca.bc.gov.nrs.vdyp.test.VdypMatchers;

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
		VALUE1, VALUE2
	}

	@Test
	void enumParserTest() throws Exception {
		var parser = ValueParser.enumParser(TestEnum.class);

		assertThat(parser.parse("VALUE1"), is(TestEnum.VALUE1));
		assertThat(parser.parse("VALUE2"), is(TestEnum.VALUE2));

		var ex = assertThrows(ValueParseException.class, () -> parser.parse("FAKE"));
		assertThat(ex.getMessage(), is("\"FAKE\" is not a valid TestEnum"));

		ex = assertThrows(ValueParseException.class, () -> parser.parse(""));
		assertThat(ex.getMessage(), is("\"\" is not a valid TestEnum"));

		ex = assertThrows(ValueParseException.class, () -> parser.parse(" "));
		assertThat(ex.getMessage(), is("\"\" is not a valid TestEnum"));
	}

	@Test
	void testValueOrMarkerParser() throws Exception {
		var parser = ValueParser.valueOrMarker(ValueParser.INTEGER, (s) -> {
			if ("MARK".equals(s)) {
				return Optional.of("MARK");
			}
			return Optional.empty();
		});

		assertThat(parser.parse("MARK").getMarker(), present(is("MARK")));
		assertThat(parser.parse("MARK").getValue(), notPresent());

		assertThat(parser.parse("1").getValue(), present(is(1)));
		assertThat(parser.parse("1").getMarker(), notPresent());

		var ex = assertThrows(ValueParseException.class, () -> parser.parse("X"));
		assertThat(ex.getMessage(), is("\"X\" is not a valid Integer"));
	}

}
