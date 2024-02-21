package ca.bc.gov.nrs.vdyp.io.parse.control;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.io.FileResolver;
import ca.bc.gov.nrs.vdyp.io.parse.coe.BaseAreaCoefficientParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.BecDefinitionParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.BreakageEquationGroupParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.BreakageParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.BySpeciesDqCoefficientParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.CloseUtilVolumeParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.ComponentSizeParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.DecayEquationGroupParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.DefaultEquationNumberParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.EquationModifierParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.GenusDefinitionParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.HLNonprimaryCoefficientParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.HLPrimarySpeciesEqnP1Parser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.HLPrimarySpeciesEqnP2Parser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.HLPrimarySpeciesEqnP3Parser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.QuadMeanDiameterCoefficientParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.SiteCurveAgeMaximumParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.SiteCurveParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.SmallComponentBaseAreaParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.SmallComponentDQParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.SmallComponentHLParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.SmallComponentProbabilityParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.SmallComponentWSVolumeParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.TotalStandWholeStemParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.UpperCoefficientParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.UtilComponentBaseAreaParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.UtilComponentDQParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.UtilComponentWSVolumeParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.VeteranBAParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.VeteranDQParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.VeteranLayerVolumeAdjustParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.VolumeEquationGroupParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.VolumeNetDecayParser;
import ca.bc.gov.nrs.vdyp.io.parse.coe.VolumeNetDecayWasteParser;
import ca.bc.gov.nrs.vdyp.io.parse.common.ResourceParseException;
import ca.bc.gov.nrs.vdyp.io.parse.value.ValueParser;

public abstract class BaseStartAppControlParser {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(BaseStartAppControlParser.class);

	protected static final ValueParser<String> FILENAME = String::strip;

	public static final String MINIMUM_HEIGHT = "MINIMUM_HEIGHT";
	public static final String MINIMUM_BASE_AREA = "MINIMUM_BASE_AREA";
	public static final String MINIMUM_PREDICTED_BASE_AREA = "MINIMUM_PREDICTED_BASE_AREA";
	public static final String MINIMUM_VETERAN_HEIGHT = "MINIMUM_VETERAN_HEIGHT";

	public static final float DEFAULT_MINIMUM_VETERAN_HEIGHT = 10.0f;

	protected ControlFileParser controlParser = new ControlFileParser() //
			.record(ControlKey.MAX_NUM_POLY, ValueParser.INTEGER)

			.record(ControlKey.BEC_DEF, FILENAME) // RD_BECD
			.record(ControlKey.SP0_DEF, FILENAME) // RD_SP0

			// Output Files
			.record(ControlKey.VDYP_POLYGON, FILENAME) //
			.record(ControlKey.VDYP_LAYER_BY_SPECIES, FILENAME) //
			.record(ControlKey.VDYP_LAYER_BY_SP0_BY_UTIL, FILENAME) //

			.record(ControlKey.VOLUME_EQN_GROUPS, FILENAME) // RD_VGRP
			.record(ControlKey.DECAY_GROUPS, FILENAME) // RD_DGRP
			.record(ControlKey.BREAKAGE_GROUPS, FILENAME) // RD_BGRP IPSJF157

			.record(ControlKey.SITE_CURVE_NUMBERS, ValueParser.optional(FILENAME)) // RD_E025
			.record(ControlKey.SITE_CURVE_AGE_MAX, ValueParser.optional(FILENAME)) // RD_E026

			.record(ControlKey.DEFAULT_EQ_NUM, FILENAME) // RD_GRBA1
			.record(ControlKey.EQN_MODIFIERS, FILENAME) // RD_GMBA1

			.record(ControlKey.COE_BA, FILENAME) // RD_E040 IPSJF128
			.record(ControlKey.COE_DQ, FILENAME) // RD_E041 IPSJF129
			.record(ControlKey.UPPER_BA_BY_CI_S0_P, FILENAME) // RD_E043 IPSJF128

			.record(ControlKey.HL_PRIMARY_SP_EQN_P1, FILENAME) // RD_YHL1
			.record(ControlKey.HL_PRIMARY_SP_EQN_P2, FILENAME) // RD_YHL2
			.record(ControlKey.HL_PRIMARY_SP_EQN_P3, FILENAME) // RD_YHL3
			.record(ControlKey.HL_NONPRIMARY, FILENAME) // RD_YHL4

			.record(ControlKey.BY_SPECIES_DQ, FILENAME) // RD_E060 IPFJF125
			.record(ControlKey.SPECIES_COMPONENT_SIZE_LIMIT, FILENAME) // RD_E061 IPSJF158

			.record(ControlKey.UTIL_COMP_BA, FILENAME) // RD_UBA1
			.record(ControlKey.UTIL_COMP_DQ, FILENAME) // RD_UDQ1

			.record(ControlKey.SMALL_COMP_PROBABILITY, FILENAME) // RD_SBA1
			.record(ControlKey.SMALL_COMP_BA, FILENAME) // RD_SBA2
			.record(ControlKey.SMALL_COMP_DQ, FILENAME) // RD_SDQ1
			.record(ControlKey.SMALL_COMP_HL, FILENAME) // RD_SHL1
			.record(ControlKey.SMALL_COMP_WS_VOLUME, FILENAME) // RD_SVT1

