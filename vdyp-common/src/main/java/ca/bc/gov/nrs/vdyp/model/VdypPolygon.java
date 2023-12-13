package ca.bc.gov.nrs.vdyp.model;

import java.util.Optional;
import java.util.function.Function;

public class VdypPolygon extends BaseVdypPolygon<VdypLayer, Float> {

	public VdypPolygon(
			String polygonIdentifier, Float percentAvailable, String fiz, String becIdentifier,
			Optional<FipMode> modeFip
	) {
		super(polygonIdentifier, percentAvailable, fiz, becIdentifier, modeFip);
	}

	/**
	 * Copy constructs from the simple attributes of another polygon, but does not
	 * copy layers.
	 *
	 * @param <O>                     Type of the polygon to copy
	 * @param <U>                     Type of percent available in the other polygon
	 * @param toCopy                  The polygon to copy
	 * @param convertPercentAvailable Function to convert
	 */
	public <O extends BaseVdypPolygon<?, U>, U> VdypPolygon(O toCopy, Function<U, Float> convertPercentAvailable) {
		super(toCopy, convertPercentAvailable);
	}

}
