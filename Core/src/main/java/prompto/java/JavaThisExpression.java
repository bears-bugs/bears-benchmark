package prompto.java;

import prompto.compiler.CompilerUtils;
import prompto.compiler.MethodInfo;
import prompto.compiler.ResultInfo;
import prompto.compiler.StackLocal;
import prompto.error.PromptoError;
import prompto.expression.ThisExpression;
import prompto.parser.Section;
import prompto.runtime.Context;
import prompto.type.IType;
import prompto.utils.CodeWriter;

public class JavaThisExpression extends Section implements JavaExpression {
	
	ThisExpression expression = new ThisExpression();
	
	@Override
	public String toString() {
		return "this";
	}
	
	@Override
	public IType check(Context context) {
		return expression.check(context);
	}
	
	@Override
	public Object interpret(Context context) throws PromptoError {
		return expression.interpret(context);
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		writer.append("this");
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method) {
		StackLocal local = method.getRegisteredLocal("$this$");
		return CompilerUtils.compileALOAD(method, local);
	}
}
