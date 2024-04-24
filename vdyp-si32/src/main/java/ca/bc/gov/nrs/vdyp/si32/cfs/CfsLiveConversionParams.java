package ca.bc.gov.nrs.vdyp.si32.cfs;

import java.text.MessageFormat;

import ca.bc.gov.nrs.vdyp.si32.enumerations.SI32Enum;
import ca.bc.gov.nrs.vdyp.si32.enumerations.SI32EnumIterator;

/**
 * Live CFS Biomass definitions.
 * <ul>
 * <li><b>UNKNOWN</b>
 * 	     Indicates an error condition or an uninitialized value. This value
 *       should never be used as an index for a specific conversion parameter.
 * <li><b>others</b>
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
	UNKNOWN(-1, "UNKNOWN"),

	/* Rows copied from spreadsheet appear below. */
	A(0, "Merch"), //
	B(1, "Merch"), //
	A_NONMERCH(2, "Non-Merch"), //
	B_NONMERCH(3, "Non-Merch"), //
	K_NONMERCH(4, "Non-Merch"), //
	CAP_NONMERCH(5, "Non-Merch"), //
	A_SAP(6, "Sapling"), //
	B_SAP(7, "Sapling"), //
	K_SAP(8, "Sapling"), //
	CAP_SAP(9, "Sapling"), //
	A1(10, "Prop Parm"), //
	A2(11, "Prop Parm"), //
	A3(12, "Prop Parm"), //
	B1(13, "Prop Parm"), //
	B2(14, "Prop Parm"), //
	B3(15, "Prop Parm"), //
	C1(16, "Prop Parm"), //
	C2(17, "Prop Parm"), //
	C3(18, "Prop Parm"), //
	MIN_VOLUME(19, "Volume"), //
	MAX_VOLUME(20, "Volume"), //
	LOW_STEMWOOD_PROP(21, "Stemwood"), //
	HIGH_STEMWOOD_PROP(22, "Stemwood"), //
	LOW_STEMBARK_PROP(23, "Stembark"), //
	HIGH_STEMBARK_PROP(24, "Stembark"), //
	LOW_BRANCHES_PROP(25, "Branches"), //
	HIGH_BRANCHES_PROP(26, "Branches"), //
	LOW_FOLIAGE_PROP(27, "Foliage"), //
	HIGH_FOLIAGE_PROP(28, "Foliage");

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
		if (this.equals(UNKNOWN)) {
			throw new UnsupportedOperationException(MessageFormat.format("Cannot call getIndex on {0} as it's not a standard member of the enumeration", this));
		}
		
		return index;
	}
	
	@Override
	public String getText() {
		if (this.equals(UNKNOWN)) {
			return "";
		}
		
		return this.toString();
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
		return HIGH_FOLIAGE_PROP.index - A.index + 1;
	}

	public static class Iterator extends SI32EnumIterator<CfsLiveConversionParams> {
		public Iterator() {
			super(A, HIGH_FOLIAGE_PROP, values());
		}
	}
}
