package ca.bc.gov.nrs.vdyp.forward;

import static ca.bc.gov.nrs.vdyp.test.VdypMatchers.controlMapHasEntry;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.bc.gov.nrs.vdyp.application.ProcessingException;
import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.forward.Bank.CopyMode;
import ca.bc.gov.nrs.vdyp.forward.test.VdypForwardTestUtils;
import ca.bc.gov.nrs.vdyp.io.parse.common.ResourceParseException;
import ca.bc.gov.nrs.vdyp.model.GenusDefinitionMap;
import ca.bc.gov.nrs.vdyp.model.LayerType;
import ca.bc.gov.nrs.vdyp.model.UtilizationClass;
import ca.bc.gov.nrs.vdyp.model.VdypLayer;
import ca.bc.gov.nrs.vdyp.model.VdypSite;
import ca.bc.gov.nrs.vdyp.model.VdypSpecies;
import ca.bc.gov.nrs.vdyp.model.VdypUtilizationHolder;

class PolygonProcessingStateTest {

	private ForwardControlParser parser;
	private Map<String, Object> controlMap;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@BeforeEach
	void before() throws IOException, ResourceParseException {

		parser = new ForwardControlParser();
		controlMap = VdypForwardTestUtils.parse(parser, "VDYP.CTR");
		assertThat(controlMap, (Matcher) controlMapHasEntry(ControlKey.SP0_DEF, instanceOf(GenusDefinitionMap.class)));
	}

	@Test
	void testConstruction() throws IOException, ResourceParseException, ProcessingException {

		ForwardDataStreamReader reader = new ForwardDataStreamReader(controlMap);

		var polygon = reader.readNextPolygon().orElseThrow(() -> new AssertionError("No polygons defined"));

		VdypLayer pLayer = polygon.getLayers().get(LayerType.PRIMARY);
		assertThat(pLayer, notNullValue());

		Bank lps = new Bank(pLayer, polygon.getBiogeoclimaticZone(), s -> true);

		int nSpecies = pLayer.getSpecies().size();

		assertThat(lps, notNullValue());
		assertThat(lps.yearsAtBreastHeight.length, is(nSpecies + 1));
		assertThat(lps.ageTotals.length, is(nSpecies + 1));
		assertThat(lps.dominantHeights.length, is(nSpecies + 1));
		assertThat(lps.percentagesOfForestedLand.length, is(nSpecies + 1));
		assertThat(lps.siteIndices.length, is(nSpecies + 1));
		assertThat(lps.sp64Distributions.length, is(nSpecies + 1));
		assertThat(lps.speciesIndices.length, is(nSpecies + 1));
		assertThat(lps.speciesNames.length, is(nSpecies + 1));
		assertThat(lps.yearsToBreastHeight.length, is(nSpecies + 1));
		assertThat(lps.getNSpecies(), is(nSpecies));

		assertThat(lps.basalAreas.length, is(nSpecies + 1));
		for (int i = 0; i < nSpecies + 1; i++) {
			assertThat(lps.basalAreas[i].length, is(UtilizationClass.values().length));
		}
		assertThat(lps.closeUtilizationVolumes.length, is(nSpecies + 1));
		for (int i = 0; i < nSpecies + 1; i++) {
			assertThat(lps.closeUtilizationVolumes[i].length, is(UtilizationClass.values().length));
		}
		assertThat(lps.cuVolumesMinusDecay.length, is(nSpecies + 1));
		for (int i = 0; i < nSpecies + 1; i++) {
			assertThat(lps.cuVolumesMinusDecay[i].length, is(UtilizationClass.values().length));
		}
		assertThat(lps.cuVolumesMinusDecayAndWastage.length, is(nSpecies + 1));
		for (int i = 0; i < nSpecies + 1; i++) {
			assertThat(lps.cuVolumesMinusDecayAndWastage[i].length, is(UtilizationClass.values().length));
		}

		assertThat(lps.loreyHeights.length, is(nSpecies + 1));
		for (int i = 0; i < nSpecies + 1; i++) {
			assertThat(lps.loreyHeights[i].length, is(2));
		}
		assertThat(lps.quadMeanDiameters.length, is(nSpecies + 1));
		for (int i = 0; i < nSpecies + 1; i++) {
			assertThat(lps.quadMeanDiameters[i].length, is(UtilizationClass.values().length));
		}
		assertThat(lps.treesPerHectare.length, is(nSpecies + 1));
		for (int i = 0; i < nSpecies + 1; i++) {
			assertThat(lps.treesPerHectare[i].length, is(UtilizationClass.values().length));
		}
		assertThat(lps.wholeStemVolumes.length, is(nSpecies + 1));
		for (int i = 0; i < nSpecies + 1; i++) {
			assertThat(lps.wholeStemVolumes[i].length, is(UtilizationClass.values().length));
		}
	}

	@Test
	void testSetCopy() throws IOException, ResourceParseException, ProcessingException {

		ForwardDataStreamReader reader = new ForwardDataStreamReader(controlMap);

		var polygon = reader.readNextPolygon().orElseThrow(() -> new AssertionError("No polygons defined"));

		VdypLayer pLayer = polygon.getLayers().get(LayerType.PRIMARY);
		assertThat(pLayer, notNullValue());

		Bank lps = new Bank(pLayer, polygon.getBiogeoclimaticZone(), s -> true);

		verifyProcessingStateMatchesLayer(lps, pLayer);

		Bank ppsCopy = lps.copy();

		verifyProcessingStateMatchesLayer(ppsCopy, pLayer);
	}

