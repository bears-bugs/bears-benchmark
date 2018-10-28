package prompto.java;

import prompto.compiler.DoubleConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.runtime.Context;
import prompto.type.IType;

public class JavaDecimalLiteral extends JavaLiteral {

	Double value;
	
	public JavaDecimalLiteral(String text) {
		super(text);
		this.value = Double.valueOf(text);
	}

	@Override
	public Double interpret(Context context) {
		return value;
	}
	
	@Override
	public IType check(Context context) {
		return new JavaClassType(Double.class);
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method) {
		if(value==0.0)
			method.addInstruction(Opcode.DCONST_0);
		else if(value==1.0)
			method.addInstruction(Opcode.DCONST_1);
		else
			method.addInstruction(Opcode.LDC2_W, new DoubleConstant(value));
		return new ResultInfo(double.class);
	}
}
