package prompto.csharp;

import prompto.utils.CodeWriter;



public class CSharpItemExpression extends CSharpSelectorExpression {

	CSharpExpression item;
	
	public CSharpItemExpression(CSharpExpression item) {
		this.item = item;
	}

	@Override
	public String toString() {
		return parent.toString() + "[" + item.toString() + "]";
	}

	@Override
	public void toDialect(CodeWriter writer) {
		parent.toDialect(writer);
		writer.append('[');
		item.toDialect(writer);
		writer.append(']');
	}

}
