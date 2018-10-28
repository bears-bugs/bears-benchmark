package prompto.java;

import prompto.compiler.IConstantOperand;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.compiler.StringConstant;
import prompto.runtime.Context;
import prompto.type.IType;

public class JavaTextLiteral extends JavaLiteral {

	String value;
	
	public JavaTextLiteral(String text) {
		super(text);
		this.value = text.substring(1,text.length()-1);
	}

	@Override
	public String toString() {
		return "\"" + value + "\"";
	}
	
	@Override
	public Object interpret(Context context) {
		return value;
	}
	
	@Override
	public IType check(Context context) {
		return new JavaClassType(String.class);
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method) {
		IConstantOperand operand = new StringConstant(value);
		method.addInstruction(Opcode.LDC_W, operand);
		return new ResultInfo(String.class);
	}

}
