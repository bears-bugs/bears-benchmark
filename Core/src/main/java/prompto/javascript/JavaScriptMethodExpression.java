package prompto.javascript;

import prompto.transpiler.Transpiler;
import prompto.utils.CodeWriter;

public class JavaScriptMethodExpression extends JavaScriptSelectorExpression {

	String name;
	JavaScriptExpressionList arguments = new JavaScriptExpressionList();
	
	public JavaScriptMethodExpression(String name) {
		this.name = name;
	}

	public void setArguments(JavaScriptExpressionList arguments) {
		this.arguments = arguments!=null ? arguments : new JavaScriptExpressionList();
	}

	@Override
	public String toString() {
		if(parent!=null)
			return parent.toString() + "." + name + "(" + arguments.toString() + ")";
		else
			return name + "(" + arguments.toString() + ")";
	}

	@Override
	public void toDialect(CodeWriter writer) {
		if(parent!=null) {
			parent.toDialect(writer);
			writer.append('.');
		}
		writer.append(name);
		writer.append('(');
		if(arguments!=null)
			arguments.toDialect(writer);
		writer.append(')');
	}
	
	@Override
	public void transpile(Transpiler transpiler) {
		if(parent!=null) {
			parent.transpile(transpiler);
			transpiler.append('.');
		}
		transpiler.append(name).append('(');
		if(arguments!=null)
			arguments.transpile(transpiler);
		transpiler.append(')');
	}
	
	@Override
	public void transpileRoot(Transpiler transpiler) {
		if(parent!=null)
			parent.transpileRoot(transpiler);
		else
			transpiler.append(name);
	}
	
}
