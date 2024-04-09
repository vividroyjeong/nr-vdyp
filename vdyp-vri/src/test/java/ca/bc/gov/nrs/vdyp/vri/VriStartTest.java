package ca.bc.gov.nrs.vdyp.vri;

import static ca.bc.gov.nrs.vdyp.test.VdypMatchers.closeTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import ca.bc.gov.nrs.vdyp.application.StandProcessingException;
import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.common.Utils;
import ca.bc.gov.nrs.vdyp.model.BecDefinition;
import ca.bc.gov.nrs.vdyp.model.BecLookup;
import ca.bc.gov.nrs.vdyp.model.Coefficients;
import ca.bc.gov.nrs.vdyp.model.LayerType;
import ca.bc.gov.nrs.vdyp.model.MatrixMap2Impl;
import ca.bc.gov.nrs.vdyp.model.PolygonMode;
import ca.bc.gov.nrs.vdyp.model.Region;
import ca.bc.gov.nrs.vdyp.test.MockFileResolver;
import ca.bc.gov.nrs.vdyp.test.VdypMatchers;
import ca.bc.gov.nrs.vdyp.vri.model.VriLayer;
import ca.bc.gov.nrs.vdyp.vri.model.VriPolygon;
import ca.bc.gov.nrs.vdyp.vri.model.VriSpecies;
import ca.bc.gov.nrs.vdyp.vri.test.VriTestUtils;

class VriStartTest {

	Map<String, Object> controlMap = new HashMap<>();

	private MockFileResolver dummyInput() {
		controlMap.put(ControlKey.VRI_OUTPUT_VDYP_POLYGON.name(), "DUMMY1");
		controlMap.put(ControlKey.VRI_OUTPUT_VDYP_LAYER_BY_SPECIES.name(), "DUMMY2");
		controlMap.put(ControlKey.VRI_OUTPUT_VDYP_LAYER_BY_SP0_BY_UTIL.name(), "DUMMY3");

		MockFileResolver resolver = new MockFileResolver("Test");
		resolver.addStream("DUMMY1", (OutputStream) new ByteArrayOutputStream());
		resolver.addStream("DUMMY2", (OutputStream) new ByteArrayOutputStream());
		resolver.addStream("DUMMY3", (OutputStream) new ByteArrayOutputStream());
		return resolver;
	}

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

	@Test
	void testFindDefaultModeLowBA() throws Exception {
		var app = new VriStart();

		MockFileResolver resolver = dummyInput();

		controlMap.put(ControlKey.MINIMA.name(), Utils.constMap(map -> {
			map.put(VriControlParser.MINIMUM_BASE_AREA, 0f);
			map.put(VriControlParser.MINIMUM_HEIGHT, 6f);
			map.put(VriControlParser.MINIMUM_PREDICTED_BASE_AREA, 14f); // Set this high for test
		}));

		var baYieldMap = new MatrixMap2Impl<>(
				List.of("IDF"), List.of("B"),
				(k1, k2) -> new Coefficients(
						new float[] { -2.9907f + 11.2376f, 2.4562f - 3.2149f, 7.2295f + 0.0000f, 0.5074f + 0.3763f,
								-0.0150f + 0.030f, 0.0120f + -0.0364f, -0.0090f + 0.0000f },
						0
				)
		);
		controlMap.put(ControlKey.BA_YIELD.name(), baYieldMap);

		controlMap.put(ControlKey.BA_DQ_UPPER_BOUNDS.name(), Utils.constMap(map -> {
			map.put(76, new Coefficients(new float[] { 89.69f, 53.60f }, 1));
		}));

		app.init(resolver, controlMap);

		Optional<Float> ageTotal = Optional.of(200f);
		Optional<Float> yearsToBreastHeight = Optional.of(191f);
		Optional<Float> height = Optional.of(10f);
		Optional<Float> baseArea = Optional.of(30f);
		Optional<Float> treesPerHectare = Optional.of(300f);
		Optional<Float> percentForest = Optional.of(90f);

		Collection<VriSpecies> species = List.of(VriSpecies.build(builder -> {
			builder.polygonIdentifier("Test");
			builder.layerType(LayerType.PRIMARY);
			builder.genus("B");
			builder.percentGenus(100f);
		}));
		var bec = new BecDefinition("IDF", Region.INTERIOR, "Interior Douglas Fir");

		var result = app.findDefaultPolygonMode(
				ageTotal, yearsToBreastHeight, height, baseArea, treesPerHectare, percentForest, species, bec,
				Optional.of(76)
		);

		assertThat(result, is(PolygonMode.YOUNG));

		app.close();
	}

