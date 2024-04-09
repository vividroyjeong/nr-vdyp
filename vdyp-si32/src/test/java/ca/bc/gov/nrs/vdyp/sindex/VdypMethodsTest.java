package ca.bc.gov.nrs.vdyp.sindex;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

import ca.bc.gov.nrs.vdyp.si32.enumerations.SpeciesRegion;
import ca.bc.gov.nrs.vdyp.si32.vdyp.SP0Name;
import ca.bc.gov.nrs.vdyp.si32.vdyp.SP64Name;
import ca.bc.gov.nrs.vdyp.si32.vdyp.SpeciesTable;
import ca.bc.gov.nrs.vdyp.si32.vdyp.VdypMethods;

class VdypMethodsTest {

	@Test
	void test_VDYP_GetNumSpecies() {
		assertThat(VdypMethods.getNumDefinedSpecies(), equalTo(142));
	}
	
	@Test
	void test_VDYP_SpeciesIndex() {
		assertThat(VdypMethods.speciesIndex("A"), equalTo(1));
		assertThat(VdypMethods.speciesIndex("ZZZ"), equalTo(0));
		assertThat(VdypMethods.speciesIndex(null), equalTo(0));
	}
	
	@Test
	void test_VDYP_IsValidSpecies() {
		assertThat(VdypMethods.isValidSpecies("A"), equalTo(true));
		assertThat(VdypMethods.isValidSpecies("ZZZ"), equalTo(false));
		assertThat(VdypMethods.isValidSpecies(null), equalTo(false));
	}
	
	@Test
	void test_VDYP_IsDeciduous() {
		assertThat(VdypMethods.isDeciduous(SP64Name.sp64_A), equalTo(true));
		assertThat(VdypMethods.isDeciduous(SP64Name.sp64_BA), equalTo(false));
		assertThat(VdypMethods.isDeciduous(null), equalTo(SpeciesTable.UNKNOWN_ENTRY_IS_DECIDUOUS_VALUE));
	}
	
	@Test
	void test_VDYP_IsConiferous() {
		assertThat(VdypMethods.isCommercial(SP64Name.sp64_A), equalTo(true));
		// there are no species that are not commercial at this time.
		assertThat(VdypMethods.isCommercial(null), equalTo(SpeciesTable.UNKNOWN_ENTRY_IS_COMMERCIAL_VALUE));
	}
	
	@Test
	void test_VDYP_GetSpeciesShortName() {
		assertThat(VdypMethods.getSpeciesShortName(SP64Name.sp64_A), equalTo("A"));
		// there are no species that are not commercial at this time.
		assertThat(VdypMethods.getSpeciesShortName(null), equalTo(SpeciesTable.UNKNOWN_ENTRY_SP0_NAME_VALUE));
	}
	
	@Test
	void test_VDYP_GetSpeciesFullName() {
		assertThat(VdypMethods.getSpeciesFullName(SP64Name.sp64_A), equalTo("Aspen/Cottonwood/Poplar"));
		assertThat(VdypMethods.getSpeciesFullName(null), equalTo(SpeciesTable.UNKNOWN_ENTRY_FULL_NAME));
	}
	
	@Test
	void test_VDYP_GetSpeciesLatinName() {
		assertThat(VdypMethods.getSpeciesLatinName(SP64Name.sp64_A), equalTo("Populus"));
		assertThat(VdypMethods.getSpeciesLatinName(null), equalTo(SpeciesTable.UNKNOWN_ENTRY_LATIN_NAME));
	}
	
	@Test
	void test_VDYP_GetSpeciesGenusName() {
		assertThat(VdypMethods.getSpeciesGenus(SP64Name.sp64_A), equalTo("A"));
		assertThat(VdypMethods.getSpeciesGenus(null), equalTo(SpeciesTable.UNKNOWN_ENTRY_GENUS_NAME));
	}
	
	@Test
	void test_VDYP_GetSINDEXSpecies() {
		assertThat(VdypMethods.getSINDEXSpecies(SP64Name.sp64_A.getText(), SpeciesRegion.spcsRgn_Coast), equalTo("At"));
		assertThat(VdypMethods.getSINDEXSpecies("ZZZ", SpeciesRegion.spcsRgn_Coast), equalTo(""));
		assertThat(VdypMethods.getSINDEXSpecies(null, SpeciesRegion.spcsRgn_Interior), equalTo(""));
		assertThat(VdypMethods.getSINDEXSpecies(SP64Name.sp64_A.getText(), null), equalTo(""));
	}
	
	@Test
	void test_VDYP_GetVDYP7Species() {
		assertThat(VdypMethods.getVDYP7Species(SP64Name.sp64_A.getText()), equalTo("AC"));
		assertThat(VdypMethods.getVDYP7Species(null), equalTo(SpeciesTable.UNKNOWN_ENTRY_SP0_NAME_VALUE));
	}
	
