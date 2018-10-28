package prompto.python;

public class PythonCharacterLiteral extends PythonLiteral {

	String value;
	
	public PythonCharacterLiteral(String text) {
		super(text);
		this.value = text.substring(1,text.length()-1);
	}

	@Override
	public String toString() {
		return "'" + value + "'";
	}
	
}
