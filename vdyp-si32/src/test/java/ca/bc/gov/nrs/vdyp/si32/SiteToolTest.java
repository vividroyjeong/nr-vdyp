package ca.bc.gov.nrs.vdyp.si32;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ca.bc.gov.nrs.vdyp.common_calculators.Height2SiteIndex;
import ca.bc.gov.nrs.vdyp.common_calculators.SiteIndexNames;
import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.CommonCalculatorException;
import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.LessThan13Exception;
import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.NoAnswerException;
import ca.bc.gov.nrs.vdyp.si32.cfs.CfsBiomassConversionSupportedGenera;
import ca.bc.gov.nrs.vdyp.si32.cfs.CfsBiomassConversionSupportedSpecies;
import ca.bc.gov.nrs.vdyp.si32.cfs.CfsDeadConversionParams;
import ca.bc.gov.nrs.vdyp.si32.cfs.CfsLiveConversionParams;
import ca.bc.gov.nrs.vdyp.si32.cfs.CfsTreeSpecies;
import ca.bc.gov.nrs.vdyp.si32.enumerations.SpeciesRegion;
import ca.bc.gov.nrs.vdyp.si32.site.NameFormat;
import ca.bc.gov.nrs.vdyp.si32.site.SiteTool;
import ca.bc.gov.nrs.vdyp.si32.vdyp.SP64Name;
import ca.bc.gov.nrs.vdyp.si32.vdyp.SpeciesTable;
import ca.bc.gov.nrs.vdyp.sindex.Reference;
import ca.bc.gov.nrs.vdyp.sindex.Sindxdll;

class SiteToolTest {

	private static SpeciesTable speciesTable;

	@BeforeAll
	static void atStart() {
		speciesTable = new SpeciesTable();
	}

	@Test
	void test_lcl_MoFSP64ToCFSSpecies() {

		SP64Name.Iterator i = new SP64Name.Iterator();

		int namesSeen = 0;
		while (i.hasNext()) {
			namesSeen += 1;
			SP64Name sp64Name = i.next();

			CfsBiomassConversionSupportedSpecies result = SiteTool.lcl_MoFSP64ToCFSSpecies(sp64Name.getText());
			assertThat(result, notNullValue(CfsBiomassConversionSupportedSpecies.class));

		}

		assertThat(namesSeen, is(SP64Name.size()));
	}

	@Test
	void test_lcl_InternalSpeciesIndexToString() {
		assertThat(SiteTool.lcl_InternalSpeciesIndexToString(null), is("??"));
		assertThat(SiteTool.lcl_InternalSpeciesIndexToString(CfsBiomassConversionSupportedSpecies.UNKNOWN), is("??"));
		assertThat(
				SiteTool.lcl_InternalSpeciesIndexToString(CfsBiomassConversionSupportedSpecies.AC), is(
						CfsBiomassConversionSupportedSpecies.AC.getText()
				)
		);
	}

	@Test
	void test_lcl_InternalGenusIndexToString() {
		assertThat(SiteTool.lcl_InternalGenusIndexToString(null), is("genusInt_INVALID"));
		assertThat(
				SiteTool.lcl_InternalGenusIndexToString(CfsBiomassConversionSupportedGenera.INVALID), is(
						"genusInt_INVALID"
				)
		);
		assertThat(
				SiteTool.lcl_InternalGenusIndexToString(CfsBiomassConversionSupportedGenera.AC), is(
						CfsBiomassConversionSupportedSpecies.AC.getText()
				)
		);
	}

	@Test
	void test_lcl_LiveConversionParamToString() {
		assertThat(SiteTool.lcl_LiveConversionParamToString(null, null), is("cfsLiveParam_UNKNOWN"));
		assertThat(
				SiteTool.lcl_LiveConversionParamToString(CfsLiveConversionParams.A_NONMERCH, null), is("A_NONMERCH")
		);
		assertThat(SiteTool.lcl_LiveConversionParamToString(null, NameFormat.ENUM_STR), is("cfsLiveParam_UNKNOWN"));
		assertThat(SiteTool.lcl_LiveConversionParamToString(null, NameFormat.CAT_NAME), is("??"));
		assertThat(
				SiteTool.lcl_LiveConversionParamToString(CfsLiveConversionParams.A_NONMERCH, NameFormat.NAME_ONLY), is(
						"A"
				)
		);
		assertThat(
				SiteTool.lcl_LiveConversionParamToString(CfsLiveConversionParams.A_NONMERCH, NameFormat.CAT_ONLY), is(
						"Non-Merch"
				)
		);
		assertThat(
				SiteTool.lcl_LiveConversionParamToString(CfsLiveConversionParams.A_NONMERCH, NameFormat.CAT_NAME), is(
						"Non-Merch A"
				)
		);
	}

