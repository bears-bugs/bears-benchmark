package prompto.problem;

import prompto.parser.ISection;

public class UnknownMethodError extends SyntaxProblemBase {

	String name;
	
	public UnknownMethodError(String name, ISection section) {
		super(section);
		this.name = name;
	}

	@Override
	public Type getType() {
		return Type.ERROR;
	}
	
	@Override
	public String getMessage() {
		return "Unknown method: " + name;
	}

}
