package ca.bc.gov.nrs.vdyp.si32;

import java.text.MessageFormat;

/*-----------------------------------------------------------------------------
 *
 * enumSpcsInternalIndex
 * =====================
 *
 *    Lists the indices for species supported by the CFS Biomass conversion
 *    process.
 *
 *
 * Members
 * -------
 *
 *    spcsInt_UNKNOWN
 *       Indicates an uninitialized or error condition. This value should never
 *       be used to indicate an actual species index value.
 *
 *    spcsInt_...
 *       Indices into the appropriate dimension for the 
 *       'cfsBiomassConversionCoefficients' array for the indicated
 *       CFS Species.
 *
 *
 * Remarks
 * -------
 *
 *    For BC, there are a limited number of CFS defined species for which
 *    there is a set of conversion factors for within specific Eco Zones.
 *
 *    This enumeration lists each of the species across all ECO Zones for
 *    which species specific conversion factors appear at least once.
 *
 *    The list of enumeration constants is automatically generated and copy
 *    and pasted into this enum definition from the:
 *       -  'Internal C Enum Definition' column of the 
 *       -  'SpeciesTable' table found on the 
 *       -  'Lookups' tab in the
 *       -  'BC_Inventory_updates_by_CBMv2bs.xlsx' located in the
 *       -  'Documents/CFS-Biomass' folder.
 *
 */

public enum enumSpcsInternalIndex implements SI32Enum<enumSpcsInternalIndex> {
	spcsInt_UNKNOWN(-1),

	spcsInt_AC(0), // 
	spcsInt_ACB(1), // 
	spcsInt_AT(2), // 
	spcsInt_B(3), // 
	spcsInt_BA(4), // 
	spcsInt_BG(5), // 
	spcsInt_BL(6), // 
	spcsInt_CW(7), // 
	spcsInt_DR(8), // 
	spcsInt_EA(9), // 
	spcsInt_EP(10), // 
	spcsInt_EXP(11), // 
	spcsInt_FD(12), // 
	spcsInt_FDC(13), // 
	spcsInt_FDI(14), // 
	spcsInt_H(15), // 
	spcsInt_HM(16), // 
	spcsInt_HW(17), // 
	spcsInt_L(18), // 
	spcsInt_LA(19), // 
	spcsInt_LT(20), // 
	spcsInt_LW(21), // 
	spcsInt_MB(22), // 
	spcsInt_PA(23), // 
	spcsInt_PL(24), // 
	spcsInt_PLC(25), // 
	spcsInt_PLI(26), // 
	spcsInt_PW(27), //
	spcsInt_PY(28), // 
	spcsInt_S(29), // 
	spcsInt_SB(30), // 
	spcsInt_SE(31), // 
	spcsInt_SS(32), // 
	spcsInt_SW(33), // 
	spcsInt_SX(34), // 
	spcsInt_W(35), // 
	spcsInt_XC(36), // 
	spcsInt_YC(37);

	private int intValue;
	private static java.util.HashMap<Integer, enumSpcsInternalIndex> mappings;

	private static java.util.HashMap<Integer, enumSpcsInternalIndex> getMappings() {
		if (mappings == null) {
			synchronized (enumSpcsInternalIndex.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, enumSpcsInternalIndex>();
				}
			}
		}
		return mappings;
	}

	private enumSpcsInternalIndex(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	@Override
	public int getValue() {
		return intValue;
	}

	public static enumSpcsInternalIndex forValue(int value) {
		return getMappings().get(value);
	}

	@Override
	public int getIndex() {
		if (this.equals(spcsInt_UNKNOWN)) {
			throw new UnsupportedOperationException(MessageFormat.format("Cannot call getIndex on {} as it's not a standard member of the enumeration", this));
		}
		
		return intValue;
	}
	
	@Override
	public String getText() {
		if (this.equals(spcsInt_UNKNOWN)) {
			throw new UnsupportedOperationException(MessageFormat
					.format("Cannot call getText on {} as it's not a standard member of the enumeration", this));
		}
		
		return this.toString().substring("spcsInt_".length());
	}

	public static int size() {
		return spcsInt_YC.intValue - spcsInt_AC.intValue + 1;
	}

	public static class Iterator extends SI32EnumIterator<enumSpcsInternalIndex> {
		public Iterator() {
			super(spcsInt_AC, spcsInt_YC, mappings);
		}
	}
}
