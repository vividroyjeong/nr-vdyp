package ca.bc.gov.nrs.vdyp.si32;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.CodeErrorException;
import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.CommonCalculatorException;
import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.CurveErrorException;
import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.ForestInventoryZoneException;
import ca.bc.gov.nrs.vdyp.si32.enumerations.BECZone;
import ca.bc.gov.nrs.vdyp.si32.enumerations.CFSBiomassConversionSupportedGenera;
import ca.bc.gov.nrs.vdyp.si32.enumerations.CFSBiomassConversionSupportedSpecies;
import ca.bc.gov.nrs.vdyp.si32.enumerations.CFSDeadConversionParams;
import ca.bc.gov.nrs.vdyp.si32.enumerations.CFSDensity;
import ca.bc.gov.nrs.vdyp.si32.enumerations.CFSLiveConversionParams;
import ca.bc.gov.nrs.vdyp.si32.enumerations.CFSTreeClass;
import ca.bc.gov.nrs.vdyp.si32.enumerations.CFSTreeGenus;
import ca.bc.gov.nrs.vdyp.si32.enumerations.CFSTreeSpecies;
import ca.bc.gov.nrs.vdyp.si32.enumerations.NameFormat;
import ca.bc.gov.nrs.vdyp.si32.enumerations.SP0Name;
import ca.bc.gov.nrs.vdyp.si32.enumerations.SP64Name;
import ca.bc.gov.nrs.vdyp.si32.enumerations.SpeciesRegion;
import ca.bc.gov.nrs.vdyp.sindex.Reference;
import ca.bc.gov.nrs.vdyp.sindex.Sindxdll;

public class Vdyp32Dll {
	private static final Logger logger = LoggerFactory.getLogger(Vdyp32Dll.class);

	private static final String UNKNOWN_BEC_ZONE_TEXT = "????";
	private static final int SI_MAX_CURVES = 123;

	private static SpeciesTable speciesTable;

	private static final Map<String, BECZone> becZoneToIndexMap = new HashMap<>();

	static {
		becZoneToIndexMap.put("AT", BECZone.bec_AT);
		becZoneToIndexMap.put("BG", BECZone.bec_BG);
		becZoneToIndexMap.put("BWBS", BECZone.bec_BWBS);
		becZoneToIndexMap.put("CDF", BECZone.bec_CDF);
		becZoneToIndexMap.put("CWH", BECZone.bec_CWH);
		becZoneToIndexMap.put("ESSF", BECZone.bec_ESSF);
		becZoneToIndexMap.put("ICH", BECZone.bec_ICH);
		becZoneToIndexMap.put("IDF", BECZone.bec_IDF);
		becZoneToIndexMap.put("MH", BECZone.bec_MH);
		becZoneToIndexMap.put("MS", BECZone.bec_MS);
		becZoneToIndexMap.put("PP", BECZone.bec_PP);
		becZoneToIndexMap.put("SBSP", BECZone.bec_SBPS);
		becZoneToIndexMap.put("SBS", BECZone.bec_SBS);
		becZoneToIndexMap.put("SWB", BECZone.bec_SWB);
	}

	private static final Map<BECZone, String> enumToBecZoneMap = new HashMap<>();

	static {
		enumToBecZoneMap.put(BECZone.bec_AT, "AT");
		enumToBecZoneMap.put(BECZone.bec_BG, "BG");
		enumToBecZoneMap.put(BECZone.bec_BWBS, "BWBS");
		enumToBecZoneMap.put(BECZone.bec_CDF, "CDF");
		enumToBecZoneMap.put(BECZone.bec_CWH, "CWH");
		enumToBecZoneMap.put(BECZone.bec_ESSF, "ESSF");
		enumToBecZoneMap.put(BECZone.bec_ICH, "ICH");
		enumToBecZoneMap.put(BECZone.bec_IDF, "IDF");
		enumToBecZoneMap.put(BECZone.bec_MH, "MH");
		enumToBecZoneMap.put(BECZone.bec_MS, "MS");
		enumToBecZoneMap.put(BECZone.bec_PP, "PP");
		enumToBecZoneMap.put(BECZone.bec_SBPS, "SBSP");
		enumToBecZoneMap.put(BECZone.bec_SBS, "SBS");
		enumToBecZoneMap.put(BECZone.bec_SWB, "SWB");
	}

	/** Convert the given BEC Zone text to it's enumeration value. */
	public static BECZone SiteTool_BECZoneToIndex(String becZone) {

		if (becZoneToIndexMap.containsKey(becZone)) {
			return becZoneToIndexMap.get(becZone);
		} else {
			return BECZone.bec_UNKNOWN;
		}
	}

	/** Convert the given enumeration to its text. */
	public static String SiteTool_IndexToBecZone(BECZone becZone) {

		if (enumToBecZoneMap.containsKey(becZone)) {
			return enumToBecZoneMap.get(becZone);
		} else {
			return UNKNOWN_BEC_ZONE_TEXT;
		}
	}

	/**
	 * The array of MoF BioMass Coefficients by BEC Zone and SP0.
	 * <p>
	 * Remarks
	 * <p>
	 * Constants are derived from a spreadsheet supplied by Sam Otukol via e-mail on June 8, 2010. Based on subsequent
	 * telephone discussions, missing BEC/SP0 combinations in that spreadsheet will be populated by the average value of
	 * populated cells for that species across all BEC Zones.
	 * <p>
	 * See the coefficient source file: Documents\VolumeToBiomassRatios.xls
	 * <p>
	 * 2013/03/28: Set the new Volume to MoF Biomass Ratios. Refer to spreadsheet 'VolumeToBiomassRatios.xls' in
	 * 'Documents' folder.
	 */
	public static float[][] mofBiomassCoeffs = {
			{ 0.75226f, 0.67773f, 0.56659f, 0.46211f, 0.68033f, 0.76075f, 0.74426f, 0.65445f, 0.70505f, 0.6811f,
					0.65392f, 0.67541f, 0.74621f, 0.72804f },
			{ 0.65f, 0.59378f, 0.529f, 0.62275f, 0.61644f, 0.66022f, 0.6267f, 0.64858f, 0.62275f, 0.62402f, 0.66216f,
					0.64787f, 0.64741f, 0.5668f },
			{ 0.64299f, 0.59104f, 0.5248f, 0.45428f, 0.57775f, 0.62298f, 0.6189f, 0.59604f, 0.59467f, 0.64494f,
					0.57311f, 0.64939f, 0.62226f, 0.56149f },
			{ 0.33381f, 0.45245f, 0.45245f, 0.38617f, 0.46044f, 0.44231f, 0.46749f, 0.42159f, 0.4479f, 0.39357f,
					0.80881f, 0.40746f, 0.40734f, 0.45245f },
			{ 0.98045f, 0.98045f, 1.31835f, 0.61461f, 0.64046f, 1.34721f, 0.72235f, 0.61955f, 0.68683f, 1.29939f,
					0.98045f, 1.26228f, 1.29339f, 0.98045f },
			{ 0.88456f, 0.81035f, 0.74582f, 0.85618f, 0.77419f, 0.86303f, 0.77514f, 0.77808f, 0.8116f, 0.80313f,
					0.73593f, 0.78081f, 0.81568f, 0.91042f },
			{ 0.59477f, 0.65612f, 0.87535f, 0.62121f, 0.64416f, 0.60205f, 0.57064f, 0.58845f, 0.6732f, 0.60828f,
					0.65134f, 0.61338f, 0.58031f, 0.63687f },
			{ 0.64593f, 0.58617f, 0.58617f, 0.60175f, 0.53275f, 0.57514f, 0.58106f, 0.55665f, 0.63182f, 0.58237f,
					0.58617f, 0.58617f, 0.5681f, 0.58617f },
			{ 0.53048f, 0.58113f, 0.58173f, 0.58113f, 0.63967f, 0.55446f, 0.52352f, 0.5581f, 0.58113f, 0.56171f,
					0.59131f, 0.58113f, 0.57607f, 0.69426f },
			{ 0.67523f, 0.67523f, 0.67523f, 0.60428f, 0.69939f, 0.59213f, 0.80674f, 0.73477f, 0.67523f, 0.69131f,
					0.67523f, 0.67523f, 0.59798f, 0.67523f },
			{ 0.46176f, 0.45991f, 0.45991f, 0.45991f, 0.4697f, 0.44976f, 0.4771f, 0.45006f, 0.4972f, 0.41802f, 0.45991f,
					0.45991f, 0.45568f, 0.45991f },
			{ 0.64937f, 0.56168f, 0.50798f, 0.55649f, 0.59781f, 0.57596f, 0.562f, 0.54768f, 0.60541f, 0.53029f,
					0.55597f, 0.51805f, 0.5793f, 0.5155f },
			{ 0.54482f, 0.54087f, 0.54087f, 0.50384f, 0.55002f, 0.54033f, 0.52083f, 0.50976f, 0.58195f, 0.53198f,
					0.57916f, 0.54087f, 0.54595f, 0.54087f },
			{ 0.70414f, 0.80409f, 0.70414f, 0.70414f, 0.52667f, 0.59504f, 0.62338f, 0.85633f, 0.70414f, 0.77297f,
					0.75048f, 0.70414f, 0.70414f, 0.70414f },
			{ 0.53945f, 0.55009f, 0.47422f, 0.58567f, 0.60615f, 0.52521f, 0.53344f, 0.53973f, 0.6291f, 0.51323f,
					0.50677f, 0.58114f, 0.53923f, 0.57781f },
			{ 0.76271f, 0.68096f, 0.68096f, 0.59783f, 0.69708f, 0.64411f, 0.68096f, 0.66552f, 0.71844f, 0.68096f,
					0.68096f, 0.68096f, 0.68096f, 0.68096f } };

