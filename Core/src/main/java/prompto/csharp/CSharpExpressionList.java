package prompto.csharp;

import prompto.utils.CodeWriter;
import prompto.utils.ObjectList;

public class CSharpExpressionList extends ObjectList<CSharpExpression> {

	private static final long serialVersionUID = 1L;

	public CSharpExpressionList() {
	}
	
	public CSharpExpressionList(CSharpExpression expression) {
		this.add(expression);
	}

	public void toDialect(CodeWriter writer) {
		if(this.size()>0) {
			for(CSharpExpression exp : this) {
				exp.toDialect(writer);
				writer.append(", ");
			}
			writer.trimLast(2);
		}
	}
	
}
