package ca.bc.gov.nrs.vdyp.si32.enumerations;

import java.text.MessageFormat;

import ca.bc.gov.nrs.vdyp.si32.CfsGenusBiomassConversionCoefficients;

/**
 * Lists the indices for CFS Genus for looking up CFS Conversion Params at the Genus level.
 * <p>
 * These values are used as an index into the {@link CfsGenusBiomassConversionCoefficients} array.
 */
public enum CFSBiomassConversionSupportedGenera implements SI32Enum<CFSBiomassConversionSupportedGenera> {
	genusInt_INVALID(-1),

	/* 0 */
	genusInt_AC(0),
	/* 1 */
	genusInt_B(1),
	/* 2 */
	genusInt_C(2),
	/* 3 */
	genusInt_D(3),
	/* 4 */
	genusInt_E(4),
	/* 5 */
	genusInt_F(5),
	/* 6 */
	genusInt_G(6),
	/* 7 */
	genusInt_H(7),
	/* 8 */
	genusInt_L(8),
	/* 9 */
	genusInt_M(9),
	/* 10 */
	genusInt_PL(10),
	/* 11 */
	genusInt_Q(11),
	/* 12 */
	genusInt_R(12),
	/* 13 */
	genusInt_S(13),
	/* 14 */
	genusInt_U(14),
	/* 15 */
	genusInt_V(15),
	/* 16 */
	genusInt_W(16),
	/* 17 */
	genusInt_XH(17),
	/* 18 */
	genusInt_ZC(18),
	/* 19 */
	genusInt_ZH(19);

	private final int intValue;
	private static java.util.HashMap<Integer, CFSBiomassConversionSupportedGenera> mappings;

	private static java.util.HashMap<Integer, CFSBiomassConversionSupportedGenera> getMappings() {
		if (mappings == null) {
			synchronized (CFSBiomassConversionSupportedGenera.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, CFSBiomassConversionSupportedGenera>();
				}
			}
		}
		return mappings;
	}

	private CFSBiomassConversionSupportedGenera(int intValue) {
		this.intValue = intValue;
		getMappings().put(intValue, this);
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
		
		return this.toString().substring("genusInt_".length());
	}
	
	public static CFSBiomassConversionSupportedGenera forValue(int value) {
		return getMappings().get(value);
	}

	public static int size() {
		return genusInt_ZH.intValue - genusInt_AC.intValue + 1;
	}

	public static class Iterator extends SI32EnumIterator<CFSBiomassConversionSupportedGenera> {
		public Iterator() {
			super(genusInt_AC, genusInt_ZH, mappings);
		}
	}
}
