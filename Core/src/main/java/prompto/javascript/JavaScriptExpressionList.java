package prompto.javascript;

import prompto.transpiler.Transpiler;
import prompto.utils.CodeWriter;
import prompto.utils.ObjectList;

public class JavaScriptExpressionList extends ObjectList<JavaScriptExpression> {

	private static final long serialVersionUID = 1L;

	public JavaScriptExpressionList() {
	}
	
	public JavaScriptExpressionList(JavaScriptExpression expression) {
		this.add(expression);
	}
	
	public void toDialect(CodeWriter writer) {
		if(this.size()>0) {
			this.forEach(exp -> {
				exp.toDialect(writer);
				writer.append(", ");
			});
			writer.trimLast(2);
		}
	}

	public void transpile(Transpiler transpiler) {
		if(this.size()>0) {
			this.forEach(exp -> {
				exp.transpile(transpiler);
				transpiler.append(", ");
			});
			transpiler.trimLast(2);
		}
	}

}
