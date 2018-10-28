package prompto.javascript;


public abstract class JavaScriptSelectorExpression implements JavaScriptExpression {

	JavaScriptExpression parent;
	
	public void setParent(JavaScriptExpression parent) {
		this.parent = parent;
	}
	
}
