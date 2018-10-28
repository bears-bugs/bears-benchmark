package prompto.problem;

import prompto.parser.ISection;

public class DuplicateError extends SyntaxProblemBase {

	String name;
	ISection other;
	
	public DuplicateError(String name, ISection section, ISection other) {
		super(section);
		this.name = name;
		this.other = other;
	}

	@Override
	public Type getType() {
		return Type.ERROR;
	}
	
	@Override
	public String getMessage() {
		return "Duplicate declaration: " + name + ", see: " + other.toString();
	}

}
