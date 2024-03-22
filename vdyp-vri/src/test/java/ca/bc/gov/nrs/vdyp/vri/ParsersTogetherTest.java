package ca.bc.gov.nrs.vdyp.vri;

import static ca.bc.gov.nrs.vdyp.test.VdypMatchers.closeTo;
import static ca.bc.gov.nrs.vdyp.test.VdypMatchers.notPresent;
import static ca.bc.gov.nrs.vdyp.test.VdypMatchers.present;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.bc.gov.nrs.vdyp.application.StandProcessingException;
import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.common.Utils;
import ca.bc.gov.nrs.vdyp.io.parse.coe.BecDefinitionParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.DefaultEquationNumberParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.EquationModifierParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.GenusDefinitionParser;
import ca.bc.gov.nrs.vdyp.io.parse.common.ResourceParseException;
import ca.bc.gov.nrs.vdyp.io.parse.streaming.MockStreamingParser;
import ca.bc.gov.nrs.vdyp.model.BecDefinition;
import ca.bc.gov.nrs.vdyp.model.BecLookup;
import ca.bc.gov.nrs.vdyp.model.LayerType;
import ca.bc.gov.nrs.vdyp.model.MatrixMap2Impl;
import ca.bc.gov.nrs.vdyp.model.Region;
import ca.bc.gov.nrs.vdyp.test.MockFileResolver;
import ca.bc.gov.nrs.vdyp.test.TestUtils;
import ca.bc.gov.nrs.vdyp.vri.model.VriLayer;
import ca.bc.gov.nrs.vdyp.vri.model.VriPolygon;
import ca.bc.gov.nrs.vdyp.vri.model.VriSite;
import ca.bc.gov.nrs.vdyp.vri.model.VriSpecies;

class ParsersTogetherTest {

	Map<String, Object> controlMap = new HashMap<>();
	MockFileResolver resolver;
	IMocksControl mockControl = EasyMock.createControl();

	@BeforeEach
	void setUp() throws IOException, ResourceParseException {
		controlMap.put(ControlKey.VRI_OUTPUT_VDYP_POLYGON.name(), "DUMMY1");
		controlMap.put(ControlKey.VRI_OUTPUT_VDYP_LAYER_BY_SPECIES.name(), "DUMMY2");
		controlMap.put(ControlKey.VRI_OUTPUT_VDYP_LAYER_BY_SP0_BY_UTIL.name(), "DUMMY3");

		resolver = new MockFileResolver("Test");

		resolver.addStream("DUMMY1", (OutputStream) new ByteArrayOutputStream());
		resolver.addStream("DUMMY2", (OutputStream) new ByteArrayOutputStream());
		resolver.addStream("DUMMY3", (OutputStream) new ByteArrayOutputStream());

		controlMap.put(
				ControlKey.BEC_DEF.name(),
				new BecDefinitionParser().parse(TestUtils.class, "coe/Becdef.dat", controlMap)
		);
		controlMap.put(
				ControlKey.SP0_DEF.name(),
				new GenusDefinitionParser().parse(TestUtils.class, "coe/SP0DEF_v0.dat", controlMap)
		);
		controlMap.put(
				ControlKey.DEFAULT_EQ_NUM.name(),
				new DefaultEquationNumberParser().parse(TestUtils.class, "coe/GRPBA1.DAT", controlMap)
		);
		controlMap.put(
				ControlKey.EQN_MODIFIERS.name(),
				new EquationModifierParser().parse(TestUtils.class, "coe/GMODBA1.DAT", controlMap)
		);
	}

	@AfterEach
	void verifyMocks() {
		mockControl.verify();
	}

