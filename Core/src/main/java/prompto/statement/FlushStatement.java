package prompto.statement;

import prompto.compiler.Flags;
import prompto.compiler.InterfaceConstant;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.error.PromptoError;
import prompto.parser.Dialect;
import prompto.runtime.Context;
import prompto.store.DataStore;
import prompto.store.IStore;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.type.VoidType;
import prompto.utils.CodeWriter;
import prompto.value.IValue;

public class FlushStatement extends SimpleStatement {

	@Override
	public IType check(Context context) {
		return VoidType.instance();
	}

	@Override
	public IValue interpret(Context context) throws PromptoError {
		DataStore.getInstance().flush();
		return null;
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		// need the data store
		MethodConstant m = new MethodConstant(DataStore.class, "getInstance", IStore.class);
		method.addInstruction(Opcode.INVOKESTATIC, m);
		// call flush
		InterfaceConstant i = new InterfaceConstant(IStore.class, "flush", void.class);
		method.addInstruction(Opcode.INVOKEINTERFACE, i);
		// done
		return new ResultInfo(void.class);
	}

	@Override
	public void toDialect(CodeWriter writer) {
		writer.append("flush");
		if(writer.getDialect()!=Dialect.E) {
			writer.append("()");
		}
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		transpiler.require("DataStore");
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
		transpiler.append("DataStore.instance.flush()");
		return false;
	}

}
