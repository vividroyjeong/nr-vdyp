package ca.bc.gov.nrs.vdyp.model;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

public class BaseVdypLayer<S extends BaseVdypSpecies> {

	private final String polygonIdentifier;
	private final LayerType layer;
	private float ageTotal; // LVCOM3/AGETOTLV, L1COM3/AGETOTL1
	private float height; // LVCOM3/HDLV, L1COM3/HDL1
	private float yearsToBreastHeight; // LVCOM3/YTBHLV, L1COM3/YTBHL1
	private LinkedHashMap<String, S> species = new LinkedHashMap<>();

	public BaseVdypLayer(
			String polygonIdentifier, LayerType layer, float ageTotal, float yearsToBreastHeight, float height
	) {
		this.polygonIdentifier = polygonIdentifier;
		this.layer = layer;
		this.ageTotal = ageTotal;
		this.yearsToBreastHeight = yearsToBreastHeight;
		this.height = height;
	}

	public String getPolygonIdentifier() {
		return polygonIdentifier;
	}

	public LayerType getLayer() {
		return layer;
	}

	public float getAgeTotal() {
		return ageTotal;
	}

	public float getHeight() {
		return height;
	}

	public float getYearsToBreastHeight() {
		return yearsToBreastHeight;
	}

	public void setAgeTotal(float ageTotal) {
		this.ageTotal = ageTotal;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public void setYearsToBreastHeight(float yearsToBreastHeight) {
		this.yearsToBreastHeight = yearsToBreastHeight;
	}

	public LinkedHashMap<String, S> getSpecies() {
		return species;
	}

	public void setSpecies(Map<String, S> species) {
		this.species.clear();
		this.species.putAll(species);
	}

	public void setSpecies(Collection<S> species) {
		this.species.clear();
		species.forEach(spec -> this.species.put(spec.getGenus(), spec));
	}

	protected abstract static class Builder<T extends BaseVdypLayer<?>> extends ModelClassBuilder<T> {
		protected Optional<String> polygonIdentifier = Optional.empty();
		protected Optional<LayerType> layer = Optional.empty();
		protected Optional<Float> ageTotal = Optional.empty();
		protected Optional<Float> height = Optional.empty();
		protected Optional<Float> yearsToBreastHeight = Optional.empty();

		public Builder<T> polygonIdentifier(String polygonIdentifier) {
			this.polygonIdentifier = Optional.of(polygonIdentifier);
			return this;
		}

		public Builder<T> layerType(LayerType layer) {
			this.layer = Optional.of(layer);
			return this;
		}

		public Builder<T> ageTotal(float ageTotal) {
			this.ageTotal = Optional.of(ageTotal);
			return this;
		}

		public Builder<T> height(float height) {
			this.height = Optional.of(height);
			return this;
		}

		public Builder<T> yearsToBreastHeight(float yearsToBreastHeight) {
			this.yearsToBreastHeight = Optional.of(yearsToBreastHeight);
			return this;
		}

		@Override
		protected void check(Collection<String> errors) {
			requirePresent(polygonIdentifier, "polygonIdentifier", errors);
			requirePresent(layer, "layer", errors);
			requirePresent(ageTotal, "ageTotal", errors);
			requirePresent(yearsToBreastHeight, "yearsToBreastHeight", errors);
			requirePresent(height, "height", errors);
		}

	};

}
