package ca.bc.gov.nrs.vdyp.backend.v1.api.factories;

import ca.bc.gov.nrs.vdyp.backend.v1.api.HelpApiService;

public class HelpApiServiceFactory {

	public static HelpApiService getHelpApi() {
		return new HelpApiService();
	}
}
