package ca.bc.gov.nrs.vdyp.common_calculators;

import static ca.bc.gov.nrs.vdyp.math.FloatMath.abs;
import static ca.bc.gov.nrs.vdyp.math.FloatMath.clamp;
import static ca.bc.gov.nrs.vdyp.math.FloatMath.exp;
import static ca.bc.gov.nrs.vdyp.math.FloatMath.log;
import static ca.bc.gov.nrs.vdyp.math.FloatMath.pow;
import static ca.bc.gov.nrs.vdyp.math.FloatMath.sqrt;
import static java.lang.Math.max;
import static java.lang.Math.min;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import ca.bc.gov.nrs.vdyp.application.ProcessingException;
import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.common.Utils;
import ca.bc.gov.nrs.vdyp.io.parse.coe.GenusDefinitionParser;
import ca.bc.gov.nrs.vdyp.math.FloatMath;
import ca.bc.gov.nrs.vdyp.model.BaseVdypSpecies;
import ca.bc.gov.nrs.vdyp.model.BecDefinition;
import ca.bc.gov.nrs.vdyp.model.Coefficients;
import ca.bc.gov.nrs.vdyp.model.MatrixMap2;
import ca.bc.gov.nrs.vdyp.model.MatrixMap3;
import ca.bc.gov.nrs.vdyp.model.NonprimaryHLCoefficients;
import ca.bc.gov.nrs.vdyp.model.Region;
import ca.bc.gov.nrs.vdyp.model.UtilizationClass;
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
	
	// EMP060
	/**
	 * Estimate DQ for a species (primary or not). Using eqn in jf125.doc.
	 *
	 * Enforces mins and maxes from EMP061.
	 *
	 * @param spec                  Species of insterest
	 * @param allSpecies            Collection of all species on the layer
	 * @param region                BEC Region of the stand
	 * @param standQuadMeanDiameter Quadratic mean diameter of the stand
	 * @param standBaseArea         Base area of the stand
	 * @param standTreesPerHectare  Density opf the stand
	 * @param standLoreyHeight      Lorey height of the stand
	 * @return Quadratic mean diameter of the species of interest
	 * @throws ProcessingException
	 */
	public float estimateQuadMeanDiameterForSpecies(
			VdypSpecies spec, // ISP, HLsp, DQsp
			Map<String, VdypSpecies> allSpecies, // FR
			Region region, // INDEX_IC
			float standQuadMeanDiameter, // DQ_TOT
			float standBaseArea, // BA_TOT
			float standTreesPerHectare, // TPH_TOT
			float standLoreyHeight // HL_TOT
	) throws ProcessingException {
		String species = spec.getGenus();

		float c = 0.00441786467f;

		float minQuadMeanDiameter = min(7.6f, standQuadMeanDiameter);

		// Quick solution
		if (spec.getFractionGenus() >= 1f || standQuadMeanDiameter < minQuadMeanDiameter) {
			return standQuadMeanDiameter;
		}

		var coeMap = Utils
				.<Map<String, Coefficients>>expectParsedControl(controlMap, ControlKey.BY_SPECIES_DQ, Map.class);
		var specAliases = GenusDefinitionParser.getSpeciesAliases(controlMap);

		// TODO we can probably remove these as they seem to only be used for debugging
		// in VDYP7
		Map<String, Float> adjust = new HashMap<>(coeMap.size());
		Map<String, Float> mult = new HashMap<>(coeMap.size());

		var specIt = specAliases.iterator();

		var spec1 = specIt.next();

		float a2 = coeMap.get(spec1).getCoe(2);

		float fractionOther = 1f - spec.getFractionGenus(); // FR_REST

		mult.put(spec1, 1f);
		float a0 = coeMap.get(spec1).getCoe(0);
		float a1 = coeMap.get(spec1).getCoe(1);

		while (specIt.hasNext()) {
			var specIAlias = specIt.next();
			var specI = allSpecies.get(specIAlias);
			if (specIAlias.equals(spec.getGenus())) {
				float multI = 1f;
				mult.put(specIAlias, multI);
				a0 += multI * coeMap.get(specIAlias).getCoe(0);
				a1 += multI * coeMap.get(specIAlias).getCoe(1);
			} else {
				if (specI != null && specI.getFractionGenus() > 0f) {
					float multI = -specI.getFractionGenus() / fractionOther;
					mult.put(specIAlias, multI);
					a0 += multI * coeMap.get(specIAlias).getCoe(0);
					a1 -= multI * coeMap.get(specIAlias).getCoe(1);
				}
			}
		}

		float loreyHeightSpec = spec.getLoreyHeightByUtilization().getCoe(UtilizationClass.ALL.index);
		float loreyHeight1 = max(4f, loreyHeightSpec);
		float loreyHeight2 = (standLoreyHeight - loreyHeightSpec * spec.getFractionGenus()) / fractionOther;
		float loreyHeightRatio = clamp( (loreyHeight1 - 3f) / (loreyHeight2 - 3f), 0.05f, 20f);

		float r = exp(
				a0 + a1 * log(loreyHeightRatio) + a2 * log(standQuadMeanDiameter) + adjust.getOrDefault(species, 0f)
		);

		float baseArea1 = spec.getFractionGenus() * standBaseArea;
		float baseArea2 = standBaseArea - baseArea1;

		float treesPerHectare1;
		if (abs(r - 1f) < 0.0005) {
			treesPerHectare1 = spec.getFractionGenus() * standTreesPerHectare;
		} else {
			float aa = (r - 1f) * c;
			float bb = c * (1f - r) * standTreesPerHectare + baseArea1 + baseArea2 * r;
			float cc = -baseArea1 * standTreesPerHectare;
			float term = bb * bb - 4 * aa * cc;
			if (term <= 0f) {
				throw new ProcessingException(
						"Term for trees per hectare calculation when estimating quadratic mean diameter for species "
								+ species + " was " + term + " but should be positive."
				);
			}
			treesPerHectare1 = (-bb + sqrt(term)) / (2f * aa);
			if (treesPerHectare1 <= 0f || treesPerHectare1 > standTreesPerHectare) {
				throw new ProcessingException(
						"Trees per hectare 1 for species " + species + " was " + treesPerHectare1
								+ " but should be positive and less than or equal to stand trees per hectare "
								+ standTreesPerHectare
				);
			}
		}

		float quadMeanDiameter1 = BaseAreaTreeDensityDiameter.quadMeanDiameter(baseArea1, treesPerHectare1);
		float treesPerHectare2 = standTreesPerHectare - treesPerHectare1;
		float quadMeanDiameter2 = BaseAreaTreeDensityDiameter.quadMeanDiameter(baseArea2, treesPerHectare2);

		if (quadMeanDiameter2 < minQuadMeanDiameter) {
			// species 2 is too small. Make target species smaller.
			quadMeanDiameter2 = minQuadMeanDiameter;
			treesPerHectare2 = BaseAreaTreeDensityDiameter.treesPerHectare(baseArea2, quadMeanDiameter2);
			treesPerHectare1 = standTreesPerHectare - treesPerHectare2;
			quadMeanDiameter1 = BaseAreaTreeDensityDiameter.quadMeanDiameter(baseArea1, treesPerHectare1);
		}
		var limits = getLimitsForHeightAndDiameter(species, region);

		final float dqMinSp = max(minQuadMeanDiameter, limits.minDiameterHeight() * loreyHeightSpec);
		final float dqMaxSp = max(7.6f, min(limits.maxQuadMeanDiameter(), limits.maxDiameterHeight() * loreyHeightSpec));
		if (quadMeanDiameter1 < dqMinSp) {
			quadMeanDiameter1 = dqMinSp;
			treesPerHectare1 = BaseAreaTreeDensityDiameter.treesPerHectare(baseArea1, quadMeanDiameter1);
			treesPerHectare2 = standTreesPerHectare - treesPerHectare2;
			quadMeanDiameter2 = BaseAreaTreeDensityDiameter.quadMeanDiameter(baseArea2, treesPerHectare2);
		}
		if (quadMeanDiameter1 > dqMaxSp) {
			// target species is too big. Make target species smaller, DQ2 bigger.

			quadMeanDiameter1 = dqMaxSp;
			treesPerHectare1 = BaseAreaTreeDensityDiameter.treesPerHectare(baseArea1, quadMeanDiameter1);
			treesPerHectare2 = standTreesPerHectare - treesPerHectare2;

			if (treesPerHectare2 > 0f && baseArea2 > 0f) {
				quadMeanDiameter2 = BaseAreaTreeDensityDiameter.quadMeanDiameter(baseArea2, treesPerHectare2);
			} else {
				quadMeanDiameter2 = 1000f;
			}

			// under rare circumstances, let DQ1 exceed DQMAXsp
			if (quadMeanDiameter2 < minQuadMeanDiameter) {
				quadMeanDiameter2 = minQuadMeanDiameter;
				treesPerHectare2 = BaseAreaTreeDensityDiameter.treesPerHectare(baseArea2, quadMeanDiameter2);
				treesPerHectare1 = standTreesPerHectare - treesPerHectare2;
				quadMeanDiameter1 = BaseAreaTreeDensityDiameter.quadMeanDiameter(baseArea1, treesPerHectare1);
			}

		}
		return quadMeanDiameter1;
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
