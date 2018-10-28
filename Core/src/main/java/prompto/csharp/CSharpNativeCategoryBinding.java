package prompto.csharp;

import prompto.grammar.NativeCategoryBinding;
import prompto.utils.CodeWriter;

public class CSharpNativeCategoryBinding extends NativeCategoryBinding {

	CSharpIdentifierExpression expression;
	
	public CSharpNativeCategoryBinding(CSharpIdentifierExpression expression) {
		this.expression = expression;
	}

	@Override
	public void toDialect(CodeWriter writer) {
		writer.append("C#: ");
		expression.toDialect(writer);
	}

}
