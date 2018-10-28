package prompto.javascript;

import prompto.transpiler.Transpiler;
import prompto.utils.CodeWriter;

public class JavaScriptNewExpression implements JavaScriptExpression {

	JavaScriptMethodExpression method;
	
	public JavaScriptNewExpression(JavaScriptMethodExpression method) {
		this.method = method;
	}

	@Override
	public void toDialect(CodeWriter writer) {
		writer.append("new ");
		method.toDialect(writer);
	}
	
	@Override
	public void transpile(Transpiler transpiler) {
		transpiler.append("new ");
		method.transpile(transpiler);
	}
}
