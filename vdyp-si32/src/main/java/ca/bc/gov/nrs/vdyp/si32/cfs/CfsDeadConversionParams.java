package ca.bc.gov.nrs.vdyp.si32.cfs;

import java.text.MessageFormat;

import ca.bc.gov.nrs.vdyp.si32.enumerations.SI32Enum;
import ca.bc.gov.nrs.vdyp.si32.enumerations.SI32EnumIterator;

/**
 * Lists the different conversion parameters that support the calculation of Dead CFS Biomass.
 * <ul>
 * <li> cfsDeadParm_UNKNOWN: Indicates an error condition or an uninitialized value. This value
 *       should never be used as an index for a specific conversion parameter.
 * <li> cfsDeadParm_...: Indices into the {@link CfsBiomassConversionCoefficientsDead} array for each
 *       of the CFS Biomass conversion parameters/coefficients.
 * </ul>
 * The CFS Biomass Conversion process is based on a number of hard coded constants/coefficients which 
 * are used to adjust the conversion by CFS Eco Zone and Species.
 * <p>
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
public enum CfsDeadConversionParams implements SI32Enum<CfsDeadConversionParams> {
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

	private final int index;
	
	private CfsDeadConversionParams(int index) {
		this.index = index;
	}

	@Override
	public int getIndex() {
		return index;
	}
	
	@Override
	public int getOffset() {
		if (this.equals(cfsDeadParm_UNKNOWN)) {
			throw new UnsupportedOperationException(MessageFormat
					.format("Cannot call getIndex on {} as it's not a standard member of the enumeration", this));
		}
		
		return index;
	}
	
	@Override
	public String getText() {
		if (this.equals(cfsDeadParm_UNKNOWN)) {
			throw new UnsupportedOperationException(MessageFormat
					.format("Cannot call getText on {} as it's not a standard member of the enumeration", this));
		}
		
		return this.toString().substring("cfsDeadParm_".length());
	}
	
	/**
	 * @return the number of non-housekeeping entries in the enumeration
	 */
	public static int size() {
		return cfsDeadParm_V4.index - cfsDeadParm_Prop1.index + 1;
	}

	public static class Iterator extends SI32EnumIterator<CfsDeadConversionParams> {
		public Iterator() {
			super(cfsDeadParm_Prop1, cfsDeadParm_V4, values());
		}
	}
}
