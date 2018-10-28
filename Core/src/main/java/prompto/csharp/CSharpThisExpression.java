package prompto.csharp;

import prompto.utils.CodeWriter;

public class CSharpThisExpression implements CSharpExpression {
	
	@Override
	public void toDialect(CodeWriter writer) {
		writer.append("this");
	}
}
