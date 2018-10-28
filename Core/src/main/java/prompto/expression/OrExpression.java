package prompto.expression;

import prompto.compiler.ClassConstant;
import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.IInstructionListener;
import prompto.compiler.IVerifierEntry.VerifierType;
import prompto.compiler.InterfaceConstant;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.OffsetListenerConstant;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.compiler.ShortOperand;
import prompto.compiler.StackLocal;
import prompto.compiler.StackState;
import prompto.compiler.StringConstant;
import prompto.declaration.TestMethodDeclaration;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.parser.Dialect;
import prompto.runtime.Context;
import prompto.store.IQueryBuilder;
import prompto.transpiler.Transpiler;
import prompto.type.BooleanType;
import prompto.type.IType;
import prompto.utils.CodeWriter;
import prompto.value.Boolean;
import prompto.value.IValue;

public class OrExpression implements IPredicateExpression, IAssertion {
	
	IExpression left;
	IExpression right;
	
	public OrExpression(IExpression left, IExpression right) {
		this.left = left;
		this.right = right;
	}

	public IExpression getLeft() {
		return left;
	}
	
	public IExpression getRight() {
		return right;
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		left.toDialect(writer);
		writer.append(operatorToDialect(writer.getDialect()));
		right.toDialect(writer);
	}
	
	private String operatorToDialect(Dialect dialect) {
		switch(dialect) {
		case E:
		case M:
			return " or ";
		case O:
			return " || ";
		default:
			throw new RuntimeException("Unsupported: " + dialect.name());	
		}
	}

	@Override
	public IType check(Context context) {
		IType lt = left.check(context);
		IType rt = right.check(context);
		if(!(lt instanceof BooleanType) || !(rt instanceof BooleanType))
			throw new SyntaxError("Cannot combine " + lt.getTypeName() + " and " + rt.getTypeName());
		return BooleanType.instance();
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		IValue lval = left.interpret(context);
		IValue rval = right.interpret(context);
		return interpret(lval, rval);
	}
	
	private IValue interpret(IValue lval, IValue rval) throws PromptoError {
		if(lval instanceof Boolean) {
			if(rval instanceof Boolean)
				return Boolean.valueOf(((Boolean)lval).getValue() || ((Boolean)rval).getValue());
			else
				throw new SyntaxError("Illegal: Boolean and " + rval.getClass().getSimpleName());
		} else
			throw new SyntaxError("Illegal: " + lval.getClass().getSimpleName() + " + " + rval.getClass().getSimpleName());
	}
	
	@Override
	public void interpretQuery(Context context, IQueryBuilder query) throws PromptoError {
		if(!(left instanceof IPredicateExpression))
			throw new SyntaxError("Not a predicate: " + left.toString());
		((IPredicateExpression)left).interpretQuery(context, query);
		if(!(right instanceof IPredicateExpression))
			throw new SyntaxError("Not a predicate: " + left.toString());
		((IPredicateExpression)right).interpretQuery(context, query);
		query.or();
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		ResultInfo li = left.compile(context, method, flags.withPrimitive(true));
		if(Boolean.class==li.getType())
			CompilerUtils.BooleanToboolean(method);
		IInstructionListener finalListener = method.addOffsetListener(new OffsetListenerConstant());
		method.activateOffsetListener(finalListener);
		method.addInstruction(Opcode.IFNE, finalListener);
		ResultInfo ri = right.compile(context, method, flags.withPrimitive(true));
		if(Boolean.class==ri.getType())
			CompilerUtils.BooleanToboolean(method);
		method.addInstruction(Opcode.IFNE, new ShortOperand((short)7));
		StackState branchState = method.captureStackState();
		method.addInstruction(Opcode.ICONST_0);
		method.addInstruction(Opcode.GOTO, new ShortOperand((short)4));
		method.restoreFullStackState(branchState);
		method.placeLabel(branchState);
		method.inhibitOffsetListener(finalListener);
		method.addInstruction(Opcode.ICONST_1);
		StackState lastState = method.captureStackState();
		method.placeLabel(lastState);
		if(flags.toPrimitive())
			return new ResultInfo(boolean.class);
		else
			return CompilerUtils.booleanToBoolean(method);
	}
	
	@Override
	public void compileQuery(Context context, MethodInfo method, Flags flags) {
		((IPredicateExpression)left).compileQuery(context, method, flags);
		((IPredicateExpression)right).compileQuery(context, method, flags);
		InterfaceConstant m = new InterfaceConstant(IQueryBuilder.class, "or", IQueryBuilder.class);
		method.addInstruction(Opcode.INVOKEINTERFACE, m);
	}

