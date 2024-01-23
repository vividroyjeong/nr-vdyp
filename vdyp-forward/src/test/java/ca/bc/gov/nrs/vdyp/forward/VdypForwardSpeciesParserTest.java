package ca.bc.gov.nrs.vdyp.forward;

import static ca.bc.gov.nrs.vdyp.test.VdypMatchers.assertEmpty;
import static ca.bc.gov.nrs.vdyp.test.VdypMatchers.assertNext;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import ca.bc.gov.nrs.vdyp.forward.model.VdypLayerSpecies;
import ca.bc.gov.nrs.vdyp.io.parse.StreamingParser;
import ca.bc.gov.nrs.vdyp.io.parse.StreamingParserFactory;
import ca.bc.gov.nrs.vdyp.model.LayerType;
import ca.bc.gov.nrs.vdyp.test.TestUtils;

public class VdypForwardSpeciesParserTest {

	@Test
	public void testParseEmpty() throws Exception {

		var parser = new VdypSpeciesParser();

		Map<String, Object> controlMap = new HashMap<>();

		controlMap.put(VdypSpeciesParser.CONTROL_KEY, "test.dat");
		TestUtils.populateControlMapBecReal(controlMap);

		var fileResolver = TestUtils.fileResolver("test.dat", TestUtils.makeStream(/* empty */));

		parser.modify(controlMap, fileResolver);

		var parserFactory = controlMap.get(VdypSpeciesParser.CONTROL_KEY);

		assertThat(parserFactory, instanceOf(StreamingParserFactory.class));

		@SuppressWarnings("unchecked")
		var stream = ((StreamingParserFactory<VdypLayerSpecies>) parserFactory).get();

		assertThat(stream, instanceOf(StreamingParser.class));

		assertEmpty(stream);
	}

	@Test
	public void testParseOneGenus() throws Exception {

		var parser = new VdypSpeciesParser();

		Map<String, Object> controlMap = new HashMap<>();

		controlMap.put(VdypSpeciesParser.CONTROL_KEY, "test.dat");
		TestUtils.populateControlMapGenusReal(controlMap);

		var fileResolver = TestUtils.fileResolver(
				"test.dat",
				TestUtils.makeStream(
						"01002 S000001 00     1970 P 15 S  S  100.0     0.0     0.0     0.0 -9.00 -9.00  -9.0  -9.0  -9.0 0 -9",
						"01002 S000001 00     1970"
				)
		);

		parser.modify(controlMap, fileResolver);

		var parserFactory = controlMap.get(VdypSpeciesParser.CONTROL_KEY);

		assertThat(parserFactory, instanceOf(StreamingParserFactory.class));

		@SuppressWarnings("unchecked")
		var stream = ((StreamingParserFactory<Collection<VdypLayerSpecies>>) parserFactory).get();

		assertThat(stream, instanceOf(StreamingParser.class));

		var genera = assertNext(stream);

		assertThat(
				genera,
				hasItem(
						allOf(
								hasProperty("polygonId", is("01002 S000001 00     1970")),
								hasProperty("layerType", is(LayerType.PRIMARY)), hasProperty("genusIndex", is(15)),
								hasProperty("genus", is(Optional.of("S"))),
								hasProperty(
										"speciesDistributions",
										hasProperty(
												"speciesDistributionMap",
												hasEntry(
														is("S"),
														allOf(
																hasProperty("species", is("S")),
																hasProperty("percentage", is(100.0f))
														)
												)
										)
								), hasProperty("siteIndex", is(-9.0f)), hasProperty("dominantHeight", is(-9.0f)),
								hasProperty("ageTotal", is(-9.0f)), hasProperty("ageAtBreastHeight", is(-9.0f)),
								hasProperty("yearsToBreastHeight", is(-9.0f)),
								hasProperty("isPrimary", is(Optional.of(false))),
								hasProperty("siteCurveNumber", is(Optional.of(-9)))
						)
				)
		);

		assertEmpty(stream);
	}

