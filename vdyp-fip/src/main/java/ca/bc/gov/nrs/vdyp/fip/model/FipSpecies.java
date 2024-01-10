package ca.bc.gov.nrs.vdyp.fip.model;

import ca.bc.gov.nrs.vdyp.model.BaseVdypSpecies;
import ca.bc.gov.nrs.vdyp.model.LayerType;

public class FipSpecies extends BaseVdypSpecies {

	static final String POLYGON_IDENTIFIER = "POLYGON_IDENTIFIER"; // POLYDESC
	static final String LAYER = "LAYER"; // LAYER

	public FipSpecies(String polygonIdentifier, LayerType layer, String genus) {
		super(polygonIdentifier, layer, genus);
	}

	public FipSpecies(FipSpecies toCopy) {
		super(toCopy);
	}
}
