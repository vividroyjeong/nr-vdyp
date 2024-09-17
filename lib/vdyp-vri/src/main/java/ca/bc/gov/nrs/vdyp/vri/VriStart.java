package ca.bc.gov.nrs.vdyp.vri;

import static ca.bc.gov.nrs.vdyp.common_calculators.BaseAreaTreeDensityDiameter.quadMeanDiameter;
import static ca.bc.gov.nrs.vdyp.common_calculators.BaseAreaTreeDensityDiameter.treesPerHectare;
import static ca.bc.gov.nrs.vdyp.math.FloatMath.pow;
import static java.lang.Math.max;

import java.io.Closeable;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.vdyp.application.ProcessingException;
import ca.bc.gov.nrs.vdyp.application.RuntimeProcessingException;
import ca.bc.gov.nrs.vdyp.application.RuntimeStandProcessingException;
import ca.bc.gov.nrs.vdyp.application.StandProcessingException;
import ca.bc.gov.nrs.vdyp.application.VdypApplicationIdentifier;
import ca.bc.gov.nrs.vdyp.application.VdypStartApplication;
import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.common.Utils;
import ca.bc.gov.nrs.vdyp.common.ValueOrMarker;
import ca.bc.gov.nrs.vdyp.common_calculators.BaseAreaTreeDensityDiameter;
import ca.bc.gov.nrs.vdyp.common_calculators.SiteIndex2Height;
import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.CommonCalculatorException;
import ca.bc.gov.nrs.vdyp.common_calculators.enumerations.SiteIndexAgeType;
import ca.bc.gov.nrs.vdyp.common_calculators.enumerations.SiteIndexEquation;
import ca.bc.gov.nrs.vdyp.io.parse.common.ResourceParseException;
import ca.bc.gov.nrs.vdyp.io.parse.control.BaseControlParser;
import ca.bc.gov.nrs.vdyp.io.parse.streaming.StreamingParser;
import ca.bc.gov.nrs.vdyp.math.FloatMath;
import ca.bc.gov.nrs.vdyp.model.BaseVdypSite;
import ca.bc.gov.nrs.vdyp.model.BaseVdypSpecies;
import ca.bc.gov.nrs.vdyp.model.BaseVdypSpecies.Builder;
import ca.bc.gov.nrs.vdyp.model.BecDefinition;
import ca.bc.gov.nrs.vdyp.model.Coefficients;
import ca.bc.gov.nrs.vdyp.model.CompatibilityVariableMode;
import ca.bc.gov.nrs.vdyp.model.ComponentSizeLimits;
import ca.bc.gov.nrs.vdyp.model.LayerType;
import ca.bc.gov.nrs.vdyp.model.MatrixMap2;
import ca.bc.gov.nrs.vdyp.model.ModelClassBuilder;
import ca.bc.gov.nrs.vdyp.model.PolygonIdentifier;
import ca.bc.gov.nrs.vdyp.model.PolygonMode;
import ca.bc.gov.nrs.vdyp.model.Region;
import ca.bc.gov.nrs.vdyp.model.UtilizationClass;
import ca.bc.gov.nrs.vdyp.model.VdypLayer;
import ca.bc.gov.nrs.vdyp.model.VdypPolygon;
import ca.bc.gov.nrs.vdyp.model.VdypSpecies;
import ca.bc.gov.nrs.vdyp.model.VolumeComputeMode;
import ca.bc.gov.nrs.vdyp.vri.model.VriLayer;
import ca.bc.gov.nrs.vdyp.vri.model.VriPolygon;
import ca.bc.gov.nrs.vdyp.vri.model.VriSite;
import ca.bc.gov.nrs.vdyp.vri.model.VriSpecies;

public class VriStart extends VdypStartApplication<VriPolygon, VriLayer, VriSpecies, VriSite> implements Closeable {

	private static final String SPECIAL_PROCESSING_LOG_TEMPLATE = "Doing special processing for mode {}";
	static final float FRACTION_AVAILABLE_N = 0.85f; // PCTFLAND_N;

	static final Logger log = LoggerFactory.getLogger(VriStart.class);

	static final float EMPOC = 0.85f;

	static final float VETERAN_MIN_DQ = UtilizationClass.OVER225.lowBound;
	static final float VETERAN_MIN_HL = UtilizationClass.OVER225.lowBound; // Seems odd that that the min height is the
																			// same as the min diameter

	public static void main(final String... args) throws IOException {

		try (var app = new VriStart();) {

			doMain(app, args);
		}
	}

	protected static <T extends Number> T requirePositive(Optional<T> opt, String name)
			throws StandProcessingException {
		T value = require(opt, name);

		if (value.doubleValue() <= 0) {
			throw new StandProcessingException(name + " " + value + " is not positive");
		}

		return value;
	}

	protected static <T> T require(Optional<T> opt, String name) throws StandProcessingException {
		return opt.orElseThrow(() -> new StandProcessingException(name + " is not present"));
	}

	// VRI_SUB
	// TODO Fortran takes a vector of flags (FIPPASS) controlling which stages are
	// implemented. FIPSTART always uses the same vector so far now that's not
	// implemented.
	@Override
	public void process() throws ProcessingException {
		int polygonsRead = 0;
		int polygonsWritten = 0;
		try (
				var polyStream = this.<VriPolygon>getStreamingParser(ControlKey.VRI_INPUT_YIELD_POLY);
				var layerStream = this.<Map<LayerType, VriLayer.Builder>>getStreamingParser(
						ControlKey.VRI_INPUT_YIELD_LAYER
				);
				var speciesStream = this
						.<Collection<VriSpecies>>getStreamingParser(ControlKey.VRI_INPUT_YIELD_SPEC_DIST);
				var siteStream = this.<Collection<VriSite>>getStreamingParser(ControlKey.VRI_INPUT_YIELD_HEIGHT_AGE_SI);
		) {
			log.atDebug().setMessage("Start Stand processing").log();

			while (polyStream.hasNext()) {

				// FIP_GET
				log.atInfo().setMessage("Getting polygon {}").addArgument(polygonsRead + 1).log();
				var polygon = getPolygon(polyStream, layerStream, speciesStream, siteStream);
				try {

					var resultPoly = processPolygon(polygonsRead, polygon);
					if (resultPoly.isPresent()) {
						polygonsRead++;

						// Output
						getVriWriter().writePolygonWithSpeciesAndUtilization(resultPoly.get());

						polygonsWritten++;
					}

					log.atInfo().setMessage("Read {} polygons and wrote {}").addArgument(polygonsRead)
							.addArgument(polygonsWritten).log();

				} catch (StandProcessingException ex) {
					// TODO include some sort of hook for different forms of user output
					// TODO Implement single stand mode that propagates the exception

					log.atWarn().setMessage("Polygon {} bypassed").addArgument(polygon.getPolygonIdentifier())
							.setCause(ex).log();
				}

			}
		} catch (IOException | ResourceParseException ex) {
			throw new ProcessingException("Error while reading or writing data.", ex);
		}
	}

	VriPolygon getPolygon(
			StreamingParser<VriPolygon> polyStream, StreamingParser<Map<LayerType, VriLayer.Builder>> layerStream,
			StreamingParser<Collection<VriSpecies>> speciesStream, StreamingParser<Collection<VriSite>> siteStream
	) throws StandProcessingException, IOException, ResourceParseException {

		log.trace("Getting polygon");
		var polygon = polyStream.next();

		BecDefinition bec = polygon.getBiogeoclimaticZone();

		log.trace("Getting species for polygon {}", polygon.getPolygonIdentifier());

		log.trace("Getting sites for polygon {}", polygon.getPolygonIdentifier());
		Collection<VriSite> sites;
		try {
			sites = new LinkedList<>(siteStream.next());
		} catch (NoSuchElementException ex) {
			throw validationError("Sites file has fewer records than polygon file.", ex);
		}

		Collection<VriSpecies> species;
		try {
			species = speciesStream.next();
		} catch (NoSuchElementException ex) {
			throw validationError("Species file has fewer records than polygon file.", ex);
		}

		Map<LayerType, VriLayer.Builder> layersBuilders = layerStream.next();

		for (final var spec : species) {
			var layerBuilder = layersBuilders.get(spec.getLayerType());

			var foundSite = sites.stream().filter(site -> site.getSiteGenus().equals(spec.getGenus())).findFirst();
			final var specWithSite = VriSpecies.build(builder -> {
				builder.copy(spec);
				builder.addSite(foundSite);
			});
			foundSite.ifPresent(sites::remove);

			// Validate that species belong to the correct polygon
			if (!specWithSite.getPolygonIdentifier().equals(polygon.getPolygonIdentifier())) {
				throw validationError(
						"Record in species file contains species for polygon %s when expecting one for %s.",
						specWithSite.getPolygonIdentifier(), polygon.getPolygonIdentifier()
				);
			}
			if (Objects.isNull(layerBuilder)) {
				throw validationError(
						"Species entry references layer %s of polygon %s but it is not present.",
						specWithSite.getLayerType(), polygon.getPolygonIdentifier()
				);
			}
			layerBuilder.addSpecies(specWithSite);
		}
		if (!sites.isEmpty()) {
			var specNames = sites.stream().map(site -> site.getSiteGenus()).collect(Collectors.joining(", "));
			var layerType = sites.iterator().next().getLayerType();
			throw validationError(
					"Site entries reference species %s of layer %s of polygon %s but they are not present.", specNames,
					layerType, polygon.getPolygonIdentifier()
			);
		}

		Map<LayerType, VriLayer> layers = getLayersForPolygon(polygon, bec, layersBuilders);

		// Validate that layers belong to the correct polygon
		for (var layer : layers.values()) {
			if (!layer.getPolygonIdentifier().equals(polygon.getPolygonIdentifier())) {
				throw validationError(
						"Record in layer file contains layer for polygon %s when expecting one for %s.",
						layer.getPolygonIdentifier(), polygon.getPolygonIdentifier()
				);
			}
			layer.setSpecies(new HashMap<>());
		}

		polygon.setLayers(layers);

		return polygon;

	}

