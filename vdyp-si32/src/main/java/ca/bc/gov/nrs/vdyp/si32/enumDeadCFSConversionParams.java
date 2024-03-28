package ca.bc.gov.nrs.vdyp.si32;

import java.text.MessageFormat;

/**
 * Lists the different conversion parameters that support the calculation of Dead CFS Biomass.
 * <ul>
 * <li> cfsDeadParm_UNKNOWN: Indicates an error condition or an uninitialized value. This value
 *       should never be used as an index for a specific conversion parameter.
 * <li> cfsDeadParm_...: Indices into the 'cfsDeadBiomassConversionCoefficients' array for each
 *       of the CFS Biomass conversion parameters/coefficients.
 * </ul>
 * <p>
 * The CFS Biomass Conversion process is based on a number of hard coded
 * constants/coefficients which are used to adjust the conversion by
 * CFS Eco Zone and Species.
 *
 * The list of enumeration constants is automatically generated and copy and pasted into this 
 * enum definition from the:
 * <ol>
 * <li> 'Conversion Param Enum Defn' column of the 
 * <li> 'DeadConversionFactorsTable' found on the 
 * <li> 'Derived C Species Table' tab in the
 * <li> 'BC_Inventory_updates_by_CBMv2bs.xlsx' located in the
 * <li> 'Documents/CFS-Biomass' folder.
 * </ol>
 */
public enum enumDeadCFSConversionParams implements SI32Enum<enumDeadCFSConversionParams> {
	cfsDeadParm_UNKNOWN(-1), 

	cfsDeadParm_Prop1(0),
	cfsDeadParm_Prop2(1),
	cfsDeadParm_Prop3(2),
	cfsDeadParm_Prop4(3),
	cfsDeadParm_Prop5(4),
	cfsDeadParm_V1(5),
	cfsDeadParm_V2(6),
	cfsDeadParm_V3(7),
	cfsDeadParm_V4(8);

	private static java.util.HashMap<Integer, enumDeadCFSConversionParams> mappings;

	private static java.util.HashMap<Integer, enumDeadCFSConversionParams> getMappings() {
		if (mappings == null) {
			synchronized (enumDeadCFSConversionParams.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, enumDeadCFSConversionParams>();
				}
			}
		}
		return mappings;
	}

	private int intValue;
	
	private enumDeadCFSConversionParams(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	@Override
	public int getValue() {
		return intValue;
	}
	
	@Override
	public int getIndex() {
		if (this.equals(cfsDeadParm_UNKNOWN)) {
			throw new UnsupportedOperationException(MessageFormat
					.format("Cannot call getIndex on {} as it's not a standard member of the enumeration", this));
		}
		
		return intValue;
	}
	
	@Override
	public String getText() {
		if (this.equals(cfsDeadParm_UNKNOWN)) {
			throw new UnsupportedOperationException(MessageFormat
					.format("Cannot call getText on {} as it's not a standard member of the enumeration", this));
		}
		
		return this.toString().substring("cfsDeadParm_".length());
	}
	
	public static int size() {
		return cfsDeadParm_Prop1.intValue - cfsDeadParm_V4.intValue + 1;
	}

	public static class Iterator extends SI32EnumIterator<enumDeadCFSConversionParams> {
		public Iterator() {
			super(cfsDeadParm_Prop1, cfsDeadParm_V4, mappings);
		}
	}
}