	static {
		if (mofBiomassCoeffs.length != BECZone.size()) {
			throw new IllegalStateException(
					MessageFormat.format(
							"mofBiomassCoeffs does not contain one row for each of the {} BEC Zones", BECZone
									.size()
					)
			);
		}

		for (int i = 0; i < mofBiomassCoeffs.length; i++) {
			if (mofBiomassCoeffs[i].length != SP0Name.values().length) {
				throw new IllegalStateException(
						MessageFormat.format(
								"mofBiomassCoeffs {} does not contain one entry for each of the {} SP0 Zones", i, SP0Name
										.size()
						)
				);
			}
		}
	}

	/**
	 * Converts a BEC Zone and a VDYP7 Species into an MoF Biomass Coefficient for multiplying corresponding Projected
	 * Volumes into an MoF Biomass value.
	 * 
	 * @param becZoneNm the name of the BEC Zone.
	 * @param sp64Nm    the name of the species.
	 * @return as described
	 * @throws an IllegalArgumentException when the MoF Biomass Coefficient cannot be determined.
	 */
	public static float SiteTool_MoFBiomassCoefficient(String becZoneNm, String sp64Nm) {

		BECZone becIndex = SiteTool_BECZoneToIndex(becZoneNm);

		if (becIndex != BECZone.bec_UNKNOWN && VDYP_IsValidSpecies(sp64Nm)) {
			String sp0Name = VDYP_GetVDYP7Species(sp64Nm);
			int sp0Index = VDYP_VDYP7SpeciesIndex(sp0Name).getValue();

			if (sp0Index != SP0Name.sp0_UNKNOWN.getValue()) {
				return mofBiomassCoeffs[sp0Index][becIndex.getIndex()];
			}
		}

		throw new IllegalArgumentException(
				MessageFormat.format(
						"becZoneNm {} and sp64Nm {} are not represented in the mofBiomassCoeffs table", becZoneNm, sp64Nm
				)
		);
	}

	/**
	 * Convert a Canadian Forest Service Tree Class to a string.
	 * <p>
	 * Names come from the 'Volume_To_Biomass.doc', Table 2.
	 * @param cfsTreeClass the CFS Tree Class to be converted to a string.
	 * @return a string corresponding to the named CFS Tree Class constant.
	 */
	public static String CFS_CFSTreeClassToString(int cfsTreeClass) {
		String rtrn = null;
		CFSTreeClass cfsTreeCls = CFSTreeClass.forValue(cfsTreeClass);

		switch (cfsTreeCls) {
		case cfsTreeCls_Missing:
			rtrn = "Missing";
			break;
		case cfsTreeCls_LiveNoPath:
			rtrn = "Live, no pathological indicators";
			break;
		case cfsTreeCls_LiveWithPath:
			rtrn = "Live, some patholigical indicators";
			break;
		case cfsTreeCls_DeadPotential:
			rtrn = "Dead, potentially merchantable";
			break;
		case cfsTreeCls_DeadUseless:
			rtrn = "Dead, not merchantable";
			break;
		case cfsTreeCls_Veteran:
			rtrn = "Veteran";
			break;
		case cfsTreeCls_NoLongerUsed:
			rtrn = "No longer used";
			break;

		default:
			rtrn = "Unknown CFS Tree Class";
			break;
		}

		return rtrn;
	}

	/**
	 * Convert the supplied CFS Genus enumeration value to a corresppnding
	 * string value.
	 * <p>
	 *    The CFS Tree Genus is defined in 'Appendix 7' in the
	 *    'Model_based_volume_to_biomass_CFS' document located in
	 *    'Documents/CFS-Biomass'.
	 * <p>
	 * Elements for this table are automatically generated and copy and pasted
	 * from the:
	 * <ol>
	 * <li> 'C Enum to String Mapping' column of the 
	 * <li> 'GenusTable' found on the 
	 * <li> 'Lookups' tab in the
	 * <li> 'BC_Inventory_updates_by_CBMv2bs.xlsx' located in the
	 * <li> 'Documents/CFS-Biomass' folder.
	 * </ol>
	 * @param genus the CFS Tree Genus value to be converted to a string.
	 * @return the string corresponding to the identified CFS Tree Genus.
	 *    The string for 'cfsGenus_UNKNOWN' if the value is not recognized.
	 */
	public static String CFS_CFSGenusToString(CFSTreeGenus genus) {
		if (genus != null) {
			return genus.getGenusName();
		} else {
			return CFSTreeGenus.cfsGenus_UNKNOWN.getGenusName();
		}
	}

	/*-----------------------------------------------------------------------------
	 *
	 * CFS_CFSSP0DensityMin
	 * CFS_CFSSP0DensityMax
	 * CFS_CFSSP0DensityMean
	 * =====================
	 *
	 *    Determine the minimum, maximum and mean density for each of the SP0s.
	 *
	 *
	 * Parameters
	 * ----------
	 *
	 *    sp0Index
	 *       The index of the SP0 for which your want the density value.
	 *
	 *
	 * Return Value
	 * ------------
	 *
	 *    The density value (in kg/m3) for the requested SP0.
	 *    -9.0 if the SP0 index value was not recognized.
	 *
	 *
	 * Remarks
	 * -------
	 *
	 *    Values comes from Tabl6 found in 'Volume_to_Biomass.doc located in
	 *    'Documents/CFS-Biomass'Volume_to_Biomass.doc'
	 */

	public static float CFS_CFSSP0DensityMin(SP0Name sp0Index) {
		float result;
		try {
			result = CfsSP0Densities.getValue(sp0Index, CFSDensity.MIN_DENSITY_INDEX);
		} catch (UnsupportedOperationException e) {
			result = -9.0F;
		}
		return result;
	}

	public static float CFS_CFSSP0DensityMax(SP0Name sp0Index) {
		float result;
		try {
			result = CfsSP0Densities.getValue(sp0Index, CFSDensity.MAX_DENSITY_INDEX);
		} catch (UnsupportedOperationException e) {
			result = -9.0F;
		}
		return result;
	}

	public static float CFS_CFSSP0DensityMean(SP0Name sp0Index) {
		float result;
		try {
			result = CfsSP0Densities.getValue(sp0Index, CFSDensity.MEAN_DENSITY_INDEX);
		} catch (UnsupportedOperationException e) {
			result = -9.0F;
		}
		return result;
	}

