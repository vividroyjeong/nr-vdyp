package ca.bc.gov.nrs.vdyp.si32.enumerations;

/**
 * Specifies where in the Species to Genus to default lookup process a set of CFS parameters
 * were resolved from.
 */
public enum ParamLookupFallback implements SI32Enum<ParamLookupFallback> {
	cfsLookup_Species, //
	cfsLookup_Genus, //
	cfsLookup_DefaultConifer, //
	cfsLookup_DefaultHardwood, //
	cfsLookup_NotFound;

	@Override
	public int getValue() {
		return this.ordinal();
	}
	
	@Override
	public String getText() {
		return this.toString().substring("cfsLookup_".length());
	}

	public static ParamLookupFallback forValue(int value) {
		return values()[value];
	}

	@Override
	public int getIndex() {
		return this.ordinal();
	}
}
