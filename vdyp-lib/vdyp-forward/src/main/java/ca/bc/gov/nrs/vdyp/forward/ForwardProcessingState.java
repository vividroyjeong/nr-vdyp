package ca.bc.gov.nrs.vdyp.forward;

import java.util.Map;

import ca.bc.gov.nrs.vdyp.application.ProcessingException;
import ca.bc.gov.nrs.vdyp.application.VdypApplicationIdentifier;
import ca.bc.gov.nrs.vdyp.common.ComputationMethods;
import ca.bc.gov.nrs.vdyp.common.EstimationMethods;
import ca.bc.gov.nrs.vdyp.forward.controlmap.ForwardResolvedControlMap;
import ca.bc.gov.nrs.vdyp.forward.controlmap.ForwardResolvedControlMapImpl;
import ca.bc.gov.nrs.vdyp.model.LayerType;
import ca.bc.gov.nrs.vdyp.model.VdypPolygon;

class ForwardProcessingState {

	/** The control map defining the context of the execution */
	final ForwardResolvedControlMap fcm;

	/** The estimators instance used by this engine */
	final EstimationMethods estimators;

	/** The compuation instance used by this engine */
	final ComputationMethods computers;

	/** The active state */
	private LayerProcessingState pps;

	public ForwardProcessingState(Map<String, Object> controlMap) {
		this.fcm = new ForwardResolvedControlMapImpl(controlMap);
		this.estimators = new EstimationMethods(this.fcm);
		this.computers = new ComputationMethods(estimators, VdypApplicationIdentifier.VDYP_FORWARD);
	}

	public void setPolygonLayer(VdypPolygon polygon, LayerType subjectLayer) throws ProcessingException {
		
		pps = new LayerProcessingState(this, polygon, subjectLayer);
	}

	public LayerProcessingState getLayerProcessingState() {
		return pps;
	}
}