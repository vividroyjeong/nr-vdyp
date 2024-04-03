package ca.bc.gov.nrs.vdyp.vri;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.bc.gov.nrs.vdyp.application.StandProcessingException;
import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.common.Utils;
import ca.bc.gov.nrs.vdyp.io.parse.coe.BasalAreaYieldParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.UpperBoundsParser;
import ca.bc.gov.nrs.vdyp.model.LayerType;
import ca.bc.gov.nrs.vdyp.model.PolygonMode;
import ca.bc.gov.nrs.vdyp.test.MockFileResolver;
import ca.bc.gov.nrs.vdyp.test.TestUtils;
import ca.bc.gov.nrs.vdyp.vri.model.VriLayer;
import ca.bc.gov.nrs.vdyp.vri.model.VriPolygon;
import ca.bc.gov.nrs.vdyp.vri.model.VriSite;

class VriInputValidationTest {

	Map<String, Object> controlMap = new HashMap<>();

	@BeforeEach
	void setupControlMap() {
		TestUtils.populateControlMapBecReal(controlMap);
		TestUtils.populateControlMapGenusReal(controlMap);
		TestUtils.populateControlMapFromResource(controlMap, new BasalAreaYieldParser(), "YLDBA407.COE");
		TestUtils.populateControlMapFromResource(controlMap, new UpperBoundsParser(), "PCT_407.coe");
	}

