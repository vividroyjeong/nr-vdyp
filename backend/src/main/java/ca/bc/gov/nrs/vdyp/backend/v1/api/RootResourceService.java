package ca.bc.gov.nrs.vdyp.backend.v1.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.vdyp.backend.v1.gen.responses.RootResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;

@ApplicationScoped
public class RootResourceService {

	private static final Logger logger = LoggerFactory.getLogger(RootResourceService.class);

	public RootResponse rootGet(UriInfo uriInfo, SecurityContext securityContext) throws NotFoundException {

		logger.info("<rootGet");
		logger.info(">rootGet");

		return RootResponse.of(uriInfo);
	}
}
