package ca.bc.gov.nrs.vdyp.forward;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.common.GenusDefinitionMap;
import ca.bc.gov.nrs.vdyp.common.Utils;
import ca.bc.gov.nrs.vdyp.common_calculators.enumerations.SiteIndexEquation;
import ca.bc.gov.nrs.vdyp.forward.model.VdypGrowthDetails;
import ca.bc.gov.nrs.vdyp.forward.model.VdypPolygon;
import ca.bc.gov.nrs.vdyp.model.BecLookup;
import ca.bc.gov.nrs.vdyp.model.Coefficients;
import ca.bc.gov.nrs.vdyp.model.CompVarAdjustments;
import ca.bc.gov.nrs.vdyp.model.GenusDefinition;
import ca.bc.gov.nrs.vdyp.model.LayerType;
import ca.bc.gov.nrs.vdyp.model.MatrixMap2;
import ca.bc.gov.nrs.vdyp.model.Region;

class ForwardProcessingState {

	/** An "instance" is the processing state of one polygon */
	private static final int MAX_INSTANCES = 3;

	/**
	 * We allocate one record for the Primary Layer and one record for the Veteran Layer of each instance.
	 */
	private static final int MAX_RECORDS = 2 * MAX_INSTANCES;

	/** The control map defining the context of the execution */
	private final Map<String, Object> controlMap;
	/** The genus definition map, extracted from the controlMap at construction time */
	private final GenusDefinitionMap genusDefinitionMap;

	/** The storage banks */
	private final Bank[] banks;

	/** Polygon on which the processor is operating */
	private Optional<VdypPolygon> polygon;

	/** The active state */
	private PolygonProcessingState processingState;
	
	// Polygon-independent COMMON block information
	
	// V7COE028
	private CompVarAdjustments compVarAdjustments; // COE028

	// VTROL
	private VdypGrowthDetails vdypGrowthDetails; // IVTROL
	
	// VDEBUG - NDEBUG
	// TODO

	public ForwardProcessingState(Map<String, Object> controlMap) {
		this.controlMap = controlMap;

		polygon = Optional.empty();
		banks = new Bank[MAX_RECORDS];

		List<GenusDefinition> genusDefinitions = Utils.expectParsedControl(controlMap, ControlKey.SP0_DEF, List.class);
		genusDefinitionMap = new GenusDefinitionMap(genusDefinitions);
		compVarAdjustments = Utils.expectParsedControl(controlMap, ControlKey.PARAM_ADJUSTMENTS, CompVarAdjustments.class);
		vdypGrowthDetails = Utils.expectParsedControl(controlMap, ControlKey.VTROL, VdypGrowthDetails.class);
	}

	public GenusDefinitionMap getGenusDefinitionMap() {
		return genusDefinitionMap;
	}
	
	public BecLookup getBecLookup() {
		return Utils.expectParsedControl(controlMap, ControlKey.BEC_DEF, BecLookup.class);
	}
	
	public MatrixMap2<String, Region, SiteIndexEquation> getSiteCurveMap() {
		return Utils.expectParsedControl(controlMap, ControlKey.SITE_CURVE_NUMBERS, MatrixMap2.class);
	}
	
	public MatrixMap2<String, Region, Coefficients> getHl1Coefficients() {
		return Utils.expectParsedControl(controlMap, ControlKey.HL_PRIMARY_SP_EQN_P1, MatrixMap2.class);	
	}

	public void setStartingState(VdypPolygon polygon) {
		this.polygon = Optional.of(polygon);

		// Move the primary layer of the given polygon to bank zero.
		banks[0] = new Bank(polygon.getPrimaryLayer(), polygon.getBiogeoclimaticZone());
		setActive(LayerType.PRIMARY, 0);
	}

	public VdypPolygon getPolygon() {
		return polygon.orElseThrow();
	}

	private void setActive(LayerType layerType, int instanceNumber) {
		processingState = new PolygonProcessingState(banks[toIndex(layerType, instanceNumber)]);
	}

	public void storeActive(LayerType layerType, int instanceNumber) {
		banks[toIndex(layerType, instanceNumber)] = processingState.wallet.copy();
	}

	public void transfer(LayerType layerType, int fromInstanceNumber, int toInstanceNumber) {
		banks[toIndex(layerType, toInstanceNumber)] = banks[toIndex(layerType, fromInstanceNumber)].copy();
	}

	public PolygonProcessingState getActive() {
		return processingState;
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