package ca.bc.gov.nrs.vdyp.io.parse;

import java.util.Map;

/**
 * Parser for a Small Component Area data file. Each line contains a species code
 * and an array of two coefficients (one-based).
 * <ol>
 * <li>(cols 0-2) key - a Species code</li>
 * <li>(col 3-12, 13-22) - float - coefficients</li>
 * </ol>
 * Lines with an empty Species code are considered blank and are skipped. All lines in
 * the file are parsed.
 * <p>
 * The result of the parse is a {@link Map} of Species codes to a set of two coefficients.
 * <p>
 * FIP Control index: 082
 * <p>
 * Example file: coe/REGDQ4C.DAT
 *
 * @author Kevin Smith, Vivid Solutions
 * @see SimpleCoefficientParser1
 */
public class SmallComponentDQParser extends SimpleCoefficientParser1<String> {

	public static final String CONTROL_KEY = "SMALL_COMP_DQ";

	public SmallComponentDQParser() {
		super(String.class, 1, CONTROL_KEY);
		this.speciesKey();
		this.coefficients(2, 10);
	}

}