	@Test
	void testPrimaryOnly() throws IOException, StandProcessingException, ResourceParseException {
		var app = new VriStart();

		final var polygonId = "Test";
		final var layerType = LayerType.PRIMARY;

		mockControl.replay();

		app.init(resolver, controlMap);

		var polyStream = new MockStreamingParser<VriPolygon>();
		var layerStream = new MockStreamingParser<Map<LayerType, VriLayer.Builder>>();
		var speciesStream = new MockStreamingParser<Collection<VriSpecies>>();
		var siteStream = new MockStreamingParser<Collection<VriSite>>();

		polyStream.addValue(VriPolygon.build(polyBuilder -> {
			polyBuilder.polygonIdentifier(polygonId);
			polyBuilder.percentAvailable(Optional.of(100.0f));
			polyBuilder.biogeoclimaticZone("IDF");
			polyBuilder.yieldFactor(0.9f);
		}));

		var layerBuilder = new VriLayer.Builder();
		layerBuilder.polygonIdentifier(polygonId);
		layerBuilder.layerType(layerType);
		layerBuilder.crownClosure(0.95f);
		layerBuilder.utilization(0.6f);
		layerBuilder.baseArea(20);
		layerBuilder.treesPerHectare(300);
		layerStream.addValue(Collections.singletonMap(layerType, layerBuilder));

		speciesStream.addValue(Collections.singleton(VriSpecies.build(specBuilder -> {
			specBuilder.polygonIdentifier("Test");
			specBuilder.layerType(layerType);
			specBuilder.genus("B");
			specBuilder.percentGenus(100f);
		})));
		siteStream.addValue(Collections.singleton(VriSite.build(siteBuilder -> {
			siteBuilder.polygonIdentifier("Test");
			siteBuilder.layerType(layerType);
			siteBuilder.siteGenus("B");
			siteBuilder.siteSpecies("B");
		})));

		var result = app.getPolygon(polyStream, layerStream, speciesStream, siteStream);

		assertThat(result, hasProperty("layers", Matchers.aMapWithSize(2)));
		var primaryResult = result.getLayers().get(LayerType.PRIMARY);
		var veteranResult = result.getLayers().get(LayerType.VETERAN);
		primaryResult.getPrimaryGenus();
		assertThat(
				primaryResult, allOf(
						hasProperty("polygonIdentifier", is(polygonId)), //
						hasProperty("layer", is(LayerType.PRIMARY)), //
						hasProperty("crownClosure", is(0.95f)), //
						hasProperty("utilization", is(7.5f)), // Raised to minimum
						hasProperty("baseArea", present(is(20f))), //
						hasProperty("treesPerHectare", present(is(300f))), //
						hasProperty("primaryGenus", present(is("B"))), //
						hasProperty("secondaryGenus", notPresent())
				)
		);
		// Set to defaults, not that optional values should be present with a value of 0
		// instead of not present per the VDYP7 Fortran
		assertThat(
				veteranResult, allOf(
						hasProperty("polygonIdentifier", is(polygonId)), //
						hasProperty("layer", is(LayerType.VETERAN)), //
						hasProperty("crownClosure", is(0f)), //
						hasProperty("utilization", is(7.5f)), //
						hasProperty("baseArea", present(is(0f))), //
						hasProperty("treesPerHectare", present(is(0f))), hasProperty("primaryGenus", notPresent()), //
						hasProperty("secondaryGenus", notPresent())
				)
		);

		app.close();
	}

