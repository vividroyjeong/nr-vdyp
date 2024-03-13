package ca.bc.gov.nrs.vdyp.forward.model;

import java.util.Map;
import java.util.Optional;

import ca.bc.gov.nrs.vdyp.model.LayerType;
import ca.bc.gov.nrs.vdyp.model.SpeciesDistributionSet;
import ca.bc.gov.nrs.vdyp.model.UtilizationClass;

public class VdypLayerSpecies {

	// See IPSJF155.doc

	private final VdypPolygonDescription polygonId; // POLYDESC
	private final LayerType layerType; // LAYERG
	private final Integer genusIndex; // ISP
	private final Optional<String> genus; // SP0
	private final SpeciesDistributionSet speciesDistributions; // SP64DISTL1
	private final Float siteIndex; // SI
	private final Float dominantHeight; // HD
	private final Float ageTotal; // AGETOT
	private final Float ageAtBreastHeight; // AGEBH
	private final Float yearsToBreastHeight; // YTBH
	private final Optional<Boolean> isPrimary; // ISITESP
	private final Optional<Integer> siteCurveNumber; // SCN

	// Set after construction
	private VdypPolygonLayer parent;
	Map<UtilizationClass, VdypSpeciesUtilization> utilizations;

	public VdypLayerSpecies(
			VdypPolygonDescription polygonId, LayerType layerType, Integer genusIndex, Optional<String> genus,
			SpeciesDistributionSet speciesDistributions, Float siteIndex, Float dominantHeight, Float ageTotal,
			Float ageAtBreastHeight, Float yearsToBreastHeight, Optional<Boolean> isPrimary,
			Optional<Integer> siteCurveNumber
	) {
		this.polygonId = polygonId;
		this.layerType = layerType;
		this.genusIndex = genusIndex;
		this.genus = genus;
		this.speciesDistributions = speciesDistributions;
		this.siteIndex = siteIndex;
		this.dominantHeight = dominantHeight;

		// From VDYPGETS.FOR, lines 235 onwards
		if (ageAtBreastHeight < -8.9) {
			if (ageTotal > 0.0 && yearsToBreastHeight > 0.0) {
				ageAtBreastHeight = ageTotal - yearsToBreastHeight;
				if (ageAtBreastHeight < 0.0)
					ageAtBreastHeight = -9.0f;
			}
		} else if (ageTotal < -8.9) {
			if (ageAtBreastHeight > 0.0 && yearsToBreastHeight > 0.0)
				ageTotal = ageAtBreastHeight + yearsToBreastHeight;
		} else if (yearsToBreastHeight < -8.9) {
			if (ageAtBreastHeight > 0.0 && ageTotal > yearsToBreastHeight)
				yearsToBreastHeight = ageTotal - ageAtBreastHeight;
		}

		this.ageTotal = ageTotal;
		this.ageAtBreastHeight = ageAtBreastHeight;
		this.yearsToBreastHeight = yearsToBreastHeight;

		this.isPrimary = isPrimary;
		this.siteCurveNumber = siteCurveNumber;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(polygonId).append(' ').append(layerType).append(' ').append(genusIndex);
		
		return sb.toString();
	}

	public void setParent(VdypPolygonLayer parent) {
		this.parent = parent;
	}

	public void setUtilizations(Map<UtilizationClass, VdypSpeciesUtilization> utilizations) {
		this.utilizations = utilizations;
	}

	public VdypPolygonDescription getPolygonId() {
		return polygonId;
	}

	public LayerType getLayerType() {
		return layerType;
	}

	public Integer getGenusIndex() {
		return genusIndex;
	}

	public Optional<String> getGenus() {
		return genus;
	}

	public SpeciesDistributionSet getSpeciesDistributions() {
		return speciesDistributions;
	}

	public Float getSiteIndex() {
		return siteIndex;
	}

	public Float getDominantHeight() {
		return dominantHeight;
	}

	public Float getAgeTotal() {
		return ageTotal;
	}

	public Float getAgeAtBreastHeight() {
		return ageAtBreastHeight;
	}

	public Float getYearsToBreastHeight() {
		return yearsToBreastHeight;
	}

	public Optional<Boolean> getIsPrimary() {
		return isPrimary;
	}

	public Optional<Integer> getSiteCurveNumber() {
		return siteCurveNumber;
	}

	public VdypPolygonLayer getParent() {
		return parent;
	}

	public Map<UtilizationClass, VdypSpeciesUtilization> getUtilizations() {
		return utilizations;
	}
}
