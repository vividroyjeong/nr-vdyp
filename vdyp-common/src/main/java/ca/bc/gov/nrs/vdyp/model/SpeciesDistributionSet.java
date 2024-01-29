package ca.bc.gov.nrs.vdyp.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpeciesDistributionSet {
	private Map<String, SpeciesDistribution> speciesDistributionMap = new HashMap<>();

	public SpeciesDistributionSet(List<SpeciesDistribution> speciesDistributionList) {
		for (SpeciesDistribution sd : speciesDistributionList) {
			speciesDistributionMap.put(sd.getSpecies(), sd);
		}
	}

	public Map<String, SpeciesDistribution> getSpeciesDistributionMap() {
		return Collections.unmodifiableMap(speciesDistributionMap);
	}

	Float getSpeciesDistribution(String species) {
		if (speciesDistributionMap.containsKey(species))
			return speciesDistributionMap.get(species).getPercentage();
		else
			return 0.0f;
	}
}
