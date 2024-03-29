package ca.bc.gov.nrs.vdyp.si32.enumerations;

/**
 * When converting an enumeration constant to a name, this enumeration indicates in which format that name should take.
 */
public enum NameFormat {
	catOnly, nameOnly, catName, enumStr;

	public int getValue() {
		return this.ordinal();
	}

	public static NameFormat forValue(int value) {
		return values()[value];
	}
}