	@Test
	void testVeteranOnly() throws IOException, StandProcessingException, ResourceParseException {
		var app = new VriStart();

		final var polygonId = "Test";
		final var layerType = LayerType.VETERAN;

		mockControl.replay();

		app.init(resolver, controlMap);

		var polyStream = new MockStreamingParser<VriPolygon>();
		var layerStream = new MockStreamingParser<Map<LayerType, VriLayer.Builder>>();
		var speciesStream = new MockStreamingParser<Collection<VriSpecies>>();
		var siteStream = new MockStreamingParser<Collection<VriSite>>();

		polyStream.addValue(VriPolygon.build(polyBuilder -> {
			polyBuilder.polygonIdentifier(polygonId);
			polyBuilder.percentAvailable(Optional.of(100.0f));
			polyBuilder.biogeoclimaticZone("IDF");
			polyBuilder.yieldFactor(0.9f);
		}));

		var layerBuilder = new VriLayer.Builder();
		layerBuilder.polygonIdentifier(polygonId);
		layerBuilder.layerType(layerType);
		layerBuilder.crownClosure(0.95f);
		layerBuilder.utilization(0.6f);
		layerBuilder.baseArea(20);
		layerBuilder.treesPerHectare(300);
		layerStream.addValue(Collections.singletonMap(layerType, layerBuilder));

		speciesStream.addValue(Collections.singleton(VriSpecies.build(specBuilder -> {
			specBuilder.polygonIdentifier("Test");
			specBuilder.layerType(layerType);
			specBuilder.genus("B");
			specBuilder.percentGenus(100f);
		})));
		siteStream.addValue(Collections.singleton(VriSite.build(siteBuilder -> {
			siteBuilder.polygonIdentifier("Test");
			siteBuilder.layerType(layerType);
			siteBuilder.siteGenus("B");
			siteBuilder.siteSpecies("B");
		})));

		var result = app.getPolygon(polyStream, layerStream, speciesStream, siteStream);

		assertThat(result, hasProperty("layers", Matchers.aMapWithSize(2)));
		var primaryResult = result.getLayers().get(LayerType.PRIMARY);
		var veteranResult = result.getLayers().get(LayerType.VETERAN);
		assertThat(
				veteranResult, allOf(
						hasProperty("polygonIdentifier", is(polygonId)), //
						hasProperty("layer", is(LayerType.VETERAN)), //
						hasProperty("crownClosure", is(0.95f)), //
						hasProperty("utilization", is(7.5f)), // Raised to minimum
						hasProperty("baseArea", present(is(20f))), //
						hasProperty("treesPerHectare", present(is(300f))), //
						hasProperty("primaryGenus", present(is("B"))), //
						hasProperty("secondaryGenus", notPresent())
				)
		);
		// Set to defaults, not that optional values should be present with a value of 0
		// instead of not present per the VDYP7 Fortran
		// Except that 0 and 0 are present so they should be nulled later during the
		// check that computed DQ is less than 7.5.
		assertThat(
				primaryResult, allOf(
						hasProperty("polygonIdentifier", is(polygonId)), //
						hasProperty("layer", is(LayerType.PRIMARY)), //
						hasProperty("crownClosure", is(0f)), //
						hasProperty("utilization", is(7.5f)), //
						hasProperty("baseArea", present(is(0f))), //
						hasProperty("treesPerHectare", present(is(0f))), //
						hasProperty("primaryGenus", notPresent()), //
						hasProperty("secondaryGenus", notPresent())
				)
		);
		app.close();
	}

