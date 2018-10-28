package prompto.statement;

import java.lang.reflect.Type;

import prompto.compiler.Flags;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.compiler.StringConstant;
import prompto.error.PromptoError;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.CategoryType;
import prompto.type.IType;
import prompto.utils.CodeWriter;
import prompto.value.ConcreteInstance;
import prompto.value.IValue;

public class WithSingletonStatement extends BaseStatement {

	CategoryType type;
	StatementList statements;

	public WithSingletonStatement(CategoryType type, StatementList statements) {
		this.type = type;
		this.statements = statements;
	}
	
	@Override
	public boolean canReturn() {
		return true;
	}

	@Override
	public IType check(Context context) {
		Context instanceContext = context.newInstanceContext(type, true);
		Context childContext = instanceContext.newChildContext();
		return statements.check(childContext, null);
	}

	@Override
	public IValue interpret(Context context) throws PromptoError {
		ConcreteInstance instance = context.loadSingleton(context, type);
		synchronized(instance) {
			Context instanceContext = context.newInstanceContext(instance, true);
			Context childContext = instanceContext.newChildContext();
			return statements.interpret(childContext);
		}
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		Type thisType = type.getJavaType(context);
		StringConstant s = new StringConstant(thisType.getTypeName());
		method.addInstruction(Opcode.LDC_W, s);
		MethodConstant m = new MethodConstant(Class.class, "forName", String.class, Class.class);
		method.addInstruction(Opcode.INVOKESTATIC, m);
		method.addInstruction(Opcode.DUP);
		method.addInstruction(Opcode.MONITORENTER);
		Context instanceContext = context.newInstanceContext(type, false);
		Context childContext = instanceContext.newChildContext();
		statements.compile(childContext, method, flags);
		method.addInstruction(Opcode.MONITOREXIT);
		return new ResultInfo(void.class);
	}

	@Override
	public void toDialect(CodeWriter writer) {
		switch(writer.getDialect()) {
		case E:
			toEDialect(writer);
			break;
		case O:
			toODialect(writer);
			break;
		case M:
			toMDialect(writer);
			break;
		}
	}
	
	private void toEDialect(CodeWriter writer) {
		writer.append("with ");
		type.toDialect(writer);
		writer.append(", do:\n");
		writer.indent();
		statements.toDialect(writer);
		writer.dedent();
	}

	private void toODialect(CodeWriter writer) {
		writer.append("with (");
		type.toDialect(writer);
		writer.append(")");
		boolean oneLine = statements.size()==1 && (statements.get(0) instanceof SimpleStatement);
		if(!oneLine)
			writer.append(" {");
		writer.newLine();
		writer.indent();
		statements.toDialect(writer);
		writer.dedent();
		if(!oneLine) {
			writer.append("}");
			writer.newLine();
		}		
	}

	private void toMDialect(CodeWriter writer) {
		writer.append("with ");
		type.toDialect(writer);
		writer.append(":\n");
		writer.indent();
		statements.toDialect(writer);
		writer.dedent();
	}
	
	@Override
	public void declare(Transpiler transpiler) {
	    this.type.declare(transpiler);
	    transpiler = transpiler.newInstanceTranspiler(this.type);
	    transpiler = transpiler.newChildTranspiler(null);
	    this.statements.declare(transpiler);
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
		Transpiler instance = transpiler.newInstanceTranspiler(this.type);
		Transpiler child = instance.newChildTranspiler(null);
	    this.statements.transpile(child);
	    child.flush();
	    instance.flush();
	    return true;
	}
	
}
