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

import java.io.File;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.Valid;

/**
 * ProjectionDcsvPostRequest
 */
@JsonPropertyOrder(
	{ ProjectionDcsvPostRequest.JSON_PROPERTY_PROJECTION_PARAMETERS,
			ProjectionDcsvPostRequest.JSON_PROPERTY_INPUT_DATA }
)
@jakarta.annotation.Generated(
		value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-11-12T09:52:55.097945-08:00[America/Vancouver]", comments = "Generator version: 7.9.0"
)
@RegisterForReflection
public class ProjectionDcsvPostRequest {
	public static final String JSON_PROPERTY_PROJECTION_PARAMETERS = "projectionParameters";
	@JsonProperty(JSON_PROPERTY_PROJECTION_PARAMETERS)
	private Parameters projectionParameters;

	public static final String JSON_PROPERTY_INPUT_DATA = "inputData";
	@JsonProperty(JSON_PROPERTY_INPUT_DATA)
	private File inputData;

	public ProjectionDcsvPostRequest projectionParameters(Parameters projectionParameters) {
		this.projectionParameters = projectionParameters;
		return this;
	}

	/**
	 * Get projectionParameters
	 *
	 * @return projectionParameters
	 **/
	@JsonProperty(value = "projectionParameters")
	@Valid
	public Parameters getProjectionParameters() {
		return projectionParameters;
	}

	public void setProjectionParameters(Parameters projectionParameters) {
		this.projectionParameters = projectionParameters;
	}

	public ProjectionDcsvPostRequest inputData(File inputData) {
		this.inputData = inputData;
		return this;
	}

	/**
	 * a file containing the input data in DCSV format.
	 *
	 * @return inputData
	 **/
	@JsonProperty(value = "inputData")

	public File getInputData() {
		return inputData;
	}

	public void setInputData(File inputData) {
		this.inputData = inputData;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ProjectionDcsvPostRequest projectionDcsvPostRequest = (ProjectionDcsvPostRequest) o;
		return Objects.equals(this.projectionParameters, projectionDcsvPostRequest.projectionParameters)
				&& Objects.equals(this.inputData, projectionDcsvPostRequest.inputData);
	}

	@Override
	public int hashCode() {
		return Objects.hash(projectionParameters, inputData);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ProjectionDcsvPostRequest {\n");

		sb.append("    projectionParameters: ").append(toIndentedString(projectionParameters)).append("\n");
		sb.append("    inputData: ").append(toIndentedString(inputData)).append("\n");
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
