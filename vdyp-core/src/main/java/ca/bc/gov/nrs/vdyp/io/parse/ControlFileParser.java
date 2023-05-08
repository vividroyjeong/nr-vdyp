package ca.bc.gov.nrs.vdyp.io.parse;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Parser for control files
 * 
 * @author Kevin Smith, Vivid Solutions
 *
 */
public class ControlFileParser {

	public static final int INDEX_LENGTH = 3;
	public static final int EXTEND_LENGTH = 1;
	public static final int CONTROL_LENGTH_EXTENDED = 120;
	public static final int CONTROL_LENGTH = 50;
	public static final int NEWLINE_LENGTH = 2;

	public static final int RECORD_BUFFER_LENGTH = INDEX_LENGTH + EXTEND_LENGTH + CONTROL_LENGTH + NEWLINE_LENGTH;
	
	public static final List<String> EXTEND_FLAGS = Arrays.asList("X", ">");
	public static final List<String> COMMENT_FLAGS = Arrays.asList("C");
	public static final String COMMENT_MARKER = "!";

	private Map<Integer, String> identifiers;
	private Map<Integer, Function<String, ?>> valueParsers;
	private Function<String, ?> defaultValueParser;
	
	public static class Entry {
		public final int index;
		public final String extend;
		public final String control;

		public int getIndex() {
			return index;
		}

		public String getExtend() {
			return extend;
		}

		public String getControl() {
			return control;
		}

		public Entry(int index, String extend, String control) {
			super();
			this.index = index;
			this.extend = extend;
			this.control = control;
		}
	}

	/**
	 * 
	 * @param identifiers a map from control file sequence index to meaningful names
	 * @param parsers a map of parsers for control values based on the sequence index
	 * @param defaultParser a default value parser to use when one can't be found in the value parser map
	 */
	public ControlFileParser(
			Map<Integer, String> identifiers, Map<Integer, Function<String, ?>> parsers, Function<String, ?> defaultParser
	) {
		this.identifiers = identifiers;
		this.valueParsers = parsers;
		this.defaultValueParser = defaultParser;
	}
	
	/**
	 * Create a control file parser which does not remap control sequence indexes and which strips leading and trailing whitepsace from values.
	 */
	public ControlFileParser() {
		this(Collections.emptyMap(), Collections.emptyMap(), String::strip);
	}

	public Stream<Entry> parseEntries(InputStream input) {
		return new BufferedReader(new InputStreamReader(input, StandardCharsets.US_ASCII), RECORD_BUFFER_LENGTH).lines()
			.flatMap(line -> {
				// 
				if(line.isBlank()) {
					return Stream.empty();
				}
				final String indexString = line.substring(0, INDEX_LENGTH);
				final String extendString = line.substring(INDEX_LENGTH, INDEX_LENGTH + EXTEND_LENGTH);
				
				// Ignore comments marked with exend flag C
				if(COMMENT_FLAGS.contains(extendString)) {
					return Stream.empty();
				}
				// Ignore comments marked with  a blank index
				final int index;
				if (indexString.isBlank()) {
					return Stream.empty();
				} else {
					index = Integer.valueOf(indexString.strip());
				}
				// Ignore comment marked with index 0
				if(index==0) {
					return Stream.empty();
				}
				
				// How long is the control value
				int controlLength = Math.min(
						EXTEND_FLAGS.contains(extendString) ? 
								CONTROL_LENGTH_EXTENDED : 
								CONTROL_LENGTH, 
						line.length( ) - (INDEX_LENGTH + EXTEND_LENGTH));
				String controlString = line
						.substring(INDEX_LENGTH + EXTEND_LENGTH, INDEX_LENGTH + EXTEND_LENGTH + controlLength);
				
				// Inline comments marked with !
				int startOfComment = controlString.indexOf(COMMENT_MARKER);
				if(startOfComment>-1) {
					controlString = controlString.substring(0,startOfComment);
				}
				
				return Stream.of(new Entry(index, extendString, controlString));
			});
	}

	/**
	 * Parse a control file into a map.  Known index values will be replaced with meaningful identifiers.  
	 * @param input
	 * @return
	 */
	public Map<String, ?> parseToMap(InputStream input) {
		try(Stream<Entry> parseEntries = parseEntries(input);) {
			return parseEntries.collect(
					Collectors.toMap(
							e->identifiers.getOrDefault(e.getIndex(), String.format("%03d", e.getIndex())), 
							e->valueParsers.getOrDefault(e.getIndex(), defaultValueParser).apply(e.getControl()),
						  (x,y)->y // On duplicates, keep the last value
							));
		}
	}
	
	/**
	 * Set a map from control file sequence index to meaningful names
	 */
	public void setIdentifiers(Map<Integer, String> identifiers) {
		this.identifiers = identifiers;
	}

	/**
	 * Set a map of parsers for control values based on the sequence index
	 */
	public void setValueParsers(Map<Integer, Function<String, ?>> parsers) {
		this.valueParsers = parsers;
	}

	/**
	 * Set a default value parser to use when one can't be found in the parser map
	 */
	public void setDefaultValueParser(Function<String, ?> defaultParser) {
		this.defaultValueParser = defaultParser;
	}
	
	
}
