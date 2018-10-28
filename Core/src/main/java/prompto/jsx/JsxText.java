package prompto.jsx;

import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.type.TextType;
import prompto.utils.CodeWriter;
import prompto.utils.StringUtils;

public class JsxText implements IJsxExpression {

	String text;
	
	
	public JsxText(String text) {
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
		String text = StringUtils.escape(this.text);
		transpiler.append('"').append(text).append('"');
		return false;
	}


}
