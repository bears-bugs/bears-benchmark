package prompto.literal;

import prompto.compiler.Flags;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.error.PromptoError;
import prompto.expression.IExpression;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.type.NullType;
import prompto.utils.CodeWriter;
import prompto.value.IValue;
import prompto.value.NullValue;

public class NullLiteral implements IExpression {

	static NullLiteral instance = new NullLiteral();
	
	public static NullLiteral instance() {
		return instance;
	}

	private NullLiteral() {
	}
	
	@Override
	public IType check(Context context) {
		return NullType.instance();
	}

	@Override
	public IValue interpret(Context context) throws PromptoError {
		return NullValue.instance();
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		method.addInstruction(Opcode.ACONST_NULL);
		return new ResultInfo(Object.class);
	}

	@Override
	public void toDialect(CodeWriter writer) {
		switch(writer.getDialect()) {
		case E:
			writer.append("nothing");
			break;
		case O:
			writer.append("null");
			break;
		case M:
			writer.append("None");
			break;
		}
	}
	
	@Override
	public String toString() {
		return "null";
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		// nothing to do
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
		transpiler.append("null");
		return false;
	}

}
