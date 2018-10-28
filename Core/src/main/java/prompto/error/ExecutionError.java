package prompto.error;

import prompto.argument.UnresolvedArgument;
import prompto.expression.ConstructorExpression;
import prompto.expression.IExpression;
import prompto.grammar.ArgumentAssignment;
import prompto.grammar.ArgumentAssignmentList;
import prompto.grammar.INamed;
import prompto.grammar.Identifier;
import prompto.literal.TextLiteral;
import prompto.runtime.Context;
import prompto.runtime.ErrorVariable;
import prompto.type.CategoryType;
import prompto.value.IValue;

public abstract class ExecutionError extends PromptoError {

	private static final long serialVersionUID = 1L;

	protected ExecutionError() {
	}
	
	protected ExecutionError(String message) {
		super(message);
	}

	public abstract IExpression getExpression(Context context);

	public IValue interpret(Context context, Identifier errorName) throws PromptoError {
		IExpression exp = this.getExpression(context);
		if(exp==null) {
			ArgumentAssignmentList args = new ArgumentAssignmentList();
			args.add(new ArgumentAssignment(
					new UnresolvedArgument(new Identifier("name")), 
					new TextLiteral(this.getClass().getSimpleName())));
			args.add(new ArgumentAssignment(
					new UnresolvedArgument(new Identifier("text")), 
					new TextLiteral(this.getMessage())));
			exp = new ConstructorExpression( new CategoryType(new Identifier("Error")), null, args, true);
		}
		if(context.getRegisteredValue(INamed.class, errorName)==null)
			context.registerValue(new ErrorVariable(errorName));
		IValue error = exp.interpret(context);
		context.setValue(errorName, error);
		return error;
	}

	
}
