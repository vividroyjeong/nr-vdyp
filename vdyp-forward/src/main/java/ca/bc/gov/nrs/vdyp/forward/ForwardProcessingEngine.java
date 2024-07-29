package ca.bc.gov.nrs.vdyp.forward;

import static ca.bc.gov.nrs.vdyp.math.FloatMath.clamp;
import static ca.bc.gov.nrs.vdyp.math.FloatMath.exp;
import static ca.bc.gov.nrs.vdyp.math.FloatMath.log;
import static ca.bc.gov.nrs.vdyp.math.FloatMath.pow;
import static java.lang.Math.max;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.vdyp.application.ProcessingException;
import ca.bc.gov.nrs.vdyp.application.StandProcessingException;
import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.common.Estimators;
import ca.bc.gov.nrs.vdyp.common.ReconcilationMethods;
import ca.bc.gov.nrs.vdyp.common.Reference;
import ca.bc.gov.nrs.vdyp.common.Utils;
import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.CommonCalculatorException;
import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.CurveErrorException;
import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.NoAnswerException;
import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.SpeciesErrorException;
import ca.bc.gov.nrs.vdyp.common_calculators.enumerations.SiteIndexAgeType;
import ca.bc.gov.nrs.vdyp.common_calculators.enumerations.SiteIndexEquation;
import ca.bc.gov.nrs.vdyp.forward.Bank.CopyMode;
import ca.bc.gov.nrs.vdyp.forward.model.ControlVariable;
import ca.bc.gov.nrs.vdyp.forward.model.ForwardControlVariables;
import ca.bc.gov.nrs.vdyp.forward.model.ForwardDebugSettings;
import ca.bc.gov.nrs.vdyp.forward.model.VdypEntity;
import ca.bc.gov.nrs.vdyp.forward.model.VdypPolygon;
import ca.bc.gov.nrs.vdyp.io.parse.coe.UpperBoundsParser;
import ca.bc.gov.nrs.vdyp.math.FloatMath;
import ca.bc.gov.nrs.vdyp.model.BecDefinition;
import ca.bc.gov.nrs.vdyp.model.Coefficients;
import ca.bc.gov.nrs.vdyp.model.CommonData;
import ca.bc.gov.nrs.vdyp.model.ComponentSizeLimits;
import ca.bc.gov.nrs.vdyp.model.GenusDistribution;
import ca.bc.gov.nrs.vdyp.model.LayerType;
import ca.bc.gov.nrs.vdyp.model.MatrixMap2;
import ca.bc.gov.nrs.vdyp.model.MatrixMap2Impl;
import ca.bc.gov.nrs.vdyp.model.MatrixMap3;
import ca.bc.gov.nrs.vdyp.model.MatrixMap3Impl;
import ca.bc.gov.nrs.vdyp.model.ModelCoefficients;
import ca.bc.gov.nrs.vdyp.model.NonprimaryHLCoefficients;
import ca.bc.gov.nrs.vdyp.model.Region;
import ca.bc.gov.nrs.vdyp.model.SiteCurveAgeMaximum;
import ca.bc.gov.nrs.vdyp.model.UtilizationClass;
import ca.bc.gov.nrs.vdyp.model.UtilizationClassVariable;
import ca.bc.gov.nrs.vdyp.model.VolumeVariable;
import ca.bc.gov.nrs.vdyp.si32.site.SiteTool;

/**
 * This class manages "growing" polygons. Create an instance of the class, providing a control
 * map, and then call <code>processPolygon</code> for each polygon to be processed. All calls
 * to <code>processPolygon</code> are entirely independent of one another, allowing (different)
 * polygons to the processed in parallel.
 */
public class ForwardProcessingEngine {

	private static final Logger logger = LoggerFactory.getLogger(ForwardProcessor.class);

	private static final int UC_ALL_INDEX = UtilizationClass.ALL.ordinal();
	private static final int UC_SMALL_INDEX = UtilizationClass.SMALL.ordinal();

	private static final float MIN_BASAL_AREA = 0.001f;

	/** π/4/10⁴ */
	public static final float PI_40K = (float) (Math.PI / 40_000);

	/* pp */ final ForwardProcessingState fps;

	public ForwardProcessingEngine(Map<String, Object> controlMap) {

		this.fps = new ForwardProcessingState(controlMap);
	}

	public enum ExecutionStep {
		// Must be first
		NONE, //

		CHECK_FOR_WORK, //
		CALCULATE_MISSING_SITE_CURVES, //
		CALCULATE_COVERAGES, //
		DETERMINE_POLYGON_RANKINGS, //
		ESTIMATE_MISSING_SITE_INDICES, //
		ESTIMATE_MISSING_YEARS_TO_BREAST_HEIGHT_VALUES, //
		CALCULATE_DOMINANT_HEIGHT_AGE_SITE_INDEX, //
		SET_COMPATIBILITY_VARIABLES, //
		GROW, //

		// Must be last
		ALL; //

		public ExecutionStep predecessor() {
			if (this == NONE) {
				throw new IllegalStateException("ExecutionStep.None has no predecessor");
			}

			return ExecutionStep.values()[ordinal() - 1];
		}

		public ExecutionStep successor() {
			if (this == ALL) {
				throw new IllegalStateException("ExecutionStep.All has no successor");
			}

			return ExecutionStep.values()[ordinal() + 1];
		}
	}

	/**
	 * Run all steps of the engine on the given polygon.
	 * 
	 * @param polygon the polygon on which to operate
	 * 
	 * @throws ProcessingException should an error with the data occur during processing
	 */
	public void processPolygon(VdypPolygon polygon) throws ProcessingException {

		processPolygon(polygon, ExecutionStep.ALL);
	}

	/**
	 * Run all steps of the engine on the given polygon up to and including the given <code>lastStep</code>.
	 * 
	 * @param polygon the polygon on which to operate
	 * @param lastStepInclusive execute up to and including this step
	 * 
	 * @throws ProcessingException should an error with the data occur during processing
	 */
	public void processPolygon(VdypPolygon polygon, ExecutionStep lastStepInclusive) throws ProcessingException {

		logger.info("Starting processing of polygon {}", polygon.getDescription());

		fps.setPolygon(polygon);

		// All of BANKCHK1 that we need
		validatePolygon(polygon);

		// Determine the target year of the growth
		int targetYear;

		int growTargetControlVariableValue = fps.fcm.getForwardControlVariables()
				.getControlVariable(ControlVariable.GROW_TARGET_1);
		if (growTargetControlVariableValue == -1) {
			if (polygon.getTargetYear().isEmpty()) {
				throw new ProcessingException(
						"Control Variable 1 has the value -1, indicating that the grow-to years are"
								+ " to be read from a grow-to-year file (at " + ControlKey.FORWARD_INPUT_GROWTO.name()
								+ " in the control file), but no such file was specified."
				);
			}
			targetYear = polygon.getTargetYear().get();
		} else {
			if (growTargetControlVariableValue <= 400) {
				targetYear = polygon.getDescription().getYear() + growTargetControlVariableValue;
			} else {
				targetYear = growTargetControlVariableValue;
			}
		}

		// Run the forward algorithm for this polygon

		executeForwardAlgorithm(lastStepInclusive, targetYear);
	}

	private void executeForwardAlgorithm(ExecutionStep lastStepInclusive, int untilYear) throws ProcessingException {

		PolygonProcessingState pps = fps.getPolygonProcessingState();
		Bank bank = fps.getBank(0, LayerType.PRIMARY);

		logger.info("Beginning processing of polygon {} layer {}", pps.getLayer().getParent(), pps.getLayer());

		// BANKCHK1, simplified for the parameters METH_CHK = 4, LayerI = 1, and INSTANCE = 1
		if (lastStepInclusive.ordinal() >= ExecutionStep.CHECK_FOR_WORK.ordinal()) {
			stopIfNoWork(pps);
		}

		// SCINXSET - note these are calculated directly from the Primary bank of instance 1
		if (lastStepInclusive.ordinal() >= ExecutionStep.CALCULATE_MISSING_SITE_CURVES.ordinal()) {
			calculateMissingSiteCurves(bank, fps.fcm.getSiteCurveMap(), fps.getPolygonProcessingState());
		}

		// VPRIME1, method == 1
		if (lastStepInclusive.ordinal() >= ExecutionStep.CALCULATE_COVERAGES.ordinal()) {
			calculateCoverages();
		}

		if (lastStepInclusive.ordinal() >= ExecutionStep.DETERMINE_POLYGON_RANKINGS.ordinal()) {
			determinePolygonRankings(CommonData.PRIMARY_SPECIES_TO_COMBINE);
		}

		// SITEADD (TODO: SITEADDU when NDEBUG 11 > 0)
		if (lastStepInclusive.ordinal() >= ExecutionStep.ESTIMATE_MISSING_SITE_INDICES.ordinal()) {
			estimateMissingSiteIndices(pps);
		}

		if (lastStepInclusive.ordinal() >= ExecutionStep.ESTIMATE_MISSING_YEARS_TO_BREAST_HEIGHT_VALUES.ordinal()) {
			estimateMissingYearsToBreastHeightValues(pps);
		}

		// VHDOM1 METH_H = 2, METH_A = 2, METH_SI = 2
		if (lastStepInclusive.ordinal() >= ExecutionStep.CALCULATE_DOMINANT_HEIGHT_AGE_SITE_INDEX.ordinal()) {
			calculateDominantHeightAgeSiteIndex(pps, fps.fcm.getHl1Coefficients());
		}

		// CVSET1
		if (lastStepInclusive.ordinal() >= ExecutionStep.SET_COMPATIBILITY_VARIABLES.ordinal()) {
			setCompatibilityVariables();
		}

		// VGROW1
		if (lastStepInclusive.ordinal() >= ExecutionStep.GROW.ordinal()) {
			int veteranLayerInstance = 0;

			int startingYear = fps.getPolygonProcessingState().getPolygon().getDescription().getYear();
			Bank startBank = pps.getStartBank();

			int primaryLayerSourceInstance = 2;
			fps.storeActive(pps.getStartBank(), primaryLayerSourceInstance, LayerType.PRIMARY);

			writeLayers(startBank, veteranLayerInstance, false);

			boolean createNewGroups = fps.fcm.getDebugSettings()
					.getValue(ForwardDebugSettings.Vars.SPECIES_DYNAMICS_1) != 1
					&& fps.getPolygonProcessingState().getNSpecies() > 1;

			int primaryLayerTargetInstance = 2;
			int currentYear = startingYear;
			while (currentYear <= untilYear) {

				Bank endBank = grow(startBank, currentYear, veteranLayerInstance);

				// Store polygon (both primary and veteran layers) to output
				writeLayers(endBank, veteranLayerInstance, createNewGroups);

				currentYear += 1;

				int newPrimaryLayerSourceInstance = primaryLayerTargetInstance;
				primaryLayerTargetInstance = primaryLayerSourceInstance;
				primaryLayerSourceInstance = newPrimaryLayerSourceInstance;

				startBank = endBank;
			}
		}
	}

	/**
	 * VGROW1 - "grow" the primary layer, located in <code>primaryLayerSourceInstance</code>, starting at the 
	 * given year, and write the results to <code>primaryLayerTargetInstance</code>. The veteran layer instance
	 * is supplied but at this point is ignored.
	 * 
	 * @param primaryLayerSourceInstance
	 * @param currentYear
	 * @param primaryLayerTargetInstance
	 * @param veteranLayerInstance
	 * @return 
	 * 
	 * @throws ProcessingException
	 */
	private Bank grow(Bank start, int currentYear, int veteranLayerInstance)
			throws ProcessingException {

		PolygonProcessingState pps = fps.getPolygonProcessingState();
		VdypPolygon polygon = pps.getPolygon();

		logger.info("Performing grow of {} for year {}", polygon.getDescription().getName(), currentYear);

		Bank end = new Bank(start, CopyMode.CopyStructure);

		Optional<Bank> veteranBank = Optional.ofNullable(fps.getBank(veteranLayerInstance, LayerType.VETERAN));

		// If update-during-growth is set, and this is not the starting year, update the context
		int startingYear = polygon.getDescription().getYear();
		if (currentYear > startingYear
				&& fps.fcm.getForwardControlVariables()
						.getControlVariable(ControlVariable.UPDATE_DURING_GROWTH_6) >= 1) {
			// VPRIME1, method == 1
			calculateCoverages();

			// VHDOM1 METH_H = 2, METH_A = 2, METH_SI = 2
			calculateDominantHeightAgeSiteIndex(pps, fps.fcm.getHl1Coefficients());
		}

		float dhStart = pps.getPrimarySpeciesDominantHeight();
		int pspSiteCurveNumber = pps.getSiteCurveNumber(pps.getPrimarySpeciesIndex());
		float pspSiteIndex = pps.getPrimarySpeciesSiteIndex();
		float pspYtbhStart = pps.getPrimarySpeciesAgeToBreastHeight();
		float pspYabhStart = pps.getPrimarySpeciesAgeAtBreastHeight();

		// Calculate change in dominant height

		float dhDelta = growDominantHeight(dhStart, pspSiteCurveNumber, pspSiteIndex, pspYtbhStart);

		// Calculate change in basal area

		final Optional<Float> veteranLayerBasalArea;
		if (veteranBank.isPresent())
			veteranLayerBasalArea = Optional.of(veteranBank.get().basalAreas[0][UC_ALL_INDEX]);
		else {
			veteranLayerBasalArea = Optional.empty();
		}

		float dqStart = start.quadMeanDiameters[0][UC_ALL_INDEX];
		float baStart = start.basalAreas[0][UC_ALL_INDEX];
		float tphStart = start.treesPerHectare[0][UC_ALL_INDEX];
		float lhStart = start.loreyHeights[0][UC_ALL_INDEX];

		float baDelta = growBasalArea(
				pspYabhStart, fps.fcm.getDebugSettings(), dhStart, baStart, veteranLayerBasalArea, dhDelta
		);

		Reference<Boolean> wasDqGrowthLimitApplied = new Reference<>();
		float dqDelta = growQuadMeanDiameter(
				pspYtbhStart, baStart, dhStart, dqStart, veteranLayerBasalArea, veteranLayerBasalArea, dhDelta, wasDqGrowthLimitApplied
		);

		if (wasDqGrowthLimitApplied.get() /* is true */) {
			// Limit BA growth if DQ hit limit.
			float dqEnd = dqStart + dqDelta;
			float baEndMax = baStart * (dqEnd * dqEnd) / (dqStart * dqStart);
			baDelta = Math.min(baDelta, baEndMax - baStart);
		}

		// Cache some values for calculations below.

		float baChangeRate = baDelta / baStart;

		float pspLhStart = start.loreyHeights[pps.getPrimarySpeciesIndex()][UC_ALL_INDEX];
		float pspTphStart = start.treesPerHectare[pps.getPrimarySpeciesIndex()][UC_ALL_INDEX];

		float dhEnd = dhStart + dhDelta;
		float dqEnd = dqStart + dqDelta;
		float baEnd = baStart + baDelta;
		float tphEnd = calculateTreesPerHectare(baEnd, dqEnd);
		float tphMultiplier = tphEnd / tphStart;

		// Begin storing computed results - dq, ba and tph for the layer
		
		end.quadMeanDiameters[0][UC_ALL_INDEX] = dqEnd;
		end.basalAreas[0][UC_ALL_INDEX] = baEnd;
		end.treesPerHectare[0][UC_ALL_INDEX] = tphEnd;

		// Now do DQ growth by species, using one of several options: "full species", "dynamic", "partial dynamics"
		// or "no dynamics."
		int debugSetting1Value = fps.fcm.getDebugSettings().getValue(ForwardDebugSettings.Vars.SPECIES_DYNAMICS_1);

		boolean wasSolutionFound = false;
		if (debugSetting1Value == 2) {
			// This is the PARTIAL SPECIES DYNAMICS section. 

			// Begin by updating HL for all species (save the ungrown HLs).
			float[] currentLoreyHeights = new float[start.getNSpecies() + 1];
			currentLoreyHeights[0] = start.loreyHeights[0][UC_ALL_INDEX];
			for (int i : start.getIndices()) {
				currentLoreyHeights[i] = start.loreyHeights[i][UC_ALL_INDEX];
			}

			float pspTphEndEstimate = pspTphStart * (tphEnd / tphStart);

			growLoreyHeights(end, dhStart, dhEnd, pspTphStart, pspTphEndEstimate, pspLhStart);

			float sum1 = 0.0f;
			float sum2 = 0.0f;

			for (int i : start.getIndices()) {
				sum1 += start.basalAreas[i][UC_ALL_INDEX] * start.loreyHeights[i][UC_ALL_INDEX];
				sum2 += start.basalAreas[i][UC_ALL_INDEX];
			}

			start.loreyHeights[0][UC_ALL_INDEX] = sum1 / sum2;

			// Now do the actual updates of DQ by species
			wasSolutionFound = growUsingPartialSpeciesDynamics(end, dqDelta, baDelta, baStart, dqStart, tphStart, currentLoreyHeights);

			// Restore the Lorey Heights back to the values at the beginning of the period.
			// They will be updated below using the new estimate of TPH-primary species.
			for (int i = 1; i < start.getNSpecies(); i++) {
				start.loreyHeights[i][UC_ALL_INDEX] = currentLoreyHeights[i];
			}
		}

		if (!wasSolutionFound) {

			if (debugSetting1Value == 1 || start.getNSpecies() == 1) {

				// This is the NO SPECIES DYNAMICS section
				growUsingNoSpeciesDynamics(end, baChangeRate, tphMultiplier);
			} else {

				// This is the FULL SPECIES DYNAMICS section
				growUsingFullSpeciesDynamics(dqDelta, baDelta, baStart, dqStart, tphStart, lhStart);
			}
		}

		// Calculate trees-per-hectare over all species, all ucs

		float totalTphEnd = 0.0f;
		for (int i = 1; i < end.getNSpecies(); i++) {
			if (end.basalAreas[i][UC_ALL_INDEX] > 0.0f) {
				totalTphEnd += end.treesPerHectare[i][UC_ALL_INDEX];
			}
		}
		end.treesPerHectare[0][UC_ALL_INDEX] = totalTphEnd;

		// Calculate quad-mean-diameter over all species, uc UC_ALL_INDEX

		end.quadMeanDiameters[0][UC_ALL_INDEX] = calculateQuadMeanDiameter(
				end.basalAreas[0][UC_ALL_INDEX], end.treesPerHectare[0][UC_ALL_INDEX]
		);

		// Calculate Lorey heights over all species, uc UC_ALL_INDEX
		
		float pspTphEnd = end.treesPerHectare[pps.getPrimarySpeciesIndex()][UC_ALL_INDEX];
		growLoreyHeights(end, dhStart, dhEnd, pspTphStart, pspTphEnd, pspLhStart);

		// We now have site level predications for basal area, quad-mean-diameter, trees-per-hectare 
		// and Lorey height. Proceed to per-species estimates.

		// Calculate basal area percentages per species, uc UC_ALL_INDEX
		
		for (int i : start.getIndices()) {
			end.percentagesOfForestedLand[i] = 100.0f * end.basalAreas[i][UC_ALL_INDEX]
					/ end.basalAreas[0][UC_ALL_INDEX];
		}

		// Update the running values. TODO: why isn't siteIndex being updated?
		pps.updatePrimarySpeciesDetailsAfterGrowth(dhEnd);

		for (int i = 1; i < start.getNSpecies(); i++) {
			if (i == pps.getPrimarySpeciesIndex()) {
				end.ageTotals[i] = pps.getPrimarySpeciesTotalAge();
				end.dominantHeights[i] = dhEnd;
				end.siteIndices[i] = pps.getPrimarySpeciesSiteIndex();
				end.yearsAtBreastHeight[i] = pps.getPrimarySpeciesAgeAtBreastHeight();
			} else {
				float spSiStart = start.siteIndices[i];
				float spDhStart = start.dominantHeights[i];
				float spYtbhStart = start.yearsToBreastHeight[i];
				float spYabhStart = start.yearsAtBreastHeight[i];

				if (!Float.isNaN(spSiStart) && !Float.isNaN(spDhStart)
						&& !Float.isNaN(spYtbhStart) && !Float.isNaN(spYabhStart)) {

					float dhGrowth = growDominantHeight(
							spDhStart, pspSiteCurveNumber, spSiStart, spYtbhStart
					);
					end.dominantHeights[i] += dhGrowth;
				} else {
					end.dominantHeights[i] = Float.NaN;
				}
			}
		}

		pps.updateCompatibilityVariablesAfterGrowth();

		computeUtilizationComponentsPrimary();

		calculateSmallComponentYields(end);
		
		return end;
	}

