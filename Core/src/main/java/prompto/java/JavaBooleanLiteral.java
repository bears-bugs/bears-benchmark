package prompto.java;

import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.runtime.Context;
import prompto.type.IType;


public class JavaBooleanLiteral extends JavaLiteral {

	Boolean value;
	
	public JavaBooleanLiteral(String text) {
		super(text);
		value = Boolean.valueOf(text);
	}
	
	@Override
	public Object interpret(Context context) {
		return value;
	}
	
	@Override
	public IType check(Context context) {
		return new JavaClassType(Boolean.class);
	}

	@Override
	public ResultInfo compile(Context context, MethodInfo method) {
		method.addInstruction(value ? Opcode.ICONST_1 : Opcode.ICONST_0);
		return new ResultInfo(boolean.class);
	}
}
