package ca.bc.gov.nrs.vdyp.forward;

import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.vdyp.application.ProcessingException;
import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.forward.ForwardProcessingEngine.ExecutionStep;
import ca.bc.gov.nrs.vdyp.forward.model.ForwardDebugSettings.Vars;
import ca.bc.gov.nrs.vdyp.forward.test.VdypForwardTestUtils;
import ca.bc.gov.nrs.vdyp.io.parse.common.ResourceParseException;
import ca.bc.gov.nrs.vdyp.io.parse.streaming.StreamingParser;
import ca.bc.gov.nrs.vdyp.io.parse.streaming.StreamingParserFactory;
import ca.bc.gov.nrs.vdyp.model.PolygonIdentifier;
import ca.bc.gov.nrs.vdyp.model.UtilizationClass;
import ca.bc.gov.nrs.vdyp.model.VdypPolygon;

class Grow5SpeciesBaDqTphTest {

	protected static final Logger logger = LoggerFactory.getLogger(Grow5SpeciesBaDqTphTest.class);

	protected static ForwardControlParser parser;
	protected static Map<String, Object> controlMap;

	protected static StreamingParserFactory<PolygonIdentifier> polygonDescriptionStreamFactory;
	protected static StreamingParser<PolygonIdentifier> polygonDescriptionStream;

	protected static ForwardDataStreamReader forwardDataStreamReader;

	@SuppressWarnings("unchecked")
	@BeforeEach
	void beforeTest() throws IOException, ResourceParseException, ProcessingException {
		parser = new ForwardControlParser();
		controlMap = VdypForwardTestUtils.parse(parser, "VDYP.CTR");

		polygonDescriptionStreamFactory = (StreamingParserFactory<PolygonIdentifier>) controlMap
				.get(ControlKey.FORWARD_INPUT_GROWTO.name());
		polygonDescriptionStream = polygonDescriptionStreamFactory.get();

		forwardDataStreamReader = new ForwardDataStreamReader(controlMap);
	}

	@Test
	void testStandardPath() throws ProcessingException {

		ForwardProcessingEngine fpe = new ForwardProcessingEngine(controlMap);

		// Select the first polygon - 01002 S000001 00(1970)
		VdypPolygon polygon = forwardDataStreamReader.readNextPolygon().orElseThrow();

		fpe.processPolygon(polygon, ExecutionStep.GROW_5A_LH_EST);
		LayerProcessingState lps = fpe.fps.getLayerProcessingState();

		float baStart = 45.3864441f;
		float baDelta = 0.351852179f;
		float dqStart = 30.9988747f;
		float dqDelta = 0.309478492f;
		float tphStart = 601.373718f;
		float[] lhAtStart = new float[] { 30.9723663f, 36.7552986f, 22.9584007f, 33.7439995f, 22.7703991f,
				32.0125008f };

		fpe.growUsingPartialSpeciesDynamics(baStart, baDelta, dqStart, dqDelta, tphStart, lhAtStart);

		// Results are stored in bank.basalAreas[1..nSpecies]

		assertThat(
				slice(lps.getBank().basalAreas, UtilizationClass.ALL),
				// Results from VDYP7:
				// Matchers.arrayContaining(45.7382965f, 0.410145015f, 5.13646269f, 29.8279209f, 5.91424417f,
				// 4.44952154f)
				Matchers.arrayContaining(45.38645f, 0.41014494f, 5.1364512f, 29.827925f, 5.914243f, 4.449531f)
		);
		assertThat(
				slice(lps.getBank().treesPerHectare, UtilizationClass.ALL),
				// Results from VDYP7:
				// Matchers.arrayContaining(594.113831f, 5.14728308f, 84.0494843f, 286.714783f, 167.523376f,
				// 50.6789017f)
				Matchers.arrayContaining(601.3333f, 5.150639f, 83.26706f, 287.04187f, 167.69823f, 50.955967f)
		);
		assertThat(
				slice(lps.getBank().quadMeanDiameters, UtilizationClass.ALL),
				// Results from VDYP7:
				// Matchers.arrayContaining(31.3083534f, 31.8518562f, 27.8945656f, 36.3949814f, 21.201519f, 33.4347534f)
				Matchers.arrayContaining(30.999918f, 31.841476f, 28.025286f, 36.374245f, 21.190462f, 33.34377f)
		);
	}

	private Float[] slice(float[][] perSpeciesUcData, UtilizationClass uc) {
		Float[] result = new Float[perSpeciesUcData.length];
		for (int i = 0; i < perSpeciesUcData.length; i++) {
			result[i] = perSpeciesUcData[i][uc.ordinal()];
		}
		return result;
	}