			.record(ControlKey.TOTAL_STAND_WHOLE_STEM_VOL, FILENAME) // RD_YVT1 IPSJF117
			.record(ControlKey.UTIL_COMP_WS_VOLUME, FILENAME) // RD_YVT2 IPSJF121
			.record(ControlKey.CLOSE_UTIL_VOLUME, FILENAME) // RD_YVC1 IPSJF122
			.record(ControlKey.VOLUME_NET_DECAY, FILENAME) // RD_YVD1 IPSJF123
			.record(ControlKey.VOLUME_NET_DECAY_WASTE, FILENAME) // RD_YVW1 IPSJF123
			.record(ControlKey.BREAKAGE, FILENAME) // RD_EMP95 IPSJF157

			.record(ControlKey.VETERAN_LAYER_VOLUME_ADJUST, FILENAME) // RD_YVET
			.record(ControlKey.VETERAN_LAYER_DQ, FILENAME) // RD_YDQV
			.record(ControlKey.VETERAN_BQ, FILENAME) // RD_E098

			.record(ControlKey.MODIFIER_FILE, ValueParser.optional(FILENAME)) // RD_E198 IPSJF155, XII

			.record(ControlKey.DEBUG_SWITCHES, ValueParser.list(ValueParser.INTEGER)) // IPSJF155
	;

	protected List<ControlMapModifier> BASIC_DEFINITIONS = Arrays.asList(

			// RD_BEC
			new BecDefinitionParser(),

			// DEF_BEC
			// Superseded by BecLookup.getGrowthBecs

			// RD_SP0
			new GenusDefinitionParser()
	);

	protected List<ControlMapModifier> GROUP_DEFINITIONS = Arrays.asList(

			// RD_VGRP
			new VolumeEquationGroupParser(),

			// RD_DGRP
			new DecayEquationGroupParser(),

			// RD_BGRP
			new BreakageEquationGroupParser(),

			// RD_GRBA1
			new DefaultEquationNumberParser(),

			// RD_GMBA1
			new EquationModifierParser()
	);

	// TODO minima?
	/*
	 * READ(CNTRV(197), 197, ERR= 912 ) FMINH, FMINBA, FMINBAF,FMINVetH IF (FMINVetH
	 * .le. 0.0) FMINVetH=10.0
	 */

	protected List<ControlMapModifier> SITE_CURVES = Arrays.asList(

			// User-assigned SC's (Site Curve Numbers)
			//
			// RD_E025
			new SiteCurveParser(),

			// Max tot ages to apply site curves (by SC)
			//
			// RD_E026
			new SiteCurveAgeMaximumParser()
	);

	protected List<ControlMapModifier> COEFFICIENTS = Arrays.asList(
			new BaseAreaCoefficientParser(),

			new QuadMeanDiameterCoefficientParser(),

			// RD_E043
			new UpperCoefficientParser(),

			new HLPrimarySpeciesEqnP1Parser(),

			new HLPrimarySpeciesEqnP2Parser(),

			new HLPrimarySpeciesEqnP3Parser(),

			// RD_YHL4
			new HLNonprimaryCoefficientParser(),

			// RD_E060
			new BySpeciesDqCoefficientParser(),

			// Min and max DQ by species

			// RD_E061
			new ComponentSizeParser(),

			// RD_UBA1
			new UtilComponentBaseAreaParser(),

			// RD_UDQ1
			new UtilComponentDQParser(),

			// Small Component (4.5 to 7.5 cm)

			// RD_SBA1
			new SmallComponentProbabilityParser(),

			// RD_SBA2
			new SmallComponentBaseAreaParser(),

			// RD_SDQ1
			new SmallComponentDQParser(),

			// RD_SHL1
			new SmallComponentHLParser(),

			// RD_SVT1
			new SmallComponentWSVolumeParser(),

			// Standard Volume Relationships

			// RD_YVT1
			new TotalStandWholeStemParser(),

			// RD_YVT2
			new UtilComponentWSVolumeParser(),

			// RD_YVC1
			new CloseUtilVolumeParser(),

			// RD_YVD1
			new VolumeNetDecayParser(),

			// RD_YVW1
			new VolumeNetDecayWasteParser(),

			// RD_E095
			new BreakageParser(),

			// Veterans

			// RD_YVVET
			new VeteranLayerVolumeAdjustParser(),

			// RD_YDQV
			new VeteranDQParser(),

			// RD_E098
			new VeteranBAParser()
	);

	protected void
			applyModifiers(Map<String, Object> control, List<ControlMapModifier> modifiers, FileResolver fileResolver)
					throws ResourceParseException, IOException {
		for (var modifier : modifiers) {
			modifier.modify(control, fileResolver);
		}
	}

	public Map<String, Object> parse(InputStream is, FileResolver fileResolver, Map<String, Object> map)
			throws IOException, ResourceParseException {
		return parse(List.of(is), fileResolver, map);
	}

	public Map<String, Object> parse(List<InputStream> resources, FileResolver fileResolver, Map<String, Object> map)
			throws IOException, ResourceParseException {

		for (var is : resources) {
			map.putAll(controlParser.parse(is, map));
		}

		applyAllModifiers(map, fileResolver);

		return map;
	}

	protected abstract void applyAllModifiers(Map<String, Object> map, FileResolver fileResolver)
			throws ResourceParseException, IOException;
}