	/**
	 * Convert the supplied string into the best fit for the CFS Species. The supplied 
	 * string must case-insensitively match one of the strings found in Appendix 7 of
	 * 'Model_based_volume_to_biomass_CFS.pdf' found in 'Documents/CFS-Biomass'.
	 *
	 * @param cfsSpcsNm the name of the species to be converted to the CFS Species constant.
	 * @return the CFSTreeSpecies corresponding to the given name. cfsSpcs_UNKNOWN is 
	 * returned if the given name doesn't match a known name.
	 *
	 */
	public static CFSTreeSpecies CFS_cfsSpeciesNameToCfsSpecies(String cfsSpcsNm) {
		return CfsSpecies.getSpeciesBySpeciesName(cfsSpcsNm);
	}

	/**
	 * Determine the CFS Genus for a particular CFS Species. The CFS Species to 
	 * Genus conversions are defined in Appendix 7 of the document 
	 * 'Model_based_volume_to_biomass_CFS.pdf' found in the folder
	 * 'Documents/CFS-Biomass'.
	 *
	 * @param cfsSpcs the CFS Species whose genus is to be determined.
	 * @return The CFS Genus code corresponding to the supplied CFS Species.
	 *    cfsGenus_UNKNOWN if the 'cfsSpcs' code was not recognized.
	 */

	public static CFSTreeGenus CFS_CFSSpcsNumToCFSGenus(CFSTreeSpecies cfsSpcs) {
		return CfsSpecies.getGenusBySpecies(cfsSpcs);
	}

	/*-----------------------------------------------------------------------------
	 *
	 * CFS_LogConfiguration
	 * ====================
	 *
	 *    Log all of the CFS Configuration parameters to the logging system.
	 *
	 *
	 * Parameters
	 * ----------
	 *
	 *    forceLogr
	 *       If desired, force logging through a supplied logger. Otherwise an
	 *       internal logger will be used.
	 *       NULL_LOGGER triggers use of a routine specific logger.
	 *
	 *    msgMrkr
	 *       The message marker to apply to each of the logging statements.
	 *       NULL_MSG_MARKER if no message marker is to be used.
	 *
	 *    logLvl
	 *       The logging level to log each logging statement at.
	 *
	 *    indent
	 *       The number of additional spaces to indent the table from the current logging
	 *       indent level.
	 *       0 indicates no additional indent.
	 *
	 *
	 * Remarks (Optional Heading)
	 * -------
	 *
	 *    Remarks, warnings, special conditions to be aware of, etc.
	 *
	 */

	public static void CFS_LogConfiguration() {
		// TODO
	}

	/*-----------------------------------------------------------------------------
	 *
	 * cfsSP0Densities
	 * ===============
	 *
	 *    Brief Description of what this object represents or contains
	 *
	 *
	 * Members (Optional Heading)
	 * -------
	 *
	 *    Member1
	 *       member description
	 *
	 *
	 * Remarks
	 * -------
	 *
	 *    Density values come from Table 6 of 'Volume_to_Biomass.doc' found in
	 *    the folder 'Documents/CFS-Biomass'.
	 *
	 */

	/*-----------------------------------------------------------------------------
	 *
	 * Logging Data Instances
	 * SAMPLE_MAPS
	 * NUM_SAMPLE_MAPS
	 * SAMPLE_BEC_SUBS
	 * NUM_SAMPLE_BEC_SUBS
	 * SAMPLE_BEC_VARS
	 * NUM_SAMPLE_BEC_VARS
	 * ======================
	 *
	 *    This section contains specific instances of data that refer to generic
	 *    values or specific values identified in the mapping rules.
	 *
	 * Members
	 * -------
	 *
	 *    SAMPLE_MAPS
	 *    NUM_SAMPLE_MAPS
	 *       Lists the explict Map Sheet prefixes identified in the mapping specification
	 *       plus a number of generic mapsheet prefixes meant to represent any other,
	 *       or unknown/unspecified mapsheet prefixes.
	 *
	 *    SAMPLE_BEC_SUBS
	 *    NUM_SAMPLE_BEC_SUBS
	 *       Lists the explicit BEC Sub-Zones identified in the mapping specification
	 *       plus a number of generic BEC Sub-Zones meant to represent any other or
	 *       unknown/unspecified BEC Sub-Zones.
	 *
	 *    SAMPLE_BEC_VARS
	 *    NUM_SAMPLE_BEC_VARS
	 *       Lists the explicit BEC Variants identified in the mapping specification
	 *       plus a number of generic BEC Variants meant to represent any other or
	 *       unknown/unspecified BEC Variants.
	 *
	 *
	 * Remarks
	 * -------
	 *
	 *    This data specification contains data values to be used for logging purposes
	 *    for listing configuration and testing how mapping occurs.
	 *
	 *    This section is not meant to be a part of the CFS Mapping specification except
	 *    that it does list elements that are part of it.
	 *
	 */

	// Create test cases for each of the explicit constants found in the mappings
	// including one outside of the explicits as well as empty and NULL strings.

	public static String[] SAMPLE_MAPS = { "092", "093", "104", "ANY", "", null /* Mapsheet not supplied */ };
	public static final int NUM_SAMPLE_MAPS = SAMPLE_MAPS.length;

	public static String[] SAMPLE_BEC_SUBS = { "dk", "mc", "mw", "wk", "xx", "", null /* Subzone not supplied */ };
	public static final int NUM_SAMPLE_BEC_SUBS = SAMPLE_BEC_SUBS.length;

	public static String[] SAMPLE_BEC_VARS = { "1", "2", "x", "", null /* Variant not supplied */ };
	public static final int NUM_SAMPLE_BEC_VARS = SAMPLE_BEC_VARS.length;

	/**
	 * Converts a MoF sp64 species name (e.g, "AC" from SP64Name.sp64_AC) to its equivalent
	 * in {@link CFSBiomassConversionSupportedSpecies}, should one exist. If one doesn't,
	 * CFSBiomassConversionSupportedSpecies.spcsInt_UNKNOWN is returned.
	 * 
	 * @param spcsNm the text portion of a SP64Name
	 * @return as described
	 */
	public static CFSBiomassConversionSupportedSpecies lcl_MoFSP64ToCFSSpecies(String spcsNm) {

		SP64Name sp64Name = SP64Name.forText(spcsNm);
		
		switch (sp64Name) {
		   case sp64_AC: return CFSBiomassConversionSupportedSpecies.spcsInt_AC;
		   case sp64_ACB: return CFSBiomassConversionSupportedSpecies.spcsInt_ACB;
		   case sp64_AT: return CFSBiomassConversionSupportedSpecies.spcsInt_AT;
		   case sp64_B: return CFSBiomassConversionSupportedSpecies.spcsInt_B;
		   case sp64_BA: return CFSBiomassConversionSupportedSpecies.spcsInt_BA;
		   case sp64_BG: return CFSBiomassConversionSupportedSpecies.spcsInt_BG;
		   case sp64_BL: return CFSBiomassConversionSupportedSpecies.spcsInt_BL;
		   case sp64_CW: return CFSBiomassConversionSupportedSpecies.spcsInt_CW;
		   case sp64_DR: return CFSBiomassConversionSupportedSpecies.spcsInt_DR;
		   case sp64_EA: return CFSBiomassConversionSupportedSpecies.spcsInt_EA;
		   case sp64_EP: return CFSBiomassConversionSupportedSpecies.spcsInt_EP;
		   case sp64_EXP: return CFSBiomassConversionSupportedSpecies.spcsInt_EXP;
		   case sp64_FD: return CFSBiomassConversionSupportedSpecies.spcsInt_FD;
		   case sp64_FDC: return CFSBiomassConversionSupportedSpecies.spcsInt_FDC;
		   case sp64_FDI: return CFSBiomassConversionSupportedSpecies.spcsInt_FDI;
		   case sp64_H: return CFSBiomassConversionSupportedSpecies.spcsInt_H;
		   case sp64_HM: return CFSBiomassConversionSupportedSpecies.spcsInt_HM;
		   case sp64_HW: return CFSBiomassConversionSupportedSpecies.spcsInt_HW;
		   case sp64_L: return CFSBiomassConversionSupportedSpecies.spcsInt_L;
		   case sp64_LA: return CFSBiomassConversionSupportedSpecies.spcsInt_LA;
		   case sp64_LT: return CFSBiomassConversionSupportedSpecies.spcsInt_LT;
		   case sp64_LW: return CFSBiomassConversionSupportedSpecies.spcsInt_LW;
		   case sp64_MB: return CFSBiomassConversionSupportedSpecies.spcsInt_MB;
		   case sp64_PA: return CFSBiomassConversionSupportedSpecies.spcsInt_PA;
		   case sp64_PL:
		   case sp64_PLI: return CFSBiomassConversionSupportedSpecies.spcsInt_PL;
		   case sp64_PLC: return CFSBiomassConversionSupportedSpecies.spcsInt_PLC;
		   case sp64_PW: return CFSBiomassConversionSupportedSpecies.spcsInt_PW;
		   case sp64_PY: return CFSBiomassConversionSupportedSpecies.spcsInt_PY;
		   case sp64_S: return CFSBiomassConversionSupportedSpecies.spcsInt_S;
		   case sp64_SB: return CFSBiomassConversionSupportedSpecies.spcsInt_SB;
		   case sp64_SE: return CFSBiomassConversionSupportedSpecies.spcsInt_SE;
		   case sp64_SS: return CFSBiomassConversionSupportedSpecies.spcsInt_SS;
		   case sp64_SW: return CFSBiomassConversionSupportedSpecies.spcsInt_SW;
		   case sp64_SX: return CFSBiomassConversionSupportedSpecies.spcsInt_SX;
		   case sp64_W: return CFSBiomassConversionSupportedSpecies.spcsInt_W;
		   case sp64_X: return CFSBiomassConversionSupportedSpecies.spcsInt_XC;
		   case sp64_YC: return CFSBiomassConversionSupportedSpecies.spcsInt_YC;
		   default: return CFSBiomassConversionSupportedSpecies.spcsInt_UNKNOWN;
		}
	}

