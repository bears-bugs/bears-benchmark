package prompto.java;

import prompto.utils.CodeWriter;
import prompto.utils.ObjectList;

public class JavaExpressionList extends ObjectList<JavaExpression> {

	private static final long serialVersionUID = 1L;

	public JavaExpressionList() {
	}
	
	public JavaExpressionList(JavaExpression expression) {
		this.add(expression);
	}

	public void toDialect(CodeWriter writer) {
		if(this.size()>0) {
			for(JavaExpression exp : this) {
				exp.toDialect(writer);
				writer.append(", ");
			}
			writer.trimLast(2);
		}
	}
	
}
