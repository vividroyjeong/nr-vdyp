package ca.bc.gov.nrs.vdyp.io.parse;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.bc.gov.nrs.vdyp.io.parse.common.LineParser;
import ca.bc.gov.nrs.vdyp.io.parse.common.ResourceParseException;
import ca.bc.gov.nrs.vdyp.io.parse.control.ControlMapSubResourceParser;
import ca.bc.gov.nrs.vdyp.io.parse.value.ValueParseException;
import ca.bc.gov.nrs.vdyp.io.parse.value.ValueParser;
import ca.bc.gov.nrs.vdyp.model.Coefficients;
import ca.bc.gov.nrs.vdyp.model.ModelCoefficients;

public abstract class PrimarySpeciesGrowthParser implements ControlMapSubResourceParser<Map<Integer, ModelCoefficients>> {
	
	private static final int MAX_BASAL_AREA_GROUP_NUMBER = 30;

	public static final String BASAL_AREA_GROUP_ID_KEY = "BasalAreaGroupId";
	public static final String MODEL_NUMBER_KEY = "Model";
	public static final String COEFFICIENTS_KEY = "Coefficients";

	public PrimarySpeciesGrowthParser() {
		
		this.lineParser = new LineParser() {
					@Override
					public boolean isStopLine(String line) {
						return line == null || line.trim().length() == 0;
					}
				}
			.value(2, BASAL_AREA_GROUP_ID_KEY, ValueParser.INTEGER)
			.value(3, MODEL_NUMBER_KEY, ValueParser.INTEGER)
			.multiValue(3, 10, COEFFICIENTS_KEY, ValueParser.FLOAT);
	}

	private LineParser lineParser;

	@Override
	public Map<Integer, ModelCoefficients> parse(InputStream is, Map<String, Object> control)
			throws IOException, ResourceParseException {

		Map<Integer, ModelCoefficients> result = new HashMap<>();
		
		lineParser.parse(is, result, (value, r, lineNumber) -> {
			var basalAreaGroupId = (Integer) value.get(BASAL_AREA_GROUP_ID_KEY);
			var modelNumber = (Integer) value.get(MODEL_NUMBER_KEY);
			
			@SuppressWarnings("unchecked")
			var coefficientList = (List<Float>) value.get(COEFFICIENTS_KEY);

			if (basalAreaGroupId < 1 || basalAreaGroupId > MAX_BASAL_AREA_GROUP_NUMBER) {
				throw new ValueParseException(
					MessageFormat.format("Line {0}: basal area group id {1} is out of range; expecting a value from 1 to {2}"
							, lineNumber, basalAreaGroupId, MAX_BASAL_AREA_GROUP_NUMBER));
			}
			
			Coefficients coefficients = new Coefficients(coefficientList, 1);
			
			ModelCoefficients mc = new ModelCoefficients(modelNumber, coefficients);
			
			r.put(basalAreaGroupId, mc);

			return r;
		}, control);

		return result;
	}
}
