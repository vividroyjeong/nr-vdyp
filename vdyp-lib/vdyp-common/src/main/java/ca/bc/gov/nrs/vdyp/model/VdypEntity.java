package ca.bc.gov.nrs.vdyp.model;

public interface VdypEntity {
	/** This indicates "not set" for a Float */
	Float MISSING_FLOAT_VALUE = Float.NaN;
	
	/** This is a "special" magic number indicating "not set" for an Integer value - a legacy of VDYP 7 */
	Integer MISSING_INTEGER_VALUE = -9;
}
