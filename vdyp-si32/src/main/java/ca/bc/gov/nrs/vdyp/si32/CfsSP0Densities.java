package ca.bc.gov.nrs.vdyp.si32;

import ca.bc.gov.nrs.vdyp.si32.enumerations.CFSDensity;
import ca.bc.gov.nrs.vdyp.si32.enumerations.SP0Name;

/**
 * This is a two dimensional array indexed first by {@link SP0Name} and then
 * {@link CFSDensity}, yielding the density value for this species and density.
 * 
 * The density values come from Table 6 of 'Volume_to_Biomass.doc' found in the 
 * folder 'Documents/CFS-Biomass'.
 */
public class CfsSP0Densities {

	public static float getValue(SP0Name sp0Name, CFSDensity cfsDensity) {
		
		return array[sp0Name.getIndex()][cfsDensity.ordinal()];
	}
	
	private final static float[][] array = {
			{ 295.00F, 229.00F, 564.00F }, //
			{ 416.00F, 304.00F, 519.00F }, //
			{ 379.25F, 204.00F, 541.00F }, //
			{ 391.00F, 238.00F, 475.00F }, //
			{ 373.00F, 333.00F, 603.00F }, //
			{ 607.00F, 512.00F, 693.00F }, //
			{ 445.00F, 323.00F, 615.00F }, //
			{ 476.00F, 249.00F, 661.00F }, //
			{ 524.25F, 323.00F, 616.00F }, //
			{ 466.00F, 466.00F, 530.00F }, //
			{ 420.00F, 204.00F, 693.00F }, //
			{ 423.00F, 256.00F, 518.00F }, //
			{ 373.00F, 237.00F, 496.00F }, //
			{ 420.00F, 204.00F, 693.00F }, //
			{ 387.00F, 257.00F, 568.00F }, //
			{ 453.00F, 239.00F, 544.00F }
	};
}
