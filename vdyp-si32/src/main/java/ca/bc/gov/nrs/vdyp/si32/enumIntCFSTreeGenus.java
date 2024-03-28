package ca.bc.gov.nrs.vdyp.si32;

import java.text.MessageFormat;

/*-----------------------------------------------------------------------------
 *
 * enumIntCFSTreeGenus
 * enumExtCFSTreeGenus
 * ===================
 *
 *    Enumerate the different Tree Genus as defined by the Canadian Forest
 *    Service for the purposes of Biomass.
 *
 *
 * Members
 * -------
 *
 *    cfsGenus_UNKNOWN
 *       Represents an uninitialized value or error state. This value should
 *       not be used as a valid Genus value.
 *
 *    cfsGenus_NotApplicable
 *       For records that are not stocked forest land.
 *
 *    cfsGenus_MissingValue
 *       No information is available.
 *    
 *    cfsGenus_...
 *       Individual Genus values.
 *
 * Remarks
 * -------
 *
 *    The values for 'cfsGenus_NotApplicable' and 'cfsGenus_MissingValue' are 
 *    defined in the CFS source table but should never be used in the context
 *    of VDYP as all CFS species have a Genus mapping. They are included for
 *    completeness of the source definitions. They should never be used as a
 *    valid CFS Genus value.
 *
 *    Elements for this table are automatically generated and copy and pasted
 *    from the:
 *       -  'Conversion Param Enum Defn' column of the 
 *       -  'LiveConversionFactorsTable' found on the 
 *       -  'Derived C Species Table' tab in the
 *       -  'BC_Inventory_updates_by_CBMv2bs.xlsx' located in the
 *       -  'Documents/CFS-Biomass' folder.
 */

public enum enumIntCFSTreeGenus implements SI32Enum<enumIntCFSTreeGenus> {
	cfsGenus_UNKNOWN(-1), //

	cfsGenus_NotApplicable(-9), //
	cfsGenus_MissingValue(-8), //
	cfsGenus_Spruce(1), //
	cfsGenus_Pine(2), //
	cfsGenus_Fir(3), //
	cfsGenus_Hemlock(4), //
	cfsGenus_DouglasFir(5), //
	cfsGenus_Larch(6), //
	cfsGenus_CedarAndOtherConifers(7), //
	cfsGenus_UnspecifiedConifers(8), //
	cfsGenus_Poplar(9), //
	cfsGenus_Birch(10), //
	cfsGenus_Maple(11), //
	cfsGenus_OtherBroadleaves(12), //
	cfsGenus_UnspecifiedBroadleaves(13); //

	private int intValue;
	private static java.util.HashMap<Integer, enumIntCFSTreeGenus> mappings;

	private static java.util.HashMap<Integer, enumIntCFSTreeGenus> getMappings() {
		if (mappings == null) {
			synchronized (enumIntCFSTreeGenus.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, enumIntCFSTreeGenus>();
				}
			}
		}
		return mappings;
	}

	private enumIntCFSTreeGenus(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	@Override
	public int getValue() {
		return intValue;
	}

	@Override
	public int getIndex() {
		if (this.equals(cfsGenus_UNKNOWN) || this.equals(cfsGenus_NotApplicable) || this.equals(cfsGenus_MissingValue)) {
			throw new UnsupportedOperationException(MessageFormat.format("Cannot call getIndex on {} as it's not a standard member of the enumeration", this));
		}
		
		return intValue - 1;
	}
	
	@Override
	public String getText() {
		if (this.equals(cfsGenus_UNKNOWN)) {
			throw new UnsupportedOperationException(MessageFormat
					.format("Cannot call getText on {} as it's not a standard member of the enumeration", this));
		}
		
		return this.toString().substring("cfsGenus_".length());
	}

	public static enumIntCFSTreeGenus forValue(int value) {
		return getMappings().get(value);
	}

	public static int size() {
		return cfsGenus_UnspecifiedBroadleaves.intValue - cfsGenus_Spruce.intValue + 1;
	}

	public static class Iterator extends SI32EnumIterator<enumIntCFSTreeGenus> {
		public Iterator() {
			super(cfsGenus_Spruce, cfsGenus_UnspecifiedBroadleaves, mappings);
		}
	}
}
