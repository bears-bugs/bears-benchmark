package prompto.value;

import prompto.compiler.Flags;
import prompto.compiler.MethodInfo;
import prompto.compiler.ResultInfo;
import prompto.error.PromptoError;
import prompto.expression.CodeExpression;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.CodeType;
import prompto.type.IType;

public class CodeValue extends BaseValue {

	CodeExpression expression;
	
	public CodeValue(CodeExpression expression) {
		super(CodeType.instance());
		this.expression = expression;
	}
	
	public CodeExpression getExpression() {
		return expression;
	}

	public IType check(Context context) {
		return expression.checkCode(context);
	}

	public IValue interpret(Context context) throws PromptoError {
		return expression.interpretCode(context);
	}

	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		return expression.compileCode(context, method, flags);
	}

	public void declareCode(Transpiler transpiler) {
		expression.declareCode(transpiler);
	}

	public void transpileCode(Transpiler transpiler) {
		expression.transpileCode (transpiler);
	}
}
