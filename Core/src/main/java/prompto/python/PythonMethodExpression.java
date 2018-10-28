package prompto.python;

import prompto.utils.CodeWriter;

public class PythonMethodExpression extends PythonSelectorExpression {

	String name;
	PythonArgumentList arguments = new PythonArgumentList();
	
	public PythonMethodExpression(String name) {
		this.name = name;
	}

	public void setArguments(PythonArgumentList l1) {
		this.arguments = l1;
	}
	
	@Override
	public String toString() {
		if(parent!=null)
			return parent.toString() + "." + name + "(" + arguments.toString() + ")";
		else
			return name + "(" + (arguments!=null ? arguments.toString() : "") + ")";
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

}
