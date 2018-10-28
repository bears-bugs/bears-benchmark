package prompto.literal;

import prompto.transpiler.Transpiler;
import prompto.value.Integer;

public class MinIntegerLiteral extends IntegerLiteral {

	public MinIntegerLiteral() {
		super("MIN_INTEGER", new Integer(Long.MIN_VALUE));
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
		transpiler.append("-0x20000000000000");
		return false;
	}
}
