package prompto.python;


public class PythonDecimalLiteral extends PythonLiteral {

	Double value;
	
	public PythonDecimalLiteral(String text) {
		super(text);
		this.value = Double.valueOf(text);
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
