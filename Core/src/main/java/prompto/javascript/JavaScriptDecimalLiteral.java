package prompto.javascript;


public class JavaScriptDecimalLiteral extends JavaScriptLiteral {

	Double value;
	
	public JavaScriptDecimalLiteral(String text) {
		super(text);
		this.value = Double.valueOf(text);
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
