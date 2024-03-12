package ca.bc.gov.nrs.vdyp.vri;

import java.io.IOException;
import java.util.Map;

import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.io.FileResolver;
import ca.bc.gov.nrs.vdyp.io.parse.common.ResourceParseException;
import ca.bc.gov.nrs.vdyp.io.parse.control.ControlMapValueReplacer;
import ca.bc.gov.nrs.vdyp.io.parse.streaming.StreamingParserFactory;
import ca.bc.gov.nrs.vdyp.io.parse.value.ValueParser;
import ca.bc.gov.nrs.vdyp.vri.model.VriLayer;

public class VriLayerParser implements ControlMapValueReplacer<StreamingParserFactory<VriLayer>, String> {

	@Override
	public ControlKey getControlKey() {
		return ControlKey.VRI_INPUT_YIELD_LAYER;
	}

	@Override
	public StreamingParserFactory<VriLayer> map(String fileName, FileResolver fileResolver, Map<String, Object> control)
			throws IOException, ResourceParseException {
		return null; // TODO
	}

	@Override
	public ValueParser<Object> getValueParser() {
		return FILENAME;
	}
}
