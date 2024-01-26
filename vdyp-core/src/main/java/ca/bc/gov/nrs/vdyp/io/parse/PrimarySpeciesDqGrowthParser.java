package ca.bc.gov.nrs.vdyp.io.parse;

public class PrimarySpeciesDqGrowthParser extends PrimarySpeciesGrowthParser {
	
	public static final String CONTROL_KEY = "PRIMARY_SP_DQ_GROWTH";

	@Override
	public String getControlKey() {
		return CONTROL_KEY;
	}
}
