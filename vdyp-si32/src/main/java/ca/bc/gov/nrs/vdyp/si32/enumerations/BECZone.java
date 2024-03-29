package ca.bc.gov.nrs.vdyp.si32.enumerations;

import java.text.MessageFormat;

/**
 * Identifies each of the BEC Zones known to the system.
 * <ul>
 * <li> bec_UNKNOWN: Represents an unknown BEC Zone or an error condition.
 * <li> bec_...: Individual BEC Zones recognized.
 */
public enum BECZone implements SI32Enum<BECZone> {
	bec_UNKNOWN(-1),
	
	bec_AT(1), 
	bec_BG(2), 
	bec_BWBS(3), 
	bec_CDF(4),
	bec_CWH(5), 
	bec_ESSF(6), 
	bec_ICH(7),
	bec_IDF(8),
	bec_MH(9), 
	bec_MS(10), 
	bec_PP(11), 
	bec_SBPS(12),
	bec_SBS(13), 
	bec_SWB(14);

	private int intValue;
	private static java.util.HashMap<Integer, BECZone> mappings;

	private static java.util.HashMap<Integer, BECZone> getMappings() {
		if (mappings == null) {
			synchronized (BECZone.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, BECZone>();
				}
			}
		}
		return mappings;
	}

	private BECZone(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	@Override
	public int getValue() {
		return intValue;
	}

	@Override
	public int getIndex() {
		if (this.equals(bec_UNKNOWN)) {
			throw new UnsupportedOperationException(MessageFormat
					.format("Cannot call getIndex on {} as it's not a standard member of the enumeration", this));
		}
		
		return ordinal() - 1;
	}
	
	@Override
	public String getText() {
		if (this.equals(bec_UNKNOWN)) {
			throw new UnsupportedOperationException(MessageFormat
					.format("Cannot call getText on {} as it's not a standard member of the enumeration", this));
		}
		
		return this.toString().substring("bec_".length());
	}

	public static BECZone forValue(int value) {
		return getMappings().get(value);
	}

	public static int size() {
		return bec_SWB.intValue - bec_AT.intValue + 1;
	}

	public static class Iterator extends SI32EnumIterator<BECZone> {
		public Iterator() {
			super(bec_AT, bec_SWB, mappings);
		}
	}
}
