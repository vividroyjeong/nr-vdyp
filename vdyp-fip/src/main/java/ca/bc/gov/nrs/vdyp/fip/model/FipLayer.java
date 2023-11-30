package ca.bc.gov.nrs.vdyp.fip.model;

import java.util.Optional;

import ca.bc.gov.nrs.vdyp.common.Computed;
import ca.bc.gov.nrs.vdyp.model.BaseVdypLayer;
import ca.bc.gov.nrs.vdyp.model.Layer;

public class FipLayer extends BaseVdypLayer<FipSpecies> {

	static final String POLYGON_IDENTIFIER = "POLYGON_IDENTIFIER"; // POLYDESC
	static final String LAYER = "LAYER"; // LAYER
	static final String AGE_TOTAL = "AGE_TOTAL"; // AGETOT
	static final String HEIGHT = "HEIGHT"; // HT
	static final String SITE_INDEX = "SITE_INDEX"; // SI
	static final String CROWN_CLOSURE = "CROWN_CLOSURE"; // CC
	static final String SITE_SP0 = "SITE_SP0"; // SITESP0
	static final String SITE_SP64 = "SITE_SP64"; // SITESP64
	static final String YEARS_TO_BREAST_HEIGHT = "YEARS_TO_BREAST_HEIGHT"; // YTBH
	static final String INVENTORY_TYPE_GROUP = "INVENTORY_TYPE_GROUP"; // ITGFIP
	static final String BREAST_HEIGHT_AGE = "BREAST_HEIGHT_AGE"; // AGEBH

	float crownClosure; // FIPL_1/CC_L1 or FIP:_V/CC_V1
	String siteGenus; // FIPL_1A/SITESP0_L1 or FIPL_VA/SITESP0_L1
	String siteSpecies; // FIPL_1A/SITESP64_L1 or FIPL_VA/SITESP64_L1

	// In VDYP7 These are read but not stored in common variables.
	// Marked as Deprecated for now but I think we can just remove them.
	@Deprecated
	Optional<Float> breastHeightAge = Optional.empty();

	public FipLayer(String polygonIdentifier, Layer layer) {
		super(polygonIdentifier, layer);
	}

	public float getCrownClosure() {
		return crownClosure;
	}

	public String getSiteSp0() {
		return siteGenus;
	}

	public String getSiteSp64() {
		return siteSpecies;
	}

	@Deprecated
	public Optional<Float> getBreastHeightAge() {
		return breastHeightAge;
	}

	public void setCrownClosure(float crownClosure) {
		this.crownClosure = crownClosure;
	}

	public void setSiteGenus(String sireSp0) {
		this.siteGenus = sireSp0;
	}

	public void setSiteSpecies(String siteSp64) {
		this.siteSpecies = siteSp64;
	}

	@Deprecated
	public void setBreastHeightAge(Optional<Float> breastHeightAge) {
		this.breastHeightAge = breastHeightAge;
	}

	@Computed
	public float getAgeTotalSafe() {
		return super.getAgeTotal().orElseThrow(() -> new IllegalStateException());
	}

	@Computed
	public float getHeightSafe() {
		return super.getHeight().orElseThrow(() -> new IllegalStateException());
	}

	@Computed
	public float getYearsToBreastHeightSafe() {
		return super.getYearsToBreastHeight().orElseThrow(() -> new IllegalStateException());
	}

	@Computed
	public void setAgeTotalSafe(float ageTotal) {
		super.setAgeTotal(Optional.of(ageTotal));
	}

	@Computed
	public void setHeightSafe(float height) {
		super.setHeight(Optional.of(height));
	}

	@Computed
	public void setYearsToBreastHeightSafe(float yearsToBreastHeight) {
		super.setYearsToBreastHeight(Optional.of(yearsToBreastHeight));
	}

	@Override
	public void setAgeTotal(Optional<Float> ageTotal) {
		ageTotal.orElseThrow(() -> new IllegalArgumentException());
		super.setAgeTotal(ageTotal);
	}

	@Override
	public void setHeight(Optional<Float> height) {
		height.orElseThrow(() -> new IllegalArgumentException());
		super.setHeight(height);
	}

	@Override
	public void setYearsToBreastHeight(Optional<Float> yearsToBreastHeight) {
		yearsToBreastHeight.orElseThrow(() -> new IllegalArgumentException());
		super.setYearsToBreastHeight(yearsToBreastHeight);
	}

}
