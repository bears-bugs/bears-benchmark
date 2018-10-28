package prompto.jsx;

import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.type.TextType;
import prompto.utils.CodeWriter;

public class JsxLiteral implements IJsxValue {

	String text;
	
	public JsxLiteral(String text) {
		this.text = text;
	}
	
	@Override
	public IType check(Context context) {
		return TextType.instance();
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		writer.append(text);
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		// nothing to do
	}

	@Override
	public boolean transpile(Transpiler transpiler) {
		transpiler.append(this.text);
		return false;
	}
}