	@Test
	void test_lcl_DeadConversionParamToString() {
		assertThat(SiteTool.lcl_DeadConversionParamToString(null, null), is("cfsDeadParam_UNKNOWN"));
		assertThat(SiteTool.lcl_DeadConversionParamToString(CfsDeadConversionParams.PROP1, null), is("PROP1"));
		assertThat(SiteTool.lcl_DeadConversionParamToString(null, NameFormat.ENUM_STR), is("cfsDeadParam_UNKNOWN"));
		assertThat(SiteTool.lcl_DeadConversionParamToString(null, NameFormat.CAT_NAME), is("??"));
		assertThat(
				SiteTool.lcl_DeadConversionParamToString(CfsDeadConversionParams.PROP1, NameFormat.NAME_ONLY), is("P1")
		);
		assertThat(
				SiteTool.lcl_DeadConversionParamToString(CfsDeadConversionParams.PROP1, NameFormat.CAT_ONLY), is("Dead")
		);
		assertThat(
				SiteTool.lcl_DeadConversionParamToString(CfsDeadConversionParams.PROP1, NameFormat.CAT_NAME), is(
						"Dead P1"
				)
		);
	}

	@Test
	void test_cfsSpcsToCfsSpcsNum() {
		assertThat(SiteTool.cfsSpcsToCfsSpcsNum(null), is(CfsTreeSpecies.UNKNOWN.getNumber()));
		assertThat(SiteTool.cfsSpcsToCfsSpcsNum(CfsTreeSpecies.ALDER), is(CfsTreeSpecies.ALDER.getNumber()));
	}

	@Test
	void test_getIsDeciduous() {
		assertThat(SiteTool.getIsDeciduous(SP64Name.A.getIndex()), is(true));
		assertThat(SiteTool.getIsDeciduous(SP64Name.BN.getIndex()), is(false));
		assertThat(SiteTool.getIsDeciduous(Integer.MAX_VALUE), is(false));
	}

	@Test
	void test_getIsSoftwood() {
		assertThat(SiteTool.getIsSoftwood("A"), is(false));
		assertThat(SiteTool.getIsSoftwood("BN"), is(true));
		assertThat(SiteTool.getIsSoftwood(null), is(false));
	}

	@Test
	void test_getIsPine() {
		assertThat(SiteTool.getIsPine("A"), is(false));
		assertThat(SiteTool.getIsPine("PL"), is(true));
		assertThat(SiteTool.getIsPine(null), is(false));
	}

	@Test
	void test_getSpeciesCFSSpcs() {
		assertThat(SiteTool.getSpeciesCFSSpcs(SP64Name.A.getText()), is(CfsTreeSpecies.UNKNOWN));
		assertThat(SiteTool.getSpeciesCFSSpcs(SP64Name.B.getText()), is(CfsTreeSpecies.FIR));
		assertThat(SiteTool.getSpeciesCFSSpcs(null), is(CfsTreeSpecies.UNKNOWN));
	}

	@Test
	void test_getSpeciesCFSSpcsNum() {
		assertThat(SiteTool.getSpeciesCFSSpcsNum(SP64Name.A.getText()), is(CfsTreeSpecies.UNKNOWN.getNumber()));
		assertThat(SiteTool.getSpeciesCFSSpcsNum(SP64Name.B.getText()), is(CfsTreeSpecies.FIR.getNumber()));
		assertThat(SiteTool.getSpeciesCFSSpcsNum(null), is(-1));
	}

