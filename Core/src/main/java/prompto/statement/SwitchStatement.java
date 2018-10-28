package prompto.statement;

import java.util.ArrayList;
import java.util.List;

import prompto.compiler.ClassConstant;
import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.IInstructionListener;
import prompto.compiler.IVerifierEntry.VerifierType;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.OffsetListenerConstant;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.compiler.StackLocal;
import prompto.compiler.StackState;
import prompto.error.PromptoError;
import prompto.expression.EqualsExpression;
import prompto.expression.IExpression;
import prompto.expression.InstanceExpression;
import prompto.grammar.EqOp;
import prompto.grammar.Identifier;
import prompto.runtime.Context;
import prompto.runtime.Variable;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.utils.CodeWriter;
import prompto.value.IValue;

public class SwitchStatement extends BaseSwitchStatement {

	IExpression expression;
	
	public SwitchStatement(IExpression expression) {
		super();
		this.expression = expression;
	}

	public SwitchStatement(IExpression expression, SwitchCaseList switchCases, StatementList defaultCase) {
		super(switchCases, defaultCase);
		this.expression = expression;
	}

	@Override
	protected void toODialect(CodeWriter writer) {
		writer.append("switch(");
		expression.toDialect(writer);
		writer.append(") {\n");
		for(SwitchCase sc : switchCases)
			sc.caseToODialect(writer);
		if(defaultCase!=null) {
			writer.append("default:\n");
			writer.indent();
			defaultCase.toDialect(writer);
			writer.dedent();
		}
		writer.append("}\n");
	}

	@Override
	protected void toEDialect(CodeWriter writer) {
		writer.append("switch on ");
		expression.toDialect(writer);
		writer.append(":\n");
		writer.indent();
		for(SwitchCase sc : switchCases)
			sc.caseToEDialect(writer);
		if(defaultCase!=null) {
			writer.append("otherwise:\n");
			writer.indent();
			defaultCase.toDialect(writer);
			writer.dedent();
		}
		writer.dedent();
	}

	@Override
	protected void toMDialect(CodeWriter writer) {
		toEDialect(writer);
	}

	@Override
	IType checkSwitchType(Context context) {
		return expression.check(context);
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		IValue switchValue = expression.interpret(context);
		return interpretSwitch(context, switchValue, null);
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		IType result = check(context);
		compileSwitchCases(context, method, flags);
		return new ResultInfo(result.getJavaType(context));
	}
	
	static class SwitchCaseBranch {
		List<IInstructionListener> finalOffsetListeners = new ArrayList<>();
		IInstructionListener branchOffsetListener = null;
		StackState neutralState = null;
		StackLocal valueLocal;
	}
	
	private void compileSwitchCases(Context context, MethodInfo method, Flags flags) {
		StackState beforeValue = method.captureStackState();
		SwitchCaseBranch branch = compileSwitchCasesWithValue(context, method, flags);
		method.restoreFullStackState(beforeValue);
		method.placeLabel(beforeValue);
		stopListeningForThisBranch(method, branch);
		branch.finalOffsetListeners.forEach((l)->
			method.inhibitOffsetListener(l));
	}
	
	private SwitchCaseBranch compileSwitchCasesWithValue(Context context, MethodInfo method, Flags flags) {
		SwitchCaseBranch branch = new SwitchCaseBranch();
		branch.valueLocal = compileSwitchValue(context, method, flags);
		branch.neutralState = method.captureStackState();
		for(SwitchCase switchCase : switchCases)
			 compileSwitchCase(context, method, flags, switchCase, branch);
		compileDefaultCase(context, method, flags, branch);
		method.unregisterLocal(branch.valueLocal);
		return branch;
	}

	private StackLocal compileSwitchValue(Context context, MethodInfo method, Flags flags) {
		context = context.newChildContext();
		String valueName = method.nextTransientName("value");
		context.registerValue(new Variable(new Identifier(valueName), expression.check(context)));
		ResultInfo info = expression.compile(context, method, flags);
		StackLocal value = method.registerLocal(valueName, VerifierType.ITEM_Object, new ClassConstant(info.getType()));
		CompilerUtils.compileASTORE(method, value);
		return value;
	}

