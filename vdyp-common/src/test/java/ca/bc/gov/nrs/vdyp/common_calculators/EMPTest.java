package ca.bc.gov.nrs.vdyp.common_calculators;

import static ca.bc.gov.nrs.vdyp.test.VdypMatchers.closeTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import ca.bc.gov.nrs.vdyp.common.Utils;
import ca.bc.gov.nrs.vdyp.model.LayerType;
import ca.bc.gov.nrs.vdyp.model.Region;
import ca.bc.gov.nrs.vdyp.model.VdypLayer;
import ca.bc.gov.nrs.vdyp.model.VdypSpecies;
import ca.bc.gov.nrs.vdyp.test.TestUtils;

public class EMPTest {

	Map<String, Object> controlMap;
	EMP emp;

	@BeforeEach
	void setup() {
		controlMap = TestUtils.loadControlMap();
		emp = new EMP(controlMap);
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

			var result = emp.estimateNonPrimaryLoreyHeight(spec, specPrime, bec, 24.2999992f, 20.5984688f);

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

			var result = emp.estimateNonPrimaryLoreyHeight(spec, specPrime, bec, 35.2999992f, 33.6889763f);

			assertThat(result, closeTo(38.7456512f));

		}
	}

	@Test
	void testEstimateQuadMeanDiameterForSpecies() throws Exception {

		var layer = VdypLayer.build(builder -> {
			builder.polygonIdentifier("Test", 2024);
			builder.layerType(LayerType.PRIMARY);
			builder.addSite(siteBuilder -> {
				siteBuilder.ageTotal(55f);
				siteBuilder.yearsToBreastHeight(1f);
				siteBuilder.height(32.2999992f);
				siteBuilder.siteGenus("H");
			});
		});

		// sp 3, 4, 5, 8, 15
		// sp B, C, D, H, S
		var spec1 = VdypSpecies.build(layer, builder -> {
			builder.genus("B");
			builder.volumeGroup(12);
			builder.decayGroup(7);
			builder.breakageGroup(5);
			builder.percentGenus(1f);
		});
		spec1.getLoreyHeightByUtilization().setCoe(0, 38.7456512f);
		spec1.setFractionGenus(0.00817133673f);

		var spec2 = VdypSpecies.build(layer, builder -> {
			builder.genus("C");
			builder.volumeGroup(20);
			builder.decayGroup(14);
			builder.breakageGroup(6);
			builder.percentGenus(7f);
		});
		spec2.getLoreyHeightByUtilization().setCoe(0, 22.8001652f);
		spec2.setFractionGenus(0.0972022042f);

		var spec3 = VdypSpecies.build(layer, builder -> {
			builder.genus("D");
			builder.volumeGroup(25);
			builder.decayGroup(19);
			builder.breakageGroup(12);
			builder.percentGenus(74f);
		});
		spec3.getLoreyHeightByUtilization().setCoe(0, 33.6889763f);
		spec3.setFractionGenus(0.695440531f);

		var spec4 = VdypSpecies.build(layer, builder -> {
			builder.genus("H");
			builder.volumeGroup(37);
			builder.decayGroup(31);
			builder.breakageGroup(17);
			builder.percentGenus(9f);
		});
		spec4.getLoreyHeightByUtilization().setCoe(0, 24.3451157f);
		spec4.setFractionGenus(0.117043354f);

		var spec5 = VdypSpecies.build(layer, builder -> {
			builder.genus("S");
			builder.volumeGroup(66);
			builder.decayGroup(54);
			builder.breakageGroup(28);
			builder.percentGenus(9f);
		});
		spec5.getLoreyHeightByUtilization().setCoe(0, 34.6888771f);
		spec5.setFractionGenus(0.082142584f);

		Map<String, VdypSpecies> specs = new HashMap<>();
		specs.put(spec1.getGenus(), spec1);
		specs.put(spec2.getGenus(), spec2);
		specs.put(spec3.getGenus(), spec3);
		specs.put(spec4.getGenus(), spec4);
		specs.put(spec5.getGenus(), spec5);

		float dq = emp.estimateQuadMeanDiameterForSpecies(
				spec1, specs, Region.COASTAL, 30.2601795f, 44.6249847f, 620.504883f, 31.6603775f
		);

		assertThat(dq, closeTo(31.7022133f));

	}

}