	@Override
	public boolean interpretAssert(Context context, TestMethodDeclaration test) throws PromptoError {
		IValue lval = left.interpret(context);
		IValue rval = right.interpret(context);
		IValue result = interpret(lval, rval);
		if(result==Boolean.TRUE) 
			return true;
		String expected = buildExpectedMessage(context, test);
		String actual = lval.toString() + operatorToDialect(test.getDialect()) + rval.toString();
		test.printFailedAssertion(context, expected, actual);
		return false;
	}
	
	private String buildExpectedMessage(Context context, TestMethodDeclaration test) {
		CodeWriter writer = new CodeWriter(test.getDialect(), context);
		this.toDialect(writer);
		return writer.toString();
	}

	@Override
	public void compileAssert(Context context, MethodInfo method, Flags flags, TestMethodDeclaration test) {
		context = context.newChildContext();
		StackState finalState = method.captureStackState();
		// compile left and store in local
		ResultInfo info = this.left.compile(context, method, flags.withPrimitive(true));
		if(Boolean.class==info.getType())
			CompilerUtils.BooleanToboolean(method);
		String leftName = method.nextTransientName("left");
		StackLocal left = method.registerLocal(leftName, VerifierType.ITEM_Integer, new ClassConstant(boolean.class));
		CompilerUtils.compileISTORE(method, left);
		// compile right and store in local
		info = this.right.compile(context, method, flags.withPrimitive(true));
		if(Boolean.class==info.getType())
			CompilerUtils.BooleanToboolean(method);
		String rightName = method.nextTransientName("right");
		StackLocal right = method.registerLocal(rightName, VerifierType.ITEM_Integer, new ClassConstant(boolean.class));
		CompilerUtils.compileISTORE(method, right);
		// check success of left or right
		CompilerUtils.compileILOAD(method, left);
		CompilerUtils.compileILOAD(method, right);
		method.addInstruction(Opcode.IADD);
		// 0 = failure
		IInstructionListener finalListener = method.addOffsetListener(new OffsetListenerConstant());
		method.activateOffsetListener(finalListener);
		method.addInstruction(Opcode.IFNE, finalListener); 
		// increment failure counter
		method.addInstruction(Opcode.ICONST_1);
		method.addInstruction(Opcode.IADD);
		// build failure message
		String message = buildExpectedMessage(context, test);
		message = test.buildFailedAssertionMessagePrefix(message);
		method.addInstruction(Opcode.LDC, new StringConstant(message));
		CompilerUtils.compileILOAD(method, left);
		MethodConstant valueOf = new MethodConstant(String.class, "valueOf", boolean.class, String.class);
		method.addInstruction(Opcode.INVOKESTATIC, valueOf);
		MethodConstant concat = new MethodConstant(String.class, "concat", String.class, String.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, concat);
		method.addInstruction(Opcode.LDC, new StringConstant(operatorToDialect(test.getDialect())));
		method.addInstruction(Opcode.INVOKEVIRTUAL, concat);
		CompilerUtils.compileILOAD(method, right);
		method.addInstruction(Opcode.INVOKESTATIC, valueOf);
		method.addInstruction(Opcode.INVOKEVIRTUAL, concat);
		test.compileFailure(context, method, flags);
		// success/final
		method.unregisterLocal(right);
		method.unregisterLocal(left);
		method.restoreFullStackState(finalState);
		method.placeLabel(finalState);
		method.inhibitOffsetListener(finalListener);
	}
	
	@Override
	public void declare(Transpiler transpiler) {
	    this.left.declare(transpiler);
	    this.right.declare(transpiler);
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
	    this.left.transpile(transpiler);
	    transpiler.append(" || ");
	    this.right.transpile(transpiler);
	    return false;
	}
	
	@Override
	public void declareQuery(Transpiler transpiler) {
	    this.left.declare(transpiler);
	    this.right.declare(transpiler);
	}
	
	@Override
	public void transpileQuery(Transpiler transpiler, String builderName) {
	    this.left.transpileQuery(transpiler, builderName);
	    this.right.transpileQuery(transpiler, builderName);
	    transpiler.append(builderName).append(".or();").newLine();
	}
	
	@Override
	public void transpileFound(Transpiler transpiler, Dialect dialect) {
	    transpiler.append("(");
	    this.left.transpile(transpiler);
	    transpiler.append(") + '").append(this.operatorToDialect(dialect)).append("' + (");
	    this.right.transpile(transpiler);
	    transpiler.append(")");
	}
}
