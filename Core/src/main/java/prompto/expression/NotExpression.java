package prompto.expression;

import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.IInstructionListener;
import prompto.compiler.InterfaceConstant;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.OffsetListenerConstant;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
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

public class NotExpression implements IUnaryExpression, IPredicateExpression, IAssertion {

	IExpression expression;
	
	public NotExpression(IExpression expression) {
		this.expression = expression;
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		writer.append(operatorToDialect(writer.getDialect()));
		expression.toDialect(writer);
	}

	private String operatorToDialect(Dialect dialect) {
		switch(dialect) {
		case E:
		case M:
			return "not ";
		case O:
			return "!";
		default:
			throw new RuntimeException("Unsupported: " + dialect.name());	
		}
	}

	@Override
	public IType check(Context context) {
		IType type = expression.check(context);
		if(!(type instanceof BooleanType))
			throw new SyntaxError("Cannot negate " + type.getTypeName());
		return BooleanType.instance();
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		IValue val = expression.interpret(context);
		return interpret(val);
	}
	
	private IValue interpret(IValue val) throws PromptoError {
		if(val instanceof Boolean) 
			return ((Boolean)val).getNot();
		else
			throw new SyntaxError("Illegal: not " + val.getClass().getSimpleName());
	}
	
	@Override
	public void interpretQuery(Context context, IQueryBuilder query) throws PromptoError {
		if(!(expression instanceof IPredicateExpression))
			throw new SyntaxError("Not a predicate: " + expression.toString());
		((IPredicateExpression)expression).interpretQuery(context, query);
		query.not();
	}

	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		ResultInfo info = expression.compile(context, method, flags.withPrimitive(true));
		if(Boolean.class==info.getType())
			CompilerUtils.BooleanToboolean(method);
		CompilerUtils.reverseBoolean(method);
		if(flags.toPrimitive())
			return new ResultInfo(boolean.class);
		else
			return CompilerUtils.booleanToBoolean(method);
	}
	
	@Override
	public void compileQuery(Context context, MethodInfo method, Flags flags) {
		((IPredicateExpression)expression).compileQuery(context, method, flags);
		InterfaceConstant m = new InterfaceConstant(IQueryBuilder.class, "not", void.class);
		method.addInstruction(Opcode.INVOKEINTERFACE, m);
	}


	@Override
	public boolean interpretAssert(Context context, TestMethodDeclaration test) throws PromptoError {
		IValue val = expression.interpret(context);
		IValue result = interpret(val);
		if(result==Boolean.TRUE) 
			return true;
		String expected = buildExpectedMessage(context, test);
		String actual = operatorToDialect(test.getDialect()) + val.toString();
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
		StackState finalState = method.captureStackState();
		// compile
		ResultInfo info = expression.compile(context, method, flags.withPrimitive(true));
		if(Boolean.class==info.getType())
			CompilerUtils.BooleanToboolean(method);
		// 0 = success (since we have not applied the not)
		IInstructionListener finalListener = method.addOffsetListener(new OffsetListenerConstant());
		method.activateOffsetListener(finalListener);
		method.addInstruction(Opcode.IFEQ, finalListener); 
		// increment failure counter
		method.addInstruction(Opcode.ICONST_1);
		method.addInstruction(Opcode.IADD);
		// build failure message
		String message = buildExpectedMessage(context, test);
		message = test.buildFailedAssertionMessagePrefix(message);
		method.addInstruction(Opcode.LDC, new StringConstant(message));
		method.addInstruction(Opcode.LDC, new StringConstant(operatorToDialect(test.getDialect())));
		MethodConstant concat = new MethodConstant(String.class, "concat", String.class, String.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, concat);
		method.addInstruction(Opcode.LDC, new StringConstant(Boolean.FALSE.toString()));
		method.addInstruction(Opcode.INVOKEVIRTUAL, concat);
		test.compileFailure(context, method, flags);
		// success/final
		method.restoreFullStackState(finalState);
		method.placeLabel(finalState);
		method.inhibitOffsetListener(finalListener);
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		this.expression.declare(transpiler);
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
	    transpiler.append("!(");
	    this.expression.transpile(transpiler);
	    transpiler.append(")");
	    return false;
	}
	
	@Override
	public void transpileFound(Transpiler transpiler, Dialect dialect) {
		this.transpile(transpiler);
	}
}
