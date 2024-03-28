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
import ca.bc.gov.nrs.vdyp.sindex.Reference;
import ca.bc.gov.nrs.vdyp.sindex.Sindxdll;

public class Vdyp32Dll {
	private static final Logger logger = LoggerFactory.getLogger(Vdyp32Dll.class);

	private static final String UNKNOWN_BEC_ZONE_TEXT = "????";

	private static SpeciesTable speciesTable;

	private static final Map<String, enumIntBECZone> becZoneToIndexMap = new HashMap<>();

	static {
		becZoneToIndexMap.put("AT", enumIntBECZone.bec_AT);
		becZoneToIndexMap.put("BG", enumIntBECZone.bec_BG);
		becZoneToIndexMap.put("BWBS", enumIntBECZone.bec_BWBS);
		becZoneToIndexMap.put("CDF", enumIntBECZone.bec_CDF);
		becZoneToIndexMap.put("CWH", enumIntBECZone.bec_CWH);
		becZoneToIndexMap.put("ESSF", enumIntBECZone.bec_ESSF);
		becZoneToIndexMap.put("ICH", enumIntBECZone.bec_ICH);
		becZoneToIndexMap.put("IDF", enumIntBECZone.bec_IDF);
		becZoneToIndexMap.put("MH", enumIntBECZone.bec_MH);
		becZoneToIndexMap.put("MS", enumIntBECZone.bec_MS);
		becZoneToIndexMap.put("PP", enumIntBECZone.bec_PP);
		becZoneToIndexMap.put("SBSP", enumIntBECZone.bec_SBPS);
		becZoneToIndexMap.put("SBS", enumIntBECZone.bec_SBS);
		becZoneToIndexMap.put("SWB", enumIntBECZone.bec_SWB);
	}

	private static final Map<enumIntBECZone, String> enumToBecZoneMap = new HashMap<>();

	static {
		enumToBecZoneMap.put(enumIntBECZone.bec_AT, "AT");
		enumToBecZoneMap.put(enumIntBECZone.bec_BG, "BG");
		enumToBecZoneMap.put(enumIntBECZone.bec_BWBS, "BWBS");
		enumToBecZoneMap.put(enumIntBECZone.bec_CDF, "CDF");
		enumToBecZoneMap.put(enumIntBECZone.bec_CWH, "CWH");
		enumToBecZoneMap.put(enumIntBECZone.bec_ESSF, "ESSF");
		enumToBecZoneMap.put(enumIntBECZone.bec_ICH, "ICH");
		enumToBecZoneMap.put(enumIntBECZone.bec_IDF, "IDF");
		enumToBecZoneMap.put(enumIntBECZone.bec_MH, "MH");
		enumToBecZoneMap.put(enumIntBECZone.bec_MS, "MS");
		enumToBecZoneMap.put(enumIntBECZone.bec_PP, "PP");
		enumToBecZoneMap.put(enumIntBECZone.bec_SBPS, "SBSP");
		enumToBecZoneMap.put(enumIntBECZone.bec_SBS, "SBS");
		enumToBecZoneMap.put(enumIntBECZone.bec_SWB, "SWB");
	}

	/** Convert the given BEC Zone text to it's enumeration value. */
	public static enumIntBECZone SiteTool_BECZoneToIndex(String becZone) {

		if (becZoneToIndexMap.containsKey(becZone)) {
			return becZoneToIndexMap.get(becZone);
		} else {
			return enumIntBECZone.bec_UNKNOWN;
		}
	}

