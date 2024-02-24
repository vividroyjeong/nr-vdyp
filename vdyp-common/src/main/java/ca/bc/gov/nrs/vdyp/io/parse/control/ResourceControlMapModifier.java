package ca.bc.gov.nrs.vdyp.io.parse.control;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import ca.bc.gov.nrs.vdyp.io.FileResolver;
import ca.bc.gov.nrs.vdyp.io.parse.common.ResourceParseException;
import ca.bc.gov.nrs.vdyp.io.parse.value.ValueParser;

/**
 * Modifies the control map based on a resource
 *
 * @author Kevin Smith, Vivid Solutions
 *
 */
public interface ResourceControlMapModifier extends ControlMapModifier, KeyedControlMapModifier {

	void modify(Map<String, Object> control, InputStream data) throws ResourceParseException, IOException;

	/**
	 * Replace the entry in the control map containing the filename for a resource
	 * with the parsed resource
	 *
	 * @param control
	 * @param fileResolver
	 * @throws IOException
	 * @throws ResourceParseException
	 */
	@Override
	default void modify(Map<String, Object> control, FileResolver fileResolver)
			throws IOException, ResourceParseException {
		var filename = (String) control.get(getControlKeyName());
		try (InputStream data = fileResolver.resolveForInput(filename)) {
			modify(control, data);
		}
	}

}
