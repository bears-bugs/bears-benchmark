package prompto.expression;

import prompto.compiler.Flags;
import prompto.compiler.MethodInfo;
import prompto.compiler.ResultInfo;
import prompto.error.PromptoError;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.CodeType;
import prompto.type.IType;
import prompto.utils.CodeWriter;
import prompto.value.CodeValue;
import prompto.value.IValue;

public class CodeExpression implements IExpression {

	IExpression expression;
	
	public CodeExpression(IExpression expression) {
		this.expression = expression;
	}

	@Override
	public void toDialect(CodeWriter writer) {
		switch(writer.getDialect()) {
		case E:
			writer.append("Code: ");
			expression.toDialect(writer);
			break;
		case O:
		case M:
			writer.append("Code(");
			expression.toDialect(writer);
			writer.append(")");
			break;
		}
	}
	
	@Override
	public IType check(Context context) {
		return CodeType.instance();
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		return new CodeValue(this);
	}
	
	// expression can only be checked, interpreted or compiled in the context of an execute:

	public IType checkCode(Context context) {
		return expression.check(context);
	}

	public IValue interpretCode(Context context) throws PromptoError {
		return expression.interpret(context);
	}

	public ResultInfo compileCode(Context context, MethodInfo method, Flags flags) {
		return expression.compile(context, method, flags);
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		// nothing to do
	}

	public void declareCode(Transpiler transpiler) {
		this.expression.declare(transpiler);
	}

	public void transpileCode(Transpiler transpiler) {
		this.expression.transpile(transpiler);
	}
	
}
