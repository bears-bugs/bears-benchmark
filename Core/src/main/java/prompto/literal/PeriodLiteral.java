package prompto.literal;

import prompto.compiler.Flags;
import prompto.compiler.IOperand;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.compiler.StringConstant;
import prompto.intrinsic.PromptoPeriod;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.type.PeriodType;
import prompto.value.Period;


public class PeriodLiteral extends Literal<Period> {

	public PeriodLiteral(String text) {
		super(text,parseDuration(text.substring(1,text.length()-1)));
	}
	
	@Override
	public IType check(Context context) {
		return PeriodType.instance();
	}
	
	public static Period parseDuration(String text) {
		return new Period(PromptoPeriod.parse(text));
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		PromptoPeriod period = value.getStorableData();
		method.addInstruction(Opcode.LDC_W, new StringConstant(period.toString()));
		IOperand oper = new MethodConstant(PromptoPeriod.class, "parse", 
				String.class, PromptoPeriod.class);
		method.addInstruction(Opcode.INVOKESTATIC, oper);
		return new ResultInfo(PromptoPeriod.class);
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		transpiler.require("Period");
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
		transpiler.append("Period.parse(").append(this.text.get()).append(")");
		return false;
	}
	
	
}