	@Test
	void test_htAgeToSI() throws CommonCalculatorException {
		assertThrows(
				LessThan13Exception.class, () -> SiteTool
						.heightAndAgeToSiteIndex(0, 0, Height2SiteIndex.SI_AT_BREAST, 1.0, 0)
		);
		assertThrows(
				NoAnswerException.class, () -> SiteTool
						.heightAndAgeToSiteIndex(0, 0, Height2SiteIndex.SI_AT_TOTAL, 0.0, 0)
		);
		assertThrows(
				NoAnswerException.class, () -> SiteTool
						.heightAndAgeToSiteIndex(0, 0, Height2SiteIndex.SI_AT_TOTAL, 23.0, 0)
		);
		assertThat(
				SiteTool.heightAndAgeToSiteIndex(
						Sindxdll.SI_AT_GOUDIE, 10.0, Height2SiteIndex.SI_AT_BREAST, 23.0, Height2SiteIndex.SI_EST_DIRECT
				), is(34.30)
		);
		assertThat(
				SiteTool.heightAndAgeToSiteIndex(
						Sindxdll.SI_AT_GOUDIE, 10.0, Height2SiteIndex.SI_AT_BREAST, 23.0, Height2SiteIndex.SI_EST_ITERATE
				), is(69.45)
		);
		assertThat(
				SiteTool.heightAndAgeToSiteIndex(
						Sindxdll.SI_FDI_THROWER, 10.0, Height2SiteIndex.SI_AT_BREAST, 23.0, Height2SiteIndex.SI_EST_DIRECT
				), is(84.31)
		);
		assertThat(
				SiteTool.heightAndAgeToSiteIndex(
						Sindxdll.SI_FDI_THROWER, 10.0, Height2SiteIndex.SI_AT_BREAST, 23.0, Height2SiteIndex.SI_EST_ITERATE
				), is(87.60)
		);
	}

	@Test
	void test_htSIToAge() throws CommonCalculatorException {
		assertThrows(
				LessThan13Exception.class, () -> SiteTool
						.heightAndSiteIndexToAge(0, 1.0, Height2SiteIndex.SI_AT_BREAST, 1.0, 0)
		);
		assertThrows(
				LessThan13Exception.class, () -> SiteTool
						.heightAndSiteIndexToAge(0, 10.0, Height2SiteIndex.SI_AT_BREAST, 1.1, 0.0)
		);
		assertThat(SiteTool.heightAndSiteIndexToAge(0, 0.0, Height2SiteIndex.SI_AT_TOTAL, 1.0, 0), is(0.0));
		assertThat(
				round(
						SiteTool.heightAndSiteIndexToAge(
								Sindxdll.SI_FDI_THROWER, 10.0, Height2SiteIndex.SI_AT_BREAST, 47.0, 5.0
						), 2
				), is(8.54)
		);
	}

	@Test
	void test_ageSIToHt() throws CommonCalculatorException {
		assertThat(
				round(
						SiteTool.ageAndSiteIndexToHeight(
								Sindxdll.SI_FDI_THROWER, 10.0, Height2SiteIndex.SI_AT_TOTAL, 30.0, 5.0
						), 2
				), is(4.10)
		);
	}

	@Test
	void test_yearsToBreastHeight() throws CommonCalculatorException {
		assertThat(
				SiteTool.yearsToBreastHeight(Sindxdll.SI_FDI_THROWER, 30.0), is(7.3)
		);
	}

	@Test
	void test_getSICurveName() {
		assertThat(SiteTool.getSICurveName(1000), is(SiteTool.UNKNOWN_CURVE_RESULT));
		assertThat(
				SiteTool.getSICurveName(Sindxdll.SI_ACT_THROWER), is(
						SiteIndexNames.si_curve_name[Sindxdll.SI_ACT_THROWER]
				)
		);
	}

	@Test
	void test_getNumSpecies() {
		assertThat(SiteTool.getNumSpecies(), is(speciesTable.getNSpecies()));
	}

	@Test
	void test_getSpeciesShortName() {
		assertThat(SiteTool.getSpeciesShortName(Integer.MAX_VALUE), is(SP64Name.UNKNOWN.getText()));
		assertThat(SiteTool.getSpeciesShortName(SP64Name.A.getIndex()), is("A"));
	}

