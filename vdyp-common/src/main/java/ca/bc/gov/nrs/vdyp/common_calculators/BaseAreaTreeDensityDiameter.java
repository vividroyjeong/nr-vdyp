package ca.bc.gov.nrs.vdyp.common_calculators;

import static ca.bc.gov.nrs.vdyp.math.FloatMath.sqrt;

/**
 * Converts between trees per hectare and quad mean diameter in a given base area
 */
public class BaseAreaTreeDensityDiameter {

	private BaseAreaTreeDensityDiameter() {}
	
	/**
	 * π/10⁴
	 */
	public static final float PI_40K = (float) (Math.PI / 40_000);

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
