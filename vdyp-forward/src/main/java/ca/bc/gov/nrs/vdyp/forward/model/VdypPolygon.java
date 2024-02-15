package ca.bc.gov.nrs.vdyp.forward.model;

import java.util.Optional;

import ca.bc.gov.nrs.vdyp.model.BecDefinition;

public class VdypPolygon {

	// See IPSJF155.doc

	private final String description; // POLYDESC
	private final Integer year; // derived - last four characters of POLYDESC
	private final BecDefinition biogeoclimaticZone; // BEC
	private final Character forestInventoryZone; // FIZ
	private final Float percentForestLand; // PCTFLAND
	private final Optional<Integer> inventoryTypeGroup; // ITG
	private final Optional<Integer> basalAreaGroup; // GRPBA1
	private final Optional<FipMode> fipMode; // MODE

	// Set after construction
	private VdypPolygonLayer primaryLayer;
	private Optional<VdypPolygonLayer> veteranLayer;

	public VdypPolygon(
			String description, Integer year, BecDefinition bec, Character fizId, Float percentForestLand,
			Optional<Integer> inventoryTypeGroup, Optional<Integer> basalAreaGroup, Optional<FipMode> fipMode
	) {
		super();
		this.description = description;
		this.year = year;
		this.biogeoclimaticZone = bec;
		this.forestInventoryZone = fizId;
		if (percentForestLand <= 0.0)
			this.percentForestLand = 90.0f;
		else
			this.percentForestLand = percentForestLand;
		this.inventoryTypeGroup = inventoryTypeGroup;
		this.basalAreaGroup = basalAreaGroup;
		this.fipMode = fipMode;
	}

	public void setPrimaryLayer(VdypPolygonLayer primaryLayer) {
		this.primaryLayer = primaryLayer;
	}

	public void setVeteranLayer(Optional<VdypPolygonLayer> veteranLayer) {
		this.veteranLayer = veteranLayer;
	}

	public String getDescription() {
		return description;
	}

	public Integer getYear() {
		return year;
	}

	public BecDefinition getBiogeoclimaticZone() {
		return biogeoclimaticZone;
	}

	public Character getForestInventoryZone() {
		return forestInventoryZone;
	}

	public Float getPercentForestLand() {
		return percentForestLand;
	}

	public Optional<Integer> getInventoryTypeGroup() {
		return inventoryTypeGroup;
	}

	public Optional<Integer> getBasalAreaGroup() {
		return basalAreaGroup;
	}

	public Optional<FipMode> getFipMode() {
		return fipMode;
	}

	public VdypPolygonLayer getPrimaryLayer() {
		return primaryLayer;
	}

	public Optional<VdypPolygonLayer> getVeteranLayer() {
		return veteranLayer;
	}
}
