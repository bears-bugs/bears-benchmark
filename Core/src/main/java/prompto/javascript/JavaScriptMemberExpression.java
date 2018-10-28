package prompto.javascript;

import prompto.declaration.IMethodDeclaration;
import prompto.statement.MethodCall;
import prompto.transpiler.Transpiler;
import prompto.utils.CodeWriter;

public class JavaScriptMemberExpression extends JavaScriptSelectorExpression {

	String name;
	
	public JavaScriptMemberExpression(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return parent.toString() + "." + name;
	}

	@Override
	public void toDialect(CodeWriter writer) {
		parent.toDialect(writer);
		writer.append('.');
		writer.append(name);
	}
	
	@Override
	public void transpile(Transpiler transpiler) {
		parent.transpile(transpiler);
		transpiler.append('.');
		transpiler.append(name);
	}
	
	@Override
	public void transpileInlineMethodCall(Transpiler transpiler, IMethodDeclaration declaration, MethodCall methodCall) {
		parent.transpileInlineMethodCall(transpiler, declaration, methodCall);
		transpiler.append('.');
		transpiler.append(name);
	}

}