	@Test
	void testPassValid() throws Exception {
		var app = new VriStart();

		MockFileResolver resolver = dummyInput();

		controlMap.put(ControlKey.MINIMA.name(), Utils.constMap(map -> {
			map.put(VriControlParser.MINIMUM_BASE_AREA, 0f);
			map.put(VriControlParser.MINIMUM_HEIGHT, 6f);
			map.put(VriControlParser.MINIMUM_PREDICTED_BASE_AREA, 2f);
		}));

		app.init(resolver, controlMap);

		var poly = VriPolygon.build(pBuilder -> {
			pBuilder.polygonIdentifier("082F074/0071         2001");
			pBuilder.biogeoclimaticZone("IDF");
			pBuilder.forestInventoryZone(" ");
			pBuilder.yieldFactor(1.0f);
			pBuilder.buildLayer(lBuilder -> {
				lBuilder.layerType(LayerType.PRIMARY);
				((VriLayer.Builder) lBuilder).crownClosure(57.8f);
				((VriLayer.Builder) lBuilder).baseArea(66.0f);
				((VriLayer.Builder) lBuilder).treesPerHectare(850f);
				((VriLayer.Builder) lBuilder).utilization(7.5f);
				((VriLayer.Builder) lBuilder).empiricalRelationshipParameterIndex(76);

				// Sites
				lBuilder.addSite(iBuilder -> {
					iBuilder.siteGenus("B");
					((VriSite.Builder) iBuilder).siteSpecies("BL");
					iBuilder.siteCurveNumber(8);
				});
				lBuilder.addSite(iBuilder -> {
					iBuilder.ageTotal(200);
					iBuilder.height(28.0f);
					iBuilder.siteIndex(14.3f);
					iBuilder.siteGenus("C");
					((VriSite.Builder) iBuilder).siteSpecies("CW");
					iBuilder.yearsToBreastHeight(10.9f);
					((VriSite.Builder) iBuilder).breastHeightAge(189.1f);
					iBuilder.siteCurveNumber(11);
				});
				lBuilder.addSite(iBuilder -> {
					iBuilder.ageTotal(200);
					iBuilder.height(32.0f);
					iBuilder.siteIndex(14.6f);
					iBuilder.siteGenus("H");
					((VriSite.Builder) iBuilder).siteSpecies("HW");
					iBuilder.yearsToBreastHeight(9.7f);
					((VriSite.Builder) iBuilder).breastHeightAge(190.3f);
					iBuilder.siteCurveNumber(37);
				});
				lBuilder.addSite(iBuilder -> {
					iBuilder.siteGenus("S");
					((VriSite.Builder) iBuilder).siteSpecies("SE");
					iBuilder.siteCurveNumber(71);
				});

				// Species
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("B");
					sBuilder.percentGenus(3f);
					sBuilder.addSpecies("BL", 100);
				});
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("C");
					sBuilder.percentGenus(30f);
					sBuilder.addSpecies("CW", 100);
				});
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("H");
					sBuilder.percentGenus(48.9f);
					sBuilder.addSpecies("HW", 100);
				});
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("S");
					sBuilder.percentGenus(18.1f);
					sBuilder.addSpecies("SE", 100);
				});

				((VriLayer.Builder) lBuilder).primaryGenus("H");

			});
			pBuilder.buildLayer(lBuilder -> {
				lBuilder.layerType(LayerType.VETERAN);
				((VriLayer.Builder) lBuilder).crownClosure(0f);
				((VriLayer.Builder) lBuilder).baseArea(0f);
				((VriLayer.Builder) lBuilder).treesPerHectare(0f);
				((VriLayer.Builder) lBuilder).utilization(0f);
			});
		});

		app.checkPolygon(poly);
	}

	@Test
	void testFailPrimarySpeciesDontSumTo100() throws Exception {
		var app = new VriStart();

		MockFileResolver resolver = dummyInput();

		app.init(resolver, controlMap);

		var poly = VriPolygon.build(pBuilder -> {
			pBuilder.polygonIdentifier("082F074/0071         2001");
			pBuilder.biogeoclimaticZone("IDF");
			pBuilder.forestInventoryZone(" ");
			pBuilder.yieldFactor(1.0f);
			pBuilder.buildLayer(lBuilder -> {
				lBuilder.layerType(LayerType.PRIMARY);
				((VriLayer.Builder) lBuilder).crownClosure(57.8f);
				((VriLayer.Builder) lBuilder).baseArea(66.0f);
				((VriLayer.Builder) lBuilder).treesPerHectare(850f);
				((VriLayer.Builder) lBuilder).utilization(7.5f);

				// Sites
				lBuilder.addSite(iBuilder -> {
					iBuilder.siteGenus("B");
					((VriSite.Builder) iBuilder).siteSpecies("BL");
					iBuilder.siteCurveNumber(8);
				});
				lBuilder.addSite(iBuilder -> {
					iBuilder.ageTotal(200);
					iBuilder.height(28.0f);
					iBuilder.siteIndex(14.3f);
					iBuilder.siteGenus("C");
					((VriSite.Builder) iBuilder).siteSpecies("CW");
					iBuilder.yearsToBreastHeight(10.9f);
					((VriSite.Builder) iBuilder).breastHeightAge(189.1f);
					iBuilder.siteCurveNumber(11);
				});
				lBuilder.addSite(iBuilder -> {
					iBuilder.ageTotal(200);
					iBuilder.height(32.0f);
					iBuilder.siteIndex(14.6f);
					iBuilder.siteGenus("H");
					((VriSite.Builder) iBuilder).siteSpecies("HW");
					iBuilder.yearsToBreastHeight(9.7f);
					((VriSite.Builder) iBuilder).breastHeightAge(190.3f);
					iBuilder.siteCurveNumber(37);
				});
				lBuilder.addSite(iBuilder -> {
					iBuilder.siteGenus("S");
					((VriSite.Builder) iBuilder).siteSpecies("SE");
					iBuilder.siteCurveNumber(71);
				});

				// Species
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("B");
					sBuilder.percentGenus(3f);
					sBuilder.addSpecies("BL", 100);
				});
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("C");
					sBuilder.percentGenus(30f);
					sBuilder.addSpecies("CW", 100);
				});
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("H");
					sBuilder.percentGenus(48.7f);
					sBuilder.addSpecies("HW", 100);
				});
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("S");
					sBuilder.percentGenus(18.1f);
					sBuilder.addSpecies("SE", 100);
				});

				((VriLayer.Builder) lBuilder).primaryGenus("H");

			});
			pBuilder.buildLayer(lBuilder -> {
				lBuilder.layerType(LayerType.VETERAN);
				((VriLayer.Builder) lBuilder).crownClosure(0f);
				((VriLayer.Builder) lBuilder).baseArea(0f);
				((VriLayer.Builder) lBuilder).treesPerHectare(0f);
				((VriLayer.Builder) lBuilder).utilization(0f);
			});
		});

		assertThrows(StandProcessingException.class, () -> app.checkPolygon(poly));
	}

	@Test
	void testFailIfMissingPrimary() throws Exception {
		var app = new VriStart();

		MockFileResolver resolver = dummyInput();

		final var polygonId = "Test";
		final var layerType = LayerType.PRIMARY;

		app.init(resolver, controlMap);

		var poly = VriPolygon.build(pBuilder -> {
			pBuilder.polygonIdentifier("082F074/0071         2001");
			pBuilder.biogeoclimaticZone("IDF");
			pBuilder.forestInventoryZone(" ");
			pBuilder.yieldFactor(1.0f);
			pBuilder.buildLayer(lBuilder -> {
				lBuilder.layerType(LayerType.VETERAN);
				((VriLayer.Builder) lBuilder).crownClosure(57.8f);
				((VriLayer.Builder) lBuilder).baseArea(66.0f);
				((VriLayer.Builder) lBuilder).treesPerHectare(850f);
				((VriLayer.Builder) lBuilder).utilization(7.5f);

				// Sites
				lBuilder.addSite(iBuilder -> {
					iBuilder.siteGenus("B");
					((VriSite.Builder) iBuilder).siteSpecies("BL");
					iBuilder.siteCurveNumber(8);
				});
				lBuilder.addSite(iBuilder -> {
					iBuilder.ageTotal(200);
					iBuilder.height(28.0f);
					iBuilder.siteIndex(14.3f);
					iBuilder.siteGenus("C");
					((VriSite.Builder) iBuilder).siteSpecies("CW");
					iBuilder.yearsToBreastHeight(10.9f);
					((VriSite.Builder) iBuilder).breastHeightAge(189.1f);
					iBuilder.siteCurveNumber(11);
				});
				lBuilder.addSite(iBuilder -> {
					iBuilder.ageTotal(200);
					iBuilder.height(32.0f);
					iBuilder.siteIndex(14.6f);
					iBuilder.siteGenus("H");
					((VriSite.Builder) iBuilder).siteSpecies("HW");
					iBuilder.yearsToBreastHeight(9.7f);
					((VriSite.Builder) iBuilder).breastHeightAge(190.3f);
					iBuilder.siteCurveNumber(37);
				});
				lBuilder.addSite(iBuilder -> {
					iBuilder.siteGenus("S");
					((VriSite.Builder) iBuilder).siteSpecies("SE");
					iBuilder.siteCurveNumber(71);
				});

				// Species
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("B");
					sBuilder.percentGenus(3f);
					sBuilder.addSpecies("BL", 100);
				});
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("C");
					sBuilder.percentGenus(30f);
					sBuilder.addSpecies("CW", 100);
				});
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("H");
					sBuilder.percentGenus(48.7f);
					sBuilder.addSpecies("HW", 100);
				});
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("S");
					sBuilder.percentGenus(18.1f);
					sBuilder.addSpecies("SE", 100);
				});

				((VriLayer.Builder) lBuilder).primaryGenus("H");

			});
		});

		assertThrows(StandProcessingException.class, () -> app.checkPolygon(poly));
	}

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

	@Test
	void testFailIfPrimaryModeYoungAndLacksAge() throws Exception {
		var app = new VriStart();

		MockFileResolver resolver = dummyInput();

		controlMap.put(ControlKey.MINIMA.name(), Utils.constMap(map -> {
			map.put(VriControlParser.MINIMUM_BASE_AREA, 0f);
			map.put(VriControlParser.MINIMUM_HEIGHT, 6f);
			map.put(VriControlParser.MINIMUM_PREDICTED_BASE_AREA, 2f);
		}));

		app.init(resolver, controlMap);

		var poly = VriPolygon.build(pBuilder -> {
			pBuilder.polygonIdentifier("082F074/0071         2001");
			pBuilder.biogeoclimaticZone("IDF");
			pBuilder.forestInventoryZone(" ");
			pBuilder.yieldFactor(1.0f);
			pBuilder.modeFip(PolygonMode.YOUNG);
			pBuilder.buildLayer(lBuilder -> {
				lBuilder.layerType(LayerType.PRIMARY);
				((VriLayer.Builder) lBuilder).crownClosure(57.8f);
				((VriLayer.Builder) lBuilder).baseArea(66.0f);
				((VriLayer.Builder) lBuilder).treesPerHectare(850f);
				((VriLayer.Builder) lBuilder).utilization(7.5f);

				// Sites
				lBuilder.addSite(iBuilder -> {
					iBuilder.siteGenus("B");
					((VriSite.Builder) iBuilder).siteSpecies("BL");
					iBuilder.siteCurveNumber(8);
				});
				lBuilder.addSite(iBuilder -> {
					iBuilder.ageTotal(200);
					iBuilder.height(28.0f);
					iBuilder.siteIndex(14.3f);
					iBuilder.siteGenus("C");
					((VriSite.Builder) iBuilder).siteSpecies("CW");
					iBuilder.yearsToBreastHeight(10.9f);
					((VriSite.Builder) iBuilder).breastHeightAge(189.1f);
					iBuilder.siteCurveNumber(11);
				});
				lBuilder.addSite(iBuilder -> {
					// iBuilder.ageTotal(200); // don't include age on primary species
					iBuilder.height(32.0f);
					iBuilder.siteIndex(14.6f);
					iBuilder.siteGenus("H");
					((VriSite.Builder) iBuilder).siteSpecies("HW");
					// iBuilder.yearsToBreastHeight(9.7f);
					// ((VriSite.Builder) iBuilder).breastHeightAge(190.3f);
					iBuilder.siteCurveNumber(37);
				});
				lBuilder.addSite(iBuilder -> {
					iBuilder.siteGenus("S");
					((VriSite.Builder) iBuilder).siteSpecies("SE");
					iBuilder.siteCurveNumber(71);
				});

				// Species
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("B");
					sBuilder.percentGenus(3f);
					sBuilder.addSpecies("BL", 100);
				});
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("C");
					sBuilder.percentGenus(30f);
					sBuilder.addSpecies("CW", 100);
				});
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("H");
					sBuilder.percentGenus(48.9f);
					sBuilder.addSpecies("HW", 100);
				});
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("S");
					sBuilder.percentGenus(18.1f);
					sBuilder.addSpecies("SE", 100);
				});

				((VriLayer.Builder) lBuilder).primaryGenus("H");
			});
			pBuilder.buildLayer(lBuilder -> {
				lBuilder.layerType(LayerType.VETERAN);
				((VriLayer.Builder) lBuilder).crownClosure(0f);
				((VriLayer.Builder) lBuilder).baseArea(0f);
				((VriLayer.Builder) lBuilder).treesPerHectare(0f);
				((VriLayer.Builder) lBuilder).utilization(0f);
			});
		});

		assertThrows(StandProcessingException.class, () -> app.checkPolygon(poly));
	}

	@Test
	void testFailIfPrimaryModeYoungAndLacksTreesPerHectare() throws Exception {
		var app = new VriStart();

		MockFileResolver resolver = dummyInput();

		controlMap.put(ControlKey.MINIMA.name(), Utils.constMap(map -> {
			map.put(VriControlParser.MINIMUM_BASE_AREA, 0f);
			map.put(VriControlParser.MINIMUM_HEIGHT, 6f);
			map.put(VriControlParser.MINIMUM_PREDICTED_BASE_AREA, 2f);
		}));

		app.init(resolver, controlMap);

		var poly = VriPolygon.build(pBuilder -> {
			pBuilder.polygonIdentifier("082F074/0071         2001");
			pBuilder.biogeoclimaticZone("IDF");
			pBuilder.forestInventoryZone(" ");
			pBuilder.yieldFactor(1.0f);
			pBuilder.modeFip(PolygonMode.YOUNG);
			pBuilder.buildLayer(lBuilder -> {
				lBuilder.layerType(LayerType.PRIMARY);
				((VriLayer.Builder) lBuilder).crownClosure(57.8f);
				((VriLayer.Builder) lBuilder).baseArea(66.0f);
				// ((VriLayer.Builder) lBuilder).treesPerHectare(850f);
				((VriLayer.Builder) lBuilder).utilization(7.5f);

				// Sites
				lBuilder.addSite(iBuilder -> {
					iBuilder.siteGenus("B");
					((VriSite.Builder) iBuilder).siteSpecies("BL");
					iBuilder.siteCurveNumber(8);
				});
				lBuilder.addSite(iBuilder -> {
					iBuilder.ageTotal(200);
					iBuilder.height(28.0f);
					iBuilder.siteIndex(14.3f);
					iBuilder.siteGenus("C");
					((VriSite.Builder) iBuilder).siteSpecies("CW");
					iBuilder.yearsToBreastHeight(10.9f);
					((VriSite.Builder) iBuilder).breastHeightAge(189.1f);
					iBuilder.siteCurveNumber(11);
				});
				lBuilder.addSite(iBuilder -> {
					iBuilder.ageTotal(200);
					iBuilder.height(32.0f);
					iBuilder.siteIndex(14.6f);
					iBuilder.siteGenus("H");
					((VriSite.Builder) iBuilder).siteSpecies("HW");
					iBuilder.yearsToBreastHeight(9.7f);
					((VriSite.Builder) iBuilder).breastHeightAge(190.3f);
					iBuilder.siteCurveNumber(37);
				});
				lBuilder.addSite(iBuilder -> {
					iBuilder.siteGenus("S");
					((VriSite.Builder) iBuilder).siteSpecies("SE");
					iBuilder.siteCurveNumber(71);
				});

				// Species
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("B");
					sBuilder.percentGenus(3f);
					sBuilder.addSpecies("BL", 100);
				});
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("C");
					sBuilder.percentGenus(30f);
					sBuilder.addSpecies("CW", 100);
				});
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("H");
					sBuilder.percentGenus(48.9f);
					sBuilder.addSpecies("HW", 100);
				});
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("S");
					sBuilder.percentGenus(18.1f);
					sBuilder.addSpecies("SE", 100);
				});

				((VriLayer.Builder) lBuilder).primaryGenus("H");
			});
			pBuilder.buildLayer(lBuilder -> {
				lBuilder.layerType(LayerType.VETERAN);
				((VriLayer.Builder) lBuilder).crownClosure(0f);
				((VriLayer.Builder) lBuilder).baseArea(0f);
				((VriLayer.Builder) lBuilder).treesPerHectare(0f);
				((VriLayer.Builder) lBuilder).utilization(0f);
			});
		});

		assertThrows(StandProcessingException.class, () -> app.checkPolygon(poly));
	}

	@Test
	void testValidPrimaryModeYoung() throws Exception {
		var app = new VriStart();

		MockFileResolver resolver = dummyInput();

		controlMap.put(ControlKey.MINIMA.name(), Utils.constMap(map -> {
			map.put(VriControlParser.MINIMUM_BASE_AREA, 0f);
			map.put(VriControlParser.MINIMUM_HEIGHT, 6f);
			map.put(VriControlParser.MINIMUM_PREDICTED_BASE_AREA, 2f);
		}));

		app.init(resolver, controlMap);

		var poly = VriPolygon.build(pBuilder -> {
			pBuilder.polygonIdentifier("082F074/0071         2001");
			pBuilder.biogeoclimaticZone("IDF");
			pBuilder.forestInventoryZone(" ");
			pBuilder.yieldFactor(1.0f);
			pBuilder.modeFip(PolygonMode.YOUNG);
			pBuilder.buildLayer(lBuilder -> {
				lBuilder.layerType(LayerType.PRIMARY);
				((VriLayer.Builder) lBuilder).crownClosure(57.8f);
				((VriLayer.Builder) lBuilder).baseArea(66.0f);
				((VriLayer.Builder) lBuilder).treesPerHectare(850f);
				((VriLayer.Builder) lBuilder).utilization(7.5f);
				((VriLayer.Builder) lBuilder).empiricalRelationshipParameterIndex(76);

				// Sites
				lBuilder.addSite(iBuilder -> {
					iBuilder.siteGenus("B");
					((VriSite.Builder) iBuilder).siteSpecies("BL");
					iBuilder.siteCurveNumber(8);
				});
				lBuilder.addSite(iBuilder -> {
					iBuilder.ageTotal(200);
					iBuilder.height(28.0f);
					iBuilder.siteIndex(14.3f);
					iBuilder.siteGenus("C");
					((VriSite.Builder) iBuilder).siteSpecies("CW");
					iBuilder.yearsToBreastHeight(10.9f);
					((VriSite.Builder) iBuilder).breastHeightAge(189.1f);
					iBuilder.siteCurveNumber(11);
				});
				lBuilder.addSite(iBuilder -> {
					iBuilder.ageTotal(200);
					iBuilder.height(32.0f);
					iBuilder.siteIndex(14.6f);
					iBuilder.siteGenus("H");
					((VriSite.Builder) iBuilder).siteSpecies("HW");
					iBuilder.yearsToBreastHeight(9.7f);
					((VriSite.Builder) iBuilder).breastHeightAge(190.3f);
					iBuilder.siteCurveNumber(37);
				});
				lBuilder.addSite(iBuilder -> {
					iBuilder.siteGenus("S");
					((VriSite.Builder) iBuilder).siteSpecies("SE");
					iBuilder.siteCurveNumber(71);
				});

				// Species
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("B");
					sBuilder.percentGenus(3f);
					sBuilder.addSpecies("BL", 100);
				});
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("C");
					sBuilder.percentGenus(30f);
					sBuilder.addSpecies("CW", 100);
				});
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("H");
					sBuilder.percentGenus(48.9f);
					sBuilder.addSpecies("HW", 100);
				});
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("S");
					sBuilder.percentGenus(18.1f);
					sBuilder.addSpecies("SE", 100);
				});

				((VriLayer.Builder) lBuilder).primaryGenus("H");
			});
			pBuilder.buildLayer(lBuilder -> {
				lBuilder.layerType(LayerType.VETERAN);
				((VriLayer.Builder) lBuilder).crownClosure(0f);
				((VriLayer.Builder) lBuilder).baseArea(0f);
				((VriLayer.Builder) lBuilder).treesPerHectare(0f);
				((VriLayer.Builder) lBuilder).utilization(0f);
			});
		});

		app.checkPolygon(poly);
	}

	@Test
	void testValidPrimaryModeStart() throws Exception {
		var app = new VriStart();

		MockFileResolver resolver = dummyInput();

		controlMap.put(ControlKey.MINIMA.name(), Utils.constMap(map -> {
			map.put(VriControlParser.MINIMUM_BASE_AREA, 0f);
			map.put(VriControlParser.MINIMUM_HEIGHT, 6f);
			map.put(VriControlParser.MINIMUM_PREDICTED_BASE_AREA, 2f);
		}));

		app.init(resolver, controlMap);

		var poly = VriPolygon.build(pBuilder -> {
			pBuilder.polygonIdentifier("082F074/0071         2001");
			pBuilder.biogeoclimaticZone("IDF");
			pBuilder.forestInventoryZone(" ");
			pBuilder.yieldFactor(1.0f);
			pBuilder.modeFip(PolygonMode.START);
			pBuilder.buildLayer(lBuilder -> {
				lBuilder.layerType(LayerType.PRIMARY);
				((VriLayer.Builder) lBuilder).crownClosure(57.8f);
				((VriLayer.Builder) lBuilder).baseArea(66.0f);
				((VriLayer.Builder) lBuilder).treesPerHectare(850f);
				((VriLayer.Builder) lBuilder).utilization(7.5f);
				((VriLayer.Builder) lBuilder).empiricalRelationshipParameterIndex(76);

				// Sites
				lBuilder.addSite(iBuilder -> {
					iBuilder.siteGenus("B");
					((VriSite.Builder) iBuilder).siteSpecies("BL");
					iBuilder.siteCurveNumber(8);
				});
				lBuilder.addSite(iBuilder -> {
					iBuilder.ageTotal(200);
					iBuilder.height(28.0f);
					iBuilder.siteIndex(14.3f);
					iBuilder.siteGenus("C");
					((VriSite.Builder) iBuilder).siteSpecies("CW");
					iBuilder.yearsToBreastHeight(10.9f);
					((VriSite.Builder) iBuilder).breastHeightAge(189.1f);
					iBuilder.siteCurveNumber(11);
				});
				lBuilder.addSite(iBuilder -> {
					iBuilder.ageTotal(200);
					iBuilder.height(32.0f);
					iBuilder.siteIndex(14.6f);
					iBuilder.siteGenus("H");
					((VriSite.Builder) iBuilder).siteSpecies("HW");
					iBuilder.yearsToBreastHeight(9.7f);
					((VriSite.Builder) iBuilder).breastHeightAge(190.3f);
					iBuilder.siteCurveNumber(37);
				});
				lBuilder.addSite(iBuilder -> {
					iBuilder.siteGenus("S");
					((VriSite.Builder) iBuilder).siteSpecies("SE");
					iBuilder.siteCurveNumber(71);
				});

				// Species
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("B");
					sBuilder.percentGenus(3f);
					sBuilder.addSpecies("BL", 100);
				});
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("C");
					sBuilder.percentGenus(30f);
					sBuilder.addSpecies("CW", 100);
				});
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("H");
					sBuilder.percentGenus(48.9f);
					sBuilder.addSpecies("HW", 100);
				});
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("S");
					sBuilder.percentGenus(18.1f);
					sBuilder.addSpecies("SE", 100);
				});

				((VriLayer.Builder) lBuilder).primaryGenus("H");
			});
			pBuilder.buildLayer(lBuilder -> {
				lBuilder.layerType(LayerType.VETERAN);
				((VriLayer.Builder) lBuilder).crownClosure(0f);
				((VriLayer.Builder) lBuilder).baseArea(0f);
				((VriLayer.Builder) lBuilder).treesPerHectare(0f);
				((VriLayer.Builder) lBuilder).utilization(0f);
			});
		});

		app.checkPolygon(poly);
	}

	@Test
	void testFailIfPrimaryModeStartMissingHeight() throws Exception {
		var app = new VriStart();

		MockFileResolver resolver = dummyInput();

		controlMap.put(ControlKey.MINIMA.name(), Utils.constMap(map -> {
			map.put(VriControlParser.MINIMUM_BASE_AREA, 0f);
			map.put(VriControlParser.MINIMUM_HEIGHT, 6f);
			map.put(VriControlParser.MINIMUM_PREDICTED_BASE_AREA, 2f);
		}));

		app.init(resolver, controlMap);

		var poly = VriPolygon.build(pBuilder -> {
			pBuilder.polygonIdentifier("082F074/0071         2001");
			pBuilder.biogeoclimaticZone("IDF");
			pBuilder.forestInventoryZone(" ");
			pBuilder.yieldFactor(1.0f);
			pBuilder.modeFip(PolygonMode.START);
			pBuilder.buildLayer(lBuilder -> {
				lBuilder.layerType(LayerType.PRIMARY);
				((VriLayer.Builder) lBuilder).crownClosure(57.8f);
				((VriLayer.Builder) lBuilder).baseArea(66.0f);
				((VriLayer.Builder) lBuilder).treesPerHectare(850f);
				((VriLayer.Builder) lBuilder).utilization(7.5f);

				// Sites
				lBuilder.addSite(iBuilder -> {
					iBuilder.siteGenus("B");
					((VriSite.Builder) iBuilder).siteSpecies("BL");
					iBuilder.siteCurveNumber(8);
				});
				lBuilder.addSite(iBuilder -> {
					iBuilder.ageTotal(200);
					iBuilder.height(28.0f);
					iBuilder.siteIndex(14.3f);
					iBuilder.siteGenus("C");
					((VriSite.Builder) iBuilder).siteSpecies("CW");
					iBuilder.yearsToBreastHeight(10.9f);
					((VriSite.Builder) iBuilder).breastHeightAge(189.1f);
					iBuilder.siteCurveNumber(11);
				});
				lBuilder.addSite(iBuilder -> {
					iBuilder.ageTotal(200);
					// iBuilder.height(32.0f); // Remove height from primary species
					iBuilder.siteIndex(14.6f);
					iBuilder.siteGenus("H");
					((VriSite.Builder) iBuilder).siteSpecies("HW");
					iBuilder.yearsToBreastHeight(9.7f);
					((VriSite.Builder) iBuilder).breastHeightAge(190.3f);
					iBuilder.siteCurveNumber(37);
				});
				lBuilder.addSite(iBuilder -> {
					iBuilder.siteGenus("S");
					((VriSite.Builder) iBuilder).siteSpecies("SE");
					iBuilder.siteCurveNumber(71);
				});

				// Species
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("B");
					sBuilder.percentGenus(3f);
					sBuilder.addSpecies("BL", 100);
				});
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("C");
					sBuilder.percentGenus(30f);
					sBuilder.addSpecies("CW", 100);
				});
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("H");
					sBuilder.percentGenus(48.9f);
					sBuilder.addSpecies("HW", 100);
				});
				lBuilder.addSpecies(sBuilder -> {
					sBuilder.genus("S");
					sBuilder.percentGenus(18.1f);
					sBuilder.addSpecies("SE", 100);
				});

				((VriLayer.Builder) lBuilder).primaryGenus("H");
			});
			pBuilder.buildLayer(lBuilder -> {
				lBuilder.layerType(LayerType.VETERAN);
				((VriLayer.Builder) lBuilder).crownClosure(0f);
				((VriLayer.Builder) lBuilder).baseArea(0f);
				((VriLayer.Builder) lBuilder).treesPerHectare(0f);
				((VriLayer.Builder) lBuilder).utilization(0f);
			});
		});

		assertThrows(StandProcessingException.class, () -> app.checkPolygon(poly));
	}

}
