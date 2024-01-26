package ca.bc.gov.nrs.vdyp.io.parse;

import java.util.Map;

/**
 * Parser for a Small Component Probability data file. Each line contains a
 * species code and an array of four coefficients (one-based).
 * <ol>
 * <li>(cols 0-2) a Species code</li>
 * <li>(col 3-12, 13-22, 23-32, 33-42) - float * 4 - coefficients</li>
 * </ol>
 * Lines with an empty Species code are considered blank and are skipped. All
 * lines in the file are parsed.
 * <p>
 * The result of the parse is a {@link Map} of Species codes to a set of four
 * coefficients.
 * <p>
 * FIP Control index: 080
 * <p>
 * Example file: coe/REGPR1C.DAT
 *
 * @author Kevin Smith, Vivid Solutions
 * @see SimpleCoefficientParser1
 */
public class SmallComponentProbabilityParser extends SimpleCoefficientParser1<String> {

	public static final String CONTROL_KEY = "SMALL_COMP_PROBABILITY";

	public SmallComponentProbabilityParser() {
		super(String.class, 1, CONTROL_KEY);
		this.speciesKey();
		this.coefficients(4, 10);
	}

}
