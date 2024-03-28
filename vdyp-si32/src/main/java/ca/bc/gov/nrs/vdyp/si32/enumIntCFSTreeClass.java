package ca.bc.gov.nrs.vdyp.si32;

import java.text.MessageFormat;

/**
 * Provides an enumeration of the different types of Tree Classes as defined by the
 * Canadian Forest Service.
 * <ul>
 * <li> cfsTreeCls_UNKNOWN<p>
 *       Represents an error condition or an uninitialized state. This should
 *       never be considered a true tree class.
 *
 * <li> cfsTreeCls_Missing<p>
 *       The tree class attribute is missing. This differs from the UNKNOWN
 *       indicator in that this represents a valid business value of Missing
 *       (which may in turn represent an business process error) but remains
 *       a valid value for the attribute.
 *
 * <li> cfsTreeCls_LiveNoPath<p>
 *       A tree or stand that is living with no pathological indicators.
 *
 * <li> cfsTreeCls_LiveWithPath<p>
 *       A living tree or stand with some pathological indicators.
 *
 * <li> cfsTreeCls_DeadPotential<p>
 *       A dead tree or stand with potential for harvesting.
 *
 * <li> cfsTreeCls_DeadUseless<p>
 *       A dead tree or stand with no further potential.
 *
 * <li> cfsTreeCls_Veteran<p>
 *       A living tree or stand falling into the Veteran class.
 *
 * <li> cfsTreeCls_NoLongerUsed<p>
 *       No longer used and should be treated as equivalent to:
 *       'cfsTreeCls_LiveWithPath'.
 * </ul>
 * The definitions of this table come from Table 2 found in the 'Volume_to_Biomass.doc' file 
 * located in 'Documents/CFS-Biomass'.
 */

public enum enumIntCFSTreeClass implements SI32Enum<enumIntCFSTreeClass> {
	cfsTreeCls_UNKNOWN(-1),

	cfsTreeCls_Missing(0), //
	cfsTreeCls_LiveNoPath(1), //
	cfsTreeCls_LiveWithPath(2), //
	cfsTreeCls_DeadPotential(3), //
	cfsTreeCls_DeadUseless(4), //
	cfsTreeCls_Veteran(5), //
	cfsTreeCls_NoLongerUsed(6);

	private int intValue;
	private static java.util.HashMap<Integer, enumIntCFSTreeClass> mappings;

	private static java.util.HashMap<Integer, enumIntCFSTreeClass> getMappings() {
		if (mappings == null) {
			synchronized (enumIntCFSTreeClass.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, enumIntCFSTreeClass>();
				}
			}
		}
		return mappings;
	}

	private enumIntCFSTreeClass(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	@Override
	public int getValue() {
		return intValue;
	}

	@Override
	public int getIndex() {
		if (this.equals(cfsTreeCls_UNKNOWN)) {
			throw new UnsupportedOperationException(MessageFormat
					.format("Cannot call getIndex on {} as it's not a standard member of the enumeration", this));
		}
		
		return intValue;
	}
	
	@Override
	public String getText() {
		if (this.equals(cfsTreeCls_UNKNOWN)) {
			throw new UnsupportedOperationException(MessageFormat
					.format("Cannot call getText on {} as it's not a standard member of the enumeration", this));
		}
		
		return this.toString().substring("cfsTreeCls_".length());
	}

	public static enumIntCFSTreeClass forValue(int value) {
		return getMappings().get(value);
	}

	public static int size() {
		return cfsTreeCls_NoLongerUsed.intValue - cfsTreeCls_Missing.intValue + 1;
	}

	public static class Iterator extends SI32EnumIterator<enumIntCFSTreeClass> {
		public Iterator() {
			super(cfsTreeCls_Missing, cfsTreeCls_NoLongerUsed, mappings);
		}
	}
}
