package prompto.csharp;



public class CSharpIntegerLiteral extends CSharpLiteral {

	Long value;
	
	public CSharpIntegerLiteral(String text) {
		super(text);
		this.value = Long.valueOf(text);
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
