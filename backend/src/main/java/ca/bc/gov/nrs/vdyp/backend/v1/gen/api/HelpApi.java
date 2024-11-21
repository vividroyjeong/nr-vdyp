package ca.bc.gov.nrs.vdyp.backend.v1.gen.api;

import java.util.List;

import ca.bc.gov.nrs.vdyp.backend.v1.api.HelpApiService;
import ca.bc.gov.nrs.vdyp.backend.v1.api.NotFoundException;
import ca.bc.gov.nrs.vdyp.backend.v1.api.factories.HelpApiServiceFactory;
import ca.bc.gov.nrs.vdyp.backend.v1.gen.model.ParameterDetailsMessage;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("/v1/help")

@io.swagger.annotations.Api(description = "the help API")
@jakarta.annotation.Generated(
		value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-11-12T09:52:55.097945-08:00[America/Vancouver]", comments = "Generator version: 7.9.0"
)
public class HelpApi {
	private final HelpApiService delegate;

	public HelpApi(/* @Context ServletConfig servletContext */) {
		HelpApiService delegate = null;

		/*
		 * if (servletContext != null) { String implClass = servletContext.getInitParameter("HelpApi.implementation");
		 * if (implClass != null && !"".equals(implClass.trim())) { try { delegate = (HelpApiService)
		 * Class.forName(implClass).getDeclaredConstructor().newInstance(); } catch (Exception e) { throw new
		 * RuntimeException(e); } } }
		 */

		if (delegate == null) {
			delegate = HelpApiServiceFactory.getHelpApi();
		}

		this.delegate = delegate;
	}

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
	public List<ParameterDetailsMessage> helpGet(/* @Context SecurityContext securityContext */)
			throws NotFoundException {
		return delegate.helpGet(null /* securityContext */);
	}
}
