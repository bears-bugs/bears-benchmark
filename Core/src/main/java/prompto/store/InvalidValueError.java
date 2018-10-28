package prompto.store;

import prompto.error.ExecutionError;
import prompto.expression.IExpression;
import prompto.literal.TextLiteral;
import prompto.runtime.Context;

public class InvalidValueError extends ExecutionError {

	private static final long serialVersionUID = 1L;

	public InvalidValueError(String message) {
		super(message);
	}
	
	@Override
	public IExpression getExpression(Context context) {
		return new TextLiteral(super.getMessage());
	}

}
