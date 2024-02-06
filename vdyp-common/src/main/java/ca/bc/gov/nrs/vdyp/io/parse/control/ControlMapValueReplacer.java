package ca.bc.gov.nrs.vdyp.io.parse.control;

import java.io.IOException;
import java.util.Map;

import ca.bc.gov.nrs.vdyp.io.FileResolver;
import ca.bc.gov.nrs.vdyp.io.parse.common.ResourceParseException;

public interface ControlMapValueReplacer<Result, Raw> extends KeyedControlMapModifier {

	/**
	 * Remap a raw control value
	 *
	 * @param rawValue
	 * @return
	 * @throws ResourceParseException
	 * @throws IOException
	 */
	public Result map(Raw rawValue, FileResolver fileResolver, Map<String, Object> control)
			throws ResourceParseException, IOException;

	@SuppressWarnings("unchecked")
	@Override
	default void modify(Map<String, Object> control, FileResolver fileResolver)
			throws ResourceParseException, IOException {

		control.put(getControlKeyName(), this.map((Raw) control.get(this.getControlKeyName()), fileResolver, control));

	}

}
