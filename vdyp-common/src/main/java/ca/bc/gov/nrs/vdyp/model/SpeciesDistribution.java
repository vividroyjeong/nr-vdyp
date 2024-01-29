package ca.bc.gov.nrs.vdyp.model;

public class SpeciesDistribution {
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
}
