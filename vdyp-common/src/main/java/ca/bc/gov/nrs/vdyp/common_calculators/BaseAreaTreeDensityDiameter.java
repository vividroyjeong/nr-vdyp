package ca.bc.gov.nrs.vdyp.common_calculators;

import static ca.bc.gov.nrs.vdyp.math.FloatMath.sqrt;


/*
 * Converts between trees per hectare and quad mean diameter given a base area
 */
public class BaseAreaTreeDensityDiameter {

	public static final float PI_40K = 0.78539816E-04f;

	// FT_BD
	public static float treesPerHectare(float baseArea, float quadraticMeanDiameter) {
		if (baseArea != 0) {
			return baseArea / PI_40K / (quadraticMeanDiameter * quadraticMeanDiameter);
		}
		return 0f;
	}

	// FD_BT
	public static float quadMeanDiameter(float baseArea, float treesPerHectare) {
		if (baseArea > 1e6f || treesPerHectare > 1e6f || Float.isNaN(baseArea) || Float.isNaN(treesPerHectare)) {
			return 0f;
		} else if (baseArea > 0f && treesPerHectare > 0f) {
			return sqrt(baseArea / treesPerHectare / PI_40K);
		}
		return 0f;
	
	}


}
