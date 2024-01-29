package ca.bc.gov.nrs.vdyp.io.parse;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import ca.bc.gov.nrs.vdyp.model.Coefficients;
import ca.bc.gov.nrs.vdyp.test.TestUtils;

class UpperBoundsParserTest {

	@Test
	void testParseSimple() throws Exception {

		var parser = new UpperBoundsParser();
		Map<String, Object> controlMap = new HashMap<>();

		TestUtils.populateControlMapFromResource(controlMap, parser, "PCT_407.coe");

		@SuppressWarnings("unchecked")
		Map<Integer, Coefficients> m = (Map<Integer, Coefficients>) controlMap.get(UpperBoundsParser.CONTROL_KEY);
		
		assertThat(m, Matchers.aMapWithSize(UpperBoundsParser.MAX_BA_GROUPS));
		assertThat(m.get(1), Matchers.contains(74.78f, 76.10f));
		assertThat(m.get(144), Matchers.contains(0.0f, 7.6f));
		assertThat(m.get(171), Matchers.contains(125.12f, 49.80f));
	}
}
