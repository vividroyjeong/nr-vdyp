package ca.bc.gov.nrs.vdyp.common;

import static ca.bc.gov.nrs.vdyp.math.FloatMath.abs;
import static ca.bc.gov.nrs.vdyp.math.FloatMath.clamp;
import static ca.bc.gov.nrs.vdyp.math.FloatMath.exp;
import static ca.bc.gov.nrs.vdyp.math.FloatMath.log;
import static ca.bc.gov.nrs.vdyp.math.FloatMath.pow;
import static ca.bc.gov.nrs.vdyp.math.FloatMath.ratio;
import static ca.bc.gov.nrs.vdyp.math.FloatMath.sqrt;
import static java.lang.Math.max;
import static java.lang.Math.min;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.vdyp.application.ProcessingException;
import ca.bc.gov.nrs.vdyp.application.StandProcessingException;
import ca.bc.gov.nrs.vdyp.common_calculators.BaseAreaTreeDensityDiameter;
import ca.bc.gov.nrs.vdyp.controlmap.ResolvedControlMap;
import ca.bc.gov.nrs.vdyp.io.parse.coe.ModifierParser;
import ca.bc.gov.nrs.vdyp.math.FloatMath;
import ca.bc.gov.nrs.vdyp.model.BecDefinition;
import ca.bc.gov.nrs.vdyp.model.Coefficients;
import ca.bc.gov.nrs.vdyp.model.MatrixMap2;
import ca.bc.gov.nrs.vdyp.model.MatrixMap3;
import ca.bc.gov.nrs.vdyp.model.NonprimaryHLCoefficients;
import ca.bc.gov.nrs.vdyp.model.Region;
import ca.bc.gov.nrs.vdyp.model.UtilizationClass;
import ca.bc.gov.nrs.vdyp.model.VdypSpecies;

public class Estimators {

	public static final Logger log = LoggerFactory.getLogger(Estimators.class);

	public static final float EMPIRICAL_OCCUPANCY = 0.85f;

	private static final int UTIL_ALL = UtilizationClass.ALL.index;
	private static final int UTIL_LARGEST = UtilizationClass.OVER225.index;

	/**
	 * Returns the new value if the index is that of a utilization class that represents a size band, otherwise the old
	 * value
	 */
	public static final IndexedFloatBinaryOperator COPY_IF_BAND = (oldX, newX, i) -> i <= UTIL_ALL ? oldX : newX;

	/**
	 * Returns the new value if the index is that of a utilization class is not the total, otherwise the old value
	 */
	public static final IndexedFloatBinaryOperator COPY_IF_NOT_TOTAL = (oldX, newX, i) -> i < UTIL_ALL ? oldX : newX;

	ResolvedControlMap controlMap;

	public Estimators(ResolvedControlMap resolvedControlMap) {
		this.controlMap = resolvedControlMap;
	}

	private float heightMultiplier(
			String genus, Region region, float treesPerHectarePrimary
	) {
		final var coeMap = controlMap
				.<MatrixMap2<String, Region, Coefficients>>get(ControlKey.HL_PRIMARY_SP_EQN_P1, MatrixMap2.class);
		var coe = coeMap.get(genus, region).reindex(0);
		return coe.get(0) - coe.getCoe(1) + coe.getCoe(1) * exp(coe.getCoe(2) * (treesPerHectarePrimary - 100f));
	}

	/**
	 * EMP050 Method 1: Return the lorey height of the primary species based on the dominant height of the lead species.
	 *
	 * @param controlMap             Control map containing coefficients
	 * @param leadHeight             dominant height of the lead species
	 * @param genus                  Primary species
	 * @param region                 Region of the polygon
	 * @param treesPerHectarePrimary trees per hectare >7.5 cm of the primary species
	 * @return
	 */
	public float primaryHeightFromLeadHeight(
			float leadHeight, String genus, Region region, float treesPerHectarePrimary
	) {
		return 1.3f + (leadHeight - 1.3f) * heightMultiplier(genus, region, treesPerHectarePrimary);
	}

	/**
	 * EMP050 Method 2: Return the dominant height of the lead species based on the lorey height of the primary species.
	 *
	 * @param primaryHeight          lorey height of the primary species
	 * @param genus                  Primary species
	 * @param region                 Region of the polygon
	 * @param treesPerHectarePrimary trees per hectare >7.5 cm of the primary species
	 */
	public float leadHeightFromPrimaryHeight(
			float primaryHeight, String genus, Region region, float treesPerHectarePrimary
	) {
		return 1.3f + (primaryHeight - 1.3f) / heightMultiplier(genus, region, treesPerHectarePrimary);
	}

