package ca.bc.gov.nrs.vdyp.io.parse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import ca.bc.gov.nrs.vdyp.model.Coefficients;

public class UpperBoundsParser extends SimpleCoefficientParser1<Integer> {
	
	public static final int MAX_BA_GROUPS = 180;
	public static final String CONTROL_KEY = "BA_DQ_UPPER_BOUNDS";
	
	public UpperBoundsParser() {
		super(Integer.class, 1, CONTROL_KEY);
		
		this.groupIndexKey(MAX_BA_GROUPS).coefficients(2, 8
				, Optional.of((Void) -> new Coefficients(new float[] { 0.0f, 7.6f }, 1))
				, Optional.empty());
	}
	
	@Override
	public Map<Integer, Coefficients> parse(InputStream is, Map<String, Object> control)
			throws IOException, ResourceParseException {
		
		Map<Integer, Coefficients> m = super.parse(is, control);
		
		IntStream.range(1, MAX_BA_GROUPS).filter(i -> ! m.containsKey(i))
				.forEach(i -> m.put(i, new Coefficients(new float[] { 0.0f, 7.6f }, 1)));
		
		return m;
	}
}
