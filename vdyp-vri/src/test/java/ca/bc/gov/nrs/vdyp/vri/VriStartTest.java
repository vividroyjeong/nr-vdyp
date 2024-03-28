package ca.bc.gov.nrs.vdyp.vri;

import static ca.bc.gov.nrs.vdyp.test.VdypMatchers.closeTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import ca.bc.gov.nrs.vdyp.application.StandProcessingException;
import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.common.Utils;
import ca.bc.gov.nrs.vdyp.model.BecLookup;
import ca.bc.gov.nrs.vdyp.model.Coefficients;
import ca.bc.gov.nrs.vdyp.model.LayerType;
import ca.bc.gov.nrs.vdyp.test.VdypMatchers;
import ca.bc.gov.nrs.vdyp.vri.model.VriLayer;
import ca.bc.gov.nrs.vdyp.vri.model.VriPolygon;
import ca.bc.gov.nrs.vdyp.vri.test.VriTestUtils;

class VriStartTest {

	@SuppressWarnings("resource")
	@Test
	void testEstimateBaseAreaYield() throws StandProcessingException {
		Map<String, Object> controlMap = VriTestUtils.loadControlMap();
		VriStart app = new VriStart();
		app.setControlMap(controlMap);

		var polygon = VriPolygon.build(pBuilder -> {
			pBuilder.polygonIdentifier("Test");
			pBuilder.biogeoclimaticZone("IDF");
			pBuilder.yieldFactor(1.0f);
			pBuilder.buildLayer(lBuilder -> {
				lBuilder.layerType(LayerType.PRIMARY);
				((VriLayer.Builder) lBuilder).crownClosure(57.8f);
				((VriLayer.Builder) lBuilder).baseArea(66f);
				((VriLayer.Builder) lBuilder).treesPerHectare(850f);
				((VriLayer.Builder) lBuilder).utilization(7.5f);
				((VriLayer.Builder) lBuilder).empiricalRelationshipParameterIndex(76);

				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("B"); // 3
					sBuilder.percentGenus(2.99999993f);
				});
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("C"); // 4
					sBuilder.percentGenus(30.0000012f);
				});
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("H"); // 8
					sBuilder.percentGenus(48.9000022f);
				});
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("S"); // 15
					sBuilder.percentGenus(18.1000009f);
				});
			});
		});

		var species = polygon.getLayers().get(LayerType.PRIMARY).getSpecies().values();

		var bec = Utils.expectParsedControl(controlMap, ControlKey.BEC_DEF, BecLookup.class).get("IDF").get();

		float result = app.estimateBaseAreaYield(32f, 190.300003f, Optional.empty(), false, species, bec, 76);

		assertThat(result, closeTo(62.0858421f));
	}

	@SuppressWarnings("resource")
	@Test
	void testEstimateBaseAreaYieldCoefficients() throws StandProcessingException {
		Map<String, Object> controlMap = VriTestUtils.loadControlMap();
		VriStart app = new VriStart();
		app.setControlMap(controlMap);

		var polygon = VriPolygon.build(pBuilder -> {
			pBuilder.polygonIdentifier("Test");
			pBuilder.biogeoclimaticZone("IDF");
			pBuilder.yieldFactor(1.0f);
			pBuilder.buildLayer(lBuilder -> {
				lBuilder.layerType(LayerType.PRIMARY);
				((VriLayer.Builder) lBuilder).crownClosure(57.8f);
				((VriLayer.Builder) lBuilder).baseArea(66f);
				((VriLayer.Builder) lBuilder).treesPerHectare(850f);
				((VriLayer.Builder) lBuilder).utilization(7.5f);
				((VriLayer.Builder) lBuilder).empiricalRelationshipParameterIndex(76);

				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("B"); // 3
					sBuilder.percentGenus(2.99999993f);
				});
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("C"); // 4
					sBuilder.percentGenus(30.0000012f);
				});
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("H"); // 8
					sBuilder.percentGenus(48.9000022f);
				});
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("S"); // 15
					sBuilder.percentGenus(18.1000009f);
				});
			});
		});

		var species = polygon.getLayers().get(LayerType.PRIMARY).getSpecies().values();

		var bec = Utils.expectParsedControl(controlMap, ControlKey.BEC_DEF, BecLookup.class).get("IDF").get();

		Coefficients result = app.estimateBaseAreaYieldCoefficients(species, bec);

		assertThat(
				result,
				VdypMatchers.coe(
						0, 7.29882717f, 0.934803009f, 7.22950029f, 0.478330702f, 0.00542420009f, 0f, -0.00899999961f
				)
		);
	}
}