	/**
	 * Calculate basal area, trees-per-hectare and quad-mean-diameter for all species
	 * in the polygon, given the per-layer basal area growth rate and tree-per-hectare
	 * muliplier
	 * 
	 * @param baChangeRate
	 * @param tphChangeRate
	 */
	private void growUsingNoSpeciesDynamics(Bank end, float baChangeRate, float tphChangeRate) {

		PolygonProcessingState pps = fps.getPolygonProcessingState();
		Bank start = pps.getStartBank();

		for (int i : pps.getIndices()) {

			float spBaStart = start.basalAreas[i][UC_ALL_INDEX];
			if (spBaStart > 0.0f) {
				float spBaEnd = spBaStart * (1.0f + baChangeRate);
				float spTphStart = start.treesPerHectare[i][UC_ALL_INDEX];
				float spTphEnd = spTphStart * tphChangeRate;
				float spDqEnd = calculateQuadMeanDiameter(spBaEnd, spTphEnd);
				if (spDqEnd < 7.51f) {
					spDqEnd = 7.51f;
					spTphEnd = calculateTreesPerHectare(spBaEnd, spDqEnd);
				}

				end.basalAreas[i][UC_ALL_INDEX] = spBaEnd;
				end.treesPerHectare[i][UC_ALL_INDEX] = spTphEnd;
				end.quadMeanDiameters[i][UC_ALL_INDEX] = spDqEnd;
			}
		}
	}

	/**
	 * GRSPpart - update quad-mean-diameter (dq) by species.
	 * 
	 * @param end the target of the computation
	 * @param dqDelta change in overall quad-mean-diameter during the growth period
	 * @param baDelta change in overall basal area during the growth period
	 * @param baStart overall basal area at start of growth period
	 * @param dqStart overall quad-mean-diameter at start of growth period
	 * @param tphStart overall trees-per-hectare value at the start of growth period
	 * @param currentLoreyHeights the current Lorey heights of each of the species 
	 * (indices 1 - # species) | overall (index 0) the start of the growth period 
	 * 
	 * @return true if and only if a solution was found.
	 */
	private boolean growUsingPartialSpeciesDynamics(Bank end, float dqDelta, float baDelta, float baStart, 
			float dqStart, float tphStart, float[] currentLoreyHeights) {

		boolean wasSuccessful = true;

		PolygonProcessingState pps = fps.getPolygonProcessingState();		

		if (dqDelta == 0 || baDelta == 0 || pps.getNSpecies() == 1) {
			return false /* no solution available */;
		}
		
		
		
		return wasSuccessful;
	}

	private void growUsingFullSpeciesDynamics(
			float dqDelta, float baDelta, float baStart, float dqStart,
			float tphStart, float lhStart
	) throws ProcessingException {

		PolygonProcessingState pps = fps.getPolygonProcessingState();
		Bank start = pps.getStartBank();
		Bank end = new Bank(start, CopyMode.CopyStructure);

		float spBaEnd[] = new float[pps.getNSpecies() + 1];
		float spTphNew[] = new float[pps.getNSpecies() + 1];
		float spDqNew[] = new float[pps.getNSpecies() + 1];
		boolean doSkip[] = new boolean[pps.getNSpecies() + 1];

		for (int i : pps.getIndices()) {
			spBaEnd[i] = start.basalAreas[i][UC_ALL_INDEX];
			spTphNew[i] = start.treesPerHectare[i][UC_ALL_INDEX];
			spDqNew[i] = start.quadMeanDiameters[i][UC_ALL_INDEX];

			doSkip[i] = false;
		}

		float sumSpBaDelta = 0.0f;
		float spBaDelta[] = new float[pps.getNSpecies()];

		for (int i : pps.getIndices()) {
			float pspLhStart = start.loreyHeights[i][UC_ALL_INDEX];
			if (i == pps.getPrimarySpeciesIndex()) {
				float pspBaStart = start.basalAreas[i][UC_ALL_INDEX];
				float pspYabhStart = pps.getPrimarySpeciesAgeAtBreastHeight();

				// Note: the FORTRAN passes Lorey height into parameter "HD" ("Dominant Height") - are these
				// equivalent?
				spBaDelta[i] = growBasalAreaForPrimarySpecies(
						baStart, baDelta, pspBaStart, lhStart, pspYabhStart, pspLhStart
				);
			} else {
				float spBaStart = start.basalAreas[i][UC_ALL_INDEX];
				float spDqStart = start.quadMeanDiameters[i][UC_ALL_INDEX];
				float spLhStart = start.loreyHeights[i][UC_ALL_INDEX];
				spBaDelta[i] = growBasalAreaForNonPrimarySpecies(
						start.speciesNames[i], baStart, baDelta, pspLhStart, spBaStart, spDqStart, spLhStart
				);
			}

			sumSpBaDelta += spBaDelta[i];
		}

		{
			// Estimate of basal area growth by species

			// Iteratively attempt to find a value f such that:
			//
			// if revised spBaDelta = spBaStart * (f + spBaDelta / spBaStart) 
			// then sum(revised spBaDelta) = baDelta
			//
			// for all species whose basal area after growth is non-negative.

			var baBase = baStart;
			var passNumber = 0;

			while (true) {

				var f = (baDelta - sumSpBaDelta) / baBase;

				int nSkipped = 0;
				sumSpBaDelta = 0.0f;

				for (int i : pps.getIndices()) {
					if (!doSkip[i]) {
						var spBaStart = pps.getStartBank().basalAreas[i][UC_ALL_INDEX];
						spBaEnd[i] = spBaStart + spBaDelta[i] + f * spBaStart;
						if (spBaEnd[i] < 0.0f) {
							spBaEnd[i] = 0.0f;
							doSkip[i] = true;
							nSkipped += 1;
							sumSpBaDelta -= spBaStart;
							baBase -= spBaStart;
						} else {
							sumSpBaDelta += spBaEnd[i] - spBaStart;
						}
					}
				}

				if (nSkipped == 0) {
					break;
				}

				passNumber += 1;
				if (passNumber > 5 || baBase <= 0.0f) {
					throw new ProcessingException(
							MessageFormat.format(
									"Unable to converge on a value for \"f\" in"
											+ " growUsingFullSpeciesDynamics({0}, {1}, {2}, {3}, {4}, {5})", dqDelta, baDelta, baStart, dqStart, tphStart, lhStart
							)
					);
				}
			}
		}

		{
			// Estimate of quad-mean-diameter growth by species

			while (true) {

				var bestScore = 1000.0;
				var bestF = Float.NaN;

				var f = 0.0f;
				var passNumber = 0;
				var nSkipped = 0;
				var totalBasalAreaSkipped = 0.0f;

				for (int i : pps.getIndices()) {
					float spDqStart = start.quadMeanDiameters[i][UC_ALL_INDEX];
					float spLhStart = start.loreyHeights[i][UC_ALL_INDEX];

					float spDqDelta;
					if (i == pps.getPrimarySpeciesIndex()) {

						spDqDelta = growQuadMeanDiameterForPrimarySpecies(
								dqStart, dqDelta, spDqStart, lhStart, spLhStart
						);
					} else {
						spDqDelta = growQuadMeanDiameterForNonPrimarySpecies(
								i, dqStart, dqDelta, spDqStart, lhStart, spLhStart
						);
					}

					spDqDelta += f;

					ComponentSizeLimits csl = getComponentSizeLimits(
							start.speciesNames[i], pps.getBecZone().getRegion()
					);

					var spLhAllStart = start.loreyHeights[i][UC_ALL_INDEX];

					float spDqMaximum = Math.min(
							csl.quadMeanDiameterMaximum(), csl.maxQuadMeanDiameterLoreyHeightRatio() * spLhAllStart
					);

					if (spDqStart + spDqDelta > spDqMaximum) {
						spDqDelta = Math.min(0.0f, spDqMaximum - spDqStart);
						nSkipped += 1;
						totalBasalAreaSkipped += start.basalAreas[i][UC_ALL_INDEX];
					}

					float spDqMinimum = Math.max(7.6f, csl.minQuadMeanDiameterLoreyHeightRatio() * spLhAllStart);

					if (spDqStart + spDqDelta < spDqMinimum) {
						spDqDelta = spDqMinimum - spDqStart;
						nSkipped += 1;
						totalBasalAreaSkipped += start.basalAreas[i][UC_ALL_INDEX];
					}

					spDqNew[i] = spDqStart + spDqDelta;
				}

				float tph = 0.0f;
				for (int i : start.speciesIndices) {
					if (spBaEnd[i] > 0.0f) {
						spTphNew[i] = calculateTreesPerHectare(spBaEnd[i], spDqNew[i]);
					} else {
						spTphNew[i] = 0.0f;
					}
					tph += spTphNew[i];
				}

				if (passNumber == 15 || (nSkipped == pps.getNSpecies() && passNumber > 2)) {
					break;
				}

				float dqNewBar = calculateQuadMeanDiameter(baStart + baDelta, tph);
				float dqStartEstimate = calculateQuadMeanDiameter(baStart, tphStart);
				float dqWant = dqStartEstimate + dqDelta;

				var score = FloatMath.abs(dqWant - dqNewBar);
				if (score < bestScore) {
					bestScore = score;
					bestF = f;
				}

				if (FloatMath.abs(score) < 0.001) {
					break;
				}

				if (totalBasalAreaSkipped > 0.7f) {
					totalBasalAreaSkipped = 0.7f * baStart;
				}

				score = score * baStart / (baStart - totalBasalAreaSkipped);
				f += score;

				passNumber += 1;
				if (passNumber == 15) {
					f = bestF;
				}
			}

			for (int i : start.speciesIndices) {
				end.basalAreas[i][UC_ALL_INDEX] = spBaEnd[i];
				end.treesPerHectare[i][UC_ALL_INDEX] = spTphNew[i];
				if (spBaEnd[i] > 0.0f) {
					end.quadMeanDiameters[i][UC_ALL_INDEX] = calculateQuadMeanDiameter(spBaEnd[i], spTphNew[i]);
				}
			}
		}
	}

	/**
	 * EMP061 - get the component size limits for the given genus and alias.
	 * 
	 * @param genusAlias the alias of the genus in question
	 * @param region the region in question
	 * 
	 * @return as described
	 */
	private ComponentSizeLimits getComponentSizeLimits(String genusAlias, Region region) {

		MatrixMap2<String, Region, Coefficients> limits = fps.fcm.getComponentSizeCoefficients();
		Coefficients coe = limits.get(genusAlias, region);

		return new ComponentSizeLimits(coe.getCoe(1), coe.getCoe(2), coe.getCoe(3), coe.getCoe(4));
	}

