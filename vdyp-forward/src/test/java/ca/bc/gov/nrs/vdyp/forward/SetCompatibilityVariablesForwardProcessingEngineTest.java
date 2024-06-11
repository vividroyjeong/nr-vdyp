package ca.bc.gov.nrs.vdyp.forward;

import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import ca.bc.gov.nrs.vdyp.application.ProcessingException;
import ca.bc.gov.nrs.vdyp.forward.model.VdypEntity;
import ca.bc.gov.nrs.vdyp.forward.parsers.VdypPolygonDescriptionParser;
import ca.bc.gov.nrs.vdyp.io.parse.common.ResourceParseException;
import ca.bc.gov.nrs.vdyp.model.LayerType;
import ca.bc.gov.nrs.vdyp.model.UtilizationClass;
import ca.bc.gov.nrs.vdyp.model.VolumeVariable;

class SetCompatibilityVariablesForwardProcessingEngineTest extends ForwardProcessingEngineTest {

	@Test
	void testSetCompatibilityVariables() throws ResourceParseException, IOException, ProcessingException {
		var testPolygonDescription = VdypPolygonDescriptionParser.parse("01002 S000001 00     1970");

		var reader = new ForwardDataStreamReader(controlMap);

		var polygon = reader.readNextPolygon(testPolygonDescription);

		ForwardProcessingEngine fpe = new ForwardProcessingEngine(controlMap);
		fpe.processPolygon(polygon, ForwardProcessingEngine.ExecutionStep.SetCompatibilityVariables);
		
		assertThat(fpe.fps.getPolygonProcessingState().volumeEquationGroups, Matchers.is(new int[] { VdypEntity.MISSING_INTEGER_VALUE, 12, 20, 25, 37, 66 }));
		assertThat(fpe.fps.getPolygonProcessingState().decayEquationGroups, Matchers.is(new int[] { VdypEntity.MISSING_INTEGER_VALUE, 7, 14, 19, 31, 54 }));
		assertThat(fpe.fps.getPolygonProcessingState().breakageEquationGroups, Matchers.is(new int[] { VdypEntity.MISSING_INTEGER_VALUE, 5, 6, 12, 17, 28 }));

		assertThat(fpe.fps.getPolygonProcessingState().getCVVolume(1, UtilizationClass.U75TO125, VolumeVariable.CLOSE_UTIL_VOL, LayerType.PRIMARY), is(-0.000114440918));
	}
}
