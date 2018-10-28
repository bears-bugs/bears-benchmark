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

public class ParenthesisExpression implements IExpression {

	IExpression expression;

	public ParenthesisExpression(IExpression expression) {
		this.expression = expression;
	}

	@Override
	public String toString() {
		return "(" + expression.toString() + ")";
	}

	@Override
	public void toDialect(CodeWriter writer) {
		writer.append("(");
		expression.toDialect(writer);
		writer.append(")");
	}

	@Override
	public IType check(Context context) {
		return expression.check(context);
	}

	@Override
	public IValue interpret(Context context) throws PromptoError {
		return expression.interpret(context);
	}

	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		return expression.compile(context, method, flags);
	}

	@Override
	public void declare(Transpiler transpiler) {
		expression.declare(transpiler);
	}

	@Override
	public boolean transpile(Transpiler transpiler) {
		transpiler.append("(");
		this.expression.transpile(transpiler);
		transpiler.append(")");
		return false;
	}
}
