package prompto.declaration;

import prompto.error.PromptoError;
import prompto.runtime.Context;
import prompto.value.ClosureValue;
import prompto.value.IValue;

/* a dummy declaration to interpret closures in context */
public class ClosureDeclaration extends AbstractMethodDeclaration {

	ClosureValue closure;
	
	public ClosureDeclaration(ClosureValue closure) {
		super(closure.getName(),closure.getArguments(),closure.getReturnType());
		this.closure = closure;
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		return closure.interpret(context);
	}

}