	/**
	 * EMP150 - return the quad-mean-diameter growth for the primary species of the
	 * Primary layer. Based on IPSJF150.doc (July, 1999).
	 * 
	 * @param dqStart primary layer quad-mean-diameter at start of growth period
	 * @param dqDelta change in quad-mean-diameter of primary layer during growth period
	 * @param pspDqStart primary species quad-mean-diameter at start of growth period
	 * @param lhStart primary layer Lorey height at start of growth period
	 * @param pspLhStart primary species Lorey height at start of growth period
	
	 * @return as described
	 * @throws ProcessingException in the event of an error
	 */
	private float growQuadMeanDiameterForPrimarySpecies(
			float dqStart, float dqDelta, float pspDqStart, float lhStart, float pspLhStart
	) throws ProcessingException {

		PolygonProcessingState pps = fps.getPolygonProcessingState();
		int pspStratumNumber = pps.getPrimarySpeciesStratumNumber();

		ModelCoefficients mc = fps.fcm.getPrimarySpeciesQuadMeanDiameterGrowthCoefficients().get(pspStratumNumber);

		if (mc == null) {
			throw new ProcessingException(
					MessageFormat.format(
							"primaryQuadMeanDiameterGrowthCoefficients do not exist"
									+ " for stratum number {0}, call growQuadMeanDiameterForPrimarySpecies("
									+ "{1}, {2}, {3}, {4}, {5})", dqStart, dqDelta, pspDqStart, lhStart, pspLhStart
					)
			);
		}

		return growQuadMeanDiameter(mc.getCoefficients(), dqStart, dqDelta, pspDqStart, lhStart, pspLhStart);
	}

	private float growQuadMeanDiameterForNonPrimarySpecies(
			int speciesIndex, float dqStart, float dqDelta, float spDqStart, float lhStart, float spLhStart
	) throws ProcessingException {

		PolygonProcessingState pps = fps.getPolygonProcessingState();

		String speciesName = pps.getStartBank().speciesNames[speciesIndex];
		int pspStratumNumber = pps.getPrimarySpeciesStratumNumber();

		var modelCoefficientsOpt = fps.fcm.getNonPrimarySpeciesQuadMeanDiameterGrowthCoefficients()
				.get(speciesName, pspStratumNumber);

		if (modelCoefficientsOpt.isEmpty()) {
			modelCoefficientsOpt = fps.fcm.getNonPrimarySpeciesQuadMeanDiameterGrowthCoefficients().get(speciesName, 0);
		}

		if (modelCoefficientsOpt.isEmpty()) {
			throw new ProcessingException(
					MessageFormat.format(
							"No nonPrimarySpeciesQuadMeanDiameterGrowthCoefficients exist for stratum {0}"
									+ "; call growQuadMeanDiameterForNonPrimarySpecies({1}, {2}, {3}, {4}, {5}, {6})", pspStratumNumber, speciesIndex, dqStart, dqDelta, lhStart, spDqStart, spLhStart
					)
			);
		}

		return growQuadMeanDiameter(modelCoefficientsOpt.get(), dqStart, dqDelta, spDqStart, lhStart, spLhStart);
	}

	private static float growQuadMeanDiameter(
			Coefficients mc, float dqStart, float dqDelta, float spDqStart, float lhStart, float spLhStart
	) {
		float a0 = mc.getCoe(1);
		float a1 = mc.getCoe(2);
		float a2 = mc.getCoe(3);

		final float dqBase = 7.45f;

		float dqRateStart = (spDqStart - dqBase) / (dqStart - dqBase);
		float logDqRateStart = FloatMath.log(dqRateStart);
		float logDqRateDelta = a0 + a1 * FloatMath.log(spDqStart) + a2 * spLhStart / lhStart;
		float dqRateEnd = FloatMath.exp(logDqRateStart + logDqRateDelta);

		float spDqEnd = dqRateEnd * (dqStart + dqDelta - dqBase) + dqBase;
		if (spDqEnd < 7.51f) {
			spDqEnd = 7.51f;
		}

		float spDqDelta = spDqEnd - spDqStart;

		return spDqDelta;
	}

	/**
	 * EMP149 - return the basal area growth for non-Primary species of Primary layer. Based on 
	 * IPSJF149.doc (July, 1999).
	 * 
	 * @param speciesName
	 * @param baStart layer basal area at start of growth period
	 * @param baDelta layer basal area growth during period
	 * @param lhStart layer Lorey height at start of growth period
	 * @param spBaStart this species basal area at start of growth period
	 * @param spDqStart this species quad-mean-diameter at start of growth period
	 * @param spLhStart this species Lorey height at start of growth period\
	 * 
	 * @return as described
	 * @throws ProcessingException 
	 */
	private float growBasalAreaForNonPrimarySpecies(
			String speciesName, float baStart, float baDelta, float lhStart, float spBaStart, float spDqStart,
			float spLhStart
	) throws ProcessingException {

		PolygonProcessingState pps = fps.getPolygonProcessingState();

		if (spBaStart <= 0.0f || spBaStart >= baStart) {
			throw new ProcessingException(
					MessageFormat.format(
							"Species basal area {} is out of range; it must be"
									+ " positive and less that overall basal area", spBaStart, baStart
					)
			);
		}

		int pspStratumNumber = pps.getPrimarySpeciesStratumNumber();

		var modelCoefficientsOpt = fps.fcm.getNonPrimarySpeciesBasalAreaGrowthCoefficients()
				.get(speciesName, pspStratumNumber);

		if (modelCoefficientsOpt.isEmpty()) {
			modelCoefficientsOpt = fps.fcm.getNonPrimarySpeciesBasalAreaGrowthCoefficients().get(speciesName, 0);
		}

		if (modelCoefficientsOpt.isEmpty()) {
			throw new ProcessingException(
					MessageFormat.format(
							"No nonPrimarySpeciesBasalAreaGrowthCoefficients exist for stratum {0}"
									+ "; call growBasalAreaForNonPrimarySpecies({0}, {1}, {2}, {3}, {4}, {5}, {6}, {7})", 
							pspStratumNumber, speciesName, baStart, baDelta, lhStart, spBaStart, spDqStart, spLhStart
					)
			);
		}

		Coefficients mc = modelCoefficientsOpt.get();

		float a0 = mc.getCoe(1);
		float a1 = mc.getCoe(2);
		float a2 = mc.getCoe(3);

		var baProportionStart = spBaStart / baStart;
		var logBaProportionStart = FloatMath.log(baProportionStart / 1.0f - baProportionStart);

		var logBaDeltaProportion = a0 + a1 * FloatMath.log(spDqStart) + a2 * spLhStart / lhStart;
		var logBaProportionEnd = logBaProportionStart + logBaDeltaProportion;
		var baProportionEnd = FloatMath.exp(logBaProportionEnd) / (1.0f + FloatMath.exp(logBaProportionEnd));

		var spDeltaBa = baProportionEnd * (baStart + baDelta) - spBaStart;

		return spDeltaBa;
	}

	/** 
	 * EMP148 - return the basal area growth for Primary species of Primary layer. Based on 
	 * IPSJF148.doc (July, 1999).
	 * 
	 * @param baStart layer basal area at start of growth period
	 * @param baDelta layer basal area growth during period
	 * @param pspBaStart primary species basal area at start of growth period
	 * @param dhStart dominant height of primary species at start of growth period
	 * @param pspYabhStart primary species years at breast height at start of growth period
	 * @param pspLhStart primary species Lorey height at start of growth period
	 * 
	 * @return as described
	 * @throws ProcessingException
	 */
	private float growBasalAreaForPrimarySpecies(
			float baStart, float baDelta, float pspBaStart, float dhStart, float pspYabhStart,
			float pspLhStart
	) throws ProcessingException {

		PolygonProcessingState pps = fps.getPolygonProcessingState();

		float spToAllProportionStart = pspBaStart / baStart;
		if (spToAllProportionStart <= 0.999f) {
			var psStratumNumber = pps.getPrimarySpeciesStratumNumber();

			ModelCoefficients mc = fps.fcm.getPrimarySpeciesBasalAreaGrowthCoefficients().get(psStratumNumber);
			int model = mc.getModel();
			var a0 = mc.getCoefficients().getCoe(1);
			var a1 = mc.getCoefficients().getCoe(2);
			var a2 = mc.getCoefficients().getCoe(3);

			var logBaProportionStart = FloatMath.log(spToAllProportionStart / (1.0f - spToAllProportionStart));

			float logBaDeltaProportion;
			if (model == 3) {
				logBaDeltaProportion = a0 + a1 * dhStart;
			} else if (model == 8) {
				logBaDeltaProportion = a0 + a1 * pspYabhStart + a2 * pspLhStart / dhStart;
			} else if (model == 9) {
				logBaDeltaProportion = a0 + a1 * logBaProportionStart + a2 * baStart;
			} else {
				throw new ProcessingException(
						MessageFormat
								.format("Model value {} for polygon stratum {} is out of range", model, psStratumNumber)
				);
			}

			var x = FloatMath.exp(logBaProportionStart + logBaDeltaProportion);
			var spToAllProportionEnd = FloatMath.exp(x) / (1 + FloatMath.exp(x));
			baDelta = spToAllProportionEnd * (baStart + baDelta) - pspBaStart;
		}

		return baDelta;
	}

	/**
	 * YUC1
	 */
	private void computeUtilizationComponentsPrimary() {
		// TODO Auto-generated method stub
	}

	/**
	 * Record small component utilization values for primary layer.
	 * 
	 * @throws ProcessingException
	 */
	private void calculateSmallComponentYields(Bank end) throws ProcessingException {

		PolygonProcessingState pps = fps.getPolygonProcessingState();
		Bank start = pps.getStartBank();

		float lhSum = 0.0f;
		float baSum = 0.0f;
		float tphSum = 0.0f;
		float wsVolumeSum = 0.0f;

		for (int speciesIndex : pps.getIndices()) {

			float spLhAll = end.loreyHeights[speciesIndex][UC_ALL_INDEX];
			float spBaAll = end.basalAreas[speciesIndex][UC_ALL_INDEX];
			float spDqAll = end.quadMeanDiameters[speciesIndex][UC_ALL_INDEX];

			Region region = pps.getPolygon().getBiogeoclimaticZone().getRegion();
			String speciesName = start.speciesNames[speciesIndex];

			// EMP080
			float smallProbability = smallComponentProbability(speciesName, spLhAll, region);

			// This whole operation is on Actual BA's, not 100% occupancy.
			float fractionAvailable = pps.getPolygon().getPercentForestLand() / 100.0f;

			if (fractionAvailable > 0.0f) {
				spBaAll *= fractionAvailable;
			}

			// EMP081
			float conditionalExpectedBasalArea = conditionalExpectedBaseArea(speciesName, spBaAll, spLhAll, region);

			if (fractionAvailable > 0.0f) {
				conditionalExpectedBasalArea /= fractionAvailable;
			}

			float spBaSmall = smallProbability * conditionalExpectedBasalArea;

			// EMP082
			float spDqSmall = smallComponentQuadMeanDiameter(speciesName, spLhAll); // DQSMsp

			// EMP085
			float spLhSmall = smallComponentLoreyHeight(speciesName, spLhAll, spDqSmall, spDqAll); // HLSMsp

			// EMP086
			float meanVolumeSmall = meanVolumeSmall(speciesName, spLhSmall, spDqSmall); // VMEANSMs

			int controlVar3Value = fps.fcm.getForwardControlVariables()
					.getControlVariable(ControlVariable.COMPAT_VAR_APPLICATION_3);

			if (controlVar3Value >= 1 /* apply compatibility variables */) {
				spBaSmall += pps.getCVSmall(speciesIndex, UtilizationClassVariable.BASAL_AREA);
				if (spBaSmall < 0.0f) {
					spBaSmall = 0.0f;
				}
				spDqSmall += pps.getCVSmall(speciesIndex, UtilizationClassVariable.QUAD_MEAN_DIAMETER);
				if (spDqSmall < 4.01f) {
					spDqSmall = 4.01f;
				} else if (spDqSmall > 7.49) {
					spDqSmall = 7.49f;
				}
				spLhSmall = 1.3f * (spLhSmall - 1.3f)
						* FloatMath.exp(pps.getCVSmall(speciesIndex, UtilizationClassVariable.LOREY_HEIGHT));

				if (controlVar3Value >= 2 && meanVolumeSmall > 0.0f) {
					meanVolumeSmall *= FloatMath
							.exp(pps.getCVSmall(speciesIndex, UtilizationClassVariable.WHOLE_STEM_VOLUME));
				}
			}

			float spTphSmall = calculateTreesPerHectare(spBaSmall, spDqSmall);
			float spWsVolumeSmall = spTphSmall * meanVolumeSmall;

			end.loreyHeights[speciesIndex][UC_SMALL_INDEX] = spLhSmall;
			end.basalAreas[speciesIndex][UC_SMALL_INDEX] = spBaSmall;
			end.treesPerHectare[speciesIndex][UC_SMALL_INDEX] = spTphSmall;
			end.quadMeanDiameters[speciesIndex][UC_SMALL_INDEX] = spDqSmall;
			end.wholeStemVolumes[speciesIndex][UC_SMALL_INDEX] = spWsVolumeSmall;
			end.closeUtilizationVolumes[speciesIndex][UC_SMALL_INDEX] = 0.0f;
			end.cuVolumesMinusDecay[speciesIndex][UC_SMALL_INDEX] = 0.0f;
			end.cuVolumesMinusDecayAndWastage[speciesIndex][UC_SMALL_INDEX] = 0.0f;

			lhSum += spBaSmall * spDqSmall;
			baSum += spBaSmall;
			tphSum += spTphSmall;
			wsVolumeSum += spWsVolumeSmall;
		}

		if (baSum > 0.0) {
			end.loreyHeights[0][UC_SMALL_INDEX] = lhSum / baSum;
		} else {
			end.loreyHeights[0][UC_SMALL_INDEX] = 0.0f;
		}
		end.basalAreas[0][UC_SMALL_INDEX] = baSum;
		end.treesPerHectare[0][UC_SMALL_INDEX] = tphSum;
		end.quadMeanDiameters[0][UC_SMALL_INDEX] = calculateQuadMeanDiameter(baSum, tphSum);
		end.wholeStemVolumes[0][UC_SMALL_INDEX] = wsVolumeSum;
		end.closeUtilizationVolumes[0][UC_SMALL_INDEX] = 0.0f;
		end.cuVolumesMinusDecay[0][UC_SMALL_INDEX] = 0.0f;
		end.cuVolumesMinusDecayAndWastage[0][UC_SMALL_INDEX] = 0.0f;
	}

	/** 
	 * GRSPHL - estimate the Lorey Heights of all species at end of growth period. The results of 
	 * the calculations are persisted in {@code start}.
	 * 
	 * @param dhStart (primary species) dominant height at start
	 * @param dhEnd (primary species) dominant height at end
	 * @param pspTphStart primary species trees-per-hectare at start
	 * @param pspTphEnd primary species trees-per-hectare at end
	 * @param pspLhStart primary species Lorey height at end
	 */
	private void growLoreyHeights(Bank end, float dhStart, float dhEnd, float pspTphStart, float pspTphEnd, float pspLhStart) {
		
		PolygonProcessingState pps = fps.getPolygonProcessingState();
		Bank start = pps.getStartBank();

		float pspLhStartEstimate = estimatePrimarySpeciesLoreyHeight(dhStart, pspTphStart);
		float pspLhEndEstimate = estimatePrimarySpeciesLoreyHeight(dhEnd, pspTphEnd);

		float primaryF = (pspLhStart - 1.3f) / (pspLhStartEstimate - 1.3f);
		float primaryLhAdjustment = fps.fcm.getCompVarAdjustments().getLoreyHeightPrimaryParam();
		primaryF = 1.0f + (primaryF - 1.0f) * primaryLhAdjustment;

		float pspLhEnd = 1.3f + (pspLhEndEstimate - 1.3f) * primaryF;

		int debugSetting8Value = fps.fcm.getDebugSettings()
				.getValue(ForwardDebugSettings.Vars.LOREY_HEIGHT_CHANGE_STRATEGY_8);

		int primarySpeciesIndex = fps.getPolygonProcessingState().getPrimarySpeciesIndex();
		if (debugSetting8Value != 2 || dhStart != dhEnd) {
			end.loreyHeights[primarySpeciesIndex][UC_ALL_INDEX] = pspLhEnd;
		} else if (debugSetting8Value == 2) {
			pspLhEnd = start.loreyHeights[primarySpeciesIndex][UC_ALL_INDEX];
		}

		float nonPrimaryLhAdjustment = fps.fcm.getCompVarAdjustments().getLoreyHeightOther();

		for (int i : pps.getIndices()) {
			if (start.basalAreas[i][UC_ALL_INDEX] > 0.0f && i != primarySpeciesIndex) {
				if (! (dhEnd == dhStart && debugSetting8Value >= 1)) {
					float spLhEstimate1 = estimateNonPrimarySpeciesLoreyHeight(
							i, dhStart, pspLhStart
					);
					float spLhEstimate2 = estimateNonPrimarySpeciesLoreyHeight(
							i, dhEnd, pspLhEnd
					);

					float otherF = (start.loreyHeights[i][UC_ALL_INDEX] - 1.3f) / (spLhEstimate1 - 1.3f);
					otherF = 1.0f - (otherF - 1.0f) * nonPrimaryLhAdjustment;
					end.loreyHeights[i][UC_ALL_INDEX] = 1.3f + (spLhEstimate2 - 1.3f) * otherF;
				}
			}
		}
	}

