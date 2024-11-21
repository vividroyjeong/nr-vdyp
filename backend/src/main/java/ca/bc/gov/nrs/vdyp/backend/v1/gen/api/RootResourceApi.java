package ca.bc.gov.nrs.vdyp.backend.v1.gen.api;

import ca.bc.gov.nrs.vdyp.backend.v1.api.NotFoundException;
import ca.bc.gov.nrs.vdyp.backend.v1.api.RootResourceService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("/v8")
@io.swagger.annotations.Api(description = "the default API")
@jakarta.annotation.Generated(
		value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-11-20T10:20:18.043409-08:00[America/Vancouver]", comments = "Generator version: 7.9.0"
)
public class RootResourceApi implements ResourceApi {

	@Inject
	private RootResourceService rootResourceApi;

	public RootResourceApi() {
	}

	@jakarta.ws.rs.GET

	@io.swagger.annotations.ApiOperation(
			value = "Return the top level resource of this service", notes = "Returns the top level resource of the service, including links to all other top level resources.", response = Void.class, authorizations = {
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
			value = { @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = Void.class) }
	)
	public Response rootGet(@Context UriInfo uriInfo /* , @Context SecurityContext securityContext */)
			throws NotFoundException {
		return Response.ok(rootResourceApi.rootGet(uriInfo, null)).build();
	}

	@Override
	public String getPath() {
		return "";
	}
}
