package ca.bc.gov.nrs.vdyp.si32.vdyp;

import java.text.MessageFormat;

import ca.bc.gov.nrs.vdyp.si32.enumerations.SI32Enum;
import ca.bc.gov.nrs.vdyp.si32.enumerations.SI32EnumIterator;

/**
 * An enumeration holding each of the valid SP0 genera known to VDYP7.
 */
public enum SP0Name implements SI32Enum<SP0Name> {
	sp0_UNKNOWN(-1),

	sp0_AC(0), //
	sp0_AT(1), //
	sp0_B(2), //
	sp0_C(3), //
	sp0_D(4), //
	sp0_E(5), //
	sp0_F(6), //
	sp0_H(7), //
	sp0_L(8), //
	sp0_MB(9), //
	sp0_PA(10), //
	sp0_PL(11), //
	sp0_PW(12), //
	sp0_PY(13), // 
	sp0_S(14), //
	sp0_Y(15);

	private final int index;

	private SP0Name(int index) {
		this.index = index;
	}

	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public int getOffset() {
		if (this.equals(sp0_UNKNOWN)) {
			throw new UnsupportedOperationException(MessageFormat.format("Cannot call getOffset on {0} as it's not a standard member of the enumeration", this));
		}
		
		return index;
	}
	
	@Override
	public String getText() {
		if (this.equals(sp0_UNKNOWN)) {
			throw new UnsupportedOperationException(MessageFormat
					.format("Cannot call getText on {0} as it's not a standard member of the enumeration", this));
		}
		
		return this.toString().substring("sp0_".length());
	}

	/**
	 * Returns the enumeration constant corresponding to the given text value.
	 * @param text the text value in question. Example: "AC". The value is converted to upper-case before
	 *     the lookup.
	 * @return the enumeration value, unless <code>text</code> is null or 
	 */
	public static SP0Name forText(String text) {
		try {
			return valueOf("sp0_" + text.toUpperCase());
		} catch (IllegalArgumentException | NullPointerException e) {
			return sp0_UNKNOWN;
		}
	}

	/**
	 * Returns the enumeration constant with the given index.
	 * @param index the value in question
	 * @return the enumeration value, unless no enumeration constant has the given 
	 * 	   <code>index</code> in which case <code>null</code> is returned.
	 */
	public static SP0Name forIndex(int index) {
		for (SP0Name e: SP0Name.values()) {
			if (index == e.index)
				return e;
		}
		
		return null;
	}
	
	/**
	 * @return the number of non-housekeeping entries in the enumeration
	 */
	public static int size() {
		return sp0_Y.index - sp0_AC.index + 1;
	}

	public static class Iterator extends SI32EnumIterator<SP0Name> {
		public Iterator() {
			super(sp0_AC, sp0_Y, values());
		}
	}
}
