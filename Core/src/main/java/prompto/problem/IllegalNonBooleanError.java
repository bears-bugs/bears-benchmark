package prompto.problem;

import prompto.parser.ISection;
import prompto.type.IType;

public class IllegalNonBooleanError extends SyntaxProblemBase {

	IType type;
	
	public IllegalNonBooleanError(ISection section, IType type) {
		super(section);
		this.type = type;
	}

	@Override
	public Type getType() {
		return Type.ERROR;
	}
	
	@Override
	public String getMessage() {
		return "Illegal expression type in test method, expected Boolean, got:" + type.getTypeName();
	}

}