	/**
	 * EMP051 Return the lorey height of the primary species based on the dominant height of the lead species.
	 *
	 * @param leadHeight dominant height of the lead species
	 * @param genus      Primary species
	 * @param region     Region of the polygon
	 */
	public float primaryHeightFromLeadHeightInitial(float leadHeight, String genus, Region region) {
		final var coeMap = controlMap.<MatrixMap2<String, Region, Coefficients>>get(
				ControlKey.HL_PRIMARY_SP_EQN_P2, MatrixMap2.class
		);
		var coe = coeMap.get(genus, region);
		return 1.3f + coe.getCoe(1) * pow(leadHeight - 1.3f, coe.getCoe(2));
	}

	/**
	 * EMP053 Estimate the lorey height of a non-primary species of a primary layer.
	 * <p>
	 * Using eqns N1 and N2 from ipsjf124.doc
	 *
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
		var coeMap = controlMap.<MatrixMap3<String, String, Region, Optional<NonprimaryHLCoefficients>>>get(
				ControlKey.HL_NONPRIMARY, MatrixMap3.class
		);

		var coe = coeMap.get(vspec, vspecPrime, bec.getRegion()).orElseThrow(
				() -> new ProcessingException(
						String.format(
								"Could not find Lorey Height Nonprimary Coefficients for %s %s %s", vspec, vspecPrime, bec
										.getRegion()
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

		var coeMap = controlMap.<Map<String, Coefficients>>get(ControlKey.BY_SPECIES_DQ, Map.class);
		var specAliases = controlMap.getGenusDefinitionMap().getAliases();

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
		final float dqMaxSp = max(
				7.6f, min(limits.maxQuadMeanDiameter(), limits.maxDiameterHeight() * loreyHeightSpec)
		);
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

	public static record Limits(
			float maxLoreyHeight, float maxQuadMeanDiameter, float minDiameterHeight, float maxDiameterHeight
	) {
	};

	// EMP061
	public Limits getLimitsForHeightAndDiameter(String genus, Region region) {
		var coeMap = controlMap.<MatrixMap2<String, Region, Coefficients>>get(
				ControlKey.SPECIES_COMPONENT_SIZE_LIMIT, MatrixMap2.class
		);

		var coe = coeMap.get(genus, region);
		return new Limits(coe.getCoe(1), coe.getCoe(2), coe.getCoe(3), coe.getCoe(4));
	}

	/**
	 * EMP070. Estimate basal area by utilization class from the given parameters, after getting the estimation
	 * coefficients map from the control map.
	 *
	 * @param bec
	 * @param quadMeanDiameterUtil
	 * @param baseAreaUtil
	 * @param genus
	 * @throws ProcessingException
	 */
	public void estimateBaseAreaByUtilization(
			BecDefinition bec, Coefficients quadMeanDiameterUtil, Coefficients baseAreaUtil, String genus
	) throws ProcessingException {
		final var basalAreaUtilCompCoeMap = controlMap.<MatrixMap3<Integer, String, String, Coefficients>>get(
				ControlKey.UTIL_COMP_BA, MatrixMap3.class
		);

		float dq = quadMeanDiameterUtil.getCoe(UTIL_ALL);
		var b = Utils.utilizationVector();
		b.setCoe(0, baseAreaUtil.getCoe(UTIL_ALL));
		for (int i = 1; i < UTIL_LARGEST; i++) {
			var coe = basalAreaUtilCompCoeMap.get(i, genus, bec.getGrowthBec().getAlias());

			float a0 = coe.getCoe(1);
			float a1 = coe.getCoe(2);

			float logit;
			if (i == 1) {
				logit = a0 + a1 * pow(dq, 0.25f);
			} else {
				logit = a0 + a1 * dq;
			}
			b.setCoe(i, b.getCoe(i - 1) * exponentRatio(logit));
			if (i == 1 && quadMeanDiameterUtil.getCoe(UTIL_ALL) < 12.5f) {
				float ba12Max = (1f - pow(
						(quadMeanDiameterUtil.getCoe(1) - 7.4f) / (quadMeanDiameterUtil.getCoe(UTIL_ALL) - 7.4f), 2f
				)) * b.getCoe(0);
				b.scalarInPlace(1, x -> min(x, ba12Max));
			}
		}

		baseAreaUtil.setCoe(1, baseAreaUtil.getCoe(UTIL_ALL) - b.getCoe(1));
		baseAreaUtil.setCoe(2, b.getCoe(1) - b.getCoe(2));
		baseAreaUtil.setCoe(3, b.getCoe(2) - b.getCoe(3));
		baseAreaUtil.setCoe(4, b.getCoe(3));
	}

