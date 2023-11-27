package ca.bc.gov.nrs.vdyp.io.parse;

import static ca.bc.gov.nrs.vdyp.common.Utils.constMap;
import static ca.bc.gov.nrs.vdyp.test.VdypMatchers.isMarker;
import static ca.bc.gov.nrs.vdyp.test.VdypMatchers.isValue;
import static ca.bc.gov.nrs.vdyp.test.VdypMatchers.notPresent;
import static ca.bc.gov.nrs.vdyp.test.VdypMatchers.present;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ca.bc.gov.nrs.vdyp.common.Utils;
import ca.bc.gov.nrs.vdyp.model.Layer;
import ca.bc.gov.nrs.vdyp.test.VdypMatchers;

class ValueParserTest {

	@Test
	void testRangeParser() throws Exception {
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

	@Test
	void testEnumParser() throws Exception {
		var unit = ValueParser.enumParser(Layer.class);

		assertThat(unit.parse("PRIMARY"), is(Layer.PRIMARY));

		var ex = assertThrows(ValueParseException.class, () -> unit.parse("NOT_A_LAYER"));
		assertThat(ex, hasProperty("message", is("\"NOT_A_LAYER\" is not a valid Layer")));
	}

	@Test
	void testValidateRangeInclusive() throws Exception {
		var unit = ValueParser.validateRangeInclusive(10f, 20f, "Test");

		assertThat(unit.apply(10f), notPresent());
		assertThat(unit.apply(20f), notPresent());
		assertThat(unit.apply(11f), notPresent());
		assertThat(unit.apply(19f), notPresent());
		assertThat(unit.apply(9f), present(is("Test is expected to be between 10.0 and 20.0 but was 9.0")));
		assertThat(unit.apply(21f), present(is("Test is expected to be between 10.0 and 20.0 but was 21.0")));

	}

	@Test
	void testValidateRangeInclusiveBadRange() throws Exception {
		assertThrows(IllegalArgumentException.class, () -> ValueParser.validateRangeInclusive(20f, 10f, "Test"));
	}

	@Test
	void testPretestOptional() throws Exception {
		var unit = ValueParser.pretestOptional(ValueParser.INTEGER, x -> !x.equals("IGNORE"));

		assertThat(unit.parse("1"), present(is(1)));
		assertThat(unit.parse("2"), present(is(2)));
		assertThat(unit.parse("IGNORE"), notPresent());
		assertThrows(ValueParseException.class, () -> unit.parse("DONT_IGNORE"));
	}

	@Test
	void testPosttestOptional() throws Exception {
		var unit = ValueParser.posttestOptional(ValueParser.INTEGER, x -> !x.equals(1));

		assertThat(unit.parse("0"), present(is(0)));
		assertThat(unit.parse("1"), notPresent());
		assertThat(unit.parse("2"), present(is(2)));
		assertThrows(ValueParseException.class, () -> unit.parse("DONT_IGNORE"));
	}

	@Test
	void testValueOrMarker() throws Exception {
		var unit = ValueParser.valueOrMarker(
				ValueParser.INTEGER, ValueParser.pretestOptional(ValueParser.STRING, x -> x.equals("MARKER"))
		);

		assertThat(unit.parse("0"), isValue(is(0)));
		assertThat(unit.parse("1"), isValue(is(1)));
		assertThat(unit.parse("MARKER"), isMarker(is("MARKER")));
		assertThrows(ValueParseException.class, () -> unit.parse("INVALID"));
	}

	@Test
	void testOptionalSingleton() throws Exception {
		var unit = ValueParser.optionalSingleton(x -> x.equals("PASS"), "X");

		assertThat(unit.parse("FAIL"), notPresent());
		assertThat(unit.parse("PASS"), present(is("X")));
	}

	@Test
	void testLayer() throws Exception {
		var unit = ValueParser.LAYER;

		assertThat(unit.parse("1"), present(is(Layer.PRIMARY)));
		assertThat(unit.parse("P"), present(is(Layer.PRIMARY)));

		assertThat(unit.parse("2"), present(is(Layer.SECONDARY)));
		assertThat(unit.parse("S"), present(is(Layer.SECONDARY)));

		assertThat(unit.parse("V"), present(is(Layer.VETERAN)));

		assertThat(unit.parse(""), notPresent());
		assertThat(unit.parse(" "), notPresent());
		assertThat(unit.parse("X"), notPresent());
	}

	@Test
	void testToMap() throws Exception {
		var map = Utils.<String, Integer>constMap(x -> {
			x.put("K3", 42);
			x.put("K4", 23);
		});
		var unit = ValueParser
				.<String, Integer>toMap(ValueParser.list(ValueParser.INTEGER), map, "K1", "K2", "K3", "K4");

		assertThat(unit.parse(" 2 3"), equalTo(constMap(x -> {
			x.put("K1", 2);
			x.put("K2", 3);
			x.put("K3", 42);
			x.put("K4", 23);
		})));
		assertThat(unit.parse(" 2 3 4"), equalTo(constMap(x -> {
			x.put("K1", 2);
			x.put("K2", 3);
			x.put("K3", 4);
			x.put("K4", 23);
		})));
		assertThat(unit.parse(" 2 3 4 5"), equalTo(constMap(x -> {
			x.put("K1", 2);
			x.put("K2", 3);
			x.put("K3", 4);
			x.put("K4", 5);
		})));

		assertThrows(ValueParseException.class, () -> unit.parse(" 2 "));

		assertThrows(
				IllegalArgumentException.class,
				() -> ValueParser
						.<String, Integer>toMap(ValueParser.list(ValueParser.INTEGER), map, "K1", "K3", "K2", "K4")
		);

	}

}
