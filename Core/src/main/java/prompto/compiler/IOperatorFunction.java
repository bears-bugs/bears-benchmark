package prompto.compiler;

import prompto.expression.IExpression;
import prompto.runtime.Context;


public interface IOperatorFunction {
	ResultInfo compile(Context context, MethodInfo method, Flags flags, ResultInfo left, IExpression right);
}