	/**
	 * EMP050, method 1. Estimate the Lorey height of the primary species from the 
	 * its dominant height and trees-per-hectare.
	 * 
	 * @param dh the dominantHeight (of the primary species)
	 * @param pspTph trees-per-hectare of the primary species
	 * 
	 * @return as described
	 */
	private float estimatePrimarySpeciesLoreyHeight(float dh, float pspTph) {

		String primarySpeciesAlias = fps.getPolygonProcessingState().getPrimarySpeciesAlias();
		Region polygonRegion = fps.getPolygonProcessingState().getBecZone().getRegion();
		var coefficients = fps.fcm.getLoreyHeightPrimarySpeciesEquationP1Coefficients();

		float a0 = coefficients.get(primarySpeciesAlias, polygonRegion).getCoe(1);
		float a1 = coefficients.get(primarySpeciesAlias, polygonRegion).getCoe(2);
		float a2 = coefficients.get(primarySpeciesAlias, polygonRegion).getCoe(3);

		float hMult = a0 - a1 + a1 * FloatMath.exp(a2 * (pspTph - 100.0f));

		return 1.3f + (dh - 1.3f) * hMult;
	}

	/**
	 * EMP053 - estimate the Lorey height of a non-primary species of the polygon.
	 * 
	 * @param speciesIndex (non-primary) species index
	 * @param dh (primary species) dominant height
	 * @param pspLoreyHeight primary species Lorey height
	 * 
	 * @return as described
	 */
	private float estimateNonPrimarySpeciesLoreyHeight(
			int speciesIndex, float dh, float pspLoreyHeight
	) {
		PolygonProcessingState pps = fps.getPolygonProcessingState();
		Bank start = pps.getStartBank();
		
		float spLh;

		int primarySpeciesIndex = pps.getPrimarySpeciesIndex();
		String primarySpeciesAlias = start.speciesNames[primarySpeciesIndex];
		String speciesAlias = start.speciesNames[speciesIndex];
		Region region = pps.getBecZone().getRegion();

		var coefficients = fps.fcm.getLoreyHeightNonPrimaryCoefficients();

		var configuredLhCoefficients = coefficients.get(speciesAlias, primarySpeciesAlias, region);
		var lhCoefficients = configuredLhCoefficients.orElseGet(() -> NonprimaryHLCoefficients.getDefault());

		float a0 = lhCoefficients.getCoe(1);
		float a1 = lhCoefficients.getCoe(2);
		int equationIndex = lhCoefficients.getEquationIndex();

		if (equationIndex == 1) {
			spLh = 1.3f + a0 * (FloatMath.pow(dh - 1.3f, a1));
		} else if (equationIndex == 2) {
			spLh = 1.3f + a0 * (FloatMath.pow(pspLoreyHeight - 1.3f, a1));
		} else {
			throw new IllegalStateException(
					MessageFormat.format("Expecting equation index 1 or 2 but saw {0}", equationIndex)
			);
		}

		return spLh;
	}

	/**
	 * EMP117 - Quad Mean Diameter growth for the primary layer.
	 * 
	 * @param pspYabhStart primary species years at breast height (age) at start of growth period
	 * @param dhStart layer dominant height (i.e., the height of the primary species) at start of growth period
	 * @param dqStart layer quad-mean-diameter at start of growth period
	 * @param veteranBaStart veteran layer basal area at start of growth period
	 * @param veteranBaEnd veteran layer basal area at end of growth period
	 * @param dhDelta growth in dominant height during growth period
	 * 
	 * @return growth in quad-mean-diameter for the year. This may be negative.
	 * 
	 * @throws StandProcessingException
	 */
	float growQuadMeanDiameter(
			float pspYabhStart, float baStart, float dhStart, float dqStart,
			Optional<Float> veteranBaStart, Optional<Float> veteranBaEnd, float dhDelta,
			Reference<Boolean> dqGrowthLimitApplied
	)
			throws StandProcessingException {

		var pps = fps.getPolygonProcessingState();
		var becZone = pps.getBecZone();

		float[] speciesProportionsByBasalArea = getSpeciesProportionsByBasalArea();

		var dqYieldCoefficients = fps.fcm.getQuadMeanDiameterYieldCoefficients();
		var decayBecZoneAlias = becZone.getDecayBec().getAlias();
		Coefficients coefficientsWeightedBySpeciesAndDecayBec = Coefficients.empty(6, 0);
		for (int i = 0; i < 6; i++) {
			float sum = 0.0f;
			for (int speciesIndex : pps.getIndices()) {
				String speciesAlias = pps.getStartBank().speciesNames[speciesIndex];
				sum += dqYieldCoefficients.get(decayBecZoneAlias, speciesAlias).getCoe(i)
						* speciesProportionsByBasalArea[speciesIndex];
			}
			coefficientsWeightedBySpeciesAndDecayBec.setCoe(i, sum);
		}

		float dqYieldStart = fps.estimators.estimateQuadMeanDiameterYield(
				coefficientsWeightedBySpeciesAndDecayBec, dhStart, pspYabhStart, veteranBaStart, becZone, pps
						.getPrimarySpeciesGroupNumber()
		);

		float dhEnd = dhStart + dhDelta;
		float pspYabhEnd = pspYabhStart + 1.0f;

		float dqYieldEnd = fps.estimators.estimateQuadMeanDiameterYield(
				coefficientsWeightedBySpeciesAndDecayBec, dhEnd, pspYabhEnd, veteranBaEnd, becZone, pps
						.getPrimarySpeciesGroupNumber()
		);

		float dqYieldGrowth = dqYieldEnd - dqYieldStart;

		int debugSetting6Value = fps.fcm.getDebugSettings().getValue(ForwardDebugSettings.Vars.DQ_GROWTH_MODEL_6);

		var growthFaitDetails = fps.fcm.getQuadMeanDiameterGrowthFiatDetails().get(becZone.getRegion());

		Optional<Float> dqGrowthFiat = Optional.empty();
		if (debugSetting6Value != 1) {
			var convergenceCoefficient = growthFaitDetails.calculateCoefficient(pspYabhStart);

			float adjust = -convergenceCoefficient * (dqStart - dqYieldStart);
			dqGrowthFiat = Optional.of(dqYieldGrowth + adjust);
		}

		Optional<Float> dqGrowthEmpirical = Optional.empty();
		if (debugSetting6Value != 0) {
			dqGrowthEmpirical = Optional.of(
					calculateQuadMeanDiameterGrowthEmpirical(
							pspYabhStart, dhStart, baStart, dqStart, dhDelta, dqYieldStart, dqYieldEnd
					)
			);
		}

		float dqGrowth;

		switch (debugSetting6Value) {
		case 0:
			dqGrowth = dqGrowthFiat.orElseThrow();
			break;

		case 1:
			dqGrowth = dqGrowthEmpirical.orElseThrow();
			break;

		case 2: {
			float empiricalProportion = 1.0f;
			if (pspYabhStart >= growthFaitDetails.getMixedCoefficient(1)) {
				empiricalProportion = 0.0f;
			} else if (pspYabhStart > growthFaitDetails.getMixedCoefficient(0)) {
				float t1 = pspYabhStart - growthFaitDetails.getMixedCoefficient(0);
				float t2 = growthFaitDetails.getMixedCoefficient(1) - growthFaitDetails.getMixedCoefficient(0);
				float t3 = growthFaitDetails.getMixedCoefficient(2);
				empiricalProportion = 1.0f - FloatMath.pow(t1 / t2, t3);
			}
			dqGrowth = empiricalProportion * dqGrowthEmpirical.orElseThrow()
					+ (1.0f - empiricalProportion) * dqGrowthFiat.orElseThrow();
			break;
		}

		default:
			throw new IllegalStateException("debugSetting6Value of " + debugSetting6Value + " is not supported");
		}

		float dqUpperBound = growQuadraticMeanDiameterUpperBound();
		float dqLimit = Math.max(dqUpperBound, dqStart);

		if (dqStart + dqGrowth < 7.6f) {
			dqGrowth = 7.6f - dqStart;
		}

		if (dqStart + dqGrowth > dqLimit - 0.001f) {
			dqGrowthLimitApplied.set(true);
			dqGrowth = Math.max(dqLimit - dqStart, 0.0f);
		} else {
			dqGrowthLimitApplied.set(false);
		}

		return dqGrowth;
	}

	/**
	 * EMP122. Calculate quad mean diameter growth using the empirical model.
	 * 
	 * @param pspYabhStart primary species years at breast height at start of growth period
	 * @param dhStart primary species dominant height at start of growth period
	 * @param baStart basal area of primary layer at start of growth period
	 * @param dqStart quad mean diameter at start of growth period
	 * @param hdGrowth growth in dominant height
	 * @param dqYieldStart quad mean diameter yield at start of growth period
	 * @param dqYieldEnd quad mean diameter yield at end of growth period
	 * 
	 * @return the change in primary layer basal area from start to start + 1 year
	 */
	private float calculateQuadMeanDiameterGrowthEmpirical(float pspYabhStart, float dhStart,
			float baStart, float dqStart, float hdGrowth, float dqYieldStart, float dqYieldEnd
	) {
		// Compute the growth in quadratic mean diameter 

		var dqGrowthEmpiricalCoefficients = fps.fcm.getQuadMeanDiameterGrowthEmpiricalCoefficients();

		Integer stratumNumber = fps.getPolygonProcessingState().getPrimarySpeciesStratumNumber();
		var firstSpeciesDqGrowthCoe = dqGrowthEmpiricalCoefficients.get(stratumNumber);

		float a0 = firstSpeciesDqGrowthCoe.get(0);
		float a1 = firstSpeciesDqGrowthCoe.get(1);
		float a2 = firstSpeciesDqGrowthCoe.get(2);
		float a3 = firstSpeciesDqGrowthCoe.get(3);
		float a4 = firstSpeciesDqGrowthCoe.get(4);
		float a5 = firstSpeciesDqGrowthCoe.get(5);
		float a6 = firstSpeciesDqGrowthCoe.get(6);

		pspYabhStart = Math.max(pspYabhStart, 1.0f);
		float dqYieldGrowth = dqYieldEnd - dqYieldStart;

		float dqDelta = FloatMath.exp(
				a0 + a2 * FloatMath.log(pspYabhStart) + a3 * dqStart
						+ a4 * dhStart + a5 * baStart + a6 * hdGrowth
		)
				+ a1 * dqYieldGrowth;

		dqDelta = Math.max(dqDelta, 0.0f);

		// Compute min/max growth in quadratic mean diameter 

		Map<Integer, Coefficients> quadMeanDiameterGrowthEmpiricalLimits = fps.fcm
				.getQuadMeanDiameterGrowthEmpiricalLimits();
		float[] dqDeltaLimits = new float[8];
		for (int i = 0; i < 8; i++) {
			dqDeltaLimits[i] = quadMeanDiameterGrowthEmpiricalLimits.get(stratumNumber).getCoe(i);
		}

		float x = dqStart - 7.5f;
		float xsq = x * x;

		var dqGrowthMin = Math.max(dqDeltaLimits[0] + dqDeltaLimits[1] * x + dqDeltaLimits[2] * xsq / 100.0f, dqDeltaLimits[6]);
		var dqGrowthMax = Math.min(dqDeltaLimits[3] + dqDeltaLimits[4] * x + dqDeltaLimits[5] * xsq / 100.0f, dqDeltaLimits[7]);

		dqGrowthMax = Math.max(dqGrowthMax, dqGrowthMin);

		// Apply the just-computed limits to the previously computed value.

		return FloatMath.clamp(dqDelta, dqGrowthMin, dqGrowthMax);
	}

	private float[] getSpeciesProportionsByBasalArea() {

		PolygonProcessingState pps = fps.getPolygonProcessingState();
		Bank start = pps.getStartBank();
		
		float[] speciesProportionsByBasalArea = new float[pps.getNSpecies() + 1];

		for (int i = 1; i <= pps.getNSpecies(); i++) {
			speciesProportionsByBasalArea[i] = start.basalAreas[i][UC_ALL_INDEX]
					/ start.basalAreas[0][UC_ALL_INDEX];
		}

		return speciesProportionsByBasalArea;
	}

	/**
	 * EMP111A - Basal area growth for the primary layer.
	 * 
	 * @param pspYabhStart at the start of the year
	 * @param pspDhStart primary species dominant height at start of year
	 * @param baStart at the start of the year
	 * @param veteranLayerBa at the start of the year
	 * @param dhDelta during the year
	 * 
	 * @return the growth in the basal area for the year
	 * @throws StandProcessingException in the event of an error
	 */
	float growBasalArea(
			float pspYabhStart, ForwardDebugSettings debugSettings, float pspDhStart,
			float baStart, Optional<Float> veteranLayerBa, float dhDelta
	) throws StandProcessingException {

		PolygonProcessingState pps = fps.getPolygonProcessingState();
		Bank start = pps.getStartBank();
		
		float[] speciesProportionsByBasalArea = getSpeciesProportionsByBasalArea();

		var baYieldCoefficients = fps.fcm.getBasalAreaYieldCoefficients();
		var becZoneAlias = fps.getPolygonProcessingState().getBecZone().getAlias();
		Coefficients estimateBasalAreaYieldCoefficients = Coefficients.empty(7, 0);
		for (int i = 0; i < 7; i++) {
			float sum = 0.0f;
			for (int speciesIndex : pps.getIndices()) {
				String speciesAlias = start.speciesNames[speciesIndex];
				sum += baYieldCoefficients.get(becZoneAlias, speciesAlias).getCoe(i)
						* speciesProportionsByBasalArea[speciesIndex];
			}
			estimateBasalAreaYieldCoefficients.setCoe(i, sum);
		}
		if (estimateBasalAreaYieldCoefficients.getCoe(5) > 0.0f) {
			estimateBasalAreaYieldCoefficients.setCoe(5, 0.0f);
		}

		boolean isFullOccupancy = true;
		int pspGroupNumber = fps.getPolygonProcessingState().getPrimarySpeciesGroupNumber();
		int debugSetting2Value = debugSettings.getValue(ForwardDebugSettings.Vars.MAX_BREAST_HEIGHT_AGE_2);

		float baYieldStart = fps.estimators.estimateBaseAreaYield(
				estimateBasalAreaYieldCoefficients, debugSetting2Value, pspDhStart, pspYabhStart, veteranLayerBa, isFullOccupancy, fps
						.getPolygonProcessingState().getBecZone(), pspGroupNumber
		);

		float pspDhEnd = pspDhStart + dhDelta;
		float pspYabhEnd = pspYabhStart + 1.0f;

		float baYieldEnd = fps.estimators.estimateBaseAreaYield(
				estimateBasalAreaYieldCoefficients, debugSetting2Value, pspDhEnd, pspYabhEnd, veteranLayerBa, isFullOccupancy, fps
						.getPolygonProcessingState().getBecZone(), pspGroupNumber
		);

		var growthFaitDetails = fps.fcm.getBasalAreaGrowthFiatDetails()
				.get(fps.getPolygonProcessingState().getBecZone().getRegion());

		var convergenceCoefficient = growthFaitDetails.calculateCoefficient(pspYabhStart);

		float baGrowth = baYieldEnd - baYieldStart;
		var adjust = -convergenceCoefficient * (baStart - baYieldStart);
		baGrowth += adjust;

		// Special check at young ages. 
		if (pspYabhStart < 40.0f && baStart > 5.0f * baYieldStart) {
			// This stand started MUCH faster than base yield model. We'll let it keep going like 
			// this for a while.
			baGrowth = Math.min(baYieldStart / pspYabhStart, Math.min(0.5f, baGrowth));
		}

		int debugSetting3Value = debugSettings.getValue(ForwardDebugSettings.Vars.BASAL_AREA_GROWTH_MODEL_3);

		if (debugSetting3Value >= 1) {
			float baGrowthFiatModel = baGrowth;

			float baGrowthEmpiricalModel = calculateBasalAreaGrowthEmpirical(
					speciesProportionsByBasalArea, baStart, pspYabhStart, pspDhStart, baYieldStart, baYieldEnd
			);

			baGrowth = baGrowthEmpiricalModel;

			if (debugSetting3Value == 2) {
				float c = 1.0f;
				if (pspYabhStart >= growthFaitDetails.getMixedCoefficient(1)) {
					c = 0.0f;
				} else if (pspYabhStart > growthFaitDetails.getMixedCoefficient(0)) {
					float t1 = pspYabhStart - growthFaitDetails.getMixedCoefficient(0);
					float t2 = growthFaitDetails.getMixedCoefficient(1) - growthFaitDetails.getMixedCoefficient(0);
					float t3 = growthFaitDetails.getMixedCoefficient(2);
					c = 1.0f - FloatMath.pow(t1 / t2, t3);
				}
				baGrowth = c * baGrowthEmpiricalModel + (1.0f - c) * baGrowthFiatModel;
			}
		}

		// UPPERGEN( 1, BATOP98, DQTOP98)
		var baUpperBound = growBasalAreaUpperBound();

		baUpperBound = baUpperBound / Estimators.EMPIRICAL_OCCUPANCY;
		var baLimit = Math.max(baUpperBound, baStart);

		// Enforce upper limit on growth
		if (baStart + baGrowth > baLimit) {
			baGrowth = Math.max(baLimit - baStart, 0.0f);
		}

		// Undocumented check to prevent negative growth that causes BA to go to less than 
		// 1.0. It is doubtful that this condition will ever occur...
		if (baGrowth < 0.0f && baStart + baGrowth < 1.0f) {
			baGrowth = -baStart + 1.0f;
		}

		return baGrowth;
	}

