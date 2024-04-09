package ca.bc.gov.nrs.vdyp.si32.cfs;

import java.text.MessageFormat;

import ca.bc.gov.nrs.vdyp.si32.enumerations.SI32Enum;
import ca.bc.gov.nrs.vdyp.si32.enumerations.SI32EnumIterator;

/**
 * Enumerate the different Tree Genus as defined by the Canadian Forest Service for 
 * the purposes of Biomass.
 * <ul>
 * <li><b>cfsGenus_UNKNOWN</b> represents an uninitialized value or error state. This value should
 *        not be used as a valid Genus value.</b>
 * <li><b>cfsGenus_NotApplicable</b> for records that are not stocked forest land.
 * <li><b>cfsGenus_MissingValue</b> no information is available.
 * <li><b>cfsGenus_...</b>individual Genus values.
 * </ul>
 * <p><b>Remarks</b>
 * <p>
 *    The values for 'cfsGenus_NotApplicable' and 'cfsGenus_MissingValue' are 
 *    defined in the CFS source table but should never be used in the context
 *    of VDYP as all CFS species have a Genus mapping. They are included for
 *    completeness of the source definitions. They should never be used as a
 *    valid CFS Genus value.
 * <p>
 *    Elements for this table are automatically generated and copy and pasted
 *    from the:
 * <ol>
 * <li>'Internal C Enum Definition' column of the 
 * <li>'SpeciesTable' table found on the 
 * <li>'Lookups' tab in the
 * <li>'BC_Inventory_updates_by_CBMv2bs.xlsx' located in the
 * <li>'Documents/CFS-Biomass' folder.
 * </ol>
 */
public enum CfsTreeGenus implements SI32Enum<CfsTreeGenus> {
	cfsGenus_UNKNOWN(-1, "UNKNOWN"), //
	
	cfsGenus_NotApplicable(-9, "Not applicable"), //
	cfsGenus_MissingValue(-8, "Missing Value"), //
	
	cfsGenus_Spruce(1, "Spruce"), //
	cfsGenus_Pine(2, "Pine"), //
	cfsGenus_Fir(3, "Fir"), //
	cfsGenus_Hemlock(4, "Hemlock"), //
	cfsGenus_DouglasFir(5, "Douglas-fir"), //
	cfsGenus_Larch(6, "Larch"), //
	cfsGenus_CedarAndOtherConifers(7, "Cedar and other conifers"), //
	cfsGenus_UnspecifiedConifers(8, "Unspecified conifers"), //
	cfsGenus_Poplar(9, "Poplar"), //
	cfsGenus_Birch(10, "Birch"), //
	cfsGenus_Maple(11, "Maple"), //
	cfsGenus_OtherBroadleaves(12, "Other broadleaved species"), //
	cfsGenus_UnspecifiedBroadleaves(13, "Unspecified broadleaved species"); //

	private final int index;
	private final String genusName;
	
	private CfsTreeGenus(int index, String genusName) {
		this.index = index;
		this.genusName = genusName;
	}

	@Override
	public int getIndex() {
		return index;
	}
	
	public String getGenusName() {
		return genusName;
	}

	@Override
	public int getOffset() {
		if (this.equals(cfsGenus_UNKNOWN) || this.equals(cfsGenus_NotApplicable) || this.equals(cfsGenus_MissingValue)) {
			throw new UnsupportedOperationException(MessageFormat.format("Cannot call getIndex on {0} as it's not a standard member of the enumeration", this));
		}
		
		return index - 1;
	}
	
	@Override
	public String getText() {
		if (this.equals(cfsGenus_UNKNOWN)) {
			throw new UnsupportedOperationException(MessageFormat
					.format("Cannot call getText on {0} as it's not a standard member of the enumeration", this));
		}
		
		return this.toString().substring("cfsGenus_".length());
	}

	/**
	 * Returns the enumeration constant with the given index.
	 * @param index the value in question
	 * @return the enumeration value, unless no enumeration constant has the given 
	 * 	   <code>index</code> in which case <code>null</code> is returned.
	 */
	public static CfsTreeGenus forIndex(int index) {
		for (CfsTreeGenus e: CfsTreeGenus.values()) {
			if (index == e.index)
				return e;
		}
		
		return null;
	}

	/**
	 * @return the number of non-housekeeping entries in the enumeration
	 */
	public static int size() {
		return cfsGenus_UnspecifiedBroadleaves.index - cfsGenus_Spruce.index + 1;
	}

	public static class Iterator extends SI32EnumIterator<CfsTreeGenus> {
		public Iterator() {
			super(cfsGenus_Spruce, cfsGenus_UnspecifiedBroadleaves, values());
		}
	}
}
