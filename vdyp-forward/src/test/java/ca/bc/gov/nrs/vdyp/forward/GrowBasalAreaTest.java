package ca.bc.gov.nrs.vdyp.forward;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.vdyp.application.ProcessingException;
import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.forward.ForwardProcessingEngine.ExecutionStep;
import ca.bc.gov.nrs.vdyp.forward.model.ForwardDebugSettings;
import ca.bc.gov.nrs.vdyp.forward.model.ForwardDebugSettings.Vars;
import ca.bc.gov.nrs.vdyp.forward.model.VdypPolygon;
import ca.bc.gov.nrs.vdyp.forward.model.VdypPolygonDescription;
import ca.bc.gov.nrs.vdyp.forward.test.VdypForwardTestUtils;
import ca.bc.gov.nrs.vdyp.io.parse.common.ResourceParseException;
import ca.bc.gov.nrs.vdyp.io.parse.streaming.StreamingParser;
import ca.bc.gov.nrs.vdyp.io.parse.streaming.StreamingParserFactory;

class GrowBasalAreaTest {

	protected static final Logger logger = LoggerFactory.getLogger(GrowBasalAreaTest.class);

	protected static ForwardControlParser parser;
	protected static Map<String, Object> controlMap;

	protected static StreamingParserFactory<VdypPolygonDescription> polygonDescriptionStreamFactory;
	protected static StreamingParser<VdypPolygonDescription> polygonDescriptionStream;

	protected static ForwardDataStreamReader forwardDataStreamReader;

	@SuppressWarnings("unchecked")
	@BeforeEach
	void beforeTest() throws IOException, ResourceParseException {
		parser = new ForwardControlParser();
		controlMap = VdypForwardTestUtils.parse(parser, "VDYP.CTR");

		polygonDescriptionStreamFactory = (StreamingParserFactory<VdypPolygonDescription>) controlMap
				.get(ControlKey.FORWARD_INPUT_GROWTO.name());
		polygonDescriptionStream = polygonDescriptionStreamFactory.get();

		forwardDataStreamReader = new ForwardDataStreamReader(controlMap);
	}

	@Test
	void testStandardPath() throws ProcessingException {
		
		ForwardProcessingEngine fpe = new ForwardProcessingEngine(controlMap);
		
		// Select the first polygon - 01002 S000001 00(1970)
		VdypPolygon polygon = forwardDataStreamReader.readNextPolygon().orElseThrow();

		fpe.processPolygon(polygon, ExecutionStep.GROW.predecessor());
		
		float yabh = 54.0f;
		float hd = 35.2999992f;
		float ba = 45.3864441f;
		float growthInHd = 0.173380271f;
		
		float gba = fpe.growBasalArea(yabh, fpe.fps.fcm.getDebugSettings(), hd, ba, Optional.empty(), growthInHd);
		
		assertThat(gba, is(0.35185286f));
	}

	@Test
	void testYoungPath() throws ProcessingException {
		
		ForwardProcessingEngine fpe = new ForwardProcessingEngine(controlMap);
		
		// Select the first polygon - 01002 S000001 00(1970)
		VdypPolygon polygon = forwardDataStreamReader.readNextPolygon().orElseThrow();

		fpe.processPolygon(polygon, ExecutionStep.GROW.predecessor());
		
		float yabh = 30.0f;
		float hd = 10.0f;
		float ba = 200.0f;
		float growthInHd = 0.173380271f;
		
		float gba = fpe.growBasalArea(yabh, fpe.fps.fcm.getDebugSettings(), hd, ba, Optional.empty(), growthInHd);
		
		assertThat(gba, is(0.0f));
	}

	@Test
	void testDebugSettings2EqualsZeroPath() throws ProcessingException {
		
		ForwardProcessingEngine fpe = new ForwardProcessingEngine(controlMap);
		
		// Select the first polygon - 01002 S000001 00(1970)
		VdypPolygon polygon = forwardDataStreamReader.readNextPolygon().orElseThrow();

		fpe.processPolygon(polygon, ExecutionStep.GROW.predecessor());
		
		float yabh = 54.0f;
		float hd = 35.2999992f;
		float ba = 45.3864441f;
		float growthInHd = 0.173380271f;
		
		ForwardDebugSettings debugSettings = fpe.fps.fcm.getDebugSettings();
		debugSettings.setValue(Vars.BASAL_AREA_GROWTH_MODEL_3, 0);
		
		float gba = fpe.growBasalArea(yabh, debugSettings, hd, ba, Optional.empty(), growthInHd);
		
		assertThat(gba, is(3.7865982f));
	}
}
