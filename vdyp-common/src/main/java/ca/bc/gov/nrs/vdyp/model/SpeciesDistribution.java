package ca.bc.gov.nrs.vdyp.model;

import java.text.MessageFormat;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;

public class SpeciesDistribution implements Comparable<SpeciesDistribution> {
	private final int index;
	private final String species;
	private final float percentage;

	public SpeciesDistribution(int index, String species, float percentage) {
		if (species == null || species.trim().length() == 0) {
			throw new IllegalArgumentException(MessageFormat.format("Species for index {0} is missing", species));
		}
		if (percentage < 0 || percentage > 100) {
			throw new IllegalArgumentException(MessageFormat.format("Percentage value {0} for index {1} must be between 0 and 100", index, percentage));
		}
	
		this.index = index;
		this.species = species;
		this.percentage = percentage;
	}

	public int getIndex() {
		return index;
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
			return this.index == that.index && Objects.equals(this.species, that.species)
					&& this.percentage == that.percentage;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return ((index * 17) * (species != null ? species.hashCode() : 0) * 17) + Float.valueOf(percentage).hashCode();
	}

	@Override
	public int compareTo(SpeciesDistribution that) {
		if (that == null)
			return 1;
		int cr = this.index - that.index;
		if (cr == 0) {
			cr = ObjectUtils.compare(this.species, that.species);
			if (cr == 0)
				cr = ObjectUtils.compare(this.percentage, that.percentage);
		}
		
		return cr;
	}
}
