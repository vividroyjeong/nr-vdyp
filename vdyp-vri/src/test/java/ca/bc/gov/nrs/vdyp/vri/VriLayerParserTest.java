package ca.bc.gov.nrs.vdyp.vri;

import static ca.bc.gov.nrs.vdyp.test.VdypMatchers.assertEmpty;
import static ca.bc.gov.nrs.vdyp.test.VdypMatchers.assertNext;
import static ca.bc.gov.nrs.vdyp.test.VdypMatchers.hasSpecificEntry;
import static ca.bc.gov.nrs.vdyp.test.VdypMatchers.notPresent;
import static ca.bc.gov.nrs.vdyp.test.VdypMatchers.present;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.io.parse.streaming.StreamingParser;
import ca.bc.gov.nrs.vdyp.io.parse.streaming.StreamingParserFactory;
import ca.bc.gov.nrs.vdyp.model.LayerType;
import ca.bc.gov.nrs.vdyp.test.TestUtils;
import ca.bc.gov.nrs.vdyp.vri.model.VriLayer;

class VriLayerParserTest {

	@Test
	void testParseEmpty() throws Exception {

		var parser = new VriLayerParser();

		Map<String, Object> controlMap = new HashMap<>();

		controlMap.put(ControlKey.VRI_YIELD_LAYER_INPUT.name(), "test.dat");
		TestUtils.populateControlMapBecReal(controlMap);

		var fileResolver = TestUtils.fileResolver("test.dat", TestUtils.makeInputStream(/* empty */));

		parser.modify(controlMap, fileResolver);

		var parserFactory = controlMap.get(ControlKey.VRI_YIELD_LAYER_INPUT.name());

		assertThat(parserFactory, instanceOf(StreamingParserFactory.class));

		@SuppressWarnings("unchecked")
		var stream = ((StreamingParserFactory<VriLayer>) parserFactory).get();

		assertThat(stream, instanceOf(StreamingParser.class));

		assertEmpty(stream);
	}

	@Test
	void testParseLayer() throws Exception {

		var parser = new VriLayerParser();

		Map<String, Object> controlMap = new HashMap<>();

		controlMap.put(ControlKey.VRI_YIELD_LAYER_INPUT.name(), "test.dat");

		var fileResolver = TestUtils.fileResolver(
				"test.dat",
				TestUtils.makeInputStream(
						"082F074/0071         2001 P  57.8     66.0      850  7.5", //
						"082F074/0071         2001 Z"
				)
		);

		parser.modify(controlMap, fileResolver);

		var parserFactory = controlMap.get(ControlKey.VRI_YIELD_LAYER_INPUT.name());

		assertThat(parserFactory, instanceOf(StreamingParserFactory.class));

		@SuppressWarnings("unchecked")
		var stream = ((StreamingParserFactory<Map<LayerType, VriLayer>>) parserFactory).get();

		assertThat(stream, instanceOf(StreamingParser.class));

		var layers = assertNext(stream);

		assertThat(layers, aMapWithSize(1));

		assertThat(
				layers,
				hasSpecificEntry(
						LayerType.PRIMARY, allOf(
								hasProperty("polygonIdentifier", is("082F074/0071         2001")), //
								hasProperty("layer", is(LayerType.PRIMARY)), //
								hasProperty("crownClosure", is(57.8f)), //
								hasProperty("baseArea", present(is(66.0f))), //
								hasProperty("treesPerHectare", present(is(850f))), //
								hasProperty("utilization", is(7.5f))
						)
				)
		);

		assertEmpty(stream);
	}

	@Test
	void testParseTwoLayers() throws Exception {

		var parser = new VriLayerParser();

		Map<String, Object> controlMap = new HashMap<>();

		controlMap.put(ControlKey.VRI_YIELD_LAYER_INPUT.name(), "test.dat");
		TestUtils.populateControlMapGenusReal(controlMap);

		var fileResolver = TestUtils.fileResolver(
				"test.dat",
				TestUtils.makeInputStream(
						"082F074/0071         2001 P  57.8     66.0      850  7.5", //
						"082F074/0071         2001 S  30.0     -9.0       -9  7.5", //
						"082F074/0071         2001 Z"
				)
		);

		parser.modify(controlMap, fileResolver);

		var parserFactory = controlMap.get(ControlKey.VRI_YIELD_LAYER_INPUT.name());

		assertThat(parserFactory, instanceOf(StreamingParserFactory.class));

		@SuppressWarnings("unchecked")
		var stream = ((StreamingParserFactory<Map<LayerType, VriLayer>>) parserFactory).get();

		assertThat(stream, instanceOf(StreamingParser.class));

		var layers = assertNext(stream);

		assertThat(layers, aMapWithSize(2));

		assertThat(
				layers,
				hasSpecificEntry(
						LayerType.PRIMARY, allOf(
								hasProperty("polygonIdentifier", is("082F074/0071         2001")), //
								hasProperty("layer", is(LayerType.PRIMARY)), //
								hasProperty("crownClosure", is(57.8f)), //
								hasProperty("baseArea", present(is(66.0f))), //
								hasProperty("treesPerHectare", present(is(850f))), //
								hasProperty("utilization", is(7.5f))
						)
				)
		);
		assertThat(
				layers,
				hasSpecificEntry(
						LayerType.SECONDARY, allOf(
								hasProperty("polygonIdentifier", is("082F074/0071         2001")), //
								hasProperty("layer", is(LayerType.SECONDARY)), //
								hasProperty("crownClosure", is(30.0f)), //
								hasProperty("baseArea", notPresent()), //
								hasProperty("treesPerHectare", notPresent()), //
								hasProperty("utilization", is(7.5f))
						)
				)
		);

		assertEmpty(stream);
	}

