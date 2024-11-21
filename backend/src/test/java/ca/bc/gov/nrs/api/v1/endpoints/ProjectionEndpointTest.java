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

	private final TestHelper testHelper;
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
				.statusCode(500).body(Matchers.containsString("Not supported"));
	}

	@Test
	void testProjectionDscv_shouldThrow() throws IOException {

		ProjectionDcsvPostRequest request = new ProjectionDcsvPostRequest();
		request.setProjectionParameters(new Parameters());
		request.setInputData(buildTestFile());

		given().basePath("/v8").when().body(request).contentType("application/json").post("/projection/dcsv").then()
				.statusCode(500).body(Matchers.containsString("Not supported"));
	}

	private File buildTestFile() throws IOException {
		Path tmpFile = Files.createTempFile("ProjectionEndpointTest", ".csv");

		OutputStream os = new ByteArrayOutputStream();
		os.write("Test data".getBytes());

		return tmpFile.toFile();
	}

//   @Test
//   void testGetUserById_givenValidID_shouldReturnTheUserAndStatusOK() {
//     given()r
//       .basePath("/api/v8")
//       .pathParam("id", userEntity.getId())
//       .when().get("/users/{id}")
//       .then()
//       .statusCode(200)
//       .body("name", equalTo(userEntity.getName()))
//       .body("email", equalTo(userEntity.getEmail()));
//   }

//   @Test
//   void testGetUserById_givenRandomID_shouldReturnTheUserAndStatusOK() {
//     given()
//       .basePath("/api/v8")
//       .pathParam("id", 20000)
//       .when().get("/users/{id}")
//       .then()
//       .statusCode(404);
//   }

//   @Test
//   void testCreateUser_givenValidPayload_shouldReturnStatusCreated() {
//     var name = faker.name().fullName();
//     var email = faker.internet().emailAddress();
//     User user = new User(null, name, email);
//     given()
//       .basePath("/api/v8")
//       .contentType(ContentType.JSON)
//       .body(user)
//       .when().post("/users")
//       .then()
//       .statusCode(201)
//       .body("name", equalTo(name))
//       .body("email", equalTo(email));
//   }

//   @Test
//   void testCreateUser_givenInValidPayload_shouldReturnStatusBadRequest() {
//     var name = faker.name().fullName();
//     var email = faker.internet().domainName();
//     User user = new User(null, name, email);
//     given()
//       .basePath("/api/v8")
//       .contentType(ContentType.JSON)
//       .body(user)
//       .when().post("/users")
//       .then()
//       .statusCode(400);
//   }

//   @Test
//   void testUpdateUser_givenValidPayload_shouldReturnStatusOK() {
//     var name = faker.name().fullName();
//     var email = faker.internet().emailAddress();
//     User user = new User(userEntity.getId(), name, email);
//     given()
//       .basePath("/api/v8")
//       .contentType(ContentType.JSON)
//       .pathParam("id", userEntity.getId())
//       .body(user)
//       .when().put("/users/{id}")
//       .then()
//       .statusCode(200)
//       .body("name", equalTo(user.name()))
//       .body("email", equalTo(user.email()));
//   }

//   @Test
//   void testDeleteUser_givenValidID_shouldReturnStatusNoContent() {
//     given()
//       .basePath("/api/v8")
//       .pathParam("id", userEntity.getId())
//       .when().delete("/users/{id}")
//       .then()
//       .statusCode(204);
//   }

//   @Test
//   void testDeleteUser_givenInvalidID_shouldReturnStatusNotFound() {
//     given()
//       .basePath("/api/v8")
//       .pathParam("id", 100003330)
//       .when().delete("/users/{id}")
//       .then()
//       .statusCode(404);
//   }

//   @Test
//   void testGetUserAddresses_noCondition_shouldReturnAllUsersAddressesAndStatusOK() {
//     given()
//       .basePath("/api/v8")
//       .pathParam("id", userEntity.getId())
//       .when().get("/users/{id}/addresses")
//       .then()
//       .statusCode(200)
//       .body("$.size()", equalTo(1));
//   }

//   @Test
//   void testCreateUserAddress_givenValidPayload_shouldCreateTheUserAddressAndReturnStatusCreated() {
//     var street = faker.address().streetAddress();
//     var city =faker.address().city();
//     var state =faker.address().state();
//     var zipCode= faker.address().zipCode();
//     UserAddress userAddress = new UserAddress(null, street, city, state, zipCode, userEntity.getId());
//     given()
//       .basePath("/api/v8")
//       .contentType(ContentType.JSON)
//       .pathParam("id", userEntity.getId())
//       .body(userAddress)
//       .when().post("/users/{id}/addresses")
//       .then()
//       .statusCode(201)
//       .body("street", equalTo(street))
//       .body("city", equalTo(city))
//       .body("state", equalTo(state))
//       .body("zipCode", equalTo(zipCode));
//   }

//   @Test
//   void testUpdateUserAddress_givenValidPayload_shouldUpdateTheUserAddressAndReturnStatusOK() {
//     var street = faker.address().streetAddress();
//     var city =faker.address().city();
//     var state =faker.address().state();
//     var zipCode= faker.address().zipCode();
//     UserAddress userAddress = new UserAddress(addressEntity.getId(), street, city, state, zipCode, userEntity.getId());
//     given()
//       .basePath("/api/v8")
//       .contentType(ContentType.JSON)
//       .pathParam("id", userEntity.getId())
//       .pathParam("addressId", addressEntity.getId())
//       .body(userAddress)
//       .when().put("/users/{id}/addresses/{addressId}")
//       .then()
//       .statusCode(200)
//       .body("street", equalTo(street))
//       .body("city", equalTo(city))
//       .body("state", equalTo(state))
//       .body("zipCode", equalTo(zipCode));
//   }

//   @Test
//   void testDeleteUserAddress_givenValidPayload_shouldDeleteTheUserAddressAndReturnStatusNoContent() {
//     given()
//       .basePath("/api/v8")
//       .pathParam("id", userEntity.getId())
//       .pathParam("addressId", addressEntity.getId())
//       .when().delete("/users/{id}/addresses/{addressId}")
//       .then()
//       .statusCode(204);
//   }
}
