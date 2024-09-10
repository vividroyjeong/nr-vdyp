package ca.bc.gov.nrs.vdyp.forward.parsers;

import java.util.List;

import ca.bc.gov.nrs.vdyp.forward.model.ForwardControlVariables;
import ca.bc.gov.nrs.vdyp.io.parse.value.ValueParseException;
import ca.bc.gov.nrs.vdyp.io.parse.value.ValueParser;

public class ForwardControlVariableParser implements ValueParser<ForwardControlVariables> {

	@Override
	public ForwardControlVariables parse(String string) throws ValueParseException {

		if (string == null) {
			throw new ValueParseException(null, "VdypControlVariableParser: supplied string is null");
		}
		if (string.trim().length() == 0) {
			throw new ValueParseException(
					string, "VdypControlVariableParser: supplied string \"" + string + "\" is empty"
			);
		}

		var parser = ValueParser.list(ValueParser.INTEGER);
		List<Integer> controlVariableValues = parser.parse(string);

		if (controlVariableValues.isEmpty()) {
			throw new ValueParseException(
					string, "VdypControlVariableParser: supplied string \"" + string + "\" is empty"
			);
		}

		return new ForwardControlVariables(controlVariableValues.toArray(new Integer[controlVariableValues.size()]));
	}
}
