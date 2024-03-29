package ca.bc.gov.nrs.vdyp.si32.enumerations;

import java.text.MessageFormat;

/**
 * Lists the indices for species supported by the CFS Biomass conversion process.
 * <ul>
 * <li><b>spcsInt_UNKNOWN</b> indicates an uninitialized or error condition. This 
 * value should never be used to indicate an actual species index value.
 * <li><b>spcsInt_...</b> indices into the appropriate dimension for the 
 * {@link CfsBiomassConversionCoefficients} array for the indicated CFS Species.
 * </ul>
 * <b>Remarks</b>
 * <p>
 *    For BC, there are a limited number of CFS defined species for which
 *    there is a set of conversion factors for within specific Eco Zones.
 * <p>
 *    This enumeration lists each of the species across all Eco Zones for
 *    which species specific conversion factors appear at least once.
 * <p>
 *    The list of enumeration constants is automatically generated and copy
 *    and pasted into this enum definition from the:
 * <ol>
 * <li>'Internal C Enum Definition' column of the 
 * <li>'SpeciesTable' table found on the 
 * <li>'Lookups' tab in the
 * <li>'BC_Inventory_updates_by_CBMv2bs.xlsx' located in the
 * <li>'Documents/CFS-Biomass' folder.
 * </ol>
 */
public enum CFSBiomassConversionSupportedSpecies implements SI32Enum<CFSBiomassConversionSupportedSpecies> {
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
	private static java.util.HashMap<Integer, CFSBiomassConversionSupportedSpecies> mappings;

	private static java.util.HashMap<Integer, CFSBiomassConversionSupportedSpecies> getMappings() {
		if (mappings == null) {
			synchronized (CFSBiomassConversionSupportedSpecies.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, CFSBiomassConversionSupportedSpecies>();
				}
			}
		}
		return mappings;
	}

	private CFSBiomassConversionSupportedSpecies(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	@Override
	public int getValue() {
		return intValue;
	}

	public static CFSBiomassConversionSupportedSpecies forValue(int value) {
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

	public static class Iterator extends SI32EnumIterator<CFSBiomassConversionSupportedSpecies> {
		public Iterator() {
			super(spcsInt_AC, spcsInt_YC, mappings);
		}
	}
}
