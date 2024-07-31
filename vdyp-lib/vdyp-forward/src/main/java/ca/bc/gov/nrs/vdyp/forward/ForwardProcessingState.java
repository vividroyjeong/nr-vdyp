package ca.bc.gov.nrs.vdyp.forward;

import java.text.MessageFormat;
import java.util.Map;

import ca.bc.gov.nrs.vdyp.application.ProcessingException;
import ca.bc.gov.nrs.vdyp.common.EstimationMethods;
import ca.bc.gov.nrs.vdyp.forward.controlmap.ForwardResolvedControlMap;
import ca.bc.gov.nrs.vdyp.forward.controlmap.ForwardResolvedControlMapImpl;
import ca.bc.gov.nrs.vdyp.model.BecDefinition;
import ca.bc.gov.nrs.vdyp.model.LayerType;
import ca.bc.gov.nrs.vdyp.model.UtilizationClass;
import ca.bc.gov.nrs.vdyp.model.VdypPolygon;

class ForwardProcessingState {

	/** An "instance" is the processing state of one polygon */
	private static final int LAYERS_PER_INSTANCE = 2;

	/** An "instance" is the processing state of one polygon */
	@SuppressWarnings("unused")
	private static final int MAX_INSTANCES = 3;

	/** The control map defining the context of the execution */
	final ForwardResolvedControlMap fcm;

	/** The estimators instance used by this engine */
	final EstimationMethods estimators;

	/** The storage banks */
	private final Bank[/* instances */][/* layers of instance */] banks;

	/** The active state */
	private PolygonProcessingState pps;

	public ForwardProcessingState(Map<String, Object> controlMap) {
		banks = new Bank[][] { new Bank[LAYERS_PER_INSTANCE], new Bank[LAYERS_PER_INSTANCE],
				new Bank[LAYERS_PER_INSTANCE] };

		this.fcm = new ForwardResolvedControlMapImpl(controlMap);
		this.estimators = new EstimationMethods(this.fcm);
	}

	private static final float MIN_BASAL_AREA = 0.001f;

	public void setPolygon(VdypPolygon polygon) throws ProcessingException {
		
		BecDefinition becZone = fcm.getBecLookup().get(polygon.getBiogeoclimaticZone()).orElseThrow(() ->
			new ProcessingException(MessageFormat.format("{0}: BEC zone {1} is unsupported", polygon.getPolygonIdentifier(),
					polygon.getBiogeoclimaticZone())));
		
		// Move the primary layer of the given polygon to bank zero.
		Bank primaryBank = banks[0][LayerType.PRIMARY.getIndex()] = new Bank(
				polygon.getLayers().get(LayerType.PRIMARY), becZone,
				s -> s.getBaseAreaByUtilization().get(UtilizationClass.ALL) >= MIN_BASAL_AREA);
		
		banks[0][LayerType.VETERAN.getIndex()] = new Bank(
				polygon.getLayers().get(LayerType.VETERAN), becZone,
				s -> s.getBaseAreaByUtilization().get(UtilizationClass.ALL) >= MIN_BASAL_AREA);
		
		pps = new PolygonProcessingState(this, polygon, primaryBank);
	}

	public PolygonProcessingState getPolygonProcessingState() {
		return pps;
	}

	public void storeActive(Bank end, int instanceNumber, LayerType layerType) {
		banks[instanceNumber][layerType.getIndex()] = end.copy();
	}

	public void transfer(int fromInstanceNumber, int toInstanceNumber, LayerType layerType) {
		banks[toInstanceNumber][layerType.getIndex()] = banks[fromInstanceNumber][layerType.getIndex()].copy();
	}

	public Bank getBank(int instanceNumber, LayerType layerType) {
		return banks[instanceNumber][layerType.getIndex()];
	}
}