package ca.bc.gov.nrs.vdyp.application;

public class InitializationIncompleteException extends RuntimeException {

	private static final long serialVersionUID = -4549468703592060502L;

	public InitializationIncompleteException(String reason) {
		super(reason);
	}
}
