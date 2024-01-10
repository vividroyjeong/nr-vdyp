package ca.bc.gov.nrs.vdyp.model;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class BaseVdypPolygon<L extends BaseVdypLayer<?>, PA> {

	String polygonIdentifier; // FIP_P/POLYDESC
	PA percentAvailable; // FIP_P2/PCTFLAND
	Map<LayerType, L> layers = Collections.emptyMap();

	public BaseVdypPolygon(String polygonIdentifier, PA percentAvailable) {
		super();
		this.polygonIdentifier = polygonIdentifier;
		this.percentAvailable = percentAvailable;
	}

	public String getPolygonIdentifier() {
		return polygonIdentifier;
	}

	public void setPolygonIdentifier(String polygonIdentifier) {
		this.polygonIdentifier = polygonIdentifier;
	}

	public Map<LayerType, L> getLayers() {
		return layers;
	}

	public void setLayers(Map<LayerType, L> layers) {
		this.layers = layers;
	}

	public void setLayers(Collection<L> layers) {
		this.layers = new EnumMap<>(LayerType.class);
		layers.forEach(spec -> this.layers.put(spec.getLayer(), spec));
	}

	public PA getPercentAvailable() {
		return percentAvailable;
	}

	public void setPercentAvailable(PA percentAvailable) {
		this.percentAvailable = percentAvailable;
	}
}
