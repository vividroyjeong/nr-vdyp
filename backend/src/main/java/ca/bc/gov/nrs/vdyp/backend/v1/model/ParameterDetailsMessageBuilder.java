package ca.bc.gov.nrs.vdyp.backend.v1.model;

import ca.bc.gov.nrs.vdyp.backend.v1.gen.model.ParameterDetailsMessage;

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
