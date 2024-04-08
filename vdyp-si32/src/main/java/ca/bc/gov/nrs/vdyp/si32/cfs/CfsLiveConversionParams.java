package ca.bc.gov.nrs.vdyp.si32.cfs;

import java.text.MessageFormat;

import ca.bc.gov.nrs.vdyp.si32.enumerations.SI32Enum;
import ca.bc.gov.nrs.vdyp.si32.enumerations.SI32EnumIterator;

/**
 * Live CFS Biomass definitions.
 * <ul>
 * <li><b>cfsLiveParm_UNKNOWN</b>
 * 	     Indicates an error condition or an uninitialized value. This value
 *       should never be used as an index for a specific conversion parameter.
 * <li><b>cfsLiveParm_...</b>
 *       Indices into the {@link CfsBiomassConversionCoefficientsForSpecies} array for each
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
public enum CfsLiveConversionParams implements SI32Enum<CfsLiveConversionParams> {
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

	private final int index;
	private final String category;

	private CfsLiveConversionParams(int index, String category) {
		this.index = index;
		this.category = category;
	}

	@Override
	public int getIndex() {
		return index;
	}

	public String getCategory() {
		return category;
	}

	@Override
	public int getOffset() {
		if (this.equals(cfsLiveParm_UNKNOWN)) {
			throw new UnsupportedOperationException(MessageFormat.format("Cannot call getIndex on {} as it's not a standard member of the enumeration", this));
		}
		
		return index;
	}
	
	@Override
	public String getText() {
		if (this.equals(cfsLiveParm_UNKNOWN)) {
			throw new UnsupportedOperationException(MessageFormat
					.format("Cannot call getText on {} as it's not a standard member of the enumeration", this));
		}
		
		return this.toString().substring("cfsLiveParm_".length());
	}

	/**
	 * Returns the enumeration constant with the given index.
	 * @param index the value in question
	 * @return the enumeration value, unless no enumeration constant has the given 
	 * 	   <code>index</code> in which case <code>null</code> is returned.
	 */
	public static CfsLiveConversionParams forIndex(int index) {
		for (CfsLiveConversionParams e: CfsLiveConversionParams.values()) {
			if (index == e.index)
				return e;
		}
		
		return null;
	}

	/**
	 * @return the number of non-housekeeping entries in the enumeration
	 */
	public static int size() {
		return cfsLiveParm_high_foliage_prop.index - cfsLiveParm_A.index + 1;
	}

	public static class Iterator extends SI32EnumIterator<CfsLiveConversionParams> {
		public Iterator() {
			super(cfsLiveParm_A, cfsLiveParm_high_foliage_prop, values());
		}
	}
}
