package prompto.problem;

import prompto.parser.ISection;

public class UnknowAttributeError extends SyntaxProblemBase {

	String name;
	
	public UnknowAttributeError(String name, ISection section) {
		super(section);
		this.name = name;
	}

	@Override
	public Type getType() {
		return Type.ERROR;
	}
	
	@Override
	public String getMessage() {
		return "Unknown attribute:" + name;
	}

}
