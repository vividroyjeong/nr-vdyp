package ca.bc.gov.nrs.api.v1.endpoints;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.nrs.vdyp.backend.v1.gen.api.ParametersProvider;
import ca.bc.gov.nrs.vdyp.backend.v1.gen.model.Filters;
import ca.bc.gov.nrs.vdyp.backend.v1.gen.model.Parameters;
import ca.bc.gov.nrs.vdyp.backend.v1.gen.model.Parameters.CombineAgeYearRangeEnum;
import ca.bc.gov.nrs.vdyp.backend.v1.gen.model.Parameters.MetadataToOutputEnum;
import ca.bc.gov.nrs.vdyp.backend.v1.gen.model.Parameters.OutputFormatEnum;
import ca.bc.gov.nrs.vdyp.backend.v1.gen.model.Parameters.SelectedDebugOptionsEnum;
import ca.bc.gov.nrs.vdyp.backend.v1.gen.model.Parameters.SelectedExecutionOptionsEnum;
import ca.bc.gov.nrs.vdyp.backend.v1.gen.model.ProgressFrequency;
import ca.bc.gov.nrs.vdyp.backend.v1.gen.model.ProgressFrequency.EnumValue;
import ca.bc.gov.nrs.vdyp.backend.v1.gen.model.UtilizationParameter;
import ca.bc.gov.nrs.vdyp.backend.v1.gen.model.UtilizationParameter.ValueEnum;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.common.constraint.Assert;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;

@QuarkusTest
public class ParametersProviderTest {

