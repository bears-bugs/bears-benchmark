package prompto.java;

import prompto.compiler.ByteOperand;
import prompto.compiler.IntConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.compiler.ShortOperand;
import prompto.runtime.Context;
import prompto.type.IType;


public class JavaCharacterLiteral extends JavaLiteral {

	Character value;
	
	public JavaCharacterLiteral(String text) {
		super(text);
		this.value = text.charAt(1);
	}

	@Override
	public String toString() {
		return "'" + value + "'";
	}
	
	@Override
	public Object interpret(Context context) {
		return value;
	}
	
	@Override
	public IType check(Context context) {
		return new JavaClassType(Character.class);
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method) {
		if((value&0xFFFFFF00)==0)
			method.addInstruction(Opcode.BIPUSH, new ByteOperand((byte)value.charValue()));
		else if((value&0xFFFF0000)==0)
			method.addInstruction(Opcode.SIPUSH, new ShortOperand((short)value.charValue()));
		else
			method.addInstruction(Opcode.LDC_W, new IntConstant(value.charValue()));
		return new ResultInfo(char.class);
	}

}
