package prompto.javascript;

import prompto.transpiler.Transpiler;
import prompto.utils.CodeWriter;

public abstract class JavaScriptLiteral implements JavaScriptExpression {

	String text;
	
	protected JavaScriptLiteral(String text) {
		this.text = text;
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		writer.append(text);
	}
	
	@Override
	public void transpile(Transpiler transpiler) {
		transpiler.append(text);
	}

}
