package ca.bc.gov.nrs.vdyp.forward;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.vdyp.application.ProcessingException;
import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.forward.model.VdypPolygonDescription;
import ca.bc.gov.nrs.vdyp.forward.test.VdypForwardTestUtils;
import ca.bc.gov.nrs.vdyp.io.parse.common.ResourceParseException;
import ca.bc.gov.nrs.vdyp.io.parse.streaming.StreamingParser;
import ca.bc.gov.nrs.vdyp.io.parse.streaming.StreamingParserFactory;
import ca.bc.gov.nrs.vdyp.model.CommonData;
import ca.bc.gov.nrs.vdyp.model.LayerType;
import ca.bc.gov.nrs.vdyp.model.SiteCurve;

class ForwardProcessingEngineTest {

	private static final Logger logger = LoggerFactory.getLogger(ForwardProcessingEngineTest.class);

	private static ForwardControlParser parser;
	private static Map<String, Object> controlMap;

	private static StreamingParserFactory<VdypPolygonDescription> polygonDescriptionStreamFactory;
	private static StreamingParser<VdypPolygonDescription> polygonDescriptionStream;

	private static ForwardDataStreamReader forwardDataStreamReader;

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
	void test() throws IOException, ResourceParseException, ProcessingException {

		ForwardProcessingEngine fpe = new ForwardProcessingEngine(controlMap);

		assertThat(fpe.getBecLookup(), notNullValue());
		assertThat(fpe.getGenusDefinitionMap(), notNullValue());
		assertThat(fpe.getSiteCurveMap(), notNullValue());

		// Fetch the next polygon to process.
		int nPolygonsProcessed = 0;
		while (polygonDescriptionStream.hasNext()) {

			var polygonDescription = polygonDescriptionStream.next();

			var polygon = forwardDataStreamReader.readNextPolygon(polygonDescription);

			fpe.processPolygon(polygon);

			nPolygonsProcessed += 1;
		}

		logger.info("{} polygons processed", nPolygonsProcessed);
	}

	@Test
	void testFindPrimarySpecies() throws IOException, ResourceParseException, ProcessingException {

		ForwardProcessingState fps = new ForwardProcessingState();

		var polygonDescription = polygonDescriptionStream.next();
		var polygon = forwardDataStreamReader.readNextPolygon(polygonDescription);

		fps.setStartingState(polygon);

		{
			PolygonProcessingState bank = fps.getBank(LayerType.PRIMARY, 0).copy();

			ForwardProcessingEngine.calculateCoverages(bank);
			ForwardProcessingEngine.determinePolygonRankings(bank, CommonData.PRIMARY_SPECIES_TO_COMBINE);
			SpeciesRankingDetails rankingDetails1 = bank.getSpeciesRankingDetails();

			assertThat(rankingDetails1.primarySpeciesIndex(), is(3));
			assertThat(rankingDetails1.secondarySpeciesIndex(), is(Optional.of(4)));
			assertThat(rankingDetails1.inventoryTypeGroup(), is(37));
		}
		{
			PolygonProcessingState bank = fps.getBank(LayerType.PRIMARY, 0).copy();

			var speciesToCombine = Arrays.asList(Arrays.asList(bank.speciesNames[3], bank.speciesNames[4]));

			ForwardProcessingEngine.calculateCoverages(bank);
			ForwardProcessingEngine.determinePolygonRankings(bank, speciesToCombine);
			SpeciesRankingDetails rankingDetails2 = bank.getSpeciesRankingDetails();

			// The test-specific speciesToCombine will combine 3 & 4 into 3 (leaving 4 at 0.0), promoting 2 to
			// secondary.
			assertThat(rankingDetails2.primarySpeciesIndex(), is(3));
			assertThat(rankingDetails2.secondarySpeciesIndex(), is(Optional.of(2)));
			assertThat(rankingDetails2.inventoryTypeGroup(), is(37));
		}
	}

