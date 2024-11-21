package ca.bc.gov.nrs.vdyp.backend.v1.gen.api;

import ca.bc.gov.nrs.vdyp.backend.v1.api.HelpApiService;
import ca.bc.gov.nrs.vdyp.backend.v1.api.NotFoundException;
import ca.bc.gov.nrs.vdyp.backend.v1.gen.model.ParameterDetailsMessage;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("/v8/help")
@io.swagger.annotations.Api(description = "the help API")
@jakarta.annotation.Generated(
		value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-11-12T09:52:55.097945-08:00[America/Vancouver]", comments = "Generator version: 7.9.0"
)
public class HelpResourceApi implements ResourceApi {

	@Inject
	private HelpApiService helpService;

	@jakarta.ws.rs.GET
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(
			value = "returns a detailed description of the parameters available when executing a projection.", notes = "", response = ParameterDetailsMessage.class, responseContainer = "List", authorizations = {
					@io.swagger.annotations.Authorization(
							value = "accessCode", scopes = {
									@io.swagger.annotations.AuthorizationScope(
											scope = "read", description = "allows reading resources"
									),
									@io.swagger.annotations.AuthorizationScope(
											scope = "write", description = "allows modifying resources"
									) }
					) }, tags = {}
	)
	@io.swagger.annotations.ApiResponses(
			value = { @io.swagger.annotations.ApiResponse(
					code = 200, message = "OK", response = ParameterDetailsMessage.class, responseContainer = "List"
			) }
	)
	public Response helpGet(@Context UriInfo uriInfo /* , @Context SecurityContext securityContext */)
			throws NotFoundException {
		return Response.ok(helpService.helpGet(uriInfo, null /* securityContext */)).build();
	}

	@Override
	public String getPath() {
		return "/v8/help";
	}
}
