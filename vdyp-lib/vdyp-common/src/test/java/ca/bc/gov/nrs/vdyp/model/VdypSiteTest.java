package ca.bc.gov.nrs.vdyp.model;

import static ca.bc.gov.nrs.vdyp.test.VdypMatchers.isPolyId;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class VdypSiteTest {

	@Test
	void build() throws Exception {
		var result = VdypSite.build(builder -> {
			builder.polygonIdentifier("Test", 2024);
			builder.layerType(LayerType.PRIMARY);
			builder.ageTotal(35.0f);
			builder.height(53.0f);
			builder.siteCurveNumber(22);
			builder.siteGenus("B");
			builder.siteIndex(42.5f);
			builder.yearsToBreastHeight(5.0f);
		});
		assertThat(result, hasProperty("polygonIdentifier", isPolyId("Test", 2024)));
		assertThat(result, hasProperty("layerType", is(LayerType.PRIMARY)));
		assertThat(result, hasProperty("ageTotal", is(Optional.of(35.0f))));
		assertThat(result, hasProperty("height", is(Optional.of(53.0f))));
		assertThat(result, hasProperty("siteCurveNumber", is(Optional.of(22))));
		assertThat(result, hasProperty("siteGenus", is("B")));
		assertThat(result, hasProperty("siteIndex", is(Optional.of(42.5f))));
		assertThat(result, hasProperty("yearsToBreastHeight", is(Optional.of(5.0f))));
		assertThat(result, hasProperty("yearsAtBreastHeight", is(Optional.of(30.0f))));
	}

	@Test
	void buildNoProperties() throws Exception {
		var ex = assertThrows(IllegalStateException.class, () -> VdypSite.build(builder -> {
		}));
		assertThat(
				ex, hasProperty(
						"message", allOf(
								containsString("polygonIdentifier"), containsString("layerType"), containsString(
										"siteGenus"
								)
						)
				)
		);
	}

	@Test
	void buildCopy() throws Exception {
		var result = VdypSite.build(builder -> {
			builder.polygonIdentifier("Test", 2024);
			builder.layerType(LayerType.PRIMARY);
			builder.ageTotal(35.0f);
			builder.height(53.0f);
			builder.siteCurveNumber(22);
			builder.siteGenus("B");
			builder.siteIndex(42.5f);
			builder.yearsToBreastHeight(5.0f);
		});

		var copiedResult = VdypSite.build(builder -> builder.copy(result));

		assertThat(copiedResult, hasProperty("polygonIdentifier", isPolyId("Test", 2024)));
		assertThat(copiedResult, hasProperty("layerType", is(LayerType.PRIMARY)));
		assertThat(copiedResult, hasProperty("ageTotal", is(Optional.of(35.0f))));
		assertThat(copiedResult, hasProperty("height", is(Optional.of(53.0f))));
		assertThat(copiedResult, hasProperty("siteCurveNumber", is(Optional.of(22))));
		assertThat(copiedResult, hasProperty("siteGenus", is("B")));
		assertThat(copiedResult, hasProperty("siteIndex", is(Optional.of(42.5f))));
		assertThat(copiedResult, hasProperty("yearsToBreastHeight", is(Optional.of(5.0f))));
		assertThat(copiedResult, hasProperty("yearsAtBreastHeight", is(Optional.of(30.0f))));
	}
}
