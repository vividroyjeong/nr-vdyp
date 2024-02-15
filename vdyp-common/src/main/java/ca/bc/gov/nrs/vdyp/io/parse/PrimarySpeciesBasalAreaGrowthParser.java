package ca.bc.gov.nrs.vdyp.io.parse;

import ca.bc.gov.nrs.vdyp.common.ControlKey;

/**
 * Parses a mapping from a Basal Area Group number to a ModelCoefficients instance containing
 * the coefficients for the Basal Area Primary Species Growth function as described in 
 * IPSJF148.doc.
 * <p>
 * Control index: 148
 * <p>
 * Example file: coe/BASP05.COE
 *
 * @author Michael Junkin, Vivid Solutions
 * @see PrimarySpeciesGrowthParser
 */
public class PrimarySpeciesBasalAreaGrowthParser extends PrimarySpeciesGrowthParser {
	
	@Override
	public ControlKey getControlKey() {
		return ControlKey.PRIMARY_SP_BA_GROWTH;
	}
}