	/**
	 * EMP071. Estimate DQ by utilization class, see ipsjf120.doc.
	 *
	 * @param bec
	 * @param quadMeanDiameterUtil
	 * @param genus
	 * @throws ProcessingException
	 */
	public void
			estimateQuadMeanDiameterByUtilization(BecDefinition bec, Coefficients quadMeanDiameterUtil, String genus)
					throws ProcessingException {

		final var coeMap = controlMap.<MatrixMap3<Integer, String, String, Coefficients>>get(
				ControlKey.UTIL_COMP_DQ, MatrixMap3.class
		);

		log.atTrace().setMessage("Estimate DQ by utilization class for {} in BEC {}.  DQ for all >7.5 is {}")
				.addArgument(genus).addArgument(bec.getName()).addArgument(quadMeanDiameterUtil.getCoe(UTIL_ALL));

		float quadMeanDiameter07 = quadMeanDiameterUtil.getCoe(UTIL_ALL);

		for (var uc : UtilizationClass.UTIL_CLASSES) {
			log.atDebug().setMessage("For util level {}").addArgument(uc.className);
			var coe = coeMap.get(uc.index, genus, bec.getGrowthBec().getAlias());

			float a0 = coe.getCoe(1);
			float a1 = coe.getCoe(2);
			float a2 = coe.getCoe(3);

			log.atDebug().setMessage("a0={}, a1={}, a3={}").addArgument(a0).addArgument(a1).addArgument(a2);

			float logit;

			switch (uc) {
			case U75TO125:
				if (quadMeanDiameter07 < 7.5001f) {
					quadMeanDiameterUtil.setCoe(UTIL_ALL, 7.5f);
				} else {
					log.atDebug().setMessage("DQ = 7.5 + a0 * (1 - exp(a1 / a0*(DQ07 - 7.5) ))**a2' )");

					logit = a1 / a0 * (quadMeanDiameter07 - 7.5f);

					quadMeanDiameterUtil
							.setCoe(uc.index, min(7.5f + a0 * pow(1 - safeExponent(logit), a2), quadMeanDiameter07));
				}
				break;
			case U125TO175, U175TO225:
				log.atDebug().setMessage(
						"LOGIT = a0 + a1*(SQ07 / 7.5)**a2,  DQ = (12.5 or 17.5) + 5 * exp(LOGIT) / (1 + exp(LOGIT))"
				);
				logit = a0 + a1 * pow(quadMeanDiameter07 / 7.5f, a2);

				quadMeanDiameterUtil.setCoe(uc.index, uc.lowBound + 5f * exponentRatio(logit));
				break;
			case OVER225:
				float a3 = coe.getCoe(4);

				log.atDebug().setMessage(
						"Coeff A3 {}, LOGIT = a2 + a1*DQ07**a3,  DQ = DQ07 + a0 * (1 - exp(LOGIT) / (1 + exp(LOGIT)) )"
				);

				logit = a2 + a1 * pow(quadMeanDiameter07, a3);

				quadMeanDiameterUtil
						.setCoe(uc.index, max(22.5f, quadMeanDiameter07 + a0 * (1f - exponentRatio(logit))));
				break;
			case ALL, SMALL:
				throw new IllegalStateException(
						"Should not be attempting to process small component or all large components"
				);
			default:
				throw new IllegalStateException("Unknown utilization class " + uc);
			}

			log.atDebug().setMessage("Util DQ for class {} is {}").addArgument(uc.className)
					.addArgument(quadMeanDiameterUtil.getCoe(uc.index));
		}

		log.atTrace().setMessage("Estimated Diameters {}").addArgument(
				() -> UtilizationClass.UTIL_CLASSES.stream()
						.map(uc -> String.format("%s: %d", uc.className, quadMeanDiameterUtil.getCoe(uc.index)))
		);
	}

