package prompto.problem;

import prompto.parser.ISection;

public class IllegalMemberError extends SyntaxProblemBase {

	String name;
	
	public IllegalMemberError(String name, ISection section) {
		super(section);
		this.name = name;
	}

	@Override
	public Type getType() {
		return Type.ERROR;
	}
	
	@Override
	public String getMessage() {
		return "Cannot read member from " + name;
	}

}
