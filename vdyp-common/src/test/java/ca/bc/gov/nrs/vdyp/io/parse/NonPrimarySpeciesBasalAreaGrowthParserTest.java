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

public class NonPrimarySpeciesBasalAreaGrowthParserTest {

	@Test
	public void readAllTest() {
		var parser = new NonPrimarySpeciesBasalAreaGrowthParser();
	
		Map<String, Object> controlMap = new HashMap<>();
		
		TestUtils.populateControlMapGenusReal(controlMap);
		TestUtils.populateControlMapFromResource(controlMap, parser, "BASP06.COE");
		
		@SuppressWarnings("unchecked")
		MatrixMap2<String, Integer, Coefficients> m = (MatrixMap2<String, Integer, Coefficients>)controlMap
				.get(ControlKey.NON_PRIMARY_SP_BA_GROWTH.name());
		
		assertThat(m.get("AC", 0), contains(-0.08787f, 0.016335f, 0.00907f));
		assertThat(m.get("Y", 30), contains(0.05873f, -0.011052f, -0.02011f));
	}
}
