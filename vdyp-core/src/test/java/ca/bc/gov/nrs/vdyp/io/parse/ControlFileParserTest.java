package ca.bc.gov.nrs.vdyp.io.parse;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasProperty;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import ca.bc.gov.nrs.vdyp.io.parse.ControlFileParser.Entry;

public class ControlFileParserTest {

	@Test
	void testParsesEntriesSimple() throws Exception {
		var parser = makeParser();

		try (InputStream is = new ByteArrayInputStream("001 Control".getBytes()); var stream = parser.parseEntries(is);) {
			var result = stream.collect(Collectors.toList());

			assertThat(result, contains(controlEntry(equalTo(1), equalTo(" "), equalTo("Control"))));
		}
	}

	@Test
	void testParsesEntriesExtended() throws Exception {
		var parser = makeParser();

		try (
				InputStream is = new ByteArrayInputStream(
						"001XControl that is longer than 50 characters. Blah Blah Blah Blah.".getBytes()
				);
				var stream = parser.parseEntries(is)
		) {
			var result = stream.collect(Collectors.toList());

			assertThat(
					result,
					contains(
							controlEntry(
									equalTo(1), equalTo("X"), equalTo("Control that is longer than 50 characters. Blah Blah Blah Blah.")
							)
					)
			);
		}
	}

	@Test
	void testParsesEntriesExtendedAlternate() throws Exception {
		var parser = makeParser();

		try (
				InputStream is = new ByteArrayInputStream(
						"001>Control that is longer than 50 characters. Blah Blah Blah Blah.".getBytes()
				);
				var stream = parser.parseEntries(is)
		) {
			var result = stream.collect(Collectors.toList());

			assertThat(
					result,
					contains(
							controlEntry(
									equalTo(1), equalTo(">"), equalTo("Control that is longer than 50 characters. Blah Blah Blah Blah.")
							)
					)
			);
		}
	}

	@Test
	void testParsesEntriesWithDistantComment() throws Exception {
		var parser = makeParser();

		try (
				InputStream is = new ByteArrayInputStream(
						"001 Control                                           Comment".getBytes()
				);
				var stream = parser.parseEntries(is)
		) {
			var result = stream.collect(Collectors.toList());

			assertThat(
					result,
					contains(
							controlEntry(equalTo(1), equalTo(" "), equalTo("Control                                           "))
					)
			);
		}
	}

	@Test
	void testParsesEntriesExtendedWithDistantComment() throws Exception {
		var parser = makeParser();

		try (
				InputStream is = new ByteArrayInputStream(
						"001XControl                                                                                                                 Comment"
								.getBytes()
				);
				var stream = parser.parseEntries(is)
		) {
			var result = stream.collect(Collectors.toList());

			assertThat(
					result,
					contains(
							controlEntry(
									equalTo(1), equalTo("X"),
									equalTo(
											"Control                                                                                                                 "
									)
							)
					)
			);
		}
	}

	@Test
	void testParsesEntriesWithMarkedComment() throws Exception {
		var parser = makeParser();

		try (
				InputStream is = new ByteArrayInputStream("001 Control!Comment".getBytes());
				var stream = parser.parseEntries(is)
		) {
			var result = stream.collect(Collectors.toList());

			assertThat(result, contains(controlEntry(equalTo(1), equalTo(" "), equalTo("Control"))));
		}
	}

	@Test
	void testParsesEntriesExtendedWithMarkedComment() throws Exception {
		var parser = makeParser();

		try (
				InputStream is = new ByteArrayInputStream("001XControl!Comment".getBytes());
				var stream = parser.parseEntries(is)
		) {
			var result = stream.collect(Collectors.toList());

			assertThat(result, contains(controlEntry(equalTo(1), equalTo("X"), equalTo("Control"))));
		}
	}

	@Test
	void testParsesEntriesIgnoreCommentLinesByExtendedMarker() throws Exception {
		var parser = makeParser();

		try (InputStream is = new ByteArrayInputStream("001CComment".getBytes()); var stream = parser.parseEntries(is)) {
			var result = stream.collect(Collectors.toList());

			assertThat(result, empty());
		}
	}

