package ca.bc.gov.nrs.vdyp.forward.model;

import ca.bc.gov.nrs.vdyp.model.LayerType;
import ca.bc.gov.nrs.vdyp.model.UtilizationClass;

public class VdypSpeciesUtilization {

	// See IPSJF155.doc

	private String polygonId; // POLYDESC
	private LayerType layerType; // LAYERG
	private Integer genusIndex; // ISP
	private String genus; // SP0
	private UtilizationClass ucIndex; // J
	private Float basalArea; // SP0
	private Float liveTreesPerHectare; 
	private Float loreyHeight;
	private Float wholeStemVolume;
	private Float closeUtilizationVolume;
	private Float cuVolumeMinusDecay;
	private Float cuVolumeMinusDecayWastage;
	private Float cuVolumeMinusDecayWastageBreakage;
	private Float quadraticMeanDiameterAtBH;
	
	// Set after construction
	private VdypLayerSpecies parent;

	public VdypSpeciesUtilization(String polygonId, LayerType layerType, Integer genusIndex
			, String genus, UtilizationClass ucIndex, Float basalArea, Float liveTreesPerHectare
			, Float loreyHeight, Float wholeStemVolume, Float closeUtilizationVolume
			, Float cuVolumeMinusDecay, Float cuVolumeMinusDecayWastage, Float cuVolumeMinusDecayWastageBreakage
			, Float quadraticMeanDiameterAtBH)
	{
		this.polygonId = polygonId;
		this.layerType = layerType;
		this.genusIndex = genusIndex;
		this.genus = genus;
		this.ucIndex = ucIndex;
		this.basalArea = basalArea;
		this.liveTreesPerHectare = liveTreesPerHectare;
		this.loreyHeight = loreyHeight;
		this.wholeStemVolume = wholeStemVolume;
		this.closeUtilizationVolume = closeUtilizationVolume;
		this.cuVolumeMinusDecay = cuVolumeMinusDecay;
		this.cuVolumeMinusDecayWastage = cuVolumeMinusDecayWastage;
		this.cuVolumeMinusDecayWastageBreakage = cuVolumeMinusDecayWastageBreakage;
		this.quadraticMeanDiameterAtBH = quadraticMeanDiameterAtBH;
	}

	void setParent(VdypLayerSpecies parent)
	{
		this.parent = parent;
	}

	public String getPolygonId()
	{
		return polygonId;
	}

	public LayerType getLayerType()
	{
		return layerType;
	}

	Integer getGenusIndex()
	{
		return genusIndex;
	}

	public String getGenus()
	{
		return genus;
	}

	public UtilizationClass getUcIndex()
	{
		return ucIndex;
	}

	public Float getBasalArea()
	{
		return basalArea;
	}

	public Float getLiveTreesPerHectare()
	{
		return liveTreesPerHectare;
	}

	public Float getLoreyHeight()
	{
		return loreyHeight;
	}

	public Float getWholeStemVolume()
	{
		return wholeStemVolume;
	}

	public Float getCloseUtilizationVolume()
	{
		return closeUtilizationVolume;
	}

	public Float getCuVolumeMinusDecay()
	{
		return cuVolumeMinusDecay;
	}

	public Float getCuVolumeMinusDecayWastage()
	{
		return cuVolumeMinusDecayWastage;
	}

	public Float getCuVolumeMinusDecayWastageBreakage()
	{
		return cuVolumeMinusDecayWastageBreakage;
	}

	public Float getQuadraticMeanDiameterAtBH()
	{
		return quadraticMeanDiameterAtBH;
	}

	public VdypLayerSpecies getParent()
	{
		return parent;
	}
}