	/**
	 * Convert the supplied Internal Species Index into its corresponding string.
	 *
	 * @param intSpeciesNdx the internal species index to be converted into a string.
	 * @return the string corresponding to the supplied internal species index. If 
	 * {@code intSpeciesNdx} is null or has the value "spcsInt_UNKNOWN", "??" is 
	 * returned.
	 */
	public static String lcl_InternalSpeciesIndexToString(CFSBiomassConversionSupportedSpecies intSpeciesNdx)
	{
		if (intSpeciesNdx == null || CFSBiomassConversionSupportedSpecies.spcsInt_UNKNOWN.equals(intSpeciesNdx)) {
			return "??";
		} else {
			return intSpeciesNdx.getText();
		}
	}

	/**
	 * Convert the supplied Internal Genus Index into its corresponding string.
	 *
	 * @param intGenusNdx the internal genus index to be converted into a string.
	 * @return the string corresponding to the supplied internal genus index. If 
	 * {@code intGenusNdx} is null or has the value "genusInt_INVALID", 
	 * "genusInt_INVALID" is returned.
	 */
	public static String lcl_InternalGenusIndexToString(CFSBiomassConversionSupportedGenera intGenusNdx) {
		if (intGenusNdx == null || intGenusNdx.equals(CFSBiomassConversionSupportedGenera.genusInt_INVALID))
			return "genusInt_INVALID";
		else
			return intGenusNdx.getText();
	}

	/**
	 * Convert the supplied Live Conversion Parameter into an identifying string.
	 *
	 * @param liveParam the conversion parameter to be converted into a string.
	 * @param nameFormat indicates in what format the enumeration constant is to be 
	 * converted.
	 * @return a string representation for the live conversion parameter. If
	 * {@code liveParam} has the value null or <code>cfsLiveParm_UNKNOWN</code>, 
	 * "cfsLiveParm_UNKNOWN" is returned.
	 */
	public static String lcl_LiveConversionParamToString(CFSLiveConversionParams liveParam, NameFormat nameFormat) {
		if (liveParam == null || liveParam.equals(CFSLiveConversionParams.cfsLiveParm_UNKNOWN)) {
			return "cfsLiveParm_UNKNOWN";
		} else if (nameFormat == null) {
			return liveParam.toString();
		} else {
			switch (nameFormat) {
			case catOnly:
				return liveParam.getCategory();
			case nameOnly:
				return liveParam.getText();
			case catName:
				return MessageFormat.format("{0} {1}", liveParam.getCategory(), liveParam.getText());
			default:
				throw new IllegalStateException(MessageFormat.format("Unsupported enumNameFormat {0}", nameFormat));
			}
		}
	}

	/**
	 * Convert the supplied Dead Conversion Parameter into an identifying string.
	 * <p>
	 * These strings are meant to be int names for the parameters.
	 * 
	 * @param deadParam the conversion parameter to be converted into a string.
	 * @param nameFormat indicates into which format the enumeration constant is to be converted.
	 * @return a string representation for the dead conversion parameter. "cfsDeadParm_UNKNOWN" 
	 * is returned if the parameter was not recognized.
	 */
	public static String lcl_DeadConversionParamToString(CFSDeadConversionParams deadParam, NameFormat nameFormat) {
		if (deadParam == null || deadParam.equals(CFSDeadConversionParams.cfsDeadParm_UNKNOWN)) {
			return "cfsDeadParm_UNKNOWN";
		} else if (nameFormat == null) {
			return deadParam.toString();
		} else {
			switch (nameFormat) {
			case catOnly:
				return "Dead";
			case nameOnly:
				return deadParam.getText();
			case catName:
				return MessageFormat.format("Dead {0}", deadParam, deadParam.getText());
			default:
				throw new IllegalStateException(MessageFormat.format("Unsupported enumNameFormat {0}", nameFormat));
			}
		}
	}

	/**
	 * Returns the total number of defined species. One is subtracted one off of the size of the speciesTable because
	 * there is a blank entry at the head of the array which does not represent a valid species.
	 *
	 * @return as described.
	 */
	public static int VDYP_NumDefinedSpecies() {
		return speciesTable.getNSpecies();
	}

	/**
	 * Converts a species 2 letter species ("code") name into an index into the Species table.
	 * 
	 * @param spName the two letter ("code") name of the species.
	 * @return the index into the Species table for the corresponding species, and SpeciesTable.UNKNOWN_ENTRY_INDEX if
	 *         the species was not recognized.
	 */
	public static int VDYP_SpeciesIndex(String spName) {
		int result;

		if (StringUtils.isNotBlank(spName)) {
			try {
				SpeciesTableItem spsc = speciesTable.getByCode(spName.toUpperCase());
				result = spsc.cfsSpecies().ordinal();
			} catch (IllegalArgumentException e) {
				result = SpeciesTable.UNKNOWN_ENTRY_INDEX;
			}
		} else {
			result = SpeciesTable.UNKNOWN_ENTRY_INDEX;
		}

		return result;
	}

	/**
	 * Determines if the given species name is valid; that is, if it is either a Commercial 
	 * or Non-Commercial tree species as defined by MoF..
	 *
     * @param spName the species name to test.
	 * @return a boolean indicating the result of the test.
	 */
	public static boolean VDYP_IsValidSpecies(String spName) {
		return VDYP_SpeciesIndex(spName) >= 0;
	}

	/**
	 * Determines if the tree species is a deciduous species or not.
	 *
	 * @param sp64Name the name of tree species to be tested.
	 * @return a boolean with the result
	 */
	public static boolean VDYP_IsDeciduous(SP64Name sp64Name) {
		return speciesTable.getByCode(sp64Name.getText()).isDeciduous();
	}

	/**
	 * Determines if the tree species is a commercial species or not.
	 *
	 * @param sp64Name the species in question
	 * @return a boolean with the result
	 */
	public static boolean VDYP_IsCommercial(SP64Name sp64Name) {
		return speciesTable.getByCode(sp64Name.getText()).isCommercial();
	}

	/**
	 * Returns the MoF abbreviation for a given species.
	 * 
	 * @param sp64Name the species in question
	 * @return the short ("code") name of the species.
	 */
	public static String VDYP_GetSpeciesShortName(SP64Name sp64Name) {
		return sp64Name.getText();
	}

