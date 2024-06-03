package ca.bc.gov.nrs.vdyp.common_calculators;

import static ca.bc.gov.nrs.vdyp.math.FloatMath.exp;
import static ca.bc.gov.nrs.vdyp.math.FloatMath.pow;

import java.util.Map;
import java.util.Optional;

import ca.bc.gov.nrs.vdyp.application.ProcessingException;
import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.common.Utils;
import ca.bc.gov.nrs.vdyp.model.BaseVdypSpecies;
import ca.bc.gov.nrs.vdyp.model.BecDefinition;
import ca.bc.gov.nrs.vdyp.model.Coefficients;
import ca.bc.gov.nrs.vdyp.model.MatrixMap2;
import ca.bc.gov.nrs.vdyp.model.MatrixMap3;
import ca.bc.gov.nrs.vdyp.model.NonprimaryHLCoefficients;
import ca.bc.gov.nrs.vdyp.model.Region;
import ca.bc.gov.nrs.vdyp.model.VdypSpecies;

/**
 * EMP### functions from VDYP 7
 */
public class EMP {

	Map<String, Object> controlMap;

	public EMP(Map<String, Object> controlMap) {
		super();
		this.controlMap = controlMap;
	}

	private float heightMultiplier(String genus, Region region, float treesPerHectarePrimary) {
		final var coeMap = Utils.<MatrixMap2<String, Region, Coefficients>>expectParsedControl(
				controlMap, ControlKey.HL_PRIMARY_SP_EQN_P1, MatrixMap2.class
		);
		var coe = coeMap.get(genus, region).reindex(0);
		return coe.get(0) - coe.getCoe(1) + coe.getCoe(1) * exp(coe.getCoe(2) * (treesPerHectarePrimary - 100f));
	}

	// EMP050 Meth==1
	/**
	 * Return the lorey height of the primary species based on the dominant height of the lead species.
	 *
	 * @param leadHeight             dominant height of the lead species
	 * @param genus                  Primary species
	 * @param region                 Region of the polygon
	 * @param treesPerHectarePrimary trees per hectare >7.5 cm of the primary species
	 * @return
	 */
	public float
			primaryHeightFromLeadHeight(float leadHeight, String genus, Region region, float treesPerHectarePrimary) {
		return 1.3f + (leadHeight - 1.3f) * heightMultiplier(genus, region, treesPerHectarePrimary);
	}

	// EMP050 Meth==2
	/**
	 * Return the dominant height of the lead species based on the lorey height of the primary species.
	 *
	 * @param primaryHeight          lorey height of the primary species
	 * @param genus                  Primary species
	 * @param region                 Region of the polygon
	 * @param treesPerHectarePrimary trees per hectare >7.5 cm of the primary species
	 * @return
	 */
	public float leadHeightFromPrimaryHeight(
			float primaryHeight, String genus, Region region, float treesPerHectarePrimary
	) {
		return 1.3f + (primaryHeight - 1.3f) / heightMultiplier(genus, region, treesPerHectarePrimary);
	}

	// EMP051
	/**
	 * Return the lorey height of the primary species based on the dominant height of the lead species.
	 *
	 * @param leadHeight dominant height of the lead species
	 * @param genus      Primary species
	 * @param region     Region of the polygon
	 * @return
	 */
	public float primaryHeightFromLeadHeightInitial(float leadHeight, String genus, Region region) {
		final var coeMap = Utils.<MatrixMap2<String, Region, Coefficients>>expectParsedControl(
				controlMap, ControlKey.HL_PRIMARY_SP_EQN_P2, MatrixMap2.class
		);
		var coe = coeMap.get(genus, region);
		return 1.3f + coe.getCoe(1) * pow(leadHeight - 1.3f, coe.getCoe(2));
	}

	// EMP053 Using eqns N1 and N2 from ipsjf124.doc
	/**
	 * Estimate the lorey height of a non-primary species of a primary layer.
	 *
	 * @param vspec         The species.
	 * @param vspecPrime    The primary species.
	 * @param leadHeight    lead height of the layer
	 * @param primaryHeight height of the primary species
	 * @throws ProcessingException
	 */
	public float estimateNonPrimaryLoreyHeight(
			BaseVdypSpecies vspec, BaseVdypSpecies vspecPrime, BecDefinition bec, float leadHeight, float primaryHeight
	) throws ProcessingException {
		return estimateNonPrimaryLoreyHeight(vspec.getGenus(), vspecPrime.getGenus(), bec, leadHeight, primaryHeight);
	}

	// EMP053 Using eqns N1 and N2 from ipsjf124.doc
	/**
	 * Estimate the lorey height of a non-primary species of a primary layer.
	 *
	 * @param vspec         The species.
	 * @param vspecPrime    The primary species.
	 * @param leadHeight    lead height of the layer
	 * @param primaryHeight height of the primary species
	 * @throws ProcessingException
	 */
	public float estimateNonPrimaryLoreyHeight(
			String vspec, String vspecPrime, BecDefinition bec, float leadHeight, float primaryHeight
	) throws ProcessingException {
		var coeMap = Utils.<MatrixMap3<String, String, Region, Optional<NonprimaryHLCoefficients>>>expectParsedControl(
				controlMap, ControlKey.HL_NONPRIMARY, MatrixMap3.class
		);

		var coe = coeMap.get(vspec, vspecPrime, bec.getRegion()).orElseThrow(
				() -> new ProcessingException(
						String.format(
								"Could not find Lorey Height Nonprimary Coefficients for %s %s %s", vspec, vspecPrime,
								bec.getRegion()
						)
				)
		);
		var heightToUse = coe.getEquationIndex() == 1 ? leadHeight : primaryHeight;
		return 1.3f + coe.getCoe(1) * pow(heightToUse - 1.3f, coe.getCoe(2));
	}
	
	public static record Limits (float maxLoreyHeight, float maxQuadMeanDiameter, float minDiameterHeight, float maxDiameterHeight) {};
	
	// EMP061
	public Limits getLimitsForHeightAndDiameter(String genus, Region region) {
		var coeMap = Utils.<MatrixMap2<String, Region, Coefficients>>expectParsedControl(
				controlMap, ControlKey.SPECIES_COMPONENT_SIZE_LIMIT, MatrixMap2.class
		);

		var coe = coeMap.get(genus, region);
		return new Limits(coe.getCoe(1), coe.getCoe(2), coe.getCoe(3), coe.getCoe(4));
	}

}
