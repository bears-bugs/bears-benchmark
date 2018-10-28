package prompto.javascript;

import prompto.transpiler.Transpiler;
import prompto.utils.CodeWriter;

public class JavaScriptParenthesisExpression implements JavaScriptExpression {

	JavaScriptExpression expression;
	
	public JavaScriptParenthesisExpression(JavaScriptExpression expression) {
		this.expression = expression;
	}

	@Override
	public void toDialect(CodeWriter writer) {
		writer.append('(');
		expression.toDialect(writer);
		writer.append(')');
	}
	
	@Override
	public void transpile(Transpiler transpiler) {
		transpiler.append('(');
		expression.transpile(transpiler);
		transpiler.append(')');
	}
}
