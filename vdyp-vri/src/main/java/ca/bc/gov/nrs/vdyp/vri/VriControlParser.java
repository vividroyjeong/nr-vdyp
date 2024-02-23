package ca.bc.gov.nrs.vdyp.vri;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.vdyp.application.VdypApplicationIdentifier;
import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.io.FileResolver;
import ca.bc.gov.nrs.vdyp.io.parse.coe.BasalAreaYieldParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.QuadraticMeanDiameterYieldParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.UpperBoundsParser;
import ca.bc.gov.nrs.vdyp.io.parse.common.ResourceParseException;
import ca.bc.gov.nrs.vdyp.io.parse.control.BaseStartAppControlParser;
import ca.bc.gov.nrs.vdyp.io.parse.control.ControlMapModifier;
import ca.bc.gov.nrs.vdyp.io.parse.value.ValueParser;

/**
 * Parser for VRI control files
 *
 * @author Kevin Smith, Vivid Solutions
 *
 */
public class VriControlParser extends BaseStartAppControlParser {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(VriControlParser.class);

	public VriControlParser() {
		this.controlParser //
				// Inputs
				.record(ControlKey.VRI_YIELD_POLY_INPUT, FILENAME) // GET_FIPP
				.record(ControlKey.VRI_YIELD_LAYER_INPUT, FILENAME) // GET_FIPL
				.record(ControlKey.VRI_YIELD_HEIGHT_AGE_SI_INPUT, FILENAME) // GET_FIPS
				.record(ControlKey.VRI_YIELD_SPEC_DIST_INPUT, FILENAME) // GET_FIPS

				.record(ControlKey.BA_YIELD, FILENAME) // COE106
				.record(ControlKey.DQ_YIELD, FILENAME) // COE107
				.record(ControlKey.BA_DQ_UPPER_BOUNDS, FILENAME) // COE108

				.record(
						ControlKey.VRI_MINIMA,
						ValueParser.toMap(
								ValueParser.list(ValueParser.FLOAT),
								Collections.singletonMap(MINIMUM_VETERAN_HEIGHT, 10.0f), MINIMUM_HEIGHT,
								MINIMUM_BASE_AREA, MINIMUM_PREDICTED_BASE_AREA, MINIMUM_VETERAN_HEIGHT
						)
				)

		;
	}

	List<ControlMapModifier> DATA_FILES = Arrays.asList(

			// V7O_FIP
			new VriPolygonParser(),

			// V7O_FIL
			new VriLayerParser(),

			// V7O_FIS
			new VriSpeciesParser()
	);

	List<ControlMapModifier> NON_FIPSTART = Arrays.asList(

			// RD_E106
			new BasalAreaYieldParser(),
			// RD_E106
			new QuadraticMeanDiameterYieldParser(),
			// RD_E106
			new UpperBoundsParser()
	);

	List<ControlMapModifier> ADDITIONAL_MODIFIERS = Arrays.asList(

			// RD_E198
			new ModifierParser(VdypApplicationIdentifier.VRIStart)
	);

	@Override
	protected void applyAllModifiers(Map<String, Object> map, FileResolver fileResolver)
			throws ResourceParseException, IOException {
		applyModifiers(map, BASIC_DEFINITIONS, fileResolver);

		// Read Groups

		applyModifiers(map, GROUP_DEFINITIONS, fileResolver);

		// Initialize data file parser factories

		applyModifiers(map, DATA_FILES, fileResolver);

		applyModifiers(map, SITE_CURVES, fileResolver);

		// Coeff for Empirical relationships

		applyModifiers(map, COEFFICIENTS, fileResolver);

		// Initiation items NOT for FIPSTART

		applyModifiers(map, NON_FIPSTART, fileResolver);

		// RD_E198
		applyModifiers(map, ADDITIONAL_MODIFIERS, fileResolver);

	}

}
