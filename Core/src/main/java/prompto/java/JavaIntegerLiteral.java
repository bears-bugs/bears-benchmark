package prompto.java;

import prompto.compiler.IntConstant;
import prompto.compiler.LongConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.runtime.Context;
import prompto.type.IType;


public class JavaIntegerLiteral extends JavaLiteral {

	Long value;
	
	public JavaIntegerLiteral(String text) {
		super(text);
		this.value = Long.valueOf(text);
	}

	@Override
	public Long interpret(Context context) {
		return value;
	}
	
	@Override
	public IType check(Context context) {
		return new JavaClassType(Long.class);
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
	
	long x() {
		return 9876543210L;
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method) {
		if(value>=0 && value<=5) {
			// ICONST_0 to ICONST_5 are consecutive
			Opcode opcode = Opcode.values()[Opcode.ICONST_0.ordinal() + value.intValue()];
			method.addInstruction(opcode);
			return new ResultInfo(int.class);
		} else if(value<=Integer.MAX_VALUE && value>=Integer.MIN_VALUE) {
			method.addInstruction(Opcode.LDC_W, new IntConstant(value.intValue()));
			return new ResultInfo(int.class);
		} else {
			method.addInstruction(Opcode.LDC2_W, new LongConstant(value));
			return new ResultInfo(long.class);
		}
	}
}
