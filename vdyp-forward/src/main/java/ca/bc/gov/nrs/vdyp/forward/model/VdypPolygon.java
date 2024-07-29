package ca.bc.gov.nrs.vdyp.forward.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import ca.bc.gov.nrs.vdyp.model.BecDefinition;
import ca.bc.gov.nrs.vdyp.model.PolygonIdentifier;
import ca.bc.gov.nrs.vdyp.model.PolygonMode;

public class VdypPolygon extends VdypEntity {

	// See IPSJF155.doc, appendix II

	private final PolygonIdentifier description; // POLYDESC
	private final BecDefinition biogeoclimaticZone; // BEC
	private final Character forestInventoryZone; // FIZ
	private final float percentForestLand; // PCTFLAND
	private final Optional<Integer> inventoryTypeGroup; // ITG
	private final Optional<Integer> basalAreaGroup; // GRPBA1
	private final Optional<PolygonMode> polygonMode; // MODE
	
	// Set after construction
	private VdypPolygonLayer primaryLayer;
	private Optional<VdypPolygonLayer> veteranLayer = Optional.empty();
	private Set<VdypPolygonLayer> layers = new HashSet<>();
	private Optional<Integer> targetYear = Optional.empty();
	
	public VdypPolygon(
			PolygonIdentifier vdypPolygonDescription, BecDefinition bec, Character fizId, float percentForestLand,
			Optional<Integer> inventoryTypeGroup, Optional<Integer> basalAreaGroup, Optional<PolygonMode> polygonMode
	) {
		this.description = vdypPolygonDescription;
		this.biogeoclimaticZone = bec;
		this.forestInventoryZone = fizId;

		// VDYPGETP lines 146-154
		if (percentForestLand <= 0.0)
			this.percentForestLand = 90.0f;
		else
			this.percentForestLand = percentForestLand;

		this.inventoryTypeGroup = inventoryTypeGroup;
		this.basalAreaGroup = basalAreaGroup;
		this.polygonMode = polygonMode;
	}

	@Override
	public String toString() {
		return description.toString();
	}

	public void setLayers(VdypPolygonLayer primaryLayer, VdypPolygonLayer veteranLayer) {
		if (primaryLayer != null) {
			this.primaryLayer = primaryLayer;
			this.layers.add(primaryLayer);
		}

		if (veteranLayer != null) {
			this.veteranLayer = Optional.of(veteranLayer);
			this.layers.add(veteranLayer);
		}
	}

	public Set<VdypPolygonLayer> getLayers() {
		return Collections.unmodifiableSet(layers);
	}

	public Optional<Integer> getTargetYear() {
		return targetYear;
	}

	public void setTargetYear(int targetYear) {
		this.targetYear = Optional.of(targetYear);
	}

	public PolygonIdentifier getDescription() {
		return description;
	}

	public String getName() {
		return description.getName();
	}

	public Integer getYear() {
		return description.getYear();
	}

	public BecDefinition getBiogeoclimaticZone() {
		return biogeoclimaticZone;
	}

	public Character getForestInventoryZone() {
		return forestInventoryZone;
	}

	public float getPercentForestLand() {
		return percentForestLand;
	}

	public Optional<Integer> getInventoryTypeGroup() {
		return inventoryTypeGroup;
	}

	public Optional<Integer> getBasalAreaGroup() {
		return basalAreaGroup;
	}

	public Optional<PolygonMode> getPolygonMode() {
		return polygonMode;
	}

	public VdypPolygonLayer getPrimaryLayer() {
		return primaryLayer;
	}

	public Optional<VdypPolygonLayer> getVeteranLayer() {
		return veteranLayer;
	}
}