	private Map<LayerType, VriLayer>
			getLayersForPolygon(VriPolygon polygon, BecDefinition bec, Map<LayerType, VriLayer.Builder> layersBuilders)
					throws StandProcessingException {
		log.trace("Getting layers for polygon {}", polygon.getPolygonIdentifier());
		Map<LayerType, VriLayer> layers;
		try {

			// Do some additional processing then build the layers.
			layers = layersBuilders.values().stream().map(builder -> {

				var layerType = builder.getLayerType().get();

				builder.buildChildren(); // Make sure all children are built before getting them.
				var layerSpecies = builder.getSpecies();

				if (layerType == LayerType.PRIMARY) {
					builder.percentAvailable(polygon.getPercentAvailable().orElse(1f));
				}
				if (!layerSpecies.isEmpty()) {
					var primarySpecs = this.findPrimarySpecies(layerSpecies);
					int itg;
					try {
						itg = findItg(primarySpecs);

						builder.inventoryTypeGroup(itg);
					} catch (StandProcessingException ex) {
						throw new RuntimeStandProcessingException(ex);
					}
					builder.primaryGenus(primarySpecs.get(0).getGenus());

					if (layerType == LayerType.PRIMARY) {
						modifyPrimaryLayerBuild(bec, builder, primarySpecs, itg);
					}
				}
				if (layerType == LayerType.VETERAN) {
					modifyVeteranLayerBuild(layersBuilders, builder);
				}
				return builder;
			}).map(VriLayer.Builder::build).collect(Collectors.toUnmodifiableMap(VriLayer::getLayerType, x -> x));

		} catch (NoSuchElementException ex) {
			throw validationError("Layers file has fewer records than polygon file.", ex);
		} catch (RuntimeStandProcessingException ex) {
			throw ex.getCause();
		}
		return layers;
	}

	private void modifyVeteranLayerBuild(
			Map<LayerType, VriLayer.Builder> layersBuilders, ca.bc.gov.nrs.vdyp.vri.model.VriLayer.Builder builder
	) {
		if (builder.getBaseArea().map(x -> x <= 0f).orElse(true)
				|| builder.getTreesPerHectare().map(x -> x <= 0f).orElse(true)) {
			// BA or TPH missing from Veteran layer.

			builder.treesPerHectare(0f);

			float crownClosure = builder.getCrownClosure().filter(x -> x > 0f).orElseThrow(
					() -> new RuntimeStandProcessingException(
							validationError(
									"Expected a positive crown closure for veteran layer but was %s",
									Utils.optNa(builder.getCrownClosure())
							)
					)
			);
			// If the primary layer base area is positive, multiply that by veteran crown
			// closure, otherwise just use half the veteran crown closure.
			builder.baseArea(
					layersBuilders.get(LayerType.PRIMARY).getBaseArea().filter(x -> x > 0f)
							.map(pba -> crownClosure / 100f * pba).orElse(crownClosure / 2f)
			);
		}
	}

	private void modifyPrimaryLayerBuild(
			BecDefinition bec, ca.bc.gov.nrs.vdyp.vri.model.VriLayer.Builder builder, List<VriSpecies> primarySpecs,
			int itg
	) {
		// This was being done in VRI_CHK but I moved it here to when the object is
		// being built instead.
		if (builder.getBaseArea()
				.flatMap(
						ba -> builder.getTreesPerHectare()
								.map(tph -> quadMeanDiameter(ba, tph) < UtilizationClass.U75TO125.lowBound)
				).orElse(false)) {
			builder.baseArea(Optional.empty());
			builder.treesPerHectare(Optional.empty());
		}

		if (primarySpecs.size() > 1) {
			builder.secondaryGenus(primarySpecs.get(1).getGenus());
		}

		builder.empiricalRelationshipParameterIndex(
				findEmpiricalRelationshipParameterIndex(primarySpecs.get(0).getGenus(), bec, itg)
		);
	}

	static final EnumSet<PolygonMode> ACCEPTABLE_MODES = EnumSet.of(PolygonMode.START, PolygonMode.YOUNG);

	Optional<VdypPolygon> processPolygon(int polygonsRead, VriPolygon polygon) throws ProcessingException {
		log.atInfo().setMessage("Read polygon {}, preparing to process").addArgument(polygon.getPolygonIdentifier())
				.log();
		var bec = polygon.getBiogeoclimaticZone();

		var mode = polygon.getMode().orElse(PolygonMode.START);

		if (mode == PolygonMode.DONT_PROCESS) {
			log.atInfo().setMessage("Skipping polygon with mode {}").addArgument(mode).log();
			return Optional.empty();
		}

		log.atInfo().setMessage("Checking validity of polygon {}:{}").addArgument(polygonsRead)
				.addArgument(polygon.getPolygonIdentifier()).log();

		mode = checkPolygon(polygon);

		final VriPolygon preProcessedPolygon;
		switch (mode) {
		case YOUNG:
			log.atTrace().setMessage(SPECIAL_PROCESSING_LOG_TEMPLATE).addArgument(mode).log();
			preProcessedPolygon = processYoung(polygon);
			break;
		case BATC:
			log.atTrace().setMessage(SPECIAL_PROCESSING_LOG_TEMPLATE).addArgument(mode).log();
			preProcessedPolygon = processBatc(polygon);
			break;
		case BATN:
			log.atTrace().setMessage(SPECIAL_PROCESSING_LOG_TEMPLATE).addArgument(mode).log();
			preProcessedPolygon = processBatn(polygon);
			break;
		default:
			log.atTrace().setMessage("No special processing for mode {}").addArgument(mode).log();
			preProcessedPolygon = polygon;
			break;
		}

		try {
			var result = Optional.of(VdypPolygon.build(pBuilder -> {
				pBuilder.adapt(preProcessedPolygon, x -> x.orElse(0f));

				pBuilder.addLayer(lBuilder -> {
					try {
						lBuilder.adapt(preProcessedPolygon.getLayers().get(LayerType.PRIMARY));
						processPrimaryLayer(preProcessedPolygon, lBuilder);
					} catch (ProcessingException e) {
						throw new RuntimeProcessingException(e);
					}
				});
				if (preProcessedPolygon.getLayers().containsKey(LayerType.VETERAN)) {
					pBuilder.addLayer(lBuilder -> {
						try {
							processVeteranLayer(preProcessedPolygon, lBuilder);
						} catch (StandProcessingException e) {
							throw new RuntimeStandProcessingException(e);
						}
					});
				}

			}));
			result.ifPresent(resultPoly -> {
				var resultPrimaryLayer = resultPoly.getLayers().get(LayerType.PRIMARY);
				var resultVeteranLayer = resultPoly.getLayers().get(LayerType.VETERAN);

				try {
					getDqBySpecies(resultPrimaryLayer, bec.getRegion());

					estimateSmallComponents(polygon, resultPrimaryLayer);

					computeUtilizationComponentsPrimary(
							bec, resultPrimaryLayer, VolumeComputeMode.BY_UTIL_WITH_WHOLE_STEM_BY_SPEC,
							CompatibilityVariableMode.NONE
					);

					if (resultVeteranLayer != null) {

						// YUCV
						computeUtilizationComponentsVeteran(resultVeteranLayer, bec);

					}
				} catch (ProcessingException e) {
					throw new RuntimeProcessingException(e);
				}
			});
			return result;
		} catch (RuntimeProcessingException e) {
			throw e.getCause();
		}
	}

