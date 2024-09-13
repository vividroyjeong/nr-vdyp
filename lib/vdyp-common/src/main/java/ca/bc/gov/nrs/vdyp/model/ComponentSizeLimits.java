package ca.bc.gov.nrs.vdyp.model;

public record ComponentSizeLimits(
		float loreyHeightMaximum, //
		float quadMeanDiameterMaximum, //
		float minQuadMeanDiameterLoreyHeightRatio, //
		float maxQuadMeanDiameterLoreyHeightRatio
) {
}
