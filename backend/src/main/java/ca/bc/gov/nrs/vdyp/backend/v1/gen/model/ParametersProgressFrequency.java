/*
 * Variable Density Yield Projection
 * API for the Variable Density Yield Projection service
 *
 * The version of the OpenAPI document: 1.0.0
 *
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

package ca.bc.gov.nrs.vdyp.backend.v1.gen.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Identifies how often or when progress will be reported from the application. In the case of a number being supplied,
 * the number indicates the number of polygons to be processed between indications of progress.
 */

@JsonPropertyOrder({})
@jakarta.annotation.Generated(
		value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-11-12T09:52:55.097945-08:00[America/Vancouver]", comments = "Generator version: 7.9.0"
)
public class ParametersProgressFrequency {

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ParametersProgressFrequency parametersProgressFrequency = (ParametersProgressFrequency) o;
		return true;
	}

	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ParametersProgressFrequency {\n");

		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces (except the first line).
	 */
	private String toIndentedString(Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}