	void processPrimaryLayer(VriPolygon polygon, VdypLayer.Builder lBuilder) throws ProcessingException {
		var primaryLayer = polygon.getLayers().get(LayerType.PRIMARY);
		var bec = polygon.getBiogeoclimaticZone();

		// BA_L1
		float primaryBaseArea = requirePositive(primaryLayer.getBaseArea(), "Primary layer base area");

		// TPH_L1
		var primaryLayerDensity = requirePositive(primaryLayer.getTreesPerHectare(), "Primary layer trees per hectare");

		var primarySiteIn = require(primaryLayer.getPrimarySite(), "Primary site for primary layer");

		var primarySpeciesPercent = require(primaryLayer.getPrimarySpeciesRecord(), "Primary species for primary layer")
				.getFractionGenus();

		// TPH_L1

		// TPHsp
		var primarySpeciesDensity = primarySpeciesPercent * primaryLayerDensity;

		// HDL1 or HT_L1
		var leadHeight = requirePositive(primarySiteIn.getHeight(), "Primary layer lead species height");

		// HLPL1
		// EMP050 Method 1
		var primaryHeight = estimationMethods.primaryHeightFromLeadHeight(
				leadHeight, primarySiteIn.getSiteGenus(), bec.getRegion(), primarySpeciesDensity
		);

		float layerQuadMeanDiameter = quadMeanDiameter(primaryBaseArea, primaryLayerDensity);
		lBuilder.quadraticMeanDiameterByUtilization(layerQuadMeanDiameter);
		lBuilder.baseAreaByUtilization(primaryBaseArea);
		lBuilder.treesPerHectareByUtilization(primaryLayerDensity);
		lBuilder.empiricalRelationshipParameterIndex(primaryLayer.getEmpiricalRelationshipParameterIndex());

		lBuilder.adaptSpecies(primaryLayer, (sBuilder, vriSpec) -> {
			var vriSite = vriSpec.getSite();

			applyGroups(bec, vriSpec.getGenus(), sBuilder);

			float fraction = primaryLayer.getSpecies().size() == 1 ? 1 : vriSpec.getFractionGenus();

			float specBaseArea = primaryBaseArea * fraction;
			sBuilder.baseArea(specBaseArea);

			if (vriSite.map(site -> site == primarySiteIn).orElse(false)) {
				sBuilder.loreyHeight(primaryHeight);
				sBuilder.adaptSite(vriSite.get(), (iBuilder, vriSite2) -> {
					vriSite2.getHeight().ifPresent(iBuilder::height);
				});
			} else {
				var loreyHeight = vriSite
						.flatMap(site -> site.getHeight().filter(x -> getDebugMode(2) != 1).map(height -> {
							float speciesQuadMeanDiameter = Math.max(
									UtilizationClass.U75TO125.lowBound, height / leadHeight * layerQuadMeanDiameter
							);
							float speciesDensity = treesPerHectare(specBaseArea, speciesQuadMeanDiameter);
							// EMP050
							return (float) estimationMethods.primaryHeightFromLeadHeight(
									site.getHeight().get(), site.getSiteGenus(), bec.getRegion(), speciesDensity
							);
						})).orElseGet(() -> {
							try {
								// EMP053
								return estimationMethods.estimateNonPrimaryLoreyHeight(
										vriSpec.getGenus(), primarySiteIn.getSiteGenus(), bec, leadHeight, primaryHeight
								);
							} catch (ProcessingException e) {
								throw new RuntimeProcessingException(e);
							}
						});

				float maxHeight = estimationMethods.getLimitsForHeightAndDiameter(vriSpec.getGenus(), bec.getRegion())
						.loreyHeightMaximum();
				loreyHeight = Math.min(loreyHeight, maxHeight);
				sBuilder.loreyHeight(loreyHeight);
			}
			vriSite.ifPresent(site -> sBuilder.adaptSite(site, (iBuilder, vriSite2) -> {
				vriSite2.getHeight().ifPresent(iBuilder::height);
			}));
			this.applyGroups(bec, vriSpec.getGenus(), sBuilder);
		});

		lBuilder.buildChildren();

		var species = lBuilder.getSpecies();

		float sumBaseAreaLoreyHeight = 0;
		// find aggregate lorey height
		if (species.size() == 1) {
			sumBaseAreaLoreyHeight = primaryBaseArea;
		} else {
			for (var spec : species) {
				float specBaseArea = spec.getBaseAreaByUtilization().getAll();
				float specHeight = spec.getLoreyHeightByUtilization().getAll();
				spec.getBaseAreaByUtilization().setAll(specBaseArea);
				sumBaseAreaLoreyHeight += specBaseArea * specHeight;
			}
		}

		lBuilder.loreyHeightByUtilization(sumBaseAreaLoreyHeight / primaryBaseArea);

	}

	// ROOTV01
	void getDqBySpecies(VdypLayer layer, Region region) throws ProcessingException {

		// DQ_TOT
		float quadMeanDiameterTotal = layer.getQuadraticMeanDiameterByUtilization().getAll();
		// BA_TOT
		float baseAreaTotal = layer.getBaseAreaByUtilization().getAll();
		// TPH_TOT
		float treeDensityTotal = treesPerHectare(baseAreaTotal, quadMeanDiameterTotal);

		float loreyHeightTotal = layer.getLoreyHeightByUtilization().getAll();

		// DQV
		Map<String, Float> initialDqEstimate = new LinkedHashMap<>(layer.getSpecies().size());
		// BAV
		Map<String, Float> baseAreaPerSpecies = new LinkedHashMap<>(layer.getSpecies().size());
		// DQMIN
		Map<String, Float> minPerSpecies = new LinkedHashMap<>(layer.getSpecies().size());
		// DQMAX
		Map<String, Float> maxPerSpecies = new LinkedHashMap<>(layer.getSpecies().size());
		// DQFINAL
		Map<String, Float> resultsPerSpecies = new LinkedHashMap<>(layer.getSpecies().size());

		getDqBySpeciesInitial(
				// In
				layer, region, quadMeanDiameterTotal, baseAreaTotal, treeDensityTotal, loreyHeightTotal,
				// Out
				initialDqEstimate, baseAreaPerSpecies, minPerSpecies, maxPerSpecies
		);

		resultsPerSpecies.putAll(initialDqEstimate);

		if (this.getDebugMode(9) > 0) {
			// TODO
		}

		findRootForQuadMeanDiameterFractionalError(
				-0.6f, 0.5f, resultsPerSpecies, initialDqEstimate, baseAreaPerSpecies, minPerSpecies, maxPerSpecies,
				treeDensityTotal
		);

		applyDqBySpecies(layer, baseAreaTotal, baseAreaPerSpecies, resultsPerSpecies);
	}

	void applyDqBySpecies(
			VdypLayer layer, float baseAreaTotal, Map<String, Float> baseAreaPerSpecies,
			Map<String, Float> resultsPerSpecies
	) {
		float quadMeanDiameterTotal;
		float treeDensityTotal;
		treeDensityTotal = 0;
		for (var spec : layer.getSpecies().values()) {
			float specDq = resultsPerSpecies.get(spec.getGenus());
			float specBa = baseAreaPerSpecies.get(spec.getGenus());
			float specTph = treesPerHectare(specBa, specDq);
			treeDensityTotal += specTph;
			spec.getQuadraticMeanDiameterByUtilization().setAll(specDq);
			spec.getTreesPerHectareByUtilization().setAll(specTph);
		}
		quadMeanDiameterTotal = quadMeanDiameter(baseAreaTotal, treeDensityTotal);
		layer.getTreesPerHectareByUtilization().setAll(treeDensityTotal);
		layer.getQuadraticMeanDiameterByUtilization().setAll(quadMeanDiameterTotal);
	}

	void getDqBySpeciesInitial(
			VdypLayer layer, Region region, float quadMeanDiameterTotal, float baseAreaTotal, float treeDensityTotal,
			float loreyHeightTotal, Map<String, Float> initialDqEstimate, Map<String, Float> baseAreaPerSpecies,
			Map<String, Float> minPerSpecies, Map<String, Float> maxPerSpecies
	) throws ProcessingException {
		for (var spec : layer.getSpecies().values()) {
			// EMP060
			float specDq = estimationMethods.estimateQuadMeanDiameterForSpecies(
					spec, layer.getSpecies(), region, quadMeanDiameterTotal, baseAreaTotal, treeDensityTotal,
					loreyHeightTotal
			);

			var limits = getLimitsForSpecies(spec, region);

			float min = Math.max(
					7.6f, limits.minQuadMeanDiameterLoreyHeightRatio() * spec.getLoreyHeightByUtilization().getAll()
			);
			float loreyHeightToUse = Math.max(spec.getLoreyHeightByUtilization().getAll(), 7.0f);
			float max = Math.min(
					limits.quadMeanDiameterMaximum(), limits.maxQuadMeanDiameterLoreyHeightRatio() * loreyHeightToUse
			);
			max = Math.max(7.75f, max);

			minPerSpecies.put(spec.getGenus(), min);
			maxPerSpecies.put(spec.getGenus(), max);

			specDq = FloatMath.clamp(specDq, Math.max(min, 7.75f), max);

			initialDqEstimate.put(spec.getGenus(), specDq);

			baseAreaPerSpecies.put(spec.getGenus(), spec.getBaseAreaByUtilization().getAll());
		}
	}

