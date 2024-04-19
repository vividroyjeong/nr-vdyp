package ca.bc.gov.nrs.vdyp.forward;

import static ca.bc.gov.nrs.vdyp.test.VdypMatchers.controlMapHasEntry;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.bc.gov.nrs.vdyp.application.ProcessingException;
import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.common.GenusDefinitionMap;
import ca.bc.gov.nrs.vdyp.forward.model.VdypLayerSpecies;
import ca.bc.gov.nrs.vdyp.forward.model.VdypPolygon;
import ca.bc.gov.nrs.vdyp.forward.model.VdypPolygonDescription;
import ca.bc.gov.nrs.vdyp.forward.model.VdypPolygonLayer;
import ca.bc.gov.nrs.vdyp.forward.model.VdypSpeciesUtilization;
import ca.bc.gov.nrs.vdyp.forward.test.VdypForwardTestUtils;
import ca.bc.gov.nrs.vdyp.io.parse.common.ResourceParseException;
import ca.bc.gov.nrs.vdyp.io.parse.streaming.StreamingParser;
import ca.bc.gov.nrs.vdyp.io.parse.streaming.StreamingParserFactory;
import ca.bc.gov.nrs.vdyp.model.GenusDefinition;
import ca.bc.gov.nrs.vdyp.model.UtilizationClass;

class PolygonProcessingStateTest {

	private ForwardControlParser parser;
	private Map<String, ?> controlMap;
	private GenusDefinitionMap gdMap;

	private StreamingParser<VdypPolygonDescription> polygonDescriptionStream;
	private StreamingParser<VdypPolygon> polygonStream;
	private StreamingParser<Collection<VdypLayerSpecies>> layerSpeciesStream;
	private StreamingParser<Collection<VdypSpeciesUtilization>> speciesUtilizationStream;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@BeforeEach
	void before() throws IOException, ResourceParseException {
		parser = new ForwardControlParser();
		controlMap = VdypForwardTestUtils.parse(parser, "VDYP.CTR");
		assertThat(
				controlMap, (Matcher) controlMapHasEntry(
						ControlKey.SP0_DEF, allOf(instanceOf(List.class), hasItem(instanceOf(GenusDefinition.class)))
				)
		);

		gdMap = new GenusDefinitionMap((List<GenusDefinition>) controlMap.get(ControlKey.SP0_DEF.name()));

		var polygonDescriptionStreamFactory = controlMap.get(ControlKey.FORWARD_INPUT_GROWTO.name());
		polygonDescriptionStream = ((StreamingParserFactory<VdypPolygonDescription>) polygonDescriptionStreamFactory)
				.get();

		var polygonStreamFactory = controlMap.get(ControlKey.FORWARD_INPUT_VDYP_POLY.name());
		polygonStream = ((StreamingParserFactory<VdypPolygon>) polygonStreamFactory).get();

		var layerSpeciesStreamFactory = controlMap.get(ControlKey.FORWARD_INPUT_VDYP_LAYER_BY_SPECIES.name());
		layerSpeciesStream = ((StreamingParserFactory<Collection<VdypLayerSpecies>>) layerSpeciesStreamFactory)
				.get();

		var speciesUtilizationStreamFactory = controlMap
				.get(ControlKey.FORWARD_INPUT_VDYP_LAYER_BY_SP0_BY_UTIL.name());
		speciesUtilizationStream = ((StreamingParserFactory<Collection<VdypSpeciesUtilization>>) speciesUtilizationStreamFactory)
				.get();
	}