	@Test
	public void testParseTwoPairsOfGenera() throws Exception {

		var parser = new VdypSpeciesParser();

		Map<String, Object> controlMap = new HashMap<>();

		controlMap.put(VdypSpeciesParser.CONTROL_KEY, "test.dat");
		TestUtils.populateControlMapGenusReal(controlMap);

		var fileResolver = TestUtils.fileResolver(
				"test.dat",
				TestUtils.makeStream(
						"01002 S000002 00     1970 P 15 S  S  100.0     0.0     0.0     0.0 -9.00 -9.00  -9.0  -9.0  -9.0 0 -9",
						"01002 S000002 00     1970 V  3 B  B   50.0 S  50.0     0.0     0.0 -9.00 -9.00  -9.0  -9.0  -9.0",
						"01002 S000002 00     1970",
						"01002 S000003 00     1970 P 15 S  S  100.0     0.0     0.0     0.0 -9.00 -9.00  -9.0  -9.0  -9.0 0 -9",
						"01002 S000003 00     1970 V  3 B  B   50.0 S  50.0     0.0     0.0 -9.00 -9.00  -9.0  -9.0  -9.0",
						"01002 S000003 00     1970"
				)
		);

		parser.modify(controlMap, fileResolver);

		var parserFactory = controlMap.get(VdypSpeciesParser.CONTROL_KEY);

		assertThat(parserFactory, instanceOf(StreamingParserFactory.class));

		@SuppressWarnings("unchecked")
		var stream = ((StreamingParserFactory<Collection<VdypLayerSpecies>>) parserFactory).get();

		assertThat(stream, instanceOf(StreamingParser.class));

		var genera1 = assertNext(stream);
		assertThat(genera1, hasSize(2));

		var genera2 = assertNext(stream);
		assertThat(genera2, hasSize(2));

		assertEmpty(stream);
	}

	@Test
	public void testParseComputesAgesWhenMissing() throws Exception {

		var parser = new VdypSpeciesParser();

		Map<String, Object> controlMap = new HashMap<>();

		controlMap.put(VdypSpeciesParser.CONTROL_KEY, "test.dat");
		TestUtils.populateControlMapGenusReal(controlMap);

		var fileResolver = TestUtils.fileResolver(
				"test.dat",
				TestUtils.makeStream(
						"01002 S000002 00     1970 P 15 S  S  100.0     0.0     0.0     0.0 -9.00 -9.00  20.0  12.0  -9.0 0 -9",
						"01002 S000002 00     1970 V  3 B  B   50.0 S  50.0     0.0     0.0 -9.00 -9.00  -9.0   8.0   4.0",
						"01002 S000002 00     1970 V  5 L  L   50.0 LA 25.0 LT 15.0 LW 10.0 -9.00 -9.00  14.0  -9.0   6.0 1",
						"01002 S000002 00     1970"
				)
		);

		parser.modify(controlMap, fileResolver);

		var parserFactory = controlMap.get(VdypSpeciesParser.CONTROL_KEY);

		assertThat(parserFactory, instanceOf(StreamingParserFactory.class));

		@SuppressWarnings("unchecked")
		var stream = ((StreamingParserFactory<Collection<VdypLayerSpecies>>) parserFactory).get();

		assertThat(stream, instanceOf(StreamingParser.class));

		var genera = assertNext(stream);
		assertThat(genera, hasSize(3));

		assertThat(
				genera,
				hasItems(
						allOf(
								hasProperty("ageTotal", is(20.0f)), hasProperty("ageAtBreastHeight", is(12.0f)),
								hasProperty("yearsToBreastHeight", is(8.0f)),
								hasProperty("isPrimary", is(Optional.of(false)))
						),
						allOf(
								hasProperty(
										"speciesDistributions",
										hasProperty(
												"speciesDistributionMap",
												allOf(
														hasEntry(
																is("B"),
																allOf(
																		hasProperty("species", is("B")),
																		hasProperty("percentage", is(50.0f))
																)
														),
														hasEntry(
																is("S"),
																allOf(
																		hasProperty("species", is("S")),
																		hasProperty("percentage", is(50.0f))
																)
														)
												)
										)
								), hasProperty("ageTotal", is(12.0f)), hasProperty("ageAtBreastHeight", is(8.0f)),
								hasProperty("yearsToBreastHeight", is(4.0f)),
								hasProperty("isPrimary", is(Optional.empty()))
						),
						allOf(
								hasProperty(
										"speciesDistributions",
										hasProperty(
												"speciesDistributionMap",
												allOf(
														hasEntry(
																is("L"),
																allOf(
																		hasProperty("species", is("L")),
																		hasProperty("percentage", is(50.0f))
																)
														),
														hasEntry(
																is("LA"),
																allOf(
																		hasProperty("species", is("LA")),
																		hasProperty("percentage", is(25.0f))
																)
														),
														hasEntry(
																is("LT"),
																allOf(
																		hasProperty("species", is("LT")),
																		hasProperty("percentage", is(15.0f))
																)
														),
														hasEntry(
																is("LW"),
																allOf(
																		hasProperty("species", is("LW")),
																		hasProperty("percentage", is(10.0f))
																)
														)
												)
										)
								), hasProperty("ageTotal", is(14.0f)), hasProperty("ageAtBreastHeight", is(8.0f)),
								hasProperty("yearsToBreastHeight", is(6.0f)),
								hasProperty("isPrimary", is(Optional.of(true)))
						)
				)
		);

		assertEmpty(stream);
	}

