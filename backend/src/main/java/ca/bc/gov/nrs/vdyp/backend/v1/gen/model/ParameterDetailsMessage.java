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

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * provides details about one parameter
 */

@JsonPropertyOrder(
	{ ParameterDetailsMessage.JSON_PROPERTY_FIELD, ParameterDetailsMessage.JSON_PROPERTY_SHORT_DESCRIPTION,
			ParameterDetailsMessage.JSON_PROPERTY_PARAMETER_VALUE,
			ParameterDetailsMessage.JSON_PROPERTY_LONG_DESCRIPTION,
			ParameterDetailsMessage.JSON_PROPERTY_DEFAULT_VALUE }
)
@jakarta.annotation.Generated(
		value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-11-12T09:52:55.097945-08:00[America/Vancouver]", comments = "Generator version: 7.9.0"
)
public class ParameterDetailsMessage implements Serializable {

	private static final long serialVersionUID = -4514646435523721853L;

	public static final String JSON_PROPERTY_FIELD = "field";
	@JsonProperty(JSON_PROPERTY_FIELD)
	private String field;

	public static final String JSON_PROPERTY_SHORT_DESCRIPTION = "shortDescription";
	@JsonProperty(JSON_PROPERTY_SHORT_DESCRIPTION)
	private String shortDescription;

	public static final String JSON_PROPERTY_PARAMETER_VALUE = "parameterValue";
	@JsonProperty(JSON_PROPERTY_PARAMETER_VALUE)
	private String parameterValue;

	public static final String JSON_PROPERTY_LONG_DESCRIPTION = "longDescription";
	@JsonProperty(JSON_PROPERTY_LONG_DESCRIPTION)
	private String longDescription;

	public static final String JSON_PROPERTY_DEFAULT_VALUE = "defaultValue";
	@JsonProperty(JSON_PROPERTY_DEFAULT_VALUE)
	private String defaultValue;

	public ParameterDetailsMessage field(String field) {
		this.field = field;
		return this;
	}

	/**
	 * the parameter name
	 *
	 * @return field
	 **/
	@JsonProperty(value = "field")

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public ParameterDetailsMessage shortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
		return this;
	}

	/**
	 * a brief description of the parameter's purpose
	 *
	 * @return shortDescription
	 **/
	@JsonProperty(value = "shortDescription")

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public ParameterDetailsMessage parameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
		return this;
	}

	/**
	 * if the parameter has a value, a description of the value
	 *
	 * @return parameterValue
	 **/
	@JsonProperty(value = "parameterValue")

	public String getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

	public ParameterDetailsMessage longDescription(String longDescription) {
		this.longDescription = longDescription;
		return this;
	}

	/**
	 * a description of the parameter
	 *
	 * @return longDescription
	 **/
	@JsonProperty(value = "longDescription")

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public ParameterDetailsMessage defaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}

	/**
	 * the default value used if the parameter is not specified
	 *
	 * @return defaultValue
	 **/
	@JsonProperty(value = "defaultValue")

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ParameterDetailsMessage parameterDetailsMessage = (ParameterDetailsMessage) o;
		return Objects.equals(this.field, parameterDetailsMessage.field)
				&& Objects.equals(this.shortDescription, parameterDetailsMessage.shortDescription)
				&& Objects.equals(this.parameterValue, parameterDetailsMessage.parameterValue)
				&& Objects.equals(this.longDescription, parameterDetailsMessage.longDescription)
				&& Objects.equals(this.defaultValue, parameterDetailsMessage.defaultValue);
	}

	@Override
	public int hashCode() {
		return Objects.hash(field, shortDescription, parameterValue, longDescription, defaultValue);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ParameterDetailsMessage {\n");

		sb.append("    field: ").append(toIndentedString(field)).append("\n");
		sb.append("    shortDescription: ").append(toIndentedString(shortDescription)).append("\n");
		sb.append("    parameterValue: ").append(toIndentedString(parameterValue)).append("\n");
		sb.append("    longDescription: ").append(toIndentedString(longDescription)).append("\n");
		sb.append("    defaultValue: ").append(toIndentedString(defaultValue)).append("\n");
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
