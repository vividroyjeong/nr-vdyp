package ca.bc.gov.nrs.vdyp.model;

import java.util.Optional;
import java.util.function.Function;

public class VdypPolygon extends BaseVdypPolygon<VdypLayer, Float> {

	// TODO better name
	int itg;

	// TODO better name
	int grpBa1;

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

	// TODO better name
	public int getItg() {
		return itg;
	}

	// TODO better name
	public void setItg(int itg) {
		this.itg = itg;
	}

	// TODO better name
	public int getGrpBa1() {
		return grpBa1;
	}

	// TODO better name
	public void setGrpBa1(int grpBa1) {
		this.grpBa1 = grpBa1;
	}

}