	@Disabled
	@Test
	void testIgnoreLayerIfHeightZero() throws Exception {

		var parser = new VriLayerParser();

		Map<String, Object> controlMap = new HashMap<>();

		controlMap.put(ControlKey.VRI_YIELD_LAYER_INPUT.name(), "test.dat");
		TestUtils.populateControlMapGenusReal(controlMap);

		var fileResolver = TestUtils.fileResolver(
				"test.dat",
				TestUtils.makeInputStream(
						"01002 S000004 00     1970 V 195 45.2 22.3  0.0   B  B  9.4 2               8",
						"01002 S000004 00     1970 1  85 42.3 31.9 82.8   H  H  4.9 0              34",
						"01002 S000004 00     1970 Z  85  0.0  0.0  0.0         0.0"
				)
		);

		parser.modify(controlMap, fileResolver);

		var parserFactory = controlMap.get(ControlKey.VRI_YIELD_LAYER_INPUT.name());

		assertThat(parserFactory, instanceOf(StreamingParserFactory.class));

		@SuppressWarnings("unchecked")
		var stream = ((StreamingParserFactory<Map<LayerType, VriLayer>>) parserFactory).get();

		assertThat(stream, instanceOf(StreamingParser.class));

		var layers = assertNext(stream);

		assertThat(layers, aMapWithSize(1));

		assertThat(
				layers,
				hasSpecificEntry(
						LayerType.PRIMARY, allOf(
								hasProperty("polygonIdentifier", is("01002 S000004 00     1970")), //
								hasProperty("layer", is(LayerType.PRIMARY)), //
								hasProperty("ageTotalSafe", is(85f)), //
								hasProperty("heightSafe", is(42.3f)), //
								hasProperty("siteIndex", present(is(31.9f))), //
								hasProperty("crownClosure", is(82.8f)), //
								hasProperty("siteGenus", present(is("H"))), //
								hasProperty("siteSpecies", present(is("H"))), //
								hasProperty("yearsToBreastHeightSafe", is(4.9f)), //
								hasProperty("stockingClass", present(is('0'))), //
								hasProperty("inventoryTypeGroup", notPresent()), //
								hasProperty("siteCurveNumber", present(is(34)))
						)
				)
		);

		assertEmpty(stream);
	}

	@Disabled
	@Test
	void testIgnoreLayerIfCrownClosureZero() throws Exception {

		var parser = new VriLayerParser();

		Map<String, Object> controlMap = new HashMap<>();

		controlMap.put(ControlKey.VRI_YIELD_LAYER_INPUT.name(), "test.dat");
		TestUtils.populateControlMapGenusReal(controlMap);

		var fileResolver = TestUtils.fileResolver(
				"test.dat",
				TestUtils.makeInputStream(
						"01002 S000004 00     1970 V 195  0.0 22.3  4.0   B  B  9.4 2               8",
						"01002 S000004 00     1970 1  85 42.3 31.9 82.8   H  H  4.9 0              34",
						"01002 S000004 00     1970 Z  85  0.0  0.0  0.0         0.0"
				)
		);

		parser.modify(controlMap, fileResolver);

		var parserFactory = controlMap.get(ControlKey.VRI_YIELD_LAYER_INPUT.name());

		assertThat(parserFactory, instanceOf(StreamingParserFactory.class));

		@SuppressWarnings("unchecked")
		var stream = ((StreamingParserFactory<Map<LayerType, VriLayer>>) parserFactory).get();

		assertThat(stream, instanceOf(StreamingParser.class));

		var layers = assertNext(stream);

		assertThat(layers, aMapWithSize(1));

		assertThat(
				layers,
				hasSpecificEntry(
						LayerType.PRIMARY, allOf(
								hasProperty("polygonIdentifier", is("01002 S000004 00     1970")), //
								hasProperty("layer", is(LayerType.PRIMARY)), //
								hasProperty("ageTotalSafe", is(85f)), //
								hasProperty("heightSafe", is(42.3f)), //
								hasProperty("siteIndex", present(is(31.9f))), //
								hasProperty("crownClosure", is(82.8f)), //
								hasProperty("siteGenus", present(is("H"))), //
								hasProperty("siteSpecies", present(is("H"))), //
								hasProperty("yearsToBreastHeightSafe", is(4.9f)),
								hasProperty("stockingClass", present(is('0'))), //
								hasProperty("inventoryTypeGroup", notPresent()), //
								hasProperty("siteCurveNumber", present(is(34)))
						)
				)
		);

		assertEmpty(stream);
	}

}