	/**
	 * EMP090. Return an estimate of the volume, per tree, of the whole stem, based on the given lorey height and quad
	 * mean diameter.
	 *
	 * @param volumeGroup      the species' volume group
	 * @param loreyHeight      the species' lorey height
	 * @param quadMeanDiameter the species' quadratic mean diameter
	 * @return as described
	 */
	public float estimateWholeStemVolumePerTree(int volumeGroup, float loreyHeight, float quadMeanDiameter) {
		var totalStandWholeStemVolumeCoeMap = controlMap.<Map<Integer, Coefficients>>get(
				ControlKey.TOTAL_STAND_WHOLE_STEM_VOL, Map.class
		);

		var coe = totalStandWholeStemVolumeCoeMap.get(volumeGroup).reindex(0);

		var logMeanVolume = //
				coe.getCoe(UtilizationClass.ALL.index) + //
						coe.getCoe(1) * log(quadMeanDiameter) + //
						coe.getCoe(2) * log(loreyHeight) + //
						coe.getCoe(3) * quadMeanDiameter + //
						coe.getCoe(4) / quadMeanDiameter + //
						coe.getCoe(5) * loreyHeight + //
						coe.getCoe(6) * quadMeanDiameter * quadMeanDiameter + //
						coe.getCoe(7) * loreyHeight * quadMeanDiameter + //
						coe.getCoe(8) * loreyHeight / quadMeanDiameter;

		return exp(logMeanVolume);
	}

	/**
	 * EMP091. Updates wholeStemVolumeUtil with estimated values, getting wholeStemUtilizationComponentMap from the
	 * given controlMap.
	 *
	 * @param controlMap
	 * @param utilizationClass
	 * @param adjustCloseUtil
	 * @param volumeGroup
	 * @param hlSp
	 * @param quadMeanDiameterUtil
	 * @param baseAreaUtil
	 * @param wholeStemVolumeUtil
	 * @throws ProcessingException
	 */
	public void estimateWholeStemVolume(
			UtilizationClass utilizationClass, float adjustCloseUtil, int volumeGroup,
			Float hlSp, Coefficients quadMeanDiameterUtil, Coefficients baseAreaUtil, Coefficients wholeStemVolumeUtil
	) throws ProcessingException {
		final var wholeStemUtilizationComponentMap = controlMap
				.<MatrixMap2<Integer, Integer, Optional<Coefficients>>>get(
						ControlKey.UTIL_COMP_WS_VOLUME, MatrixMap2.class
				);

		var dqSp = quadMeanDiameterUtil.getCoe(UTIL_ALL);

		estimateUtilization(baseAreaUtil, wholeStemVolumeUtil, utilizationClass, (uc, ba) -> {
			Coefficients wholeStemCoe = wholeStemUtilizationComponentMap.get(uc.index, volumeGroup).orElseThrow(
					() -> new ProcessingException(
							"Could not find whole stem utilization coefficients for group " + volumeGroup
					)
			);

			// Fortran code uses 1 index into array when reading it here, but 0 index when
			// writing into it in the parser. I use 0 for both.
			var a0 = wholeStemCoe.getCoe(0);
			var a1 = wholeStemCoe.getCoe(1);
			var a2 = wholeStemCoe.getCoe(2);
			var a3 = wholeStemCoe.getCoe(3);

			var arg = a0 + a1 * log(hlSp) + a2 * log(quadMeanDiameterUtil.getCoe(uc.index))
					+ ( (uc != UtilizationClass.OVER225) ? a3 * log(dqSp) : a3 * dqSp);

			if (uc == utilizationClass) {
				arg += adjustCloseUtil;
			}

			var vbaruc = exp(arg); // volume base area ?? utilization class?

			return ba * vbaruc;
		}, x -> x <= 0f, 0f);

		if (utilizationClass == UtilizationClass.ALL) {
			normalizeUtilizationComponents(wholeStemVolumeUtil);
		}
	}

	/**
	 * EMP092. Updates closeUtilizationVolumeUtil with estimated values.
	 *
	 * @param utilizationClass
	 * @param aAdjust
	 * @param volumeGroup
	 * @param hlSp
	 * @param quadMeanDiameterUtil
	 * @param wholeStemVolumeUtil
	 * @param closeUtilizationVolumeUtil
	 * @throws ProcessingException
	 */
	public void estimateCloseUtilizationVolume(
			UtilizationClass utilizationClass, Coefficients aAdjust, int volumeGroup, float hlSp,
			Coefficients quadMeanDiameterUtil, Coefficients wholeStemVolumeUtil, Coefficients closeUtilizationVolumeUtil
	) throws ProcessingException {
		final var closeUtilizationCoeMap = controlMap
				.<MatrixMap2<Integer, Integer, Optional<Coefficients>>>get(
						ControlKey.CLOSE_UTIL_VOLUME, MatrixMap2.class
				);
		
		estimateUtilization(wholeStemVolumeUtil, closeUtilizationVolumeUtil, utilizationClass, (uc, ws) -> {
			Coefficients closeUtilCoe = closeUtilizationCoeMap.get(uc.index, volumeGroup).orElseThrow(
					() -> new ProcessingException(
							"Could not find whole stem utilization coefficients for group " + volumeGroup
					)
			);
			var a0 = closeUtilCoe.getCoe(1);
			var a1 = closeUtilCoe.getCoe(2);
			var a2 = closeUtilCoe.getCoe(3);

			var arg = a0 + a1 * quadMeanDiameterUtil.getCoe(uc.index) + a2 * hlSp + aAdjust.getCoe(uc.index);

			float ratio = ratio(arg, 7.0f);

			return ws * ratio;
		});

		if (utilizationClass == UtilizationClass.ALL) {
			storeSumUtilizationComponents(closeUtilizationVolumeUtil);
		}
	}

