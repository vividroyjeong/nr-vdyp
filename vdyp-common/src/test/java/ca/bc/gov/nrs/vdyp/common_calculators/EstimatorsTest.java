package ca.bc.gov.nrs.vdyp.common_calculators;

import static ca.bc.gov.nrs.vdyp.test.VdypMatchers.closeTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import ca.bc.gov.nrs.vdyp.common.Utils;
import ca.bc.gov.nrs.vdyp.model.LayerType;
import ca.bc.gov.nrs.vdyp.model.VdypSpecies;
import ca.bc.gov.nrs.vdyp.test.TestUtils;

public class EstimatorsTest {

	Map<String, Object> controlMap;
	EMP estimators;

	@BeforeEach
	void setup() {
		controlMap = TestUtils.loadControlMap();
		estimators = new EMP(controlMap);
	}

	@Nested
	class EstimateNonPrimaryLoreyHeight {

		@Test
		void testEqn1() throws Exception {

			var bec = Utils.getBec("CWH", controlMap);

			var spec = VdypSpecies.build(builder -> {
				builder.polygonIdentifier("Test", 2024);
				builder.layerType(LayerType.PRIMARY);
				builder.genus("B");
				builder.percentGenus(50f);
				builder.volumeGroup(-1);
				builder.decayGroup(-1);
				builder.breakageGroup(-1);
			});
			var specPrime = VdypSpecies.build(builder -> {
				builder.polygonIdentifier("Test", 2024);
				builder.layerType(LayerType.PRIMARY);
				builder.genus("H");
				builder.percentGenus(50f);
				builder.volumeGroup(-1);
				builder.decayGroup(-1);
				builder.breakageGroup(-1);
			});

			var result = estimators.estimateNonPrimaryLoreyHeight(spec, specPrime, bec, 24.2999992f, 20.5984688f);

			assertThat(result, closeTo(21.5356998f));

		}

		@Test
		void testEqn2() throws Exception {

			var bec = Utils.getBec("CWH", controlMap);

			var spec = VdypSpecies.build(builder -> {
				builder.polygonIdentifier("Test", 2024);
				builder.layerType(LayerType.PRIMARY);
				builder.genus("B");
				builder.percentGenus(50f);
				builder.volumeGroup(-1);
				builder.decayGroup(-1);
				builder.breakageGroup(-1);
			});
			var specPrime = VdypSpecies.build(builder -> {
				builder.polygonIdentifier("Test", 2024);
				builder.layerType(LayerType.PRIMARY);
				builder.genus("D");
				builder.percentGenus(50f);
				builder.volumeGroup(-1);
				builder.decayGroup(-1);
				builder.breakageGroup(-1);
			});

			var result = estimators.estimateNonPrimaryLoreyHeight(spec, specPrime, bec, 35.2999992f, 33.6889763f);

			assertThat(result, closeTo(38.7456512f));

		}
	}
}
