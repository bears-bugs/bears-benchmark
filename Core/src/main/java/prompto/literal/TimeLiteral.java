package prompto.literal;

import prompto.compiler.Flags;
import prompto.compiler.IOperand;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.compiler.StringConstant;
import prompto.intrinsic.PromptoTime;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.type.TimeType;
import prompto.value.Time;


public class TimeLiteral extends Literal<Time> {

	public TimeLiteral(String text) {
		super(text, parseTime(text.substring(1,text.length()-1)));
	}
	
	public TimeLiteral(PromptoTime time) {
		super("'" + time.toString() + "'", new Time(time));
	}
	
	@Override
	public IType check(Context context) {
		return TimeType.instance();
	}
	
	public static Time parseTime(String text) {
		return new Time(PromptoTime.parse(text));
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		PromptoTime time = value.getStorableData();
		method.addInstruction(Opcode.LDC_W, new StringConstant(time.toString()));
		IOperand oper = new MethodConstant(PromptoTime.class, "parse", String.class, PromptoTime.class);
		method.addInstruction(Opcode.INVOKESTATIC, oper);
		return new ResultInfo(PromptoTime.class);
	}

	@Override
	public void declare(Transpiler transpiler) {
		transpiler.require("Period");
		transpiler.require("LocalTime");
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
		transpiler.append("LocalTime.parse(").append(this.text.get()).append(")");
		return false;
	}
}
