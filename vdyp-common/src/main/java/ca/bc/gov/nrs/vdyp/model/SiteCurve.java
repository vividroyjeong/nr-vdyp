package ca.bc.gov.nrs.vdyp.model;

import java.text.MessageFormat;

import ca.bc.gov.nrs.vdyp.common_calculators.enumerations.SiteIndexEquation;

public class SiteCurve {
	private final SiteIndexEquation coastalRegionValue;
	private final SiteIndexEquation interiorRegionValue;

	public SiteCurve(SiteIndexEquation coastalRegionValue, SiteIndexEquation interiorRegionValue) {
		this.coastalRegionValue = coastalRegionValue;
		this.interiorRegionValue = interiorRegionValue;
	}

	public SiteIndexEquation getValue(Region region) {
		if (region.equals(Region.COASTAL))
			return coastalRegionValue;
		else if (region.equals(Region.INTERIOR))
			return interiorRegionValue;
		throw new IllegalStateException(MessageFormat.format("{0} is not a supported region", region));
	}
}