	/**
	 * EMP121. Calculate basal area growth using the empirical model.
	 * 
	 * @param speciesBasalAreaProportions the proportion by basal area of each of the polygon's species 
	 * @param baStart basal area of primary layer
	 * @param pspYabhStart primary species years at breast height or more
	 * @param pspDhStart primary species dominant height
	 * @param baYieldStart basal area yield at start of period
	 * @param baYieldEnd basal area yield at end of period
	 * 
	 * @return the change in primary layer basal area from start to start + 1 year
	 */
	private float calculateBasalAreaGrowthEmpirical(
			float[] speciesBasalAreaProportions, float baStart, float pspYabhStart,
			float pspDhStart, float baYieldStart, float baYieldEnd
	) {

		pspYabhStart = Math.max(pspYabhStart, 1.0f);
		if (pspYabhStart > 999.0f) {
			pspYabhStart = 999.0f;
		}

		var basalAreaGrowthEmpiricalCoefficients = fps.fcm.getBasalAreaGrowthEmpiricalCoefficients();

		String becZoneAlias = fps.getPolygonProcessingState().getBecZone().getAlias();
		String firstSpecies = fps.fcm.getGenusDefinitionMap().getByIndex(1).getAlias();
		var firstSpeciesBaGrowthCoe = basalAreaGrowthEmpiricalCoefficients.get(becZoneAlias, firstSpecies);

		float b0 = firstSpeciesBaGrowthCoe.get(0);
		float b1 = firstSpeciesBaGrowthCoe.get(1);
		float b2 = firstSpeciesBaGrowthCoe.get(2);
		float b3 = firstSpeciesBaGrowthCoe.get(3);
		float b4 = 0.0f;
		float b5 = 0.0f;
		float b6 = firstSpeciesBaGrowthCoe.get(6);
		float b7 = firstSpeciesBaGrowthCoe.get(7);

		for (int i = 1; i <= fps.getPolygonProcessingState().getNSpecies(); i++) {
			String speciesAlias = fps.getPolygonProcessingState().getStartBank().speciesNames[i];
			var baGrowthCoe = basalAreaGrowthEmpiricalCoefficients.get(becZoneAlias, speciesAlias);
			b4 += speciesBasalAreaProportions[i] * baGrowthCoe.getCoe(4);
			b5 += speciesBasalAreaProportions[i] * baGrowthCoe.getCoe(5);
		}

		b4 = Math.max(b4, 0.0f);
		b5 = Math.min(b5, 0.0f);

		float term1;
		if (pspDhStart > b0) {
			term1 = 1.0f - FloatMath.exp(b1 * (pspDhStart - b0));
		} else {
			term1 = 0.0f;
		}

		float fLogIt = -0.05f * (pspYabhStart - 350.0f);
		float term2a = FloatMath.exp(fLogIt) / (1.0f + FloatMath.exp(fLogIt));
		float term2 = b2 * FloatMath.pow(pspDhStart / 20.0f, b3) * term2a;
		float term3 = b4 * FloatMath.exp(b5 * pspYabhStart);

		float term4;
		float basalAreaYieldDelta = baYieldEnd - baYieldStart;
		if (basalAreaYieldDelta > 0.0) {
			term4 = b6 * FloatMath.pow(basalAreaYieldDelta, b7);
		} else {
			term4 = 0.0f;
		}

		float basalAreaDelta = term1 * (term2 + term3) + term4;

		// An undocumented check to prevent negative growth that causes BA to go to 
		// less than 1.0. It is doubtful that this condition will ever occur.
		if (basalAreaDelta < 0.0 && baStart + basalAreaDelta < 1.0f) {
			basalAreaDelta = -baStart + 1.0f;
		}

		return basalAreaDelta;
	}

	/**
	 * UPPERGEN(1, BATOP98, DQTOP98) for basal area
	 */
	private float growBasalAreaUpperBound() {

		PolygonProcessingState pps = fps.getPolygonProcessingState();
		
		int debugSetting4Value = fps.fcm.getDebugSettings()
				.getValue(ForwardDebugSettings.Vars.PER_SPECIES_AND_REGION_MAX_BREAST_HEIGHT_4);
		if (debugSetting4Value > 0) {
			var upperBoundsCoefficients = fps.fcm.getUpperBoundsCoefficients();
			Region region = pps.getBecZone().getRegion();
			int primarySpeciesIndex = pps.getPrimarySpeciesIndex();
			return upperBoundsCoefficients
					.get(region, pps.getStartBank().speciesNames[primarySpeciesIndex], 1);
		} else {
			var primarySpeciesGroupNumber = pps.getPrimarySpeciesGroupNumber();
			return fps.fcm.getUpperBounds().get(primarySpeciesGroupNumber).getCoe(UpperBoundsParser.BA_INDEX);
		}
	}

	/**
	 * UPPERGEN(1, BATOP98, DQTOP98) for quad-mean-diameter
	 */
	private float growQuadraticMeanDiameterUpperBound() {

		PolygonProcessingState pps = fps.getPolygonProcessingState();

		int debugSetting4Value = fps.fcm.getDebugSettings()
				.getValue(ForwardDebugSettings.Vars.PER_SPECIES_AND_REGION_MAX_BREAST_HEIGHT_4);
		if (debugSetting4Value > 0) {
			
			var upperBoundsCoefficients = fps.fcm.getUpperBoundsCoefficients();
			Region region = pps.getBecZone().getRegion();
			int primarySpeciesIndex = pps.getPrimarySpeciesIndex();
			return upperBoundsCoefficients
					.get(region, pps.getStartBank().speciesNames[primarySpeciesIndex], 2);
		} else {
			var primarySpeciesGroupNumber = fps.getPolygonProcessingState().getPrimarySpeciesGroupNumber();
			return fps.fcm.getUpperBounds().get(primarySpeciesGroupNumber).getCoe(UpperBoundsParser.DQ_INDEX);
		}
	}

	/**
	 * HDGROW - calculate growth in dominant height from the current dominant height and the parameters
	 * needed to do the computation
	 * 
	 * @param spDhStart dominant height at the start of the growth period
	 * @param siteCurveNumber site curve number, used to find the site index equation
	 * @param siStart 
	 * @param yearsToBreastHeight for the given species
	 * 
	 * @return the difference in dominant height from the beginning to the end of the growth period
	 * @throws ProcessingException
	 */
	float growDominantHeight(
			float spDhStart, int siteCurveNumber, float siStart, float yearsToBreastHeight
	) throws ProcessingException {

		SiteCurveAgeMaximum scAgeMaximums = fps.fcm.getMaximumAgeBySiteCurveNumber().get(siteCurveNumber);
		Region region = fps.getPolygonProcessingState().getStartBank().getBecZone().getRegion();

		if (siteCurveNumber == VdypEntity.MISSING_INTEGER_VALUE) {
			throw new ProcessingException("No SiteCurveNumber supplied");
		}

		var siteIndexEquation = SiteIndexEquation.getByIndex(siteCurveNumber);

		if (spDhStart <= 1.3) {
			throw new ProcessingException(
					MessageFormat.format(
							"(current) DominantHeight {0} is out of range (must be above 1.3)", spDhStart
					)
			);
		}

		final SiteIndexAgeType ageType = SiteIndexAgeType.SI_AT_BREAST;

		double siStart_d = siStart;
		double dhStart_d = spDhStart;
		double yearsToBreastHeight_d = yearsToBreastHeight;

		double ageStart;
		try {
			ageStart = SiteTool.heightAndSiteIndexToAge(
					siteIndexEquation, dhStart_d, ageType, siStart_d, yearsToBreastHeight_d
			);
		} catch (CommonCalculatorException e) {
			throw new ProcessingException(
					MessageFormat.format(
							"Encountered exception when calling heightAndSiteIndexToAge({0}, {1}, {2}, {3}, {4})", siteIndexEquation, dhStart_d, ageType, siStart_d, yearsToBreastHeight_d
					), e
			);
		}

		if (ageStart <= 0.0d) {
			if (dhStart_d > siStart_d) {
				return 0.0f /* no growth */;
			} else {
				throw new ProcessingException(
						MessageFormat.format("currentBreastHeightAge value {0} must be positive", ageStart)
				);
			}
		}

		double ageEnd = ageStart + 1.0f;

		// If we are past the total age limit for site curve, assign no growth. If we are almost there, go 
		// slightly past the limit (by .01 yr). Once there, we should stop growing. The TOTAL age limit was 
		// stored so we must calculate a BH age limit first...

		float ageLimitInYears = scAgeMaximums.getAgeMaximum(region);

		float breastHeightAgeLimitInYears = 0.0f;
		if (ageLimitInYears > 0) {
			breastHeightAgeLimitInYears = ageLimitInYears - yearsToBreastHeight;
		}

		if (ageStart <= breastHeightAgeLimitInYears || scAgeMaximums.getT1() <= 0.0f) {

			float yearPart = 1.0f;

			if (scAgeMaximums.getT1() <= 0.0f) {

				if (breastHeightAgeLimitInYears > 0.0f && ageEnd > breastHeightAgeLimitInYears) {
					if (ageStart > breastHeightAgeLimitInYears) {
						return 0.0f /* no growth */;
					}

					yearPart = (float) (breastHeightAgeLimitInYears - ageStart + 0.01);
					ageEnd = ageStart + yearPart;
				}
			}

			// The above code to find ages allows errors up to .005 m. At high ages with some 
			// species this can correspond to a half year. Therefore, AGED1 can not be assumed to 
			// correspond to HDD1. Find a new HDD1 to at least get the increment correct.

			double currentDominantHeight = ageAndSiteIndexToHeight(
					siteIndexEquation, ageStart, ageType, siStart_d, yearsToBreastHeight_d
			);

			double nextDominantHeight = ageAndSiteIndexToHeight(
					siteIndexEquation, ageEnd, ageType, siStart_d, yearsToBreastHeight_d, r -> r >= 0.0
			);

			if (nextDominantHeight < currentDominantHeight && yearPart == 1.0) {
				// Rounding error in site routines?
				if (Math.abs(currentDominantHeight - nextDominantHeight) < 0.01) {
					return 0.0f /* no growth */;
				} else {
					throw new ProcessingException(
							MessageFormat.format(
									"New dominant height {0} is less than the current dominant height {1}", nextDominantHeight, currentDominantHeight
							)
					);
				}
			}

			return (float) (nextDominantHeight - currentDominantHeight);

		} else {
			// We are in a special extension of the curve. Derive the new curve form and then 
			// compute the answer.

			double breastHeightAgeLimitInYears_d = breastHeightAgeLimitInYears;

			double currentDominantHeight = ageAndSiteIndexToHeight(
					siteIndexEquation, breastHeightAgeLimitInYears_d, ageType, siStart_d, yearsToBreastHeight_d
			);

			breastHeightAgeLimitInYears_d += 1.0;

			double nextDominantHeight = ageAndSiteIndexToHeight(
					siteIndexEquation, breastHeightAgeLimitInYears_d, ageType, siStart_d, yearsToBreastHeight_d
			);

			float rate = (float) (nextDominantHeight - currentDominantHeight);
			if (rate < 0.0005f) {
				rate = 0.0005f;
			}

			float a = FloatMath.log(0.5f) / scAgeMaximums.getT1();
			float y = (float) currentDominantHeight;
			// Model is:
			//     Y = y - rate/a * (1 - exp(a * t)) where t = AGE - BHAGELIM
			// Solve for t:
			//	   1 - exp(a * t) = (y - dominantHeight) * a/rate
			//	   -exp(a * t) = (y - dominantHeight) * a/rate - 1
			//	   exp(a * t) = (dominantHeight - y) * a/rate + 1
			//	   a * t = ln(1 + (dominantHeight - y) * a/rate)
			//	   t = ln(1 + (dominantHeight - y) * a/rate) / a
			float t;
			if (spDhStart > y) {
				float term = 1.0f + (spDhStart - y) * a / rate;
				if (term <= 1.0e-7) {
					return 0.0f;
				}
				t = FloatMath.log(term) / a;
			} else {
				t = 0.0f;
			}

			if (t > scAgeMaximums.getT2()) {
				return 0.0f;
			} else {
				return rate / a * (-FloatMath.exp(a * t) + FloatMath.exp(a * (t + 1.0f)));
			}
		}
	}

	private static double ageAndSiteIndexToHeight(
			SiteIndexEquation curve, double age, SiteIndexAgeType ageType, double siteIndex, double years2BreastHeight,
			Function<Double, Boolean> checkResultValidity
	) throws ProcessingException {
		Double r = ageAndSiteIndexToHeight(curve, age, ageType, siteIndex, years2BreastHeight);
		if (!checkResultValidity.apply(r)) {
			throw new ProcessingException(
					MessageFormat.format(
							"SiteTool.ageAndSiteIndexToHeight({0}, {1}, {2}, {3}, {4}) returned {5}", curve, age, ageType, siteIndex, years2BreastHeight, r
					)
			);
		}

		return r;
	}

	private static double ageAndSiteIndexToHeight(
			SiteIndexEquation curve, double age, SiteIndexAgeType ageType, double siteIndex, double years2BreastHeight
	) throws ProcessingException {
		try {
			return SiteTool.ageAndSiteIndexToHeight(
					curve, age, ageType, siteIndex, years2BreastHeight
			);
		} catch (CommonCalculatorException e) {
			throw new ProcessingException(
					MessageFormat.format(
							"SiteTool.ageAndSiteIndexToHeight({0}, {1}, {2}, {3}, {4}) threw exception", curve, age, ageType, siteIndex, years2BreastHeight
					), e
			);
		}
	}

