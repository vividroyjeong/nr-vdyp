package ca.bc.gov.nrs.vdyp.sindex;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

import ca.bc.gov.nrs.vdyp.si32.cfs.CfsBiomassConversionCoefficientsDead;
import ca.bc.gov.nrs.vdyp.si32.cfs.CfsBiomassConversionCoefficientsForGenus;
import ca.bc.gov.nrs.vdyp.si32.cfs.CfsBiomassConversionCoefficientsForSpecies;
import ca.bc.gov.nrs.vdyp.si32.cfs.CfsDensity;
import ca.bc.gov.nrs.vdyp.si32.cfs.CfsMethods;
import ca.bc.gov.nrs.vdyp.si32.cfs.CfsSP0Densities;
import ca.bc.gov.nrs.vdyp.si32.cfs.CfsSpeciesMethods;
import ca.bc.gov.nrs.vdyp.si32.cfs.CfsTreeClass;
import ca.bc.gov.nrs.vdyp.si32.cfs.CfsTreeGenus;
import ca.bc.gov.nrs.vdyp.si32.cfs.CfsTreeSpecies;
import ca.bc.gov.nrs.vdyp.si32.vdyp.SP0Name;

class CfsMethodsTest {

	@Test
	void test_CFS_CFSTreeClassToString() {
		assertThat(
				CfsMethods.cfsTreeClassToString(CfsTreeClass.cfsTreeCls_LiveNoPath.getIndex()), equalTo(
						CfsTreeClass.cfsTreeCls_LiveNoPath.getDescription()
				)
		);
		assertThat(CfsMethods.cfsTreeClassToString(100), equalTo(CfsTreeClass.cfsTreeCls_UNKNOWN.getDescription()));
	}

	@Test
	void test_CFS_CFSGenusToString() {
		assertThat(
				CfsMethods.cfsGenusToString(CfsTreeGenus.cfsGenus_Birch), equalTo(
						CfsTreeGenus.cfsGenus_Birch.getGenusName()
				)
		);
		assertThat(CfsMethods.cfsGenusToString(null), equalTo(CfsTreeGenus.cfsGenus_UNKNOWN.getGenusName()));
	}
	
	@Test
	void test_CFS_CFSSP0DensityFunctions() {
		assertThat(CfsMethods.cfsSP0DensityMax(null), equalTo(CfsSP0Densities.DEFAULT_VALUE));
		assertThat(CfsMethods.cfsSP0DensityMax(SP0Name.sp0_AC), equalTo(564.00F));
		assertThat(CfsMethods.cfsSP0DensityMean(SP0Name.sp0_AC), equalTo(295.00F));
		assertThat(CfsMethods.cfsSP0DensityMin(SP0Name.sp0_AC), equalTo(229.00F));
	}
	
	@Test
	void test_CFS_StringToCfsSpeciesTest() {
		assertThat(CfsMethods.stringToCfsSpecies("Black Spruce"), equalTo(CfsTreeSpecies.cfsSpcs_SpruceBlack));
		assertThat(CfsMethods.stringToCfsSpecies("Black spruce"), equalTo(CfsTreeSpecies.cfsSpcs_SpruceBlack));
		assertThat(CfsMethods.stringToCfsSpecies("something"), equalTo(CfsTreeSpecies.cfsSpcs_UNKNOWN));
		assertThat(CfsMethods.stringToCfsSpecies(null), equalTo(CfsTreeSpecies.cfsSpcs_UNKNOWN));
	}
	
	@Test
	void test_CFS_CFSSpcsNumToCFSGenus() {
		assertThat(CfsMethods.cfsSpcsNumToCFSGenus(CfsTreeSpecies.cfsSpcs_SpruceBlack), equalTo(CfsTreeGenus.cfsGenus_Spruce));
		assertThat(CfsMethods.cfsSpcsNumToCFSGenus(null), equalTo(CfsTreeGenus.cfsGenus_UNKNOWN));
	}
	
	@Test
	void test_CFS_CFSBiomassConversionCoefficientArrays() {
		assertThat(CfsBiomassConversionCoefficientsDead.array[1][1].parms()[1], equalTo(0.29900000f));
		assertThat(CfsBiomassConversionCoefficientsForSpecies.array[1][1].parms()[1], equalTo(0.84904177f));
		assertThat(CfsBiomassConversionCoefficientsForGenus.array[1][1].parms()[1], equalTo(0.95456459f));
	}
	
	@Test
	public static void testGetSpeciesBySpeciesName() {
		String name = CfsTreeSpecies.cfsSpcs_AlderRed.getCfsSpeciesName();
		CfsTreeSpecies ts;
		
		ts = CfsSpeciesMethods.getSpeciesBySpeciesName(name);
		assertThat(ts, equalTo("Red alder"));
		
		ts = CfsSpeciesMethods.getSpeciesBySpeciesName(name.toLowerCase());
		assertThat(ts, equalTo("Red alder"));
		
		ts = CfsSpeciesMethods.getSpeciesBySpeciesName(name.toUpperCase());
		assertThat(ts, equalTo("Red alder"));
		
		ts = CfsSpeciesMethods.getSpeciesBySpeciesName(null);
		assertThat(ts, equalTo("Unknown Species"));
	}

	@Test
	public static void testGetGenusBySpecies() {
		CfsTreeGenus g = CfsSpeciesMethods.getGenusBySpecies(CfsTreeSpecies.cfsSpcs_AlderRed);
		assertThat(g, equalTo(CfsTreeGenus.cfsGenus_OtherBroadleaves));
		assertThat(CfsSpeciesMethods.getGenusBySpecies(null), equalTo(CfsTreeGenus.cfsGenus_UNKNOWN));
	}
	
	@Test
	public static void testGetSpeciesIndexBySpecies() {
		int r = CfsSpeciesMethods.getSpeciesIndexBySpecies(CfsTreeSpecies.cfsSpcs_AlderRed);
		assertThat(r, equalTo(1802));
		assertThat(CfsSpeciesMethods.getSpeciesIndexBySpecies(null), equalTo(-1));
	}
	
	@Test
	public static void testCfsSP0Densities() {
		assertThat(CfsSP0Densities.getValue(SP0Name.sp0_B, CfsDensity.MEAN_DENSITY_INDEX), equalTo(379.25F));
		assertThat(CfsSP0Densities.getValue(null, CfsDensity.MEAN_DENSITY_INDEX), equalTo(CfsSP0Densities.DEFAULT_VALUE));
		assertThat(CfsSP0Densities.getValue(SP0Name.sp0_B, null), equalTo(CfsSP0Densities.DEFAULT_VALUE));
	}
}