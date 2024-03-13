package ca.bc.gov.nrs.vdyp.forward;

import java.io.IOException;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.vdyp.application.ProcessingException;
import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.common.GenusDefinitionMap;
import ca.bc.gov.nrs.vdyp.forward.model.VdypLayerSpecies;
import ca.bc.gov.nrs.vdyp.forward.model.VdypPolygon;
import ca.bc.gov.nrs.vdyp.forward.model.VdypPolygonDescription;
import ca.bc.gov.nrs.vdyp.forward.model.VdypPolygonLayer;
import ca.bc.gov.nrs.vdyp.forward.model.VdypSpeciesUtilization;
import ca.bc.gov.nrs.vdyp.io.FileSystemFileResolver;
import ca.bc.gov.nrs.vdyp.io.parse.common.ResourceParseException;
import ca.bc.gov.nrs.vdyp.io.parse.streaming.StreamingParser;
import ca.bc.gov.nrs.vdyp.io.parse.streaming.StreamingParserFactory;
import ca.bc.gov.nrs.vdyp.model.GenusDefinition;
import ca.bc.gov.nrs.vdyp.model.LayerType;
import ca.bc.gov.nrs.vdyp.model.UtilizationClass;

/**
 *
 * The algorithmic part of VDYP7 GROWTH Program. In October, 2000 this was split off from the main PROGRAM, which now
 * just defines units and fills /C_CNTR/
 *
 * VDYPPASS IN/OUT I*4(10) Major Control Functions
 * <ul>
 * <li>(1) IN Perform Initiation activities? (0=No, 1=Yes)
 * <li>(2) IN Open the stand data files (0=No, 1=Yes)
 * <li>(3) IN Process stands (0=No, 1=Yes)
 * <li>(4) IN Allow multiple polygons (0=No, 1=Yes) (Subset of stand processing. May limit to 1 stand)
 * <li>(5) IN CLOSE data files.
 * <li>(10) OUT Indicator variable that in the case of single stand processing with VDYPPASS(4) set, behaves as follows:
 * <ul>
 * <li>-100 due to EOF, nothing to read
 * <li>other -ve value, incl -99. Could not process the stand.
 * <li>0 Stand was processed and written
 * <li>+ve value. Serious error. Set to IER.
 * <li>IER OUTPUT I*4 Error code
 * <ul>
 * <li>0: No error
 * <li>>0: Error 99: Error generated in routine called by this subr.
 * <li><0: Warning
 * </ul>
 * </ul>
 * </ol>
 *
 * @author Michael Junkin, Vivid Solutions
 */
public class VdypForwardProcessor {

	private static final Logger logger = LoggerFactory.getLogger(VdypForwardProcessor.class);
	
	/**
	 * Initialize VdypForwardProcessor
	 *
	 * @param resolver
	 * @param controlFileNames
	 * 
	 * @throws IOException
	 * @throws ResourceParseException
	 * @throws ProcessingException 
	 */
	void run(FileSystemFileResolver resolver, List<String> controlFileNames, Set<VdypPass> vdypPassSet) throws IOException, ResourceParseException, ProcessingException {

		logger.info("VDYPPASS: {}", vdypPassSet);
		logger.debug("VDYPPASS(1): Perform Initiation activities?");
		logger.debug("VDYPPASS(2): Open the stand data files");
		logger.debug("VDYPPASS(3): Process stands");
		logger.debug("VDYPPASS(4): Allow multiple polygons");
		logger.debug("VDYPPASS(5): Close data files");
		logger.debug(" ");
		
		// Load the control map
		Map<String, Object> controlMap = new HashMap<>();
		
		var parser = new VdypForwardControlParser();

		for (var controlFileName : controlFileNames) {
			logger.info("Resolving and parsing {}", controlFileName);

			try (var is = resolver.resolveForInput(controlFileName)) {
				Path controlFilePath = Path.of(controlFileName).getParent();
				FileSystemFileResolver relativeResolver = new FileSystemFileResolver(controlFilePath);

				parser.parse(is, relativeResolver, controlMap);
			}
		}
		
		process(vdypPassSet, controlMap);
	}

