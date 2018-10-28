package prompto.java;

import prompto.runtime.Context;
import prompto.type.IType;
import prompto.utils.CodeWriter;


public class JavaItemExpression extends JavaSelectorExpression {

	JavaExpression item;
	
	public JavaItemExpression(JavaExpression item) {
		this.item = item;
	}

	@Override
	public IType check(Context context) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Object interpret(Context context) {
		// TODO Auto-generated method stub
		return null;
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
