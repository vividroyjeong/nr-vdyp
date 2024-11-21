package ca.bc.gov.nrs.api.v1.endpoints;

import static io.restassured.RestAssured.given;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.bc.gov.nrs.api.helpers.TestHelper;
import ca.bc.gov.nrs.vdyp.backend.v1.gen.model.Parameters;
import ca.bc.gov.nrs.vdyp.backend.v1.gen.model.ProjectionDcsvPostRequest;
import ca.bc.gov.nrs.vdyp.backend.v1.gen.model.ProjectionHcsvPostRequest;
import ca.bc.gov.nrs.vdyp.backend.v1.gen.model.ProjectionScsvPostRequest;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import net.datafaker.Faker;

@QuarkusTest
class ProjectionEndpointTest {

	@SuppressWarnings("unused")
	private final TestHelper testHelper;
	@SuppressWarnings("unused")
	private final Faker faker = new Faker();

	@Inject
	ProjectionEndpointTest(TestHelper testHelper) {
		this.testHelper = testHelper;
	}

	@BeforeEach
	void setup() {
	}

	@Test
	void testProjectionHscv_shouldReturnStatusOK() throws IOException {

		ProjectionHcsvPostRequest request = new ProjectionHcsvPostRequest();
		request.setLayerInputData(buildTestFile());
		request.setPolygonInputData(buildTestFile());
		request.setProjectionParameters(new Parameters());

		given().basePath("/v8").when().body(request).contentType("application/json").post("/projection/hcsv").then()
				.statusCode(201).and().contentType("multipart/form-data").and()
				.header("content-disposition", Matchers.startsWith("attachment;filename=\"vdyp-output-"))
				.body(Matchers.not(Matchers.empty()));
	}

	@Test
	void testProjectionSscv_shouldThrow() throws IOException {

		ProjectionScsvPostRequest request = new ProjectionScsvPostRequest();
		request.setLayerInputData(buildTestFile());
		request.setPolygonInputData(buildTestFile());
		request.setProjectionParameters(new Parameters());
		request.setHistoryInputData(buildTestFile());
		request.setNonVegetationInputData(buildTestFile());
		request.setOtherVegetationInputData(buildTestFile());
		request.setPolygonIdInputData(buildTestFile());
		request.setSpeciesInputData(buildTestFile());
		request.setVriAdjustInputData(buildTestFile());

		given().basePath("/v8").when().body(request).contentType("application/json").post("/projection/scsv").then()
				.statusCode(501);
	}

	@Test
	void testProjectionDscv_shouldThrow() throws IOException {

		ProjectionDcsvPostRequest request = new ProjectionDcsvPostRequest();
		request.setProjectionParameters(new Parameters());
		request.setInputData(buildTestFile());

		given().basePath("/v8").when().body(request).contentType("application/json").post("/projection/dcsv").then()
				.statusCode(501);
	}

	private File buildTestFile() throws IOException {
		Path tmpFile = Files.createTempFile("ProjectionEndpointTest", ".csv");

		OutputStream os = new ByteArrayOutputStream();
		os.write("Test data".getBytes());

		return tmpFile.toFile();
	}
}
