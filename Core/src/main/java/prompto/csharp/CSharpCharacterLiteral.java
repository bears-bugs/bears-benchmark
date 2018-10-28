package prompto.csharp;



public class CSharpCharacterLiteral extends CSharpLiteral {

	char value;
	
	public CSharpCharacterLiteral(String text) {
		super(text);
		value = text.charAt(1);
	}
	
}
