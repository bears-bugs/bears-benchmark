package prompto.javascript;

public class JavaScriptTextLiteral extends JavaScriptLiteral {

	String value;
	
	public JavaScriptTextLiteral(String text) {
		super(text);
		this.value = text.substring(1,text.length()-1);
	}

	@Override
	public String toString() {
		return "\"" + value + "\"";
	}
	
}