	@Test
	void testApplyPercentAvailableToPrimaryLayer()
			throws IOException, StandProcessingException, ResourceParseException {
		var app = new VriStart();

		final var polygonId = "Test";
		final var layerType = LayerType.VETERAN;

		mockControl.replay();

		app.init(resolver, controlMap);

		var polyStream = new MockStreamingParser<VriPolygon>();
		var layerStream = new MockStreamingParser<Map<LayerType, VriLayer.Builder>>();
		var speciesStream = new MockStreamingParser<Collection<VriSpecies>>();
		var siteStream = new MockStreamingParser<Collection<VriSite>>();

		polyStream.addValue(VriPolygon.build(polyBuilder -> {
			polyBuilder.polygonIdentifier(polygonId);
			polyBuilder.percentAvailable(Optional.of(75.0f));
			polyBuilder.biogeoclimaticZone("IDF");
			polyBuilder.yieldFactor(0.9f);
		}));

		var layerBuilder1 = new VriLayer.Builder();
		layerBuilder1.polygonIdentifier(polygonId);
		layerBuilder1.layerType(LayerType.PRIMARY);
		layerBuilder1.crownClosure(0.95f);
		layerBuilder1.utilization(9f);
		layerBuilder1.baseArea(20);
		layerBuilder1.treesPerHectare(300);
		var layerBuilder2 = new VriLayer.Builder();
		layerBuilder2.polygonIdentifier(polygonId);
		layerBuilder2.layerType(LayerType.VETERAN);
		layerBuilder2.crownClosure(0.8f);
		layerBuilder2.utilization(8f);
		layerBuilder2.baseArea(30);
		layerBuilder2.treesPerHectare(200);
		layerStream.addValue(Utils.constMap(map -> {
			map.put(LayerType.PRIMARY, layerBuilder1);
			map.put(LayerType.VETERAN, layerBuilder2);
		}));

		speciesStream.addValue(Collections.singleton(VriSpecies.build(specBuilder -> {
			specBuilder.polygonIdentifier("Test");
			specBuilder.layerType(layerType);
			specBuilder.genus("B");
			specBuilder.percentGenus(100f);
		})));
		siteStream.addValue(Collections.singleton(VriSite.build(siteBuilder -> {
			siteBuilder.polygonIdentifier("Test");
			siteBuilder.layerType(layerType);
			siteBuilder.siteGenus("B");
			siteBuilder.siteSpecies("B");
		})));

		var result = app.getPolygon(polyStream, layerStream, speciesStream, siteStream);

		assertThat(result, hasProperty("layers", Matchers.aMapWithSize(2)));
		var primaryResult = result.getLayers().get(LayerType.PRIMARY);
		var veteranResult = result.getLayers().get(LayerType.VETERAN);
		assertThat(
				primaryResult, allOf(
						hasProperty("polygonIdentifier", is(polygonId)), //
						hasProperty("layer", is(LayerType.PRIMARY)), //
						hasProperty("crownClosure", is(0.95f)), //
						hasProperty("utilization", is(9f)), hasProperty("baseArea", present(closeTo(20f * 0.75f))), // Apply
																													// Layer
																													// Percent
																													// Available
						hasProperty("treesPerHectare", present(closeTo(300f * 0.75f))) // Apply Layer Percent Available
				)
		);
		// Set to defaults, not that optional values should be present with a value of 0
		// instead of not present per the VDYP7 Fortran
		assertThat(
				veteranResult, allOf(
						hasProperty("polygonIdentifier", is(polygonId)), //
						hasProperty("layer", is(LayerType.VETERAN)), //
						hasProperty("crownClosure", is(0.8f)), //
						hasProperty("utilization", is(8f)), //
						hasProperty("baseArea", present(is(30f))), //
						hasProperty("treesPerHectare", present(is(200f)))
				)
		);
		app.close();
	}

