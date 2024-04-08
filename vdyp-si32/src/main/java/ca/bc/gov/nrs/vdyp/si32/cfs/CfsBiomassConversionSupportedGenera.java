package ca.bc.gov.nrs.vdyp.si32.cfs;

import java.text.MessageFormat;

import ca.bc.gov.nrs.vdyp.si32.enumerations.SI32Enum;
import ca.bc.gov.nrs.vdyp.si32.enumerations.SI32EnumIterator;

/**
 * Lists the indices for CFS Genus for looking up CFS Conversion Params at the Genus level.
 * <p>
 * These values are used as an index into the {@link CfsBiomassConversionCoefficientsForGenus} array.
 */
public enum CfsBiomassConversionSupportedGenera implements SI32Enum<CfsBiomassConversionSupportedGenera> {
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

	private final int index;

	private CfsBiomassConversionSupportedGenera(int index) {
		this.index = index;
	}

	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public int getOffset() {
		if (this.equals(genusInt_INVALID)) {
			throw new UnsupportedOperationException(MessageFormat
					.format("Cannot call getIndex on {} as it's not a standard member of the enumeration", this));
		}
		
		return index;
	}
	
	@Override
	public String getText() {
		if (this.equals(genusInt_INVALID)) {
			throw new UnsupportedOperationException(MessageFormat
					.format("Cannot call getText on {} as it's not a standard member of the enumeration", this));
		}
		
		return this.toString().substring("genusInt_".length());
	}
	
	/**
	 * Returns the enumeration constant with the given index.
	 * @param index the value in question
	 * @return the enumeration value, unless no enumeration constant has the given 
	 * 	   <code>index</code> in which case <code>null</code> is returned.
	 */
	public static CfsBiomassConversionSupportedGenera forIndex(int index) {
		for (CfsBiomassConversionSupportedGenera e: CfsBiomassConversionSupportedGenera.values()) {
			if (index == e.index)
				return e;
		}
		
		return null;
	}

	/**
	 * @return the number of non-housekeeping entries in the enumeration
	 */
	public static int size() {
		return genusInt_ZH.index - genusInt_AC.index + 1;
	}

	public static class Iterator extends SI32EnumIterator<CfsBiomassConversionSupportedGenera> {
		public Iterator() {
			super(genusInt_AC, genusInt_ZH, values());
		}
	}
}
