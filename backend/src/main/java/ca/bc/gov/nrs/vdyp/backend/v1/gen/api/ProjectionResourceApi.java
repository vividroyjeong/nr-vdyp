package ca.bc.gov.nrs.vdyp.backend.v1.gen.api;

import ca.bc.gov.nrs.vdyp.backend.v1.api.ProjectionApiService;
import ca.bc.gov.nrs.vdyp.backend.v1.gen.model.MessagesInner;
import ca.bc.gov.nrs.vdyp.backend.v1.gen.model.ProjectionDcsvPostRequest;
import ca.bc.gov.nrs.vdyp.backend.v1.gen.model.ProjectionHcsvPostRequest;
import ca.bc.gov.nrs.vdyp.backend.v1.gen.model.ProjectionScsvPostRequest;
import ca.bc.gov.nrs.vdyp.backend.v1.gen.responses.ProjectionResponse;
import io.swagger.annotations.ApiParam;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/v8/projection")
@io.swagger.annotations.Api(description = "the projection API")
@jakarta.annotation.Generated(
		value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-11-12T09:52:55.097945-08:00[America/Vancouver]", comments = "Generator version: 7.9.0"
)
public class ProjectionResourceApi implements ResourceApi {

	@Inject
	private ProjectionApiService projectionService;

	@jakarta.ws.rs.POST
	@Path("/dcsv")
	@Consumes({ "application/json" })
	@Produces({ "multipart/form-data", "application/json" })
	@io.swagger.annotations.ApiOperation(
			value = "Project the growth of one or more polygons to a given year.", notes = "Run a projection of polygons in the supplied DCSV formatted input file as  controlled by the parameters in the supplied projection parameters file.", response = ProjectionResponse.class, authorizations = {
					@io.swagger.annotations.Authorization(
							value = "accessCode", scopes = { @io.swagger.annotations.AuthorizationScope(
									scope = "read", description = "allows reading resources"
							), @io.swagger.annotations.AuthorizationScope(scope = "write", description = "allows modifying resources") }
					) }, tags = {}
	)
	@io.swagger.annotations.ApiResponses(
			value = { @io.swagger.annotations.ApiResponse(
					code = 201, message = "OK", response = ProjectionResponse.class
			), @io.swagger.annotations.ApiResponse(code = 400, message = "Client Error. Response content is a list of one or more messages describing the error.", response = MessagesInner.class, responseContainer = "List") }
	)
	public Response projectionDcsvPost(@ApiParam(value = "") @Valid ProjectionDcsvPostRequest projectionDcsvPostRequest
	/* , @Context SecurityContext securityContext */

	) {
		return projectionService.projectionDcsvPost(projectionDcsvPostRequest, null /* securityContext */);
	}

	@jakarta.ws.rs.POST
	@Path("/hcsv")
	@Consumes({ "application/json" })
	@Produces({ "multipart/form-data", "application/json" })
	@io.swagger.annotations.ApiOperation(
			value = "Project the growth of one or more polygons to a given year.", notes = "Run a projection of polygons in the supplied HCSV formatted input files as  controlled by the parameters in the supplied projection parameters file.", response = ProjectionResponse.class, authorizations = {
					@io.swagger.annotations.Authorization(
							value = "accessCode", scopes = { @io.swagger.annotations.AuthorizationScope(
									scope = "read", description = "allows reading resources"
							), @io.swagger.annotations.AuthorizationScope(scope = "write", description = "allows modifying resources") }
					) }, tags = {}
	)
	@io.swagger.annotations.ApiResponses(
			value = { @io.swagger.annotations.ApiResponse(
					code = 200, message = "OK", response = ProjectionResponse.class
			), @io.swagger.annotations.ApiResponse(code = 400, message = "Client Error. Response content is a list of one or more messages describing the error.", response = MessagesInner.class, responseContainer = "List") }
	)
	public Response projectionHcsvPost(
			//
			@ApiParam(value = "") @Valid ProjectionHcsvPostRequest projectionHcsvPostRequest //
	// , @Context SecurityContext securityContext
	) {
		return projectionService.projectionHcsvPost(projectionHcsvPostRequest, null /* securityContext */);
	}

	@jakarta.ws.rs.POST
	@Path("/scsv")
	@Consumes({ "application/json" })
	@Produces({ "multipart/form-data", "application/json" })
	@io.swagger.annotations.ApiOperation(
			value = "Project the growth of one or more polygons to a given year.", notes = "Run a projection of polygons in the supplied SCSV formatted input files as  controlled by the parameters in the supplied projection parameters file.", response = ProjectionResponse.class, authorizations = {
					@io.swagger.annotations.Authorization(
							value = "accessCode", scopes = { @io.swagger.annotations.AuthorizationScope(
									scope = "read", description = "allows reading resources"
							), @io.swagger.annotations.AuthorizationScope(scope = "write", description = "allows modifying resources") }
					) }, tags = {}
	)
	@io.swagger.annotations.ApiResponses(
			value = { @io.swagger.annotations.ApiResponse(
					code = 200, message = "OK", response = ProjectionResponse.class
			), @io.swagger.annotations.ApiResponse(code = 400, message = "Client Error. Response content is a list of one or more messages describing the error.", response = MessagesInner.class, responseContainer = "List") }
	)
	public Response projectionScsvPost(
			//
			@ApiParam(value = "") @Valid ProjectionScsvPostRequest projectionScsvPostRequest //
	// , @Context SecurityContext securityContext
	) {
		return projectionService.projectionScsvPost(projectionScsvPostRequest, null /* securityContext */);
	}

	@Override
	public String getPath() {
		return "/v8/help";
	}
}
