package ca.bc.gov.nrs.vdyp.io.parse;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import ca.bc.gov.nrs.vdyp.model.CompVarAdjustments;

public class CompVarAdjustmentsParser implements ControlMapSubResourceParser<CompVarAdjustments>
{
	public static final String CONTROL_KEY = "PARAM_ADJUSTMENTS";
	
	private static final int MAX_INDEX = 98;
	
	private static final String INDEX_KEY = "index";
	private static final String ADJUSTMENT_KEY = "adjustmentKey";
	
	private LineParser lineParser;

	public CompVarAdjustmentsParser() {
		
		this.lineParser = new LineParser() {

			@Override
			public boolean isStopLine(String line) {
				return line.startsWith("999");
			}

			@Override
			public boolean isIgnoredLine(String line) {
				return line.substring(0, Math.min(line.length(), 3)).trim().length() == 0;
			}
		}.value(3, INDEX_KEY, ValueParser.INTEGER).value(8, ADJUSTMENT_KEY, ValueParser.FLOAT);
	}

	@Override
	public CompVarAdjustments parse(InputStream is, Map<String, Object> control)
			throws IOException, ResourceParseException {

		Map<Integer, Float> values = new HashMap<>();
		
		lineParser.parse(is, values, (v, r) -> {
			var index = (Integer) v.get(INDEX_KEY);
			
			if (index < 1 || index > MAX_INDEX)
				throw new ValueParseException("Index " + index + " is not in the required range of 1.." + MAX_INDEX);
			
			var adjustment = (Float) v.get(ADJUSTMENT_KEY);

			r.put(index, adjustment);
			
			return r;
		}, control);
		
		return new CompVarAdjustments(values);
	}
	
	@Override
	public String getControlKey() {
		return CONTROL_KEY;
	}
}
