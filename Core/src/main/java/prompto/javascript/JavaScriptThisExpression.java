package prompto.javascript;

import prompto.declaration.IMethodDeclaration;
import prompto.expression.MethodSelector;
import prompto.statement.MethodCall;
import prompto.transpiler.Transpiler;
import prompto.utils.CodeWriter;

public class JavaScriptThisExpression implements JavaScriptExpression {
	
	@Override
	public void toDialect(CodeWriter writer) {
		writer.append("this");
	}
	
	@Override
	public void transpile(Transpiler transpiler) {
		transpiler.append("this");
	}
	
	@Override
	public void transpileInlineMethodCall(Transpiler transpiler, IMethodDeclaration declaration, MethodCall methodCall) {
		MethodSelector selector = methodCall.resolveSelector(transpiler, declaration);
		selector.getParent().transpile(transpiler);
		
	}
}
