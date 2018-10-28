package prompto.python;

import prompto.utils.CodeWriter;



public class PythonParenthesisExpression implements PythonExpression {

	PythonExpression expression;
	
	public PythonParenthesisExpression(PythonExpression expression) {
		this.expression = expression;
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		writer.append('(');
		expression.toDialect(writer);
		writer.append('(');
	}

}
