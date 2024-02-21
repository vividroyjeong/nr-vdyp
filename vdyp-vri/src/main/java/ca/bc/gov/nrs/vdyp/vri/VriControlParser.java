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
import ca.bc.gov.nrs.vdyp.io.parse.coe.StockingClassFactorParser;
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
	
	/*
	 * Debug switches (25) 0=default See IPSJF155, App IX 1st: 1: Do NOT apply BA
	 * limits from SEQ043 2nd: 1: Do NOT apply DQ limits from SEQ043 4th: Future
	 * Development. Choice of upper limits 9th: 0: Normal - Suppress MATH77 error
	 * messages. 1: show some MATH77 errors; 2: show all. 22nd 1: extra preference
	 * for preferred sp (SEQ 010).
	 */

	;

	public VriControlParser() {
		this.controlParser //
				// Inputs
				.record(ControlKey.VRI_YIELD_POLY_INPUT, FILENAME) // GET_FIPP
				.record(ControlKey.VRI_YIELD_LAYER_INPUT, FILENAME) // GET_FIPL
				.record(ControlKey.VRI_YIELD_HEIGHT_AGE_SI_INPUT, FILENAME) // GET_FIPS
				.record(ControlKey.VRI_YIELD_SPEC_DIST_INPUT, FILENAME) // GET_FIPS

				// FIP only
				.record(ControlKey.STOCKING_CLASS_FACTORS, FILENAME) // RD_STK33

				// FIP/VRI specific execution
				.record(
						ControlKey.MINIMA,
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

	List<ControlMapModifier> FIPSTART_ONLY = Arrays.asList(

			// RD_STK33
			new StockingClassFactorParser()
	);

	List<ControlMapModifier> ADDITIONAL_MODIFIERS = Arrays.asList(

			// RD_E198
			new ModifierParser(VdypApplicationIdentifier.VRIStart)
	);

	
	@Override
	protected void applyAllModifiers(Map<String, Object> map, FileResolver fileResolver) throws ResourceParseException, IOException {
		applyModifiers(map, BASIC_DEFINITIONS, fileResolver);

		// Read Groups

		applyModifiers(map, GROUP_DEFINITIONS, fileResolver);

		// Initialize data file parser factories

		applyModifiers(map, DATA_FILES, fileResolver);

//		if (jprogram == VdypApplicationIdentifier.FIPStart) {
//			applyModifiers(map, FIPSTART_ONLY, fileResolver);
//		}

		applyModifiers(map, SITE_CURVES, fileResolver);

		// Coeff for Empirical relationships

		applyModifiers(map, COEFFICIENTS, fileResolver);

		// Initiation items NOT for FIPSTART

			// RD_E106
			// TODO

			// RD_E107
			// TODO

			// RD_E108
			// TODO

			// Minima again, differently?
			// TODO

			/*
			 * READ(CNTRV(197), 197, ERR= 912 ) VMINH, VMINBA, VMINBAeqn,VMINvetH IF
			 * (VMINVetH .le. 0.0) VMINVetH=10.0
			 */

			// RD_E112
			// Was commented out in Fortran

			// RD_E116
			// Was commented out in Fortran
		//}

		// Modifiers, IPSJF155-Appendix XII

		// RD_E198
		applyModifiers(map, ADDITIONAL_MODIFIERS, fileResolver);

		// Debug switches (normally zero)
		// TODO

	}

}