	/**
	 * Returns the full name of a given species.
	 * 
	 * @param sp64Name the species in question
	 * @return the full ("common") name of the species.
	 */
	public static String VDYP_GetSpeciesFullName(SP64Name sp64Name) {
		return speciesTable.getByCode(sp64Name.getText()).fullName();
	}

	/**
	 * Returns the latin name of a given species.
	 * 
	 * @param sp64Name the species
	 * @return the latin name of the species.
	 */
	public static String VDYP_GetSpeciesLatinName(SP64Name sp64Name) {
		return speciesTable.getByCode(sp64Name.getText()).latinName();
	}

	/**
	 * Returns the genus of a given species.
	 * 
	 * @param sp64 the species
	 * @return the latin name of the species.
	 */
	public static String VDYP_GetSpeciesGenus(SP64Name sp64Name) {
		return speciesTable.getByCode(sp64Name.getText()).genusName();
	}

	/**
	 * Converts a species name to an equivalent SINDEX supported Species code.
	 *
	 * @param spName the name of the species to convert.
	 * @param region indicates which provincial region to set the site curve for,
	 *
	 * @return the equivalent SINDEX species code. "" is returned if the species 
	 * is not supported by SINDEX and no mapping exists.
	 */
	public static String VDYP_GetSINDEXSpecies(String speciesName, SpeciesRegion region) {

		String speciesCode = SpeciesTable.UNKNOWN_ENTRY_CODE_NAME;

		if (speciesName != null) {

			char fiz;

			switch (region) {
			case spcsRgn_Coast:
				fiz = 'A';
				break;
			case spcsRgn_Interior:
				fiz = 'D';
				break;
			default:
				fiz = ' ';
				break;
			}

			if (fiz != ' ') {
				int ndx = -1;

				try {
					ndx = Sindxdll.SpecRemap(speciesName, fiz);
				} catch (CodeErrorException | ForestInventoryZoneException e) {
					// fall through
				}

				if (ndx >= 0) {
					speciesCode = Sindxdll.SpecCode(ndx);
				}
			}
		}

		return speciesCode;
	}

	/**
	 * Converts a species name to an equivalent VDYP7 species (genus) name.
	 * 
	 * @param spName the name of the species to convert.
	 * @return the equivalent VDYP7 species code. "" is returned if the 
	 * species is not supported by VDYP7 and no mapping exists.
	 */
	public static String VDYP_GetVDYP7Species(String spName) {
		return speciesTable.getByCode(spName).sp0Name();
	}

	/**
	 * Convert the supplied SP0Name constant into a VDYP7 species name.
	 * <p>
	 * SP0 and VDYP7 species names are one and the same.
	 * <p>
	 * The returned name is a valid VDYP7 class of species names and would also
	 * be a valid SP64 species name.
	 *
	 * @param sp0 the species whose name is to be fetched.
	 * @return the name sought, or "" if the SP0 number is not recognized.
	 */
	public static String VDYP_GetSP0Species(SP0Name sp0) {

		if (sp0 != null && !SP0Name.sp0_UNKNOWN.equals(sp0)) {
			return sp0.getText();
		} else {
			return "";
		}
	}

	/**
	 * Determines the currently assigned Site Index curve type to use for the specified species.
	 * <p>
	 * Within this program, there a number of core "Freddie" routines which use this value as an input parameter instead
	 * of a species name.
	 * <p>
	 * Changes
	 * <p>
	 * November 21, 1997 - Added a Coastal/Interior Differentiation for Hemlock.
	 * <p>
	 * The curves assigned to specific species may be reassigned through calls to 'VDYP_SetCurrentSICurve'. If no such
	 * curve assignment has been made, the default curve from SINDEX will be returned.
	 *
	 * @param spName The 2 letter name of the species.
	 * @param region Indicates which provincial region to look up the site curve for,
	 * @return The Site Index curve type to use for this species. -1 if the species or region was not recognized.
	 */
	public static int VDYP_GetCurrentSICurve(String spName, SpeciesRegion region) {

		var entry = speciesTable.getByCode(spName);
		int siCurve = entry.currentSICurve()[region.ordinal()];

		// If the curve for this species is not set, look it up from SINDEX.

		if (siCurve < 0) {
			int sindexSpcs;
			try {
				sindexSpcs = Sindxdll.SpecRemap(spName, region == SpeciesRegion.spcsRgn_Coast ? 'A' : 'D');

				siCurve = Sindxdll.DefCurve(sindexSpcs);
			} catch (CommonCalculatorException e) {
				siCurve = -1;
			}
			entry.currentSICurve()[region.ordinal()] = siCurve;
		} else {
			siCurve = -1;
		}

		return siCurve;
	}

	/**
	 * Determines the BC default Site Index curve type to use for the specified species.
	 * <p>
	 * Within this program, there a number of core "Freddie" routines which use this 
	 * value as an input parameter instead of a species name.
	 *
	 * @param spName the species short ("code") name such as "ABAL".
	 * @param region the region under consideration.
	 *
	 * @return the BC default Site Index curve type to use for this species. Returns -2 
	 * for non-commercial species.
	 */
	public static int VDYP_GetDefaultSICurve(String spName, SpeciesRegion region) {
		
		int siCurve;

		if (spName != null && region != null && speciesTable.getByCode(spName) != SpeciesTable.DefaultEntry) {

			int sindxSpcs;
			try {
				sindxSpcs = Sindxdll.SpecRemap(spName, (region == SpeciesRegion.spcsRgn_Coast) ? 'A' : 'D');

				if (sindxSpcs >= 0) {
					siCurve = Sindxdll.DefCurve(sindxSpcs);
				} else {
					siCurve = -1;
				}
			} catch (CommonCalculatorException e) {
				siCurve = -1;
			}
		} else {
			siCurve = -1;
		}

		return siCurve;
	}

	/**
	 * Sets the Site Index curve to use for a particular species.
	 *
	 * @param spName the 2 letter name of the species.
	 * @param region indicates which provincial region to set the site curve for,
	 * @param siCurve the site index curve to use for the specified species. -1 
	 * resets the curve to the default.
	 *
	 * @return the previous value.
	 */
	public static int VDYP_SetCurrentSICurve(String spName, SpeciesRegion region, int siCurve) {
		
		int oldCurve = VDYP_GetCurrentSICurve(spName, region);
		var species = speciesTable.getByCode(spName);
		if (region != null && species != SpeciesTable.DefaultEntry) {

			species.currentSICurve()[region.ordinal()] = siCurve;
		}

		return oldCurve;
	}

