package ca.bc.gov.nrs.vdyp.forward;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.vdyp.application.ProcessingException;
import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.common.Utils;
import ca.bc.gov.nrs.vdyp.io.parse.common.ResourceParseException;
import ca.bc.gov.nrs.vdyp.io.parse.streaming.StreamingParser;
import ca.bc.gov.nrs.vdyp.io.parse.streaming.StreamingParserFactory;
import ca.bc.gov.nrs.vdyp.model.LayerType;
import ca.bc.gov.nrs.vdyp.model.PolygonIdentifier;
import ca.bc.gov.nrs.vdyp.model.UtilizationClass;
import ca.bc.gov.nrs.vdyp.model.VdypLayer;
import ca.bc.gov.nrs.vdyp.model.VdypPolygon;
import ca.bc.gov.nrs.vdyp.model.VdypSpecies;
import ca.bc.gov.nrs.vdyp.model.VdypUtilization;

public class ForwardDataStreamReader {

	private static final Logger logger = LoggerFactory.getLogger(ForwardDataStreamReader.class);

	private final StreamingParser<VdypPolygon> polygonStream;
	private final StreamingParser<Collection<VdypSpecies>> layerSpeciesStream;
	private final StreamingParser<Collection<VdypUtilization>> speciesUtilizationStream;
	Optional<StreamingParser<PolygonIdentifier>> polygonDescriptionStream;

	@SuppressWarnings("unchecked")
	public ForwardDataStreamReader(Map<String, Object> controlMap) throws IOException {

		var polygonStreamFactory = controlMap.get(ControlKey.FORWARD_INPUT_VDYP_POLY.name());
		polygonStream = ((StreamingParserFactory<VdypPolygon>) polygonStreamFactory).get();

		var layerSpeciesStreamFactory = controlMap.get(ControlKey.FORWARD_INPUT_VDYP_LAYER_BY_SPECIES.name());
		layerSpeciesStream = ((StreamingParserFactory<Collection<VdypSpecies>>) layerSpeciesStreamFactory).get();

		var speciesUtilizationStreamFactory = controlMap.get(ControlKey.FORWARD_INPUT_VDYP_LAYER_BY_SP0_BY_UTIL.name());
		speciesUtilizationStream = ((StreamingParserFactory<Collection<VdypUtilization>>) speciesUtilizationStreamFactory)
				.get();
		
		polygonDescriptionStream = Optional.empty();
		if (controlMap.containsKey(ControlKey.FORWARD_INPUT_GROWTO.name())) {
			var polygonDescriptionStreamFactory = Utils.<StreamingParserFactory<PolygonIdentifier>>
					expectParsedControl(controlMap, ControlKey.FORWARD_INPUT_GROWTO, StreamingParserFactory.class);
			
			polygonDescriptionStream = Optional.of(polygonDescriptionStreamFactory.get());
		} else {
			polygonDescriptionStream = Optional.empty();
		}
	}

