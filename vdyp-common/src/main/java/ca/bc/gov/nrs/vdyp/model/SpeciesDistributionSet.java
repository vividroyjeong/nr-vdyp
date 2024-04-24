package ca.bc.gov.nrs.vdyp.model;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import ca.bc.gov.nrs.vdyp.io.parse.common.InvalidSpeciesDistributionSet;

public class SpeciesDistributionSet implements Comparable<SpeciesDistributionSet> {
	
	private Map<Integer, SpeciesDistribution> speciesDistributionMap = new HashMap<>();
	private int maxIndex;
	
	public SpeciesDistributionSet(int maxIndex, List<SpeciesDistribution> sdList) {
		
		try {
			validate(maxIndex, sdList);
		} catch (InvalidSpeciesDistributionSet e) {
			throw new IllegalArgumentException(e);
		}
		
		for (SpeciesDistribution sd: sdList) {
			speciesDistributionMap.put(sd.getIndex(), sd);
		}
		
		this.maxIndex = maxIndex;
	}

	private SpeciesDistributionSet(SpeciesDistributionSet other) {
		speciesDistributionMap = new HashMap<>();

		for (var e : other.speciesDistributionMap.entrySet()) {
			speciesDistributionMap
					.put(
							e.getKey(), new SpeciesDistribution(
									e.getValue().getIndex(),
									e.getValue().getSpecies(), e.getValue().getPercentage()
							)
					);
		}
	}

	public Map<Integer, SpeciesDistribution> getSpeciesDistributionMap() {
		return Collections.unmodifiableMap(speciesDistributionMap);
	}
	
	public Optional<SpeciesDistribution> getSpeciesDistribution(int index) {
		if (index > maxIndex) {
			throw new IllegalArgumentException(MessageFormat.format("Index {0} exceeds the maximum value {1}", index, maxIndex));
		}
		if (speciesDistributionMap.containsKey(index))
			return Optional.of(speciesDistributionMap.get(index));
		else
			return Optional.empty();
	}

	public SpeciesDistributionSet copy() {
		return new SpeciesDistributionSet(this);
	}

	public static void validate(int maxIndex, List<SpeciesDistribution> sdList)
			throws InvalidSpeciesDistributionSet {
		if (sdList == null || sdList.size() == 0) {
			throw new InvalidSpeciesDistributionSet("SpeciesDistributionSet does not have the required first entry");
		}

		Set<String> sp0Seen = new HashSet<>();
		Set<Integer> indicesSeen = new HashSet<>();
		
		float distributionTotal = 0.0f;

		for (SpeciesDistribution sd : sdList) {
			if (sp0Seen.contains(sd.getSpecies())) {
				throw new InvalidSpeciesDistributionSet(
						MessageFormat.format(
								"Species {0} appears more than once in SpeciesDistributionSet", sd.getSpecies()
						)
				);
			}
			if (sp0Seen.contains(sd.getSpecies())) {
				throw new InvalidSpeciesDistributionSet(
						MessageFormat.format(
								"Species {0} appears more than once in SpeciesDistributionSet", sd.getSpecies()
						)
				);
			}
			if (indicesSeen.contains(sd.getIndex())) {
				throw new InvalidSpeciesDistributionSet(
						MessageFormat.format(
								"Index {0} appears more than once in SpeciesDistributionSet", sd.getIndex()
						)
				);				
			}
			if (sd.getIndex() < 0 || sd.getIndex() > maxIndex) {
				throw new InvalidSpeciesDistributionSet(
						MessageFormat.format(
								"Index {0} is out of range - acceptable values are between 0 and {1}, inclusive", sd.getIndex(), maxIndex
						)
				);								
			}
			sp0Seen.add(sd.getSpecies());
			indicesSeen.add(sd.getIndex());
			distributionTotal += sd.getPercentage();
		}

		if (distributionTotal > 100.05f || distributionTotal < 99.95) {
			throw new InvalidSpeciesDistributionSet(
					MessageFormat
							.format("Sum {0} of species distributions isn't within 0.05 of 100.0", distributionTotal)
			);
		}
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof SpeciesDistributionSet that) {
			return compareTo(that) == 0;
		} else {
			return false;
		}
	}

	@Override
	public int compareTo(SpeciesDistributionSet that) {
		if (that != null) {
			if (that.speciesDistributionMap.size() != speciesDistributionMap.size()) {
				return speciesDistributionMap.size() - that.speciesDistributionMap.size();
			}

			for (SpeciesDistribution sd: this.speciesDistributionMap.values()) {
				if (that.speciesDistributionMap.containsKey(sd.getIndex())) {
					var result = sd.compareTo(that.speciesDistributionMap.get(sd.getIndex()));
					if (result != 0) {
						return result;
					}
				}
			}

			return 0;
		} else {
			// null is less than non-null, per ObjectUtils.compare()
			return 1;
		}
	}
}