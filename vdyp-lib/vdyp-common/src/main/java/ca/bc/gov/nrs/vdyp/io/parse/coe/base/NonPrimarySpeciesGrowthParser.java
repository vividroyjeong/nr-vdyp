package ca.bc.gov.nrs.vdyp.io.parse.coe.base;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.common.Utils;
import ca.bc.gov.nrs.vdyp.io.parse.value.ValueParser;

public abstract class NonPrimarySpeciesGrowthParser extends OptionalCoefficientParser2<String, Integer> {

	private static final int MAX_BASAL_AREA_GROUP_ID = 30;

	private static final String BASAL_AREA_GROUP_ID_KEY = "BasalAreaGroupId";

	protected NonPrimarySpeciesGrowthParser(ControlKey controlKey) {
		super(1, controlKey);
		this.speciesKey()
				.key(
						3, BASAL_AREA_GROUP_ID_KEY, ValueParser.INTEGER,
						IntStream.rangeClosed(0, MAX_BASAL_AREA_GROUP_ID).boxed().collect(Collectors.toList()),
						"%s is not a valid basal area group id", k -> Utils.nullOrBlank(k)
				).coefficients(3, 10);
	}
}
