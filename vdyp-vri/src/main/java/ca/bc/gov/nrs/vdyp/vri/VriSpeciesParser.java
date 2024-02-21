package ca.bc.gov.nrs.vdyp.vri;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.common.ValueOrMarker;
import ca.bc.gov.nrs.vdyp.io.EndOfRecord;
import ca.bc.gov.nrs.vdyp.io.FileResolver;
import ca.bc.gov.nrs.vdyp.io.parse.common.LineParser;
import ca.bc.gov.nrs.vdyp.io.parse.common.ResourceParseException;
import ca.bc.gov.nrs.vdyp.io.parse.common.ResourceParseValidException;
import ca.bc.gov.nrs.vdyp.io.parse.control.ControlMapValueReplacer;
import ca.bc.gov.nrs.vdyp.io.parse.streaming.AbstractStreamingParser;
import ca.bc.gov.nrs.vdyp.io.parse.streaming.GroupingStreamingParser;
import ca.bc.gov.nrs.vdyp.io.parse.streaming.StreamingParserFactory;
import ca.bc.gov.nrs.vdyp.io.parse.value.ControlledValueParser;
import ca.bc.gov.nrs.vdyp.io.parse.value.ValueParser;
import ca.bc.gov.nrs.vdyp.model.LayerType;
import ca.bc.gov.nrs.vdyp.vri.model.VriSpecies;

public class VriSpeciesParser
		implements ControlMapValueReplacer<StreamingParserFactory<VriSpecies>, String> {

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
}
