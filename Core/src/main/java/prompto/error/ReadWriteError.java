package prompto.error;

import prompto.expression.IExpression;
import prompto.grammar.Identifier;
import prompto.runtime.Context;

public class ReadWriteError extends ExecutionError {
	
	private static final long serialVersionUID = 1L;
	
	public ReadWriteError(String message) {
		super(message);
	}

	@Override
	public IExpression getExpression(Context context) {
		return context.getRegisteredSymbol(new Identifier("READ_WRITE"), true);
	}

}
