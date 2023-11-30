package ca.bc.gov.nrs.vdyp.model;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class BaseVdypLayer<S extends BaseVdypSpecies> {

	private final String polygonIdentifier;
	private final Layer layer;
	private Optional<Float> ageTotal = Optional.empty(); // LVCOM3/AGETOTLV, L1COM3/AGETOTL1
	private Optional<Float> height = Optional.empty(); // LVCOM3/HDLV, L1COM3/HDL1
	private Optional<Float> yearsToBreastHeight = Optional.empty(); // LVCOM3/YTBHLV, L1COM3/YTBHL1
	private LinkedHashMap<String, S> species = new LinkedHashMap<>();
	private Optional<Float> siteIndex = Optional.empty();
	private Optional<Integer> siteCurveNumber = Optional.empty();
	private Optional<Integer> inventoryTypeGroup = Optional.empty();

	public BaseVdypLayer(String polygonIdentifier, Layer layer) {
		super();
		this.polygonIdentifier = polygonIdentifier;
		this.layer = layer;
	}

	public String getPolygonIdentifier() {
		return polygonIdentifier;
	}

	public Layer getLayer() {
		return layer;
	}

	public Optional<Float> getAgeTotal() {
		return ageTotal;
	}

	public Optional<Float> getHeight() {
		return height;
	}

	public Optional<Float> getYearsToBreastHeight() {
		return yearsToBreastHeight;
	}

	public void setAgeTotal(Optional<Float> ageTotal) {
		this.ageTotal = ageTotal;
	}

	public void setHeight(Optional<Float> height) {
		this.height = height;
	}

	public void setYearsToBreastHeight(Optional<Float> yearsToBreastHeight) {
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

	public Optional<Float> getSiteIndex() {
		return siteIndex;
	}

	public void setSiteIndex(Optional<Float> siteIndex) {
		this.siteIndex = siteIndex;
	}

	public Optional<Integer> getSiteCurveNumber() {
		return siteCurveNumber;
	}

	public void setSiteCurveNumber(Optional<Integer> siteCurveNumber) {
		this.siteCurveNumber = siteCurveNumber;
	}

	public Optional<Integer> getInventoryTypeGroup() {
		return inventoryTypeGroup;
	}

	public void setInventoryTypeGroup(Optional<Integer> inventoryTypeGroup) {
		this.inventoryTypeGroup = inventoryTypeGroup;
	}

}