	private void writeLayers(Bank endBank, int veteranLayerInstance, boolean b) {

		logger.info(
				"Writing primary layer, and veteran layer from instance {}", endBank, veteranLayerInstance
		);
	}

	private static final float[] DEFAULT_QUAD_MEAN_DIAMETERS = new float[] { Float.NaN, 10.0f, 15.0f, 20.0f, 25.0f };
	private static final float V_BASE_MIN = 0.1f;
	private static final float B_BASE_MIN = 0.01f;

	@SuppressWarnings("unchecked")
	void setCompatibilityVariables() throws ProcessingException {
		
		Coefficients aAdjust = new Coefficients(new float[] { 0.0f, 0.0f, 0.0f, 0.0f }, 1);

		var growthDetails = fps.fcm.getForwardControlVariables();
		var pps = fps.getPolygonProcessingState();
		Bank start = pps.getStartBank();
		
		// Note: L1COM2 (INL1VGRP, INL1DGRP, INL1BGRP) is initialized when
		// PolygonProcessingState (volumeEquationGroups, decayEquationGroups
		// breakageEquationGroups, respectively) is constructed. Copying
		// the values into LCOM1 is not necessary. Note, however, that
		// VolumeEquationGroup 10 is mapped to 11 (VGRPFIND) - this is done
		// when volumeEquationGroups is built (i.e., when the equivalent to
		// INL1VGRP is built, rather than when LCOM1 VGRPL is built in the
		// original code.)

		var cvVolume = new MatrixMap3[pps.getNSpecies() + 1];
		var cvBasalArea = new MatrixMap2[pps.getNSpecies() + 1];
		var cvQuadraticMeanDiameter = new MatrixMap2[pps.getNSpecies() + 1];
		var cvSmall = new HashMap[pps.getNSpecies() + 1];

		for (int s = 1; s <= pps.getNSpecies(); s++) {

			String genusName = start.speciesNames[s];

			float spLoreyHeight_All = start.loreyHeights[s][UtilizationClass.ALL.ordinal()];

			Coefficients basalAreas = Utils.utilizationVector();
			Coefficients wholeStemVolumes = Utils.utilizationVector();
			Coefficients closeUtilizationVolumes = Utils.utilizationVector();
			Coefficients closeUtilizationVolumesNetOfDecay = Utils.utilizationVector();
			Coefficients closeUtilizationVolumesNetOfDecayAndWaste = Utils.utilizationVector();
			Coefficients quadMeanDiameters = Utils.utilizationVector();
			Coefficients treesPerHectare = Utils.utilizationVector();

			cvVolume[s] = new MatrixMap3Impl<UtilizationClass, VolumeVariable, LayerType, Float>(
					UtilizationClass.UTIL_CLASSES, VolumeVariable.ALL, LayerType.ALL_USED, (k1, k2, k3) -> 0f
			);
			cvBasalArea[s] = new MatrixMap2Impl<UtilizationClass, LayerType, Float>(
					UtilizationClass.UTIL_CLASSES, LayerType.ALL_USED, (k1, k2) -> 0f
			);
			cvQuadraticMeanDiameter[s] = new MatrixMap2Impl<UtilizationClass, LayerType, Float>(
					UtilizationClass.UTIL_CLASSES, LayerType.ALL_USED, (k1, k2) -> 0f
			);

			for (UtilizationClass uc : UtilizationClass.ALL_BUT_SMALL) {

				basalAreas.setCoe(uc.index, start.basalAreas[s][uc.ordinal()]);
				wholeStemVolumes.setCoe(uc.index, start.wholeStemVolumes[s][uc.ordinal()]);
				closeUtilizationVolumes.setCoe(uc.index, start.closeUtilizationVolumes[s][uc.ordinal()]);
				closeUtilizationVolumesNetOfDecay.setCoe(uc.index, start.cuVolumesMinusDecay[s][uc.ordinal()]);
				closeUtilizationVolumesNetOfDecayAndWaste
						.setCoe(uc.index, start.cuVolumesMinusDecayAndWastage[s][uc.ordinal()]);

				quadMeanDiameters.setCoe(uc.index, start.quadMeanDiameters[s][uc.ordinal()]);
				if (uc != UtilizationClass.ALL && quadMeanDiameters.getCoe(uc.index) <= 0.0f) {
					quadMeanDiameters.setCoe(uc.index, DEFAULT_QUAD_MEAN_DIAMETERS[uc.ordinal()]);
				}
			}

			for (UtilizationClass uc : UtilizationClass.UTIL_CLASSES) {

				float adjustment;
				float baseVolume;

				// Volume less decay and waste
				adjustment = 0.0f;
				baseVolume = start.cuVolumesMinusDecay[s][uc.ordinal()];

				if (growthDetails.allowCalculation(baseVolume, V_BASE_MIN, (l, r) -> l > r)) {

					// EMP094
					fps.estimators.estimateNetDecayAndWasteVolume(
							pps.getBecZone()
									.getRegion(), uc, aAdjust, start.speciesNames[s], spLoreyHeight_All, quadMeanDiameters, closeUtilizationVolumes, closeUtilizationVolumesNetOfDecay, closeUtilizationVolumesNetOfDecayAndWaste
					);

					float actualVolume = start.cuVolumesMinusDecayAndWastage[s][uc.ordinal()];
					float staticVolume = closeUtilizationVolumesNetOfDecayAndWaste.getCoe(uc.index);
					adjustment = calculateCompatibilityVariable(actualVolume, baseVolume, staticVolume);
				}

				cvVolume[s]
						.put(uc, VolumeVariable.CLOSE_UTIL_VOL_LESS_DECAY_LESS_WASTAGE, LayerType.PRIMARY, adjustment);

				// Volume less decay
				adjustment = 0.0f;
				baseVolume = start.closeUtilizationVolumes[s][uc.ordinal()];

				if (growthDetails.allowCalculation(baseVolume, V_BASE_MIN, (l, r) -> l > r)) {

					// EMP093
					int decayGroup = pps.decayEquationGroups[s];
					fps.estimators.estimateNetDecayVolume(
							start.speciesNames[s], pps.getBecZone().getRegion(), uc, aAdjust, decayGroup, pps
									.getPrimarySpeciesAgeAtBreastHeight(), quadMeanDiameters, closeUtilizationVolumes, closeUtilizationVolumesNetOfDecay
					);

					float actualVolume = start.cuVolumesMinusDecay[s][uc.ordinal()];
					float staticVolume = closeUtilizationVolumesNetOfDecay.getCoe(uc.index);
					adjustment = calculateCompatibilityVariable(actualVolume, baseVolume, staticVolume);
				}

				cvVolume[s].put(uc, VolumeVariable.CLOSE_UTIL_VOL_LESS_DECAY, LayerType.PRIMARY, adjustment);

				// Volume
				adjustment = 0.0f;
				baseVolume = start.wholeStemVolumes[s][uc.ordinal()];

				if (growthDetails.allowCalculation(baseVolume, V_BASE_MIN, (l, r) -> l > r)) {

					// EMP092
					int volumeGroup = pps.volumeEquationGroups[s];
					fps.estimators.estimateCloseUtilizationVolume(
							uc, aAdjust, volumeGroup, spLoreyHeight_All, quadMeanDiameters, wholeStemVolumes, closeUtilizationVolumes
					);

					float actualVolume = start.closeUtilizationVolumes[s][uc.ordinal()];
					float staticVolume = closeUtilizationVolumes.getCoe(uc.index);
					adjustment = calculateCompatibilityVariable(actualVolume, baseVolume, staticVolume);
				}

				cvVolume[s].put(uc, VolumeVariable.CLOSE_UTIL_VOL, LayerType.PRIMARY, adjustment);
			}

			int primarySpeciesVolumeGroup = pps.volumeEquationGroups[s];
			float primarySpeciesQMDAll = start.quadMeanDiameters[s][UC_ALL_INDEX];
			var wholeStemVolume = start.treesPerHectare[s][UC_ALL_INDEX]
					* fps.estimators.estimateWholeStemVolumePerTree(
							primarySpeciesVolumeGroup, spLoreyHeight_All, primarySpeciesQMDAll
					);

			wholeStemVolumes.setCoe(UC_ALL_INDEX, wholeStemVolume);

			fps.estimators.estimateWholeStemVolume(
					UtilizationClass.ALL, 0.0f, primarySpeciesVolumeGroup, spLoreyHeight_All, quadMeanDiameters, basalAreas, wholeStemVolumes
			);

			for (UtilizationClass uc : UtilizationClass.UTIL_CLASSES) {
				float adjustment = 0.0f;
				float basalArea = basalAreas.getCoe(uc.index);
				if (growthDetails.allowCalculation(basalArea, B_BASE_MIN, (l, r) -> l > r)) {
					adjustment = calculateWholeStemVolume(
							start.wholeStemVolumes[s][uc.ordinal()], basalArea, wholeStemVolumes.getCoe(uc.index)
					);
				}

				cvVolume[s].put(uc, VolumeVariable.WHOLE_STEM_VOL, LayerType.PRIMARY, adjustment);
			}

			fps.estimators.estimateQuadMeanDiameterByUtilization(
					pps.getBecZone(), quadMeanDiameters, genusName
			);

			fps.estimators.estimateBaseAreaByUtilization(
					pps.getBecZone(), quadMeanDiameters, basalAreas, genusName
			);

			// Calculate trees-per-hectare per utilization
			treesPerHectare.setCoe(UtilizationClass.ALL.index, start.treesPerHectare[s][UC_ALL_INDEX]);
			for (UtilizationClass uc : UtilizationClass.UTIL_CLASSES) {
				treesPerHectare.setCoe(
						uc.index, calculateTreesPerHectare(
								basalAreas.getCoe(uc.index), quadMeanDiameters.getCoe(uc.index)
						)
				);
			}

			ReconcilationMethods.reconcileComponents(basalAreas, treesPerHectare, quadMeanDiameters);

			for (UtilizationClass uc : UtilizationClass.UTIL_CLASSES) {
				float baCvValue = start.basalAreas[s][uc.ordinal()] - basalAreas.getCoe(uc.index);
				cvBasalArea[s].put(uc, LayerType.PRIMARY, baCvValue);

				float originalQmd = start.quadMeanDiameters[s][uc.ordinal()];
				float adjustedQmd = quadMeanDiameters.getCoe(uc.index);

				float qmdCvValue;
				if (growthDetails.allowCalculation(() -> originalQmd < B_BASE_MIN)) {
					qmdCvValue = 0.0f;
				} else if (originalQmd > 0 && adjustedQmd > 0) {
					qmdCvValue = originalQmd - adjustedQmd;
				} else {
					qmdCvValue = 0.0f;
				}

				cvQuadraticMeanDiameter[s].put(uc, LayerType.PRIMARY, qmdCvValue);
			}

			// Small components

			cvSmall[s] = calculateSmallCompatibilityVariables(s, growthDetails);
		}

		pps.setCompatibilityVariableDetails(cvVolume, cvBasalArea, cvQuadraticMeanDiameter, cvSmall);
	}

	/**
	 * Calculate the small component compatibility variables.
	 * 
	 * @param speciesIndex the index of the species for which this operation is to be performed
	 * @param forwardControlVariables the control variables for this run
	 *
	 * @throws ProcessingException
	 */
	private HashMap<UtilizationClassVariable, Float>
			calculateSmallCompatibilityVariables(int speciesIndex, ForwardControlVariables forwardControlVariables)
					throws ProcessingException {

		var pps = fps.getPolygonProcessingState();
		Bank start = pps.getStartBank();

		Region region = pps.getPolygon().getBiogeoclimaticZone().getRegion();
		String speciesName = start.speciesNames[speciesIndex];

		float spLoreyHeight_All = start.loreyHeights[speciesIndex][UC_ALL_INDEX]; // HLsp
		float spQuadMeanDiameter_All = start.quadMeanDiameters[speciesIndex][UC_ALL_INDEX]; // DQsp

		// this WHOLE operation on Actual BA's, not 100% occupancy.
		// TODO: verify this: float fractionAvailable = polygon.getPercentForestLand();
		float spBaseArea_All = start.basalAreas[speciesIndex][UC_ALL_INDEX] /* * fractionAvailable */; // BAsp

		// EMP080
		float smallProbability = smallComponentProbability(speciesName, spLoreyHeight_All, region); // PROBsp

		// EMP081
		float conditionalExpectedBaseArea = conditionalExpectedBaseArea(
				speciesName, spBaseArea_All, spLoreyHeight_All, region
		); // BACONDsp

		// TODO (see previous TODO): conditionalExpectedBaseArea /= fractionAvailable;

		float baSmall = smallProbability * conditionalExpectedBaseArea;

		// EMP082
		float qmdSmall = smallComponentQuadMeanDiameter(speciesName, spLoreyHeight_All); // DQSMsp

		// EMP085
		float lhSmall = smallComponentLoreyHeight(
				speciesName, spLoreyHeight_All, qmdSmall, spQuadMeanDiameter_All
		); // HLSMsp

		// EMP086
		float meanVolumeSmall = meanVolumeSmall(speciesName, qmdSmall, lhSmall); // VMEANSMs

		var cvSmall = new HashMap<UtilizationClassVariable, Float>();

		float spInputBasalArea_Small = start.basalAreas[speciesIndex][UC_SMALL_INDEX];
		cvSmall.put(UtilizationClassVariable.BASAL_AREA, spInputBasalArea_Small - baSmall);

		if (forwardControlVariables.allowCalculation(spInputBasalArea_Small, B_BASE_MIN, (l, r) -> l > r)) {
			float spInputQuadMeanDiameter_Small = start.quadMeanDiameters[speciesIndex][UC_SMALL_INDEX];
			cvSmall.put(
					UtilizationClassVariable.QUAD_MEAN_DIAMETER, spInputQuadMeanDiameter_Small
							- qmdSmall
			);
		} else {
			cvSmall.put(UtilizationClassVariable.QUAD_MEAN_DIAMETER, 0.0f);
		}

		float spInputLoreyHeight_Small = start.loreyHeights[speciesIndex][UC_SMALL_INDEX];
		if (spInputLoreyHeight_Small > 1.3f && lhSmall > 1.3f && spInputBasalArea_Small > 0.0f) {
			float cvLoreyHeight = FloatMath.log( (spInputLoreyHeight_Small - 1.3f) / (lhSmall - 1.3f));
			cvSmall.put(UtilizationClassVariable.LOREY_HEIGHT, cvLoreyHeight);
		} else {
			cvSmall.put(UtilizationClassVariable.LOREY_HEIGHT, 0.0f);
		}

		float spInputWholeStemVolume_Small = start.wholeStemVolumes[speciesIndex][UC_SMALL_INDEX];
		if (spInputWholeStemVolume_Small > 0.0f && meanVolumeSmall > 0.0f
				&& forwardControlVariables.allowCalculation(spInputBasalArea_Small, B_BASE_MIN, (l, r) -> l >= r)) {

			float spInputTreePerHectare_Small = start.treesPerHectare[speciesIndex][UC_SMALL_INDEX];

			var wsVolumeSmall = FloatMath
					.log(spInputWholeStemVolume_Small / spInputTreePerHectare_Small / meanVolumeSmall);
			cvSmall.put(UtilizationClassVariable.WHOLE_STEM_VOLUME, wsVolumeSmall);

		} else {
			cvSmall.put(UtilizationClassVariable.WHOLE_STEM_VOLUME, 0.0f);
		}

		return cvSmall;
	}

	// EMP080
	private float smallComponentProbability(
			String speciesName, float loreyHeight, Region region
	) {
		PolygonProcessingState pps = fps.getPolygonProcessingState();

		Coefficients coe = fps.fcm.getSmallComponentProbabilityCoefficients().get(speciesName);

		// EQN 1 in IPSJF118.doc

		float a0 = coe.getCoe(1);
		float a1 = coe.getCoe(2);
		float a2 = coe.getCoe(3);
		float a3 = coe.getCoe(4);

		a1 = (region == Region.COASTAL) ? a1 : 0.0f;

		float logit = a0 + //
				a1 + //
				a2 * pps.getStartBank().yearsAtBreastHeight[pps.getPrimarySpeciesIndex()] + //
				a3 * loreyHeight;

		return exp(logit) / (1.0f + exp(logit));
	}

