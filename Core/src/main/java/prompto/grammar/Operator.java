package prompto.grammar;

public enum Operator {
	PLUS("+"),
	MINUS("-"),
	MULTIPLY("*"),
	DIVIDE("/"),
	IDIVIDE("\\"),
	MODULO("%");
	
	String token;
	
	Operator(String token) {
		this.token = token;
	}
	
	public String getToken() {
		return token;
	}
}
