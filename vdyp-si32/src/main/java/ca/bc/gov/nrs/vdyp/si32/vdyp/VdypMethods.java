package ca.bc.gov.nrs.vdyp.si32.vdyp;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.CodeErrorException;
import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.CommonCalculatorException;
import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.CurveErrorException;
import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.ForestInventoryZoneException;
import ca.bc.gov.nrs.vdyp.si32.enumerations.SpeciesRegion;
import ca.bc.gov.nrs.vdyp.sindex.Sindxdll;

public class VdypMethods {
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(VdypMethods.class);

	private static final int SI_MAX_CURVES = 123;

	public static SpeciesTable speciesTable = new SpeciesTable();

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
	 * Converts a CFS species code name into an index into the Species table.
	 * 
	 * @param sp64Name the name of the CFS species.
	 * @return the index into the Species table for the corresponding species, and SpeciesTable.UNKNOWN_ENTRY_INDEX if
	 *         the species was not recognized.
	 */
	public static int VDYP_SpeciesIndex(String sp64Name) {
		int result;

		if (StringUtils.isNotBlank(sp64Name)) {
			try {
				SpeciesTableItem spsc = speciesTable.getByCode(sp64Name.toUpperCase());
				result = spsc.cfsSpecies().getOffset();
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
	public static boolean VDYP_IsValidSpecies(String sp64Name) {
		return VDYP_SpeciesIndex(sp64Name) >= 0;
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
	 * Converts a CFS species name to an equivalent VDYP7 species (genus) name.
	 * 
	 * @param sp64Name the name of the species to convert.
	 * @return the equivalent VDYP7 species code. "" is returned if the 
	 * species is not supported by VDYP7 and no mapping exists.
	 */
	public static String VDYP_GetVDYP7Species(String sp64Name) {
		return speciesTable.getByCode(sp64Name).sp0Name();
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
				sp0Index = SP0Name.forText(sp0Name);
			} catch (IllegalArgumentException e) {
				sp0Index = SP0Name.sp0_UNKNOWN;
			}
		}

		return sp0Index;
	}
}