	// EMP081
	private float conditionalExpectedBaseArea(
			String speciesName, float basalArea, float loreyHeight, Region region
	) {
		Coefficients coe = fps.fcm.getSmallComponentBasalAreaCoefficients().get(speciesName);

		// EQN 3 in IPSJF118.doc

		float a0 = coe.getCoe(1);
		float a1 = coe.getCoe(2);
		float a2 = coe.getCoe(3);
		float a3 = coe.getCoe(4);

		float coast = region == Region.COASTAL ? 1.0f : 0.0f;

		// FIXME due to a bug in VDYP7 it always treats this as interior. Replicating
		// that for now.
		coast = 0f;

		float arg = (a0 + a1 * coast + a2 * basalArea) * exp(a3 * loreyHeight);
		arg = max(arg, 0f);

		return arg;
	}

	// EMP082
	private float smallComponentQuadMeanDiameter(String speciesName, float loreyHeight) {
		Coefficients coe = fps.fcm.getSmallComponentQuadMeanDiameterCoefficients().get(speciesName);

		// EQN 5 in IPSJF118.doc

		float a0 = coe.getCoe(1);
		float a1 = coe.getCoe(2);

		float logit = a0 + a1 * loreyHeight;

		return 4.0f + 3.5f * exp(logit) / (1.0f + exp(logit));
	}

	// EMP085
	private float smallComponentLoreyHeight(
			String speciesName, float speciesLoreyHeight_All, float quadMeanDiameterSpecSmall,
			float speciesQuadMeanDiameter_All
	) {
		Coefficients coe = fps.fcm.getSmallComponentLoreyHeightCoefficients().get(speciesName);

		// EQN 1 in IPSJF119.doc

		float a0 = coe.getCoe(1);
		float a1 = coe.getCoe(2);

		return 1.3f + (speciesLoreyHeight_All - 1.3f) //
				* exp(a0 * (pow(quadMeanDiameterSpecSmall, a1) - pow(speciesQuadMeanDiameter_All, a1)));
	}

	// EMP086
	private float meanVolumeSmall(
			String speciesName, float quadMeanDiameterSpecSmall, float loreyHeightSpecSmall
	) {
		Coefficients coe = fps.fcm.getSmallComponentWholeStemVolumeCoefficients().get(speciesName);

		// EQN 1 in IPSJF119.doc

		float a0 = coe.getCoe(1);
		float a1 = coe.getCoe(2);
		float a2 = coe.getCoe(3);
		float a3 = coe.getCoe(4);

		return exp(
				a0 + a1 * log(quadMeanDiameterSpecSmall) + a2 * log(loreyHeightSpecSmall)
						+ a3 * quadMeanDiameterSpecSmall
		);
	}

	public static float calculateTreesPerHectare(float basalArea, float qmd) {
		if (qmd == 0.0f || Float.isNaN(qmd) || Float.isNaN(basalArea)) {
			return 0.0f;
		} else {
			// basalArea is in m**2/hectare. qmd is diameter in cm. pi/4 converts between
			// diameter in cm and area in cm**2 since a = pi * r**2 = pi * (d/2)**2
			// = pi/4 * d**2. Finally, dividing by 10000 converts from cm**2 to m**2.

			return basalArea / PI_40K / (qmd * qmd);
		}
	}

	public static float calculateBasalArea(float qmd, float treesPerHectare) {

		if (Float.isNaN(qmd) || Float.isNaN(treesPerHectare)) {
			return 0.0f;
		} else {
			// qmd is diameter in cm (per tree); qmd**2 is in cm**2. Multiplying by pi/4 converts
			// to area in cm**2. Dividing by 10000 converts into m**2. Finally, multiplying
			// by trees-per-hectare takes the per-tree area and converts it into a per-hectare
			// area - that is, the basal area per hectare.

			return qmd * qmd * PI_40K * treesPerHectare;
		}
	}

	public static float calculateQuadMeanDiameter(float basalArea, float treesPerHectare) {

		if (basalArea > 1.0e6 || basalArea == 0.0 || Float.isNaN(basalArea) || treesPerHectare > 1.0e6
				|| treesPerHectare == 0.0 || Float.isNaN(treesPerHectare)) {
			return 0.0f;
		} else {
			// See comments above explaining this calculation
			return FloatMath.sqrt(basalArea / treesPerHectare / PI_40K);
		}
	}

	private static float calculateCompatibilityVariable(float actualVolume, float baseVolume, float staticVolume) {

		float staticRatio = staticVolume / baseVolume;
		float staticLogit;
		if (staticRatio <= 0.0f) {
			staticLogit = -7.0f;
		} else if (staticRatio >= 1.0f) {
			staticLogit = 7.0f;
		} else {
			staticLogit = clamp(log(staticRatio / (1.0f - staticRatio)), -7.0f, 7.0f);
		}

		float actualRatio = actualVolume / baseVolume;
		float actualLogit;
		if (actualRatio <= 0.0f) {
			actualLogit = -7.0f;
		} else if (actualRatio >= 1.0f) {
			actualLogit = 7.0f;
		} else {
			actualLogit = clamp(log(actualRatio / (1.0f - actualRatio)), -7.0f, 7.0f);
		}

		return actualLogit - staticLogit;
	}

	private static float calculateWholeStemVolume(float actualVolume, float basalArea, float staticVolume) {

		float staticRatio = staticVolume / basalArea;
		float staticLogit;
		if (staticRatio <= 0.0f) {
			staticLogit = -2.0f;
		} else {
			staticLogit = log(staticRatio);
		}

		float actualRatio = actualVolume / basalArea;
		float actualLogit;
		if (actualRatio <= 0.0f) {
			actualLogit = -2.0f;
		} else {
			actualLogit = log(actualRatio);
		}

		return actualLogit - staticLogit;
	}

	/**
	 * VHDOM1 METH_H = 2, METH_A = 2, METH_SI = 2
	 * 
	 * @param pps
	 * @param hl1Coefficients
	 * @throws ProcessingException
	 */
	static void calculateDominantHeightAgeSiteIndex(
			PolygonProcessingState pps, MatrixMap2<String, Region, Coefficients> hl1Coefficients
	) throws ProcessingException {

		Bank startBank = pps.getStartBank();
		
		// Calculate primary species values
		int primarySpeciesIndex = pps.getPrimarySpeciesIndex();

		// (1) Dominant Height
		float primarySpeciesDominantHeight = startBank.dominantHeights[primarySpeciesIndex];
		if (Float.isNaN(primarySpeciesDominantHeight)) {
			float loreyHeight = startBank.loreyHeights[primarySpeciesIndex][UC_ALL_INDEX];
			if (Float.isNaN(loreyHeight)) {
				throw new ProcessingException(
						MessageFormat.format(
								"Neither dominant nor lorey height[All] is available for primary species {}", startBank.speciesNames[primarySpeciesIndex]
						), 2
				);
			}

			// Estimate dominant height from the lorey height
			String primarySpeciesAlias = startBank.speciesNames[primarySpeciesIndex];
			Region primarySpeciesRegion = pps.getBecZone().getRegion();

			var coefficients = hl1Coefficients.get(primarySpeciesAlias, primarySpeciesRegion);
			float a0 = coefficients.getCoe(1);
			float a1 = coefficients.getCoe(2);
			float a2 = coefficients.getCoe(3);

			float treesPerHectare = startBank.treesPerHectare[primarySpeciesIndex][UC_ALL_INDEX];
			float hMult = a0 - a1 + a1 * FloatMath.exp(a2 * (treesPerHectare - 100.0f));

			primarySpeciesDominantHeight = 1.3f + (loreyHeight - 1.3f) / hMult;
		}

		// (2) Age (total, years at breast height, years to breast height
		float primarySpeciesTotalAge = startBank.ageTotals[primarySpeciesIndex];
		float primarySpeciesYearsAtBreastHeight = startBank.yearsAtBreastHeight[primarySpeciesIndex];
		float primarySpeciesYearsToBreastHeight = startBank.yearsToBreastHeight[primarySpeciesIndex];

		Optional<Integer> activeIndex = Optional.empty();

		if (Float.isNaN(primarySpeciesTotalAge)) {

			if (pps.hasSecondarySpeciesIndex()
					&& !Float.isNaN(startBank.ageTotals[pps.getSecondarySpeciesIndex()])) {
				activeIndex = Optional.of(pps.getSecondarySpeciesIndex());
			} else {
				for (int i = 1; i <= pps.getNSpecies(); i++) {
					if (!Float.isNaN(startBank.ageTotals[i])) {
						activeIndex = Optional.of(i);
						break;
					}
				}
			}

			activeIndex.orElseThrow(() -> new ProcessingException("Age data unavailable for ALL species", 5));

			primarySpeciesTotalAge = startBank.ageTotals[activeIndex.get()];
			if (!Float.isNaN(primarySpeciesYearsToBreastHeight)) {
				primarySpeciesYearsAtBreastHeight = primarySpeciesTotalAge - primarySpeciesYearsToBreastHeight;
			} else if (!Float.isNaN(primarySpeciesYearsAtBreastHeight)) {
				primarySpeciesYearsToBreastHeight = primarySpeciesTotalAge - primarySpeciesYearsAtBreastHeight;
			} else {
				primarySpeciesYearsAtBreastHeight = startBank.yearsAtBreastHeight[activeIndex.get()];
				primarySpeciesYearsToBreastHeight = startBank.yearsToBreastHeight[activeIndex.get()];
			}
		}

		// (3) Site Index
		float primarySpeciesSiteIndex = startBank.siteIndices[primarySpeciesIndex];
		if (Float.isNaN(primarySpeciesSiteIndex)) {

			if (pps.hasSecondarySpeciesIndex()
					&& !Float.isNaN(startBank.siteIndices[pps.getSecondarySpeciesIndex()])) {
				activeIndex = Optional.of(pps.getSecondarySpeciesIndex());
			} else {
				if (activeIndex.isEmpty() || Float.isNaN(startBank.siteIndices[activeIndex.get()])) {
					for (int i = 1; i <= pps.getNSpecies(); i++) {
						if (!Float.isNaN(startBank.siteIndices[i])) {
							activeIndex = Optional.of(i);
							break;
						}
					}
				}
			}
			primarySpeciesSiteIndex = startBank.siteIndices[activeIndex
					.orElseThrow(() -> new ProcessingException("Site Index data unavailable for ALL species", 7))];
		} else {
			activeIndex = Optional.of(primarySpeciesIndex);
		}

		SiteIndexEquation siteCurve1 = SiteIndexEquation.getByIndex(pps.getSiteCurveNumber(activeIndex.get()));
		SiteIndexEquation siteCurve2 = SiteIndexEquation.getByIndex(pps.getSiteCurveNumber(0));

		try {
			double newSI = SiteTool.convertSiteIndexBetweenCurves(siteCurve1, primarySpeciesSiteIndex, siteCurve2);
			if (newSI > 1.3) {
				primarySpeciesSiteIndex = (float) newSI;
			}
		} catch (CommonCalculatorException e) {
			// do nothing. primarySpeciesSiteIndex will not be modified.
		}

		pps.setPrimarySpeciesDetails(
				new PrimarySpeciesDetails(
						primarySpeciesDominantHeight, primarySpeciesSiteIndex, primarySpeciesTotalAge,
						primarySpeciesYearsAtBreastHeight, primarySpeciesYearsToBreastHeight
				)
		);
	}

	/**
	 * For each species for which a years-to-breast-height value was not supplied, calculate it from the given
	 * years-at-breast-height and age-total values if given or otherwise estimate it from the site curve and site index
	 * values for the species.
	 *
	 * @param pps the current state of the processing of the polygon
	 */
	static void estimateMissingYearsToBreastHeightValues(PolygonProcessingState pps) {

		Bank start = pps.getStartBank();
		
		int primarySpeciesIndex = pps.getPrimarySpeciesIndex();
		float primarySpeciesSiteIndex = start.siteIndices[primarySpeciesIndex];

		// Determine the default site index by using the site index of the primary species unless
		// it hasn't been set in which case pick any. Note that there may still not be a
		// meaningful value after this for example when the value is not available for the primary
		// species (see estimateMissingSiteIndices) and it's the only one.

		float defaultSiteIndex = primarySpeciesSiteIndex;

		if (Float.isNaN(defaultSiteIndex)) {
			for (int i : pps.getIndices()) {
				if (!Float.isNaN(start.siteIndices[i])) {
					defaultSiteIndex = start.siteIndices[i];
					break;
				}
			}
		}

		for (int i : pps.getIndices()) {
			if (!Float.isNaN(start.yearsToBreastHeight[i])) {
				// was supplied
				continue;
			}

			// Note: this block will normally never be executed because of the logic in
			// the constructor of VdypLayerSpecies that computes missing values when the
			// other two measurement values are present.
			if (!Float.isNaN(start.yearsAtBreastHeight[i])
					&& start.ageTotals[i] > start.yearsAtBreastHeight[i]) {
				start.yearsToBreastHeight[i] = start.ageTotals[i] - start.yearsAtBreastHeight[i];
				continue;
			}

			float siteIndex = !Float.isNaN(start.siteIndices[i]) ? start.siteIndices[i]
					: defaultSiteIndex;
			try {
				SiteIndexEquation curve = SiteIndexEquation.getByIndex(pps.getSiteCurveNumber(i));
				double yearsToBreastHeight = SiteTool.yearsToBreastHeight(curve, siteIndex);
				start.yearsToBreastHeight[i] = (float) yearsToBreastHeight;
			} catch (CommonCalculatorException e) {
				logger.warn(MessageFormat.format("Unable to determine yearsToBreastHeight of species {0}", i), e);
			}
		}
	}

	/**
	 * (1) If the site index of the primary species has not been set, calculate it as the average of the site indices of
	 * the other species that -do- have one, after converting it between the site curve of the other species and that of
	 * the primary species.
	 * <p>
	 * (2) If the site index of the primary species has (now) been set, calculate that of the other species whose site
	 * index has not been set from the primary site index after converting it between the site curve of the other
	 * species and that of the primary species.
	 *
	 * @param pps the bank in which the calculations are done.
	 * @throws ProcessingException
	 */
	static void estimateMissingSiteIndices(PolygonProcessingState pps) throws ProcessingException {

		Bank start = pps.getStartBank();
		
		int primarySpeciesIndex = pps.getPrimarySpeciesIndex();
		SiteIndexEquation primarySiteCurve = SiteIndexEquation.getByIndex(pps.getSiteCurveNumber(primarySpeciesIndex));

		// (1)

		if (Float.isNaN(start.siteIndices[primarySpeciesIndex])) {

			double otherSiteIndicesSum = 0.0f;
			int nOtherSiteIndices = 0;

			for (int i : pps.getIndices()) {

				if (i == primarySpeciesIndex) {
					continue;
				}

				float siteIndexI = start.siteIndices[i];

				if (!Float.isNaN(siteIndexI)) {
					SiteIndexEquation siteCurveI = SiteIndexEquation.getByIndex(pps.getSiteCurveNumber(i));

					try {
						double mappedSiteIndex = SiteTool
								.convertSiteIndexBetweenCurves(siteCurveI, siteIndexI, primarySiteCurve);
						if (mappedSiteIndex > 1.3) {
							otherSiteIndicesSum += mappedSiteIndex;
							nOtherSiteIndices += 1;
						}
					} catch (NoAnswerException e) {
						logger.warn(
								MessageFormat.format(
										"there is no conversion from curves {0} to {1}. Skipping species {3}", siteCurveI, primarySiteCurve, i
								)
						);
					} catch (CurveErrorException | SpeciesErrorException e) {
						throw new ProcessingException(
								MessageFormat.format(
										"convertSiteIndexBetweenCurves on {0}, {1} and {2} failed", siteCurveI, siteIndexI, primarySiteCurve
								), e
						);
					}
				}
			}

			if (nOtherSiteIndices > 0) {
				start.siteIndices[primarySpeciesIndex] = (float) (otherSiteIndicesSum / nOtherSiteIndices);
			}
		}

		// (2)

		float primarySpeciesSiteIndex = start.siteIndices[primarySpeciesIndex];
		if (!Float.isNaN(primarySpeciesSiteIndex)) {

			for (int i : pps.getIndices()) {

				if (i == primarySpeciesIndex) {
					continue;
				}

				float siteIndexI = start.siteIndices[i];
				if (Float.isNaN(siteIndexI)) {
					SiteIndexEquation siteCurveI = SiteIndexEquation.getByIndex(pps.getSiteCurveNumber(i));

					try {
						double mappedSiteIndex = SiteTool
								.convertSiteIndexBetweenCurves(primarySiteCurve, primarySpeciesSiteIndex, siteCurveI);
						start.siteIndices[i] = (float) mappedSiteIndex;
					} catch (NoAnswerException e) {
						logger.warn(
								MessageFormat.format(
										"there is no conversion between curves {0} and {1}. Skipping species {2}", primarySiteCurve, siteCurveI, i
								)
						);
					} catch (CurveErrorException | SpeciesErrorException e) {
						throw new ProcessingException(
								MessageFormat.format(
										"convertSiteIndexBetweenCurves on {0}, {1} and {2} failed. Skipping species {3}", primarySiteCurve, primarySpeciesSiteIndex, siteCurveI, i
								), e
						);
					}
				}
			}
		}

		// Finally, set bank.siteIndices[0] to that of the primary species.
		start.siteIndices[0] = primarySpeciesSiteIndex;
	}