	@Test
	void testCombinePercentages() {
		
		String[] speciesNames = new String[] { "AC",  "B",  "C",  "D",  "E",  "F", "PW",  "H", "PY",  "L", "PA", "AT",  "S", "MB",  "Y", "PL" };
		float[] percentages = new float[]    { 1.1f, 2.2f, 3.3f, 4.4f, 5.5f, 6.6f, 7.7f, 8.8f, 9.9f, 9.3f, 8.4f, 7.1f, 6.3f, 8.4f, 9.4f, 1.6f };

		List<String> combineGroup;
		float[] testPercentages;

		combineGroup = List.of("C", "Y");
		testPercentages = Arrays.copyOf(percentages, percentages.length);

		ForwardProcessingEngine.combinePercentages(speciesNames, combineGroup, testPercentages);

		assertThat(testPercentages[2], is(0f));
		assertThat(testPercentages[14], is(12.7f));

		combineGroup = List.of("D", "PL");
		testPercentages = Arrays.copyOf(percentages, percentages.length);

		ForwardProcessingEngine.combinePercentages(speciesNames, combineGroup, testPercentages);

		assertThat(testPercentages[3], is(6.0f));
		assertThat(testPercentages[15], is(0.0f));
	}

	@Test
	void testCombinePercentagesOneGenusNotInCombinationList() {
		
		String[] speciesNames = new String[] { "AC", "C",  "D",  "E",  "F", "PW",  "H", "PY",  "L", "PA", "AT",  "S", "MB",  "Y", "PL" };
		float[] percentages = new float[]    { 1.1f, 3.3f, 4.4f, 5.5f, 6.6f, 7.7f, 8.8f, 9.9f, 9.3f, 8.4f, 7.1f, 6.3f, 8.4f, 9.4f, 1.6f };

		List<String> combineGroup = List.of("B", "Y");
		float[] testPercentages = Arrays.copyOf(percentages, percentages.length);

		ForwardProcessingEngine.combinePercentages(speciesNames, combineGroup, testPercentages);

		assertThat(testPercentages[13], is(9.4f));
	}

	@Test
	void testCombinePercentagesBothGeneraNotInCombinationList() {
		
		String[] speciesNames = new String[] { "AC", "D",  "E",  "F", "PW",  "H", "PY",  "L", "PA", "AT",  "S", "MB",  "Y", "PL" };
		float[] percentages = new float[]    { 1.1f, 4.4f, 5.5f, 6.6f, 7.7f, 8.8f, 9.9f, 9.3f, 8.4f, 7.1f, 6.3f, 8.4f, 9.4f, 1.6f };

		List<String> combineGroup = List.of("B", "C");
		float[] testPercentages = Arrays.copyOf(percentages, percentages.length);

		ForwardProcessingEngine.combinePercentages(speciesNames, combineGroup, testPercentages);

		assertThat(testPercentages, is(percentages));
	}

