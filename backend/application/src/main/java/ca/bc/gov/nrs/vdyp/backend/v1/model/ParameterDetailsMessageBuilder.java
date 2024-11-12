package ca.bc.gov.nrs.vdyp.backend.v1.model;

public class ParameterDetailsMessageBuilder {
	public static ParameterDetailsMessage build(
			String field, String shortDescription, String parameterValue, String longDescription, String defaultValue
	) {
		ParameterDetailsMessage m = new ParameterDetailsMessage();

		m.setDefaultValue(defaultValue);
		m.setField(field);
		m.setLongDescription(longDescription);
		m.setParameterValue(parameterValue);
		m.setShortDescription(shortDescription);

		return m;
	}
}