	@Test
	void testConstruction() {

		PolygonProcessingState pps = new PolygonProcessingState(gdMap);

		int nSpecies = gdMap.getMaxIndex();

		assertThat(pps, notNullValue());
		assertThat(pps.ageBreastHeight.length, is(nSpecies + 1));
		assertThat(pps.ageTotal.length, is(nSpecies + 1));
		assertThat(pps.dominantHeight.length, is(nSpecies + 1));
		assertThat(pps.percentForestedLand.length, is(nSpecies + 1));
		assertThat(pps.siteIndex.length, is(nSpecies + 1));
		assertThat(pps.sp64Distribution.length, is(nSpecies + 1));
		assertThat(pps.speciesIndex.length, is(nSpecies + 1));
		assertThat(pps.speciesName.length, is(nSpecies + 1));
		assertThat(pps.yearsToBreastHeight.length, is(nSpecies + 1));
		assertThat(pps.getNSpecies(), is(nSpecies));

		assertThat(pps.basalArea.length, is(nSpecies + 1));
		for (int i = 0; i < nSpecies + 1; i++) {
			assertThat(pps.basalArea[i].length, is(UtilizationClass.values().length));
		}
		assertThat(pps.closeUtilizationVolume.length, is(nSpecies + 1));
		for (int i = 0; i < nSpecies + 1; i++) {
			assertThat(pps.closeUtilizationVolume[i].length, is(UtilizationClass.values().length));
		}
		assertThat(pps.cuVolumeMinusDecay.length, is(nSpecies + 1));
		for (int i = 0; i < nSpecies + 1; i++) {
			assertThat(pps.cuVolumeMinusDecay[i].length, is(UtilizationClass.values().length));
		}
		assertThat(pps.cuVolumeMinusDecayWastage.length, is(nSpecies + 1));
		for (int i = 0; i < nSpecies + 1; i++) {
			assertThat(pps.cuVolumeMinusDecayWastage[i].length, is(UtilizationClass.values().length));
		}

		assertThat(pps.loreyHeight.length, is(nSpecies + 1));
		for (int i = 0; i < nSpecies + 1; i++) {
			assertThat(pps.loreyHeight[i].length, is(2));
		}
		assertThat(pps.quadMeanDiameter.length, is(nSpecies + 1));
		for (int i = 0; i < nSpecies + 1; i++) {
			assertThat(pps.quadMeanDiameter[i].length, is(UtilizationClass.values().length));
		}
		assertThat(pps.treesPerHectare.length, is(nSpecies + 1));
		for (int i = 0; i < nSpecies + 1; i++) {
			assertThat(pps.treesPerHectare[i].length, is(UtilizationClass.values().length));
		}
		assertThat(pps.wholeStemVolume.length, is(nSpecies + 1));
		for (int i = 0; i < nSpecies + 1; i++) {
			assertThat(pps.wholeStemVolume[i].length, is(UtilizationClass.values().length));
		}
	}

	@Test
	void testSetCopy() throws IOException, ResourceParseException, ProcessingException {
		PolygonProcessingState pps = new PolygonProcessingState(gdMap);

		var processor = new ForwardProcessor();
		
		assertThat(polygonDescriptionStream.hasNext(), is(true));
		var polygonDescription = polygonDescriptionStream.next();
		var polygon = processor.readPolygon(
				gdMap, polygonDescription, polygonStream, layerSpeciesStream, speciesUtilizationStream
		);

		VdypPolygonLayer pLayer = polygon.getPrimaryLayer();
		assertThat(pLayer, notNullValue());
		
		pps.set(pLayer);
		
		verifyLayer(pps, pLayer);
		
		PolygonProcessingState ppsCopy = pps.copy();
		
		verifyLayer(ppsCopy, pLayer);
	}
	
	@Test
	void testReplace() throws IOException, ResourceParseException, ProcessingException {
		PolygonProcessingState pps = new PolygonProcessingState(gdMap);

		var processor = new ForwardProcessor();
		
		assertThat(polygonDescriptionStream.hasNext(), is(true));
		var polygonDescription = polygonDescriptionStream.next();
		var polygon = processor.readPolygon(
				gdMap, polygonDescription, polygonStream, layerSpeciesStream, speciesUtilizationStream
		);

		VdypPolygonLayer pLayer = polygon.getPrimaryLayer();
		assertThat(pLayer, notNullValue());
		
		pps.set(pLayer);
		verifyLayer(pps, pLayer);
	
		PolygonProcessingState ppsCopy = new PolygonProcessingState(pps);
		verifyLayer(ppsCopy, pLayer);

		Set<Integer> speciesToRemove = new HashSet<>(List.of(2));
		ppsCopy.remove(speciesToRemove);
		
		int nSpeciesRemoved = 0;
		for (int i = 0; i < ppsCopy.getNSpecies(); i++) {
			if (speciesToRemove.contains(i)) {
				nSpeciesRemoved += 1;
			}
			verifyEquals(ppsCopy, i, pps, i + nSpeciesRemoved);
		}
	}