	private void compileSwitchCase(Context context, MethodInfo method, Flags flags, SwitchCase element, SwitchCaseBranch branch) {
		restoreNeutralStackState(method, branch);
		stopListeningForThisBranch(method, branch);
		compileCondition(context, method, flags, element, branch);
		startListeningForNextBranch(method, element, branch);
		compileBranch(method, element, branch);
		ResultInfo info = compileStatements(context, method, flags, element, branch);
		startListeningForFinalThenGoto(context, method, flags, element, branch, info);
	}
	
	private ResultInfo compileDefaultCase(Context context, MethodInfo method, Flags flags, SwitchCaseBranch branch) {
		if(this.defaultCase==null)
			return null;
		restoreNeutralStackState(method, branch);
		stopListeningForThisBranch(method, branch);
		return this.defaultCase.compile(context, method, flags);
	}
	
	private void compileCondition(Context context, MethodInfo method, Flags flags, SwitchCase switchCase, SwitchCaseBranch branch) {
		if(switchCase instanceof CollectionSwitchCase) {
			ResultInfo info = switchCase.expression.compile(context, method, flags);
			CompilerUtils.compileALOAD(method, branch.valueLocal);
			MethodConstant m = new MethodConstant(info.getType(), "contains", Object.class, boolean.class);
			method.addInstruction(Opcode.INVOKEVIRTUAL, m);
		} else {
			IExpression equals = new EqualsExpression(
					new InstanceExpression(new Identifier(branch.valueLocal.getName())),
					EqOp.EQUALS, switchCase.expression);
			ResultInfo info = equals.compile(context, method, flags.withPrimitive(true));
			if(Boolean.class==info.getType())
				CompilerUtils.BooleanToboolean(method);
		}
	}
	
	private void compileBranch(MethodInfo method, SwitchCase switchCase, SwitchCaseBranch branch) {
		method.addInstruction(Opcode.IFEQ, branch.branchOffsetListener);
	}
	
	private ResultInfo compileStatements(Context context, MethodInfo method, Flags flags, SwitchCase switchCase, SwitchCaseBranch branch) {
		if(switchCase.statements!=null)
			return switchCase.statements.compile(context, method, flags);
		else
			return new ResultInfo(void.class); 
	}

	
	private void restoreNeutralStackState(MethodInfo method, SwitchCaseBranch branch) {
		// is there a need to restore the stack?
		if(branch.branchOffsetListener!=null) {
			method.restoreFullStackState(branch.neutralState);
			method.placeLabel(branch.neutralState);
		}
	}
	
	private void startListeningForFinalThenGoto(Context context, MethodInfo method, Flags flags, SwitchCase switchCase, SwitchCaseBranch branch, ResultInfo info) {
		if(!info.isReturn()) {
			IInstructionListener finalOffset = method.addOffsetListener(new OffsetListenerConstant());
			method.activateOffsetListener(finalOffset);
			branch.finalOffsetListeners.add(finalOffset);
			method.addInstruction(Opcode.GOTO, finalOffset);
		}
	}
	
	private void startListeningForNextBranch(MethodInfo method, SwitchCase switchCase, SwitchCaseBranch branch) {
		branch.branchOffsetListener = method.addOffsetListener(new OffsetListenerConstant());
		method.activateOffsetListener(branch.branchOffsetListener);
	}
	
	private void stopListeningForThisBranch(MethodInfo method, SwitchCaseBranch branch) {
		if(branch.branchOffsetListener!=null) {
			method.inhibitOffsetListener(branch.branchOffsetListener);
			branch.branchOffsetListener = null;
		}
	}
	
	@Override
	public void declare(Transpiler transpiler) {
	    this.expression.declare(transpiler);
	    this.declareSwitch(transpiler);
	}

	@Override
	public boolean transpile(Transpiler transpiler) {
	    transpiler.append("switch (");
	    this.expression.transpile(transpiler);
	    transpiler.append(") {").newLine();
	    this.switchCases.forEach(switchCase -> {
	        switchCase.transpile(transpiler);
	    });
	    if(this.defaultCase!=null) {
	        transpiler.append("default:");
	        transpiler.indent();
	        this.defaultCase.transpile(transpiler);
	        transpiler.dedent();
	    }
	    transpiler.append("}");
	    transpiler.newLine();
	    return true;
	}
}
