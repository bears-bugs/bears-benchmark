package prompto.javascript;



public class JavaScriptBooleanLiteral extends JavaScriptLiteral {

	Boolean value;
	
	public JavaScriptBooleanLiteral(String text) {
		super(text);
		value = Boolean.valueOf(text);
	}
	
}