	protected ComponentSizeLimits getLimitsForSpecies(VdypSpecies spec, Region region) {
		// TODO for JPROGRAM = 7 implement this differently, see ROOTV01 L91-L99

		// EMP061
		return estimationMethods.getLimitsForHeightAndDiameter(spec.getGenus(), region);
	}

	float quadMeanDiameterFractionalError(
			double x, Map<String, Float> finalDiameters, Map<String, Float> initial, Map<String, Float> baseArea,
			Map<String, Float> min, Map<String, Float> max, float totalTreeDensity
	) {
		finalDiameters.clear();

		float xToUse = FloatMath.clamp((float) x, -10, 10);

		double tphSum = initial.entrySet().stream().mapToDouble(spec -> {
			float speciesFinal = quadMeanDiameterSpeciesAdjust(
					xToUse, spec.getValue(), min.get(spec.getKey()), max.get(spec.getKey())
			);
			finalDiameters.put(spec.getKey(), speciesFinal);
			return treesPerHectare(baseArea.get(spec.getKey()), speciesFinal);
		}).sum();

		return (float) ( (tphSum - totalTreeDensity) / totalTreeDensity);
	}

	float quadMeanDiameterSpeciesAdjust(float x, float initialDq, float min, float max) {
		return FloatMath.clamp(
				UtilizationClass.U75TO125.lowBound
						+ (initialDq - UtilizationClass.U75TO125.lowBound) * FloatMath.exp(x),
				min, max
		);
	}

	private float processVeteranLayer(VriPolygon polygon, VdypLayer.Builder lBuilder) throws StandProcessingException {
		var veteranLayer = polygon.getLayers().get(LayerType.VETERAN);
		lBuilder.layerType(LayerType.VETERAN);

		lBuilder.adapt(veteranLayer);

		var bec = polygon.getBiogeoclimaticZone();

		var primarySite = veteranLayer.getPrimarySite();
		var primarySpecies = veteranLayer.getPrimarySpeciesRecord();

		float ageTotal = primarySite.flatMap(VriSite::getAgeTotal).map(at -> at + veteranLayer.getAgeIncrease())
				.orElse(0f); // AGETOTLV
		float yearsToBreastHeight = primarySite.flatMap(VriSite::getYearsToBreastHeight).orElse(0f); // YTBHLV
		float dominantHeight = primarySite.flatMap(VriSite::getHeight).orElse(0f); // HDLV

		var baseArea = veteranLayer.getBaseArea()
				.orElseThrow(() -> new StandProcessingException("Expected veteran layer to have a base area")); // BA_V1
		var treesPerHectare = veteranLayer.getTreesPerHectare()
				.orElseThrow(() -> new StandProcessingException("Expected veteran layer to have a trees per hectare")); // TPH_V1

		treesPerHectare = enforceMinimumDiameter(baseArea, treesPerHectare);

		float inputTreesPerHectare = treesPerHectare; // TPHInput

		if (baseArea <= 0) {
			throw new StandProcessingException(
					MessageFormat.format("Veteran layer base area ({0}) was not positive", baseArea)
			);
		}
		if (treesPerHectare <= 0) {
			throw new StandProcessingException(
					MessageFormat.format("Veteran layer trees per hectare ({0}) was not positive", baseArea)
			);
		}
		var quadMeanDiameter = BaseAreaTreeDensityDiameter.quadMeanDiameter(baseArea, treesPerHectare); // DQ(0,0)

		lBuilder.adaptSpecies(veteranLayer, (sBuilder, spec) -> {
			applyGroups(bec, spec.getGenus(), sBuilder);
			if (primarySpecies.map(spec::equals).orElse(false)) {
				sBuilder.addSite(iBuilder -> {
					iBuilder.adapt(primarySite.get());
					iBuilder.ageTotal(ageTotal);
					iBuilder.yearsToBreastHeight(yearsToBreastHeight);
				});
			}
			float specHeight = spec.getSite().flatMap(VriSite::getHeight).filter(x -> x > 0).orElse(dominantHeight);
			float specBaseArea = spec.getFractionGenus() * baseArea;
			float specQuadMeanDiameter = max(
					estimateVeteranQuadMeanDiameter(spec.getGenus(), bec, specHeight), VETERAN_MIN_DQ
			);
			float specTreeDensity = BaseAreaTreeDensityDiameter.treesPerHectare(specBaseArea, specQuadMeanDiameter);
			sBuilder.loreyHeight(specHeight);
			sBuilder.baseArea(specBaseArea);
			sBuilder.quadMeanDiameter(specQuadMeanDiameter);
			sBuilder.treesPerHectare(specTreeDensity);
		});

		lBuilder.buildChildren();
		var specList = lBuilder.getSpecies();

		// Really crude reconciliation to the specified TPH (if any)
		// Could improve with code similar to ROOTV01
		if (treesPerHectare > 0) {
			float tphSum = 0;
			for (var spec : specList) {
				tphSum += spec.getTreesPerHectareByUtilization().getAll();
			}
			float k = treesPerHectare / tphSum;
			for (var spec : specList) {
				float specBaseArea = spec.getBaseAreaByUtilization().getLarge();
				float specTreesPerHectare = spec.getTreesPerHectareByUtilization().getLarge() * k;
				float specQuadMeanDiameter = BaseAreaTreeDensityDiameter
						.quadMeanDiameter(specBaseArea, specTreesPerHectare);
				if (specQuadMeanDiameter < VETERAN_MIN_DQ) {
					specQuadMeanDiameter = VETERAN_MIN_DQ;
					specTreesPerHectare = BaseAreaTreeDensityDiameter
							.treesPerHectare(specBaseArea, specQuadMeanDiameter);
				}
				spec.setTreesPerHectareByUtilization(Utils.utilizationVector(specTreesPerHectare));
				spec.setQuadraticMeanDiameterByUtilization(Utils.utilizationVector(specQuadMeanDiameter));
			}

		}

		// Sum BA and TPH
		float tphSum = 0; // TPH(0,4)
		float baSum = 0; // BA(0,4)
		for (var spec : specList) {
			tphSum += spec.getTreesPerHectareByUtilization().getAll();
			baSum += spec.getBaseAreaByUtilization().getAll();
		}

		if (polygon.getMode().filter(mode -> mode == PolygonMode.BATC).isPresent()) {
			// for mode 4 (with CC) input TPH was NOT available.
			inputTreesPerHectare = tphSum;
			treesPerHectare = tphSum;
		}

		return inputTreesPerHectare;
	}

	static final String MINIMUM_DIAMETER_BASE_MESSAGE = "Quadratic mean diameter {0} cm was lower than {1} cm";
	static final String MINIMUM_DIAMETER_WARN_MESSAGE = MINIMUM_DIAMETER_BASE_MESSAGE
			+ ", raising tree density to {2} trees/ha";
	static final String MINIMUM_DIAMETER_FAIL_MESSAGE = MINIMUM_DIAMETER_BASE_MESSAGE + ".";

	private Float enforceMinimumDiameter(Float baseArea, Float treesPerHectare) throws StandProcessingException {
		final float quadMeanDiameter = BaseAreaTreeDensityDiameter.quadMeanDiameter(baseArea, treesPerHectare);
		if (quadMeanDiameter < VETERAN_MIN_DQ) {
			if (this.getId() == VdypApplicationIdentifier.VRI_START && this.getDebugMode(1) == 2) {
				throw new StandProcessingException(
						MessageFormat.format(MINIMUM_DIAMETER_FAIL_MESSAGE, quadMeanDiameter, VETERAN_MIN_DQ)
				);
			}
			treesPerHectare = BaseAreaTreeDensityDiameter.treesPerHectare(baseArea, VETERAN_MIN_DQ);
			log.atWarn().setMessage(MINIMUM_DIAMETER_WARN_MESSAGE).addArgument(treesPerHectare);
		}
		return treesPerHectare;
	}

	// EMP097 TODO move to EstimationMethods and this should probably be used in FipStart

	float estimateVeteranQuadMeanDiameter(String sp0, BecDefinition bec, float loreyHeight) {
		var vetDqMap = Utils.<MatrixMap2<String, Region, Coefficients>>expectParsedControl(
				controlMap, ControlKey.VETERAN_LAYER_DQ, MatrixMap2.class
		);
		var coe = vetDqMap.get(sp0, bec.getRegion());
		var a0 = coe.getCoe(1);
		var a1 = coe.getCoe(2);
		var a2 = coe.getCoe(3);

		return a0 + a1 * pow(loreyHeight, a2);
	}

