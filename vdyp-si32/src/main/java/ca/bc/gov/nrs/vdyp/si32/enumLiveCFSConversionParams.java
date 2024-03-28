package ca.bc.gov.nrs.vdyp.si32;

import java.text.MessageFormat;

/**
 * Live CFS Biomass definitions.
 * <ul>
 * <li> cfsLiveParm_UNKNOWN<p>
 * 	     Indicates an error condition or an uninitialized value. This value
 *       should never be used as an index for a specific conversion parameter.
 * <li> cfsLiveParm_...<p>
 *       Indices into the 'cfsSpcsBiomassConversionCoefficients' array for each
 *       of the CFS Biomass conversion parameters/coefficients.
 * </ul>
 * The CFS Biomass Conversion process is based on a number of hard coded constants/coefficients 
 * which are used to adjust the conversion by CFS Eco Zone and Species.
 * <ol>
 * <li> 'Conversion Param Enum Defn' column of the 
 * <li> 'DeadConversionFactorsTable' found on the 
 * <li> 'Derived C Species Table' tab in the
 * <li> 'BC_Inventory_updates_by_CBMv2bs.xlsx' located in the
 * <li> 'Documents/CFS-Biomass' folder.
 * </ol>
 */
public enum enumLiveCFSConversionParams implements SI32Enum<enumLiveCFSConversionParams> {
	cfsLiveParm_UNKNOWN(-1, "UNKNOWN"),

	/* Rows copied from spreadsheet appear below. */
	cfsLiveParm_A(0, "Merch"), //
	cfsLiveParm_B(1, "Merch"), //
	cfsLiveParm_a_nonmerch(2, "Non-Merch"), //
	cfsLiveParm_b_nonmerch(3, "Non-Merch"), //
	cfsLiveParm_k_nonmerch(4, "Non-Merch"), //
	cfsLiveParm_cap_nonmerch(5, "Non-Merch"), //
	cfsLiveParm_a_sap(6, "Sapling"), //
	cfsLiveParm_b_sap(7, "Sapling"), //
	cfsLiveParm_k_sap(8, "Sapling"), //
	cfsLiveParm_cap_sap(9, "Sapling"), //
	cfsLiveParm_a1(10, "Prop Parm"), //
	cfsLiveParm_a2(11, "Prop Parm"), //
	cfsLiveParm_a3(12, "Prop Parm"), //
	cfsLiveParm_b1(13, "Prop Parm"), //
	cfsLiveParm_b2(14, "Prop Parm"), //
	cfsLiveParm_b3(15, "Prop Parm"), //
	cfsLiveParm_c1(16, "Prop Parm"), //
	cfsLiveParm_c2(17, "Prop Parm"), //
	cfsLiveParm_c3(18, "Prop Parm"), //
	cfsLiveParm_min_volume(19, "Volume"), //
	cfsLiveParm_max_volume(20, "Volume"), //
	cfsLiveParm_low_stemwood_prop(21, "Stemwood"), //
	cfsLiveParm_high_stemwood_prop(22, "Stemwood"), //
	cfsLiveParm_low_stembark_prop(23, "Stembark"), //
	cfsLiveParm_high_stembark_prop(24, "Stembark"), //
	cfsLiveParm_low_branches_prop(25, "Branches"), //
	cfsLiveParm_high_branches_prop(26, "Branches"), //
	cfsLiveParm_low_foliage_prop(27, "Foliage"), //
	cfsLiveParm_high_foliage_prop(28, "Foliage");

	private final int intValue;
	private final String category;
	private static java.util.HashMap<Integer, enumLiveCFSConversionParams> mappings;

	private static java.util.HashMap<Integer, enumLiveCFSConversionParams> getMappings() {
		if (mappings == null) {
			synchronized (enumLiveCFSConversionParams.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, enumLiveCFSConversionParams>();
				}
			}
		}
		return mappings;
	}

	private enumLiveCFSConversionParams(int value, String category) {
		this.intValue = value;
		this.category = category;
		getMappings().put(value, this);
	}

	@Override
	public int getValue() {
		return intValue;
	}

	public String getCategory() {
		return category;
	}

	@Override
	public int getIndex() {
		if (this.equals(cfsLiveParm_UNKNOWN)) {
			throw new UnsupportedOperationException(MessageFormat.format("Cannot call getIndex on {} as it's not a standard member of the enumeration", this));
		}
		
		return intValue;
	}
	
	@Override
	public String getText() {
		if (this.equals(cfsLiveParm_UNKNOWN)) {
			throw new UnsupportedOperationException(MessageFormat
					.format("Cannot call getText on {} as it's not a standard member of the enumeration", this));
		}
		
		return this.toString().substring("cfsLiveParm_".length());
	}

	public static enumLiveCFSConversionParams forValue(int value) {
		return getMappings().get(value);
	}

	public static int size() {
		return cfsLiveParm_high_foliage_prop.intValue - cfsLiveParm_A.intValue + 1;
	}

	public static class Iterator extends SI32EnumIterator<enumLiveCFSConversionParams> {
		public Iterator() {
			super(cfsLiveParm_A, cfsLiveParm_high_foliage_prop, mappings);
		}
	}
}