	/**
	 * EMP093. Estimate volume NET OF DECAY by (DBH) utilization classes
	 *
	 * @param genus
	 * @param region
	 * @param utilizationClass
	 * @param aAdjust
	 * @param decayGroup
	 * @param ageBreastHeight
	 * @param quadMeanDiameterUtil
	 * @param closeUtilizationUtil
	 * @param closeUtilizationNetOfDecayUtil
	 * @throws ProcessingException
	 */
	public void estimateNetDecayVolume(
			String genus, Region region, UtilizationClass utilizationClass, Coefficients aAdjust, int decayGroup,
			float ageBreastHeight, Coefficients quadMeanDiameterUtil, Coefficients closeUtilizationUtil,
			Coefficients closeUtilizationNetOfDecayUtil
	) throws ProcessingException {
		final var netDecayCoeMap = controlMap.<MatrixMap2<Integer, Integer, Optional<Coefficients>>>get(
				ControlKey.VOLUME_NET_DECAY, MatrixMap2.class
		);
		final var decayModifierMap = controlMap.<MatrixMap2<String, Region, Float>>get(
				ModifierParser.CONTROL_KEY_MOD301_DECAY, MatrixMap2.class
		);

		var dqSp = quadMeanDiameterUtil.getCoe(UTIL_ALL);

		final var ageTr = (float) Math.log(Math.max(20.0, ageBreastHeight));

		estimateUtilization(closeUtilizationUtil, closeUtilizationNetOfDecayUtil, utilizationClass, (uc, cu) -> {
			Coefficients netDecayCoe = netDecayCoeMap.get(uc.index, decayGroup).orElseThrow(
					() -> new ProcessingException("Could not find net decay coefficients for group " + decayGroup)
			);
			var a0 = netDecayCoe.getCoe(1);
			var a1 = netDecayCoe.getCoe(2);
			var a2 = netDecayCoe.getCoe(3);

			float arg;
			if (uc != UtilizationClass.OVER225) {
				arg = a0 + a1 * log(dqSp) + a2 * ageTr;
			} else {
				arg = a0 + a1 * log(quadMeanDiameterUtil.getCoe(uc.index)) + a2 * ageTr;
			}

			arg += aAdjust.getCoe(uc.index) + decayModifierMap.get(genus, region);

			float ratio = ratio(arg, 8.0f);

			return cu * ratio;
		});

		if (utilizationClass == UtilizationClass.ALL) {
			storeSumUtilizationComponents(closeUtilizationNetOfDecayUtil);
		}
	}

