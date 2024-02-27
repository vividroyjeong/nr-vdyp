package ca.bc.gov.nrs.vdyp.forward;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ca.bc.gov.nrs.vdyp.application.VdypApplication;
import ca.bc.gov.nrs.vdyp.application.VdypApplicationIdentifier;
import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.io.FileResolver;
import ca.bc.gov.nrs.vdyp.io.parse.coe.CompVarAdjustmentsParser;
import ca.bc.gov.nrs.vdyp.io.parse.common.ResourceParseException;
import ca.bc.gov.nrs.vdyp.io.parse.control.ControlFileParser;
import ca.bc.gov.nrs.vdyp.io.parse.control.ControlMapSubResourceParser;
import ca.bc.gov.nrs.vdyp.io.parse.control.ControlMapValueReplacer;
import ca.bc.gov.nrs.vdyp.io.parse.control.NonFipControlParser;

/**
 * Parser for VDYP control files
 *
 * @author Michael Junkin, Vivid Solutions
 */
public class VdypForwardControlParser extends NonFipControlParser {

	private final VdypApplication app;

	ControlFileParser controlParser = new ControlFileParser()

			.record(ControlKey.VDYP_POLYGON, FILENAME) //
			.record(ControlKey.VDYP_LAYER_BY_SPECIES, FILENAME) //
			.record(ControlKey.VDYP_LAYER_BY_SP0_BY_UTIL, FILENAME);

	public VdypForwardControlParser(VdypApplication app) {
		super();
		this.app = app;
		assert app.getId() == getProgramId();

		recordAll(parsers);
	}

	List<ControlMapSubResourceParser<?>> parsers = List.of(
			//
			new CompVarAdjustmentsParser()
	);

	@Override
	protected void applyAllModifiers(Map<String, Object> map, FileResolver fileResolver)
			throws ResourceParseException, IOException {

		super.applyAllModifiers(map, fileResolver);

		applyModifiers(map, parsers, fileResolver);
	}

	@Override
	protected List<ControlMapValueReplacer<Object, String>> inputFileParsers() {
		return inputParserList(

				// V7O_VIP
				new VdypPolygonParser(),

				// V7O_VIS
				new VdypSpeciesParser(),

				// V7O_VIU
				new VdypUtilizationParser()
		);
	}

	@Override
	protected List<ControlKey> outputFileParsers() {
		return Collections.emptyList();
	}

	@Override
	protected VdypApplicationIdentifier getProgramId() {
		return VdypApplicationIdentifier.VDYP_FORWARD;
	}

}
