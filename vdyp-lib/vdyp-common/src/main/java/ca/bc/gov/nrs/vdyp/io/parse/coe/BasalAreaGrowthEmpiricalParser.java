package ca.bc.gov.nrs.vdyp.io.parse.coe;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.common.Utils;
import ca.bc.gov.nrs.vdyp.io.parse.coe.base.BecZoneBySpeciesCoefficientParser;
import ca.bc.gov.nrs.vdyp.io.parse.common.LineParser;
import ca.bc.gov.nrs.vdyp.io.parse.common.ResourceParseException;
import ca.bc.gov.nrs.vdyp.io.parse.control.ControlMapSubResourceParser;
import ca.bc.gov.nrs.vdyp.io.parse.value.ControlledValueParser;
import ca.bc.gov.nrs.vdyp.io.parse.value.ValueParser;
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
 * If, for a given line "indicator" is zero, only the first coefficient is recorded (for species 0) and all others are
 * set to zero. If "indicator" is one, all coefficients on the line are assigned to their respective species.
 * <p>
 * Control index: 121
 * <p>
 * Example file: coe/GROWBA27.COE
 *
 * @author Michael Junkin, Vivid Solutions
 * @see BecZoneBySpeciesCoefficientParser
 */
public class BasalAreaGrowthEmpiricalParser
		implements ControlMapSubResourceParser<MatrixMap2<String, String, Coefficients>> {

	private static final int NUM_SPECIES = 16;
	private static final int NUM_COEFFICIENTS = 8;

	public static final String BEC_ZONE_ID_KEY = "BecId";
	public static final String INDEX_KEY = "index";
	public static final String INDICATOR_KEY = "indicator";
	public static final String COEFFICIENTS_KEY = "coefficients";

	public BasalAreaGrowthEmpiricalParser() {

		this.lineParser = new LineParser() {
			@Override
			public boolean isIgnoredLine(String line) {
				return Utils.nullOrPrefixBlank(line, 4);
			}

		} //
				.value(4, BEC_ZONE_ID_KEY, ControlledValueParser.BEC) //
				.space(2) //
				.value(
						1, INDEX_KEY,
						ValueParser.range(ValueParser.INTEGER, 0, true, NUM_COEFFICIENTS, false, "Index value")
				) //
				.value(2, INDICATOR_KEY, ValueParser.range(ValueParser.INTEGER, 0, true, 1, true, "Indicator value")) //
				.multiValue(NUM_SPECIES, 8, COEFFICIENTS_KEY, ValueParser.FLOAT);
	}

	private LineParser lineParser;

	@Override
	public MatrixMap2<String, String, Coefficients> parse(InputStream is, Map<String, Object> control)
			throws IOException, ResourceParseException {

		var becAliases = BecDefinitionParser.getBecs(control).getBecAliases();
		var sp0Aliases = GenusDefinitionParser.getSpeciesAliases(control);
		MatrixMap2<String, String, Coefficients> result = new MatrixMap2Impl<>(
				becAliases, sp0Aliases, (k1, k2) -> Coefficients.empty(NUM_COEFFICIENTS, 0)
		);

		lineParser.parse(is, result, (value, r, lineNumber) -> {
			var becZoneId = (String) value.get(BEC_ZONE_ID_KEY);
			var index = (int) value.get(INDEX_KEY);
			var indicator = (int) value.get(INDICATOR_KEY);

			@SuppressWarnings("unchecked")
			var specCoefficients = (List<Float>) value.get(COEFFICIENTS_KEY);

			var specIt = sp0Aliases.iterator();

			int coefficientIndex = 0;
			while (specIt.hasNext()) {
				var spec = specIt.next();
				Coefficients coefficients = r.get(becZoneId, spec);
				Float coe = specCoefficients.get(coefficientIndex);

				if (indicator == 0) {
					coefficients.setCoe(index, coefficientIndex == 0 ? coe : 0.0f);
				} else {
					coefficients.setCoe(index, coe);
				}

				coefficientIndex += 1;
			}

			return r;
		}, control);

		return result;
	}

	@Override
	public ControlKey getControlKey() {
		return ControlKey.BA_GROWTH_EMPIRICAL;
	}
}