	private void verifyEquals(PolygonProcessingState pps1, int i, PolygonProcessingState pps2, int j) {
		assertThat(pps1.ageBreastHeight[i], is(pps2.ageBreastHeight[j]));
		assertThat(pps1.ageTotal[i], is(pps2.ageTotal[j]));
		assertThat(pps1.dominantHeight[i], is(pps2.dominantHeight[j]));
		assertThat(pps1.siteIndex[i], is(pps2.siteIndex[j]));
		assertThat(pps1.sp64Distribution[i], is(pps2.sp64Distribution[j]));
		assertThat(pps1.speciesIndex[i], is(pps2.speciesIndex[j]));
		assertThat(pps1.speciesName[i], is(pps2.speciesName[j]));
		assertThat(pps1.yearsToBreastHeight[i], is(pps2.yearsToBreastHeight[j]));
		
		for (UtilizationClass uc: UtilizationClass.values()) {
			assertThat(pps1.basalArea[i][uc.index + 1], is(pps2.basalArea[j][uc.index + 1]));
			assertThat(pps1.closeUtilizationVolume[i][uc.index + 1], is(pps2.closeUtilizationVolume[j][uc.index + 1]));
			assertThat(pps1.cuVolumeMinusDecay[i][uc.index + 1], is(pps2.cuVolumeMinusDecay[j][uc.index + 1]));
			assertThat(pps1.cuVolumeMinusDecayWastage[i][uc.index + 1], is(pps2.cuVolumeMinusDecayWastage[j][uc.index + 1]));
			if (uc.index <= 0) {
				assertThat(pps1.loreyHeight[i][uc.index + 1], is(pps2.loreyHeight[j][uc.index + 1]));
			}
			assertThat(pps1.quadMeanDiameter[i][uc.index + 1], is(pps2.quadMeanDiameter[j][uc.index + 1]));
			assertThat(pps1.treesPerHectare[i][uc.index + 1], is(pps2.treesPerHectare[j][uc.index + 1]));
			assertThat(pps1.wholeStemVolume[i][uc.index + 1], is(pps2.wholeStemVolume[j][uc.index + 1]));
		}
	}

	@Test
	void testCopyConstructor() throws IOException, ResourceParseException, ProcessingException {
		PolygonProcessingState pps = new PolygonProcessingState(gdMap);

		var processor = new ForwardProcessor();
		
		assertThat(polygonDescriptionStream.hasNext(), is(true));
		var polygonDescription = polygonDescriptionStream.next();
		var polygon = processor.readPolygon(
				gdMap, polygonDescription, polygonStream, layerSpeciesStream, speciesUtilizationStream
		);

		VdypPolygonLayer pLayer = polygon.getPrimaryLayer();
		assertThat(pLayer, notNullValue());
		
		pps.set(pLayer);
		
		PolygonProcessingState ppsCopy = new PolygonProcessingState(pps);
		
		verifyLayer(ppsCopy, pLayer);
	}

	private void verifyLayer(PolygonProcessingState pps, VdypPolygonLayer layer) {
		for (var se: layer.getGenus().entrySet()) {
			int spIndex = gdMap.getIndex(se.getKey().getAlias());
			
			verifySpecies(pps, spIndex, se.getKey(), se.getValue());

			if (se.getValue().getUtilizations().isPresent()) {
				verifyUtilizations(pps, spIndex, se.getValue().getUtilizations().get());
			}
		}
		
		if (layer.getDefaultUtilizationMap().isPresent()) {
			verifyUtilizations(pps, 0, layer.getDefaultUtilizationMap().get());
		}
	}
	
	private void verifyUtilizations(PolygonProcessingState pps, int spIndex, Map<UtilizationClass, VdypSpeciesUtilization> map) {
		// TODO Auto-generated method stub
		
		for (UtilizationClass uc: UtilizationClass.values()) {
			VdypSpeciesUtilization u = map.get(uc);
			
			assertThat(pps.basalArea[spIndex][uc.index + 1], is(u.getBasalArea()));
			assertThat(pps.closeUtilizationVolume[spIndex][uc.index + 1], is(u.getCloseUtilizationVolume()));
			assertThat(pps.cuVolumeMinusDecay[spIndex][uc.index + 1], is(u.getCuVolumeMinusDecay()));
			assertThat(pps.cuVolumeMinusDecayWastage[spIndex][uc.index + 1], is(u.getCuVolumeMinusDecayWastage()));
			if (uc.index <= 0) {
				assertThat(pps.loreyHeight[spIndex][uc.index + 1], is(u.getLoreyHeight()));
			}
			assertThat(pps.quadMeanDiameter[spIndex][uc.index + 1], is(u.getQuadraticMeanDiameterAtBH()));
			assertThat(pps.treesPerHectare[spIndex][uc.index + 1], is(u.getLiveTreesPerHectare()));
			assertThat(pps.wholeStemVolume[spIndex][uc.index + 1], is(u.getWholeStemVolume()));
		}
	}
	
	private void verifySpecies(PolygonProcessingState pps, int spIndex, GenusDefinition genus, VdypLayerSpecies species) {
		assertThat(pps.ageBreastHeight[spIndex], is(species.getAgeAtBreastHeight()));
		assertThat(pps.ageTotal[spIndex], is(species.getAgeTotal()));
		assertThat(pps.dominantHeight[spIndex], is(species.getDominantHeight()));
		assertThat(pps.siteIndex[spIndex], is(species.getSiteIndex()));
		assertThat(pps.sp64Distribution[spIndex], is(species.getSpeciesDistributions()));
		assertThat(pps.speciesIndex[spIndex], is(species.getGenusIndex()));
		assertThat(pps.speciesName[spIndex], is(genus.getName()));
		assertThat(pps.yearsToBreastHeight[spIndex], is(species.getYearsToBreastHeight()));
	}
}
