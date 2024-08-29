package ca.bc.gov.nrs.vdyp.forward;

import ca.bc.gov.nrs.vdyp.application.ProcessingException;

public class LambdaProcessingException extends RuntimeException {

	private static final long serialVersionUID = 7768827464368942152L;

	public LambdaProcessingException(ProcessingException e) {
		super(e);
	}
	
	@Override
	public ProcessingException getCause() {
		return (ProcessingException)super.getCause();
	}
}
