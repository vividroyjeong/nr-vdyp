package ca.bc.gov.nrs.vdyp.backend.v1.gen.responses;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

abstract public class VdypApiResponse {

	public static final String JSON_PROPERTY_LINKS = "links";
	@JsonProperty(JSON_PROPERTY_LINKS)
	private final Set<Link> links;

	VdypApiResponse(Set<Link> links) {
		this.links = links;
	}

	public Set<Link> getLinks() {
		return links;
	}
}
