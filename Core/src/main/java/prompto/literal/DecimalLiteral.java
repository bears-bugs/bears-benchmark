package prompto.literal;

import prompto.compiler.CompilerUtils;
import prompto.compiler.DoubleConstant;
import prompto.compiler.Flags;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.DecimalType;
import prompto.type.IType;
import prompto.value.Decimal;

public class DecimalLiteral extends Literal<Decimal> {

	public DecimalLiteral(String text) {
		super(text,Decimal.Parse(text));
	}

	public DecimalLiteral(double value) {
		super(Double.toString(value), new Decimal(value));
	}

	@Override
	public IType check(Context context) {
		return DecimalType.instance();
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		double d = value.doubleValue();
		if(d==0.0)
			method.addInstruction(Opcode.DCONST_0);
		else if(d==1.0)
			method.addInstruction(Opcode.DCONST_1);
		else
			method.addInstruction(Opcode.LDC2_W, new DoubleConstant(d));
		if(flags.toPrimitive())
			return new ResultInfo(double.class);
		else
			return CompilerUtils.doubleToDouble(method);
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
