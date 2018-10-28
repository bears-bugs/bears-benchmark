package prompto.error;

public class SyntaxError extends PromptoError {

	private static final long serialVersionUID = 1L;

	public SyntaxError(String message) {
		super(message);
	}

	public SyntaxError(String message, Throwable cause) {
		super(message, cause);
	}
	
}
