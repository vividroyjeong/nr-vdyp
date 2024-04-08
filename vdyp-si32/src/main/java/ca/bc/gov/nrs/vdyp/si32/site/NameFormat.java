package ca.bc.gov.nrs.vdyp.si32.site;

import ca.bc.gov.nrs.vdyp.si32.enumerations.SI32Enum;

/**
 * When converting an enumeration constant to a name, this enumeration indicates in which format that name should take.
 */
public enum NameFormat implements SI32Enum<NameFormat> {
	catOnly, nameOnly, catName, enumStr;

	@Override
	public int getIndex() {
		return ordinal();
	}

	@Override
	public int getOffset() {
		return ordinal();
	}

	@Override
	public String getText() {
		return toString();
	}
}
