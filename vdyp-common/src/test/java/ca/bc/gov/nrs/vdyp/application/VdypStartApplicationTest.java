package ca.bc.gov.nrs.vdyp.application;

import static ca.bc.gov.nrs.vdyp.test.VdypMatchers.causedBy;
import static ca.bc.gov.nrs.vdyp.test.VdypMatchers.closeTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.describedAs;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.fip.FipStart;
import ca.bc.gov.nrs.vdyp.fip.model.FipLayer;
import ca.bc.gov.nrs.vdyp.fip.model.FipPolygon;
import ca.bc.gov.nrs.vdyp.fip.model.FipSite;
import ca.bc.gov.nrs.vdyp.fip.model.FipSpecies;
import ca.bc.gov.nrs.vdyp.fip.test.FipTestUtils;
import ca.bc.gov.nrs.vdyp.io.parse.common.ResourceParseException;
import ca.bc.gov.nrs.vdyp.io.parse.control.BaseControlParser;
import ca.bc.gov.nrs.vdyp.io.parse.streaming.StreamingParser;
import ca.bc.gov.nrs.vdyp.io.parse.streaming.StreamingParserFactory;
import ca.bc.gov.nrs.vdyp.model.BaseVdypSpecies;
import ca.bc.gov.nrs.vdyp.model.LayerType;
import ca.bc.gov.nrs.vdyp.test.MockFileResolver;
import ca.bc.gov.nrs.vdyp.test.TestUtils;

class VdypStartApplicationTest {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testInitWithoutErrors() throws IOException, ResourceParseException {
		BaseControlParser controlParser = EasyMock.createMock(BaseControlParser.class);
		MockFileResolver resolver = new MockFileResolver("Test");

		InputStream inputStream = TestUtils.makeInputStream("");
		resolver.addStream("testControl", inputStream);

		var controlMap = new HashMap<String, Object>();

		controlMap.put(ControlKey.VRI_OUTPUT_VDYP_POLYGON.name(), "DUMMY1");
		controlMap.put(ControlKey.VRI_OUTPUT_VDYP_LAYER_BY_SPECIES.name(), "DUMMY2");
		controlMap.put(ControlKey.VRI_OUTPUT_VDYP_LAYER_BY_SP0_BY_UTIL.name(), "DUMMY3");

		resolver.addStream("DUMMY1", new ByteArrayOutputStream());
		resolver.addStream("DUMMY2", new ByteArrayOutputStream());
		resolver.addStream("DUMMY3", new ByteArrayOutputStream());

		EasyMock.expect(
				controlParser.parse(EasyMock.anyObject(List.class), EasyMock.same(resolver), EasyMock.anyObject())
		).andReturn(controlMap);

		var app = new VdypStartApplication() {

			@Override
			protected BaseControlParser getControlFileParser() {
				return controlParser;
			}

			@Override
			public void process() throws ProcessingException {
				// Do Nothing
			}

			@Override
			public VdypApplicationIdentifier getId() {
				return VdypApplicationIdentifier.FIP_START;
			}

			@Override
			protected BaseVdypSpecies copySpecies(BaseVdypSpecies toCopy, Consumer config) {
				return null;
			}
		};

		EasyMock.replay(controlParser);

		app.init(resolver, "testControl");
		assertThat(app.controlMap, is(controlMap));

		EasyMock.verify(controlParser);
		app.close();
	}