	@Test
	void testGrowUsingNoSpeciesDynamics() throws ProcessingException {

		ForwardProcessingEngine fpe = new ForwardProcessingEngine(controlMap);

		// Select the first polygon - 01002 S000001 00(1970)
		VdypPolygon polygon = forwardDataStreamReader.readNextPolygon().orElseThrow();

		fpe.processPolygon(polygon, ExecutionStep.GROW_4_LAYER_BA_AND_DQTPH_EST);
		LayerProcessingState lps = fpe.fps.getLayerProcessingState();

		float baChangeRate = 0.00775236264f;
		float tphChangeRate = 0.987927794f;

		fpe.fps.fcm.getDebugSettings().setValue(Vars.SPECIES_DYNAMICS_1, 1);

		fpe.growUsingNoSpeciesDynamics(baChangeRate, tphChangeRate);

		// Results are stored in bank.basalAreas[1..nSpecies]

		assertThat(
				slice(lps.getBank().basalAreas, UtilizationClass.ALL),
				// Results from VDYP7:
				// Matchers.arrayContaining(45.7382965f, 0.410145015f, 5.13646269f, 29.8279209f, 5.91424417f,
				// 4.44952154f)
				Matchers.arrayContaining(45.38645f, 0.410145f, 5.1364527f, 29.827932f, 5.914244f, 4.449532f)
		);
		assertThat(
				slice(lps.getBank().treesPerHectare, UtilizationClass.ALL),
				// Results from VDYP7:
				// Matchers.arrayContaining(594.113831f, 5.15917826f, 83.2853012f, 287.107758f, 167.558533f,
				// 51.0030174f)
				Matchers.arrayContaining(601.3333f, 5.149199f, 83.2853f, 287.09778f, 167.54855f, 50.99304f)
		);
		assertThat(
				slice(lps.getBank().quadMeanDiameters, UtilizationClass.ALL),
				// Results from VDYP7:
				// Matchers.arrayContaining(31.3083534f, 31.8151169f, 28.0222473f, 36.3700676f, 21.199295f, 33.3283463f)
				Matchers.arrayContaining(30.999918f, 31.84593f, 28.022219f, 36.370705f, 21.199926f, 33.33165f)
		);
	}

	@Test
	void testGrowUsingFullSpeciesDynamics() throws ProcessingException {

		ForwardProcessingEngine fpe = new ForwardProcessingEngine(controlMap);

		// Select the first polygon - 01002 S000001 00(1970)
		VdypPolygon polygon = forwardDataStreamReader.readNextPolygon().orElseThrow();

		fpe.processPolygon(polygon, ExecutionStep.GROW_4_LAYER_BA_AND_DQTPH_EST);
		LayerProcessingState lps = fpe.fps.getLayerProcessingState();

		float baStart = 45.3864441f;
		float baDelta = 0.351852179f;
		float dqStart = 30.9988747f;
		float dqDelta = 0.309478492f;
		float tphStart = 601.373718f;
		float lhStart = 30.9723663f;

		fpe.fps.fcm.getDebugSettings().setValue(Vars.SPECIES_DYNAMICS_1, 0);

		fpe.growUsingFullSpeciesDynamics(baStart, baDelta, dqStart, dqDelta, tphStart, lhStart);

		// Results are stored in bank.basalAreas[1..nSpecies]

		assertThat(
				slice(lps.getBank().basalAreas, UtilizationClass.ALL),
				// Results from VDYP7:
				// Matchers.arrayContaining(45.7382965f, 0.40808472f, 5.28018427f, 29.444725f, 6.11862803f, 4.48667192f)
				Matchers.arrayContaining(45.38645f, 0.40808356f, 5.280174f, 29.444735f, 6.1186314f, 4.486681f)
		);
		assertThat(
				slice(lps.getBank().treesPerHectare, UtilizationClass.ALL),
				// Results from VDYP7:
				// Matchers.arrayContaining(594.113831f, 5.07865763f, 82.9379883f, 288.292511f, 167.419479f,
				// 50.3814774f)
				Matchers.arrayContaining(601.3333f, 5.151279f, 84.45613f, 292.43683f, 171.47192f, 51.14954f)
		);
		assertThat(
				slice(lps.getBank().quadMeanDiameters, UtilizationClass.ALL),
				// Results from VDYP7:
				// Matchers.arrayContaining(31.3083534f, 31.985693f, 28.4710083f, 36.0613632f, 21.5714378f, 33.6729965f)
				Matchers.arrayContaining(30.999918f, 31.759386f, 28.21393f, 35.80493f, 21.315018f, 33.41926f)
		);
	}
}
