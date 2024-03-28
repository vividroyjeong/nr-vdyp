package ca.bc.gov.nrs.vdyp.model;

import java.text.MessageFormat;

public class SiteCurve {
	private final int coastalRegionValue;
	private final int interiorRegionValue;

	public SiteCurve(int coastalRegionValue, int interiorRegionValue) {
		this.coastalRegionValue = coastalRegionValue;
		this.interiorRegionValue = interiorRegionValue;
	}

	public int getValue(Region region) {
		if (region.equals(Region.COASTAL))
			return coastalRegionValue;
		else if (region.equals(Region.INTERIOR))
			return interiorRegionValue;
		throw new IllegalStateException(MessageFormat.format("{} is not a supported region", region));
	}
}
