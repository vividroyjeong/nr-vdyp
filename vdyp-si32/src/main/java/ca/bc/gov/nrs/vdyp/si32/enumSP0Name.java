package ca.bc.gov.nrs.vdyp.si32;

import java.text.MessageFormat;

/**
 * An enumeration holding each of the valid SP0 Species known to VDYP7.
 */
public enum enumSP0Name implements SI32Enum<enumSP0Name> {
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

	private int intValue;
	private static java.util.HashMap<Integer, enumSP0Name> mappings;

	private static java.util.HashMap<Integer, enumSP0Name> getMappings() {
		if (mappings == null) {
			synchronized (enumSP0Name.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, enumSP0Name>();
				}
			}
		}
		return mappings;
	}

	private enumSP0Name(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	@Override
	public int getValue() {
		return intValue;
	}

	@Override
	public int getIndex() {
		if (this.equals(sp0_UNKNOWN)) {
			throw new UnsupportedOperationException(MessageFormat.format("Cannot call getIndex on {} as it's not a standard member of the enumeration", this));
		}
		
		return intValue;
	}
	
	@Override
	public String getText() {
		if (this.equals(sp0_UNKNOWN)) {
			throw new UnsupportedOperationException(MessageFormat
					.format("Cannot call getText on {} as it's not a standard member of the enumeration", this));
		}
		
		return this.toString().substring("sp0_".length());
	}

	public static enumSP0Name forValue(int value) {
		return getMappings().get(value);
	}
	
	public static int size() {
		return sp0_Y.intValue - sp0_AC.intValue + 1;
	}

	public static class Iterator extends SI32EnumIterator<enumSP0Name> {
		public Iterator() {
			super(sp0_AC, sp0_Y, mappings);
		}
	}
}
