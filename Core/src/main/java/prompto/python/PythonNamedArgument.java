package prompto.python;

import prompto.utils.CodeWriter;

public class PythonNamedArgument implements PythonArgument{
	
	String name;
	PythonExpression expression;
	
	public PythonNamedArgument(String name, PythonExpression expression) {
		this.name = name;
		this.expression = expression;
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		writer.append(name);
		writer.append(" = ");
		expression.toDialect(writer);
	}

}
