package ca.bc.gov.nrs.vdyp.forward.model;

import java.util.Optional;

import ca.bc.gov.nrs.vdyp.model.BecDefinition;

public class VdypPolygon {

	// See IPSJF155.doc
	
	private String description; // POLYDESC
	private Integer year; // derived - last four characters of POLYDESC
	private BecDefinition biogeoclimaticZone; // BEC
	private Character forestInventoryZone; // FIZ
	private Float percentForestLand; // PCTFLAND
	private Optional<Integer> inventoryTypeGroup; // ITG
	private Optional<Integer> basalAreaGroup; // GRPBA1
	private Optional<FipMode> modeFip; // MODE

	// Set after construction
	private VdypPolygonLayer primaryLayer;
	private Optional<VdypPolygonLayer> veteranLayer;
	
	public VdypPolygon(String description, Integer year, BecDefinition biogeoclimaticZone, Character fizId, Float percentForestLand,
			Optional<Integer> inventoryTypeGroup, Optional<Integer> basalAreaGroup, Optional<FipMode> modeFip)
	{
		super();
		this.description = description;
		this.year = year;
		this.biogeoclimaticZone = biogeoclimaticZone;
		this.forestInventoryZone = fizId;
		this.percentForestLand = percentForestLand;
		this.inventoryTypeGroup = inventoryTypeGroup;
		this.basalAreaGroup = basalAreaGroup;
		this.modeFip = modeFip;
	}
	
	void setDescription(String description)
	{
		this.description = description;
	}
	
	void setYear(Integer year)
	{
		this.year = year;
	}
	
	void setBiogeoclimaticZone(BecDefinition biogeoclimaticZone)
	{
		this.biogeoclimaticZone = biogeoclimaticZone;
	}
	
	void setForestInventoryZone(Character forestInventoryZone)
	{
		this.forestInventoryZone = forestInventoryZone;
	}
	
	void setPercentForestLand(Float percentForestLand)
	{
		this.percentForestLand = percentForestLand;
	}
	
	void setInventoryTypeGroup(Optional<Integer> inventoryTypeGroup)
	{
		this.inventoryTypeGroup = inventoryTypeGroup;
	}
	
	void setBasalAreaGroup(Optional<Integer> basalAreaGroup)
	{
		this.basalAreaGroup = basalAreaGroup;
	}
	
	void setModeFip(Optional<FipMode> modeFip)
	{
		this.modeFip = modeFip;
	}
	
	public void setPrimaryLayer(VdypPolygonLayer primaryLayer)
	{
		this.primaryLayer = primaryLayer;
	}

	public void setVeteranLayer(Optional<VdypPolygonLayer> veteranLayer)
	{
		this.veteranLayer = veteranLayer;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public Integer getYear()
	{
		return year;
	}
	
	public BecDefinition getBiogeoclimaticZone()
	{
		return biogeoclimaticZone;
	}
	
	public Character getForestInventoryZone()
	{
		return forestInventoryZone;
	}
	
	public Float getPercentForestLand()
	{
		return percentForestLand;
	}
	
	public Optional<Integer> getInventoryTypeGroup()
	{
		return inventoryTypeGroup;
	}
	
	public Optional<Integer> getBasalAreaGroup()
	{
		return basalAreaGroup;
	}
	
	public Optional<FipMode> getModeFip()
	{
		return modeFip;
	}
	
	public VdypPolygonLayer getPrimaryLayer()
	{
		return primaryLayer;
	}
	
	public Optional<VdypPolygonLayer> getVeteranLayer()
	{
		return veteranLayer;
	}
}
