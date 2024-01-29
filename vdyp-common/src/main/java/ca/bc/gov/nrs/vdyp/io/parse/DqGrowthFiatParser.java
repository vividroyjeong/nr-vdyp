package ca.bc.gov.nrs.vdyp.io.parse;

import java.util.Map;

/**
 * Parser for Dq (quadratic mean diameter) growth Fiat model details data file. 
 * <ol>
 * <li>(cols 0-2) int - region (either 1 or 2)</li>
 * <li>(cols 3-8, 9-14, 15-20, 21-26) float - age values</li>
 * <li>(cols 27-32, 33-38, 39-44, 45-50) float - coefficient values</li>
 * <li>(cols 51-56, 57-62, 63-68) float - mixed model coefficient values</li>
 * </ol>
 * The result of the parse is a {@link Map} from Region to a {@link GrowthFaitDetails} instance.
 * <p>
 * Lines with a blank or empty region are ignored. There must be exactly one line for region 1
 * (COASTAL) and one line for region 2 (INTERIOR). No other region ids are supported. The age
 * values must start with a value other than 0.0. 
 * <p>
 * FIP Control index: 117
 * <p>
 * Example file: coe/EMP117A1.prm
 *
 * @author Michael Junkin, Vivid Solutions
 * @see GrowthFiatParser
 */
public class DqGrowthFiatParser extends GrowthFiatParser {
	
	public static final String CONTROL_KEY = "DQ_GROWTH_FIAT";

	@Override
	public String getControlKey()
	{
		return CONTROL_KEY;
	}
}
