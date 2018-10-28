package prompto.jsx;

import java.util.List;

import prompto.grammar.Identifier;
import prompto.utils.CodeWriter;

public class JsxSelfClosing extends JsxElementBase {

	String nameSuite;
	String openingSuite;
	
	public JsxSelfClosing(Identifier name, String nameSuite, List<JsxAttribute> attributes, String openingSuite) {
		super(name, attributes);
		this.nameSuite = nameSuite;
		this.openingSuite = openingSuite;
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		writer.append("<").append(id);
		if(nameSuite!=null)
			writer.appendRaw(nameSuite);
		else if(!attributes.isEmpty())
			writer.append(" ");
		attributes.forEach(attr->attr.toDialect(writer));
		writer.append("/>");
		if(openingSuite!=null)
			writer.appendRaw(openingSuite);
	}

}
