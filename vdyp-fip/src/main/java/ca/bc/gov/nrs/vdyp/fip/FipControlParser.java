package ca.bc.gov.nrs.vdyp.fip;

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
import ca.bc.gov.nrs.vdyp.io.parse.coe.StockingClassFactorParser;
import ca.bc.gov.nrs.vdyp.io.parse.common.ResourceParseException;
import ca.bc.gov.nrs.vdyp.io.parse.control.BaseStartAppControlParser;
import ca.bc.gov.nrs.vdyp.io.parse.control.ControlMapModifier;
import ca.bc.gov.nrs.vdyp.io.parse.value.ValueParser;

/**
 * Parser for FIP control files
 *
 * @author Kevin Smith, Vivid Solutions
 *
 */
public class FipControlParser extends BaseStartAppControlParser {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(FipControlParser.class);

	public FipControlParser() {
		this.controlParser //
				// Inputs
				.record(ControlKey.FIP_YIELD_POLY_INPUT, FILENAME) // GET_FIPP
				.record(ControlKey.FIP_YIELD_LAYER_INPUT, FILENAME) // GET_FIPL
				.record(ControlKey.FIP_YIELD_LX_SP0_INPUT, FILENAME) // GET_FIPS

				// FIP only
				.record(ControlKey.STOCKING_CLASS_FACTORS, FILENAME) // RD_STK33

				// FIP/VRI specific execution
				.record(
						ControlKey.FIP_MINIMA,
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
			new FipPolygonParser(),

			// V7O_FIL
			new FipLayerParser(),

			// V7O_FIS
			new FipSpeciesParser()
	);

	List<ControlMapModifier> FIPSTART_ONLY = Arrays.asList(

			// RD_STK33
			new StockingClassFactorParser()
	);

	List<ControlMapModifier> ADDITIONAL_MODIFIERS = Arrays.asList(

			// RD_E198
			new ModifierParser(VdypApplicationIdentifier.VRI_START)
	);

	@Override
	protected void applyAllModifiers(Map<String, Object> map, FileResolver fileResolver)
			throws ResourceParseException, IOException {
		applyModifiers(map, BASIC_DEFINITIONS, fileResolver);

		// Read Groups

		applyModifiers(map, GROUP_DEFINITIONS, fileResolver);

		// Initialize data file parser factories

		applyModifiers(map, DATA_FILES, fileResolver);

		applyModifiers(map, FIPSTART_ONLY, fileResolver);

		applyModifiers(map, SITE_CURVES, fileResolver);

		// Coeff for Empirical relationships

		applyModifiers(map, COEFFICIENTS, fileResolver);

		// Modifiers, IPSJF155-Appendix XII

		// RD_E198
		applyModifiers(map, ADDITIONAL_MODIFIERS, fileResolver);

		// Debug switches (normally zero)
		// TODO

	}

}
