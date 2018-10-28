package prompto.grammar;

import prompto.transpiler.Transpiler;


public enum CmpOp {
	GT(">"),
	GTE(">="),
	LT("<"),
	LTE("<=");

	String token;
	
	CmpOp(String token) {
		this.token = token;
	}
	
	public String toString() {
		return token;
	}

	public void transpile(Transpiler transpiler) {
		transpiler.append(this.name().toLowerCase());
	}
}
