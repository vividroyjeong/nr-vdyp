package ca.bc.gov.nrs.vdyp.io.parse;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class NonPrimarySpeciesGrowthParser extends SimpleCoefficientParser2<String, Integer> {
	
	private static final int MAX_BASAL_AREA_GROUP_ID = 30;
	
	private static final String BASAL_AREA_GROUP_ID_KEY = "BasalAreaGroupId";

	public NonPrimarySpeciesGrowthParser(String controlKey)
	{
		super(1, controlKey);
			this.speciesKey()
				.key(3, BASAL_AREA_GROUP_ID_KEY, ValueParser.INTEGER, IntStream.rangeClosed(0, MAX_BASAL_AREA_GROUP_ID)
						.boxed().collect(Collectors.toList()), "%s is not a valid basal area group id")
				.coefficients(3, 10);
	}
}
