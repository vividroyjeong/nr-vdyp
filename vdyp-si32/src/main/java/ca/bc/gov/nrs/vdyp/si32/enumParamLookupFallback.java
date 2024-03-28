package ca.bc.gov.nrs.vdyp.si32;

/**
 * Specifies where in the Species to Genus to default lookup process a set of CFS parameters were resolved from.
 */
public enum enumParamLookupFallback {
	cfsLookup_Species, //
	cfsLookup_Genus, //
	cfsLookup_DefaultConifer, //
	cfsLookup_DefaultHardwood, //
	cfsLookup_NotFound;

	public int getValue() {
		return this.ordinal();
	}
	
	public String getText() {
		return this.toString().substring("cfsLookup_".length());
	}

	public static enumParamLookupFallback forValue(int value) {
		return values()[value];
	}
}
