package prompto.problem;

import prompto.parser.ISection;

public class UnknownIdentifierError extends SyntaxProblemBase {

	String name;
	
	public UnknownIdentifierError(String name, ISection section) {
		super(section);
		this.name = name;
	}

	@Override
	public Type getType() {
		return Type.ERROR;
	}
	
	@Override
	public String getMessage() {
		return "Unknown identifier: " + name;
	}

}
