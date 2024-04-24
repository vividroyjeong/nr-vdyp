package ca.bc.gov.nrs.vdyp.si32.site;

import ca.bc.gov.nrs.vdyp.si32.enumerations.SI32Enum;

/**
 * Specifies where in the Species to Genus to default lookup process a set of CFS parameters
 * were resolved from.
 */
public enum ParamLookupFallback implements SI32Enum<ParamLookupFallback> {
	Species, //
	Genus, //
	DefaultConifer, //
	DefaultHardwood, //
	NotFound;

	@Override
	public int getIndex() {
		return this.ordinal();
	}
	
	@Override
	public String getText() {
		return toString();
	}

	@Override
	public int getOffset() {
		return this.ordinal();
	}
}