	@Test
	void testCombinePercentagesBadCombinationList() {
		
		String[] speciesNames = new String[] { "AC", "D",  "E",  "F", "PW",  "H", "PY",  "L", "PA", "AT",  "S", "MB",  "Y", "PL" };
		float[] percentages = new float[]    { 1.1f, 4.4f, 5.5f, 6.6f, 7.7f, 8.8f, 9.9f, 9.3f, 8.4f, 7.1f, 6.3f, 8.4f, 9.4f, 1.6f };

		List<String> combineGroup = List.of("B", "C", "D");

		try {
			ForwardProcessingEngine.combinePercentages(speciesNames, combineGroup, percentages);
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	void testCombinePercentagesBadArrays() {
		
		String[] speciesNames = new String[] { "D",  "E",  "F", "PW",  "H", "PY",  "L", "PA", "AT",  "S", "MB",  "Y", "PL" };
		float[] percentages = new float[] { 1.1f, 4.4f, 5.5f, 6.6f, 7.7f, 8.8f, 9.9f, 9.3f, 8.4f, 7.1f, 6.3f, 8.4f, 9.4f, 1.6f };

		List<String> combineGroup = List.of("B", "C");

		try {
			ForwardProcessingEngine.combinePercentages(speciesNames, combineGroup, percentages);
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	void testFindInventoryTypeGroup() throws ProcessingException {
		try {
			ForwardProcessingEngine.findInventoryTypeGroup("A", Optional.empty(), 80.0f);
			fail();
		} catch (ProcessingException e) {
			// expected
		}

		assertThrows(
				IllegalArgumentException.class,
				() -> ForwardProcessingEngine.findInventoryTypeGroup("AC", Optional.of("AC"), 0.0f)
		);

		assertThat(ForwardProcessingEngine.findInventoryTypeGroup("AC", Optional.empty(), 80.0f), is(36));

		int[] expectedResults = new int[] { 
					  /*                  Secondary                         */
				      /* AC  B  C  D  E  F PW  H PY  L PA AT  S MB  Y PL -- */  
				/* AC */    35,35,36,36,35,35,35,35,35,35,36,35,36,35,35,35, //
				/*  B */ 20,   19,20,20,20,20,19,20,20,20,20,20,20,19,20,20, //
				/*  C */ 10,11,   10,10,10,10,11,10,10,10,10,11,10,10,10,10, //
				/*  D */ 38,37,37,   38,37,37,37,37,37,37,38,37,38,37,37,37, //
				/*  E */ 40,40,40,40,   40,40,40,40,40,40,40,40,40,40,40,40, //
				/*  F */  8, 3, 2, 8, 8,    7, 3, 6, 7, 5, 8, 4, 8, 2, 5, 8, //
				/* PW */ 27,27,27,27,27,27,   27,27,27,27,27,27,27,27,27,27, //
				/*  H */ 13,15,14,13,13,13,13,   13,13,13,13,16,13,14,13,13, //
				/* PY */ 32,32,32,32,32,32,32,32,   32,32,32,32,32,32,32,32, //
				/*  L */ 34,34,34,34,34,33,34,34,34,   34,34,34,34,34,34,34, //
				/* PA */ 31,30,30,31,31,29,29,30,29,29,   31,30,31,30,28,30, //
				/* AT */ 42,41,41,42,42,41,41,41,41,41,41,   41,42,41,41,41, //
				/*  S */ 26,24,23,26,26,22,22,23,22,22,22,26,   26,23,25,22, //
				/* MB */ 39,39,39,39,39,39,39,39,39,39,39,39,39,   39,39,39, //
				/*  Y */ 10,11,10,10,10,10,10,11,10,10,10,10,11,10,   10,10, //
				/* PL */ 31,30,30,31,31,29,29,30,29,29,28,31,30,31,30,   30 };

		int currentAnswerIndex = 0;

		for (String primaryGenus : CommonData.ITG_PURE.keySet()) {
			for (Optional<String> secondaryGenus : CommonData.ITG_PURE.keySet().stream()
					.filter(k -> !k.equals(primaryGenus)).map(k -> Optional.of(k)).collect(Collectors.toList())) {
				int itg = ForwardProcessingEngine.findInventoryTypeGroup(primaryGenus, secondaryGenus, 50.0f);
				assertThat(itg, is(expectedResults[currentAnswerIndex++]));
			}
			int itg = ForwardProcessingEngine.findInventoryTypeGroup(primaryGenus, Optional.empty(), 50.0f);
			assertThat(itg, is(expectedResults[currentAnswerIndex++]));
		}
	}
	
	@Test
	void testEstimateMissingYearsToBreastHeightValues() throws ProcessingException, IOException, ResourceParseException {

		var polygonDescription = polygonDescriptionStream.next();
		var polygon = forwardDataStreamReader.readNextPolygon(polygonDescription);

		PolygonProcessingState bank = new PolygonProcessingState(polygon.getPrimaryLayer(), polygon.getBiogeoclimaticZone());

		@SuppressWarnings("unchecked")
		var siteCurveMap = (Map<String, SiteCurve>) controlMap.get(ControlKey.SITE_CURVE_NUMBERS.name());

		ForwardProcessingEngine.executeForwardAlgorithm(bank, siteCurveMap, 3);
		
		ForwardProcessingEngine.estimateMissingSiteIndices(bank);
	}
}
