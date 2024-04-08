package ca.bc.gov.nrs.vdyp.si32.cfs;

import java.util.HashMap;
import java.util.Map;

/**
 * Methods supporting CFS Tree Species.
 */
public class CfsSpeciesMethods {

	private static Map<String, CfsTreeSpecies> speciesByName = new HashMap<>();
	
	/**
	 * Perform a case-insensitive search for the attributes of the species with
	 * the given name. If the parameter is null or doesn't match any species,
	 * CFSTreeSpecies.cfsSpcs_UNKNOWN is returned.
	 * 
	 * @param cfsSpeciesName the name of the species to look up
	 * @return as described
	 */
	public static CfsTreeSpecies getSpeciesBySpeciesName(String cfsSpeciesName) {
		
		if (cfsSpeciesName != null) {
			String cfsSpeciesNameUpperCase = cfsSpeciesName.toUpperCase();
			if (speciesByName.containsKey(cfsSpeciesNameUpperCase)) {
				return speciesByName.get(cfsSpeciesNameUpperCase);
			}
		}
		
		return CfsTreeSpecies.cfsSpcs_UNKNOWN;
	}

	/**
	 * Return the genus of the given species, or CFSTreeGenus.cfsGenus_UNKNOWN 
	 * if null.
	 * 
	 * @param cfsSpecies the name of the species to look up
	 * @return as described
	 */
	public static CfsTreeGenus getGenusBySpecies(CfsTreeSpecies cfsSpecies) {
		
		if (cfsSpecies != null) {
			return cfsSpecies.getCfsTreeGenus();
		} else {
			return CfsTreeGenus.cfsGenus_UNKNOWN;
		}
	}
	
	/**
	 * Returns the species index for the given CFS species, or that of <code>cfsSpcs_UNKNOWN</code> if null.
	 * 
	 * @param cfsSpecies the name of the species to look up
	 * @return as described
	 */
	public static int getSpeciesIndexBySpecies(CfsTreeSpecies cfsSpecies) {
		
		if (cfsSpecies != null) {
			return cfsSpecies.getCfsSpeciesNumber();
		} else {
			return CfsTreeSpecies.cfsSpcs_UNKNOWN.getCfsSpeciesNumber();
		}
	}

	static {
		// Build the lookup assistance maps
		for (CfsTreeSpecies s: CfsTreeSpecies.values()) {
			speciesByName.put(s.getCfsSpeciesName().toUpperCase(), s);
		}
	}
}
