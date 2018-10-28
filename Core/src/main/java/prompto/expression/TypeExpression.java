package prompto.expression;

import prompto.compiler.Flags;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.compiler.StringConstant;
import prompto.error.PromptoError;
import prompto.grammar.Identifier;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.utils.CodeWriter;
import prompto.value.IValue;
import prompto.value.TypeValue;

public class TypeExpression implements IExpression {

	IType type;
	
	public TypeExpression(IType type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return type.toString();
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		type.toDialect(writer);
	}
	
	@Override
	public IType check(Context context) {
		return type;
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		return new TypeValue(type);
	}

	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		StringConstant s = new StringConstant(type.getJavaType(context).getTypeName());
		method.addInstruction(Opcode.LDC_W, s);
		MethodConstant m = new MethodConstant(Class.class, "forName", String.class, Class.class);
		method.addInstruction(Opcode.INVOKESTATIC, m);
		return new ResultInfo(Class.class);
	}
	
	public IValue getMember(Context context, Identifier name) throws PromptoError {
		return type.getMemberValue(context, name);
	}

	public IType getType() {
		return type;
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		this.type.declare(transpiler);
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
		this.type.transpile(transpiler);
		return false;
	}

}