	@Test
	void testParsesEntriesIgnoreCommentLinesByZeroIndex() throws Exception {
		var parser = makeParser();

		try (InputStream is = new ByteArrayInputStream("000 Comment".getBytes()); var stream = parser.parseEntries(is);) {
			var result = stream.collect(Collectors.toList());

			assertThat(result, empty());
		}
	}

	@Test
	void testParsesEntriesIgnoreCommentLinesByNullIndex() throws Exception {
		var parser = makeParser();

		try (InputStream is = new ByteArrayInputStream("    Comment".getBytes()); var stream = parser.parseEntries(is);) {
			var result = stream.collect(Collectors.toList());

			assertThat(result, empty());
		}
	}

	@Test
	void testParsesEntriesIgnoreEmptyLines() throws Exception {
		var parser = makeParser();

		try (
				InputStream is = new ByteArrayInputStream("\n \n  \n   \n    ".getBytes());
				var stream = parser.parseEntries(is);
		) {
			var result = stream.collect(Collectors.toList());

			assertThat(result, empty());
		}
	}

	@Test
	void testParsesMultipleEntries() throws Exception {
		var parser = makeParser();

		try (
				var is = new ByteArrayInputStream("001 Control 1\n002 Control 2".getBytes());
				var stream = parser.parseEntries(is);
		) {
			var result = stream.collect(Collectors.toList());

			assertThat(
					result,
					contains(
							controlEntry(equalTo(1), equalTo(" "), equalTo("Control 1")),
							controlEntry(equalTo(2), equalTo(" "), equalTo("Control 2"))
					)
			);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testParseToMap() throws Exception {
		var parser = makeParser();
		String file = 
				  "097 coe\\vetdq2.dat                                    DQ for Vet layer           RD_YDQV\n"
				+ "098 coe\\REGBAV01.coe                                  VET BA, IPSJF168.doc       RD_E098\n"
				+ "\n"
				+ "197    5.0   0.0   2.0                                Minimum Height, Minimum BA, Min BA fully stocked.\n"
				+ "\n"
				+ "198 coe\\MOD19813.prm                                  Modifier file (IPSJF155, XII) RD_E198\n"
				+ "199  0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 Debug switches (0 by default) See IPSJF155  App IX                              Debug switches (25) 0=default See IPSJF155, App IX\n"
				+ "                                                      1st:  1: Do NOT apply BA limits from SEQ043\n"
				+ "                                                      2nd:  1: Do NOT apply DQ limits from SEQ043\n";
		try(
			var is = new ByteArrayInputStream(file.getBytes());
		) {
			var result = parser.parseToMap(is);

			
			assertThat(result, hasEntry(equalTo("097"), equalTo("coe\\vetdq2.dat")));
			
			assertThat(result, hasEntry(equalTo("098"), equalTo("coe\\REGBAV01.coe")));
			assertThat(result, hasEntry(equalTo("minimums"), (Matcher)contains(5.0f, 0.0f, 2.0f)));
			assertThat(result, hasEntry(equalTo("modifier_file"), equalTo("coe\\MOD19813.prm")));
			assertThat(result, hasEntry(equalTo("debugSwitches"), (Matcher)contains(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0)));
		}
	}

	private static Matcher<Entry> controlEntry(Matcher<Integer> index, Matcher<String> extend, Matcher<String> control) {
		return allOf(hasProperty("index", index), hasProperty("extend", extend), hasProperty("control", control));
	}

	private ControlFileParser makeParser() {
		var identifiers = new HashMap<Integer, String>();
		var parsers = new HashMap<Integer, Function<String, ?>>();

		identifiers.put(197, "minimums");
		parsers.put(197, (String s) -> Arrays.stream(s.strip().split("\s+")).map(Float::valueOf).collect(Collectors.toList()));

		identifiers.put(198, "modifier_file");

		identifiers.put(199, "debugSwitches");
		parsers.put(199, (String s) -> Arrays.stream(s.strip().split("\s+")).map(Integer::valueOf).collect(Collectors.toList()));

		return new ControlFileParser(identifiers, parsers);
	}

}
