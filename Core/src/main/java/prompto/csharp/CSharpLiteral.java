package prompto.csharp;

import prompto.utils.CodeWriter;

public abstract class CSharpLiteral implements CSharpExpression {

	String text;
	
	protected CSharpLiteral(String text) {
		this.text = text;
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		writer.append(text);
	}

}