	@Test
	public void testParametersProvider() throws WebApplicationException, IOException {
		Parameters op = new Parameters();

		op.addSelectedDebugOptionsItem(SelectedDebugOptionsEnum.DO_INCLUDE_DEBUG_ENTRY_EXIT);
		op.addSelectedDebugOptionsItem(SelectedDebugOptionsEnum.DO_INCLUDE_DEBUG_INDENT_BLOCKS);
		op.addSelectedDebugOptionsItem(SelectedDebugOptionsEnum.DO_INCLUDE_DEBUG_ROUTINE_NAMES);
		op.addSelectedDebugOptionsItem(SelectedDebugOptionsEnum.DO_INCLUDE_DEBUG_TIMESTAMPS);

		op.addSelectedExecutionOptionsItem(SelectedExecutionOptionsEnum.BACK_GROW_ENABLED);
		op.addSelectedExecutionOptionsItem(
				SelectedExecutionOptionsEnum.DO_ALLOW_BASAL_AREA_AND_TREES_PER_HECTARE_VALUE_SUBSTITUTION
		);
		op.addSelectedExecutionOptionsItem(SelectedExecutionOptionsEnum.DO_ENABLE_DEBUG_LOGGING);
		op.addSelectedExecutionOptionsItem(SelectedExecutionOptionsEnum.DO_ENABLE_ERROR_LOGGING);
		op.addSelectedExecutionOptionsItem(SelectedExecutionOptionsEnum.DO_ENABLE_PROGRESS_LOGGING);
		op.addSelectedExecutionOptionsItem(
				SelectedExecutionOptionsEnum.DO_FORCE_CALENDAR_YEAR_INCLUSION_IN_YIELD_TABLES
		);
		op.addSelectedExecutionOptionsItem(SelectedExecutionOptionsEnum.DO_FORCE_CURRENT_YEAR_INCLUSION_IN_YIELD_TABLES);
		op.addSelectedExecutionOptionsItem(
				SelectedExecutionOptionsEnum.DO_FORCE_REFERENCE_YEAR_INCLUSION_IN_YIELD_TABLES
		);
		op.addSelectedExecutionOptionsItem(SelectedExecutionOptionsEnum.DO_INCLUDE_AGE_ROWS_IN_YIELD_TABLE);
		op.addSelectedExecutionOptionsItem(SelectedExecutionOptionsEnum.DO_INCLUDE_COLUMN_HEADERS_IN_YIELD_TABLE);
		op.addSelectedExecutionOptionsItem(SelectedExecutionOptionsEnum.DO_INCLUDE_FILE_HEADER);
		op.addSelectedExecutionOptionsItem(SelectedExecutionOptionsEnum.DO_INCLUDE_POLYGON_RECORD_ID_IN_YIELD_TABLE);
		op.addSelectedExecutionOptionsItem(SelectedExecutionOptionsEnum.DO_INCLUDE_PROJECTED_CFS_BIOMASS);
		op.addSelectedExecutionOptionsItem(SelectedExecutionOptionsEnum.DO_INCLUDE_PROJECTED_MOF_BIOMASS);
		op.addSelectedExecutionOptionsItem(SelectedExecutionOptionsEnum.DO_INCLUDE_PROJECTED_MOF_VOLUMES);
		op.addSelectedExecutionOptionsItem(SelectedExecutionOptionsEnum.DO_INCLUDE_PROJECTION_MODE_IN_YIELD_TABLE);
		op.addSelectedExecutionOptionsItem(
				SelectedExecutionOptionsEnum.DO_INCLUDE_SECONDARY_SPECIES_DOMINANT_HEIGHT_IN_YIELD_TABLE
		);
		op.addSelectedExecutionOptionsItem(SelectedExecutionOptionsEnum.DO_INCLUDE_SPECIES_PROJECTION);
		op.addSelectedExecutionOptionsItem(SelectedExecutionOptionsEnum.DO_INCLUDE_YEAR_ROWS_IN_YIELD_TABLE);
		op.addSelectedExecutionOptionsItem(SelectedExecutionOptionsEnum.DO_SAVE_INTERMEDIATE_FILES);
		op.addSelectedExecutionOptionsItem(SelectedExecutionOptionsEnum.DO_SUMMARIZE_PROJECTION_BY_LAYER);
		op.addSelectedExecutionOptionsItem(SelectedExecutionOptionsEnum.DO_SUMMARIZE_PROJECTION_BY_POLYGON);
		op.addSelectedExecutionOptionsItem(SelectedExecutionOptionsEnum.FORWARD_GROW_ENABLED);

		op.addUtilsItem(new UtilizationParameter().speciesName("AL").value(ValueEnum._12_5));
		op.addUtilsItem(new UtilizationParameter().speciesName("C").value(ValueEnum._17_5));
		op.addUtilsItem(new UtilizationParameter().speciesName("D").value(ValueEnum._22_5));
		op.addUtilsItem(new UtilizationParameter().speciesName("E").value(ValueEnum._4_0));
		op.addUtilsItem(new UtilizationParameter().speciesName("F").value(ValueEnum._7_5));
		op.addUtilsItem(new UtilizationParameter().speciesName("H").value(ValueEnum.EXCL));

		op.ageEnd(2030) //
				.ageIncrement(1) //
				.ageStart(2020) //
				.combineAgeYearRange(CombineAgeYearRangeEnum.DIFFERENCE) //
				.forceYear(2020);

		var filters = new Filters().maintainer("maintainer").mapsheet("mapsheet").polygon("polygon")
				.polygonId("polygonId");
		op.filters(filters);

		op.progressFrequency(new ProgressFrequency(ProgressFrequency.EnumValue.MAPSHEET));

		op.maxAgeEnd(3000) //
				.maxAgeIncrement(10) //
				.maxAgeStart(2030) //
				.metadataToOutput(MetadataToOutputEnum.ALL) //
				.minAgeEnd(2000) //
				.minAgeStart(1970) //
				.minAgeIncrement(1) //
				.outputFormat(OutputFormatEnum.CSV_YIELD_TABLE) //
				.yearEnd(2030) //
				.yearStart(2020);
		
		var objectMapper = new ObjectMapper();
		byte[] json = objectMapper.writeValueAsBytes(op);
		
		ParametersProvider provider = new ParametersProvider();
		
		Assert.assertTrue(provider.isReadable(Parameters.class, Parameters.class, null, MediaType.APPLICATION_JSON_TYPE));
		
		Parameters np = provider.readFrom(Parameters.class, Parameters.class, null, MediaType.APPLICATION_JSON_TYPE, null
				, new ByteArrayInputStream(json));
		
		Assert.assertTrue(op.equals(np));
		
		np.getProgressFrequency().setEnumValue(EnumValue.POLYGON);

		Assert.assertFalse(op.equals(np));
	}
}
