package prompto.csharp;

import prompto.utils.CodeWriter;



public interface CSharpExpression {

	void toDialect(CodeWriter writer);
	
}
