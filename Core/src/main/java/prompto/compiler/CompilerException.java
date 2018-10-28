package prompto.compiler;


@SuppressWarnings("serial")
public class CompilerException extends RuntimeException {

	public CompilerException(Exception e) {
		super(e);
	}

	public CompilerException(String msg) {
		super(msg);
	}

}
