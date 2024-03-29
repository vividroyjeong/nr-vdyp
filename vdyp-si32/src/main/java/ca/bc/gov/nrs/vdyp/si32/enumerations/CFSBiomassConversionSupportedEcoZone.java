package ca.bc.gov.nrs.vdyp.si32.enumerations;

import java.text.MessageFormat;

/**
 * Lists indices to Eco Zone conversion factors for the supported CFS Biomass 
 * conversion factors.
 * <ul>
 * <li> ecoZoneInt_UNKNOWN: Indicates an uninitialized value or error condition. This should
 *      never be used to indicate a valid Eco Zone Index.
 * <li> ecoZoneInt_...: Indices into the {@link CfsBiomassConversionCoefficients} array corresponding 
 * 		to the identified CFS Eco Zone.
 * </ul>
 * For the BC implementation of the CFS Conversion Factors, only a subset Eco Zones are supported. 
 * This enumeration lists each of those Eco Zones and their corresponding index into the
 * Cfs*BiomassConversionCoefficients arrays.
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
public enum CFSBiomassConversionSupportedEcoZone implements SI32Enum<CFSBiomassConversionSupportedEcoZone> {
	ecoZoneInt_UNKNOWN(-1), 

	/* 4 */
	ecoZoneInt_TaigaPlains(0),
	/* 9 */
	ecoZoneInt_BorealPlains(1),
	/* 12 */
	ecoZoneInt_BorealCordillera(2),
	/* 13 */
	ecoZoneInt_PacificMaritime(3),
	/* 14 */
	ecoZoneInt_MontaneCordillera(4);

	private int intValue;
	private static java.util.HashMap<Integer, CFSBiomassConversionSupportedEcoZone> mappings;

	private static java.util.HashMap<Integer, CFSBiomassConversionSupportedEcoZone> getMappings() {
		if (mappings == null) {
			synchronized (CFSBiomassConversionSupportedEcoZone.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, CFSBiomassConversionSupportedEcoZone>();
				}
			}
		}
		return mappings;
	}

	private CFSBiomassConversionSupportedEcoZone(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	@Override
	public int getValue() {
		return intValue;
	}

	@Override
	public int getIndex() {
		if (this.equals(ecoZoneInt_UNKNOWN)) {
			throw new UnsupportedOperationException(MessageFormat
					.format("Cannot call getIndex on {} as it's not a standard member of the enumeration", this));
		}
		
		return intValue;
	}
	
	@Override
	public String getText() {
		if (this.equals(ecoZoneInt_UNKNOWN)) {
			throw new UnsupportedOperationException(MessageFormat
					.format("Cannot call getText on {} as it's not a standard member of the enumeration", this));
		}
		
		return this.toString().substring("ecoZoneInt_".length());
	}

	public static int size() {
		return ecoZoneInt_MontaneCordillera.intValue - ecoZoneInt_TaigaPlains.intValue + 1;
	}

	public static class Iterator extends SI32EnumIterator<CFSBiomassConversionSupportedEcoZone> {
		public Iterator() {
			super(ecoZoneInt_TaigaPlains, ecoZoneInt_MontaneCordillera, mappings);
		}
	}
}
