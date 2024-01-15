package ca.bc.gov.nrs.vdyp.model;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class BaseVdypLayer<S extends BaseVdypSpecies> {

	private final String polygonIdentifier;
	private final LayerType layer;
	private float ageTotal; // LVCOM3/AGETOTLV, L1COM3/AGETOTL1
	private float height; // LVCOM3/HDLV, L1COM3/HDL1
	private float yearsToBreastHeight; // LVCOM3/YTBHLV, L1COM3/YTBHL1
	private LinkedHashMap<String, S> species = new LinkedHashMap<>();

	public BaseVdypLayer(String polygonIdentifier, LayerType layer, float ageTotal, float yearsToBreastHeight) {
		this.polygonIdentifier = polygonIdentifier;
		this.layer = layer;
		this.ageTotal = ageTotal;
		this.yearsToBreastHeight = yearsToBreastHeight;
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

}
