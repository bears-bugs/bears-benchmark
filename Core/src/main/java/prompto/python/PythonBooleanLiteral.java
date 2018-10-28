package prompto.python;



public class PythonBooleanLiteral extends PythonLiteral {

	Boolean value;
	
	public PythonBooleanLiteral(String text) {
		super(text);
		value = Boolean.valueOf(text);
	}
	
}