	@Test
	void testFindDefaultModeLowHeight() throws Exception {
		var app = new VriStart();

		MockFileResolver resolver = dummyInput();

		controlMap.put(ControlKey.MINIMA.name(), Utils.constMap(map -> {
			map.put(VriControlParser.MINIMUM_BASE_AREA, 0f);
			map.put(VriControlParser.MINIMUM_HEIGHT, 6f);
			map.put(VriControlParser.MINIMUM_PREDICTED_BASE_AREA, 2f);
		}));

		var baYieldMap = new MatrixMap2Impl<>(
				List.of("IDF"), List.of("B"),
				(k1, k2) -> new Coefficients(
						new float[] { -2.9907f + 11.2376f, 2.4562f - 3.2149f, 7.2295f + 0.0000f, 0.5074f + 0.3763f,
								-0.0150f + 0.030f, 0.0120f + -0.0364f, -0.0090f + 0.0000f },
						0
				)
		);
		controlMap.put(ControlKey.BA_YIELD.name(), baYieldMap);

		controlMap.put(ControlKey.BA_DQ_UPPER_BOUNDS.name(), Utils.constMap(map -> {
			map.put(76, new Coefficients(new float[] { 89.69f, 53.60f }, 1));
		}));

		app.init(resolver, controlMap);

		Optional<Float> ageTotal = Optional.of(200f);
		Optional<Float> yearsToBreastHeight = Optional.of(189f);
		Optional<Float> height = Optional.of(1f); // Set this low for test.
		Optional<Float> baseArea = Optional.of(30f);
		Optional<Float> treesPerHectare = Optional.of(300f);
		Optional<Float> percentForest = Optional.of(90f);

		Collection<VriSpecies> species = List.of(VriSpecies.build(builder -> {
			builder.polygonIdentifier("Test");
			builder.layerType(LayerType.PRIMARY);
			builder.genus("B");
			builder.percentGenus(100f);
		}));
		var bec = new BecDefinition("IDF", Region.INTERIOR, "Interior Douglas Fir");

		var result = app.findDefaultPolygonMode(
				ageTotal, yearsToBreastHeight, height, baseArea, treesPerHectare, percentForest, species, bec,
				Optional.of(76)
		);

		assertThat(result, is(PolygonMode.YOUNG));

		app.close();
	}

