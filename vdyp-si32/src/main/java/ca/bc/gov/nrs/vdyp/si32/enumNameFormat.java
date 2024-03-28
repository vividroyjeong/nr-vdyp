package ca.bc.gov.nrs.vdyp.si32;

/**
 * When converting an enumeration constant to a name, this enumeration indicates in which format that name should take.
 */
public enum enumNameFormat {
	catOnly, nameOnly, catName, enumStr;

	public int getValue() {
		return this.ordinal();
	}

	public static enumNameFormat forValue(int value) {
		return values()[value];
	}
}