	/**
	 * Determines the number of available SI curves for a particular species.
	 * 
	 * @param respectSpeciesBoundariesInd
	 * <ul>
	 * <li><b>true</b> all remaining parameters are used to filter the particular
	 *          set of curves to count.
	 * <li><b>false</b> the count of all curves is returned, regardless of species
	 *          and provincial location.
	 * </ul>
	 * @param spName the short ("code") name of the species.
	 * @param mixInteriorCoastalInd
	 * <ul>
	 * <li><b>true</b> curves from the interior and the coast are counted in the
	 *          total count. In this case, the <code>region</code> parameter is ignored.
	 * <li><b>false</b> only the curves from the particular region of the province
	 *          specified by the <code>region</code> parameter are counted.
	 * </ul>
	 * @param region indicates which provincial region to get the number for. If
	 * 			<code>mixInteriorCoastalInd</code> is <code>true</code>, this 
	 *          parameter is ignored.
	 * @return a count of all of the Site Index curves for a particular species. 0 
	 * 			is returned if the species is non-commercial or the species is not 
	 *          recognized. This count is always positive for commercial species. 
	 *          Always 0 for non-commercial species. When mixing interior and coastal 
	 *          curves, the count may be the same as when treating these regions 
	 *          separately.
	 */
	public static int VDYP_GetNumSICurves(boolean respectSpeciesBoundariesInd, String spName, 
			boolean mixInteriorCoastalInd, SpeciesRegion region) {

		int numCurves = 0;
		int specNum = VDYP_SpeciesIndex(spName);

		boolean[] countedCurve = new boolean[SI_MAX_CURVES];
		Arrays.fill(countedCurve, 0, SI_MAX_CURVES, false);

		// Check if we have a valid species. This doesn't matter if we are not respecting species boundaries.
		if (respectSpeciesBoundariesInd && (specNum < 0)) {
			return 0;
		}

		// Check if we are going to return a count of all curves regardless of region.
		if (!respectSpeciesBoundariesInd) {
			return SI_MAX_CURVES;
		}

		// Count the available coastal curves.
		if (mixInteriorCoastalInd || SpeciesRegion.spcsRgn_Coast.equals(region)) {
			try {
				int sindexSpcs = Sindxdll.SpecRemap(spName, 'A');

				if (sindexSpcs >= 0) {
					// Track each of the curves in the range of curves for the species.

					int curveNum = Sindxdll.FirstCurve(sindexSpcs);

					while (curveNum >= 0) {
						if (!countedCurve[curveNum]) {
							numCurves++;
							countedCurve[curveNum] = true;
						}
						curveNum = Sindxdll.NextCurve(sindexSpcs, curveNum);
					}

					// Make sure we track the default curve just in case it lies outside of the range of curves
					// enumerated above.

					curveNum = Sindxdll.DefCurve(sindexSpcs);
					if (curveNum >= 0 && !countedCurve[curveNum]) {
						numCurves++;
						countedCurve[curveNum] = true;
					}
				}
			} catch (CommonCalculatorException e) {
				// do nothing
			}
		}

		// Add in the number of available interior curves if we are selecting from the available interior curves.

		if (mixInteriorCoastalInd || SpeciesRegion.spcsRgn_Interior.equals(region)) {
			try {
				int sindexSpcs = Sindxdll.SpecRemap(spName, 'D');

				if (sindexSpcs >= 0) {
					// Track each of the curves in the range of curves for the species.

					int curveNum = Sindxdll.FirstCurve(sindexSpcs);

					while (curveNum >= 0) {
						if (!countedCurve[curveNum]) {
							numCurves++;
							countedCurve[curveNum] = true;
						}

						curveNum = Sindxdll.NextCurve(sindexSpcs, curveNum);
					}

					// Make sure we track the default curve just in case it lies outside of the range of curves enumerated

					curveNum = Sindxdll.DefCurve(sindexSpcs);
					if (curveNum >= 0 && !countedCurve[curveNum]) {
						numCurves++;
						countedCurve[curveNum] = true;
					}
				}
			} catch (CommonCalculatorException e) {
				// do nothing
			}
		}

		return numCurves;
	}

	/**
	 * Obtains the SINDEX species index most closely associated with a particular site curve.
	 *
	 * @param siCurve the Site Index curve to convert into a species name.
	 * @return species index, for use in other Sindex functions. -1 is returned if the curve
	 * was not recognized.
	 */
	public static int VDYP_GetSICurveSpeciesIndex(int siCurve) {
		try {
			return Sindxdll.CurveToSpecies(siCurve);
		} catch (CurveErrorException e) {
			return -1;
		}
	}

	/**
	 * For a specific species, returns the default Crown Closure associated with that species within a particular region
	 * of the province.
	 *
	 * @param spName the species name to be looked up.
	 * @param region indicates which provincial region to get the default CC for.
	 *
	 * @return the default CC associated with the species in that particular region and -1.0 if the species was not
	 *         recognized or no default CC has been assigned to that species and region.
	 */
	public static float VDYP_GetDefaultCrownClosure(String spName, SpeciesRegion region) {

		// Note that if spName is invalid, the default entry is returned, which in
		// turn contains the right default value of -1.0f.

		return speciesTable.getByCode(spName).defaultCrownClosure()[region.ordinal()];
	}

	/**
	 * Convert a VDYP7 SP0 Species Name into an enumSP0Name.
	 *
	 * @param sp0Name the VDYP7 Species Name to be converted into a species index.
	 *
	 * @return The enumSP0Name corresponding to the supplied species, and sp0_UNKNOWN if the species name was not
	 *         supplied or was not recognized. Note that the returned index is a zero-based index.
	 */
	public static SP0Name VDYP_VDYP7SpeciesIndex(String sp0Name) {
		SP0Name sp0Index = SP0Name.sp0_UNKNOWN;

		if (sp0Name != null) {
			try {
				sp0Index = SP0Name.valueOf(sp0Name.toUpperCase());
			} catch (IllegalArgumentException e) {
				sp0Index = SP0Name.sp0_UNKNOWN;
			}
		}

		return sp0Index;
	}
	
	/**
	 * Return the species number for the given CFS Tree Species.
	 * <p>
	 * CFS Species are defined in Appendix 7 of the document 'Model_based_volume_to_biomass_CFS.pdf' found in
	 * 'Documents/CFS-Biomass'.
	 *
	 * @param cfsSpcs the CFS tree species whose species number is to be returned.
	 * 
	 * @return as described.
	 */
	public static int SiteTool_CFSSpcsToCFSSpcsNum(CFSTreeSpecies cfsSpcs) {
		return CfsSpecies.getSpeciesIndexBySpecies(cfsSpcs);
	}

	/** 
	 * Determines if the supplied species is a deciduous or coniferous species.
	 * 
	 * @param spcsIndx the SP64Name's -index- of species in question.
	 * @return as described
	 */
	public static boolean SiteTool_IsDeciduous(int spcsIndx) {
	
		return VDYP_IsDeciduous(SP64Name.forValue(spcsIndx));
	}

	/** 
	 * Determines if the supplied species is a softwood species.
	 * 
	 * @param spName the species short ("code") name.
	 * @return as described
	 */
	public static boolean SiteTool_IsSoftwood(String spName) {
		
		// Note that if spName is not a recognized species name, the correct default value is returned.
		return speciesTable.getByCode(spName).isSoftwood();
	}

	/**
	 * Determines if the supplied species corresponds to a Pine species or not.
	 * 
	 * @param spName the species short ("code") name.
	 * @return {@code true} when the supplied species is a Pine related species and false if not, or the supplied
	 *         species was not recognized.
	 */
	public static boolean SiteTool_IsPine(String spName) {
		
		String sSP0 = VDYP_GetVDYP7Species(spName);
		if (sSP0 != null) {
			switch (sSP0) {
			case "PA", "PL", "PW", "PY":
				return true;
			default:
				return false;
			}
		}
	
		return false;
	}

	/**
	 * Converts a species name to its corresponding CFS defined species.
	 * <p>
	 * The list of species mappings is defined in the file 'BCSpcsToCFSSpcs-SAS.txt' found in 'Documents/CFS-Biomass'.
	 * 
	 * @param spName the species short ("code") name.
	 * @return the mapping to the equivalent CFS defined tree species (if a mapping exists). {@code cfsSpcs_UNKNOWN} is
	 *         returned if the species was not recognized or a mapping does not exist.
	 */
	public static CFSTreeSpecies SiteTool_GetSpeciesCFSSpcs(String spName) {

		// Note that if spName is not a recognized species name, the correct default value is returned.
		return speciesTable.getByCode(spName).cfsSpecies();
	}

	/**
	 * Returns the Canadian Forest Service Species Number corresponding to the MoF Species Number.
	 * <p>
	 * The mapping from MoF Species is defined in 'BCSpcsToCFSSpcs-SAS.txt' found in 'Documents/CFS-Biomass'.
	 *
	 * @param spName the species short ("code") name.
	 * @return the CFS Species Number corresponding to the MoF Species index, and -1 if the species 
	 * index is not in range or there is no mapping from the MoF Species to the CFS Species.
	 */
	public static int SiteTool_GetSpeciesCFSSpcsNum(String spName) {
		
		CFSTreeSpecies cfsSpcs = SiteTool_GetSpeciesCFSSpcs(spName);

		if (cfsSpcs != CFSTreeSpecies.cfsSpcs_UNKNOWN) {
			return SiteTool_CFSSpcsToCFSSpcsNum(cfsSpcs);
		} else {
			return -1;
		}
	}