	/**
	 * EMP094. Estimate utilization net of decay and waste
	 *
	 * @param region
	 * @param utilizationClass
	 * @param aAdjust
	 * @param genus
	 * @param loreyHeight
	 * @param quadMeanDiameterUtil
	 * @param closeUtilizationUtil
	 * @param closeUtilizationNetOfDecayUtil
	 * @param closeUtilizationNetOfDecayAndWasteUtil
	 * @throws ProcessingException
	 */
	public void estimateNetDecayAndWasteVolume(
			Region region, UtilizationClass utilizationClass, Coefficients aAdjust, String genus, float loreyHeight,
			Coefficients quadMeanDiameterUtil, Coefficients closeUtilizationUtil,
			Coefficients closeUtilizationNetOfDecayUtil, Coefficients closeUtilizationNetOfDecayAndWasteUtil
	) throws ProcessingException {
		final var netDecayWasteCoeMap = controlMap.<Map<String, Coefficients>>get(
				ControlKey.VOLUME_NET_DECAY_WASTE, Map.class
		);
		final var wasteModifierMap = controlMap.<MatrixMap2<String, Region, Float>>get(
				ControlKey.WASTE_MODIFIERS, MatrixMap2.class
		);
		
		estimateUtilization(
				closeUtilizationNetOfDecayUtil, closeUtilizationNetOfDecayAndWasteUtil, utilizationClass, (
						i, netDecay
				) -> {
					if (Float.isNaN(netDecay) || netDecay <= 0f) {
						return 0f;
					}

					Coefficients netWasteCoe = netDecayWasteCoeMap.get(genus);
					if (netWasteCoe == null) {
						throw new ProcessingException("Could not find net waste coefficients for genus " + genus);
					}

					var a0 = netWasteCoe.getCoe(0);
					var a1 = netWasteCoe.getCoe(1);
					var a2 = netWasteCoe.getCoe(2);
					var a3 = netWasteCoe.getCoe(3);
					var a4 = netWasteCoe.getCoe(4);
					var a5 = netWasteCoe.getCoe(5);

					if (i == UtilizationClass.OVER225) {
						a0 += a5;
					}
					var frd = 1.0f - netDecay / closeUtilizationUtil.getCoe(i.index);

					float arg = a0 + a1 * frd + a3 * log(quadMeanDiameterUtil.getCoe(i.index)) + a4 * log(loreyHeight);

					arg += wasteModifierMap.get(genus, region);

					arg = clamp(arg, -10f, 10f);

					var frw = (1.0f - exp(a2 * frd)) * exp(arg) / (1f + exp(arg)) * (1f - frd);
					frw = min(frd, frw);

					float result = closeUtilizationUtil.getCoe(i.index) * (1f - frd - frw);

					/*
					 * Check for an apply adjustments. This is done after computing the result above to allow for
					 * clamping frw to frd
					 */
					if (aAdjust.getCoe(i.index) != 0f) {
						var ratio = result / netDecay;
						if (ratio < 1f && ratio > 0f) {
							arg = log(ratio / (1f - ratio));
							arg += aAdjust.getCoe(i.index);
							arg = clamp(arg, -10f, 10f);
							result = exp(arg) / (1f + exp(arg)) * netDecay;
						}
					}

					return result;
				}
		);

		if (utilizationClass == UtilizationClass.ALL) {
			storeSumUtilizationComponents(closeUtilizationNetOfDecayAndWasteUtil);
		}
	}

	/**
	 * EMP095. Estimate utilization net of decay, waste, and breakage
	 *
	 * @param controlMap
	 * @param utilizationClass
	 * @param breakageGroup
	 * @param quadMeanDiameterUtil
	 * @param closeUtilizationUtil
	 * @param closeUtilizationNetOfDecayAndWasteUtil
	 * @param closeUtilizationNetOfDecayWasteAndBreakageUtil
	 * @throws ProcessingException
	 */
	public void estimateNetDecayWasteAndBreakageVolume(
			UtilizationClass utilizationClass, int breakageGroup, Coefficients quadMeanDiameterUtil,
			Coefficients closeUtilizationUtil, Coefficients closeUtilizationNetOfDecayAndWasteUtil,
			Coefficients closeUtilizationNetOfDecayWasteAndBreakageUtil
	) throws ProcessingException {
		final var netBreakageCoeMap = controlMap
				.<Map<Integer, Coefficients>>get(ControlKey.BREAKAGE, Map.class);

		final var coefficients = netBreakageCoeMap.get(breakageGroup);
		if (coefficients == null) {
			throw new ProcessingException("Could not find net breakage coefficients for group " + breakageGroup);
		}

		final var a1 = coefficients.getCoe(1);
		final var a2 = coefficients.getCoe(2);
		final var a3 = coefficients.getCoe(3);
		final var a4 = coefficients.getCoe(4);

		estimateUtilization(
				closeUtilizationNetOfDecayAndWasteUtil, closeUtilizationNetOfDecayWasteAndBreakageUtil, utilizationClass, (
						uc, netWaste
				) -> {

					if (netWaste <= 0f) {
						return 0f;
					}
					var percentBroken = a1 + a2 * log(quadMeanDiameterUtil.getCoe(uc.index));
					percentBroken = clamp(percentBroken, a3, a4);
					var broken = min(percentBroken / 100 * closeUtilizationUtil.getCoe(uc.index), netWaste);
					return netWaste - broken;
				}
		);

		if (utilizationClass == UtilizationClass.ALL) {
			storeSumUtilizationComponents(closeUtilizationNetOfDecayWasteAndBreakageUtil);
		}
	}

	@FunctionalInterface
	public static interface UtilizationProcessor {
		float apply(UtilizationClass utilizationClass, float inputValue) throws ProcessingException;
	}

