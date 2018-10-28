package prompto.expression;

import prompto.compiler.Flags;
import prompto.compiler.MethodInfo;
import prompto.compiler.ResultInfo;
import prompto.error.PromptoError;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.utils.CodeWriter;
import prompto.value.IValue;

public class DefaultExpression implements IExpression {
	
	IExpression expression;
	IValue value; 
	
	public DefaultExpression(IExpression expression) {
		this.expression = expression;
	}

	@Override
	public IType check(Context context) {
		return expression.check(context);
	}

	@Override
	public IValue interpret(Context context) throws PromptoError {
		if(value==null)
			value = expression.interpret(context.getGlobalContext());
		return value;
	}

	@Override
	public void toDialect(CodeWriter writer) {
		expression.toDialect(writer);
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		return expression.compile(context.getGlobalContext(), method, flags);
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
		return expression.transpile(transpiler);
	}
}
