package prompto.problem;

import prompto.parser.ISection;
import prompto.type.IType;

public class IllegalComparisonError extends SyntaxProblemBase {

	IType t1, t2;
	
	public IllegalComparisonError(IType t1, IType t2, ISection section) {
		super(section);
		this.t1 = t1;
		this.t2 = t2;
	}

	@Override
	public Type getType() {
		return Type.ERROR;
	}
	
	@Override
	public String getMessage() {
		return "Cannot compare " + t1.getTypeName() + " to " + t2.getTypeName();
	}

}
