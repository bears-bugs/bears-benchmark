package prompto.problem;

import prompto.parser.ISection;

public class IllegalOperationError extends SyntaxProblemBase {

	String message;
	
	public IllegalOperationError(String message, ISection section) {
		super(section);
		this.message = message;
	}

	@Override
	public Type getType() {
		return Type.ERROR;
	}
	
	@Override
	public String getMessage() {
		return message;
	}

}
