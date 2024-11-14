package ca.bc.gov.nrs.vdyp.backend.v1.api.factories;

import ca.bc.gov.nrs.vdyp.backend.v1.api.ProjectionApiService;

public class ProjectionApiServiceFactory {

	public static ProjectionApiService getProjectionApi() {
		return new ProjectionApiService();
	}
}