	// VRI_CHK
	PolygonMode checkPolygon(VriPolygon polygon) throws ProcessingException {

		BecDefinition bec = polygon.getBiogeoclimaticZone();

		// At this point the Fortran implementation nulled the BA and TPH of Primary
		// layers if the BA and TPH were present and resulted in a DQ <7.5
		// I did that in getPolygon instead of here.

		for (var layer : polygon.getLayers().values()) {
			checkLayer(polygon, layer);
		}

		PolygonMode mode = checkPolygonForMode(polygon, bec);

		Map<String, Float> minMap = Utils.expectParsedControl(controlMap, ControlKey.MINIMA, Map.class);

		float veteranMinHeight = minMap.get(VriControlParser.MINIMUM_VETERAN_HEIGHT);

		VriLayer veteranLayer = polygon.getLayers().get(LayerType.VETERAN);
		if (veteranLayer != null) {
			Optional<Float> veteranHeight = veteranLayer.getPrimarySite().flatMap(VriSite::getHeight);
			validateMinimum("Veteran layer primary species height", veteranHeight, veteranMinHeight, true);
		}

		return mode;
	}

	private void checkLayer(VriPolygon polygon, VriLayer layer) throws StandProcessingException {
		if (layer.getSpecies().isEmpty())
			return;
		if (layer.getLayerType() == LayerType.PRIMARY)
			this.getPercentTotal(layer); // Validate that percent total is close to 100%
		Optional<VriSite> primarySite = layer.getPrimaryGenus().flatMap(id -> Utils.optSafe(layer.getSites().get(id)));
		var ageTotal = primarySite.flatMap(VriSite::getAgeTotal);
		var treesPerHectare = layer.getTreesPerHectare();
		var height = primarySite.flatMap(VriSite::getHeight);
		if (polygon.getMode().map(x -> x == PolygonMode.YOUNG).orElse(false)
				&& layer.getLayerType() == LayerType.PRIMARY) {
			if (ageTotal.map(x -> x <= 0f).orElse(true) || treesPerHectare.map(x -> x <= 0f).orElse(true)) {
				throw validationError(
						"Age Total and Trees Per Hectare must be positive for a PRIMARY layer in mode YOUNG"
				);
			}
		} else {
			if (height.map(x -> x <= 0f).orElse(true)) {
				throw validationError(
						"Height must be positive for a VETERAN layer or a PRIMARY layer not in mode YOUNG"
				);
			}
		}
	}

	static final String SITE_INDEX_PROPERTY_NAME = "Site index";
	static final String AGE_TOTAL_PROPERTY_NAME = "Age total";
	static final String BREAST_HEIGHT_AGE_PROPERTY_NAME = "Breast height age";
	static final String YEARS_TO_BREAST_HEIGHT_PROPERTY_NAME = "Years to breast height";
	static final String HEIGHT_PROPERTY_NAME = "Height";
	static final String BASE_AREA_PROPERTY_NAME = "Base area";
	static final String TREES_PER_HECTARE_PROPERTY_NAME = "Trees per hectare";
	static final String CROWN_CLOSURE_PROPERTY_NAME = "Crown closure";

	protected PolygonMode checkPolygonForMode(VriPolygon polygon, BecDefinition bec) throws StandProcessingException {
		VriLayer primaryLayer = polygon.getLayers().get(LayerType.PRIMARY);
		Optional<VriSite> primarySite = primaryLayer.getPrimaryGenus()
				.flatMap(id -> Utils.optSafe(primaryLayer.getSites().get(id)));
		var ageTotal = primarySite.flatMap(VriSite::getAgeTotal);
		var height = primarySite.flatMap(VriSite::getHeight);
		var siteIndex = primarySite.flatMap(VriSite::getSiteIndex);
		var yearsToBreastHeight = primarySite.flatMap(VriSite::getYearsToBreastHeight);
		var baseArea = primaryLayer.getBaseArea();
		var treesPerHectare = primaryLayer.getTreesPerHectare();
		var crownClosure = primaryLayer.getCrownClosure();
		var percentForest = polygon.getPercentAvailable();

		try {
			PolygonMode mode = polygon.getMode().orElseGet(() -> {
				try {
					return findDefaultPolygonMode(
							ageTotal, yearsToBreastHeight, height, baseArea, treesPerHectare, percentForest,
							primaryLayer.getSpecies().values(), bec,
							primaryLayer.getEmpiricalRelationshipParameterIndex()
					);
				} catch (StandProcessingException e) {
					throw new RuntimeStandProcessingException(e);
				}
			});
			polygon.setMode(Optional.of(mode));
			Optional<Float> primaryBreastHeightAge = Utils.mapBoth(
					primaryLayer.getPrimarySite().flatMap(VriSite::getAgeTotal),
					primaryLayer.getPrimarySite().flatMap(VriSite::getYearsToBreastHeight), (at, ytbh) -> at - ytbh
			);
			log.atDebug().setMessage("Polygon mode {} checks").addArgument(mode).log();
			switch (mode) {

			case START:
				validateMinimum(SITE_INDEX_PROPERTY_NAME, siteIndex, 0f, false);
				validateMinimum(AGE_TOTAL_PROPERTY_NAME, ageTotal, 0f, false);
				validateMinimum(BREAST_HEIGHT_AGE_PROPERTY_NAME, primaryBreastHeightAge, 0f, false);
				validateMinimum(HEIGHT_PROPERTY_NAME, height, 4.5f, false);
				validateMinimum(BASE_AREA_PROPERTY_NAME, baseArea, 0f, false);
				validateMinimum(TREES_PER_HECTARE_PROPERTY_NAME, treesPerHectare, 0f, false);
				break;

			case YOUNG:
				validateMinimum(SITE_INDEX_PROPERTY_NAME, siteIndex, 0f, false);
				validateMinimum(AGE_TOTAL_PROPERTY_NAME, ageTotal, 0f, false);
				validateMinimum(YEARS_TO_BREAST_HEIGHT_PROPERTY_NAME, yearsToBreastHeight, 0f, false);
				break;

			case BATN:
				validateMinimum(SITE_INDEX_PROPERTY_NAME, siteIndex, 0f, false);
				validateMinimum(AGE_TOTAL_PROPERTY_NAME, ageTotal, 0f, false);
				validateMinimum(BREAST_HEIGHT_AGE_PROPERTY_NAME, primaryBreastHeightAge, 0f, false);
				validateMinimum(HEIGHT_PROPERTY_NAME, height, 1.3f, false);
				break;

			case BATC:
				validateMinimum(SITE_INDEX_PROPERTY_NAME, siteIndex, 0f, false);
				validateMinimum(AGE_TOTAL_PROPERTY_NAME, ageTotal, 0f, false);
				validateMinimum(BREAST_HEIGHT_AGE_PROPERTY_NAME, primaryBreastHeightAge, 0f, false);
				validateMinimum(HEIGHT_PROPERTY_NAME, height, 1.3f, false);
				validateMinimum(CROWN_CLOSURE_PROPERTY_NAME, crownClosure, 0f, false);
				break;

			case DONT_PROCESS:
				log.atDebug().setMessage("Skipping validation for ignored polygon");
				// Do Nothing
				break;
			}
			return mode;
		} catch (RuntimeStandProcessingException e) {
			throw e.getCause();
		}
	}

	void validateMinimum(String fieldName, float value, float minimum, boolean inclusive)
			throws StandProcessingException {
		if (value < minimum || (value == minimum && !inclusive))
			throw validationError(
					"%s %s should be %s %s", fieldName, value, inclusive ? "greater than or equal to" : "greater than",
					minimum
			);
	}

	void validateMinimum(String fieldName, Optional<Float> value, float minimum, boolean inclusive)
			throws StandProcessingException {
		validateMinimum(
				fieldName, value.orElseThrow(() -> validationError("%s is not present", fieldName)), minimum, inclusive
		);
	}

	// UPPERGEN Method 1
	Coefficients upperBounds(int baseAreaGroup) {
		var upperBoundsMap = Utils
				.<Map<Integer, Coefficients>>expectParsedControl(controlMap, ControlKey.BA_DQ_UPPER_BOUNDS, Map.class);
		return Utils.<Coefficients>optSafe(upperBoundsMap.get(baseAreaGroup)).orElseThrow(
				() -> new IllegalStateException("Could not find limits for base area group " + baseAreaGroup)
		);
	}

	float upperBoundsBaseArea(int baseAreaGroup) {
		return upperBounds(baseAreaGroup).getCoe(1);
	}

	float upperBoundsQuadMeanDiameter(int baseAreaGroup) {
		return upperBounds(baseAreaGroup).getCoe(2);
	}