	@Test
	void testRemoveSmallLayers() throws IOException, ResourceParseException, ProcessingException {

		ForwardDataStreamReader reader = new ForwardDataStreamReader(controlMap);

		var polygon = reader.readNextPolygon().orElseThrow(() -> new AssertionError("No polygons defined"));

		VdypLayer pLayer = polygon.getLayers().get(LayerType.PRIMARY);
		assertThat(pLayer, notNullValue());

		Bank bank1 = new Bank(
				pLayer, polygon.getBiogeoclimaticZone(),
				s -> s.getBaseAreaByUtilization().get(UtilizationClass.ALL) >= 0.5
		);

		// the filter should have removed genus B (index 3) since it's ALL basal area is below 0.5
		assertThat(bank1.getNSpecies(), is(pLayer.getSpecies().size() - 1));
		assertThat(bank1.speciesIndices, is(new int[] { 0, 4, 5, 8, 15 }));

		Bank bank2 = new Bank(
				pLayer, polygon.getBiogeoclimaticZone(),
				s -> s.getBaseAreaByUtilization().get(UtilizationClass.ALL) >= 100.0
		);

		// the filter should have removed all genera.
		assertThat(bank2.getNSpecies(), is(0));
		assertThat(bank2.speciesIndices, is(new int[] { 0 }));
	}

	@Test
	void testCopyConstructor() throws IOException, ResourceParseException, ProcessingException {

		ForwardDataStreamReader reader = new ForwardDataStreamReader(controlMap);

		var polygon = reader.readNextPolygon().orElseThrow(() -> new AssertionError("No polygons defined"));

		VdypLayer pLayer = polygon.getLayers().get(LayerType.PRIMARY);
		assertThat(pLayer, notNullValue());

		Bank lps = new Bank(pLayer, polygon.getBiogeoclimaticZone(), s -> true);

		Bank ppsCopy = new Bank(lps, CopyMode.CopyAll);

		verifyProcessingStateMatchesLayer(ppsCopy, pLayer);
	}

	private void verifyProcessingStateMatchesLayer(Bank lps, VdypLayer layer) {

		List<Integer> sortedSpIndices = layer.getSpecies().values().stream().map(s -> s.getGenusIndex()).sorted().toList();

		int arrayIndex = 1;
		for (int i = 0; i < sortedSpIndices.size(); i++) {
			VdypSpecies genus = layer.getSpeciesByIndex(sortedSpIndices.get(i));
			
			verifyProcessingStateSpeciesMatchesSpecies(lps, arrayIndex, genus);

			verifyProcessingStateSpeciesUtilizationsMatchesUtilizations(
					lps, arrayIndex, genus
			);
			
			arrayIndex += 1;
		}

		verifyProcessingStateSpeciesUtilizationsMatchesUtilizations(lps, 0, layer);
	}

	private void verifyProcessingStateSpeciesUtilizationsMatchesUtilizations(
			Bank lps, int spIndex, VdypUtilizationHolder u
	) {

		for (UtilizationClass uc : UtilizationClass.values()) {
			assertThat(lps.basalAreas[spIndex][uc.index + 1], is(u.getBaseAreaByUtilization().get(uc)));
			assertThat(lps.closeUtilizationVolumes[spIndex][uc.index + 1], is(u.getCloseUtilizationVolumeByUtilization().get(uc)));
			assertThat(lps.cuVolumesMinusDecay[spIndex][uc.index + 1], is(u.getCloseUtilizationVolumeNetOfDecayByUtilization().get(uc)));
			assertThat(lps.cuVolumesMinusDecayAndWastage[spIndex][uc.index + 1], is(u.getCloseUtilizationVolumeNetOfDecayAndWasteByUtilization().get(uc)));
			if (uc.index <= 0) {
				assertThat(lps.loreyHeights[spIndex][uc.index + 1], is(u.getLoreyHeightByUtilization().get(uc)));
			}
			assertThat(lps.quadMeanDiameters[spIndex][uc.index + 1], is(u.getQuadraticMeanDiameterByUtilization().get(uc)));
			assertThat(lps.treesPerHectare[spIndex][uc.index + 1], is(u.getTreesPerHectareByUtilization().get(uc)));
			assertThat(lps.wholeStemVolumes[spIndex][uc.index + 1], is(u.getWholeStemVolumeByUtilization().get(uc)));
		}
	}

	private void verifyProcessingStateSpeciesMatchesSpecies(Bank bank, int index, VdypSpecies species) {
		VdypSite site = species.getSite().orElseThrow();
		
		assertThat(bank.yearsAtBreastHeight[index], is(site.getYearsAtBreastHeight().get()));
		assertThat(bank.ageTotals[index], is(site.getAgeTotal().get()));
		assertThat(bank.dominantHeights[index], is(site.getHeight().get()));
		assertThat(bank.siteIndices[index], is(site.getSiteIndex().get()));
		assertThat(bank.sp64Distributions[index], is(species.getSp64DistributionSet()));
		assertThat(bank.speciesIndices[index], is(species.getGenusIndex()));
		assertThat(bank.speciesNames[index], is(species.getGenus()));
		assertThat(bank.yearsToBreastHeight[index], is(site.getYearsToBreastHeight().get()));
	}
}
