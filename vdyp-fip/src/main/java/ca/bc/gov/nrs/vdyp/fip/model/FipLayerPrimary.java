package ca.bc.gov.nrs.vdyp.fip.model;

import java.util.Optional;

import ca.bc.gov.nrs.vdyp.common.Computed;
import ca.bc.gov.nrs.vdyp.model.Layer;

public class FipLayerPrimary extends FipLayer {

	Optional<Character> stockingClass = Optional.empty(); // FIPL_1ST/STK_L1

	private String primaryGenus; // FIPL_1C/JPRIME

	public FipLayerPrimary(String polygonIdentifier) {
		super(polygonIdentifier, Layer.PRIMARY);
	}

	public Optional<Character> getStockingClass() {
		return stockingClass;
	}

	public void setStockingClass(Optional<Character> stockingClass) {
		this.stockingClass = stockingClass;
	}

	public String getPrimaryGenus() {
		return primaryGenus;
	}

	public void setPrimaryGenus(String primaryGenus) {
		this.primaryGenus = primaryGenus;
	}

	@Computed
	public FipSpecies getPrimarySpeciesRecord() {
		return getSpecies().get(primaryGenus);
	}

	@Override
	public void setAgeTotal(Optional<Float> value) {
		value.orElseThrow(() -> new IllegalArgumentException());
		super.setAgeTotal(value);
	}

	@Computed
	public float getAgeTotalSafe() {
		return super.getAgeTotal().get();
	}

	@Computed
	public void setAgeTotalSafe(float value) {
		setAgeTotal(Optional.of(value));
	}

	@Override
	public void setYearsToBreastHeight(Optional<Float> value) {
		value.orElseThrow(() -> new IllegalArgumentException());
		super.setYearsToBreastHeight(value);
	}

	@Computed
	public float getYearsToBreastHeightSafe() {
		return super.getYearsToBreastHeight().get();
	}

	@Computed
	public void setYearsToBreastHeightSafe(float value) {
		setYearsToBreastHeight(Optional.of(value));
	}
}
