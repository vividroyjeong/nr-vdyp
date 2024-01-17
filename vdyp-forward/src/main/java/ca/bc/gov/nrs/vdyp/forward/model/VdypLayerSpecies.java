package ca.bc.gov.nrs.vdyp.forward.model;

import java.util.Map;
import java.util.Optional;

import ca.bc.gov.nrs.vdyp.model.LayerType;
import ca.bc.gov.nrs.vdyp.model.SpeciesDistributionSet;
import ca.bc.gov.nrs.vdyp.model.UtilizationClass;

public class VdypLayerSpecies {

	// See IPSJF155.doc
	
	private String polygonId; // POLYDESC
	private LayerType layerType; // LAYERG
	private Integer genusIndex; // ISP
	private String genus; // SP0
	private SpeciesDistributionSet speciesDistributions; // SP64DISTL1
	private Float siteIndex; // SI
	private Float dominantHeight; // HD
	private Float ageTotal; // AGETOT
	private Float ageAtBreastHeight; // AGEBH
	private Float yearsToBreastHeight; // YTBH
	private Optional<Boolean> isPrimary; // ISITESP
	private Optional<Integer> siteCurveNumber; // SCN
	
	// Set after construction
	private VdypPolygonLayer parent;
	Map<UtilizationClass, VdypSpeciesUtilization> utilizations;

	public VdypLayerSpecies(String polygonId, LayerType layerType, Integer genusIndex, String genus
			, SpeciesDistributionSet speciesDistributions, Float siteIndex, Float dominantHeight
			, Float ageTotal, Float ageAtBreastHeight, Float yearsToBreastHeight
			, Optional<Boolean> isPrimary, Optional<Integer> siteCurveNumber)
	{
		this.polygonId = polygonId;
		this.layerType = layerType;
		this.genusIndex = genusIndex;
		this.genus = genus;
		this.speciesDistributions = speciesDistributions;
		this.siteIndex = siteIndex;
		this.dominantHeight = dominantHeight;
		this.ageTotal = ageTotal;
		
		// From VDYPGETS.FOR, lines 235 onwards
		if (ageAtBreastHeight < -8.9)
		{
			if (ageTotal > 0.0 && yearsToBreastHeight > 0.0)
			{
				ageAtBreastHeight = ageTotal - yearsToBreastHeight;
				if (ageAtBreastHeight < 0.0)
					ageAtBreastHeight = -9.0f;
			}
		}
		else if (ageTotal < -8.9)
		{
			if (ageAtBreastHeight > 0.0 && yearsToBreastHeight > 0.0)
				ageTotal = ageAtBreastHeight + yearsToBreastHeight;
		}
		else if (yearsToBreastHeight < -8.9)
		{
			if (ageAtBreastHeight > 0.0 && ageTotal > yearsToBreastHeight)
				yearsToBreastHeight = ageTotal - ageAtBreastHeight;
		}
		
		this.ageAtBreastHeight = ageAtBreastHeight;
		this.yearsToBreastHeight = yearsToBreastHeight;
		
		this.isPrimary = isPrimary;
		this.siteCurveNumber = siteCurveNumber;
	}

	void setParent(VdypPolygonLayer parent)
	{
		this.parent = parent;
	}

	public void setUtilizations(Map<UtilizationClass, VdypSpeciesUtilization> utilizations)
	{
		this.utilizations = utilizations;
	}

	String getPolygonId()
	{
		return polygonId;
	}

	LayerType getLayerType()
	{
		return layerType;
	}

	public Integer getSpeciesIndex()
	{
		return genusIndex;
	}

	public String getSpecies()
	{
		return genus;
	}

	public SpeciesDistributionSet getSpeciesDistributions()
	{
		return speciesDistributions;
	}

	public Float getSiteIndex()
	{
		return siteIndex;
	}

	public Float getDominantHeight()
	{
		return dominantHeight;
	}

	public Float getTotalAge()
	{
		return ageTotal;
	}

	public Float getAgeAtBreastHeight()
	{
		return ageAtBreastHeight;
	}

	public Float getYearsToBreastHeight()
	{
		return yearsToBreastHeight;
	}

	public Optional<Boolean> getIsPrimary()
	{
		return isPrimary;
	}

	public Optional<Integer> getSiteCurveNumber()
	{
		return siteCurveNumber;
	}

	public VdypPolygonLayer getParent()
	{
		return parent;
	}

	public Map<UtilizationClass, VdypSpeciesUtilization> getUtilizations()
	{
		return utilizations;
	}
}
