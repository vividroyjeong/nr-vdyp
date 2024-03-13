package ca.bc.gov.nrs.vdyp.forward.model;

import java.util.Optional;

import ca.bc.gov.nrs.vdyp.model.LayerType;
import ca.bc.gov.nrs.vdyp.model.UtilizationClass;

public class VdypSpeciesUtilization {

	// See IPSJF155.doc

	private final VdypPolygonDescription polygonId; // POLYDESC
	private final LayerType layerType; // LAYERG
	private final Integer genusIndex; // ISP
	private final Optional<String> genus; // SP0
	private final UtilizationClass ucIndex; // J - utilization index
	private final Float basalArea; 
	private final Float liveTreesPerHectare;
	private final Float loreyHeight;
	private final Float wholeStemVolume;
	private final Float closeUtilizationVolume;
	private final Float cuVolumeMinusDecay;
	private final Float cuVolumeMinusDecayWastage;
	private final Float cuVolumeMinusDecayWastageBreakage;
	private final Float quadraticMeanDiameterAtBH;

	// Set after construction
	private VdypLayerSpecies parent;

	public VdypSpeciesUtilization(
			VdypPolygonDescription polygonId, LayerType layerType, Integer genusIndex, Optional<String> genus, UtilizationClass ucIndex,
			Float basalArea, Float liveTreesPerHectare, Float loreyHeight, Float wholeStemVolume,
			Float closeUtilizationVolume, Float cuVolumeMinusDecay, Float cuVolumeMinusDecayWastage,
			Float cuVolumeMinusDecayWastageBreakage, Float quadraticMeanDiameterAtBH
	) {
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
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(polygonId).append(' ').append(layerType).append(' ').append(genusIndex).append(' ').append(ucIndex.index);
		
		return sb.toString();
	}

	public void setParent(VdypLayerSpecies parent) {
		this.parent = parent;
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

	public UtilizationClass getUcIndex() {
		return ucIndex;
	}

	public Float getBasalArea() {
		return basalArea;
	}

	public Float getLiveTreesPerHectare() {
		return liveTreesPerHectare;
	}

	public Float getLoreyHeight() {
		return loreyHeight;
	}

	public Float getWholeStemVolume() {
		return wholeStemVolume;
	}

	public Float getCloseUtilizationVolume() {
		return closeUtilizationVolume;
	}

	public Float getCuVolumeMinusDecay() {
		return cuVolumeMinusDecay;
	}

	public Float getCuVolumeMinusDecayWastage() {
		return cuVolumeMinusDecayWastage;
	}

	public Float getCuVolumeMinusDecayWastageBreakage() {
		return cuVolumeMinusDecayWastageBreakage;
	}

	public Float getQuadraticMeanDiameterAtBH() {
		return quadraticMeanDiameterAtBH;
	}

	public VdypLayerSpecies getParent() {
		return parent;
	}
}
