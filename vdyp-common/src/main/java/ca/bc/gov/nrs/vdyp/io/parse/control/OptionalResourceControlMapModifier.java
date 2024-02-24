package ca.bc.gov.nrs.vdyp.io.parse.control;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

import ca.bc.gov.nrs.vdyp.io.FileResolver;
import ca.bc.gov.nrs.vdyp.io.parse.common.ResourceParseException;
import ca.bc.gov.nrs.vdyp.io.parse.value.ValueParser;

/**
 * Modify a control map based on a resource referenced within it if it is
 * specified, otherwise perform a default operation.
 *
 * @author Kevin Smith, Vivid Solutions
 *
 */
public interface OptionalResourceControlMapModifier extends ResourceControlMapModifier {

	@Override
	default void modify(Map<String, Object> control, FileResolver fileResolver)
			throws IOException, ResourceParseException {
		@SuppressWarnings("unchecked")
		var filename = (Optional<String>) control.get(getControlKeyName());
		if (filename.isPresent()) {
			try (InputStream data = fileResolver.resolveForInput(filename.get())) {
				modify(control, data);
			}
		} else {
			defaultModify(control);
		}

	}

	/**
	 * Perform this operation on the control map if no resource is specified.
	 *
	 * @param control
	 */
	void defaultModify(Map<String, Object> control);

	@Override
	default ValueParser<? extends Object> getValueParser() {
		return ValueParser.optional(ValueParser.FILENAME);
	}

}
