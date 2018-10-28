package prompto.csharp;

import prompto.utils.CodeWriter;



public class CSharpParenthesisExpression implements CSharpExpression {

	CSharpExpression expression;
	
	public CSharpParenthesisExpression(CSharpExpression expression) {
		this.expression = expression;
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		writer.append('(');
		expression.toDialect(writer);
		writer.append(')');
	}

}
