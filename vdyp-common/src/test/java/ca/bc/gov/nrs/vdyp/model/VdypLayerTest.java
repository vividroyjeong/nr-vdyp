package ca.bc.gov.nrs.vdyp.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

class VdypLayerTest {

	@Test
	void build() throws Exception {
		var result = VdypLayer.build(builder -> {
			builder.polygonIdentifier("Test");
			builder.layerType(LayerType.PRIMARY);
			builder.ageTotal(42f);
			builder.yearsToBreastHeight(2f);
			builder.height(10f);
		});
		assertThat(result, hasProperty("polygonIdentifier", is("Test")));
		assertThat(result, hasProperty("layer", is(LayerType.PRIMARY)));
		assertThat(result, hasProperty("ageTotal", is(42f)));
		assertThat(result, hasProperty("yearsToBreastHeight", is(2f)));
		assertThat(result, hasProperty("height", is(10f)));
		assertThat(result, hasProperty("species", anEmptyMap()));
	}

	@Test
	void buildNoProperties() throws Exception {
		var ex = assertThrows(IllegalStateException.class, () -> VdypLayer.build(builder -> {
		}));
		assertThat(
				ex,
				hasProperty(
						"message",
						allOf(
								containsString("polygonIdentifier"), containsString("layer"),
								containsString("ageTotal"), containsString("yearsToBreastHeight"),
								containsString("height")
						)
				)
		);
	}

	@Test
	void buildForPolygon() throws Exception {

		var poly = VdypPolygon.build(builder -> {
			builder.polygonIdentifier("Test");
			builder.percentAvailable(50f);
		});

		var result = VdypLayer.build(poly, builder -> {
			builder.layerType(LayerType.PRIMARY);
			builder.ageTotal(42f);
			builder.yearsToBreastHeight(2f);
			builder.height(10f);
		});

		assertThat(result, hasProperty("polygonIdentifier", is("Test")));
		assertThat(result, hasProperty("layer", is(LayerType.PRIMARY)));
		assertThat(result, hasProperty("ageTotal", is(42f)));
		assertThat(result, hasProperty("yearsToBreastHeight", is(2f)));
		assertThat(result, hasProperty("height", is(10f)));
		assertThat(result, hasProperty("species", anEmptyMap()));

		assertThat(poly.getLayers(), hasEntry(LayerType.PRIMARY, result));
	}

	@Test
	void buildAddSpecies() throws Exception {
		VdypSpecies mock = EasyMock.mock(VdypSpecies.class);
		EasyMock.expect(mock.getLayer()).andStubReturn(LayerType.PRIMARY);
		EasyMock.expect(mock.getGenus()).andStubReturn("B");
		EasyMock.replay(mock);
		var result = VdypLayer.build(builder -> {
			builder.polygonIdentifier("Test");
			builder.layerType(LayerType.PRIMARY);
			builder.ageTotal(42f);
			builder.yearsToBreastHeight(2f);
			builder.height(10f);
			builder.addSpecies(mock);
		});
		assertThat(result, hasProperty("species", hasEntry("B", mock)));
	}

}
