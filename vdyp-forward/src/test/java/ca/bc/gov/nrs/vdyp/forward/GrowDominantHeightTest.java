package ca.bc.gov.nrs.vdyp.forward;

//import static ca.bc.gov.nrs.vdyp.test.VdypMatchers.coe;
//import static ca.bc.gov.nrs.vdyp.test.VdypMatchers.controlMapHasEntry;
//import static ca.bc.gov.nrs.vdyp.test.VdypMatchers.hasBec;
//import static ca.bc.gov.nrs.vdyp.test.VdypMatchers.mmEmpty;
//import static ca.bc.gov.nrs.vdyp.test.VdypMatchers.mmHasEntry;
//import static ca.bc.gov.nrs.vdyp.test.VdypMatchers.present;
import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.allOf;
//import static org.hamcrest.Matchers.contains;
//import static org.hamcrest.Matchers.hasEntry;
//import static org.hamcrest.Matchers.hasItem;
//import static org.hamcrest.Matchers.hasKey;
//import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
//import static org.hamcrest.Matchers.isA;
//import static org.hamcrest.Matchers.not;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.vdyp.application.ProcessingException;
import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.common.Utils;
import ca.bc.gov.nrs.vdyp.forward.model.VdypPolygonDescription;
import ca.bc.gov.nrs.vdyp.forward.test.VdypForwardTestUtils;
import ca.bc.gov.nrs.vdyp.io.parse.common.ResourceParseException;
import ca.bc.gov.nrs.vdyp.io.parse.streaming.StreamingParser;
import ca.bc.gov.nrs.vdyp.io.parse.streaming.StreamingParserFactory;
import ca.bc.gov.nrs.vdyp.model.Region;
import ca.bc.gov.nrs.vdyp.model.SiteCurveAgeMaximum;

class GrowDominantHeightTest {

	protected static final Logger logger = LoggerFactory.getLogger(GrowDominantHeightTest.class);

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
	void test1() throws ProcessingException {
		
		var polygon = forwardDataStreamReader.readNextPolygon().orElseThrow();

		float hd = 35.2999992f;
		int sc = 13;
		float si = 35;
		float ytbh = 1.0f;
		
		Map<Integer, SiteCurveAgeMaximum> map 
			= Utils.<Map<Integer, SiteCurveAgeMaximum>>optSafe(controlMap.get(ControlKey.SITE_CURVE_AGE_MAX.name()))
				.orElseThrow(() -> new IllegalStateException());
		SiteCurveAgeMaximum maximums = map.get(sc); 
	
		Region region = polygon.getBiogeoclimaticZone().getRegion();
		float gdh = ForwardProcessingEngine.growDominantHeight(maximums, region, hd, sc, si, ytbh);
		
		assertThat(gdh, is(0.173380271f));
	}
}
