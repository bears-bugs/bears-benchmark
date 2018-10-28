package prompto.csharp;

import prompto.utils.CodeWriter;



public class CSharpMethodExpression extends CSharpSelectorExpression {

	String name;
	CSharpExpressionList arguments;
	
	public CSharpMethodExpression(String name, CSharpExpressionList arguments) {
		this.name = name;
		this.arguments = arguments;
	}

	public void addArgument(CSharpExpression expression) {
		arguments.add(expression);
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		parent.toDialect(writer);
		writer.append('.');
		writer.append(name);
		writer.append('(');
		if(arguments!=null)
			arguments.toDialect(writer);
		writer.append(')');
	}

	@Override
	public String toString() {
		return parent.toString() + "." + name + "(" + (arguments!=null ? arguments.toString() : "") + ")";
	}
	
}
