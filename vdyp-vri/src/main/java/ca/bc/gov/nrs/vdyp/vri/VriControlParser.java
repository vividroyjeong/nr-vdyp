package ca.bc.gov.nrs.vdyp.vri;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ca.bc.gov.nrs.vdyp.application.VdypApplicationIdentifier;
import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.io.parse.control.ControlMapValueReplacer;
import ca.bc.gov.nrs.vdyp.io.parse.control.NonFipControlParser;
import ca.bc.gov.nrs.vdyp.io.parse.streaming.StreamingParserFactory;
import ca.bc.gov.nrs.vdyp.io.parse.value.ValueParser;

/**
 * Parser for VRI control files
 *
 * @author Kevin Smith, Vivid Solutions
 *
 */
public class VriControlParser extends NonFipControlParser {

	List<ControlMapValueReplacer<? extends StreamingParserFactory<? extends Object>, String>> dataFiles = Arrays.asList(

			// V7O_FIP
			new VriPolygonParser(),

			// V7O_FIL
			new VriLayerParser(),

			// V7O_FIS
			new VriSpeciesParser()
	);

	@Override
	protected VdypApplicationIdentifier getProgramId() {
		return VdypApplicationIdentifier.VRI_START;
	}

	@Override
	protected List<ControlMapValueReplacer<?, String>> inputFileParsers() {
		return List.of(

				// V7O_FIP
				new VriPolygonParser(),

				// V7O_FIL
				new VriLayerParser(),

				// V7O_FIS
				new VriSpeciesParser()
		);

	}

	@Override
	protected List<ControlKey> outputFileParsers() {
		return List.of(ControlKey.VDYP_POLYGON, ControlKey.VDYP_LAYER_BY_SPECIES, ControlKey.VDYP_LAYER_BY_SP0_BY_UTIL);
	}
}