	/**
	 * Estimate values for one utilization vector from another
	 *
	 * @param input            source utilization
	 * @param output           result utilization
	 * @param utilizationClass the utilization class for which to do the computation, UTIL_ALL for all of them.
	 * @param processor        Given a utilization class, and the source utilization for that class, return the result
	 *                         utilization
	 * @param skip             a utilization class will be skipped and the result set to the default value if this is
	 *                         true for the value of the source utilization
	 * @param defaultValue     the default value
	 * @throws ProcessingException
	 */
	private static void estimateUtilization(
			Coefficients input, Coefficients output, UtilizationClass utilizationClass, UtilizationProcessor processor,
			Predicate<Float> skip, float defaultValue
	) throws ProcessingException {
		for (var uc : UtilizationClass.UTIL_CLASSES) {
			var inputValue = input.getCoe(uc.index);

			// it seems like this should be done after checking i against utilizationClass,
			// which could just be done as part of the processor definition, but this is how
			// VDYP7 did it.
			if (skip.test(inputValue)) {
				output.setCoe(uc.index, defaultValue);
				continue;
			}

			if (utilizationClass != UtilizationClass.ALL && utilizationClass != uc) {
				continue;
			}

			var result = processor.apply(uc, inputValue);
			output.setCoe(uc.index, result);
		}
	}

	/**
	 * EMP106
	 * 
	 * @param estimateBaseAreaYieldCoefficients
	 * @param debugSetting2Value
	 * @param dominantHeight
	 * @param breastHeightAge
	 * @param baseAreaOverstory
	 * @param fullOccupancy
	 * @param bec
	 * @param baseAreaGroup
	 * @return
	 * @throws StandProcessingException
	 */
	public float estimateBaseAreaYield(
			Coefficients estimateBaseAreaYieldCoefficients, int debugSetting2Value, float dominantHeight, 
			float breastHeightAge, Optional<Float> baseAreaOverstory, boolean fullOccupancy, BecDefinition bec, 
			int baseAreaGroup
	) throws StandProcessingException {
		float upperBoundBasalArea = upperBoundsBaseArea(baseAreaGroup);

		/*
		 * The original Fortran had the following comment and a commented out modification to upperBoundsBaseArea
		 * (BATOP98). I have included them here.
		 */

		/*
		 * And one POSSIBLY one last vestage of grouping by ITG That limit applies to full occupancy and Empirical
		 * occupancy. They were derived as the 98th percentile of Empirical stocking, though adjusted PSP's were
		 * included. If the ouput of this routine is bumped up from empirical to full, MIGHT adjust this limit DOWN
		 * here, so that at end, it is correct. Tentatively decide NOT to do this.
		 */

		// if (fullOccupancy)
		// upperBoundsBaseArea *= EMPOC;

		float ageToUse = breastHeightAge;

		if (debugSetting2Value > 0) {
			// Cap ageToUse to at most 100 * debugSetting2Value
			ageToUse = Math.min(ageToUse, debugSetting2Value * 100.0f);
		}

		if (ageToUse <= 0f) {
			throw new StandProcessingException(MessageFormat.format("Age ({0}) was not positive", ageToUse));
		}

		float trAge = FloatMath.log(ageToUse);

		float a00 = Math.max(
				estimateBaseAreaYieldCoefficients.getCoe(0) + estimateBaseAreaYieldCoefficients.getCoe(1) * trAge, 0f
		);
		float ap = Math.max(
				estimateBaseAreaYieldCoefficients.getCoe(3) + estimateBaseAreaYieldCoefficients.getCoe(4) * trAge, 0f
		);

		float bap;
		if (dominantHeight <= estimateBaseAreaYieldCoefficients.getCoe(2)) {
			bap = 0f;
		} else {
			bap = a00 * FloatMath.pow(dominantHeight - estimateBaseAreaYieldCoefficients.getCoe(2), ap)
					* FloatMath.exp(
							estimateBaseAreaYieldCoefficients.getCoe(5) * dominantHeight
									+ estimateBaseAreaYieldCoefficients.getCoe(6) * baseAreaOverstory.orElse(0f)
					);
			bap = Math.min(bap, upperBoundBasalArea);
		}

		if (fullOccupancy)
			bap /= EMPIRICAL_OCCUPANCY;

		return bap;
	}

