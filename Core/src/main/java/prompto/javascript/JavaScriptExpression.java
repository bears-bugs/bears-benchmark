package prompto.javascript;

import prompto.declaration.IMethodDeclaration;
import prompto.statement.MethodCall;
import prompto.transpiler.Transpiler;
import prompto.utils.CodeWriter;



public interface JavaScriptExpression {

	void toDialect(CodeWriter writer);
	void transpile(Transpiler transpiler);
	default void transpileRoot(Transpiler transpiler) {
		throw new UnsupportedOperationException("transpileRoot " + this.getClass().getName());
	}
	default void transpileInlineMethodCall(Transpiler transpiler, IMethodDeclaration declaration, MethodCall methodCall) {
		throw new UnsupportedOperationException("transpileInlineMethodCall " + this.getClass().getName());
	}
	
}
