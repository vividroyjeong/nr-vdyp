package ca.bc.gov.nrs.vdyp.model;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;

public class SpeciesDistribution implements Comparable<SpeciesDistribution> {
	private final String species;
	private final Float percentage;

	public SpeciesDistribution(String species, Float percentage) {
		this.species = species;
		this.percentage = percentage;
	}

	public String getSpecies() {
		return species;
	}

	public Float getPercentage() {
		return percentage;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof SpeciesDistribution that) {
			return Objects.equals(this.percentage, that.percentage);
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() { 
		return ((species != null ? species.hashCode() : 0) * 17) + (percentage != null ? percentage.hashCode() : 0);
	}

	@Override
	public int compareTo(SpeciesDistribution that) {
		if (that == null)
		    return 1;
		int cr = ObjectUtils.compare(this.species, that.species);
		if (cr == 0)
			return ObjectUtils.compare(this.percentage, that.percentage);
		else
			return cr;
	}
}
