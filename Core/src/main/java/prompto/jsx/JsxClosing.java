package prompto.jsx;

import prompto.grammar.Identifier;
import prompto.utils.CodeWriter;

public class JsxClosing {

	Identifier id;
	String suite;
	
	public JsxClosing(Identifier id, String suite) {
		this.id = id;
		this.suite = suite;
	}

	public void toDialect(CodeWriter writer) {
		writer.append("</").append(id).append(">");
		if(suite!=null)
			writer.appendRaw(suite);
	}

}
