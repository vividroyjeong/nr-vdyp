package ca.bc.gov.nrs.vdyp.forward;

import java.util.Map;

import ca.bc.gov.nrs.vdyp.common.Estimators;
import ca.bc.gov.nrs.vdyp.forward.controlmap.ForwardResolvedControlMap;
import ca.bc.gov.nrs.vdyp.forward.controlmap.ForwardResolvedControlMapImpl;
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
	final Estimators estimators;

	/** The storage banks */
	private final Bank[/* instances */][/* layers of instance */] banks;

	/** The active state */
	private PolygonProcessingState pps;

	public ForwardProcessingState(Map<String, Object> controlMap) {
		banks = new Bank[][] { new Bank[LAYERS_PER_INSTANCE], new Bank[LAYERS_PER_INSTANCE],
				new Bank[LAYERS_PER_INSTANCE] };

		this.fcm = new ForwardResolvedControlMapImpl(controlMap);
		this.estimators = new Estimators(this.fcm);
	}

	private static final float MIN_BASAL_AREA = 0.001f;

	public void setPolygon(VdypPolygon polygon) {
		
		// Move the primary layer of the given polygon to bank zero.
		Bank primaryBank = banks[0][LayerType.PRIMARY.getIndex()] = new Bank(
				polygon.getLayers().get(LayerType.PRIMARY), polygon.getBiogeoclimaticZone(),
				s -> s.getUtilizations().isPresent()
						? s.getUtilizations().get().get(UtilizationClass.ALL).getBasalArea() >= MIN_BASAL_AREA
						: true
		);
		
		polygon.getLayers().get(LayerType.VETERAN).ifPresent(l ->
			banks[0][LayerType.VETERAN.getIndex()] = new Bank(
					l, polygon.getBiogeoclimaticZone(),
					s -> s.getUtilizations().isPresent()
							? s.getUtilizations().get().get(UtilizationClass.ALL).getBasalArea() >= MIN_BASAL_AREA
							: true));
		
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