package prompto.statement;

import prompto.compiler.Flags;
import prompto.compiler.IInstructionListener;
import prompto.compiler.MethodInfo;
import prompto.compiler.OffsetListenerConstant;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.compiler.ResultInfo.Flag;
import prompto.error.PromptoError;
import prompto.runtime.BreakResult;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.type.VoidType;
import prompto.utils.CodeWriter;
import prompto.value.IValue;

public class BreakStatement extends SimpleStatement {
	
	@Override
	public void toDialect(CodeWriter writer) {
		writer.append("break");
	}
	
	@Override
	public String toString() {
		return "break";
	}
	
	@Override
	public boolean canReturn() {
		return true;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof BreakStatement);
	}
	
	@Override
	public IType check(Context context) {
		return VoidType.instance();
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		return BreakResult.instance();
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		IInstructionListener breakLoop = method.addOffsetListener(new OffsetListenerConstant());
		method.activateOffsetListener(breakLoop);
		flags.addBreakLoopListener(breakLoop);
		method.addInstruction(Opcode.GOTO, breakLoop);
		return new ResultInfo(void.class, Flag.BREAK);
	}

	@Override
	public void declare(Transpiler transpiler) {
		// nothing to do;
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
		transpiler.append("break");
		return false;
	}
}
