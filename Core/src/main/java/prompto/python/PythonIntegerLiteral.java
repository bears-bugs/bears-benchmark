package prompto.python;

public class PythonIntegerLiteral extends PythonLiteral {

	Long value;
	
	public PythonIntegerLiteral(String text) {
		super(text);
		this.value = Long.valueOf(text);
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
