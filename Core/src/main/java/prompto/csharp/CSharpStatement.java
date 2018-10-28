package prompto.csharp;

import prompto.utils.CodeWriter;


public class CSharpStatement {

	CSharpExpression expression;
	boolean isReturn;
	
	public CSharpStatement(CSharpExpression expression,boolean isReturn) {
		this.expression = expression;
		this.isReturn = isReturn;
	}

	@Override
	public String toString() {
		return "" + (isReturn ? "return " : "") + expression.toString() + ";";
	}

	public void toDialect(CodeWriter writer) {
		if(isReturn)
			writer.append("return ");
		expression.toDialect(writer);
		writer.append(';');
	}
}
