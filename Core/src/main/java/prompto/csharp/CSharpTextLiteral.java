package prompto.csharp;


public class CSharpTextLiteral extends CSharpLiteral {

	String value;
	
	public CSharpTextLiteral(String text) {
		super(text);
		this.value = text.substring(1,text.length()-1);
	}

	@Override
	public String toString() {
		return "\"" + value + "\"";
	}
	
}
