package prompto.problem;

import prompto.parser.ISection;

public class AmbiguousIdentifierError extends SyntaxProblemBase {

	String name;
	
	public AmbiguousIdentifierError(String name, ISection section) {
		super(section);
		this.name = name;
	}

	@Override
	public Type getType() {
		return Type.ERROR;
	}
	
	@Override
	public String getMessage() {
		return "Ambiguous identifier: " + name;
	}

}
