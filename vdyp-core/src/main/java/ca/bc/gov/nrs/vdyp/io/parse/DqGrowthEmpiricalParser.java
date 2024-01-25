package ca.bc.gov.nrs.vdyp.io.parse;

import java.util.Optional;

/**
 * Parses a mapping from a Basal Area Group number to a list of ten coefficients. Each row contains 
 * <ol>
 * <li>(cols 0-2) int - Basal Area Group number</li>
 * <li>(cols 3-12, 13-22, 23-32, ...) float * 10 - coefficient list</li>
 * </ol>
 * All lines are parsed. There is no provision for blank lines; all lines must have content. If fewer than
 * ten coefficients are supplied on a given line, the remainder default to 0.0f.
 * <p>
 * The result of the parse is a map from a Basal Area Group number to a (one-based) ten-element coefficient array.
 * <p>
 * Control index: 122
 * <p>
 * Example file: coe/GD23.coe
 *
 * @author Michael Junkin, Vivid Solutions
 * @see SimpleCoefficientParser1
 */
public class DqGrowthEmpiricalParser extends SimpleCoefficientParser1<Integer> {
	
	public static final String CONTROL_KEY = "DQ_GROWTH_EMPIRICAL";
	public static final int MAX_GROUPS = 40;
	
	public DqGrowthEmpiricalParser() {
		super(Integer.class, 1, CONTROL_KEY);
		
		this.groupIndexKey(MAX_GROUPS).coefficients(10, 9
				, Optional.empty()
				, Optional.of((index) -> 0.0f));
	}
}
