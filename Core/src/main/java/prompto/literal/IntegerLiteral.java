package prompto.literal;

import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.LongConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.type.IntegerType;
import prompto.value.Integer;

public class IntegerLiteral extends Literal<Integer> {

	public IntegerLiteral(String text) {
		super(text, new Integer(Long.valueOf(text)));
	}

	public IntegerLiteral(long value) {
		super(Long.toString(value), new Integer(value));
	}
	
	public IntegerLiteral(String text, Integer value) {
		super(text, value);
	}

	@Override
	public IType check(Context context) {
		return IntegerType.instance();
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		long l = value.longValue();
		if(l>=0 && l<=1) {
			// LCONST_0 to LCONST_1 are consecutive
			Opcode opcode = Opcode.values()[Opcode.LCONST_0.ordinal() + (int)l];
			method.addInstruction(opcode);
		} else
			method.addInstruction(Opcode.LDC2_W, new LongConstant(l));
		if(flags.toPrimitive())
			return new ResultInfo(long.class);
		else
			return CompilerUtils.longToLong(method);
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		// nothing to do
	}

	@Override
	public boolean transpile(Transpiler transpiler) {
		transpiler.append(this.text.get());
		return false;
	}


}
