package prompto.literal;

import prompto.compiler.Flags;
import prompto.compiler.IOperand;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.compiler.StringConstant;
import prompto.intrinsic.PromptoDateTime;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.DateTimeType;
import prompto.type.IType;
import prompto.value.DateTime;


public class DateTimeLiteral extends Literal<DateTime> {

	public DateTimeLiteral(String text) {
		super(text, parseDateTime(text.substring(1,text.length()-1)));
	}
	
	public DateTimeLiteral(PromptoDateTime dateTime) {
		super("'" + dateTime.toString() + "'" , new DateTime(dateTime));
	}

	@Override
	public IType check(Context context) {
		return DateTimeType.instance();
	}
	
	public static DateTime parseDateTime(String text) {
		return new DateTime(PromptoDateTime.parse(text));
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		PromptoDateTime dateTime = value.getStorableData();
		method.addInstruction(Opcode.LDC_W, new StringConstant(dateTime.toString()));
		IOperand oper = new MethodConstant(PromptoDateTime.class, "parse", 
				String.class, PromptoDateTime.class);
		method.addInstruction(Opcode.INVOKESTATIC, oper);
		return new ResultInfo(PromptoDateTime.class);
	}

	@Override
	public void declare(Transpiler transpiler) {
		transpiler.require("Period");
		transpiler.require("DateTime");
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
		transpiler.append("DateTime.parse(").append(this.text.get()).append(")");
		return false;
	}
	
}
