package prompto.statement;

import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.InterfaceConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.compiler.StackLocal;
import prompto.error.PromptoError;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.utils.CodeWriter;
import prompto.value.IResource;
import prompto.value.IValue;

public class WithResourceStatement extends BaseStatement {

	AssignVariableStatement resource;
	StatementList statements;
	
	public WithResourceStatement(AssignVariableStatement resource, StatementList statements) {
		this.resource = resource;
		this.statements = statements;
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
		resource.toDialect(writer);
		writer.append(", do:\n");
		writer.indent();
		statements.toDialect(writer);
		writer.dedent();
	}

	private void toODialect(CodeWriter writer) {
		writer.append("with (");
		resource.toDialect(writer);
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
		resource.toDialect(writer);
		writer.append(":\n");
		writer.indent();
		statements.toDialect(writer);
		writer.dedent();
	}


	@Override
	public IType check(Context context) {
		context = context.newResourceContext();
		resource.checkResource(context);
		return statements.check(context, null);
	}

	@Override
	public IValue interpret(Context context) throws PromptoError {
		context = context.newResourceContext();
		try {
			resource.interpret(context);
			return statements.interpret(context);
		} finally {
			Object res = context.getValue(resource.getVariableId());
			if(res instanceof IResource)
				((IResource)res).close();
		}
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		// TODO: protect with try-catch
		context = context.newResourceContext();
		resource.compile(context, method, flags);
		ResultInfo info = statements.compile(context, method, flags.withVariable("%return%"));
		// call close
		StackLocal variable = method.getRegisteredLocal(resource.getVariableName());
		CompilerUtils.compileALOAD(method, variable);
		InterfaceConstant c = new InterfaceConstant(IResource.class, "close", void.class);
		method.addInstruction(Opcode.INVOKEINTERFACE, c);
		// return
		if(info.isReturn()) {
			StackLocal result = method.getRegisteredLocal("%return%");
			if(result!=null) { // not a void return
				info = CompilerUtils.compileALOAD(method, result);
				method.addInstruction(Opcode.ARETURN);
				return info;
			} else {
				method.addInstruction(Opcode.RETURN);
				return new ResultInfo(void.class);
			}
		} else
			return info;
	}
	
	@Override
	public void declare(Transpiler transpiler) {
	    transpiler = transpiler.newResourceTranspiler();
	    this.resource.declare(transpiler);
	    this.statements.declare(transpiler);
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
	    transpiler = transpiler.newResourceTranspiler();
	    this.resource.transpile(transpiler);
	    transpiler.append(";").newLine();
	    transpiler.append("try {").indent();
	    this.statements.transpile(transpiler);
	    transpiler.dedent().append("} finally {").indent();
	    this.resource.transpileClose(transpiler);
	    transpiler.dedent().append("}");
	    transpiler.flush();
		return true;
	}

}
