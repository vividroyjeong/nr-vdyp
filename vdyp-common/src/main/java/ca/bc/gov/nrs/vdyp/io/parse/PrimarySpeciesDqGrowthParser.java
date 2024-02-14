package ca.bc.gov.nrs.vdyp.io.parse;

import ca.bc.gov.nrs.vdyp.common.ControlKey;

public class PrimarySpeciesDqGrowthParser extends PrimarySpeciesGrowthParser {
	
	public static final String CONTROL_KEY = "PRIMARY_SP_DQ_GROWTH";

	@Override
	public ControlKey getControlKey() {
		return ControlKey.PRIMARY_SP_DQ_GROWTH;
	}
}
