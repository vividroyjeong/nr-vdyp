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

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * MessagesInner
 */
@JsonPropertyOrder(
	{ MessagesInner.JSON_PROPERTY_ID, MessagesInner.JSON_PROPERTY_LAYER_ID,
			MessagesInner.JSON_PROPERTY_STAND_COMPONENT_ID, MessagesInner.JSON_PROPERTY_ERROR_CODE,
			MessagesInner.JSON_PROPERTY_SEVERITY, MessagesInner.JSON_PROPERTY_MESSAGE_CODE,
			MessagesInner.JSON_PROPERTY_MESSAGE }
)
@RegisterForReflection
public class MessagesInner {
	public static final String JSON_PROPERTY_ID = "id";
	@JsonProperty(JSON_PROPERTY_ID)
	private String id;

	public static final String JSON_PROPERTY_LAYER_ID = "layerId";
	@JsonProperty(JSON_PROPERTY_LAYER_ID)
	private String layerId;

	public static final String JSON_PROPERTY_STAND_COMPONENT_ID = "standComponentId";
	@JsonProperty(JSON_PROPERTY_STAND_COMPONENT_ID)
	private String standComponentId;

	public static final String JSON_PROPERTY_ERROR_CODE = "errorCode";
	@JsonProperty(JSON_PROPERTY_ERROR_CODE)
	private String errorCode;

	public static final String JSON_PROPERTY_SEVERITY = "severity";
	@JsonProperty(JSON_PROPERTY_SEVERITY)
	private EnumSeverity severity;

	public static final String JSON_PROPERTY_MESSAGE_CODE = "messageCode";
	@JsonProperty(JSON_PROPERTY_MESSAGE_CODE)
	private String messageCode;

	public static final String JSON_PROPERTY_MESSAGE = "message";
	@JsonProperty(JSON_PROPERTY_MESSAGE)
	private String message;

	public MessagesInner id(String id) {
		this.id = id;
		return this;
	}

	/**
	 * the message's unique identifier
	 *
	 * @return id
	 **/
	@JsonProperty(value = "id")

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MessagesInner layerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	/**
	 * the id of the layer to which the message applies. This value is null if this is a polygon level message and so
	 * does not apply to a specific layer
	 *
	 * @return layerId
	 **/
	@JsonProperty(value = "layerId")

	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	public MessagesInner standComponentId(String standComponentId) {
		this.standComponentId = standComponentId;
		return this;
	}

	/**
	 * the id of the stand component to which the message applies. This value will be null if not known or applicable
	 *
	 * @return standComponentId
	 **/
	@JsonProperty(value = "standComponentId")

	public String getStandComponentId() {
		return standComponentId;
	}

	public void setStandComponentId(String standComponentId) {
		this.standComponentId = standComponentId;
	}

	public MessagesInner errorCode(String errorCode) {
		this.errorCode = errorCode;
		return this;
	}

	/**
	 * the element of the ReturnCode enumeration returned from the operation that resulted in this message being
	 * generated
	 *
	 * @return errorCode
	 **/
	@JsonProperty(value = "errorCode")

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public MessagesInner severity(EnumSeverity severity) {
		this.severity = severity;
		return this;
	}

	/**
	 * Get severity
	 *
	 * @return severity
	 **/
	@JsonProperty(value = "severity")

	public EnumSeverity getSeverity() {
		return severity;
	}

	public void setSeverity(EnumSeverity severity) {
		this.severity = severity;
	}

	public MessagesInner messageCode(String messageCode) {
		this.messageCode = messageCode;
		return this;
	}

	/**
	 * the element of the MessageCode enumeration describing this message
	 *
	 * @return messageCode
	 **/
	@JsonProperty(value = "messageCode")

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public MessagesInner message(String message) {
		this.message = message;
		return this;
	}

	/**
	 * the message contents
	 *
	 * @return message
	 **/
	@JsonProperty(value = "message")

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		MessagesInner messagesInner = (MessagesInner) o;
		return Objects.equals(this.id, messagesInner.id) && Objects.equals(this.layerId, messagesInner.layerId)
				&& Objects.equals(this.standComponentId, messagesInner.standComponentId)
				&& Objects.equals(this.errorCode, messagesInner.errorCode)
				&& Objects.equals(this.severity, messagesInner.severity)
				&& Objects.equals(this.messageCode, messagesInner.messageCode)
				&& Objects.equals(this.message, messagesInner.message);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, layerId, standComponentId, errorCode, severity, messageCode, message);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class MessagesInner {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    layerId: ").append(toIndentedString(layerId)).append("\n");
		sb.append("    standComponentId: ").append(toIndentedString(standComponentId)).append("\n");
		sb.append("    errorCode: ").append(toIndentedString(errorCode)).append("\n");
		sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
		sb.append("    messageCode: ").append(toIndentedString(messageCode)).append("\n");
		sb.append("    message: ").append(toIndentedString(message)).append("\n");
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