	@Test
	void testFindDefaultModeNoBA() throws Exception {
		var app = new VriStart();

		MockFileResolver resolver = dummyInput();

		controlMap.put(ControlKey.MINIMA.name(), Utils.constMap(map -> {
			map.put(VriControlParser.MINIMUM_BASE_AREA, 0f);
			map.put(VriControlParser.MINIMUM_HEIGHT, 6f);
			map.put(VriControlParser.MINIMUM_PREDICTED_BASE_AREA, 2f);
		}));

		var baYieldMap = new MatrixMap2Impl<>(
				List.of("IDF"), List.of("B"),
				(k1, k2) -> new Coefficients(
						new float[] { -2.9907f + 11.2376f, 2.4562f - 3.2149f, 7.2295f + 0.0000f, 0.5074f + 0.3763f,
								-0.0150f + 0.030f, 0.0120f + -0.0364f, -0.0090f + 0.0000f },
						0
				)
		);
		controlMap.put(ControlKey.BA_YIELD.name(), baYieldMap);

		controlMap.put(ControlKey.BA_DQ_UPPER_BOUNDS.name(), Utils.constMap(map -> {
			map.put(76, new Coefficients(new float[] { 89.69f, 53.60f }, 1));
		}));

		app.init(resolver, controlMap);

		Optional<Float> ageTotal = Optional.of(200f);
		Optional<Float> yearsToBreastHeight = Optional.of(189f);
		Optional<Float> height = Optional.of(20f);
		Optional<Float> baseArea = Optional.empty(); // Null this for the test
		Optional<Float> treesPerHectare = Optional.of(300f);
		Optional<Float> percentForest = Optional.of(90f);

		Collection<VriSpecies> species = List.of(VriSpecies.build(builder -> {
			builder.polygonIdentifier("Test");
			builder.layerType(LayerType.PRIMARY);
			builder.genus("B");
			builder.percentGenus(100f);
		}));
		var bec = new BecDefinition("IDF", Region.INTERIOR, "Interior Douglas Fir");

		var result = app.findDefaultPolygonMode(
				ageTotal, yearsToBreastHeight, height, baseArea, treesPerHectare, percentForest, species, bec,
				Optional.of(76)
		);

		assertThat(result, is(PolygonMode.YOUNG));

		app.close();
	}

	@Test
	void testFindDefaultModeNoTPH() throws Exception {
		var app = new VriStart();

		MockFileResolver resolver = dummyInput();

		controlMap.put(ControlKey.MINIMA.name(), Utils.constMap(map -> {
			map.put(VriControlParser.MINIMUM_BASE_AREA, 0f);
			map.put(VriControlParser.MINIMUM_HEIGHT, 6f);
			map.put(VriControlParser.MINIMUM_PREDICTED_BASE_AREA, 2f);
		}));

		var baYieldMap = new MatrixMap2Impl<>(
				List.of("IDF"), List.of("B"),
				(k1, k2) -> new Coefficients(
						new float[] { -2.9907f + 11.2376f, 2.4562f - 3.2149f, 7.2295f + 0.0000f, 0.5074f + 0.3763f,
								-0.0150f + 0.030f, 0.0120f + -0.0364f, -0.0090f + 0.0000f },
						0
				)
		);
		controlMap.put(ControlKey.BA_YIELD.name(), baYieldMap);

		controlMap.put(ControlKey.BA_DQ_UPPER_BOUNDS.name(), Utils.constMap(map -> {
			map.put(76, new Coefficients(new float[] { 89.69f, 53.60f }, 1));
		}));

		app.init(resolver, controlMap);

		Optional<Float> ageTotal = Optional.of(200f);
		Optional<Float> yearsToBreastHeight = Optional.of(189f);
		Optional<Float> height = Optional.of(20f);
		Optional<Float> baseArea = Optional.of(30f);
		Optional<Float> treesPerHectare = Optional.empty(); // Null this for the test
		Optional<Float> percentForest = Optional.of(90f);

		Collection<VriSpecies> species = List.of(VriSpecies.build(builder -> {
			builder.polygonIdentifier("Test");
			builder.layerType(LayerType.PRIMARY);
			builder.genus("B");
			builder.percentGenus(100f);
		}));
		var bec = new BecDefinition("IDF", Region.INTERIOR, "Interior Douglas Fir");

		var result = app.findDefaultPolygonMode(
				ageTotal, yearsToBreastHeight, height, baseArea, treesPerHectare, percentForest, species, bec,
				Optional.of(76)
		);

		assertThat(result, is(PolygonMode.YOUNG));

		app.close();
	}