	@Test
	void test_getSpeciesIndex() {
		assertThat(SiteTool.getSpeciesIndex("ZZZZ"), is(SpeciesTable.UNKNOWN_ENTRY_INDEX));
		assertThat(SiteTool.getSpeciesIndex("A"), is(SP64Name.A.getIndex()));
	}

	@Test
	void test_getSpeciesFullName() {
		assertThat(SiteTool.getSpeciesFullName("ZZZZ"), is(SpeciesTable.DefaultEntry.fullName()));
		assertThat(SiteTool.getSpeciesFullName("A"), is(speciesTable.getByCode("A").details().fullName()));
	}

	@Test
	void test_getSpeciesLatinName() {
		assertThat(SiteTool.getSpeciesLatinName("ZZZZ"), is(SpeciesTable.DefaultEntry.latinName()));
		assertThat(SiteTool.getSpeciesLatinName("A"), is(speciesTable.getByCode("A").details().latinName()));
	}

	@Test
	void test_getSpeciesGenusCode() {
		assertThat(SiteTool.getSpeciesGenusCode("ZZZZ"), is(SpeciesTable.DefaultEntry.genusName()));
		assertThat(SiteTool.getSpeciesGenusCode("A"), is(speciesTable.getByCode("A").details().genusName()));
	}

	@Test
	void test_getSpeciesSINDEXCode() {
		assertThat(SiteTool.getSpeciesSINDEXCode("ZZZZ", true), is(""));
		assertThat(SiteTool.getSpeciesSINDEXCode("A", false), is("At"));
	}

	@Test
	void test_getSpeciesVDYP7Code() {
		assertThat(SiteTool.getSpeciesVDYP7Code("ZZZZ"), is(""));
		assertThat(SiteTool.getSpeciesVDYP7Code("A"), is("AC"));
	}

	@Test
	void test_setSICurve() {
		int currentSiteCurve = SiteTool.getSICurve("ABAL", true);
		assertThat(SiteTool.setSICurve("ABAL", true, currentSiteCurve + 1), is(currentSiteCurve));
		assertThat(SiteTool.getSICurve("ABAL", true), is(currentSiteCurve + 1));
	}

	@Test
	void test_getSICurve() {
		assertThat(SiteTool.getSICurve("ABAL", true), is(119));
	}

	@Test
	void test_getSiteCurveSINDEXSpecies() {
		assertThat(SiteTool.getSiteCurveSINDEXSpecies(2000), is(""));
		assertThat(SiteTool.getSiteCurveSINDEXSpecies(119), is("Sw"));
	}

	@Test
	void test_getSpeciesDefaultCrownClosure() {
		assertThat(SiteTool.getSpeciesDefaultCrownClosure("ZZZZ", true), is(-1.0f));
		assertThat(
				SiteTool.getSpeciesDefaultCrownClosure("ABAL", true), is(
						speciesTable.getByCode("ABAL").details().defaultCrownClosure()[SpeciesRegion.COAST.ordinal()]
				)
		);
	}

	@Test
	void test_fillInAgeTriplet() {
		Reference<Double> rTotalAge = new Reference<>();
		Reference<Double> rBreastHeightAge = new Reference<>();
		Reference<Double> rYTBH = new Reference<>();
		
		rTotalAge.set(10.0);
		rBreastHeightAge.set(5.0);
		rYTBH.set(-9.0);
		SiteTool.fillInAgeTriplet(rTotalAge, rBreastHeightAge, rYTBH);
		assertThat(rYTBH.get(), is(5.5));
		
		rTotalAge.set(-9.0);
		rBreastHeightAge.set(5.0);
		rYTBH.set(6.0);
		SiteTool.fillInAgeTriplet(rTotalAge, rBreastHeightAge, rYTBH);
		assertThat(rTotalAge.get(), is(10.5));
		
		rTotalAge.set(10.0);
		rBreastHeightAge.set(-9.0);
		rYTBH.set(7.0);
		SiteTool.fillInAgeTriplet(rTotalAge, rBreastHeightAge, rYTBH);
		assertThat(rBreastHeightAge.get(), is(3.5));
	}

	private double round(double d, int precision) {
		assert precision >= 0;
		double factor = Math.pow(10.0, precision);
		return Math.round(d * factor) / factor;
	}
}