	// EMP106
	float estimateBaseAreaYield(
			float dominantHeight, float breastHeightAge, Optional<Float> baseAreaOverstory, boolean fullOccupancy,
			Collection<? extends BaseVdypSpecies<? extends BaseVdypSite>> species, BecDefinition bec, int baseAreaGroup
	) throws StandProcessingException {
		var coe = estimateBaseAreaYieldCoefficients(species, bec);

		float upperBoundBaseArea = upperBoundsBaseArea(baseAreaGroup);

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

		// TODO getDebugMode(2)==1

		if (ageToUse <= 0f) {
			throw new StandProcessingException("Age was not positive");
		}

		float trAge = FloatMath.log(ageToUse);

		float a00 = Math.max(coe.getCoe(0) + coe.getCoe(1) * trAge, 0f);
		float ap = Math.max(coe.getCoe(3) + coe.getCoe(4) * trAge, 0f);

		float bap;
		if (dominantHeight <= coe.getCoe(2)) {
			bap = 0f;
		} else {
			bap = a00 * FloatMath.pow(dominantHeight - coe.getCoe(2), ap)
					* FloatMath.exp(coe.getCoe(5) * dominantHeight + coe.getCoe(6) * baseAreaOverstory.orElse(0f));
			bap = Math.min(bap, upperBoundBaseArea);
		}

		if (fullOccupancy)
			bap /= EMPOC;

		return bap;
	}

	Coefficients estimateBaseAreaYieldCoefficients(
			Collection<? extends BaseVdypSpecies<? extends BaseVdypSite>> species, BecDefinition bec
	) {
		var coe = sumCoefficientsWeightedBySpeciesAndDecayBec(species, bec, ControlKey.BA_YIELD, 7);

		// TODO confirm going over 0.5 should drop to 0 as this seems odd.
		coe.scalarInPlace(5, x -> x > 0.0f ? 0f : x);
		return coe;
	}

	Coefficients sumCoefficientsWeightedBySpeciesAndDecayBec(
			Collection<? extends BaseVdypSpecies<? extends BaseVdypSite>> species, BecDefinition bec, ControlKey key,
			int size
	) {
		var coeMap = Utils
				.<MatrixMap2<String, String, Coefficients>>expectParsedControl(controlMap, key, MatrixMap2.class);

		final String decayBecAlias = bec.getDecayBec().getAlias();

		return weightedCoefficientSum(
				size, 0, //
				species, //
				BaseVdypSpecies::getFractionGenus, // Weight by fraction
				spec -> coeMap.get(decayBecAlias, spec.getGenus())
		);
	}

	// EMP107
	/**
	 *
	 * @param dominantHeight  Dominant height (m)
	 * @param breastHeightAge breast height age
	 * @param veteranBaseArea Basal area of overstory (>= 0)
	 * @param species         Species for the layer
	 * @param bec             BEC of the polygon
	 * @param baseAreaGroup   Index of the base area group
	 * @return DQ of primary layer (w DBH >= 7.5)
	 * @throws StandProcessingException
	 */
	float estimateQuadMeanDiameterYield(
			float dominantHeight, float breastHeightAge, Optional<Float> veteranBaseArea,
			Collection<? extends BaseVdypSpecies<? extends BaseVdypSite>> species, BecDefinition bec, int baseAreaGroup
	) throws StandProcessingException {
		final var coe = sumCoefficientsWeightedBySpeciesAndDecayBec(species, bec, ControlKey.DQ_YIELD, 6);

		// TODO handle getDebugMode(2) case
		final float ageUse = breastHeightAge;

		final float upperBoundsQuadMeanDiameter = upperBoundsQuadMeanDiameter(baseAreaGroup);

		if (ageUse <= 0f) {
			throw new StandProcessingException("Primary breast height age must be positive but was " + ageUse);
		}

		final float trAge = FloatMath.log(ageUse);

		final float c0 = coe.getCoe(0);
		final float c1 = Math.max(coe.getCoe(1) + coe.getCoe(2) * trAge, 0f);
		final float c2 = Math.max(coe.getCoe(3) + coe.getCoe(4) * trAge, 0f);

		return FloatMath.clamp(c0 + c1 * FloatMath.pow(dominantHeight - 5f, c2), 7.6f, upperBoundsQuadMeanDiameter);

	}

	PolygonMode findDefaultPolygonMode(
			Optional<Float> ageTotal, Optional<Float> yearsToBreastHeight, Optional<Float> height,
			Optional<Float> baseArea, Optional<Float> treesPerHectare, Optional<Float> percentForest,
			Collection<VriSpecies> species, BecDefinition bec, Optional<Integer> baseAreaGroup
	) throws StandProcessingException {
		Optional<Float> ageBH = ageTotal.map(at -> at - yearsToBreastHeight.orElse(3f));

		float bap;
		if (ageBH.map(abh -> abh >= 1).orElse(false)) {
			bap = this.estimateBaseAreaYield(
					height.get(), ageBH.get(), Optional.empty(), false, species, bec, baseAreaGroup.get()
			);
		} else {
			bap = 0;
		}

		var mode = PolygonMode.START;

		Map<String, Float> minMap = Utils.expectParsedControl(controlMap, ControlKey.MINIMA, Map.class);

		float minHeight = minMap.get(BaseControlParser.MINIMUM_HEIGHT);
		float minBA = minMap.get(BaseControlParser.MINIMUM_BASE_AREA);
		float minPredictedBA = minMap.get(BaseControlParser.MINIMUM_PREDICTED_BASE_AREA);

		if (height.map(h -> h < minHeight).orElse(true)) {
			mode = PolygonMode.YOUNG;

			log.atDebug().setMessage("Mode {} because Height {} is below minimum {}.").addArgument(mode)
					.addArgument(height).addArgument(minHeight).log();
		} else if (bap < minPredictedBA) {
			mode = PolygonMode.YOUNG;

			log.atDebug().setMessage("Mode {} because predicted Base Area {} is below minimum {}.").addArgument(mode)
					.addArgument(bap).addArgument(minBA).log();
		} else if (baseArea.map(x -> x == 0).orElse(true) || treesPerHectare.map(x -> x == 0).orElse(true)) {
			mode = PolygonMode.YOUNG;

			log.atDebug().setMessage("Mode {} because given Base Area and Trees Per Hectare were not specified or zero")
					.addArgument(mode).log();
		} else {
			var ration = Utils.mapBoth(baseArea, percentForest, (ba, pf) -> ba * (100f / pf));

			if (ration.map(r -> r < minBA).orElse(false)) {
				mode = PolygonMode.YOUNG;
				log.atDebug().setMessage(
						"Mode {} because ration ({}) of given Base Area ({}) to Percent Forest Land ({}) was below minimum {}"
				).addArgument(mode).addArgument(ration).addArgument(baseArea).addArgument(percentForest)
						.addArgument(minBA).log();

			}
		}
		log.atDebug().setMessage("Defaulting to mode {}.").addArgument(mode).log();

		return mode;
	}

	VdypPolygon createVdypPolygon(VriPolygon sourcePolygon, Map<LayerType, VdypLayer> processedLayers)
			throws ProcessingException {

		// TODO expand this

		var vdypPolygon = VdypPolygon.build(builder -> builder.adapt(sourcePolygon, x -> x.get()));
		vdypPolygon.setLayers(processedLayers);
		return vdypPolygon;
	}

	@Override
	public VdypApplicationIdentifier getId() {
		return VdypApplicationIdentifier.VRI_START;
	}

	@Override
	protected BaseControlParser getControlFileParser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected VriSpecies copySpecies(VriSpecies toCopy, Consumer<Builder<VriSpecies, VriSite, ?>> config) {
		return VriSpecies.build(builder -> builder.copy(toCopy));
	}

	static record Increase(float dominantHeight, float ageIncrease) {
	}