	@Test
	void testFindDefaultLowRation() throws Exception {
		var app = new VriStart();

		MockFileResolver resolver = dummyInput();

		controlMap.put(ControlKey.MINIMA.name(), Utils.constMap(map -> {
			map.put(VriControlParser.MINIMUM_BASE_AREA, 13f);// Set this high
			map.put(VriControlParser.MINIMUM_HEIGHT, 0f);
			map.put(VriControlParser.MINIMUM_PREDICTED_BASE_AREA, 2f);
		}));

		var baYieldMap = new MatrixMap2Impl<>(
				List.of("IDF"), List.of("B"),
				(k1, k2) -> new Coefficients(
						new float[] { -2.9907f + 11.2376f, 2.4562f - 3.2149f, 7.2295f + 0.0000f, 0.5074f + 0.3763f,
								-0.0150f + 0.030f, 0.0120f + -0.0364f, -0.0090f + 0.0000f },
						0
				)
		);
		controlMap.put(ControlKey.BA_YIELD.name(), baYieldMap);

		controlMap.put(ControlKey.BA_DQ_UPPER_BOUNDS.name(), Utils.constMap(map -> {
			map.put(76, new Coefficients(new float[] { 89.69f, 53.60f }, 1));
		}));

		app.init(resolver, controlMap);

		Optional<Float> ageTotal = Optional.of(200f);
		Optional<Float> yearsToBreastHeight = Optional.of(189f);
		Optional<Float> height = Optional.of(20f);
		Optional<Float> baseArea = Optional.of(10f); // Set this low
		Optional<Float> treesPerHectare = Optional.of(300f);
		Optional<Float> percentForest = Optional.of(95f); // Set this high

		Collection<VriSpecies> species = List.of(VriSpecies.build(builder -> {
			builder.polygonIdentifier("Test");
			builder.layerType(LayerType.PRIMARY);
			builder.genus("B");
			builder.percentGenus(100f);
		}));
		var bec = new BecDefinition("IDF", Region.INTERIOR, "Interior Douglas Fir");

		var result = app.findDefaultPolygonMode(
				ageTotal, yearsToBreastHeight, height, baseArea, treesPerHectare, percentForest, species, bec,
				Optional.of(76)
		);

		assertThat(result, is(PolygonMode.YOUNG));

		app.close();
	}

	@Test
	void testFindDefaultStart() throws Exception {
		var app = new VriStart();

		MockFileResolver resolver = dummyInput();

		controlMap.put(ControlKey.MINIMA.name(), Utils.constMap(map -> {
			map.put(VriControlParser.MINIMUM_BASE_AREA, 0f);
			map.put(VriControlParser.MINIMUM_HEIGHT, 0f);
			map.put(VriControlParser.MINIMUM_PREDICTED_BASE_AREA, 2f);
		}));

		var baYieldMap = new MatrixMap2Impl<>(
				List.of("IDF"), List.of("B"),
				(k1, k2) -> new Coefficients(
						new float[] { -2.9907f + 11.2376f, 2.4562f - 3.2149f, 7.2295f + 0.0000f, 0.5074f + 0.3763f,
								-0.0150f + 0.030f, 0.0120f + -0.0364f, -0.0090f + 0.0000f },
						0
				)
		);
		controlMap.put(ControlKey.BA_YIELD.name(), baYieldMap);

		controlMap.put(ControlKey.BA_DQ_UPPER_BOUNDS.name(), Utils.constMap(map -> {
			map.put(76, new Coefficients(new float[] { 89.69f, 53.60f }, 1));
		}));

		app.init(resolver, controlMap);

		Optional<Float> ageTotal = Optional.of(200f);
		Optional<Float> yearsToBreastHeight = Optional.of(189f);
		Optional<Float> height = Optional.of(20f);
		Optional<Float> baseArea = Optional.of(30f);
		Optional<Float> treesPerHectare = Optional.of(300f);
		Optional<Float> percentForest = Optional.of(85f);

		Collection<VriSpecies> species = List.of(VriSpecies.build(builder -> {
			builder.polygonIdentifier("Test");
			builder.layerType(LayerType.PRIMARY);
			builder.genus("B");
			builder.percentGenus(100f);
		}));
		var bec = new BecDefinition("IDF", Region.INTERIOR, "Interior Douglas Fir");

		var result = app.findDefaultPolygonMode(
				ageTotal, yearsToBreastHeight, height, baseArea, treesPerHectare, percentForest, species, bec,
				Optional.of(76)
		);

		assertThat(result, is(PolygonMode.START));

		app.close();
	}

}
