package ca.bc.gov.nrs.vdyp.io.parse.control;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.vdyp.application.VdypApplicationIdentifier;
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
import ca.bc.gov.nrs.vdyp.io.parse.coe.ModifierParser;
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

public abstract class BaseControlParser {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(BaseControlParser.class);

	protected static final ValueParser<String> FILENAME = String::strip;

	public static final String MINIMUM_HEIGHT = "MINIMUM_HEIGHT";
	public static final String MINIMUM_BASE_AREA = "MINIMUM_BASE_AREA";
	public static final String MINIMUM_PREDICTED_BASE_AREA = "MINIMUM_PREDICTED_BASE_AREA";
	public static final String MINIMUM_FULLY_STOCKED_AREA = "MINIMUM_FULLY_STOCKED_BASE_AREA";
	public static final String MINIMUM_VETERAN_HEIGHT = "MINIMUM_VETERAN_HEIGHT";

	public static final float DEFAULT_MINIMUM_VETERAN_HEIGHT = 10.0f;

	@SuppressWarnings("unchecked")
	protected static List<ControlMapValueReplacer<Object, String>>
			inputParserList(@SuppressWarnings("rawtypes") ControlMapValueReplacer... inputParsers) {
		return Arrays.asList(inputParsers);

	}

	protected ControlFileParser controlParser = new ControlFileParser();

	protected BaseControlParser() {

		controlParser.record(ControlKey.MAX_NUM_POLY, ValueParser.INTEGER);

		inputFileParsers().forEach(
				subResourceParser -> controlParser
						.record(subResourceParser.getControlKey(), subResourceParser.getValueParser())
		);

		outputFileParsers().forEach(key -> controlParser.record(key, ValueParser.FILENAME));

		List.of(basicDefinitions, groupDefinitions, siteCurves, coefficients, additionalModifiers).forEach(group -> {
			recordAll(group);
		});

		controlParser.record(ControlKey.MINIMA, minimaParser());

		controlParser.record(ControlKey.DEBUG_SWITCHES, ValueParser.list(ValueParser.INTEGER)); // IPSJF155

	}

	protected void recordAll(List<? extends ResourceControlMapModifier> group) {
		group.forEach(
				subResourceParser -> controlParser
						.record(subResourceParser.getControlKey(), subResourceParser.getValueParser())
		);
	}

	protected abstract ValueParser<Map<String, Float>> minimaParser();

	protected abstract List<ControlMapValueReplacer<Object, String>> inputFileParsers();

	protected abstract List<ControlKey> outputFileParsers();

	protected List<ControlMapSubResourceParser<?>> basicDefinitions = Arrays.asList(

			// RD_BEC
			new BecDefinitionParser(),

			// DEF_BEC
			// Superseded by BecLookup.getGrowthBecs

			// RD_SP0
			new GenusDefinitionParser()
	);

	protected List<ControlMapSubResourceParser<?>> groupDefinitions = Arrays.asList(

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

	protected List<ControlMapSubResourceParser<?>> siteCurves = Arrays.asList(

			// User-assigned SC's (Site Curve Numbers)
			//
			// RD_E025
			new SiteCurveParser(),

			// Max tot ages to apply site curves (by SC)
			//
			// RD_E026
			new SiteCurveAgeMaximumParser()
	);

	protected List<ControlMapSubResourceParser<?>> coefficients = Arrays.asList(
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

	protected List<OptionalResourceControlMapModifier> additionalModifiers = Arrays.asList(

			// RD_E198
			new ModifierParser(getProgramId())
	);

	protected void applyModifiers(
			Map<String, Object> control, List<? extends ControlMapModifier> modifiers, FileResolver fileResolver
	) throws ResourceParseException, IOException {
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

	protected abstract VdypApplicationIdentifier getProgramId();
}
