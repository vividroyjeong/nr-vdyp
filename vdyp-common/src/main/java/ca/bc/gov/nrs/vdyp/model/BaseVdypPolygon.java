package ca.bc.gov.nrs.vdyp.model;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public abstract class BaseVdypPolygon<L extends BaseVdypLayer<?>, PA> {

	String polygonIdentifier; // FIP_P/POLYDESC
	PA percentAvailable; // FIP_P2/PCTFLAND
	Map<Layer, L> layers = Collections.emptyMap();
	protected String biogeoclimaticZone;
	protected String forestInventoryZone;
	protected Optional<FipMode> modeFip;

	protected BaseVdypPolygon(
			String polygonIdentifier, PA percentAvailable, String fiz, String becIdentifier, Optional<FipMode> modeFip
	) {
		super();
		this.forestInventoryZone = fiz;
		this.biogeoclimaticZone = becIdentifier;
		this.modeFip = modeFip;
		this.polygonIdentifier = polygonIdentifier;
		this.percentAvailable = percentAvailable;
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
	protected <O extends BaseVdypPolygon<?, U>, U> BaseVdypPolygon(O toCopy, Function<U, PA> convertPercentAvailable) {
		this(
				toCopy.getPolygonIdentifier(), convertPercentAvailable.apply(toCopy.getPercentAvailable()),
				toCopy.getForestInventoryZone(), toCopy.getBiogeoclimaticZone(), toCopy.getModeFip()
		);
	}

	public String getPolygonIdentifier() {
		return polygonIdentifier;
	}

	public void setPolygonIdentifier(String polygonIdentifier) {
		this.polygonIdentifier = polygonIdentifier;
	}

	public Map<Layer, L> getLayers() {
		return layers;
	}

	public void setLayers(Map<Layer, L> layers) {
		this.layers = layers;
	}

	public void setLayers(Collection<L> layers) {
		this.layers = new EnumMap<>(Layer.class);
		layers.forEach(spec -> this.layers.put(spec.getLayer(), spec));
	}

	public PA getPercentAvailable() {
		return percentAvailable;
	}

	public void setPercentAvailable(PA percentAvailable) {
		this.percentAvailable = percentAvailable;
	}

	public String getBiogeoclimaticZone() {
		return biogeoclimaticZone;
	}

	public void setBiogeoclimaticZone(String biogeoclimaticZone) {
		this.biogeoclimaticZone = biogeoclimaticZone;
	}

	public String getForestInventoryZone() {
		return forestInventoryZone;
	}

	public void setForestInventoryZone(String forestInventoryZone) {
		this.forestInventoryZone = forestInventoryZone;
	}

	public Optional<FipMode> getModeFip() {
		return modeFip;
	}

	public void setModeFip(Optional<FipMode> modeFip) {
		this.modeFip = modeFip;
	}
}