	@Test
	void test_VDYP_GetSP0Species() {
		assertThat(VdypMethods.getVDYP7Species(SP0Name.sp0_AC.getText()), equalTo("AC"));
		assertThat(VdypMethods.getVDYP7Species(null), equalTo(SpeciesTable.UNKNOWN_ENTRY_SP0_NAME_VALUE));
	}
	
	@Test
	void test_VDYP_GetCurrentSICurve() {
		assertThat(VdypMethods.getCurrentSICurve(SP0Name.sp0_AC.getText(), SpeciesRegion.spcsRgn_Interior), equalTo(97));
		// ensure result from previous call was cached properly.
		assertThat(VdypMethods.speciesTable.getByCode(SP0Name.sp0_AC.getText()).details()
				.currentSICurve()[SpeciesRegion.spcsRgn_Interior.ordinal()], equalTo(97));
		assertThat(VdypMethods.getCurrentSICurve(null, SpeciesRegion.spcsRgn_Interior), equalTo(-1));
		assertThat(VdypMethods.getCurrentSICurve(SP0Name.sp0_AC.getText(), null), equalTo(-1));
	}
	
	@Test
	void test_VDYP_GetDefaultSICurve() {
		assertThat(VdypMethods.getDefaultSICurve(SP0Name.sp0_AC.getText(), SpeciesRegion.spcsRgn_Coast), equalTo(97));
		assertThat(VdypMethods.getDefaultSICurve(null, SpeciesRegion.spcsRgn_Interior), equalTo(-1));
		assertThat(VdypMethods.getDefaultSICurve(SP0Name.sp0_AC.getText(), null), equalTo(-1));
	}
	
	@Test 
	void test_VDYP_SetCurrentSICurve() {
		String sp64Name = SP0Name.sp0_AC.getText();
		SpeciesRegion region = SpeciesRegion.spcsRgn_Coast;
		
		int oldCurve = VdypMethods.getCurrentSICurve(sp64Name, region);
		int result = VdypMethods.setCurrentSICurve(sp64Name, region, oldCurve + 1);
		
		assertThat(result, equalTo(oldCurve));
		assertThat(VdypMethods.getCurrentSICurve(sp64Name, region), equalTo(oldCurve + 1));
	}
	
	@Test
	void test_VDYP_GetNumSICurves() {
		String sp64Name = SP64Name.sp64_A.getText();

		assertThat(VdypMethods.getNumSICurves(true, null, false, null), equalTo(0));
		assertThat(VdypMethods.getNumSICurves(true, sp64Name, false, null), equalTo(0));
		
		int allCurvesCount = VdypMethods.getNumSICurves(false, null, false, null);
		assertThat(allCurvesCount, equalTo(123));

		int atAllRegionsCurveCount = VdypMethods.getNumSICurves(true, sp64Name, true, null);
		assertThat(atAllRegionsCurveCount, equalTo(5));

		int atInteriorCurveCount = VdypMethods.getNumSICurves(true, sp64Name, false, SpeciesRegion.spcsRgn_Interior);
		assertThat(atInteriorCurveCount, equalTo(5));

		int atCoastCurveCount = VdypMethods.getNumSICurves(true, sp64Name, false, SpeciesRegion.spcsRgn_Coast);
		assertThat(atCoastCurveCount, equalTo(5));
	}
	
	@Test
	void test_VDYP_GetSICurveSpeciesIndex() {
		int speciesIndex = VdypMethods.getSICurveSpeciesIndex(97);
		assertThat(speciesIndex, equalTo(4 /* SI_SPEC_ACB (SI_ACB_HUANGAC) from si_curve_intend in Sindxdll */));
	}
	
	@Test
	void test_VDYP_GetDefaultCrownClosure() {
		String sp64Name = SP64Name.sp64_AC.getText();
		SpeciesRegion region = SpeciesRegion.spcsRgn_Coast;
		
		float result = VdypMethods.getDefaultCrownClosure(sp64Name, region);
		assertThat(result, equalTo(61.0f /* from speciesTable[1 == AC].defaultCrownClosure[0] */));
		
		assertThat(VdypMethods.getDefaultCrownClosure(null, region), equalTo(-1.0f));
		assertThat(VdypMethods.getDefaultCrownClosure(sp64Name, null), equalTo(-1.0f));
	}
	
	@Test
	void test_VDYP_GetVDYP7SpeciesIndex() {
		String sp0Name = SP0Name.sp0_AC.getText();
		assertThat(VdypMethods.getVDYP7SpeciesIndex(sp0Name), equalTo(SP0Name.sp0_AC));
		assertThat(VdypMethods.getVDYP7SpeciesIndex("ZZZ"), equalTo(SP0Name.sp0_UNKNOWN));
		assertThat(VdypMethods.getVDYP7SpeciesIndex(null), equalTo(SP0Name.sp0_UNKNOWN));
	}
}