package ca.bc.gov.nrs.vdyp.model;

import java.util.Collections;
import java.util.List;

public enum UtilizationClassVariable {
	BASAL_AREA, QUAD_MEAN_DIAMETER, LOREY_HEIGHT, WHOLE_STEM_VOLUME;

	public static final List<UtilizationClassVariable> ALL = Collections.unmodifiableList(
			List.of(BASAL_AREA, QUAD_MEAN_DIAMETER, LOREY_HEIGHT, WHOLE_STEM_VOLUME)
	);
}