	@Test
	void testPrimaryWithSmallComputedDiameter() throws IOException, StandProcessingException, ResourceParseException {
		var app = new VriStart();

		final var polygonId = "Test";
		final var layerType = LayerType.PRIMARY;

		mockControl.replay();

		app.init(resolver, controlMap);

		var polyStream = new MockStreamingParser<VriPolygon>();
		var layerStream = new MockStreamingParser<Map<LayerType, VriLayer.Builder>>();
		var speciesStream = new MockStreamingParser<Collection<VriSpecies>>();
		var siteStream = new MockStreamingParser<Collection<VriSite>>();

		polyStream.addValue(VriPolygon.build(polyBuilder -> {
			polyBuilder.polygonIdentifier(polygonId);
			polyBuilder.percentAvailable(Optional.of(100.0f));
			polyBuilder.biogeoclimaticZone("IDF");
			polyBuilder.yieldFactor(0.9f);
		}));

		var layerBuilder = new VriLayer.Builder();
		layerBuilder.polygonIdentifier(polygonId);
		layerBuilder.layerType(layerType);
		layerBuilder.crownClosure(0.95f);
		layerBuilder.utilization(0.6f);
		layerBuilder.baseArea(20);
		layerBuilder.treesPerHectare(300);
		layerStream.addValue(Collections.singletonMap(layerType, layerBuilder));

		speciesStream.addValue(Collections.singleton(VriSpecies.build(specBuilder -> {
			specBuilder.polygonIdentifier("Test");
			specBuilder.layerType(layerType);
			specBuilder.genus("B");
			specBuilder.percentGenus(100f);
		})));
		siteStream.addValue(Collections.singleton(VriSite.build(siteBuilder -> {
			siteBuilder.polygonIdentifier("Test");
			siteBuilder.layerType(layerType);
			siteBuilder.siteGenus("B");
			siteBuilder.siteSpecies("B");
		})));

		var result = app.getPolygon(polyStream, layerStream, speciesStream, siteStream);

		assertThat(result, hasProperty("layers", Matchers.aMapWithSize(2)));
		var primaryResult = result.getLayers().get(LayerType.PRIMARY);
		var veteranResult = result.getLayers().get(LayerType.VETERAN);
		assertThat(
				primaryResult, allOf(
						hasProperty("polygonIdentifier", is(polygonId)), //
						hasProperty("layer", is(LayerType.PRIMARY)), //
						hasProperty("crownClosure", is(0.95f)), //
						hasProperty("utilization", is(7.5f)), // Raised to minimum
						hasProperty("baseArea", present(is(20f))), //
						hasProperty("treesPerHectare", present(is(300f)))
				)
		);
		// Set to defaults, not that optional values should be present with a value of 0
		// instead of not present per the VDYP7 Fortran
		assertThat(
				veteranResult, allOf(
						hasProperty("polygonIdentifier", is(polygonId)), //
						hasProperty("layer", is(LayerType.VETERAN)), //
						hasProperty("crownClosure", is(0f)), //
						hasProperty("utilization", is(7.5f)), //
						hasProperty("baseArea", present(is(0f))), //
						hasProperty("treesPerHectare", present(is(0f)))
				)
		);

		app.close();
	}

	@Test
	void testFindsPrimaryGenusAndITG() throws IOException, StandProcessingException, ResourceParseException {
		var app = new VriStart();

		final var polygonId = "Test";
		final var layerType = LayerType.PRIMARY;

		mockControl.replay();

		app.init(resolver, controlMap);

		var polyStream = new MockStreamingParser<VriPolygon>();
		var layerStream = new MockStreamingParser<Map<LayerType, VriLayer.Builder>>();
		var speciesStream = new MockStreamingParser<Collection<VriSpecies>>();
		var siteStream = new MockStreamingParser<Collection<VriSite>>();

		polyStream.addValue(VriPolygon.build(polyBuilder -> {
			polyBuilder.polygonIdentifier(polygonId);
			polyBuilder.percentAvailable(Optional.of(100.0f));
			polyBuilder.biogeoclimaticZone("IDF");
			polyBuilder.yieldFactor(0.9f);
		}));

		var layerBuilder = new VriLayer.Builder();
		layerBuilder.polygonIdentifier(polygonId);
		layerBuilder.layerType(layerType);
		layerBuilder.crownClosure(0.95f);
		layerBuilder.utilization(0.6f);
		layerBuilder.baseArea(20);
		layerBuilder.treesPerHectare(300);
		layerStream.addValue(Collections.singletonMap(layerType, layerBuilder));

		speciesStream.addValue(List.of(VriSpecies.build(specBuilder -> {
			specBuilder.polygonIdentifier("Test");
			specBuilder.layerType(layerType);
			specBuilder.genus("B");
			specBuilder.percentGenus(80f);
		}), VriSpecies.build(specBuilder -> {
			specBuilder.polygonIdentifier("Test");
			specBuilder.layerType(layerType);
			specBuilder.genus("S");
			specBuilder.percentGenus(20f);
		})));
		siteStream.addValue(List.of(VriSite.build(siteBuilder -> {
			siteBuilder.polygonIdentifier("Test");
			siteBuilder.layerType(layerType);
			siteBuilder.siteGenus("B");
			siteBuilder.siteSpecies("B");
		}), VriSite.build(siteBuilder -> {
			siteBuilder.polygonIdentifier("Test");
			siteBuilder.layerType(layerType);
			siteBuilder.siteGenus("S");
			siteBuilder.siteSpecies("S");
		})));

		var result = app.getPolygon(polyStream, layerStream, speciesStream, siteStream);

		assertThat(result, hasProperty("layers", Matchers.aMapWithSize(2)));
		var primaryResult = result.getLayers().get(LayerType.PRIMARY);
		var veteranResult = result.getLayers().get(LayerType.VETERAN);
		assertThat(
				primaryResult, allOf(
						hasProperty("primaryGenus", present(is("B"))), //
						hasProperty("secondaryGenus", present(is("S"))), //
						hasProperty("inventoryTypeGroup", present(is(18))) // ITG for a pure (80%) B layer
				)
		);
		assertThat(
				veteranResult, allOf(
						// Veteran layer should not have primary genus or ITG
						hasProperty("primaryGenus", notPresent()), //
						hasProperty("secondaryGenus", notPresent()), //
						hasProperty("inventoryTypeGroup", notPresent())
				)
		);

		app.close();
	}

