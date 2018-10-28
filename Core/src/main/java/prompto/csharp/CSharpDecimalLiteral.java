package prompto.csharp;


public class CSharpDecimalLiteral extends CSharpLiteral {

	Double value;
	
	public CSharpDecimalLiteral(String text) {
		super(text);
		this.value = Double.valueOf(text);
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
