package prompto.verifier;

import prompto.compiler.CompilerException;

@SuppressWarnings("serial")
public class VerifierException extends CompilerException {

	public VerifierException(String msg) {
		super(msg);
	}

}
