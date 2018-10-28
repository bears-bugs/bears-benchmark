package prompto.java;

import prompto.parser.Section;

public abstract class JavaSelectorExpression extends Section implements JavaExpression {

	JavaExpression parent;
	
	public void setParent(JavaExpression parent) {
		this.parent = parent;
	}
	
}
