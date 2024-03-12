package ca.bc.gov.nrs.vdyp.vri;

import java.io.IOException;
import java.util.Map;

import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.io.FileResolver;
import ca.bc.gov.nrs.vdyp.io.parse.common.ResourceParseException;
import ca.bc.gov.nrs.vdyp.io.parse.control.ControlMapValueReplacer;
import ca.bc.gov.nrs.vdyp.io.parse.streaming.StreamingParserFactory;
import ca.bc.gov.nrs.vdyp.io.parse.value.ValueParser;
import ca.bc.gov.nrs.vdyp.vri.model.VriPolygon;

public class VriPolygonParser implements ControlMapValueReplacer<StreamingParserFactory<VriPolygon>, String> {

	@Override
	public ControlKey getControlKey() {
		return ControlKey.FIP_INPUT_YIELD_POLY;
	}

	@Override
	public StreamingParserFactory<VriPolygon>
			map(String fileName, FileResolver fileResolver, Map<String, Object> control)
					throws IOException, ResourceParseException {
		return null; // TODO
	}

	@Override
	public ValueParser<Object> getValueParser() {
		return FILENAME;
	}
}