	/**
	 * Calculate the percentage of forested land covered by each species by dividing the basal area of each given
	 * species with the basal area of the polygon covered by forest.
	 *
	 * @param state the bank in which the calculations are performed
	 */
	void calculateCoverages() {

		PolygonProcessingState pps = this.fps.getPolygonProcessingState();
		Bank start = pps.getStartBank();
		
		logger.atDebug().addArgument(pps.getNSpecies()).addArgument(start.basalAreas[0][0]).log(
				"Calculating coverages as a ratio of Species BA over Total BA. # species: {}; Layer total 7.5cm+ basal area: {}"
		);

		for (int i : pps.getIndices()) {
			start.percentagesOfForestedLand[i] = start.basalAreas[i][UC_ALL_INDEX]
					/ start.basalAreas[0][UC_ALL_INDEX] * 100.0f;

			logger.atDebug().addArgument(i).addArgument(start.speciesIndices[i])
					.addArgument(start.speciesNames[i]).addArgument(start.basalAreas[i][0])
					.addArgument(start.percentagesOfForestedLand[i])
					.log("Species {}: SP0 {}, Name {}, Species 7.5cm+ BA {}, Calculated Percent {}");
		}
	}

	/**
	 * Calculate the siteCurve number of all species for which one was not supplied. All calculations are done in the
	 * given bank, but the resulting site curve vector is stored in the given PolygonProcessingState.
	 *
	 * FORTRAN notes: the original SXINXSET function set both INXSC/INXSCV and BANK3/SCNB, except for index 0 of SCNB.
	 *
	 * @param bank         the bank in which the calculations are done.
	 * @param siteCurveMap the Site Curve definitions.
	 * @param pps          the PolygonProcessingState to where the calculated curves are also to be
	 */
	static void calculateMissingSiteCurves(
			Bank bank, MatrixMap2<String, Region, SiteIndexEquation> siteCurveMap, PolygonProcessingState pps
	) {
		BecDefinition becZone = bank.getBecZone();

		for (int i : bank.getIndices()) {

			if (bank.siteCurveNumbers[i] == VdypEntity.MISSING_INTEGER_VALUE) {

				Optional<SiteIndexEquation> scIndex = Optional.empty();

				Optional<GenusDistribution> sp0Dist = bank.sp64Distributions[i].getSpeciesDistribution(0);

				// First alternative is to use the name of the first of the species' sp64Distributions
				if (sp0Dist.isPresent()) {
					if (!siteCurveMap.isEmpty()) {
						scIndex = Utils
								.optSafe(siteCurveMap.get(sp0Dist.get().getGenus().getAlias(), becZone.getRegion()));
					} else {
						SiteIndexEquation siCurve = SiteTool
								.getSICurve(bank.speciesNames[i], becZone.getRegion().equals(Region.COASTAL));
						scIndex = siCurve == SiteIndexEquation.SI_NO_EQUATION ? Optional.empty() : Optional.of(siCurve);
					}
				}

				// Second alternative is to use the species name as given in the species' "speciesName" field
				if (scIndex.isEmpty()) {
					String sp0 = bank.speciesNames[i];
					if (!siteCurveMap.isEmpty()) {
						scIndex = Utils.optSafe(siteCurveMap.get(sp0, becZone.getRegion()));
					} else {
						SiteIndexEquation siCurve = SiteTool
								.getSICurve(sp0, becZone.getRegion().equals(Region.COASTAL));
						scIndex = siCurve == SiteIndexEquation.SI_NO_EQUATION ? Optional.empty() : Optional.of(siCurve);
					}
				}

				bank.siteCurveNumbers[i] = scIndex.orElseThrow().n();
			}
		}

		pps.setSiteCurveNumbers(bank.siteCurveNumbers);
	}

	/**
	 * Validate that the given polygon is in good order for processing.
	 *
	 * @param polygon the subject polygon.
	 * @returns if this method doesn't throw, all is good.
	 * @throws ProcessingException if the polygon does not pass validation.
	 */
	private static void validatePolygon(VdypPolygon polygon) throws ProcessingException {

		if (polygon.getDescription().getYear() < 1900) {

			throw new ProcessingException(
					MessageFormat.format(
							"Polygon {0}''s year value {1} is < 1900", polygon.getDescription().getName(), polygon
									.getDescription().getYear()
					)
			);
		}
	}

	private static void stopIfNoWork(PolygonProcessingState state) throws ProcessingException {

		// The following is extracted from BANKCHK1, simplified for the parameters
		// METH_CHK = 4, LayerI = 1, and INSTANCE = 1. So IR = 1, which is the first
		// bank, numbered 0.

		// => all that is done is that an exception is thrown if there are no species to
		// process.

		if (state.getNSpecies() == 0) {
			throw new ProcessingException(
					MessageFormat.format(
							"Polygon {0} layer 0 has no species with basal area above {1}", state.getLayer().getParent()
									.getDescription().getName(), MIN_BASAL_AREA
					)
			);
		}
	}

	/** Default Equation Group, by species. Indexed by the species number, a one-based value. */
	private static final int[] defaultEquationGroups = { 0 /* placeholder */, 1, 2, 3, 4, 1, 2, 5, 6, 7, 1, 9, 8, 9, 9,
			10, 4 };
	private static final Set<Integer> exceptedSpeciesIndicies = new HashSet<>(List.of(3, 4, 5, 6, 10));

	// PRIMFIND
	/**
	 * Returns a {@code SpeciesRankingDetails} instance giving:
	 * <ul>
	 * <li>the index in {@code bank} of the primary species
	 * <li>the index in {@code bank} of the secondary species, or Optional.empty() if none, and
	 * <li>the percentage of forested land occupied by the primary species
	 * </ul>
	 *
	 * @param pps the bank on which to operate
	 * @return as described
	 */
	void determinePolygonRankings(Collection<List<String>> speciesToCombine) {

		PolygonProcessingState pps = fps.getPolygonProcessingState();
		Bank start = pps.getStartBank();
		
		if (pps.getNSpecies() == 0) {
			throw new IllegalArgumentException("Can not find primary species as there are no species");
		}

		float[] percentages = Arrays
				.copyOf(start.percentagesOfForestedLand, start.percentagesOfForestedLand.length);

		for (var speciesPair : speciesToCombine) {
			combinePercentages(start.speciesNames, speciesPair, percentages);
		}

		float highestPercentage = 0.0f;
		int highestPercentageIndex = -1;
		float secondHighestPercentage = 0.0f;
		int secondHighestPercentageIndex = -1;
		for (int i : pps.getIndices()) {

			if (percentages[i] > highestPercentage) {

				secondHighestPercentageIndex = highestPercentageIndex;
				secondHighestPercentage = highestPercentage;
				highestPercentageIndex = i;
				highestPercentage = percentages[i];

			} else if (percentages[i] > secondHighestPercentage) {

				secondHighestPercentageIndex = i;
				secondHighestPercentage = percentages[i];
			}

			// TODO: implement NDEBUG22 = 1 logic
		}

		if (highestPercentageIndex == -1) {
			throw new IllegalStateException("There are no species with covering percentage > 0");
		}

		String primaryGenusName = start.speciesNames[highestPercentageIndex];
		Optional<String> secondaryGenusName = secondHighestPercentageIndex != -1
				? Optional.of(start.speciesNames[secondHighestPercentageIndex])
				: Optional.empty();

		try {
			int inventoryTypeGroup = findInventoryTypeGroup(primaryGenusName, secondaryGenusName, highestPercentage);

			int basalAreaGroup1 = 0;

			String primarySpeciesName = start.speciesNames[highestPercentageIndex];
			String becZoneAlias = start.getBecZone().getAlias();

			int defaultEquationGroup = fps.fcm.getDefaultEquationGroup().get(primarySpeciesName, becZoneAlias);
			Optional<Integer> equationModifierGroup = fps.fcm.getEquationModifierGroup()
					.get(defaultEquationGroup, inventoryTypeGroup);
			if (equationModifierGroup.isPresent()) {
				basalAreaGroup1 = equationModifierGroup.get();
			} else {
				basalAreaGroup1 = defaultEquationGroup;
			}

			int primarySpeciesIndex = start.speciesIndices[highestPercentageIndex];
			int basalAreaGroup3 = defaultEquationGroups[primarySpeciesIndex];
			if (Region.INTERIOR.equals(start.getBecZone().getRegion())
					&& exceptedSpeciesIndicies.contains(primarySpeciesIndex)) {
				basalAreaGroup3 += 20;
			}

			pps.setSpeciesRankingDetails(
					new SpeciesRankingDetails(
							highestPercentageIndex,
							secondHighestPercentageIndex != -1 ? Optional.of(secondHighestPercentageIndex)
									: Optional.empty(),
							inventoryTypeGroup,
							basalAreaGroup1,
							basalAreaGroup3
					)
			);
		} catch (ProcessingException e) {
			// This should never fail because the bank has already been validated and hence the genera
			// are known to be valid.

			throw new IllegalStateException(e);
		}
	}

	/**
	 * <code>combinationGroup</code> is a list of precisely two species names. This method determines the indices within
	 * <code>speciesNames</code> that match the two given names. If two do, say i and j, the <code>percentages</code> is
	 * modified as follows. Assuming percentages[i] > percentages[j], percentages[i] is set to percentages[i] +
	 * percentages[j] and percentages[j] is set to 0.0. If fewer than two indices match, nothing is done.
	 *
	 * @param speciesNames     an array of (possibly null) distinct Strings.
	 * @param combinationGroup a pair of (not null) Strings.
	 * @param percentages      an array with one entry for each entry in <code>speciesName</code>.
	 */
	static void combinePercentages(String[] speciesNames, List<String> combinationGroup, float[] percentages) {

		if (combinationGroup.size() != 2) {
			throw new IllegalArgumentException(
					MessageFormat.format("combinationGroup must have size 2; it has size {0}", combinationGroup.size())
			);
		}

		if (combinationGroup.get(0) == null || combinationGroup.get(1) == null) {
			throw new IllegalArgumentException("combinationGroup must not contain null values");
		}

		if (speciesNames.length != percentages.length) {
			throw new IllegalArgumentException(
					MessageFormat.format(
							"the length of speciesNames ({}) must match that of percentages ({}) but it doesn't", speciesNames.length, percentages.length
					)
			);
		}

		Set<Integer> groupIndices = new HashSet<>();
		for (int i = 0; i < speciesNames.length; i++) {
			if (combinationGroup.contains(speciesNames[i]))
				groupIndices.add(i);
		}

		if (groupIndices.size() == 2) {
			Integer[] groupIndicesArray = new Integer[2];
			groupIndices.toArray(groupIndicesArray);

			int higherPercentageIndex;
			int lowerPercentageIndex;
			if (percentages[groupIndicesArray[0]] > percentages[groupIndicesArray[1]]) {
				higherPercentageIndex = groupIndicesArray[0];
				lowerPercentageIndex = groupIndicesArray[1];
			} else {
				higherPercentageIndex = groupIndicesArray[1];
				lowerPercentageIndex = groupIndicesArray[0];
			}
			percentages[higherPercentageIndex] = percentages[higherPercentageIndex] + percentages[lowerPercentageIndex];
			percentages[lowerPercentageIndex] = 0.0f;
		}
	}

	// ITGFIND
	/**
	 * Find Inventory type group (ITG) for the given primary and secondary (if given) genera.
	 *
	 * @param primaryGenus           the genus of the primary species
	 * @param optionalSecondaryGenus the genus of the primary species, which may be empty
	 * @param primaryPercentage      the percentage covered by the primary species
	 * @return as described
	 * @throws ProcessingException if primaryGenus is not a known genus
	 */
	static int findInventoryTypeGroup(
			String primaryGenus, Optional<String> optionalSecondaryGenus, float primaryPercentage
	) throws ProcessingException {

		if (primaryPercentage > 79.999 /* Copied from VDYP7 */) {

			Integer recordedInventoryTypeGroup = CommonData.ITG_PURE.get(primaryGenus);
			if (recordedInventoryTypeGroup == null) {
				throw new ProcessingException("Unrecognized primary species: " + primaryGenus);
			}

			return recordedInventoryTypeGroup;
		}

		String secondaryGenus = optionalSecondaryGenus.isPresent() ? optionalSecondaryGenus.get() : "";

		if (primaryGenus.equals(secondaryGenus)) {
			throw new IllegalArgumentException("The primary and secondary genera are the same");
		}

		switch (primaryGenus) {
		case "F":
			switch (secondaryGenus) {
			case "C", "Y":
				return 2;
			case "B", "H":
				return 3;
			case "S":
				return 4;
			case "PL", "PA":
				return 5;
			case "PY":
				return 6;
			case "L", "PW":
				return 7;
			default:
				return 8;
			}
		case "C", "Y":
			switch (secondaryGenus) {
			case "H", "B", "S":
				return 11;
			default:
				return 10;
			}
		case "H":
			switch (secondaryGenus) {
			case "C", "Y":
				return 14;
			case "B":
				return 15;
			case "S":
				return 16;
			default:
				return 13;
			}
		case "B":
			switch (secondaryGenus) {
			case "C", "Y", "H":
				return 19;
			default:
				return 20;
			}
		case "S":
			switch (secondaryGenus) {
			case "C", "Y", "H":
				return 23;
			case "B":
				return 24;
			case "PL":
				return 25;
			default:
				if (CommonData.HARDWOODS.contains(secondaryGenus)) {
					return 26;
				}
				return 22;
			}
		case "PW":
			return 27;
		case "PL", "PA":
			switch (secondaryGenus) {
			case "PL", "PA":
				return 28;
			case "F", "PW", "L", "PY":
				return 29;
			default:
				if (CommonData.HARDWOODS.contains(secondaryGenus)) {
					return 31;
				}
				return 30;
			}
		case "PY":
			return 32;
		case "L":
			switch (secondaryGenus) {
			case "F":
				return 33;
			default:
				return 34;
			}
		case "AC":
			if (CommonData.HARDWOODS.contains(secondaryGenus)) {
				return 36;
			}
			return 35;
		case "D":
			if (CommonData.HARDWOODS.contains(secondaryGenus)) {
				return 38;
			}
			return 37;
		case "MB":
			return 39;
		case "E":
			return 40;
		case "AT":
			if (CommonData.HARDWOODS.contains(secondaryGenus)) {
				return 42;
			}
			return 41;
		default:
			throw new ProcessingException("Unrecognized primary species: " + primaryGenus);
		}
	}
}
