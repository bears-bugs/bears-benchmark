package prompto.python;

public class PythonTextLiteral extends PythonLiteral {

	String value;
	
	public PythonTextLiteral(String text) {
		super(text);
		this.value = text.substring(1,text.length()-1);
	}

	@Override
	public String toString() {
		return "\"" + value + "\"";
	}
	
}
