package prompto.error;

public class PromptoError extends RuntimeException {

	private static final long serialVersionUID = 1L;

	protected PromptoError() {
	}
	
	protected PromptoError(String message) {
		super(message);
	}

	protected PromptoError(Throwable cause) {
		super(cause);
	}

	public PromptoError(String message, Throwable cause) {
		super(message, cause);
	}


	
}
