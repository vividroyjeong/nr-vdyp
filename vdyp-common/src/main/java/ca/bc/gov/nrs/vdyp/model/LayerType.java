package ca.bc.gov.nrs.vdyp.model;

public enum LayerType {
	PRIMARY("P"), SECONDARY("S"), VETERAN("V");

	private final String alias;

	private LayerType(String alias) {
		this.alias = alias;
	}

	public String getAlias() {
		return alias;
	}
}