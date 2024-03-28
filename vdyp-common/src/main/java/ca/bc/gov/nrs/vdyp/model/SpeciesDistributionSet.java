package ca.bc.gov.nrs.vdyp.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SpeciesDistributionSet {
	private Map<String, SpeciesDistribution> speciesDistributionMap = new HashMap<>();

	public SpeciesDistributionSet(List<SpeciesDistribution> speciesDistributionList) {
		for (SpeciesDistribution sd : speciesDistributionList) {
			speciesDistributionMap.put(sd.getSpecies(), sd);
		}
	}

	private SpeciesDistributionSet(SpeciesDistributionSet other) {
		speciesDistributionMap = new HashMap<>();
		
		for (var e: other.speciesDistributionMap.entrySet()) {
			speciesDistributionMap.put(e.getKey(), new SpeciesDistribution(e.getValue().getSpecies(), e.getValue().getPercentage()));
		}
	}

	public Map<String, SpeciesDistribution> getSpeciesDistributionMap() {
		return Collections.unmodifiableMap(speciesDistributionMap);
	}

	public Optional<Float> getSpeciesDistribution(String species) {
		if (speciesDistributionMap.containsKey(species))
			return Optional.of(speciesDistributionMap.get(species).getPercentage());
		else
			return Optional.empty();
	}

	public SpeciesDistributionSet copy() {
		return new SpeciesDistributionSet(this);
	}
}