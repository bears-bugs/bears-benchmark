package prompto.literal;

import prompto.compiler.Flags;
import prompto.compiler.IOperand;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.compiler.StringConstant;
import prompto.intrinsic.PromptoDate;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.DateType;
import prompto.type.IType;
import prompto.value.Date;


public class DateLiteral extends Literal<Date> {

	public DateLiteral(String text) {
		super(text,parseDate(text.substring(1,text.length()-1)));
	}
	
	public DateLiteral(PromptoDate date) {
		super("'" + date.toString() + "'", new Date(date));
	}

	@Override
	public IType check(Context context) {
		return DateType.instance();
	}
	
	public static Date parseDate(String text) {
		return new Date(PromptoDate.parse(text));
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		PromptoDate date = value.getStorableData();
		method.addInstruction(Opcode.LDC_W, new StringConstant(date.toString()));
		IOperand oper = new MethodConstant(PromptoDate.class, "parse", String.class, PromptoDate.class);
		method.addInstruction(Opcode.INVOKESTATIC, oper);
		return new ResultInfo(PromptoDate.class);
	}

	@Override
	public void declare(Transpiler transpiler) {
		transpiler.require("Period");
		transpiler.require("LocalDate");
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
		transpiler.append("LocalDate.parse(").append(this.text.get()).append(")");
		return false;
	}
}
