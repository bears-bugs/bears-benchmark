package prompto.css;

import prompto.transpiler.Transpiler;
import prompto.utils.CodeWriter;
import prompto.utils.StringUtils;

public class CssText implements ICssValue {

	String text;
	
	public CssText(String text) {
		this.text = text;
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
	public void transpile(Transpiler transpiler) {
		String text = StringUtils.escape(this.text);
		transpiler.append('"').append(text).append('"');
	}
}