	public Optional<VdypPolygon> readNextPolygon() throws ProcessingException {

		// Advance all the streams until the definition for the polygon is found.

		Optional<VdypPolygon> thePolygon = Optional.empty();

		try {
			if (polygonStream.hasNext()) {
				var polygon = polygonStream.next();

				logger.debug("Reading polygon {}", polygon);

				var utilizationCollection = speciesUtilizationStream.next();
				var utilizationsBySpeciesMap = new HashMap<UtilizationBySpeciesKey, Map<UtilizationClass, VdypUtilization>>();
				for (var utilization : utilizationCollection) {
					logger.trace("Saw utilization {}", utilization);

					var key = new UtilizationBySpeciesKey(utilization.getLayerType(), utilization.getGenusIndex());
					utilizationsBySpeciesMap.putIfAbsent(key, new EnumMap<>(UtilizationClass.class));
					utilizationsBySpeciesMap.get(key).put(utilization.getUcIndex(), utilization);
				}
				
				if (polygonDescriptionStream.isPresent()) {
					var pdStream = polygonDescriptionStream.get();
					if (!pdStream.hasNext()) {
						throw new ProcessingException(MessageFormat.format("Grow-to-year file at {0} in the control file does"
								+ " not contain a record for {1} as expected, but instead the end-of-file was reached"
								, ControlKey.FORWARD_INPUT_GROWTO.name(), polygon.getPolygonIdentifier().getName()));
					}
					var polygonDescription = pdStream.next();
					if (! polygonDescription.getName().equals(polygon.getPolygonIdentifier().getName())) {
						throw new ProcessingException(MessageFormat.format("Grow-to-year file at {0} in the control file does"
								+ " not contain a record for {1} as expected, but instead contains a record for {2}"
								, ControlKey.FORWARD_INPUT_GROWTO.name(), polygon.getPolygonIdentifier().getName()
								, polygonDescription.getName()));
					}
					
					polygon.setTargetYear(polygonDescription.getYear());
				}

				var speciesCollection = layerSpeciesStream.next();
				var primarySpecies = new HashMap<Integer, VdypSpecies>();
				var veteranSpecies = new HashMap<Integer, VdypSpecies>();
				for (var species : speciesCollection) {
					logger.trace("Saw species {}", species);

					var key = new UtilizationBySpeciesKey(species.getLayerType(), species.getGenusIndex());
					var speciesUtilizations = utilizationsBySpeciesMap.get(key);

					if (speciesUtilizations != null) {
						species.setUtilizations(Optional.of(speciesUtilizations));

						for (VdypUtilization u : speciesUtilizations.values()) {
							u.setParent(species);
						}
					} else {
						species.setUtilizations(Optional.empty());
					}

					if (LayerType.PRIMARY.equals(species.getLayerType())) {
						primarySpecies.put(species.getGenusIndex(), species);
					} else if (LayerType.VETERAN.equals(species.getLayerType())) {
						veteranSpecies.put(species.getGenusIndex(), species);
					} else {
						throw new IllegalStateException(
								MessageFormat.format(
										"Unrecognized layer type {} for species {} of polygon {}",
										species.getLayerType(), species.getGenusIndex(), polygon.getPolygonIdentifier()
								)
						);
					}
				}

				VdypLayer primaryLayer = null;
				if (primarySpecies.size() > 0) {

					var key = new UtilizationBySpeciesKey(LayerType.PRIMARY, 0);
					Map<UtilizationClass, VdypUtilization> defaultSpeciesUtilization = utilizationsBySpeciesMap
							.get(key);

					primaryLayer = new VdypLayer(
							LayerType.PRIMARY, polygon, primarySpecies, Optional.ofNullable(defaultSpeciesUtilization)
					);

					for (VdypSpecies v : primarySpecies.values()) {
						v.setParent(primaryLayer);
					}
				}

				VdypLayer veteranLayer = null;
				if (veteranSpecies.size() > 0) {

					var key = new UtilizationBySpeciesKey(LayerType.VETERAN, 0);
					Map<UtilizationClass, VdypUtilization> defaultSpeciesUtilization = utilizationsBySpeciesMap
							.get(key);

					veteranLayer = new VdypLayer(
							LayerType.VETERAN, polygon, veteranSpecies, Optional.ofNullable(defaultSpeciesUtilization)
					);

					for (VdypSpecies v : veteranSpecies.values()) {
						v.setParent(veteranLayer);
					}
				}

				polygon.setLayers(primaryLayer, veteranLayer);

				thePolygon = Optional.of(polygon);
				adjustUtilizations(polygon);
			}
		} catch (ResourceParseException | IOException e) {
			throw new ProcessingException(e);
		}

		return thePolygon;
	}

	/**
	 * Both scale the per-hectare values of all the utilizations of the primary layer of the given polygon, and for all
	 * utilizations of both the primary and veteran layer (if present) of the polygon, 1. Adjust the basal area to be
	 * within bounds of the utilization class, and 2. Calculate the quad-mean-diameter value from the basal area and
	 * trees per hectare.
	 *
	 * @param polygon
	 */
	private void adjustUtilizations(VdypPolygon polygon) throws ProcessingException {

		float percentForestedLand = polygon.getPercentAvailable();
		assert !Float.isNaN(percentForestedLand);
		float scalingFactor = 100.0f / percentForestedLand;

		List<VdypUtilization> utilizationsToAdjust = new ArrayList<>();

		for (VdypLayer l : polygon.getLayers().values()) {

			l.getDefaultUtilizationMap().ifPresent(m -> utilizationsToAdjust.addAll(m.values()));

			l.getGenera().values().stream()
					.forEach(s -> s.getUtilizations().ifPresent(m -> utilizationsToAdjust.addAll(m.values())));
		}

		for (VdypUtilization u : utilizationsToAdjust) {

			if (percentForestedLand > 0.0f && percentForestedLand < 100.0f) {
				u.scale(scalingFactor);
			}

			u.doPostCreateAdjustments();
		}
	}

	private class UtilizationBySpeciesKey {
		private final LayerType layerType;
		private final Integer genusIndex;

		public UtilizationBySpeciesKey(LayerType layerType, Integer genusIndex) {
			this.layerType = layerType;
			this.genusIndex = genusIndex;
		}

		@Override
		public boolean equals(Object other) {
			if (other instanceof UtilizationBySpeciesKey that) {
				return layerType.equals(that.layerType) && genusIndex.equals(that.genusIndex);
			} else {
				return false;
			}
		}

		@Override
		public int hashCode() {
			return layerType.hashCode() * 17 + genusIndex.hashCode();
		}
	}
}
