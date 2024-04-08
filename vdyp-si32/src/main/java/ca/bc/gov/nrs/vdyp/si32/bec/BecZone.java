package ca.bc.gov.nrs.vdyp.si32.bec;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import ca.bc.gov.nrs.vdyp.si32.enumerations.SI32Enum;
import ca.bc.gov.nrs.vdyp.si32.enumerations.SI32EnumIterator;

/**
 * Identifies each of the BEC Zones known to the system.
 * <ul>
 * <li> bec_UNKNOWN: Represents an unknown BEC Zone or an error condition.
 * <li> bec_...: Individual BEC Zones recognized.
 */
public enum BecZone implements SI32Enum<BecZone> {
	bec_UNKNOWN(-1),
	
	bec_AT(0), 
	bec_BG(1), 
	bec_BWBS(2), 
	bec_CDF(3),
	bec_CWH(4), 
	bec_ESSF(5), 
	bec_ICH(6),
	bec_IDF(7),
	bec_MH(8), 
	bec_MS(9), 
	bec_PP(10), 
	bec_SBPS(11),
	bec_SBS(12), 
	bec_SWB(13);

	private static Map<Integer, BecZone> index2EnumMap = null;

	private static Map<Integer, BecZone> getIndex2EnumMap() {
		if (index2EnumMap == null) {
			synchronized (BecZone.class) {
				if (index2EnumMap == null) {
					index2EnumMap = new HashMap<>();
				}
			}
		}
		return index2EnumMap;
	}
	
	private final int index;

	private BecZone(int index) {
		this.index = index;
		getIndex2EnumMap().put(index, this);
	}

	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public int getOffset() {
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

	/**
	 * Returns the enumeration constant with the given index.
	 * @param index the value in question
	 * @return the enumeration value, unless no enumeration constant has the given 
	 * 	   <code>index</code> in which case <code>null</code> is returned.
	 */
	public static BecZone forIndex(int index) {
		return getIndex2EnumMap().get(index);
	}

	/**
	 * @return the number of non-housekeeping entries in the enumeration
	 */
	public static int size() {
		return bec_SWB.index - bec_AT.index + 1;
	}

	public static class Iterator extends SI32EnumIterator<BecZone> {
		public Iterator() {
			super(bec_AT, bec_SWB, values());
		}
	}
}
