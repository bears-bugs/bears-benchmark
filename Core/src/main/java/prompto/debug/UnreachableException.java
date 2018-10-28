package prompto.debug;

@SuppressWarnings("serial")
public class UnreachableException extends RuntimeException {

	public UnreachableException() {
	}

	public UnreachableException(String message) {
		super(message);
	}

	public UnreachableException(Throwable cause) {
		super(cause);
	}

	public UnreachableException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnreachableException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
