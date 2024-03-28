package ca.bc.gov.nrs.vdyp.si32;

public interface SI32Enum<T> {
	/**
	 * @return the integer value of the enumeration entry.
	 */
	public int getValue();

	/**
	 * @return the index of the enumeration entry. This is the relative
	 * position of the entry in the enumeration, ignoring any "housekeeping"
	 * entries (such as Unknown and the like.)
	 * 
	 * @throws IllegalArgumentException if this is a housekeeping entry.
	 */
	int getIndex();

	/**
	 * @return the text identifying the enumeration entry. This cannot be 
	 * called on "housekeeping" entries.
	 * 
	 * @throws IllegalArgumentException if this is a housekeeping entry.
	 */
	String getText();
}