	/**
	 * Converts a Height and Age to a Site Index for a particular Site Index Curve.
	 *
	 * @param curve the particular site index curve to project the height and age along.
	 *			This curve must be one of the active curves defined in "sindex.h"
	 * @param age the age of the trees indicated by the curve selection. The
	 *			interpretation of this age is modified by the 'ageType' parameter.
	 * @param ageType must be one of:
	 * <ul>
	 * <li>AT_TOTAL the age is the total age of the stand in years since planting.
	 * <li>AT_BREAST the age indicates the number of years since the stand reached breast height.
	 * </ul>
	 * @param height the height of the species in meters.
	 * @param estType must be one of:
	 * <ul>
	 * <li>SI_EST_DIRECT compute the site index based on direct equations if available. If 
	 * 		the equations are not available, then automatically fall to the SI_EST_ITERATE
	 *		method.
	 * <li> SI_EST_ITERATE compute the site index based on an iterative method which converges
	 * 		on the true site index.
	 * </ul>
	 * @return the site index of the pure species stand given the height and age.
	 */
	public static double SiteTool_HtAgeToSI(int curve, double age, int ageType, double height, int estType)
			throws CommonCalculatorException {
		
		Reference<Double> siRef = new Reference<>();
		// This method always returns 0; in the event of an error, an exception is thrown.
		Sindxdll.HtAgeToSI(curve, age, ageType, height, estType, siRef);
		
		double SI = siRef.get();

		// Round SI off to two decimals.
		SI = ((int) (SI * 100.0 + 0.5)) / 100.0;

		return SI;
	}
	
	/**
	 * Converts a Height and Site Index to an Age for a particular Site Index Curve.
	 *
	 * @param curve the particular site index curve to project the height and age along.
	 *			This curve must be one of the active curves defined in "sindex.h"
	 * @param height the height of the species in meters.
	 * @param ageType must be one of:
	 * <ul>
	 * <li>AT_TOTAL the age is the total age of the stand in years since planting.
	 * <li>AT_BREAST the age indicates the number of years since the stand reached breast height.
	 * </ul>
	 * @param siteIndex the site index value of the stand.
	 * @param years2BreastHeight the number of years it takes the stand to reach breast height.
	 * 
	 * @return the age of the stand (given the ageType) at which point it has reached the 
	 * height specified.
	 */
	public static double SiteTool_HtSIToAge(int curve, double height, int ageType, double siteIndex, 
			double years2BreastHeight)
				throws CommonCalculatorException {
		
		Reference<Double> tempRef_rtrn = new Reference<>();
		
		// This call always returns 0; in the event of an error, an exception is thrown.
		Sindxdll.HtSIToAge(curve, height, ageType, siteIndex, years2BreastHeight, tempRef_rtrn);
		
		return tempRef_rtrn.get();
	}

	/**
	 * Converts an Age and Site Index to a Height for a particular Site Index Curve.
	 *
	 * @param curve the particular site index curve to project the height and age along.
	 *		This curve must be one of the active curves defined in "sindex.h"
	 *
	 * @param age the age of the trees indicated by the curve selection. The
	 *		interpretation of this age is modified by the 'ageType' parameter.
	 *
	 * @param ageType must be one of:
	 * <ul>
	 * <li>AT_TOTAL the age is the total age of the stand in years since planting.
	 * <li>AT_BREAST the age indicates the number of years since the stand reached breast height.
	 * </ul>
	 * @param siteIndex the site index value of the stand.
	 * @param years2BreastHeight the number of years it takes the stand to reach breast height.
	 *	
	 * @return the height of the stand given the height and site index.
	 * 
	 * @throws CommonCalculatorException
	 */
	public static double SiteTool_AgeSIToHt(int curve, double age, int ageType, double siteIndex, 
				double years2BreastHeight)
			throws CommonCalculatorException {
		
		int freddieCurve = curve;
		int freddieAgeType = ageType;
		double freddieAge = age;
		double freddieSI = siteIndex;
		double freddieY2BH = years2BreastHeight;

		Reference<Double> tempRef_rtrn = new Reference<>();
		
		// This call always returns 0; if an error occurs, an exception is thrown.
		Sindxdll.AgeSIToHt(freddieCurve, freddieAge, freddieAgeType, freddieSI, freddieY2BH, tempRef_rtrn);

		return tempRef_rtrn.get();
	}

	/**
	 * Calculates the number of years a stand takes to grow from seed to breast height.
	 *
	 * @param curve the particular site index curve to project the height and age along.
	 *			This curve must be one of the active curves defined in "sindex.h"
	 * @param siteIndex the site index value of the stand.
	 * @return the number of years to grow from seed to breast height.
	 * @throws CommonCalculatorException in the event of an error
	 */
	public static double SiteTool_YearsToBreastHeight(int curve, double siteIndex) throws CommonCalculatorException {
		double rtrn = 0.0;

		Reference<Double> tempRef_rtrn = new Reference<>(rtrn);
		
		// This call always returns 0; if an error occurs, an exception is thrown.
		Sindxdll.Y2BH(curve, siteIndex, tempRef_rtrn);

		rtrn = tempRef_rtrn.get();

		// Round off to 1 decimal.
		rtrn = Math.round((int) (rtrn * 10.0 + 0.5)) / 10.0;

		return rtrn;
	}

	/**
	 * Returns the name of a particular curve.
	 * 
	 * @param siCurve the site index curve to get the name of.
	 * @return string corresponding the name of the supplied curve number. "Unknown 
	 * 		Curve" is returned for unrecognized curves.
	 */
	public static String SiteTool_SICurveName(int siCurve) {
		String retStr;

		try {
			retStr = Sindxdll.CurveName(siCurve);
		} catch (CurveErrorException e) {
			retStr = "Unknown Curve";
		}

		return retStr;
	}

	public static int SiteTool_NumSpecies() {
		return VDYP_NumDefinedSpecies();
	}

	public static String SiteTool_SpeciesShortName(int spcsIndx) {
		return VDYP_GetSpeciesShortName(SP64Name.forValue(spcsIndx));
	}

	public static int SiteTool_SpeciesIndex(String spcsCodeName) {
		return VDYP_SpeciesIndex(spcsCodeName);
	}

	public static String SiteTool_SpeciesFullName(String spcsCodeName) {
		return VDYP_GetSpeciesFullName(SP64Name.forText(spcsCodeName));
	}

	public static String SiteTool_SpeciesLatinName(String spcsCodeName) {
		return VDYP_GetSpeciesLatinName(SP64Name.forText(spcsCodeName));
	}

	public static String SiteTool_SpeciesGenusCode(String spcsCodeName) {
		return VDYP_GetSpeciesGenus(SP64Name.forText(spcsCodeName));
	}

	public static String SiteTool_SpeciesSINDEXCode(String spcsCode, boolean isCoastal) {
		return VDYP_GetSINDEXSpecies(spcsCode, 
				isCoastal ? SpeciesRegion.spcsRgn_Coast : SpeciesRegion.spcsRgn_Interior);
	}

	public static String SiteTool_SpeciesVDYP7Code(String spcsCode) {
		return VDYP_GetVDYP7Species(spcsCode);
	}

	/**
	 * Sets the Site Index curve to use for a particular species.
	 *
	 * @param speciesCodeName the short ("code") name of the species.
	 * @param coastalInd if <code>true</code>, the Coastal region is used and otherwise Interior is used.
	 * @param siCurve the site index curve to use for the specified species. -1 resets the curve 
	 * 		to the default.
	 *
	 * @return the previous value.
	 */
	public static int SiteTool_SetSICurve(String speciesCodeName, boolean coastalInd, int siCurve) {
		
		SpeciesRegion region = (coastalInd ? SpeciesRegion.spcsRgn_Coast : SpeciesRegion.spcsRgn_Interior);
		return VDYP_SetCurrentSICurve(speciesCodeName, region, siCurve);
	}

