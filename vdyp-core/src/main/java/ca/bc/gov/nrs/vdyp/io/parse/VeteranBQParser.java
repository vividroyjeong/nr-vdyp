package ca.bc.gov.nrs.vdyp.io.parse;

import ca.bc.gov.nrs.vdyp.model.Region;

/**
 * Parse a Veteran Basal Area Coefficients data file.
 *
 * Each line contains:
 * <ol>
 * <li>(cols 0-1) int - a Species identifier</li>
 * <li>(cols 2-5) int - a Region ('C' or 'I')</li>
 * <li>(cols 6-15, 16-25, 26-35) - floats - coefficients</li>
 * </ol>
 * All lines are read - there is no termination line. There can be no blank lines in the 
 * file. The file must contain 32 lines and all three coefficients must be present for 
 * each line.
 * <p>
 * The result of the parse is a {@link MatrixMap2} of three coefficients (first index = 1)
 * indexed first by Species then Region. 
 * <p>
 * FIP Control index: 098
 * <p>
 * Example file: coe/REGBAV01.COE
 *
 * @author Kevin Smith, Vivid Solutions
 * @see SimpleCoefficientParser2
 */
public class VeteranBQParser extends SimpleCoefficientParser2<String, Region> {

	public static final String CONTROL_KEY = "VETERAN_BQ";

	public VeteranBQParser() {
		super(1, CONTROL_KEY);
		this.speciesKey().space(1).regionKey().coefficients(3, 9);
	}

}