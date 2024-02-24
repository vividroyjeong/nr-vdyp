package ca.bc.gov.nrs.vdyp.vri;

import java.io.IOException;
import java.util.Map;

import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.io.FileResolver;
import ca.bc.gov.nrs.vdyp.io.parse.common.ResourceParseException;
import ca.bc.gov.nrs.vdyp.io.parse.control.ControlMapValueReplacer;
import ca.bc.gov.nrs.vdyp.io.parse.streaming.StreamingParserFactory;
import ca.bc.gov.nrs.vdyp.io.parse.value.ValueParser;
import ca.bc.gov.nrs.vdyp.vri.model.VriSpecies;

public class VriSpeciesParser implements ControlMapValueReplacer<StreamingParserFactory<VriSpecies>, String> {

	@Override
	public ControlKey getControlKey() {
		return ControlKey.VRI_YIELD_SPEC_DIST_INPUT;
	}

	@Override
	public StreamingParserFactory<VriSpecies>
			map(String fileName, FileResolver fileResolver, Map<String, Object> control)
					throws IOException, ResourceParseException {
		return null; // TODO

	}

	@Override
	public ValueParser<? extends Object> getValueParser() {
		return ValueParser.FILENAME;
	}
}
