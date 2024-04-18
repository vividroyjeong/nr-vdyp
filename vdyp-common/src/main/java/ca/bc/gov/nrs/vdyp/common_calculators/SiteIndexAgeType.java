package ca.bc.gov.nrs.vdyp.common_calculators;

public enum SiteIndexAgeType {
	TOTAL((short) 0), BREAST_HEIGHT((short) 1);

	private final short index;

	SiteIndexAgeType(short i) {
		index = i;
	}

	public short getIndex() {
		return index;
	}

}
