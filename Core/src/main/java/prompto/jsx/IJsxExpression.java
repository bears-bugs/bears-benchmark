package prompto.jsx;

import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.MethodInfo;
import prompto.compiler.ResultInfo;
import prompto.error.PromptoError;
import prompto.expression.IExpression;
import prompto.runtime.Context;
import prompto.value.IValue;
import prompto.value.JsxValue;

public interface IJsxExpression extends IExpression {

	default IValue interpret(Context context) throws PromptoError {
		return new JsxValue(this);
	}
	
	@Override
	public default ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		return CompilerUtils.compileNewInstance(method, Object.class);
	}

}
