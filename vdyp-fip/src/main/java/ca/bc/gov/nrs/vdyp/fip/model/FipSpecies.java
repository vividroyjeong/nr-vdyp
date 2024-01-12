package ca.bc.gov.nrs.vdyp.fip.model;

import ca.bc.gov.nrs.vdyp.model.BaseVdypSpecies;
import ca.bc.gov.nrs.vdyp.model.LayerType;

public class FipSpecies extends BaseVdypSpecies {

	public FipSpecies(String polygonIdentifier, LayerType layer, String genus) {
		super(polygonIdentifier, layer, genus);
	}

	public FipSpecies(FipSpecies toCopy) {
		super(toCopy);
	}
}
