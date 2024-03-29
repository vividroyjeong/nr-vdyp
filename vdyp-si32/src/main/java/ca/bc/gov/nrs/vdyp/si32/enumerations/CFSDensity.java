package ca.bc.gov.nrs.vdyp.si32.enumerations;

public enum CFSDensity {
	MEAN_DENSITY_INDEX(0),
	MIN_DENSITY_INDEX(1),
	MAX_DENSITY_INDEX(2);

	public final int Index;
	
	CFSDensity(int index) {
		this.Index = index;
	}
}
