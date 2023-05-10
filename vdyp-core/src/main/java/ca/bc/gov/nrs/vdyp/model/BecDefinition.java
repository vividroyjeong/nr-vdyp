package ca.bc.gov.nrs.vdyp.model;

public class BecDefinition {
	
	final String alias;
	final Region region;
	final String name;
	
	public BecDefinition(String alias, Region region, String name) {
		super();
		this.alias = alias;
		this.region = region;
		this.name = name;
	}
	
	public String getAlias() {
		return alias;
	}
	public Region getRegion() {
		return region;
	}
	public String getName() {
		return name;
	}
}
