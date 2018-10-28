package prompto.javascript;

public class JavaScriptCharacterLiteral extends JavaScriptLiteral {

	String value;
	
	public JavaScriptCharacterLiteral(String text) {
		super(text);
		this.value = text.substring(1,text.length()-1);
	}

	@Override
	public String toString() {
		return "'" + value + "'";
	}
	
}