	/**
	 * EMP107
	 * 
	 * @param dominantHeight  Dominant height (m)
	 * @param breastHeightAge breast height age
	 * @param veteranBaseArea Basal area of overstory (>= 0)
	 * @param bec             BEC of the polygon
	 * @param baseAreaGroup   Index of the base area group
	 * @return DQ of primary layer (w DBH >= 7.5)
	 * @throws StandProcessingException
	 */
	public float estimateQuadMeanDiameterYield(
			Coefficients coefficientsWeightedBySpeciesAndDecayBec, float dominantHeight, float breastHeightAge,
			Optional<Float> veteranBaseArea, BecDefinition bec, int baseAreaGroup
	) throws StandProcessingException {

		// TODO handle getDebugMode(2) case
		final float ageUse = breastHeightAge;

		final float upperBoundsQuadMeanDiameter = upperBoundsQuadMeanDiameter(baseAreaGroup);

		if (ageUse <= 0f) {
			throw new StandProcessingException("Primary breast height age must be positive but was " + ageUse);
		}

		final float trAge = FloatMath.log(ageUse);

		final float c0 = coefficientsWeightedBySpeciesAndDecayBec.getCoe(0);
		final float c1 = Math.max(
				coefficientsWeightedBySpeciesAndDecayBec.getCoe(1)
						+ coefficientsWeightedBySpeciesAndDecayBec.getCoe(2) * trAge, 0f
		);
		final float c2 = Math.max(
				coefficientsWeightedBySpeciesAndDecayBec.getCoe(3)
						+ coefficientsWeightedBySpeciesAndDecayBec.getCoe(4) * trAge, 0f
		);

		return FloatMath.clamp(c0 + c1 * FloatMath.pow(dominantHeight - 5f, c2), 7.6f, upperBoundsQuadMeanDiameter);

	}

	// UPPERGEN Method 1
	Coefficients upperBounds(int baseAreaGroup) {
		var upperBoundsMap = controlMap
				.<Map<Integer, Coefficients>>get(ControlKey.BA_DQ_UPPER_BOUNDS, Map.class);
		return Utils.<Coefficients>optSafe(upperBoundsMap.get(baseAreaGroup)).orElseThrow(
				() -> new IllegalStateException("Could not find limits for base area group " + baseAreaGroup)
		);
	}

	private float upperBoundsBaseArea(int baseAreaGroup) {
		return upperBounds(baseAreaGroup).getCoe(1);
	}

	private float upperBoundsQuadMeanDiameter(int baseAreaGroup) {
		return upperBounds(baseAreaGroup).getCoe(2);
	}

	/**
	 * Estimate values for one utilization vector from another
	 *
	 * @param input            source utilization
	 * @param output           result utilization
	 * @param utilizationClass the utilization class for which to do the computation, UTIL_ALL for all of them.
	 * @param processor        Given a utilization class, and the source utilization for that class, return the result
	 *                         utilization
	 * @throws ProcessingException
	 */
	private static void estimateUtilization(
			Coefficients input, Coefficients output, UtilizationClass utilizationClass, UtilizationProcessor processor
	) throws ProcessingException {
		estimateUtilization(input, output, utilizationClass, processor, x -> false, 0f);
	}

	private static float exponentRatio(float logit) throws ProcessingException {
		float exp = safeExponent(logit);
		return exp / (1f + exp);
	}

	private static float safeExponent(float logit) throws ProcessingException {
		if (logit > 88f) {
			throw new ProcessingException("logit " + logit + " exceeds 88");
		}
		return exp(logit);
	}

	/**
	 * Normalizes the utilization components 1-4 so they sum to the value of component UTIL_ALL
	 *
	 * @throws ProcessingException if the sum is not positive
	 */
	private static float normalizeUtilizationComponents(Coefficients components) throws ProcessingException {
		var sum = sumUtilizationComponents(components);
		var k = components.getCoe(UTIL_ALL) / sum;
		if (sum <= 0f) {
			throw new ProcessingException("Total volume " + sum + " was not positive.");
		}
		UtilizationClass.UTIL_CLASSES.forEach(uc -> components.setCoe(uc.index, components.getCoe(uc.index) * k));
		return k;
	}

	/**
	 * Sums the individual utilization components (1-4)
	 */
	private static float sumUtilizationComponents(Coefficients components) {
		return (float) UtilizationClass.UTIL_CLASSES.stream().mapToInt(x -> x.index).mapToDouble(components::getCoe)
				.sum();
	}

	/**
	 * Sums the individual utilization components (1-4) and stores the results in coefficient UTIL_ALL
	 */
	private static float storeSumUtilizationComponents(Coefficients components) {
		var sum = sumUtilizationComponents(components);
		components.setCoe(UtilizationClass.ALL.index, sum);
		return sum;
	}
}
