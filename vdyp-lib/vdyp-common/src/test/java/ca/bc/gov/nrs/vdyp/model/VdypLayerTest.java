package ca.bc.gov.nrs.vdyp.model;

import static ca.bc.gov.nrs.vdyp.test.VdypMatchers.isPolyId;
import static ca.bc.gov.nrs.vdyp.test.VdypMatchers.present;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import ca.bc.gov.nrs.vdyp.common.Utils;
import ca.bc.gov.nrs.vdyp.test.TestUtils;

class VdypLayerTest {

	@Test
	void build() throws Exception {
		var result = VdypLayer.build(builder -> {
			builder.polygonIdentifier("Test", 2024);
			builder.layerType(LayerType.PRIMARY);

			builder.addSpecies(specBuilder -> {
				specBuilder.genus("PL", 12);
				specBuilder.percentGenus(100);
				specBuilder.volumeGroup(-1);
				specBuilder.decayGroup(-1);
				specBuilder.breakageGroup(-1);
				specBuilder.addSite(siteBuilder -> {
					siteBuilder.height(10f);
					siteBuilder.ageTotal(42f);
					siteBuilder.yearsToBreastHeight(2f);
					siteBuilder.siteCurveNumber(0);
				});
			});

		});
		assertThat(result, hasProperty("polygonIdentifier", isPolyId("Test", 2024)));
		assertThat(result, hasProperty("layerType", is(LayerType.PRIMARY)));
		assertThat(result, hasProperty("ageTotal", present(is(42f))));
		assertThat(result, hasProperty("yearsToBreastHeight", present(is(2f))));
		assertThat(result, hasProperty("height", present(is(10f))));
		assertThat(result, hasProperty("species", aMapWithSize(1)));
	}

	@Test
	void buildNoProperties() throws Exception {
		var ex = assertThrows(IllegalStateException.class, () -> VdypLayer.build(builder -> {
		}));
		assertThat(ex, hasProperty("message", allOf(containsString("polygonIdentifier"), containsString("layer"))));
	}

	@Test
	void buildForPolygon() throws Exception {

		Map<String, Object> controlMap = new HashMap<>();
		TestUtils.populateControlMapBecReal(controlMap);

		var poly = VdypPolygon.build(builder -> {
			builder.polygonIdentifier("Test", 2024);
			builder.percentAvailable(50f);

			builder.forestInventoryZone("?");
			builder.biogeoclimaticZone(Utils.getBec("IDF", controlMap));
		});

		var result = VdypLayer.build(poly, builder -> {
			builder.layerType(LayerType.PRIMARY);
			builder.addSpecies(specBuilder -> {
				specBuilder.genus("PL", 12);
				specBuilder.percentGenus(100);
				specBuilder.volumeGroup(-1);
				specBuilder.decayGroup(-1);
				specBuilder.breakageGroup(-1);
				specBuilder.addSite(siteBuilder -> {
					siteBuilder.height(10f);
					siteBuilder.ageTotal(42f);
					siteBuilder.yearsToBreastHeight(2f);
					siteBuilder.siteCurveNumber(0);
				});
			});

		});

		assertThat(result, hasProperty("polygonIdentifier", isPolyId("Test", 2024)));
		assertThat(result, hasProperty("layerType", is(LayerType.PRIMARY)));
		assertThat(result, hasProperty("ageTotal", present(is(42f))));
		assertThat(result, hasProperty("yearsToBreastHeight", present(is(2f))));
		assertThat(result, hasProperty("height", present(is(10f))));
		assertThat(result, hasProperty("species", aMapWithSize(1)));

		assertThat(poly.getLayers(), hasEntry(LayerType.PRIMARY, result));
	}

	@Test
	void buildAddSpecies() throws Exception {
		VdypSpecies mock = EasyMock.mock(VdypSpecies.class);
		EasyMock.expect(mock.getLayerType()).andStubReturn(LayerType.PRIMARY);
		EasyMock.expect(mock.getGenus()).andStubReturn("B");
		EasyMock.replay(mock);
		var result = VdypLayer.build(builder -> {
			builder.polygonIdentifier("Test", 2024);
			builder.layerType(LayerType.PRIMARY);
			builder.addSpecies(specBuilder -> {
				specBuilder.genus("PL", 12);
				specBuilder.percentGenus(100);
				specBuilder.volumeGroup(-1);
				specBuilder.decayGroup(-1);
				specBuilder.breakageGroup(-1);
				specBuilder.addSite(siteBuilder -> {
					siteBuilder.height(10f);
					siteBuilder.ageTotal(42f);
					siteBuilder.yearsToBreastHeight(2f);
					siteBuilder.siteCurveNumber(0);
				});
			});

			builder.addSpecies(specBuilder -> {
				specBuilder.genus("B", 3);
				specBuilder.percentGenus(90f);
				specBuilder.volumeGroup(10);
				specBuilder.decayGroup(10);
				specBuilder.breakageGroup(10);
			});
		});
		assertThat(result, hasProperty("species", hasEntry(is("B"), hasProperty("genus", is("B")))));
	}

	@Test
	void buildAdapt() throws Exception {

		var control = EasyMock.createControl();

		BaseVdypLayer<?, ?> toCopy = control.createMock(BaseVdypLayer.class);

		EasyMock.expect(toCopy.getPolygonIdentifier()).andStubReturn(new PolygonIdentifier("Test", 2024));
		EasyMock.expect(toCopy.getLayerType()).andStubReturn(LayerType.PRIMARY);
		EasyMock.expect(toCopy.getInventoryTypeGroup()).andStubReturn(Optional.of(12));

		control.replay();

		var result = VdypLayer.build(builder -> {

			builder.adapt(toCopy);

		});
		assertThat(result, hasProperty("polygonIdentifier", isPolyId("Test", 2024)));
		assertThat(result, hasProperty("layerType", is(LayerType.PRIMARY)));
		assertThat(result, hasProperty("species", anEmptyMap()));
		assertThat(result, hasProperty("inventoryTypeGroup", present(is(12))));

		control.verify();

	}