	VriPolygon processYoung(VriPolygon poly) throws ProcessingException {

		PolygonIdentifier polygonIdentifier = poly.getPolygonIdentifier();
		int year = polygonIdentifier.getYear();

		if (year < 1900) {
			throw new StandProcessingException("Year for YOUNG stand should be at least 1900 but was " + year);
		}

		var bec = poly.getBiogeoclimaticZone();

		var primaryLayer = poly.getLayers().get(LayerType.PRIMARY);
		var primarySite = primaryLayer.getPrimarySite().orElseThrow();
		try {
			SiteIndexEquation siteCurve = primaryLayer.getPrimarySite() //
					.flatMap(BaseVdypSite::getSiteCurveNumber) //
					.map(SiteIndexEquation::getByIndex)//
					.orElseGet(() -> {
						try {
							return this.findSiteCurveNumber(
									bec.getRegion(), primarySite.getSiteSpecies(), primarySite.getSiteGenus()
							);
						} catch (StandProcessingException e) {
							throw new RuntimeStandProcessingException(e);
						}
					});

			float primaryAgeTotal = primarySite.getAgeTotal().orElseThrow(); // AGETOT_L1
			float primaryYearsToBreastHeight = primarySite.getYearsToBreastHeight().orElseThrow(); // YTBH_L1

			float primaryBreastHeightAge0 = primaryAgeTotal - primaryYearsToBreastHeight; // AGEBH0

			float siteIndex = primarySite.getSiteIndex().orElseThrow(); // SID
			float yeastToBreastHeight = primaryYearsToBreastHeight; // YTBHD

			Map<String, Float> minimaMap = Utils.expectParsedControl(controlMap, ControlKey.MINIMA, Map.class);

			float minimumPredictedBaseArea = minimaMap.get(BaseControlParser.MINIMUM_PREDICTED_BASE_AREA); // VMINBAeqn
			float minimumHeight = minimaMap.get(BaseControlParser.MINIMUM_HEIGHT); // VMINH

			// Find an increase that puts stand into suitable condition with EMP106
			// predicting reasonable BA

			float baseAreaTarget = minimumPredictedBaseArea; // BATARGET
			float heightTarget = minimumHeight; // HTARGET
			float ageTarget = 5f; // AGETARGET

			// If PCTFLAND is very low, INCREASE the target BA, so as to avoid rounding
			// problems on output. But Target never increased by more than a factor of 4.
			// Before Jan 2008, this all started at PCTFLAND < 50.

			float percentAvailable = poly.getPercentAvailable().filter(x -> x >= 0f).orElse(85.0f); // PCT

			if (percentAvailable < 10f) {
				float factor = Math.min(10f / percentAvailable, 4f);
				baseAreaTarget *= factor;
			}

			float dominantHeight0 = 0f; // HD0

			int moreYears = Math.max(80, (int) (130 - primaryAgeTotal));

			float primaryHeight = primarySite.getHeight().orElseThrow(); // HT_L1

			final Increase inc = findIncreaseForYoungMode(
					bec, primaryLayer, siteCurve, primaryBreastHeightAge0, siteIndex, yeastToBreastHeight,
					baseAreaTarget, heightTarget, ageTarget, dominantHeight0, moreYears, primaryHeight
			);

			return VriPolygon.build(pBuilder -> {
				pBuilder.copy(poly);
				pBuilder.polygonIdentifier(polygonIdentifier.forYear(year + (int) inc.ageIncrease));
				pBuilder.mode(PolygonMode.BATN);
				pBuilder.copyLayers(poly, (lBuilder, layer) -> {

					lBuilder.copySpecies(layer, (sBuilder, species) -> {
						sBuilder.copySiteFrom(species, (iBuilder, site) -> {
							if (layer.getLayerType() == LayerType.PRIMARY
									&& primaryLayer.getPrimaryGenus().map(site.getSiteGenus()::equals).orElse(false)) {
								iBuilder.height(inc.dominantHeight);
							} else {
								iBuilder.height(Optional.empty());
							}

							site.getAgeTotal().map(x -> x + inc.ageIncrease).ifPresentOrElse(ageTotal -> {
								iBuilder.ageTotal(ageTotal);
								iBuilder.breastHeightAge(
										site.getYearsToBreastHeight()//
												.map(ytbh -> ageTotal - ytbh)
												.or(() -> site.getBreastHeightAge().map(bha -> bha + inc.ageIncrease))
								);
							}, () -> iBuilder
									.breastHeightAge(site.getBreastHeightAge().map(bha -> bha + inc.ageIncrease))
							);

						});
						lBuilder.ageIncrease(inc.ageIncrease());
					});
				});
			});

		} catch (RuntimeStandProcessingException e) {
			throw e.getCause();
		} catch (CommonCalculatorException e) {
			throw new StandProcessingException(e);
		}
	}

	private Increase findIncreaseForYoungMode(
			BecDefinition bec, VriLayer primaryLayer, SiteIndexEquation siteCurve, float primaryBreastHeightAge0,
			float siteIndex, float yeastToBreastHeight, float baseAreaTarget, float heightTarget, float ageTarget,
			float dominantHeight0, int moreYears, float primaryHeight
	) throws CommonCalculatorException, StandProcessingException {
		float dominantHeight;
		float ageIncrease;
		for (int increase = 0; increase <= moreYears; increase++) {
			float primaryBreastHeightAge = primaryBreastHeightAge0 + increase; // AGEBH

			if (primaryBreastHeightAge > 1f) {

				float ageD = primaryBreastHeightAge; // AGED

				float dominantHeightD = (float) SiteIndex2Height.indexToHeight(
						siteCurve, ageD, SiteIndexAgeType.SI_AT_BREAST, siteIndex, ageD, yeastToBreastHeight
				); // HDD

				if (increase == 0) {
					dominantHeight0 = dominantHeightD;
				}
				dominantHeight = dominantHeightD; // HD
				if (primaryHeight > 0f && dominantHeight0 > 0f) {
					dominantHeight = primaryHeight + (dominantHeight - dominantHeight0);
				}

				// check empirical BA assuming BAV = 0

				float predictedBaseArea = estimateBaseAreaYield(
						dominantHeight, primaryBreastHeightAge, Optional.empty(), false,
						primaryLayer.getSpecies().values(), bec,
						primaryLayer.getEmpiricalRelationshipParameterIndex().orElseThrow()
				); // BAP

				// Calculate the full occupancy BA Hence the BA we will test is the Full
				// occupanct BA

				predictedBaseArea /= FRACTION_AVAILABLE_N;

				if (dominantHeight >= heightTarget && primaryBreastHeightAge >= ageTarget
						&& predictedBaseArea >= baseAreaTarget) {
					ageIncrease = increase;
					return new Increase(dominantHeight, ageIncrease);
				}
			}
		}
		throw new StandProcessingException("Unable to increase to target height.");

	}

	static final <T, B extends ModelClassBuilder<T>> BiConsumer<B, T> noChange() {
		return (builder, toCopy) -> {
			/* Do Nothing */
		};
	}

	static final BiConsumer<VriSpecies.Builder, VriSpecies> noChangeSpecies() {
		return (builder, toCopy) -> {
			toCopy.getSite().ifPresent(site -> builder.copySite(site, noChange()));
		};
	}

	VriPolygon processBatc(VriPolygon poly) throws ProcessingException {

		VriLayer primaryLayer = getPrimaryLayer(poly);
		Optional<VriLayer> veteranLayer = getVeteranLayer(poly);
		var bec = poly.getBiogeoclimaticZone();

		try {
			//
			final float percentForestLand = poly.getPercentAvailable().orElseGet(() -> {
				try {
					return this.estimatePercentForestLand(poly, veteranLayer, primaryLayer);
				} catch (ProcessingException ex) {
					throw new RuntimeProcessingException(ex);
				}
			}); // PCTFLAND

			final float primaryBreastHeightAge = getLayerBreastHeightAge(primaryLayer).orElseThrow();

			// EMP040
			final float initialPrimaryBaseArea = this
					.estimatePrimaryBaseArea(primaryLayer, bec, poly.getYieldFactor(), primaryBreastHeightAge, 0.0f);

			final Optional<Float> veteranBaseArea = veteranLayer.map(VriLayer::getCrownClosure) // BAV
					.map(ccV -> ccV * initialPrimaryBaseArea / primaryLayer.getCrownClosure());

			final float primaryBaseArea = this.estimatePrimaryBaseArea(
					primaryLayer, bec, poly.getYieldFactor(), primaryBreastHeightAge, veteranBaseArea.orElse(0.0f) // BAP
			);

			final float primaryQuadMeanDiameter = this.estimatePrimaryQuadMeanDiameter(
					primaryLayer, bec, primaryBreastHeightAge, veteranBaseArea.orElse(0f)
			);

			return VriPolygon.build(pBuilder -> {
				pBuilder.copy(poly);

				pBuilder.addLayer(lBuilder -> {
					lBuilder.copy(primaryLayer);
					lBuilder.baseArea(primaryBaseArea * (percentForestLand / 100));
					lBuilder.treesPerHectare(treesPerHectare(primaryBaseArea, primaryQuadMeanDiameter));
					lBuilder.copySpecies(primaryLayer, noChangeSpecies());
				});
				veteranLayer.ifPresent(vLayer -> pBuilder.addLayer(lBuilder -> {
					lBuilder.copy(vLayer);
					lBuilder.baseArea(veteranBaseArea);

					lBuilder.copySpecies(primaryLayer, noChangeSpecies());
				}));

			});

		} catch (RuntimeProcessingException ex) {
			throw ex.getCause();
		}
	}

