package ca.bc.gov.nrs.vdyp.io.parse;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import ca.bc.gov.nrs.vdyp.model.Coefficients;
import ca.bc.gov.nrs.vdyp.model.MatrixMap2;
import ca.bc.gov.nrs.vdyp.model.MatrixMap2Impl;

/**
 * Parses a mapping from a BEC Zone Alias and Species to a list of eight coefficients. Each row contains 
 * <ol>
 * <li>(cols 0-3) BEC Zone Alias</li>
 * <li>(col 6) int - index into the zero-based coefficient list (range 0-7)</li>
 * <li>(cols 7-9) int - indicator. 0 = process only the first coefficient; 1 = process all coefficients</li>
 * <li>(cols 10-17, 18-25, 26-33 ...) float * 16 - coefficient list, by species ordinal (AC = 0, AT = 1, etc)</li>
 * </ol>
 * All lines are parsed. A BEC Zone Alias value, trimmed, of "" results in the line being skipped.
 * <p>
 * The result of the parse is a map from BEC Zone Alias and Species to a (zero-based) eight-element coefficient array.
 * <p>
 * FIP Control index: 121
 * <p>
 * Example file: coe/GROWBA27.COE
 *
 * @author Michael Junkin, Vivid Solutions
 * @see ControlMapSubResourceParser
 */
public class BasalAreaGrowthEmpiricalParser implements ControlMapSubResourceParser<MatrixMap2<String, String, Coefficients>> {

	public static final String CONTROL_KEY = "BA_GROWTH_EMPIRICAL";

	public static final String BEC_ZONE_ID_KEY = "BecId";
	public static final String INDEX_KEY = "index";
	public static final String INDICATOR_KEY = "indicator";
	public static final String COEFFICIENTS_KEY = "coefficients";

	private int NUM_COEFFICIENTS = 8;
	private int NUM_SPECIES = 16;

	public BasalAreaGrowthEmpiricalParser() {
		this.lineParser = new LineParser() {

			@Override
			public boolean isIgnoredLine(String line) {
				return line.substring(0, Math.min(4,  line.length())).trim().length() == 0;
			}

		}
		.value(4, BEC_ZONE_ID_KEY, ValueParser.STRING)
		.space(2)
		.value(1, INDEX_KEY, ValueParser.INTEGER)
		.value(2, INDICATOR_KEY, ValueParser.INTEGER)
		.multiValue(NUM_SPECIES, 8, COEFFICIENTS_KEY, ValueParser.FLOAT);
	}

	LineParser lineParser;

	@Override
	public MatrixMap2<String, String, Coefficients> parse(InputStream is, Map<String, Object> control)
			throws IOException, ResourceParseException {

		var becAliases = BecDefinitionParser.getBecs(control).getBecAliases();
		var sp0Aliases = GenusDefinitionParser.getSpeciesAliases(control);
		MatrixMap2<String, String, Coefficients> result = new MatrixMap2Impl<>(
				becAliases, sp0Aliases, (k1, k2) -> Coefficients.empty(NUM_COEFFICIENTS, 0)
		);
		
		lineParser.parse(is, result, (v, r) -> {
			var becZoneId = (String)v.get(BEC_ZONE_ID_KEY);
			var index = (int) v.get(INDEX_KEY);
			var indicator = (int) v.get(INDICATOR_KEY);
			
			@SuppressWarnings("unchecked")
			var specCoefficients = (List<Float>) v.get(COEFFICIENTS_KEY);

			if (index < 0 || index >= NUM_COEFFICIENTS) {
				throw new ValueParseException("Index value " + index + " is out of range; expecting a value from 0 to 7");
			}
			
			if (indicator < 0 || indicator > 1) {
				throw new ValueParseException("Indicator value " + indicator + " is out of range; expecting either 0 or 1");
			}
			
			BecDefinitionParser.getBecs(control).get(becZoneId).orElseThrow(() -> new ValueParseException("BEC Zone Id " + becZoneId + " is not a recognized BEC Zone"));
			
			var specIt = sp0Aliases.iterator();
			
			int coefficientIndex = 0;
			while (specIt.hasNext()) {
				
				Coefficients coefficients = result.get(becZoneId, specIt.next());
				Float coe = specCoefficients.get(coefficientIndex);
				coefficients.setCoe(index, coe);
				
				if (indicator == 0)
					break;
				
				coefficientIndex += 1;
			}

			return r;
		}, control);

		return result;
	}

	@Override
	public String getControlKey() {
		return CONTROL_KEY;
	}
}
