package ca.bc.gov.nrs.vdyp.forward;

import java.text.MessageFormat;
import java.util.Optional;

import ca.bc.gov.nrs.vdyp.forward.model.VdypPolygon;
import ca.bc.gov.nrs.vdyp.model.LayerType;

class ForwardProcessingState {

	/** An "instance" is the processing state of one polygon */
	private static final int MAX_INSTANCES = 3;

	/**
	 * We allocate one record for the Primary Layer and one record for the Veteran Layer of each instance.
	 */
	private static final int MAX_RECORDS = 2 * MAX_INSTANCES;

	private final PolygonProcessingState[] banks;

	private Optional<VdypPolygon> polygon;
	private PolygonProcessingState activeBank;

	public ForwardProcessingState() {
		polygon = Optional.empty();
		banks = new PolygonProcessingState[MAX_RECORDS];
	}

	public void setStartingState(VdypPolygon polygon) {
		this.polygon = Optional.of(polygon);
		
		// Move the primary layer of the given polygon to bank zero.
		banks[0] = new PolygonProcessingState(polygon.getPrimaryLayer(), polygon.getBiogeoclimaticZone());
	}

	public VdypPolygon getPolygon() {
		return polygon.orElseThrow();
	}

	public void setActive(LayerType layerType, int instanceNumber) {
		activeBank = banks[toIndex(layerType, instanceNumber)].copy();
	}

	public void storeActive(LayerType layerType, int instanceNumber) {
		banks[toIndex(layerType, instanceNumber)] = activeBank.copy();
	}

	public void transfer(LayerType layerType, int fromInstanceNumber, int toInstanceNumber) {
		banks[toIndex(layerType, toInstanceNumber)] = banks[toIndex(layerType, fromInstanceNumber)].copy();
	}

	public PolygonProcessingState getActive() {
		return activeBank;
	}

	public PolygonProcessingState getBank(LayerType layerType, int instanceNumber) {
		return banks[toIndex(layerType, instanceNumber)];
	}

	private static int toIndex(LayerType layerType, int instanceNumber) {
		return toLayerIndex(layerType) * MAX_INSTANCES + instanceNumber;
	}

	private static int toLayerIndex(LayerType layerType) {
		switch (layerType) {
		case PRIMARY:
			return 0;
		case VETERAN:
			return 1;
		default:
			throw new IllegalStateException(MessageFormat.format("Unsupported LayerType {0}", layerType));
		}
	}
}