package ca.bc.gov.nrs.vdyp.io.parse;

public class ResourceParseException extends Exception {

	private static final long serialVersionUID = 5188546056230073563L;
	
	int line;

	public int getLine() {
		return line;
	}

	public ResourceParseException(int line,
			String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace
	) {
		super(message(line, message), cause, enableSuppression, writableStackTrace);
		this.line = line;
	}

	public ResourceParseException(int line, String message, Throwable cause) {
		super(message, cause);
		this.line = line;
	}

	public ResourceParseException(int line, String message) {
		super(message(line, message));
		this.line = line;
	}

	public ResourceParseException(int line, Throwable cause) {
		super(message(line, cause.getMessage()), cause);
		this.line = line;
	}	
	
	private static String message(int line, String message) {
		return String.format("Error at line %d: %s",line,message);
	}
}