	/**
	 * Implements VDYP_SUB
	 *
	 * @throws ProcessingException
	 */
	public void process(Set<VdypPass> vdypPassSet, Map<String, Object> controlMap) throws ProcessingException {

		logger.info("Beginning processing with given configuration");
		
		int maxPoly = 0;
		if (vdypPassSet.contains(VdypPass.PASS_1)) {
			Object maxPolyValue = controlMap.get(ControlKey.MAX_NUM_POLY.name());
			if (maxPolyValue != null) {
				maxPoly = (Integer)maxPolyValue;
			}
		}
		
		logger.debug("MaxPoly: {}", maxPoly);

		if (vdypPassSet.contains(VdypPass.PASS_2)) {
			// input files are already opened
			// TODO: open output files			
		}
		
		if (vdypPassSet.contains(VdypPass.PASS_3)) {
			
			try {
				@SuppressWarnings("unchecked")
				var genusDefinitionMap = new GenusDefinitionMap((List<GenusDefinition>)controlMap.get(ControlKey.SP0_DEF.name()));
				
				var polygonDescriptionStreamFactory = controlMap.get(ControlKey.FORWARD_INPUT_GROWTO.name());
				@SuppressWarnings("unchecked")
				var polygonDescriptionStream = ((StreamingParserFactory<VdypPolygonDescription>)polygonDescriptionStreamFactory).get();
	
				var polygonStreamFactory = controlMap.get(ControlKey.FORWARD_INPUT_VDYP_POLY.name());
				@SuppressWarnings("unchecked")
				var polygonStream = ((StreamingParserFactory<VdypPolygon>)polygonStreamFactory).get();
	
				var layerSpeciesStreamFactory = controlMap.get(ControlKey.FORWARD_INPUT_VDYP_LAYER_BY_SPECIES.name());
				@SuppressWarnings("unchecked")
				var layerSpeciesStream = ((StreamingParserFactory<Collection<VdypLayerSpecies>>)layerSpeciesStreamFactory).get();
	
				var speciesUtilizationStreamFactory = controlMap.get(ControlKey.FORWARD_INPUT_VDYP_LAYER_BY_SP0_BY_UTIL.name());
				@SuppressWarnings("unchecked")
				var speciesUtilizationStream = ((StreamingParserFactory<Collection<VdypSpeciesUtilization>>)speciesUtilizationStreamFactory).get();
				
				// Fetch the next polygon to process.
				int nPolygonsProcessed = 0;
				while (polygonDescriptionStream.hasNext()) {
					if (nPolygonsProcessed == maxPoly) {
						logger.info("Prematurely terminating polygon processing since MAX_POLY ({}) polygons have been processed"
								, maxPoly);
					}
					
					var polygonDescription = polygonDescriptionStream.next();

					var polygon = readPolygon(genusDefinitionMap, polygonDescription, polygonStream, layerSpeciesStream, speciesUtilizationStream);
					
					processPolygon(polygon);
					nPolygonsProcessed += 1;
				}
				
			} catch (ResourceParseException | IOException e) {
				throw new ProcessingException(e);
			}
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
	
	public VdypPolygon readPolygon(
			  GenusDefinitionMap genusDefinitionMap
			, VdypPolygonDescription polygonDescription
			, StreamingParser<VdypPolygon> polygonStream
			, StreamingParser<Collection<VdypLayerSpecies>> layerSpeciesStream
			, StreamingParser<Collection<VdypSpeciesUtilization>> speciesUtilizationStream) throws ProcessingException {
			
		// Advance all the streams until the definition for the polydon 
		
		logger.debug("Looking for polygon {}", polygonDescription);
		
		VdypPolygon thePolygon = null;
		
		try {
			while (thePolygon == null && polygonStream.hasNext()) {
				var polygon = polygonStream.next();
				
				logger.debug("Reading polygon {}", polygon);
	
				var utilizationCollection = speciesUtilizationStream.next();
				var utilizationsBySpeciesMap = new HashMap<UtilizationBySpeciesKey, Map<UtilizationClass, VdypSpeciesUtilization>>();
				for (var utilization: utilizationCollection) {
					logger.trace("Saw utilization {}", utilization);
					
					var key = new UtilizationBySpeciesKey(utilization.getLayerType(), utilization.getGenusIndex());
					utilizationsBySpeciesMap.putIfAbsent(key, new EnumMap<>(UtilizationClass.class));
					utilizationsBySpeciesMap.get(key).put(utilization.getUcIndex(), utilization);
				}						
				
				var speciesCollection = layerSpeciesStream.next();
				var primarySpecies = new HashMap<GenusDefinition, VdypLayerSpecies>();
				var veteranSpecies = new HashMap<GenusDefinition, VdypLayerSpecies>();
				for (var species: speciesCollection) {
					logger.trace("Saw species {}", species);
					
					var key = new UtilizationBySpeciesKey(species.getLayerType(), species.getGenusIndex());
					var speciesUtilizations = utilizationsBySpeciesMap.get(key);
					species.setUtilizations(speciesUtilizations);
					
					GenusDefinition genus = genusDefinitionMap.get(species.getGenus().orElseThrow(() -> 
						new ProcessingException(MessageFormat.format("Genus missing for species {} of polygon {}"
							, species.getGenusIndex(), polygon.getDescription()))));
					if (LayerType.PRIMARY.equals(species.getLayerType())) {
						primarySpecies.put(genus, species);
					} else if (LayerType.VETERAN.equals(species.getLayerType())) {
						veteranSpecies.put(genus, species);
					} else {
						throw new IllegalStateException(MessageFormat.format("Unrecognized layer type {} for species {} of polygon {}"
								, species.getLayerType(), species.getGenusIndex(), polygon.getDescription()));
					}
				}
				
				VdypPolygonLayer primaryLayer = null;
				if (primarySpecies.size() > 0) {
					
					var key = new UtilizationBySpeciesKey(LayerType.PRIMARY, 0);
					Map<UtilizationClass, VdypSpeciesUtilization> defaultSpeciesUtilization = utilizationsBySpeciesMap.get(key);
	
					primaryLayer = new VdypPolygonLayer(LayerType.PRIMARY, polygon, primarySpecies, defaultSpeciesUtilization);
					
					polygon.setPrimaryLayer(primaryLayer);
					for (VdypLayerSpecies v: primarySpecies.values()) {
						v.setParent(primaryLayer);
					}
				}
				VdypPolygonLayer veteranLayer = null; 
				if (veteranSpecies.size() > 0) {
	
					var key = new UtilizationBySpeciesKey(LayerType.VETERAN, 0);
					Map<UtilizationClass, VdypSpeciesUtilization> defaultSpeciesUtilization = utilizationsBySpeciesMap.get(key);
					
					veteranLayer = new VdypPolygonLayer(LayerType.VETERAN, polygon, veteranSpecies, defaultSpeciesUtilization);
					
					polygon.setPrimaryLayer(veteranLayer);
					for (VdypLayerSpecies v: veteranSpecies.values()) {
						v.setParent(veteranLayer);
					}
				}
				
				if (polygonDescription.equals(polygon.getDescription())) {
					thePolygon = polygon;
				}
			}
		} catch (ResourceParseException | IOException e) {
			throw new ProcessingException(e);
		}
		
		if (thePolygon == null) {
			throw new ProcessingException(MessageFormat.format("Unable to find the definition of {}", polygonDescription));
		}
		
		return thePolygon;
	}
	
	private void processPolygon(VdypPolygon polygon) {
		logger.info("Starting processing of polygon {}", polygon.getDescription());
		
	}
}