	VriPolygon processBatn(VriPolygon poly) throws ProcessingException {

		final VriLayer primaryLayer = poly.getLayers().get(LayerType.PRIMARY);
		final VriSite primarySite = primaryLayer.getPrimarySite()
				.orElseThrow(() -> new StandProcessingException("Primary layer does not have a primary site"));
		final Optional<VriLayer> veteranLayer = Utils.optSafe(poly.getLayers().get(LayerType.VETERAN));
		BecDefinition bec = poly.getBiogeoclimaticZone();

		final float primaryHeight = primarySite.getHeight()
				.orElseThrow(() -> new StandProcessingException("Primary site does not have a height"));
		final float primaryBreastHeightAge = primarySite.getBreastHeightAge()
				.orElseThrow(() -> new StandProcessingException("Primary site does not have a breast height age"));
		final Optional<Float> veteranBaseArea = veteranLayer.flatMap(VriLayer::getBaseArea);

		final int primaryEmpiricalRelationshipParameterIndex = primaryLayer.getEmpiricalRelationshipParameterIndex()
				.orElseThrow(
						() -> new StandProcessingException(
								"Primary layer does not have an empirical relationship parameter index"
						)
				);

		float primaryBaseAreaEstimated = estimateBaseAreaYield(
				primaryHeight, primaryBreastHeightAge, veteranBaseArea, false, primaryLayer.getSpecies().values(), bec,
				primaryEmpiricalRelationshipParameterIndex
		);

		// EMP107
		float normativeQuadMeanDiameter = estimateQuadMeanDiameterYield(
				primaryHeight, primaryBreastHeightAge, veteranBaseArea, primaryLayer.getSpecies().values(), bec,
				primaryEmpiricalRelationshipParameterIndex
		);

		final float normativePercentAvailable = 85f;

		final float primaryBaseAreaFinal = primaryBaseAreaEstimated * (100 / normativePercentAvailable);

		final float primaryTreesPerHectare = treesPerHectare(primaryBaseAreaFinal, normativeQuadMeanDiameter);

		if (primaryBaseAreaFinal < 0.5f) {
			throw new StandProcessingException(
					"Normative esitimate " + primaryBaseAreaFinal + " of base area was less than 0.5."
			);
		}

		return VriPolygon.build(pBuilder -> {
			pBuilder.copy(poly);

			pBuilder.addLayer(lBuilder -> {
				lBuilder.copy(primaryLayer);
				lBuilder.baseArea(primaryBaseAreaFinal);
				lBuilder.treesPerHectare(primaryTreesPerHectare);
				lBuilder.copySpecies(primaryLayer, noChangeSpecies());
			});
			veteranLayer.ifPresent(vLayer -> pBuilder.addLayer(lBuilder -> {
				lBuilder.copy(vLayer);
				lBuilder.baseArea(veteranBaseArea);

				lBuilder.copySpecies(primaryLayer, noChangeSpecies());
			}));

		});

	}

	@Override
	protected ValueOrMarker<Float, Boolean>
			isVeteranForEstimatePercentForestLand(VriPolygon polygon, Optional<VriLayer> vetLayer) {
		return FLOAT_OR_BOOL.marker(vetLayer.isPresent());
	}

	/**
	 * Returns the siteCurveNumber for the first of the given ids that has one.
	 *
	 * @param region
	 * @param ids
	 * @return
	 * @throws StandProcessingException if no entry for any of the given species IDs is present.
	 */
	SiteIndexEquation findSiteCurveNumber(Region region, String... ids) throws StandProcessingException {
		var scnMap = Utils.<MatrixMap2<String, Region, SiteIndexEquation>>expectParsedControl(
				controlMap, ControlKey.SITE_CURVE_NUMBERS, MatrixMap2.class
		);

		for (String id : ids) {
			if (scnMap.hasM(id, region))
				return scnMap.get(id, region);
		}
		throw new StandProcessingException(
				"Could not find Site Curve Number for inst of the following species: " + String.join(", ", ids)
		);
	}

	@Override
	protected Optional<VriSite> getPrimarySite(VriLayer layer) {
		return layer.getPrimarySite();
	}

	@Override
	protected float getYieldFactor(VriPolygon polygon) {
		return polygon.getYieldFactor();
	}

	float findRootForQuadMeanDiameterFractionalError(
			float min, float max, Map<String, Float> resultPerSpecies, Map<String, Float> initialDqs,
			Map<String, Float> baseAreas, Map<String, Float> minDq, Map<String, Float> maxDq, float tph
	) throws StandProcessingException {

		// Note, this function has side effects in that it modifies resultPerSpecies. This is intentional, the goal is
		// to apply adjustment factor x to the values in initialDqs until the combination of their values has minimal
		// error then use those adjusted values.

		// Keeping track of the recent X values tied can be used to make some sort of guess if it doesn't converge.
		double[] lastXes = new double[2];
		double[] lastFs = new double[2];

		final double tol = 0.00001;

		UnivariateFunction errorFunc = x -> {
			lastXes[1] = lastXes[0];
			lastXes[0] = x;
			lastFs[1] = lastFs[0];
			lastFs[0] = this
					.quadMeanDiameterFractionalError(x, resultPerSpecies, initialDqs, baseAreas, minDq, maxDq, tph);
			return lastFs[0];
		};
		try {
			double x = doSolve(min, max, errorFunc);

			return (float) x;
		} catch (NoBracketingException ex) {

			// Decide if we want to propagate the exception or try to come up with something anyway.
			handleRootForQuadMeanDiameterFractionalErrorException(ex);

			// Try three values and take the least bad option.

			double x = bestOf(errorFunc, 0, -0.1, 0.1);

			// Invoke the function again to set the species map via
			errorFunc.value(x);

			return (float) x;

		} catch (TooManyEvaluationsException ex) {

			if (tol > 0.0 && Math.abs(lastFs[0]) < tol / 2) {

				if (Math.abs(lastFs[0]) < tol) {

					// Decide if we want to propagate the exception or try to use the last result.
					handleRootForQuadMeanDiameterFractionalErrorException(ex);

					return (float) lastXes[0];
				}
			}

			throw new StandProcessingException(
					"Could not find solution for quadratic mean diameter.  There appears to be a discontinuity.", ex
			);

		}
	}

	double doSolve(float min, float max, UnivariateFunction errorFunc) {
		var interval = new Interval(min, max);

		// I couldn't identify the method the original Fortran was using, so I just picked one and it worked
		// We could swap this for another like NewtonRaphsonSolver
		var solver = new BrentSolver();

		// The Fortran solver library, $ZERO, included an ability to search for a better interval if given one where
		// the function values at the end points have the same sign. This replicates that.
		interval = findInterval(new Interval(min, max), errorFunc);

		double x = solver.solve(100, errorFunc, interval.start(), interval.end(), interval.mid());
		return x;
	}

	/**
	 * Returns the x value for which func(x) is closest to 0.
	 *
	 * @param func
	 * @param values
	 * @return
	 */
	static double bestOf(UnivariateFunction func, double... values) {
		if (values.length <= 0) {
			throw new IllegalArgumentException("bestOf requires at least one point to compare");
		}
		double bestX = values[0];
		double bestY = func.value(bestX);
		for (int i = 1; i < values.length; i++) {
			double newX = values[i];
			double newY = func.value(newX);
			if (Math.abs(newY) < Math.abs(bestY)) {
				bestX = newX;
				bestY = newY;
			}
		}
		return bestX;
	}

	private void handleRootForQuadMeanDiameterFractionalErrorException(RuntimeException ex)
			throws StandProcessingException {
		// Only do this in VRIStart

		if (getDebugMode(1) == 2) {
			throw new StandProcessingException("Could not find solution for quadratic mean diameter", ex);
		}

		log.atWarn().setMessage("Could not find exact solution for quadratic mean diameter.  Using inexact estimate.")
				.setCause(ex);

	}

	public static record Interval(double start, double end) {
		double mid() {
			return (start() + end()) / 2;
		}

		double size() {
			return end() - start();
		}

		Interval evaluate(UnivariateFunction func) {
			return new Interval(func.value(start()), func.value(end()));
		}

	}

	/**
	 * This replicates the behavior of the SZERO root finding library used by VDYP7
	 *
	 * @param interval Initial interval of parameters to func
	 * @param func
	 * @return an interval for parameters to func
	 */
	public Interval findInterval(Interval intervalInit, UnivariateFunction func) {

		var interval = intervalInit;
		// Try 40 times before giving up.

		double currentX = interval.start();
		double lastX = interval.end();
		double lastF = func.value(lastX);
		double currentF = func.value(currentX);
		int i;
		for (i = 0; i < 40; i++) {

			if (currentF * lastF <= 0) {
				var newInterval = new Interval(Math.min(currentX, lastX), Math.max(currentX, lastX));
				log.atInfo().setMessage("Looking for root in range {}").addArgument(interval);
				return newInterval;
			}

			double tp = currentF / lastF;

			if (tp >= 1) {
				double temp = currentX;
				currentX = lastX;
				lastX = temp;
				temp = currentF;
				currentF = lastF;
				lastF = temp;
			}

			if (Math.abs(currentF) >= 8 * Math.abs(lastF - currentF)) {
				tp = 8;
			} else {
				tp = Math.max(0.25 * i, currentF / (lastF - currentF));
			}

			lastF = currentF;
			double oppositeX = lastX;
			lastX = currentX;
			if (currentX == oppositeX) {
				oppositeX = 1.03125 * currentX + (0.001 * Math.signum(currentX));
			}
			currentX += tp * (currentX - oppositeX);
			currentF = func.value(currentX);
		}

		throw new NoBracketingException(currentX, lastX, currentF, lastF);
	}
}