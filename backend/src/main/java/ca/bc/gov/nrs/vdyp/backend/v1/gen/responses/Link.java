package ca.bc.gov.nrs.vdyp.backend.v1.gen.responses;

import ca.bc.gov.nrs.vdyp.backend.v1.gen.api.ResourceApi;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;

public record Link(String rel, String href, String method) {

	public static Link getSelfLink(Class<? extends ResourceApi> clazz, UriInfo uriInfo) {
		String rel = clazz.getName();
		String href = uriInfo.getAbsolutePath().toString();
		String method = "GET";
		return new Link(rel, href, method);
	}

	public static Link getLink(Class<? extends ResourceApi> clazz, UriInfo uriInfo, String method) {
		String rel = clazz.getName();
		String href = UriBuilder.fromUri(uriInfo.getBaseUri()).path(clazz).build().toString();
		return new Link(rel, href, method);
	}

	public static Link getLink(Class<? extends ResourceApi> clazz, String methodPath, UriInfo uriInfo, String method) {
		String rel = clazz.getName() + "+" + methodPath;
		String href = UriBuilder.fromUri(uriInfo.getBaseUri()).path(clazz).path(methodPath).build().toString();
		return new Link(rel, href, method);
	}
}