	@SuppressWarnings({ "rawtypes" })
	@Test
	void testInitNoControlFiles() throws IOException, ResourceParseException {
		BaseControlParser controlParser = EasyMock.createMock(BaseControlParser.class);
		MockFileResolver resolver = new MockFileResolver("Test");

		var app = new VdypStartApplication() {

			@Override
			protected BaseControlParser getControlFileParser() {
				return controlParser;
			}

			@Override
			public void process() throws ProcessingException {
				// Do Nothing
			}

			@Override
			public VdypApplicationIdentifier getId() {
				return VdypApplicationIdentifier.FIP_START;
			}

			@Override
			protected BaseVdypSpecies copySpecies(BaseVdypSpecies toCopy, Consumer config) {
				return null;
			}
		};

		EasyMock.replay(controlParser);

		var ex = assertThrows(IllegalArgumentException.class, () -> app.init(resolver));
		assertThat(ex, hasProperty("message", is("At least one control file must be specified.")));

		EasyMock.verify(controlParser);
		app.close();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testGetStreamingParser() throws IOException, ResourceParseException, ProcessingException {
		BaseControlParser controlParser = EasyMock.createMock(BaseControlParser.class);
		StreamingParserFactory streamingParserFactory = EasyMock.createMock(StreamingParserFactory.class);
		StreamingParser streamingParser = EasyMock.createMock(StreamingParser.class);

		MockFileResolver resolver = new MockFileResolver("Test");
		var controlMap = new HashMap<String, Object>();

		controlMap.put(ControlKey.VRI_OUTPUT_VDYP_POLYGON.name(), "DUMMY1");
		controlMap.put(ControlKey.VRI_OUTPUT_VDYP_LAYER_BY_SPECIES.name(), "DUMMY2");
		controlMap.put(ControlKey.VRI_OUTPUT_VDYP_LAYER_BY_SP0_BY_UTIL.name(), "DUMMY3");
		controlMap.put(ControlKey.FIP_INPUT_YIELD_LAYER.name(), streamingParserFactory);

		resolver.addStream("DUMMY1", new ByteArrayOutputStream());
		resolver.addStream("DUMMY2", new ByteArrayOutputStream());
		resolver.addStream("DUMMY3", new ByteArrayOutputStream());

		EasyMock.expect(streamingParserFactory.get()).andReturn(streamingParser);

		var app = new VdypStartApplication() {

			@Override
			protected BaseControlParser getControlFileParser() {
				return controlParser;
			}

			@Override
			public void process() throws ProcessingException {
				// Do Nothing
			}

			@Override
			public VdypApplicationIdentifier getId() {
				return VdypApplicationIdentifier.FIP_START;
			}

			@Override
			protected BaseVdypSpecies copySpecies(BaseVdypSpecies toCopy, Consumer config) {
				return null;
			}
		};

		EasyMock.replay(controlParser, streamingParserFactory, streamingParser);

		app.init(resolver, controlMap);

		var result = app.getStreamingParser(ControlKey.FIP_INPUT_YIELD_LAYER);

		assertThat(result, is(streamingParser));

		EasyMock.verify(controlParser, streamingParserFactory, streamingParser);

		app.close();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testGetStreamingParserMapEntryMissing() throws IOException, ResourceParseException, ProcessingException {
		BaseControlParser controlParser = EasyMock.createMock(BaseControlParser.class);

		MockFileResolver resolver = new MockFileResolver("Test");
		var controlMap = new HashMap<String, Object>();

		controlMap.put(ControlKey.VRI_OUTPUT_VDYP_POLYGON.name(), "DUMMY1");
		controlMap.put(ControlKey.VRI_OUTPUT_VDYP_LAYER_BY_SPECIES.name(), "DUMMY2");
		controlMap.put(ControlKey.VRI_OUTPUT_VDYP_LAYER_BY_SP0_BY_UTIL.name(), "DUMMY3");

		resolver.addStream("DUMMY1", new ByteArrayOutputStream());
		resolver.addStream("DUMMY2", new ByteArrayOutputStream());
		resolver.addStream("DUMMY3", new ByteArrayOutputStream());

		var app = new VdypStartApplication() {

			@Override
			protected BaseControlParser getControlFileParser() {
				return controlParser;
			}

			@Override
			public void process() throws ProcessingException {
				// Do Nothing
			}

			@Override
			public VdypApplicationIdentifier getId() {
				return VdypApplicationIdentifier.FIP_START;
			}

			@Override
			protected BaseVdypSpecies copySpecies(BaseVdypSpecies toCopy, Consumer config) {
				return null;
			}
		};

		EasyMock.replay(controlParser);

		app.init(resolver, controlMap);

		var ex = assertThrows(
				ProcessingException.class, () -> app.getStreamingParser(ControlKey.FIP_INPUT_YIELD_LAYER)
		);
		assertThat(ex, hasProperty("message", is("Data file FIP_INPUT_YIELD_LAYER not specified in control map.")));

		EasyMock.verify(controlParser);

		app.close();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testGetStreamingParserErrorOpeningFile() throws IOException, ResourceParseException, ProcessingException {
		BaseControlParser controlParser = EasyMock.createMock(BaseControlParser.class);
		StreamingParserFactory streamingParserFactory = EasyMock.createMock(StreamingParserFactory.class);

		MockFileResolver resolver = new MockFileResolver("Test");
		var controlMap = new HashMap<String, Object>();

		controlMap.put(ControlKey.VRI_OUTPUT_VDYP_POLYGON.name(), "DUMMY1");
		controlMap.put(ControlKey.VRI_OUTPUT_VDYP_LAYER_BY_SPECIES.name(), "DUMMY2");
		controlMap.put(ControlKey.VRI_OUTPUT_VDYP_LAYER_BY_SP0_BY_UTIL.name(), "DUMMY3");
		controlMap.put(ControlKey.FIP_INPUT_YIELD_LAYER.name(), streamingParserFactory);

		resolver.addStream("DUMMY1", new ByteArrayOutputStream());
		resolver.addStream("DUMMY2", new ByteArrayOutputStream());
		resolver.addStream("DUMMY3", new ByteArrayOutputStream());

		IOException exception = new IOException("This is a Test");
		EasyMock.expect(streamingParserFactory.get()).andThrow(exception);

		var app = getTestUnit(controlParser);

		EasyMock.replay(controlParser, streamingParserFactory);

		app.init(resolver, controlMap);

		var ex = assertThrows(
				ProcessingException.class, () -> app.getStreamingParser(ControlKey.FIP_INPUT_YIELD_LAYER)
		);
		assertThat(ex, hasProperty("message", is("Error while opening data file.")));
		assertThat(ex, causedBy(is(exception)));

		EasyMock.verify(controlParser, streamingParserFactory);

		app.close();
	}

	protected VdypStartApplication getTestUnit(BaseControlParser controlParser) {
		return new VdypStartApplication() {

			@Override
			protected BaseControlParser getControlFileParser() {
				return controlParser;
			}

			@Override
			public void process() throws ProcessingException {
				// Do Nothing
			}

			@Override
			public VdypApplicationIdentifier getId() {
				return VdypApplicationIdentifier.FIP_START;
			}

			@Override
			protected BaseVdypSpecies copySpecies(BaseVdypSpecies toCopy, Consumer config) {
				return null;
			}
		};
	}
	@Test
	void testFindPrimarySpeciesNoSpecies() throws Exception {
		var controlMap = FipTestUtils.loadControlMap();
		try (VdypStartApplication<FipPolygon, FipLayer, FipSpecies, FipSite> app = new FipStart()) {
			app.setControlMap(controlMap);

			Map<String, FipSpecies> allSpecies = Collections.emptyMap();
			assertThrows(IllegalArgumentException.class, () -> app.findPrimarySpecies(allSpecies));
		}
	}

	@Test
	void testFindPrimarySpeciesOneSpecies() throws Exception {
		var controlMap = FipTestUtils.loadControlMap();
		try (VdypStartApplication<FipPolygon, FipLayer, FipSpecies, FipSite> app = new FipStart()) {
			app.setControlMap(controlMap);

			var spec = this.getTestSpecies("test polygon", LayerType.PRIMARY, "B", valid());

			Map<String, FipSpecies> allSpecies = Collections.singletonMap("B", spec);
			var result = app.findPrimarySpecies(allSpecies);

			assertThat(result, hasSize(1));
			assertThat(
					result, contains(allOf(hasProperty("genus", is("B")), hasProperty("percentGenus", closeTo(100f))))
			);
		}
	}

	@Test
	void testFindPrimaryCombinePAIntoPL() throws Exception {
		var controlMap = FipTestUtils.loadControlMap();
		try (VdypStartApplication<FipPolygon, FipLayer, FipSpecies, FipSite> app = new FipStart()) {
			app.setControlMap(controlMap);

			var spec1 = this.getTestSpecies("test polygon", LayerType.PRIMARY, "PA", spec -> {
				spec.setPercentGenus(25);
			});
			var spec2 = this.getTestSpecies("test polygon", LayerType.PRIMARY, "PL", spec -> {
				spec.setPercentGenus(75);
			});

			Map<String, FipSpecies> allSpecies = new HashMap<>();
			allSpecies.put(spec1.getGenus(), spec1);
			allSpecies.put(spec2.getGenus(), spec2);

			var result = app.findPrimarySpecies(allSpecies);

			assertThat(result, hasSize(1));
			assertThat(
					result, contains(allOf(hasProperty("genus", is("PL")), hasProperty("percentGenus", closeTo(100f))))
			);
		}
	}

	@Test
	void testFindPrimaryCombinePLIntoPA() throws Exception {
		var controlMap = FipTestUtils.loadControlMap();
		try (VdypStartApplication<FipPolygon, FipLayer, FipSpecies, FipSite> app = new FipStart()) {
			app.setControlMap(controlMap);

			var spec1 = this.getTestSpecies("test polygon", LayerType.PRIMARY, "PA", spec -> {
				spec.setPercentGenus(75);
			});
			var spec2 = this.getTestSpecies("test polygon", LayerType.PRIMARY, "PL", spec -> {
				spec.setPercentGenus(25);
			});

			Map<String, FipSpecies> allSpecies = new HashMap<>();
			allSpecies.put(spec1.getGenus(), spec1);
			allSpecies.put(spec2.getGenus(), spec2);

			var result = app.findPrimarySpecies(allSpecies);

			assertThat(result, hasSize(1));
			assertThat(
					result, contains(allOf(hasProperty("genus", is("PA")), hasProperty("percentGenus", closeTo(100f))))
			);
		}
	}

	@Test
	void testFindPrimaryCombineCIntoY() throws Exception {
		var controlMap = FipTestUtils.loadControlMap();
		try (VdypStartApplication<FipPolygon, FipLayer, FipSpecies, FipSite> app = new FipStart()) {
			app.setControlMap(controlMap);

			var spec1 = this.getTestSpecies("test polygon", LayerType.PRIMARY, "C", spec -> {
				spec.setPercentGenus(25);
			});
			var spec2 = this.getTestSpecies("test polygon", LayerType.PRIMARY, "Y", spec -> {
				spec.setPercentGenus(75);
			});

			Map<String, FipSpecies> allSpecies = new HashMap<>();
			allSpecies.put(spec1.getGenus(), spec1);
			allSpecies.put(spec2.getGenus(), spec2);

			var result = app.findPrimarySpecies(allSpecies);

			assertThat(result, hasSize(1));
			assertThat(
					result, contains(allOf(hasProperty("genus", is("Y")), hasProperty("percentGenus", closeTo(100f))))
			);
		}
	}

	@Test
	void testFindPrimaryCombineYIntoC() throws Exception {
		var controlMap = FipTestUtils.loadControlMap();
		try (VdypStartApplication<FipPolygon, FipLayer, FipSpecies, FipSite> app = new FipStart()) {
			app.setControlMap(controlMap);

			var spec1 = this.getTestSpecies("test polygon", LayerType.PRIMARY, "C", spec -> {
				spec.setPercentGenus(75);
			});
			var spec2 = this.getTestSpecies("test polygon", LayerType.PRIMARY, "Y", spec -> {
				spec.setPercentGenus(25);
			});

			Map<String, FipSpecies> allSpecies = new HashMap<>();
			allSpecies.put(spec1.getGenus(), spec1);
			allSpecies.put(spec2.getGenus(), spec2);

			var result = app.findPrimarySpecies(allSpecies);

			assertThat(result, hasSize(1));
			assertThat(
					result, contains(allOf(hasProperty("genus", is("C")), hasProperty("percentGenus", closeTo(100f))))
			);
		}
	}

	@Test
	void testFindPrimarySort() throws Exception {
		var controlMap = FipTestUtils.loadControlMap();
		try (VdypStartApplication<FipPolygon, FipLayer, FipSpecies, FipSite> app = new FipStart()) {
			app.setControlMap(controlMap);

			var spec1 = this.getTestSpecies("test polygon", LayerType.PRIMARY, "B", spec -> {
				spec.setPercentGenus(20);
			});
			var spec2 = this.getTestSpecies("test polygon", LayerType.PRIMARY, "H", spec -> {
				spec.setPercentGenus(70);
			});
			var spec3 = this.getTestSpecies("test polygon", LayerType.PRIMARY, "MB", spec -> {
				spec.setPercentGenus(10);
			});

			Map<String, FipSpecies> allSpecies = new HashMap<>();
			allSpecies.put(spec1.getGenus(), spec1);
			allSpecies.put(spec2.getGenus(), spec2);
			allSpecies.put(spec3.getGenus(), spec3);

			var result = app.findPrimarySpecies(allSpecies);

			assertThat(
					result,
					contains(
							allOf(hasProperty("genus", is("H")), hasProperty("percentGenus", closeTo(70f))),
							allOf(hasProperty("genus", is("B")), hasProperty("percentGenus", closeTo(20f)))
					)
			);
		}
	}

	@Test
	void testFindItg80PercentPure() throws Exception {
		var controlMap = FipTestUtils.loadControlMap();
		try (VdypStartApplication<FipPolygon, FipLayer, FipSpecies, FipSite> app = new FipStart()) {
			app.setControlMap(controlMap);

			var spec1 = this.getTestSpecies("test polygon", LayerType.PRIMARY, "F", spec -> {
				spec.setPercentGenus(80);
			});
			var spec2 = this.getTestSpecies("test polygon", LayerType.PRIMARY, "C", spec -> {
				spec.setPercentGenus(20);
			});

			List<FipSpecies> primarySpecies = List.of(spec1, spec2);

			var result = app.findItg(primarySpecies);

			assertEquals(1, result);
		}
	}

	@Test
	void testFindItgNoSecondary() throws Exception {
		var controlMap = FipTestUtils.loadControlMap();
		try (VdypStartApplication<FipPolygon, FipLayer, FipSpecies, FipSite> app = new FipStart()) {
			app.setControlMap(controlMap);

			var spec1 = this.getTestSpecies("test polygon", LayerType.PRIMARY, "F", spec -> {
				spec.setPercentGenus(100);
			});

			List<FipSpecies> primarySpecies = List.of(spec1);

			var result = app.findItg(primarySpecies);

			assertEquals(1, result);
		}
	}

	List<FipSpecies> primarySecondarySpecies(String primary, String secondary) {
		var spec1 = this.getTestSpecies("test polygon", LayerType.PRIMARY, primary, spec -> {
			spec.setPercentGenus(70);
		});
		var spec2 = this.getTestSpecies("test polygon", LayerType.PRIMARY, secondary, spec -> {
			spec.setPercentGenus(20);
		});

		return List.of(spec1, spec2);
	}

	void assertItgMixed(VdypStartApplication<FipPolygon, FipLayer, FipSpecies, FipSite> app, int expected, String primary, String... secondary) throws ProcessingException {
		for (var sec : secondary) {
			var result = app.findItg(primarySecondarySpecies(primary, sec));
			assertThat(
					result, describedAs("ITG for " + primary + " and " + sec + " should be " + expected, is(expected))
			);
		}
	}

	void assertItgMixed(VdypStartApplication<FipPolygon, FipLayer, FipSpecies, FipSite> app, int expected, String primary, Collection<String> secondary)
			throws ProcessingException {
		for (var sec : secondary) {
			var result = app.findItg(primarySecondarySpecies(primary, sec));
			assertThat(
					result, describedAs("ITG for " + primary + " and " + sec + " should be " + expected, is(expected))
			);
		}
	}

	@Test
	void testFindItgMixed() throws Exception {
		var controlMap = FipTestUtils.loadControlMap();
		VdypStartApplication<FipPolygon, FipLayer, FipSpecies, FipSite> app = new FipStart();
		app.setControlMap(controlMap);

		assertItgMixed(app, 2, "F", /*  */ "Y", "C");
		assertItgMixed(app, 3, "F", /*  */ "B", "H");
		assertItgMixed(app, 3, "F", /*  */ "H");
		assertItgMixed(app, 4, "F", /*  */ "S");
		assertItgMixed(app, 5, "F", /*  */ "PL", "PA");
		assertItgMixed(app, 6, "F", /*  */ "PY");
		assertItgMixed(app, 7, "F", /*  */ "L", "PW");
		assertItgMixed(app, 8, "F", /*  */ FipStart.HARDWOODS);

		assertItgMixed(app, 10, "C", /* */ "Y");
		assertItgMixed(app, 11, "C", /* */ "B", "H", "S");
		assertItgMixed(app, 10, "C", /* */ "PL", "PA", "PY", "L", "PW");
		assertItgMixed(app, 10, "C", /* */ FipStart.HARDWOODS);

		assertItgMixed(app, 10, "Y", /* */ "C");
		assertItgMixed(app, 11, "Y", /* */ "B", "H", "S");
		assertItgMixed(app, 10, "Y", /* */ "PL", "PA", "PY", "L", "PW");
		assertItgMixed(app, 10, "Y", /* */ FipStart.HARDWOODS);

		assertItgMixed(app, 14, "H", /* */ "C", "Y");
		assertItgMixed(app, 15, "H", /* */ "B");
		assertItgMixed(app, 16, "H", /* */ "S");
		assertItgMixed(app, 17, "H", /* */ FipStart.HARDWOODS);
		assertItgMixed(app, 13, "H", /* */ "F", "L", "PA", "PL", "PY");

		assertItgMixed(app, 19, "B", /* */ "C", "Y", "H");
		assertItgMixed(app, 20, "B", /* */ "S", "PL", "PA", "PY", "L", "PW");
		assertItgMixed(app, 20, "B", /* */ FipStart.HARDWOODS);

		assertItgMixed(app, 22, "S", /* */ "F", "L", "PA", "PW", "PY");
		assertItgMixed(app, 23, "S", /* */ "C", "Y", "H");
		assertItgMixed(app, 24, "S", /* */ "B");
		assertItgMixed(app, 25, "S", /* */ "PL");
		assertItgMixed(app, 26, "S", /* */ FipStart.HARDWOODS);

		assertItgMixed(app, 27, "PW", /**/ "B", "C", "F", "H", "L", "PA", "PL", "PY", "S", "Y");
		assertItgMixed(app, 27, "PW", /**/ FipStart.HARDWOODS);

		assertItgMixed(app, 28, "PL", /**/ "PA");
		assertItgMixed(app, 30, "PL", /**/ "B", "C", "H", "S", "Y");
		assertItgMixed(app, 29, "PL", /**/ "F", "PW", "L", "PY");
		assertItgMixed(app, 31, "PL", /**/ FipStart.HARDWOODS);

		assertItgMixed(app, 28, "PA", /**/ "PL");
		assertItgMixed(app, 30, "PA", /**/ "B", "C", "H", "S", "Y");
		assertItgMixed(app, 29, "PA", /**/ "F", "PW", "L", "PY");
		assertItgMixed(app, 31, "PA", /**/ FipStart.HARDWOODS);

		assertItgMixed(app, 32, "PY", /**/ "B", "C", "F", "H", "L", "PA", "PL", "PW", "S", "Y");
		assertItgMixed(app, 32, "PY", /**/ FipStart.HARDWOODS);

		assertItgMixed(app, 33, "L", /* */ "F");
		assertItgMixed(app, 34, "L", /* */ "B", "C", "H", "PA", "PL", "PW", "PY", "S", "Y");
		assertItgMixed(app, 34, "L", /* */ FipStart.HARDWOODS);

		assertItgMixed(app, 35, "AC", /**/ "B", "C", "F", "H", "L", "PA", "PL", "PW", "PY", "S", "Y");
		assertItgMixed(app, 36, "AC", /**/ "AT", "D", "E", "MB");

		assertItgMixed(app, 37, "D", /* */ "B", "C", "F", "H", "L", "PA", "PL", "PW", "PY", "S", "Y");
		assertItgMixed(app, 38, "D", /* */ "AC", "AT", "E", "MB");

		assertItgMixed(app, 39, "MB", /**/ "B", "C", "F", "H", "L", "PA", "PL", "PW", "PY", "S", "Y");
		assertItgMixed(app, 39, "MB", /**/ "AC", "AT", "D", "E");

		assertItgMixed(app, 40, "E", /* */ "B", "C", "F", "H", "L", "PA", "PL", "PW", "PY", "S", "Y");
		assertItgMixed(app, 40, "E", /* */ "AC", "AT", "D", "MB");

		assertItgMixed(app, 41, "AT", /**/ "B", "C", "F", "H", "L", "PA", "PL", "PW", "PY", "S", "Y");
		assertItgMixed(app, 42, "AT", /**/ "AC", "D", "E", "MB");

	}

}
