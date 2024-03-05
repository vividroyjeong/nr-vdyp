package ca.bc.gov.nrs.vdyp.vri.model;

import java.util.Optional;

import ca.bc.gov.nrs.vdyp.model.BaseVdypSite;

public class VriSite extends BaseVdypSite {

	public VriSite(
			String siteGenus, Optional<Integer> siteCurveNumber, Optional<Float> siteIndex, Optional<Float> height,
			Optional<Float> ageTotal, Optional<Float> yearsToBreastHeight
	) {
		super(siteGenus, siteCurveNumber, siteIndex, height, ageTotal, yearsToBreastHeight);
	}

}
