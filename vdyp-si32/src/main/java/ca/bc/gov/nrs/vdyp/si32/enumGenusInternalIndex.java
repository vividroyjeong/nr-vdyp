package ca.bc.gov.nrs.vdyp.si32;

import java.text.MessageFormat;

/**
 * Lists the indices for CFS Genus for looking up CFS Conversion Params at the Genus level.
 * <p>
 * These values are used as an index into the {@code cfsGenusBiomassConversionCoefficients} array.
 */
public enum enumGenusInternalIndex implements SI32Enum<enumGenusInternalIndex> {
	genusInt_INVALID(-1),

	/* 0 */
	genusInt_AC(0, "AC"),
	/* 1 */
	genusInt_B(1, "B"),
	/* 2 */
	genusInt_C(2, "C"),
	/* 3 */
	genusInt_D(3, "D"),
	/* 4 */
	genusInt_E(4, "E"),
	/* 5 */
	genusInt_F(5, "F"),
	/* 6 */
	genusInt_G(6, "G"),
	/* 7 */
	genusInt_H(7, "H"),
	/* 8 */
	genusInt_L(8, "L"),
	/* 9 */
	genusInt_M(9, "M"),
	/* 10 */
	genusInt_PL(10, "PL"),
	/* 11 */
	genusInt_Q(11, "O"),
	/* 12 */
	genusInt_R(12, "R"),
	/* 13 */
	genusInt_S(13, "S"),
	/* 14 */
	genusInt_U(14, "U"),
	/* 15 */
	genusInt_V(15, "V"),
	/* 16 */
	genusInt_W(16, "W"),
	/* 17 */
	genusInt_XH(17, "XH"),
	/* 18 */
	genusInt_ZC(18, "ZC"),
	/* 19 */
	genusInt_ZH(19, "ZH");

	private int intValue;
	private String textValue;
	private static java.util.HashMap<Integer, enumGenusInternalIndex> mappings;

	private static java.util.HashMap<Integer, enumGenusInternalIndex> getMappings() {
		if (mappings == null) {
			synchronized (enumGenusInternalIndex.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, enumGenusInternalIndex>();
				}
			}
		}
		return mappings;
	}

	private enumGenusInternalIndex(int intValue) {
		this.intValue = intValue;
		getMappings().put(intValue, this);
	}

	private enumGenusInternalIndex(int value, String textValue) {
		this(value);
		this.textValue = textValue;
	}

	@Override
	public int getValue() {
		return intValue;
	}

	@Override
	public int getIndex() {
		if (this.equals(genusInt_INVALID)) {
			throw new UnsupportedOperationException(MessageFormat
					.format("Cannot call getIndex on {} as it's not a standard member of the enumeration", this));
		}
		
		return intValue;
	}
	
	@Override
	public String getText() {
		if (this.equals(genusInt_INVALID)) {
			throw new UnsupportedOperationException(MessageFormat
					.format("Cannot call getText on {} as it's not a standard member of the enumeration", this));
		}
		
		return textValue;
	}
	
	public static enumGenusInternalIndex forValue(int value) {
		return getMappings().get(value);
	}

	public static int size() {
		return genusInt_ZH.intValue - genusInt_AC.intValue + 1;
	}

	public static class Iterator extends SI32EnumIterator<enumGenusInternalIndex> {
		public Iterator() {
			super(genusInt_AC, genusInt_ZH, mappings);
		}
	}
}
