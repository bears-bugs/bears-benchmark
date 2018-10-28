package prompto.csharp;



public class CSharpBooleanLiteral extends CSharpLiteral {

	Boolean value;
	
	public CSharpBooleanLiteral(String text) {
		super(text);
		value = Boolean.valueOf(text);
	}
	
}