	@Test
	public void testParseTwoGenera() throws Exception {

		var parser = new VdypSpeciesParser();

		Map<String, Object> controlMap = new HashMap<>();

		controlMap.put(VdypSpeciesParser.CONTROL_KEY, "test.dat");
		TestUtils.populateControlMapGenusReal(controlMap);

		var fileResolver = TestUtils.fileResolver(
				"test.dat",
				TestUtils.makeStream(
						"01002 S000002 00     1970 P 15 S  S  100.0     0.0     0.0     0.0 -9.00 -9.00  -9.0  -9.0  -9.0 0 -9",
						"01002 S000002 00     1970 V  3 B  B   50.0 S  50.0     0.0     0.0 -9.00 -9.00  -9.0  -9.0  -9.0",
						"01002 S000002 00     1970"
				)
		);

		parser.modify(controlMap, fileResolver);

		var parserFactory = controlMap.get(VdypSpeciesParser.CONTROL_KEY);

		assertThat(parserFactory, instanceOf(StreamingParserFactory.class));

		@SuppressWarnings("unchecked")
		var stream = ((StreamingParserFactory<Collection<VdypLayerSpecies>>) parserFactory).get();

		assertThat(stream, instanceOf(StreamingParser.class));

		var genera = assertNext(stream);

		assertThat(
				genera,
				hasItems(
						allOf(
								hasProperty("polygonId", is("01002 S000002 00     1970")),
								hasProperty("layerType", is(LayerType.PRIMARY)), hasProperty("genusIndex", is(15)),
								hasProperty("genus", is(Optional.of("S"))),
								hasProperty(
										"speciesDistributions",
										hasProperty(
												"speciesDistributionMap",
												hasEntry(
														is("S"),
														allOf(
																hasProperty("species", is("S")),
																hasProperty("percentage", is(100.0f))
														)
												)
										)
								), hasProperty("siteIndex", is(-9.0f)), hasProperty("dominantHeight", is(-9.0f)),
								hasProperty("ageTotal", is(-9.0f)), hasProperty("ageAtBreastHeight", is(-9.0f)),
								hasProperty("yearsToBreastHeight", is(-9.0f)),
								hasProperty("isPrimary", is(Optional.of(false))),
								hasProperty("siteCurveNumber", is(Optional.of(-9)))
						),
						allOf(
								hasProperty("polygonId", is("01002 S000002 00     1970")),
								hasProperty("layerType", is(LayerType.VETERAN)), hasProperty("genusIndex", is(3)),
								hasProperty("genus", is(Optional.of("B"))),
								hasProperty(
										"speciesDistributions",
										hasProperty(
												"speciesDistributionMap",
												allOf(
														hasEntry(
																is("B"),
																allOf(
																		hasProperty("species", is("B")),
																		hasProperty("percentage", is(50.0f))
																)
														),
														hasEntry(
																is("S"),
																allOf(
																		hasProperty("species", is("S")),
																		hasProperty("percentage", is(50.0f))
																)
														)
												)
										)
								), hasProperty("siteIndex", is(-9.0f)), hasProperty("dominantHeight", is(-9.0f)),
								hasProperty("ageTotal", is(-9.0f)), hasProperty("ageAtBreastHeight", is(-9.0f)),
								hasProperty("yearsToBreastHeight", is(-9.0f)),
								hasProperty("isPrimary", is(Optional.empty())),
								hasProperty("siteCurveNumber", is(Optional.of(9)))
						)
				)
		);

		assertEmpty(stream);
	}
}
