package ca.bc.gov.nrs.vdyp.io.parse.control;

import java.io.IOException;
import java.util.Map;

import ca.bc.gov.nrs.vdyp.io.FileResolver;
import ca.bc.gov.nrs.vdyp.io.parse.common.ResourceParseException;

/**
 * Modifies a control map
 *
 * @author Kevin Smith, Vivid Solutions
 *
 */
public interface ControlMapModifier {
	void modify(Map<String, Object> control, FileResolver fileResolver) throws ResourceParseException, IOException;
}
