package prompto.compiler;

import prompto.expression.IExpression;
import prompto.runtime.Context;


public interface ISlicerFunction {
	ResultInfo compile(Context context, MethodInfo method, Flags flags, ResultInfo parent, IExpression first, IExpression last);
}
