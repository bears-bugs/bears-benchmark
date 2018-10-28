package prompto.error;

import prompto.expression.IExpression;
import prompto.runtime.Context;


public class UserError extends ExecutionError {

	private static final long serialVersionUID = 1L;

	IExpression expression;
	
	public UserError(IExpression expression) {
		this.expression = expression;
	}
	
	@Override
	public IExpression getExpression(Context context) {
		return expression;
	}
}
