package ca.bc.gov.nrs.vdyp.io.parse;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.model.Coefficients;
import ca.bc.gov.nrs.vdyp.model.MatrixMap2;
import ca.bc.gov.nrs.vdyp.test.TestUtils;

public class NonPrimarySpeciesDqGrowthParserTest {

	@Test
	public void readAllTest() {
		var parser = new NonPrimarySpeciesDqGrowthParser();
	
		Map<String, Object> controlMap = new HashMap<>();
		
		TestUtils.populateControlMapGenusReal(controlMap);
		TestUtils.populateControlMapFromResource(controlMap, parser, "DQSP06.COE");
		
		@SuppressWarnings("unchecked")
		MatrixMap2<String, Integer, Coefficients> m = (MatrixMap2<String, Integer, Coefficients>)controlMap
				.get(ControlKey.NON_PRIMARY_SP_DQ_GROWTH.name());
		
		assertThat(m.get("AC", 0), contains(-0.010264f, 0.005373f, -0.016904f));
		assertThat(m.get("Y", 30), contains(0.069221f, -0.024821f, 0.001982f));
	}
}
