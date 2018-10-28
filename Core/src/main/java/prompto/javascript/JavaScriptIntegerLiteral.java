package prompto.javascript;

public class JavaScriptIntegerLiteral extends JavaScriptLiteral {

	Long value;
	
	public JavaScriptIntegerLiteral(String text) {
		super(text);
		this.value = Long.valueOf(text);
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