	/** Convert the given enumeration to its text. */
	public static String SiteTool_IndexToBecZone(enumIntBECZone becZone) {

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
		if (mofBiomassCoeffs.length != enumIntBECZone.size()) {
			throw new IllegalStateException(
					MessageFormat.format(
							"mofBiomassCoeffs does not contain one row for each of the {} BEC Zones", enumIntBECZone
									.size()
					)
			);
		}

		for (int i = 0; i < mofBiomassCoeffs.length; i++) {
			if (mofBiomassCoeffs[i].length != enumSP0Name.values().length) {
				throw new IllegalStateException(
						MessageFormat.format(
								"mofBiomassCoeffs {} does not contain one entry for each of the {} SP0 Zones", i, enumSP0Name
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

		enumIntBECZone becIndex = SiteTool_BECZoneToIndex(becZoneNm);

		if (becIndex != enumIntBECZone.bec_UNKNOWN && VDYP_IsValidSpecies(sp64Nm)) {
			String sp0Name = VDYP_GetVDYP7Species(sp64Nm);
			int sp0Index = VDYP_VDYP7SpeciesIndex(sp0Name).getValue();

			if (sp0Index != enumSP0Name.sp0_UNKNOWN.getValue()) {
				return mofBiomassCoeffs[sp0Index][becIndex.getIndex()];
			}
		}

		throw new IllegalArgumentException(
				MessageFormat.format(
						"becZoneNm {} and sp64Nm {} are not represented in the mofBiomassCoeffs table", becZoneNm, sp64Nm
				)
		);
	}

	/*-----------------------------------------------------------------------------
	 *
	 * CFS_CFSTreeClassToString
	 * ========================
	 *
	 *    Convert a Canadian Forest Service Tree Class to a string.
	 *
	 *
	 * Parameters
	 * ----------
	 *
	 *    cfsTreeClass
	 *       The CFS Tree Class to be converted to a string.
	 *
	 *
	 * Return Value
	 * ------------
	 *
	 *    A string corresponding to the named CFS Tree Class constant.
	 *
	 *
	 * Remarks
	 * -------
	 *
	 *    Names come from the 'Volume_To_Biomass.doc', Table 2.
	 *
	 */
	public static String CFS_CFSTreeClassToString(int cfsTreeClass) {
		String rtrn = null;
		enumIntCFSTreeClass cfsTreeCls = enumIntCFSTreeClass.forValue(cfsTreeClass);

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

	/*-----------------------------------------------------------------------------
	 *
	 * CFS_CFSGenusToString
	 * ====================
	 *
	 *    Convert the supplied CFS Genus enumeration value to a corresppnding
	 *    string value.
	 *
	 *
	 * Parameters
	 * ----------
	 *
	 *    genus
	 *       The CFS Tree Genus value to be converted to a string.
	 *
	 *
	 * Return Value
	 * ------------
	 *
	 *    The string corresponding to the identified CFS Tree Genus.
	 *    The string for 'cfsGenus_UNKNOWN' if the value is not recognized.
	 *
	 *
	 * Remarks
	 * -------
	 *
	 *    The CFS Tree Genus is defined in 'Appendix 7' in the
	 *    'Model_based_volume_to_biomass_CFS' document located in
	 *    'Documents/CFS-Biomass'.
	 *
	 *    Elements for this table are automatically generated and copy and pasted
	 *    from the:
	 *       -  'C Enum to String Mapping' column of the 
	 *       -  'GenusTable' found on the 
	 *       -  'Lookups' tab in the
	 *       -  'BC_Inventory_updates_by_CBMv2bs.xlsx' located in the
	 *       -  'Documents/CFS-Biomass' folder.
	 *
	 */
	public static String CFS_CFSGenusToString(enumIntCFSTreeGenus genus) {
		String rtrn = null;

		switch (genus) {
		/* Automatically generated statements. */
		case cfsGenus_NotApplicable:
			rtrn = "Not applicable";
			break;
		case cfsGenus_MissingValue:
			rtrn = "Missing Value";
			break;
		case cfsGenus_Spruce:
			rtrn = "Spruce";
			break;
		case cfsGenus_Pine:
			rtrn = "Pine";
			break;
		case cfsGenus_Fir:
			rtrn = "Fir";
			break;
		case cfsGenus_Hemlock:
			rtrn = "Hemlock";
			break;
		case cfsGenus_DouglasFir:
			rtrn = "Douglas-fir";
			break;
		case cfsGenus_Larch:
			rtrn = "Larch";
			break;
		case cfsGenus_CedarAndOtherConifers:
			rtrn = "Cedar and other conifers";
			break;
		case cfsGenus_UnspecifiedConifers:
			rtrn = "Unspecified conifers";
			break;
		case cfsGenus_Poplar:
			rtrn = "Poplar";
			break;
		case cfsGenus_Birch:
			rtrn = "Birch";
			break;
		case cfsGenus_Maple:
			rtrn = "Maple";
			break;
		case cfsGenus_OtherBroadleaves:
			rtrn = "Other broadleaved species";
			break;
		case cfsGenus_UnspecifiedBroadleaves:
			rtrn = "Unspecified broadleaved species";
			break;

		/* Leave these alone - they are not automatically generated. */
		case cfsGenus_UNKNOWN:
			rtrn = "UNKNOWN";
			break;
		default:
			rtrn = CFS_CFSGenusToString(enumIntCFSTreeGenus.cfsGenus_UNKNOWN);
			break;
		}

		return rtrn;
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
	 *
	 */

	public static float CFS_CFSSP0DensityMin(enumSP0Name sp0Index) {
		float result;
		try {
			result = cfsSP0Densities[sp0Index.getIndex()][DefineConstants.MIN_DENSITY_INDEX];
		} catch (UnsupportedOperationException e) {
			result = -9.0F;
		}
		return result;
	}

	public static float CFS_CFSSP0DensityMax(enumSP0Name sp0Index) {
		float result;
		try {
			result = cfsSP0Densities[sp0Index.getIndex()][DefineConstants.MAX_DENSITY_INDEX];
		} catch (UnsupportedOperationException e) {
			result = -9.0F;
		}
		return result;
	}

	public static float CFS_CFSSP0DensityMean(enumSP0Name sp0Index) {
		float result;
		try {
			result = cfsSP0Densities[sp0Index.getIndex()][DefineConstants.MEAN_DENSITY_INDEX];
		} catch (UnsupportedOperationException e) {
			result = -9.0F;
		}
		return result;
	}

	/*-----------------------------------------------------------------------------
	 *
	 * CFS_StringToCFSSpcs
	 * ===================
	 *
	 *    Convert the supplied string into the best fit for the CFS Species.
	 *
	 *
	 * Parameters
	 * ----------
	 *
	 *    cfsSpcsNm
	 *       The name of the species to be converted to the CFS Species constant.
	 *       The search is case insensitive.
	 *       NULL and "" treated as an unknown species.
	 *
	 *
	 * Return Value
	 * ------------
	 *
	 *    The constant corresponding to the supplied species name.
	 *    Returns 'cfsSpcs_UNKNOWN' if the string was not recognized.
	 *    
	 *
	 *
	 * Remarks
	 * -------
	 *
	 *    The supplied string must case-insensitively match one of the strings
	 *    found in Appendix 7 of 'Model_based_volume_to_biomass_CFS.pdf' found
	 *    in 'Documents/CFS-Biomass'.
	 *
	 */

	public static enumIntCFSTreeSpecies CFS_StringToCFSSpcs(String cfsSpcsNm) {
		enumIntCFSTreeSpecies spcs = enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN;

		// Perform a full table scan for the species name. This is terribly inefficient but it is
		// not expected that this operation will be frequently done.

		if (StringUtils.isNotBlank(cfsSpcsNm)) {

			enumIntCFSTreeSpecies.Iterator i = new enumIntCFSTreeSpecies.Iterator();

			while (i.hasNext()) {

				enumIntCFSTreeSpecies e = i.next();
				if (CfsSpecies.array[e.ordinal()].cfsSpcsNm().equals(cfsSpcsNm)) {
					spcs = e;
					break;
				}
			}
		}

		return spcs;
	}

	/*-----------------------------------------------------------------------------
	 *
	 * CFS_CFSSpcsNumToCFSGenus
	 * ========================
	 *
	 *    Determine the CFS Genus for a particular CFS Species.
	 *
	 *
	 * Parameters
	 * ----------
	 *
	 *    cfsSpcs
	 *       The CFS Species to be converted to the encompassing Genus.
	 *
	 *
	 * Return Value
	 * ------------
	 *
	 *    The CFS Genus code corresponding to the supplied CFS Species.
	 *    cfsGenus_UNKNOWN if the 'cfsSpcs' code was not recognized.
	 *
	 *
	 * Remarks
	 * -------
	 *
	 *    The CFS Species to Genus conversions are defined in Appendix 7 of the
	 *    document 'Model_based_volume_to_biomass_CFS.pdf' found in the folder
	 *    'Documents/CFS-Biomass'.
	 *
	 */

	public static enumIntCFSTreeGenus CFS_CFSSpcsNumToCFSGenus(enumIntCFSTreeSpecies cfsSpcs) {
		return CfsSpecies.array[cfsSpcs.getIndex()].cfsGenusEnum();
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

	public static float[][] cfsSP0Densities = { { 295.00F, 229.00F, 564.00F }, { 416.00F, 304.00F, 519.00F },
			{ 379.25F, 204.00F, 541.00F }, { 391.00F, 238.00F, 475.00F }, { 373.00F, 333.00F, 603.00F },
			{ 607.00F, 512.00F, 693.00F }, { 445.00F, 323.00F, 615.00F }, { 476.00F, 249.00F, 661.00F },
			{ 524.25F, 323.00F, 616.00F }, { 466.00F, 466.00F, 530.00F }, { 420.00F, 204.00F, 693.00F },
			{ 423.00F, 256.00F, 518.00F }, { 373.00F, 237.00F, 496.00F }, { 420.00F, 204.00F, 693.00F },
			{ 387.00F, 257.00F, 568.00F }, { 453.00F, 239.00F, 544.00F } };


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

	public static String[] SAMPLE_MAPS = { "092", "093", "104", "ANY", "", null }; // Mapsheet not supplied as NULL.
	public static final int NUM_SAMPLE_MAPS = SAMPLE_MAPS.length;

	public static String[] SAMPLE_BEC_SUBS = { "dk", "mc", "mw", "wk", "xx", "", null }; // Sub-Zone not supplied as
																							// NULL.
	public static final int NUM_SAMPLE_BEC_SUBS = SAMPLE_BEC_SUBS.length;

	public static String[] SAMPLE_BEC_VARS = { "1", "2", "x", "", null }; // Variant not supplied as NULL.
	public static final int NUM_SAMPLE_BEC_VARS = SAMPLE_BEC_VARS.length;

	public static enumSpcsInternalIndex lcl_MoFSP64ToInternalIndex(String spcsNm) {

		enumSpcsInternalIndex spcsNdx;
		if (StringUtils.isNotBlank(spcsNm)) {
			try {
				spcsNdx = enumSpcsInternalIndex.valueOf(spcsNm);
			} catch (IllegalArgumentException e) {
				spcsNdx = enumSpcsInternalIndex.spcsInt_UNKNOWN;
			}
		} else {
			spcsNdx = enumSpcsInternalIndex.spcsInt_UNKNOWN;
		}

		return spcsNdx;
	}

	/*-----------------------------------------------------------------------------
	 *
	 * lcl_InternalSpeciesIndexToString
	 * ================================
	 *
	 *    Convert the supplied Internal Species Index into its corresponding
	 *    string.
	 *
	 *
	 * Parameters
	 * ----------
	 *
	 *    intSpeciesNdx
	 *       The internal genus index to be converted into a string.
	 *
	 *
	 * Return Value
	 * ------------
	 *
	 *    The string corresponding to the supplied internal genus index.
	 *    The string corresponding to 'genusInt_INVALID' for unrecognized values.
	 *
	 *
	 * Remarks (Optional Heading)
	 * -------
	 *
	 *    Remarks, warnings, special conditions to be aware of, etc.
	 *
	 *
	 * Dependencies (Optional Heading)
	 * ------------
	 *
	 *    Object/Function Name          Module Where Located
	 *
	 *
	 * Functional Description (Optional Heading)
	 * ----------------------
	 *
	 *    More detailed information about how the function does what it does.
	 *
	 */

	public static String lcl_InternalSpeciesIndexToString(enumSpcsInternalIndex intSpeciesNdx)

	{
		String rtrn = null;

		switch (intSpeciesNdx) {
		case spcsInt_AC:
			rtrn = "AC";
			break;
		case spcsInt_ACB:
			rtrn = "ACB";
			break;
		case spcsInt_AT:
			rtrn = "AT";
			break;
		case spcsInt_B:
			rtrn = "B";
			break;
		case spcsInt_BA:
			rtrn = "BA";
			break;
		case spcsInt_BG:
			rtrn = "BG";
			break;
		case spcsInt_BL:
			rtrn = "BL";
			break;
		case spcsInt_CW:
			rtrn = "CW";
			break;
		case spcsInt_DR:
			rtrn = "DR";
			break;
		case spcsInt_EA:
			rtrn = "EA";
			break;
		case spcsInt_EP:
			rtrn = "EP";
			break;
		case spcsInt_EXP:
			rtrn = "EXP";
			break;
		case spcsInt_FD:
			rtrn = "FD";
			break;
		case spcsInt_FDC:
			rtrn = "FDC";
			break;
		case spcsInt_FDI:
			rtrn = "FDI";
			break;
		case spcsInt_H:
			rtrn = "H";
			break;
		case spcsInt_HM:
			rtrn = "HM";
			break;
		case spcsInt_HW:
			rtrn = "HW";
			break;
		case spcsInt_L:
			rtrn = "L";
			break;
		case spcsInt_LA:
			rtrn = "LA";
			break;
		case spcsInt_LT:
			rtrn = "LT";
			break;
		case spcsInt_LW:
			rtrn = "LW";
			break;
		case spcsInt_MB:
			rtrn = "MB";
			break;
		case spcsInt_PA:
			rtrn = "PA";
			break;
		case spcsInt_PL:
			rtrn = "PL";
			break;
		case spcsInt_PLC:
			rtrn = "PLC";
			break;
		case spcsInt_PLI:
			rtrn = "PLI";
			break;
		case spcsInt_PW:
			rtrn = "PW";
			break;
		case spcsInt_PY:
			rtrn = "PY";
			break;
		case spcsInt_S:
			rtrn = "S";
			break;
		case spcsInt_SB:
			rtrn = "SB";
			break;
		case spcsInt_SE:
			rtrn = "SE";
			break;
		case spcsInt_SS:
			rtrn = "SS";
			break;
		case spcsInt_SW:
			rtrn = "SW";
			break;
		case spcsInt_SX:
			rtrn = "SX";
			break;
		case spcsInt_W:
			rtrn = "W";
			break;
		case spcsInt_XC:
			rtrn = "XC";
			break;
		case spcsInt_YC:
			rtrn = "YC";
			break;

		default:
			rtrn = "??";
			break;
		}

		return rtrn;
	}

	/*-----------------------------------------------------------------------------
	 *
	 * lcl_InternalGenusIndexToString
	 * ==============================
	 *
	 *    Convert the supplied Internal Genus Index into its corresponding string.
	 *
	 *
	 * Parameters
	 * ----------
	 *
	 *    intGenusNdx
	 *       The internal genus index to be converted into a string.
	 *
	 *
	 * Return Value
	 * ------------
	 *
	 *    The string corresponding to the supplied internal genus index.
	 *    The string corresponding to 'genusInt_INVALID' for unrecognized values.
	 *
	 *
	 * Remarks (Optional Heading)
	 * -------
	 *
	 *    Remarks, warnings, special conditions to be aware of, etc.
	 *
	 *
	 * Dependencies (Optional Heading)
	 * ------------
	 *
	 *    Object/Function Name           Module Where Located
	 *
	 *
	 * Functional Description (Optional Heading)
	 * ----------------------
	 *
	 *    More detailed information about how the function does what it does.
	 *
	 */

	public static String lcl_InternalGenusIndexToString(enumGenusInternalIndex intGenusNdx) {
		if (intGenusNdx == null || intGenusNdx.equals(enumGenusInternalIndex.genusInt_INVALID))
			return "genusInt_INVALID";
		else
			return intGenusNdx.getText();
	}

	/*-----------------------------------------------------------------------------
	 *
	 * lcl_LiveConversionParamToString
	 * ===============================
	 *
	 *    Convert the supplied Live Conversion Parameter into an identifying string.
	 *
	 *
	 * Parameters
	 * ----------
	 *
	 *    liveParam
	 *       The conversion parameter to be converted into a string.
	 *
	 *    nameFormat
	 *       Indicates in what format to convert the enumeration constant into.
	 *
	 *
	 * Return Value
	 * ------------
	 *
	 *    A string representation for the live conversion parameter.
	 *    "??" or "cfsLiveParm_UNKNOWN" if the parameter was not recognized.
	 *
	 *
	 * Remarks
	 * -------
	 *
	 *    These strings are meant to be int names for the parameters not the
	 *    full enumeration constant.
	 *
	 */

	public static String
			lcl_LiveConversionParamToString(enumLiveCFSConversionParams liveParam, enumNameFormat nameFormat) {
		if (liveParam == null || liveParam.equals(enumLiveCFSConversionParams.cfsLiveParm_UNKNOWN)) {
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

	/*-----------------------------------------------------------------------------
	 *
	 * lcl_DeadConversionParamToString
	 * ===============================
	 *
	 *    Convert the supplied Dead Conversion Parameter into an identiying string.
	 *
	 *
	 * Parameters
	 * ----------
	 *
	 *    deadParam
	 *       The conversion parameter to be converted into a string.
	 *
	 *    nameFormat
	 *       Indicates in what format to convert the enumeration constant into.
	 *
	 *
	 * Return Value
	 * ------------
	 *
	 *    A string rpresentation for the dead conversion parameter.
	 *    "??" or "cfsDeadParm_UNKNOWN" if the parameter was not necognized.
	 *
	 *
	 * Remarks
	 * -------
	 *
	 *    These strings are meant to be int names for the parameters.
	 *
	 */

	public static String
			lcl_DeadConversionParamToString(enumDeadCFSConversionParams deadParam, enumNameFormat nameFormat) {
		if (deadParam == null || deadParam.equals(enumDeadCFSConversionParams.cfsDeadParm_UNKNOWN)) {
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

	/*-----------------------------------------------------------------------------
	 *
	 * lcl_LookupFallbackToString
	 * ==========================
	 *
	 *    Brief Description of what this function does
	 *
	 *
	 * Parameters (Optional Heading)
	 * ----------
	 *
	 *    Param1
	 *       parameter description
	 *
	 *
	 * Return Value (Optional Heading)
	 * ------------
	 *
	 *    What the return value is and what it means (if applicable)
	 *
	 *
	 * Remarks (Optional Heading)
	 * -------
	 *
	 *    Remarks, warnings, special conditions to be aware of, etc.
	 *
	 *
	 * Dependencies (Optional Heading)
	 * ------------
	 *
	 *    Object/Function Name            Module Where Located
	 *
	 *
	 * Functional Description (Optional Heading)
	 * ----------------------
	 *
	 *    More detailed information about how the function does what it does.
	 *
	 */

	public static String lcl_LookupFallbackToString(enumParamLookupFallback lookupFallback) {

		if (lookupFallback == null) {
			return "Unknown";
		} else {
			return lookupFallback.getText();
		}
	}

	/*-----------------------------------------------------------------------------
	 *
	 * SiteTool_CFSSpcsToString
	 * ========================
	 *
	 *    Convert a CFS Species Constant to the corresponding species name.
	 *
	 *
	 * Parameters
	 * ----------
	 *
	 *    cfsSpcs
	 *       The CFS defined species to be converted to a string.
	 *       Must be one of the 'enumIntCFSTreeSpecies' constant values.
	 *
	 *
	 * Return Value
	 * ------------
	 *
	 *    The string corresponding to the species enumeration constant.
	 *    The value for 'cfsSpcs_UNKNOWN' if the constant was not recognized.
	 *
	 *
	 * Remarks
	 * -------
	 *
	 *    The return string points to an internal constant buffer and must not
	 *    be modified by the caller.
	 *
	 */

	public static String SiteTool_CFSSpcsToString(enumIntCFSTreeSpecies cfsSpcs) {
		return CfsSpecies.array[cfsSpcs.ordinal()].cfsSpcsNm();
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
	public static int SiteTool_CFSSpcsToCFSSpcsNum(enumIntCFSTreeSpecies cfsSpcs) {
		int rtrn;

		if (cfsSpcs == null) {
			rtrn = CfsSpecies.array[0].cfsSpcsNum();
		} else {
			rtrn = CfsSpecies.array[cfsSpcs.ordinal()].cfsSpcsNum();
		}

		return rtrn;
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
				structSpeciesTableItem spsc = speciesTable.getByCode(spName.toUpperCase());
				result = spsc.iCFSSpcs().ordinal();
			} catch (IllegalArgumentException e) {
				result = SpeciesTable.UNKNOWN_ENTRY_INDEX;
			}
		} else {
			result = SpeciesTable.UNKNOWN_ENTRY_INDEX;
		}

		return result;
	}

	/*-----------------------------------------------------------------------------
	 *
	 * VDYP_IsValidSpecies
	 * ===================
	 *
	 *    Determines if the passed species name is defined.
	 *
	 *
	 * Parameters
	 * ----------
	 *
	 *    spName
	 *       The species name to test.
	 *
	 *
	 * Return Value
	 * ------------
	 *
	 *    TRUE  The species is valid.
	 *    FALSE The species was not recognized.
	 *
	 *
	 * Remarks
	 * -------
	 *
	 *    A species is valid if it is either a Commercial or Non-Commercial
	 *    tree species as defined by MoF.
	 *
	 */

	public static boolean VDYP_IsValidSpecies(String spName) {
		return VDYP_SpeciesIndex(spName) >= 0;
	}

	/*-----------------------------------------------------------------------------
	 *
	 * VDYP_IsDeciduous
	 * ================
	 *
	 *    Determines if the tree species is a deciduous species or not.
	 *
	 *
	 * Parameters
	 * ----------
	 *
	 *    SpcsNm
	 *       The tree species to be tested.
	 *
	 *
	 * Return Value
	 * ------------
	 *
	 *    TRUE  The species is a deciduous tree species.
	 *    FALSE The species is coniferous.
	 *
	 *
	 * Remarks
	 * -------
	 *
	 *    The species name is expected to be one of the MoF standard
	 *    two uppercase letter species names.
	 *
	 */

	public static boolean VDYP_IsDeciduous(String spName) {
		return speciesTable.getByCode(spName).bIsDeciduous();
	}

	/*-----------------------------------------------------------------------------
	 *
	 * VDYP_IsCommercial
	 * =================
	 *
	 *    Determines if the species is a Commercial Species.
	 *
	 *
	 * Parameters
	 * ----------
	 *
	 *    spName
	 *       The MoF 2 letter species name to test.
	 *
	 *
	 * Return Value
	 * ------------
	 *
	 *    TRUE  The species is a valid commercial species.
	 *    FALSE The species is not a commercial species.
	 *
	 */

	public static boolean VDYP_IsCommercial(String spName) {
		return speciesTable.getByCode(spName).bIsCommercial();
	}

	/*-----------------------------------------------------------------------------
	 *
	 * VDYP_GetSpeciesShortName
	 * ========================
	 *
	 *    Returns the MoF abbreviation for a particular species.
	 *
	 *
	 * Parameters
	 * ----------
	 *
	 *    ndx
	 *       The zero based index to the species you wish.
	 *
	 *
	 * Return Value
	 * ------------
	 *
	 *    A pointer to the species name.
	 *
	 *
	 * Remarks
	 * -------
	 *
	 *    The 'ndx' must range from '0' to 'NumValidSpecies() - 1'.
	 *    Any invalid values will return "".
	 *
	 *    The return value represents an internal buffer and should not be
	 *    modified in any way.
	 *
	 */

	public static boolean SiteTool_IsSoftwood(String spName) {
		// Note that if spName is not a recognized species name, the correct default value is returned.
		return speciesTable.getByCode(spName).bIsSoftwood();
	}

	/**
	 * Determines if the supplied species corresponds to a Pine species or not.
	 * 
	 * @param spName the name of the species to test.
	 *
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

	public static String VDYP_GetSpeciesShortName(enumSP64Name sp64) {
		return speciesTable.getByCode(sp64.getText()).sCodeName();
	}

	/*-----------------------------------------------------------------------------
	 *
	 * VDYP_GetSpeciesFullName
	 * =======================
	 *
	 *    Returns the full English name of a MoF Species 2 Letter
	 *    abbreviation.
	 *
	 *
	 * Parameters
	 * ----------
	 *
	 *    spName
	 *       The species to convert to English.
	 *
	 *
	 * Return Value
	 * ------------
	 *
	 *    A pointer to an internal buffer containing the English Name.
	 *    NULL if the species was not recognized.
	 *
	 *
	 * Remarks
	 * -------
	 *
	 *    The return value must not be modified or 'free'd.
	 *
	 */
	public static String VDYP_GetSpeciesFullName(enumSP64Name sp64) {
		var item = speciesTable.getByCode(sp64.getText());
		return item.sFullName();
	}

	/*-----------------------------------------------------------------------------
	 *
	 * VDYP_GetSpeciesLatinName
	 * ========================
	 *
	 *    Returns the full Latin name of a MoF Species 2 Letter abbreviation.
	 *
	 *
	 * Parameters
	 * ----------
	 *
	 *    spName
	 *       The species to convert to Latin.
	 *
	 *
	 * Return Value
	 * ------------
	 *
	 *    A pointer to an internal buffer containing the Latin Name.
	 *    NULL if the species was not recognized.
	 *
	 *
	 * Remarks
	 * -------
	 *
	 *    The return value must not be modified or 'free'd.
	 *
	 */

	public static String VDYP_GetSpeciesLatinName(enumSP64Name sp64) {
		var item = speciesTable.getByCode(sp64.getText());
		return item.sLatinName();
	}

	/*-----------------------------------------------------------------------------
	 *
	 * VDYP_GetSpeciesGenus
	 * ====================
	 *
	 *    Converts a species name to its corresponding genus name.
	 *
	 *
	 * Parameters
	 * ----------
	 *
	 *    spName
	 *       The name of the species to convert.
	 *
	 *
	 * Return Value
	 * ------------
	 *
	 *    The genus name of the specified species.
	 *
	 *
	 * Remarks
	 * -------
	 *
	 *    The return value points to an internal buffer and should not
	 *    be modified or 'free'd.
	 *
	 */

	public static String VDYP_GetSpeciesGenus(enumSP64Name sp64) {
		var item = speciesTable.getByCode(sp64.getText());
		return item.sGenusName();
	}

	/*-----------------------------------------------------------------------------
	 *
	 * VDYP_GetSINDEXSpecies
	 * =====================
	 *
	 *    Converts a species name to an equivalent SINDEX supported Species code.
	 *
	 *
	 * Parameters
	 * ----------
	 *
	 *    spName
	 *       The name of the species to convert.
	 *
	 *    region
	 *       Indicates which provincial region to set the site curve for,
	 *
	 *
	 * Return Value
	 * ------------
	 *
	 *    The equivalent SINDEX species code.
	 *    "" if the species is not supported by SINDEX and no mapping exists.
	 *
	 *
	 * Remarks
	 * -------
	 *
	 *    The return value points to an internal buffer and should not
	 *    be modified or 'free'd.
	 */
	public static String VDYP_GetSINDEXSpecies(String speciesName, enumSpeciesRegion region) {

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

	/*-----------------------------------------------------------------------------
	 *
	 * VDYP_GetVDYP7Species
	 * ====================
	 *
	 *    Converts a species name to an equivalent VDYP7 supported Species code.
	 *
	 *
	 * Parameters
	 * ----------
	 *
	 *    spName
	 *       The name of the species to convert.
	 *
	 *
	 * Return Value
	 * ------------
	 *
	 *    The equivalent VDYP7 species code.
	 *    "" if the species is not supported by VDYP7 and no mapping exists.
	 *
	 *
	 * Remarks
	 * -------
	 *
	 *    The return value points to an internal buffer and should not
	 *    be modified or 'free'd.
	 *
	 */

	public static String VDYP_GetVDYP7Species(String spName) {
		return speciesTable.getByCode(spName).sSP0Name();
	}

	/*-----------------------------------------------------------------------------
	 *
	 * VDYP_GetSP0Species
	 * ==================
	 *
	 *    Convert the supplied SP0 enum constant into a VDYP7 species name.
	 *
	 *
	 * Parameters
	 * ----------
	 *
	 *    sp0Num
	 *       The SP0 Species Number to be converted into a VDYP7 Species Name.
	 *       Must be a valid constant from the 'enumSP0Name' enumeration.
	 *
	 *
	 * Return Value (Optional Heading)
	 * ------------
	 *
	 *    The name of the
	 *    "" if the SP0 number is not recognized.
	 *
	 *
	 * Remarks
	 * ------
	 *
	 *    SP0 and VDYP7 species names are one and the same.
	 *
	 *    The returned name is a valid VDYP7 class of species names and would also
	 *    be a valid SP64 species name.
	 *
	 */
	public static String VDYP_GetSP0Species(enumSP0Name sp0) {

		if (sp0 != null && !enumSP0Name.sp0_UNKNOWN.equals(sp0)) {
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
	public static int VDYP_GetCurrentSICurve(String spName, enumSpeciesRegion region) {

		var entry = speciesTable.getByCode(spName);
		int siCurve = entry.iCrntSICurve()[region.ordinal()];

		// If the curve for this species is not set, look it up from SINDEX.

		if (siCurve < 0) {
			int sindexSpcs;
			try {
				sindexSpcs = Sindxdll.SpecRemap(spName, region == enumSpeciesRegion.spcsRgn_Coast ? 'A' : 'D');

				siCurve = Sindxdll.DefCurve(sindexSpcs);
			} catch (CommonCalculatorException e) {
				siCurve = -1;
			}
			entry.iCrntSICurve()[region.ordinal()] = siCurve;
		} else {
			siCurve = -1;
		}

		return siCurve;
	}

	/*-----------------------------------------------------------------------------
	 *
	 * VDYP_GetDefaultSICurve
	 * ======================
	 *
	 *    Determines the BC default Site Index curve type to use for the
	 *    specified species.
	 *
	 *
	 * Parameters
	 * ----------
	 *
	 *    IsCoastal
	 *       TRUE     The region the tree resides in is the Coast.
	 *       FALSE    The region the tree resides in is the Interior.
	 *
	 *    spName
	 *       The 2 letter name of the species.
	 *
	 *
	 * Return Value
	 * ------------
	 *
	 *    The BC default Site Index curve type to use for this species.
	 *    Returns -2 for non-commercial species.
	 *
	 *
	 * Remarks
	 * -------
	 *
	 *    Within this program, there a number of core "Freddie" routines
	 *    which use this value as an input parameter instead of a species
	 *    name.
	 *
	 *    November 21, 1997
	 *    Added a Coastal/Interior Differentiation for Hemlock.
	 *
	 */
	public static int VDYP_GetDefaultSICurve(String spName, enumSpeciesRegion region) {
		int siCurve;

		if (spName != null && region != null && speciesTable.getByCode(spName) != SpeciesTable.DefaultEntry) {

			int sindxSpcs;
			try {
				sindxSpcs = Sindxdll.SpecRemap(spName, (region == enumSpeciesRegion.spcsRgn_Coast) ? 'A' : 'D');

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

	/*-----------------------------------------------------------------------------
	 *
	 * VDYP_SetCurrentSICurve
	 * ======================
	 *
	 *    Sets the Site Index curve to use for a particular species.
	 *
	 *
	 * Parameters
	 * ----------
	 *
	 *    spName
	 *       The 2 letter name of the species.
	 *
	 *    region
	 *       Indicates which provincial region to set the site curve for,
	 *
	 *    siCurve
	 *       The site index curve to use for the specified species.
	 *       -1  resets the curve to the default.
	 *
	 *
	 * Return Value
	 * ------------
	 *
	 *    The Site Index curve type for the species prior to setting the new
	 *    site index curve.
	 *
	 *
	 * Remarks
	 * -------
	 *
	 *    Within this program, there a number of core "Freddie" routines
	 *    which use this value as an input parameter instead of a species
	 *    name.
	 *
	 *    November 21, 1997
	 *    Added a Coastal/Interior Differentiation for Hemlock.
	 *
	 */
	public static int VDYP_SetCurrentSICurve(String spName, enumSpeciesRegion region, int siCurve) {
		int oldCurve = VDYP_GetCurrentSICurve(spName, region);
		var species = speciesTable.getByCode(spName);
		if (region != null && species != SpeciesTable.DefaultEntry) {

			species.iCrntSICurve()[region.ordinal()] = siCurve;
		}

		return oldCurve;
	}

	/*-----------------------------------------------------------------------------
	 *
	 * VDYP_GetNumSICurves
	 * ===================
	 *
	 *    Determines the number of available SI curves for a particular species.
	 *
	 *
	 * Parameters
	 * ----------
	 *
	 *    respectSpeciesBoundaries
	 *       TRUE   All remaining parameters are used to filter the particular
	 *              set of curves to count.
	 *       FALSE  The count of all curves is returned, regardless of species
	 *              and provincial location.
	 *
	 *    spName
	 *       The 2 letter species name to return the number of curves for.
	 *
	 *    mixInteriorCoastal
	 *       TRUE   Curves from the interior and the coast are counted in the
	 *              total count.  In this case, the 'IsCoastal' parameter is
	 *              ignored.
	 *
	 *       FALSE  Only the curves from the particular region of the province
	 *              specified by the 'isCoastal' parameter are counted.
	 *
	 *    region
	 *       Indicates which provincial region to set the site curve for,
	 *
	 *
	 * Return Value
	 * ------------
	 *
	 *    A count of all of the Site Index curves for a particular species.
	 *    0 if the species is non-commercial or the species is not recognized.
	 *
	 *
	 * Remarks
	 * -------
	 *
	 *    This count is always positive for commercial species.  Always 0 for
	 *    non-commercial species.
	 *
	 *    When mixing interior and coastal curves, the count may be the same as
	 *    when treating these regions separately.
	 *
	 */

	public static int VDYP_GetNumSICurves(
			boolean respectSpeciesBoundaries, String spName, boolean mixInteriorCoastal, enumSpeciesRegion region
	) {
		int numCurves = 0;
		int specNum = VDYP_SpeciesIndex(spName);

		boolean[] countedCurve = new boolean[DefineConstants.SI_MAX_CURVES];
		Arrays.fill(countedCurve, 0, DefineConstants.SI_MAX_CURVES, false);

		// Check if we have a valid species. This doesn't matter if we are not respecting species boundaries.
		if (respectSpeciesBoundaries && (specNum < 0)) {
			return 0;
		}

		// Check if we are going to return a count of all curves regardless of region.
		if (!respectSpeciesBoundaries) {
			return DefineConstants.SI_MAX_CURVES;
		}

		// Count the available coastal curves.
		if (mixInteriorCoastal || enumSpeciesRegion.spcsRgn_Coast.equals(region)) {
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

		if (mixInteriorCoastal || enumSpeciesRegion.spcsRgn_Interior.equals(region)) {
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

	/*-----------------------------------------------------------------------------
	 *
	 * VDYP_GetSICurveSpeciesIndex
	 * ===========================
	 *
	 *    Obtains the SINDEX species index most closely associated with a
	 *    particular site curve.
	 *
	 *
	 * Parameters
	 * ----------
	 *
	 *    siCurve
	 *       The Site Index curve to convert into a species name.
	 *
	 *
	 * Return Value
	 * ------------
	 *
	 *    The SINDEX species index associated with the curve number.
	 *    -1 if the curve was not recognized.
	 *
	 *
	 * Remarks
	 * -------
	 *
	 *    This routine and its associated array must be maintained with the SINDEX
	 *    library.
	 *
	 *    SINDEX will eventually implement a version of this routine which will
	 *    cause this routine to be superfluous.
	 *
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
	public static float VDYP_GetDefaultCC(String spName, enumSpeciesRegion region) {

		// Note that if spName is invalid, the default entry is returned, which in
		// turn contains the right default value of -1.0f.

		return speciesTable.getByCode(spName).fDefaultCC()[region.ordinal()];
	}

	/**
	 * Convert a VDYP7 SP0 Species Name into an enumSP0Name.
	 *
	 * @param sp0Name the VDYP7 Species Name to be converted into a species index.
	 *
	 * @return The enumSP0Name corresponding to the supplied species, and sp0_UNKNOWN if the species name was not
	 *         supplied or was not recognized. Note that the returned index is a zero-based index.
	 */
	public static enumSP0Name VDYP_VDYP7SpeciesIndex(String sp0Name) {
		enumSP0Name sp0Index = enumSP0Name.sp0_UNKNOWN;

		if (sp0Name != null) {
			try {
				sp0Index = enumSP0Name.valueOf(sp0Name.toUpperCase());
			} catch (IllegalArgumentException e) {
				sp0Index = enumSP0Name.sp0_UNKNOWN;
			}
		}

		return sp0Index;
	}

	/**
	 * Converts a species name to its corresponding CFS defined species.
	 * <p>
	 * The list of species mappings is defined in the file 'BCSpcsToCFSSpcs-SAS.txt' found in 'Documents/CFS-Biomass'.
	 * 
	 * @param spName the name of the species to convert.
	 *
	 * @return the mapping to the equivalent CFS defined tree species (if a mapping exists). {@code cfsSpcs_UNKNOWN} is
	 *         returned if the species was not recognized or a mapping does not exist.
	 */
	public static enumIntCFSTreeSpecies SiteTool_GetSpeciesCFSSpcs(String spName) {

		// Note that if spName is not a recognized species name, the correct default value is returned.
		return speciesTable.getByCode(spName).iCFSSpcs();
	}

	/**
	 * Returns the Canadian Forest Service Species Number corresponding to the MoF Species Number.
	 * <p>
	 * The mapping from MoF Species is defined in 'BCSpcsToCFSSpcs-SAS.txt' found in 'Documents/CFS-Biomass'.
	 *
	 * @param spName the name of the species to convert.
	 *
	 * @return the CFS Species Number corresponding to the MoF Species index, and -1 if the species index is not in
	 *         range or there is no mapping from the MoF Species to the CFS Species.
	 */
	public static int SiteTool_GetSpeciesCFSSpcsNum(String spName) {
		int rtrn = -1;
		enumIntCFSTreeSpecies cfsSpcs = SiteTool_GetSpeciesCFSSpcs(spName);

		if (cfsSpcs != enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN) {
			rtrn = SiteTool_CFSSpcsToCFSSpcsNum(cfsSpcs);
		}

		return rtrn;
	}

	/*-----------------------------------------------------------------------------
	 *
	 * SiteTool_HtAgeToSI
	 * ==================
	 *
	 *    Converts a Height and Age to a Site Index for a particular Site Index
	 *    Curve.
	 *
	 *
	 * Parameters
	 * ----------
	 *
	 *    curve
	 *       The particular site index curve to project the height and age along.
	 *       This curve must be one of the active curves defined in "sindex.h"
	 *
	 *    age
	 *       The age of the trees indicated by the curve selection.  The
	 *       interpretation of this age is modified by the 'ageType' parameter.
	 *
	 *    ageType
	 *       Must be one of:
	 *          SI_AT_TOTAL  The age is the total age of the stand in years
	 *                       since planting.
	 *
	 *          SI_AT_BREAST The age indicates the number of years since the
	 *                       stand reached breast height.
	 *
	 *    height
	 *       The height of the species in meters.
	 *
	 *    estType
	 *       Must be one of:
	 *          SI_EST_DIRECT   Compute the site index based on direct equations
	 *                          if available.  If the equations are not available,
	 *                          then automatically fall to the SI_EST_ITERATE
	 *                          method.
	 *
	 *          SI_EST_ITERATE  Compute the site index based on an iterative
	 *                          method which converges on the true site index.
	 *
	 *
	 *
	 * Return Value
	 * ------------
	 *
	 *    The site index of the pure species stand given the height and age.
	 *
	 *    May also return one of the SI_ERR_xxx error codes.
	 *
	 *
	 */

	public static double SiteTool_HtAgeToSI(int curve, double age, int ageType, double height, int estType)
			throws CommonCalculatorException {
		Reference<Double> siRef = new Reference<>();
		int rslt = Sindxdll.HtAgeToSI(curve, age, ageType, height, estType, siRef);
		double SI = siRef.get();

		if (rslt < 0) {
			SI = (double) rslt;
		}

		/*
		 * Round SI off to two decimals.
		 *
		 */

		if (SI >= 0.0) {
			SI = ((int) (SI * 100.0 + 0.5)) / 100.0;
		}

		return SI;
	}

	/*-----------------------------------------------------------------------------
	 *
	 * sitetool_for_htagetosi_
	 * =======================
	 *
	 *    Converts a Height and Age to a Site Index for a particular Site Index
	 *    Curve using a FORTRAN compatible interface.
	 *
	 *
	 * Parameters
	 * ----------
	 *
	 *    curve
	 *       The particular site index curve to project the height and age along.
	 *       This curve must be one of the active curves defined in "sindex.h"
	 *
	 *    age
	 *       The age of the trees indicated by the curve selection.  The
	 *       interpretation of this age is modified by the 'ageType' parameter.
	 *
	 *    ageType
	 *       Must be one of:
	 *          SI_AT_TOTAL    The age is the total age of the stand in years
	 *                         since planting.
	 *
	 *          SI_AT_BREAST   The age indicates the number of years since the
	 *                         stand reached breast height.
	 *
	 *    height
	 *       The height of the species in meters.
	 *
	 *    estType
	 *       Must be one of:
	 *          SI_EST_DIRECT  Compute the site index based on direct equations
	 *                         if available.  If the equations are not available,
	 *                         then automatically fall to the SI_EST_ITERATE
	 *                         method.
	 *
	 *          SI_EST_ITERATE Compute the site index based on an iterative
	 *                         method which converges on the true site index.
	 *
	 *
	 *
	 * Return Value
	 * ------------
	 *
	 *    The site index of the pure species stand given the height and age.
	 *
	 *    May also return one of the SI_ERR_xxx error codes.
	 *
	 *
	 */
	public static double
			SiteTool_HtSIToAge(int curve, double height, int ageType, double siteIndex, double years2BreastHeight)
					throws CommonCalculatorException {
		Reference<Double> tempRef_rtrn = new Reference<>();
		int rslt = Sindxdll.HtSIToAge(curve, height, ageType, siteIndex, years2BreastHeight, tempRef_rtrn);
		double rtrn = tempRef_rtrn.get();

		if (rslt < 0) {
			rtrn = rslt;
		}

		return rtrn;
	}

	/*-----------------------------------------------------------------------------
	 *
	 * sitetool_for_htsitoage_
	 * =======================
	 *
	 *    Converts a Height and Site Index to an Age for a particular Site Index
	 *    Curve using a FORTRAN compatible interface.
	 *
	 *
	 * Parameters
	 * ----------
	 *
	 *    curve
	 *       The particular site index curve to project the height and age along.
	 *       This curve must be one of the active curves defined in "sindex.h"
	 *
	 *    height
	 *       The height of the species in meters.
	 *
	 *    ageType
	 *       Must be one of:
	 *          SI_AT_TOTAL    The age is the total age of the stand in years
	 *                         since planting.
	 *
	 *          SI_AT_BREAST   The age indicates the number of years since the
	 *                         stand reached breast height.
	 *
	 *    siteIndex
	 *       The site index value of the stand.
	 *
	 *    years2BreastHeight
	 *       The number of years it takes the stand to reach breast height.
	 *
	 *
	 * Return Value
	 * ------------
	 *
	 *    The age of the stand (given the ageType) at which point it has reached
	 *    the height specified.
	 *
	 *    May also return one of the SI_ERR_xxx error codes.
	 *
	 */
	public static double
			SiteTool_AgeSIToHt(int curve, double age, int ageType, double siteIndex, double years2BreastHeight)
					throws CommonCalculatorException {
		int freddieCurve = (int) curve;
		int freddieAgeType = (int) ageType;
		double freddieAge = (double) age;
		double freddieSI = (double) siteIndex;
		double freddieY2BH = (double) years2BreastHeight;

		Reference<Double> tempRef_rtrn = new Reference<>();
		int rslt = Sindxdll.AgeSIToHt(freddieCurve, freddieAge, freddieAgeType, freddieSI, freddieY2BH, tempRef_rtrn);

		double rtrn;
		if (rslt == 0) {
			rtrn = tempRef_rtrn.get();
		} else {
			rtrn = rslt;
		}
		return rtrn;
	}

	public static double SiteTool_YearsToBreastHeight(int curve, double siteIndex) {
		double rtrn = 0.0;
		int rslt;

		Reference<Double> tempRef_rtrn = new Reference<>(rtrn);
		try {
			rslt = Sindxdll.Y2BH(curve, siteIndex, tempRef_rtrn);
		} catch (CommonCalculatorException e) {
			rslt = -1;
		}

		if (rslt < 0) {
			rtrn = (double) rslt;
		} else {
			rtrn = tempRef_rtrn.get();
		}

		// Round off to 1 decimal.
		if (rtrn >= 0.0) {
			rtrn = Math.round((int) (rtrn * 10.0 + 0.5)) / 10.0;
		}

		return rtrn;
	}

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

	/*-----------------------------------------------------------------------------
	 *
	 * SiteTool_SpeciesIndex
	 * =====================
	 *
	 *    Converts a species name into an internal index.
	 *
	 *
	 * Parameters
	 * ----------
	 *
	 *    spcsCodeName
	 *       The species int name to be converted into a SITETOOL index.
	 *
	 *
	 * Return Value
	 * ------------
	 *
	 *    The internal index of the species.
	 *    -1 if the species was not recognized.
	 *
	 *
	 * Remarks
	 * -------
	 *
	 *    When referring to species, the curves index is zero based and
	 *    valid indices run from 0 to n-1 where n is the number returned
	 *    by this function.
	 *
	 */
	public static int SiteTool_SpeciesIndex(String spcsCodeName) {
		return VDYP_SpeciesIndex(spcsCodeName);
	}

	public static String SiteTool_SpeciesShortName(int spcsIndx) {
		return VDYP_GetSpeciesShortName(enumSP64Name.forValue(spcsIndx));
	}

	public static String SiteTool_SpeciesFullName(String spcsCode) {
		return VDYP_GetSpeciesFullName(enumSP64Name.forText(spcsCode));
	}

	public static String SiteTool_SpeciesLatinName(String spcsCode) {
		return VDYP_GetSpeciesLatinName(enumSP64Name.forText(spcsCode));
	}

	public static String SiteTool_SpeciesGenusCode(String spcsCode) {
		return VDYP_GetSpeciesGenus(enumSP64Name.forText(spcsCode));
	}

	public static String SiteTool_SpeciesSINDEXCode(String spcsCode, boolean isCoastal) {
		return VDYP_GetSINDEXSpecies(
				spcsCode, isCoastal ? enumSpeciesRegion.spcsRgn_Coast : enumSpeciesRegion.spcsRgn_Interior
		);
	}

	/*-----------------------------------------------------------------------------
	 *
	 *	SiteTool_VB_SpeciesSINDEXCode
	 *	=============================
	 *
	 *		Translates the species code into the corresponding SINDEX code.using a
	 *		Visual Basic Interface.
	 *
	 *
	 *	Parameters
	 *	----------
	 *
	 *		spcsSINDEXCode
	 *			The buffer to place the SINDEX code of the species into.
	 *
	 *		spcsBufrLen
	 *			Input:	specifies the length of the 'spcsSINDEXCode' buffer.
	 *			Output:	specifies the length of the SINDEX species code.
	 *
	 *		spcsCode
	 *			The species code you wish converted.
	 *
	 *    isCoastal
	 *       TRUE     Indicates the species is residing in a coastal location.
	 *       FALSE    Indicates the species is residing in an interior location.
	 *
	 *
	 *	Return Value
	 *	------------
	 *
	 *		The string indicating the SINDEX code is returned through
	 *		the 'spcsSINDEXCode' and 'spcsBufrLen' parameters.
	 *
	 *    "??" is returned if there is an error.
	 *
	 *
	 * Remarks
	 * -------
	 *
	 *		If the buffer or buffer length are not supplied or the buffer is
	 *		not long enough, a string of "" is returned.
	 *
	 *    SINDEX codes appear as two character species codes and can in fact
	 *    be used as if they are species.
	 *
	 *    The species recognized by SINDEX is a subset of the total number of
	 *    species.  This routine provides the mapping from the universal set to
	 *    the restricted SINDEX set.
	 *
	 */

	/*-----------------------------------------------------------------------------
	 *
	 * SiteTool_VB_SpeciesSINDEXCode
	 * =============================
	 *
	 *    Translates the species code into the corresponding SINDEX code.using a
	 *    Visual Basic Interface.
	 *
	 *
	 * Parameters
	 * ----------
	 *
	 *    spcsSINDEXCode
	 *       The buffer to place the SINDEX code of the species into.
	 *
	 *    spcsBufrLen
	 *       Input:   specifies the length of the 'spcsSINDEXCode' buffer.
	 *       Output:  specifies the length of the SINDEX species code.
	 *
	 *    spcsCode
	 *       The species code you wish converted.
	 *
	 *    isCoastal
	 *       TRUE     Indicates the species is residing in a coastal location.
	 *       FALSE    Indicates the species is residing in an interior location.
	 *
	 *
	 * Return Value
	 * ------------
	 *
	 *    The string indicating the SINDEX code is returned through
	 *    the 'spcsSINDEXCode' and 'spcsBufrLen' parameters.
	 *
	 *    "??" is returned if there is an error.
	 *
	 *
	 * Remarks
	 * -------
	 *
	 *    If the buffer or buffer length are not supplied or the buffer is
	 *    not long enough, a string of "" is returned.
	 *
	 *    SINDEX codes appear as two character species codes and can in fact
	 *    be used as if they are species.
	 *
	 *    The species recognized by SINDEX is a subset of the total number of
	 *    species.  This routine provides the mapping from the universal set to
	 *    the restricted SINDEX set.
	 *
	 */
	public static String SiteTool_SpeciesVDYP7Code(String spcsCode) {

		return VDYP_GetVDYP7Species(spcsCode);
	}

	public static int SiteTool_SetSICurve(String spcsCode, boolean isCoastal, int iCurve) {

		enumSpeciesRegion region = (isCoastal ? enumSpeciesRegion.spcsRgn_Coast
				: enumSpeciesRegion.spcsRgn_Interior);

		return VDYP_SetCurrentSICurve(spcsCode, region, iCurve);
	}

	/*-----------------------------------------------------------------------------
	 *
	 * SiteTool_GetSICurve
	 * ===================
	 *
	 *    Maps a Species Code Name to a specific SI Curve.
	 *
	 *
	 * Parameters
	 * ----------
	 *
	 *    spcsCode
	 *       The two letter code corresponding to a particular species.
	 *
	 *    isCoastal
	 *       TRUE     Species is in a coastal region.
	 *       FALSE    Species is in an interior region.
	 *
	 *
	 * Return Value
	 * ------------
	 *
	 *    The SI Curve number for the species.
	 *    SI_ERR_CODE    if the species was not recognized.
	 *
	 *
	 * Remarks
	 * -------
	 *
	 *    Maps the species to an internal hard-coded default.
	 *
	 *    October 16, 2000
	 *    Changed the FIZ parameter to a simple Interior/Coast indicator.
	 *
	 */

	public static int SiteTool_GetSICurve(String spcsCode, boolean isCoastal) {

		int rtrn = VDYP_GetCurrentSICurve(
				spcsCode, isCoastal ? enumSpeciesRegion.spcsRgn_Coast : enumSpeciesRegion.spcsRgn_Interior
		);

		return rtrn;
	}

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

	public static float SiteTool_SpeciesDefaultCC(String spcsCode, boolean isCoastal) {
		return VDYP_GetDefaultCC(
				spcsCode, (isCoastal ? enumSpeciesRegion.spcsRgn_Coast : enumSpeciesRegion.spcsRgn_Interior)
		);
	}

	public static String SiteTool_SINDEXErrorToString(int iSINDEXError) {
		String rtrnStr = null;

		switch (iSINDEXError) {
		case 0:
			rtrnStr = "SUCCESS";
			break;

		case DefineConstants.SI_ERR_LT13:
			rtrnStr = "SI_ERR_LT13";
			break;
		case DefineConstants.SI_ERR_GI_MIN:
			rtrnStr = "SI_ERR_GI_MIN";
			break;
		case DefineConstants.SI_ERR_GI_MAX:
			rtrnStr = "SI_ERR_GI_MAX";
			break;
		case DefineConstants.SI_ERR_NO_ANS:
			rtrnStr = "SI_ERR_NO_ANS";
			break;
		case DefineConstants.SI_ERR_CURVE:
			rtrnStr = "SI_ERR_CURVE";
			break;
		case DefineConstants.SI_ERR_CLASS:
			rtrnStr = "SI_ERR_CLASS";
			break;
		case DefineConstants.SI_ERR_FIZ:
			rtrnStr = "SI_ERR_FIZ";
			break;
		case DefineConstants.SI_ERR_CODE:
			rtrnStr = "SI_ERR_CODE";
			break;
		case DefineConstants.SI_ERR_GI_TOT:
			rtrnStr = "SI_ERR_GI_TOT";
			break;
		case DefineConstants.SI_ERR_SPEC:
			rtrnStr = "SI_ERR_SPEC";
			break;
		case DefineConstants.SI_ERR_AGE_TYPE:
			rtrnStr = "SI_ERR_AGE_TYPE";
			break;
		case DefineConstants.SI_ERR_ESTAB:
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
	public static int SiteTool_FillInAgeTriplet(
			Reference<Double> rTotalAge, Reference<Double> rBreastHeightAge,
			Reference<Double> rYTBH
	) {
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

	/*-----------------------------------------------------------------------------
	 *
	 * SiteTool_IsDeciduous
	 * ====================
	 *
	 *    Determines if the supplied species is a deciduous or coniferous species.
	 *
	 *
	 * Parameters
	 * ----------
	 *
	 *    spcsIndex
	 *       The species index you wish to determine the deciduous status of.
	 *
	 *
	 * Return Value
	 * ------------
	 *
	 *    TRUE     The supplied species is a deciduous species.
	 *    FALSE    The supplied species is coniferous or is unknown.
	 *
	 */

	public static boolean SiteTool_IsDeciduous(int spcsIndx) {

		String spcsName = VDYP_GetSpeciesShortName(enumSP64Name.forValue(spcsIndx));
		return VDYP_IsDeciduous(spcsName);
	}
}