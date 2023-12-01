package ca.bc.gov.nrs.vdyp.io.write;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ca.bc.gov.nrs.vdyp.common.ControlKeys;
import ca.bc.gov.nrs.vdyp.io.FileResolver;
import ca.bc.gov.nrs.vdyp.model.FipMode;
import ca.bc.gov.nrs.vdyp.model.Layer;
import ca.bc.gov.nrs.vdyp.model.VdypLayer;
import ca.bc.gov.nrs.vdyp.model.VdypPolygon;
import ca.bc.gov.nrs.vdyp.model.VdypSpecies;
import ca.bc.gov.nrs.vdyp.test.TestUtils;
import ca.bc.gov.nrs.vdyp.test.TestUtils.MockOutputStream;

class VriAdjustInputWriterTest {

	MockOutputStream polyStream;
	MockOutputStream specStream;
	MockOutputStream utilStream;

	FileResolver fileResolver;

	Map<String, Object> controlMap;

	@BeforeEach
	void initStreams() {
		controlMap = new HashMap<String, Object>();
		TestUtils.populateControlMapGenusReal(controlMap);

		polyStream = new TestUtils.MockOutputStream("polygons");
		specStream = new TestUtils.MockOutputStream("species");
		utilStream = new TestUtils.MockOutputStream("utilization");

		controlMap.put(ControlKeys.VDYP_POLYGON, "testPolygonFile");
		controlMap.put(ControlKeys.VDYP_LAYER_BY_SPECIES, "testSpeciesFile");
		controlMap.put(ControlKeys.VDYP_LAYER_BY_SP0_BY_UTIL, "testUtilizationFile");

		fileResolver = new FileResolver() {

			@Override
			public InputStream resolveForInput(String filename) throws IOException {
				fail("Should not be attempting to open for reading");
				return null;
			}

			@Override
			public OutputStream resolveForOutput(String filename) throws IOException {
				switch (filename) {
				case "testPolygonFile":
					return polyStream;
				case "testSpeciesFile":
					return specStream;
				case "testUtilizationFile":
					return utilStream;
				default:
					fail("Unexpected file " + filename + " opened");
				}
				return null;
			}

			@Override
			public String toString(String filename) throws IOException {
				return "TEST:" + filename;
			}

		};

	}

	@Test
	void testClosesGivenStreams() throws IOException {

		var unit = new VriAdjustInputWriter(polyStream, specStream, utilStream, controlMap);

		unit.close();

		polyStream.assertClosed();
		specStream.assertClosed();
		utilStream.assertClosed();

		polyStream.assertContent(emptyString());
		specStream.assertContent(emptyString());
		utilStream.assertContent(emptyString());
	}

	@Test
	void testClosesOpenedStreams() throws IOException {

		var unit = new VriAdjustInputWriter(controlMap, fileResolver);

		unit.close();

		polyStream.assertClosed();
		specStream.assertClosed();
		utilStream.assertClosed();

		polyStream.assertContent(emptyString());
		specStream.assertContent(emptyString());
		utilStream.assertContent(emptyString());
	}

	@Test
	void testWritePolygon() throws IOException {
		var unit = new VriAdjustInputWriter(controlMap, fileResolver);

		VdypPolygon polygon = new VdypPolygon(
				"082E004    615       1988", 90f, "D", "IDF", Optional.of(FipMode.FIPSTART)
		);

		polygon.setItg(28);
		polygon.setGrpBa1(119);

		unit.writePolygon(polygon);

		polyStream.assertContent(is("082E004    615       1988 IDF  D    90 28119  1"));
		specStream.assertContent(emptyString());
		utilStream.assertContent(emptyString());
	}

	@Test
	void testWriteSpecies() throws IOException {
		var unit = new VriAdjustInputWriter(controlMap, fileResolver);

		var polygon = new VdypPolygon("082E004    615       1988", 90f, "D", "IDF", Optional.of(FipMode.FIPSTART));

		polygon.setItg(28);
		polygon.setGrpBa1(119);
		var layer = new VdypLayer("082E004    615       1988", Layer.PRIMARY);

		var species = new VdypSpecies("082E004    615       1988", Layer.PRIMARY, "PL");
		species.setSpeciesPercent(Collections.singletonMap("PL", 100f));

		layer.setSpecies(List.of(species));
		
		layer.setSiteIndex(Optional.of(14.7f));
		layer.setHeight(Optional.of(15f));
		layer.setAgeTotal(Optional.of(60f));
		layer.setBreastHeightAge(Optional.of(51.5f));
		layer.setYearsToBreastHeight(Optional.of(8.5f));
		layer.setSiteGenus(Optional.of("PL"));
		layer.setSiteCurveNumber(Optional.of(0));

		unit.writeSpecies(polygon, layer, species);

		specStream.assertContent(
				is(
						"082E004    615       1988 P 12 PL PL 100.0     0.0     0.0     0.0 14.70 15.00  60.0  51.5   8.5 1  0"
				)
		);
		polyStream.assertContent(emptyString());
		utilStream.assertContent(emptyString());
	}
}