	@Test
	void buildAdaptSpecies() throws Exception {

		var control = EasyMock.createControl();

		BaseVdypLayer<BaseVdypSpecies<BaseVdypSite>, BaseVdypSite> toCopy = control.createMock(BaseVdypLayer.class);
		BaseVdypSpecies<BaseVdypSite> speciesToCopy = control.createMock(BaseVdypSpecies.class);

		EasyMock.expect(toCopy.getPolygonIdentifier()).andStubReturn(new PolygonIdentifier("Test", 2024));
		EasyMock.expect(speciesToCopy.getPolygonIdentifier()).andStubReturn(new PolygonIdentifier("Test", 2024));
		EasyMock.expect(toCopy.getLayerType()).andStubReturn(LayerType.PRIMARY);
		EasyMock.expect(speciesToCopy.getLayerType()).andStubReturn(LayerType.PRIMARY);
		EasyMock.expect(toCopy.getInventoryTypeGroup()).andStubReturn(Optional.of(12));
		EasyMock.expect(toCopy.getSpecies())
				.andStubReturn(new LinkedHashMap<>(Collections.singletonMap("B", speciesToCopy)));
		EasyMock.expect(speciesToCopy.getGenus()).andStubReturn("B");
		EasyMock.expect(speciesToCopy.getGenusIndex()).andStubReturn(3);
		EasyMock.expect(speciesToCopy.getPercentGenus()).andStubReturn(100f);
		EasyMock.expect(speciesToCopy.getFractionGenus()).andStubReturn(1f);
		EasyMock.expect(speciesToCopy.getSp64DistributionSet()).andStubReturn(
				new Sp64DistributionSet(List.of(new Sp64Distribution(1, "BL", 75f), new Sp64Distribution(2, "BX", 25f)))
		);

		control.replay();

		var result = VdypLayer.build(builder -> {

			builder.adapt(toCopy);
			builder.adaptSpecies(toCopy, (siteBuilder, site) -> {
				siteBuilder.volumeGroup(1);
				siteBuilder.decayGroup(2);
				siteBuilder.breakageGroup(3);
			});

		});
		assertThat(result, hasProperty("species", hasEntry(is("B"), hasProperty("genus", is("B")))));
		var resultSpecies = result.getSpecies().get("B");
		assertThat(resultSpecies, hasProperty("polygonIdentifier", isPolyId("Test", 2024)));
		assertThat(resultSpecies, hasProperty("layerType", is(LayerType.PRIMARY)));
		assertThat(resultSpecies, hasProperty("genus", is("B")));
		assertThat(resultSpecies, hasProperty("percentGenus", is(100f)));
		assertThat(resultSpecies, hasProperty("fractionGenus", is(1f)));
		assertThat(
				resultSpecies, hasProperty(
						"sp64DistributionSet", hasProperty(
								"sp64DistributionMap", hasEntry(
										is(1), allOf(
												hasProperty("genusAlias", is("BL")), hasProperty("percentage", is(75f))
										)
								)
						)
				)
		);

		control.verify();

	}

	@Test
	void buildCopySpecies() throws Exception {

		var toCopy = VdypLayer.build(builder -> {
			builder.polygonIdentifier("Test", 2024);
			builder.layerType(LayerType.PRIMARY);

			builder.addSpecies(speciesBuilder -> {
				speciesBuilder.genus("B", 3);
				speciesBuilder.percentGenus(100f);
				speciesBuilder.fractionGenus(1f);
				speciesBuilder.volumeGroup(1);
				speciesBuilder.decayGroup(2);
				speciesBuilder.breakageGroup(3);
				speciesBuilder.addSp64Distribution("BL", 75f);
				speciesBuilder.addSp64Distribution("BX", 25f);
			});

		});

		var result = VdypLayer.build(builder -> {

			builder.copy(toCopy);
			builder.copySpecies(toCopy, (speciesBuilder, species) -> {
				// Do Nothing
			});

		});

		assertThat(result, hasProperty("species", hasEntry(is("B"), hasProperty("genus", is("B")))));
		var resultSpecies = result.getSpecies().get("B");
		assertThat(resultSpecies, hasProperty("polygonIdentifier", isPolyId("Test", 2024)));
		assertThat(resultSpecies, hasProperty("layerType", is(LayerType.PRIMARY)));
		assertThat(resultSpecies, hasProperty("genus", is("B")));
		assertThat(resultSpecies, hasProperty("percentGenus", is(100f)));
		assertThat(resultSpecies, hasProperty("fractionGenus", is(1f)));
		assertThat(resultSpecies, hasProperty("volumeGroup", is(1)));
		assertThat(resultSpecies, hasProperty("decayGroup", is(2)));
		assertThat(resultSpecies, hasProperty("breakageGroup", is(3)));
		assertThat(
				resultSpecies, hasProperty(
						"sp64DistributionSet", hasProperty(
								"sp64DistributionMap", allOf(
										hasEntry(
												is(1), allOf(
														hasProperty("genusAlias", is("BL")), hasProperty(
																"percentage", is(75f)
														)
												)
										), hasEntry(
												is(2), allOf(
														hasProperty("genusAlias", is("BX")), hasProperty(
																"percentage", is(25f)
														)
												)
										)
								)
						)
				)
		);
	}

}