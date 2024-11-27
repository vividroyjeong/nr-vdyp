package ca.bc.gov.nrs.vdyp.backend.v1.api.impl.exceptions;

public class ProjectionException extends Exception {

	private static final long serialVersionUID = -3026466812172806593L;

	public ProjectionException(Exception cause) {
		super(cause);
	}

	public ProjectionException(String reason) {
		super(reason);
	}

	public ProjectionException(Exception cause, String reason) {
		super(reason, cause);
	}
}
