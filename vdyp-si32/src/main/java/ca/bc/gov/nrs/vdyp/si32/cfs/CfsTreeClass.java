package ca.bc.gov.nrs.vdyp.si32.cfs;

import java.text.MessageFormat;

import ca.bc.gov.nrs.vdyp.si32.enumerations.SI32Enum;
import ca.bc.gov.nrs.vdyp.si32.enumerations.SI32EnumIterator;

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
public enum CfsTreeClass implements SI32Enum<CfsTreeClass> {
	cfsTreeCls_UNKNOWN(-1, "Unknown CFS Tree Class"),

	cfsTreeCls_Missing(0, "Missing"), //
	cfsTreeCls_LiveNoPath(1, "Live, no pathological indicators"), //
	cfsTreeCls_LiveWithPath(2, "Live, some patholigical indicators"), //
	cfsTreeCls_DeadPotential(3, "Dead, potentially merchantable"), //
	cfsTreeCls_DeadUseless(4, "Dead, not merchantable"), //
	cfsTreeCls_Veteran(5, "Veteran"), //
	cfsTreeCls_NoLongerUsed(6, "No longer used");

	private final int index;
	private final String description;

	private CfsTreeClass(int index, String description) {
		this.index = index;
		this.description = description;
	}

	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public int getOffset() {
		if (this.equals(cfsTreeCls_UNKNOWN)) {
			throw new UnsupportedOperationException(MessageFormat
					.format("Cannot call getIndex on {} as it's not a standard member of the enumeration", this));
		}
		
		return index;
	}
	
	@Override
	public String getText() {
		if (this.equals(cfsTreeCls_UNKNOWN)) {
			throw new UnsupportedOperationException(MessageFormat
					.format("Cannot call getText on {} as it's not a standard member of the enumeration", this));
		}
		
		return this.toString().substring("cfsTreeCls_".length());
	}
	
	public String getDescription() {
		return this.description;
	}

	/**
	 * Returns the enumeration constant with the given index.
	 * @param index the value in question
	 * @return the enumeration value, unless no enumeration constant has the given 
	 * 	   <code>index</code> in which case <code>null</code> is returned.
	 */
	public static CfsTreeClass forIndex(int index) {
		
		for (CfsTreeClass e: CfsTreeClass.values()) {
			if (index == e.index)
				return e;
		}
		
		return null;
	}

	/**
	 * @return the number of non-housekeeping entries in the enumeration
	 */
	public static int size() {
		return cfsTreeCls_NoLongerUsed.index - cfsTreeCls_Missing.index + 1;
	}

	public static class Iterator extends SI32EnumIterator<CfsTreeClass> {
		public Iterator() {
			super(cfsTreeCls_Missing, cfsTreeCls_NoLongerUsed, values());
		}
	}
}
