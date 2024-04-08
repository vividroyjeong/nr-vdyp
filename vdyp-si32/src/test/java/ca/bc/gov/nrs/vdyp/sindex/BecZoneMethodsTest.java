package ca.bc.gov.nrs.vdyp.sindex;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

import ca.bc.gov.nrs.vdyp.si32.bec.BecZone;
import ca.bc.gov.nrs.vdyp.si32.bec.BecZoneMethods;
import ca.bc.gov.nrs.vdyp.si32.vdyp.SP64Name;

class BecZoneMethodsTest {

	@Test
	void test_SiteTool_MoFBiomassCoefficient() {
		assertThat(
				BecZoneMethods.mofBiomassCoefficient(BecZone.bec_AT.getText(), SP64Name.sp64_A.getText()), equalTo(
						BecZoneMethods.mofBiomassCoeffs[SP64Name.sp64_A.getOffset()][BecZone.bec_AT.getOffset()]
				)
		);
		assertThat(
				BecZoneMethods.mofBiomassCoefficient("at", "a"), equalTo(
						BecZoneMethods.mofBiomassCoeffs[SP64Name.sp64_A.getOffset()][BecZone.bec_AT.getOffset()]
				)
		);
		assertThat(BecZoneMethods.mofBiomassCoefficient("??", "??"), equalTo(-1.0f));
		assertThat(BecZoneMethods.mofBiomassCoefficient(SP64Name.sp64_A.getText(), "??"), equalTo(-1.0f));
	}

	@Test
	void test_SiteTool_IndexToBecZone() {
		assertThat(BecZoneMethods.becZoneToIndex(BecZone.bec_AT.getText()), equalTo(BecZone.bec_AT));
		assertThat(BecZoneMethods.becZoneToIndex(null), equalTo(BecZone.bec_UNKNOWN));
	}

	@Test
	void test_SiteTool_BECZoneToCode() {
		assertThat(BecZoneMethods.becZoneToCode(BecZone.bec_AT), equalTo(BecZone.bec_AT.getText()));
		assertThat(BecZoneMethods.becZoneToCode(null), equalTo(BecZoneMethods.UNKNOWN_BEC_ZONE_TEXT));
	}
}