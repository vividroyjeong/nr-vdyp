package ca.bc.gov.nrs.vdyp.io.parse;

public class PrimarySpeciesBasalAreaGrowthParser extends PrimarySpeciesGrowthParser {
	
	public static final String CONTROL_KEY = "PRIMARY_SP_BA_GROWTH";
	
	@Override
	public String getControlKey() {
		return CONTROL_KEY;
	}
}
