package ca.bc.gov.nrs.vdyp.model;

public enum Layer {
	PRIMARY("P"), SECONDARY("S"), VETERAN("V");
	private final String alias;

	private Layer(String alias) {
		this.alias = alias;
	}

	public String getAlias() {
		return alias;
	}
}