	@Test
	void testFindsGRPBA1() throws IOException, StandProcessingException, ResourceParseException {
		var app = new VriStart();

		final var polygonId = "Test";
		final var layerType = LayerType.PRIMARY;

		mockControl.replay();

		app.init(resolver, controlMap);

		var polyStream = new MockStreamingParser<VriPolygon>();
		var layerStream = new MockStreamingParser<Map<LayerType, VriLayer.Builder>>();
		var speciesStream = new MockStreamingParser<Collection<VriSpecies>>();
		var siteStream = new MockStreamingParser<Collection<VriSite>>();

		polyStream.addValue(VriPolygon.build(polyBuilder -> {
			polyBuilder.polygonIdentifier(polygonId);
			polyBuilder.percentAvailable(Optional.of(100.0f));
			polyBuilder.biogeoclimaticZone("IDF");
			polyBuilder.yieldFactor(0.9f);
		}));

		var layerBuilder = new VriLayer.Builder();
		layerBuilder.polygonIdentifier(polygonId);
		layerBuilder.layerType(layerType);
		layerBuilder.crownClosure(0.95f);
		layerBuilder.utilization(0.6f);
		layerBuilder.baseArea(20);
		layerBuilder.treesPerHectare(300);
		layerStream.addValue(Collections.singletonMap(layerType, layerBuilder));

		speciesStream.addValue(List.of(VriSpecies.build(specBuilder -> {
			specBuilder.polygonIdentifier("Test");
			specBuilder.layerType(layerType);
			specBuilder.genus("B");
			specBuilder.percentGenus(80f);
		}), VriSpecies.build(specBuilder -> {
			specBuilder.polygonIdentifier("Test");
			specBuilder.layerType(layerType);
			specBuilder.genus("S");
			specBuilder.percentGenus(20f);
		})));
		siteStream.addValue(List.of(VriSite.build(siteBuilder -> {
			siteBuilder.polygonIdentifier("Test");
			siteBuilder.layerType(layerType);
			siteBuilder.siteGenus("B");
			siteBuilder.siteSpecies("B");
		}), VriSite.build(siteBuilder -> {
			siteBuilder.polygonIdentifier("Test");
			siteBuilder.layerType(layerType);
			siteBuilder.siteGenus("S");
			siteBuilder.siteSpecies("S");
		})));

		var result = app.getPolygon(polyStream, layerStream, speciesStream, siteStream);

		assertThat(result, hasProperty("layers", Matchers.aMapWithSize(2)));
		var primaryResult = result.getLayers().get(LayerType.PRIMARY);
		var veteranResult = result.getLayers().get(LayerType.VETERAN);
		assertThat(primaryResult, allOf(hasProperty("empericalRelationshipParameterIndex", present(is(27)))));
		assertThat(
				veteranResult, allOf(
						// Veteran layer should not have a GRPBA1
						hasProperty("empericalRelationshipParameterIndex", notPresent())
				)
		);

		app.close();
		mockControl.verify();
	}

}