	/**
	 * Maps a Species code name to a specific SI Curve.
	 *
	 * @param spcsCodeName the species short ("code") name.
	 * @param isCoastal <code>true</code> if coastal, <code>false</code> if interior.
	 * @return the SI Curve number for the species, or -1 if the species was not recognized.
	 */
	public static int SiteTool_GetSICurve(String spcsCode, boolean isCoastal) {

		return VDYP_GetCurrentSICurve(spcsCode, 
				isCoastal ? SpeciesRegion.spcsRgn_Coast : SpeciesRegion.spcsRgn_Interior);
	}

	/**
	 * Converts a SI Curve number to a Species code name, or "" if the SI Curve number
	 * is not recognized.
	 * 
	 * @param siCurve the SI Curve number for the species
	 * @return the short ("code") name of the species, in SIndex33 format (leading character
	 * in upper case; following characters in lower case.)
	 */
	public static String SiteTool_SiteCurveSINDEXSpecies(int siCurve) {
		
		int spcsNdx = VDYP_GetSICurveSpeciesIndex(siCurve);

		String spcsNm;
		if (spcsNdx >= 0) {
			spcsNm = Sindxdll.SpecCode(spcsNdx);
		} else {
			spcsNm = "";
		}

		return spcsNm;
	}

	/**
	 * For a specific species, returns the default Crown Closure associated with that species within a particular region
	 * of the province.
	 *
	 * @param spName the short ("code") name to be looked up.
	 * @param isCoastal if <code>true</code>, region is Coastal. Otherwise, the region is Interior.
	 *
	 * @return the default CC associated with the species in that particular region and -1.0 if the species was not
	 *         recognized or no default CC has been assigned to that species and region.
	 */
	public static float SiteTool_SpeciesDefaultCrownClosure(String speciesCodeName, boolean isCoastal) {
		return VDYP_GetDefaultCrownClosure(
				speciesCodeName, (isCoastal ? SpeciesRegion.spcsRgn_Coast : SpeciesRegion.spcsRgn_Interior)
		);
	}

	public static String SiteTool_SINDEXErrorToString(int iSINDEXError) {
		String rtrnStr = null;

		switch (iSINDEXError) {
		case 0:
			rtrnStr = "SUCCESS";
			break;

		case SIErrors.SI_ERR_LT13:
			rtrnStr = "SI_ERR_LT13";
			break;
		case SIErrors.SI_ERR_GI_MIN:
			rtrnStr = "SI_ERR_GI_MIN";
			break;
		case SIErrors.SI_ERR_GI_MAX:
			rtrnStr = "SI_ERR_GI_MAX";
			break;
		case SIErrors.SI_ERR_NO_ANS:
			rtrnStr = "SI_ERR_NO_ANS";
			break;
		case SIErrors.SI_ERR_CURVE:
			rtrnStr = "SI_ERR_CURVE";
			break;
		case SIErrors.SI_ERR_CLASS:
			rtrnStr = "SI_ERR_CLASS";
			break;
		case SIErrors.SI_ERR_FIZ:
			rtrnStr = "SI_ERR_FIZ";
			break;
		case SIErrors.SI_ERR_CODE:
			rtrnStr = "SI_ERR_CODE";
			break;
		case SIErrors.SI_ERR_GI_TOT:
			rtrnStr = "SI_ERR_GI_TOT";
			break;
		case SIErrors.SI_ERR_SPEC:
			rtrnStr = "SI_ERR_SPEC";
			break;
		case SIErrors.SI_ERR_AGE_TYPE:
			rtrnStr = "SI_ERR_AGE_TYPE";
			break;
		case SIErrors.SI_ERR_ESTAB:
			rtrnStr = "SI_ERR_ESTAB";
			break;

		default:
			rtrnStr = "UNKNOWN";
			break;
		}

		return rtrnStr;
	}

	/**
	 * Compute the third of three age values given two of the others. Exactly one of these parameters
	 * must be -9.0. The other two must be set to valid values. If this does not hold, then nothing is
	 * computed.
	 * <p>
	 * This routine implements the equation:
	 * <p>
	 * Total Age = Breast Height Age + YTBH - 0.5
	 * <p>
	 * As this equation recently changed, it was thought to place it in a
	 * single routine to automatically keep all the individualcalculations
	 * up to date. Previously, all places that needed to calculate the
	 * third value, did this "in-place" and now all have to be seartched for
	 * and modified.
	 * <p>
	 * The following note from Gordon Nigh explains the rationale behind
	 * the 0.5 half year age correction. It is dated June 11, 2003 and was
	 * received from Cam embedded in his e-mail dated January 21, 2004:
	 * <p>
	 * Further comments: there has been some confusion about the
	 * issues surrounding age in Sindex. I would like to clarify them
	 * here, and make a proposition.
	 * <ol>
	 * <li>The newer ministry recommended height-age curves have the
	 * 0.5 age correction. These curves have been developed
	 * internally. The older curves do not have the age correction.
	 * This correction (documented in Research Report 03) was
	 * implemented to make the models consistent with the definition
	 * of breast height age, which is the number of annual growth rings
	 * at breast height. Since the innermost ring represents a half
	 * years growth (on average), the height-age models acutally go
	 * through a height of 1.3 at breast height age 0.5, not age 0 (it
	 * also means that site index is the height of the tree at 49.5
	 * growing seasons after the tree reaches breast height).
	 * Therefore, the age adjustment explicitiy incorporates this
	 * assumption (i.e., tree reaches breast height midway through the
	 * growing season) into the height-age model.
	 * <li>Logic has it that if we make this assumption for the height-age
	 * curves, then we should make the same assumption for the
	 * years-to-breast-height functions as well. That is, if the
	 * years-to-breast-height functions give us the number of years to
	 * reach breast height, then this number should end in 0.5. To do
	 * this, we had KenP truncate the decimal part of the years to
	 * breast height estimate, and then add 0.5.
	 * <li>Total age is just the number of years the tree has been growing.
	 * If we add breast height age to years-to-breast-height, we get
	 * xx.5 (see item 2 above) but we should be getting a whole number.
	 * This 0.5 year "error" results because of the definition of
	 * breast height age. At breast height age nn the tree has only
	 * been growing nn-0.5 years since it reached breast height (e.g.
	 * it has only been growing 0.5 years since it reached breast
	 * height at breast height age 1). Therefore,
	 *
	 * Total Age = Breast Height Age + Years-to-Breast-Height - 0.5.
	 * <li>
	 * It seems to me that if we accept item 1 above (and I think it is
	 * generally accepted now since it is logically correct), then items 2
	 * and 3 should follow automatically to make the systems internally
	 * consistent with respect to the assumptions.
	 * </ol>
	 *
	 * @param rTotalAge        total age of the stand, or -9.0 if unknown
	 * @param rBreastHeightAge breast height age of the stand, or -9.0 if unknown
	 * @param rYTBH            years to breast height of the stand, or -9.0 if unknown
	 *
	 * @return always returns zero. In the future, this routine will return an error code, but none has
	 *         been defined for this library yet.
	 */
	public static int SiteTool_FillInAgeTriplet(Reference<Double> rTotalAge, Reference<Double> rBreastHeightAge,
			Reference<Double> rYTBH) {
		
		int rtrnCode = 0;

		// Ensure the parameters have values - one of them must be set to "unknown".
		if (rTotalAge.isPresent() && rBreastHeightAge.isPresent() && rYTBH.isPresent()) {
			// All the parameters are supplied, perform the calculation based on which of the two values are supplied.

			// Note that because BHAge can be negative, we must use the less restrictive test of not being equal to

			if (rTotalAge.get() >= 0.0 && rBreastHeightAge.get() != -9.0 && rYTBH.get() < 0.0 /* unknown */) {
				rYTBH.set(rTotalAge.get() - rBreastHeightAge.get() + 0.5);
			} else if (rTotalAge.get() >= 0.0 && rBreastHeightAge.get() == -9.0 /* unknown */ && rYTBH.get() >= 0.0) {
				rBreastHeightAge.set(rTotalAge.get() - rYTBH.get() + 0.5);
			} else if (rTotalAge.get() < 0.0 /* unknown */ && rBreastHeightAge.get() != -9.0 && rYTBH.get() >= 0.0) {
				rTotalAge.set(rBreastHeightAge.get() + rYTBH.get() - 0.5);
			}
		}

		return rtrnCode;
	}
}