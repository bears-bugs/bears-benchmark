package prompto.constraint;

import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.IInstructionListener;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.OffsetListenerConstant;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.compiler.StackState;
import prompto.compiler.StringConstant;
import prompto.error.PromptoError;
import prompto.expression.IExpression;
import prompto.grammar.Identifier;
import prompto.intrinsic.PromptoException;
import prompto.runtime.Context;
import prompto.runtime.Variable;
import prompto.store.InvalidValueError;
import prompto.transpiler.Transpiler;
import prompto.type.AnyType;
import prompto.type.IType;
import prompto.utils.CodeWriter;
import prompto.value.Boolean;
import prompto.value.IValue;

public class MatchingExpressionConstraint extends MatchingConstraintBase {

	IExpression expression;
	
	public MatchingExpressionConstraint(IExpression expression) {
		this.expression = expression;
	}
	
	@Override
	public void checkValue(Context context, IValue value) throws PromptoError {
		Context child = context.newChildContext();
		child.registerValue(new Variable(new Identifier("value"), AnyType.instance()));
		child.setValue(new Identifier("value"), value);
		Object test = expression.interpret(child);
		if(!Boolean.TRUE.equals(test))
			throw new InvalidValueError(String.valueOf(value) + expressionToString(context));
	}
	
	private String expressionToString(Context context) {
		CodeWriter cw = new CodeWriter(this.getDialect(), context);
		expression.toDialect(cw);
		return " does not match:" + cw.toString();
	}

	@Override
	public void toDialect(CodeWriter writer) {
		writer.append(" matching ");
		expression.toDialect(writer);
	}
	
	@Override
	public void compile(Context context, MethodInfo method, Flags flags) {
		Context child = context.newChildContext();
		child.registerValue(new Variable(new Identifier("value"), AnyType.instance()));
		ResultInfo info = expression.compile(child, method, flags.withPrimitive(true));
		if(Boolean.class==info.getType())
			CompilerUtils.BooleanToboolean(method);
		// 1 = success
		IInstructionListener finalListener = method.addOffsetListener(new OffsetListenerConstant());
		method.activateOffsetListener(finalListener);
		method.addInstruction(Opcode.IFNE, finalListener); 
		StackState finalState = method.captureStackState();
		// build failure message
		method.addInstruction(Opcode.LDC, new StringConstant("INVALID_VALUE"));
		CompilerUtils.compileALOAD(method, "value");
		MethodConstant m = new MethodConstant(String.class, "valueOf", Object.class, String.class);
		method.addInstruction(Opcode.INVOKESTATIC, m);
		method.addInstruction(Opcode.LDC, new StringConstant(expressionToString(context)));
		m = new MethodConstant(String.class, "concat", String.class, String.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, m);
		// throw exception
		m = new MethodConstant(PromptoException.class, "throwEnumeratedException", String.class, String.class, void.class);
		method.addInstruction(Opcode.INVOKESTATIC, m);
		// success/final
		method.restoreFullStackState(finalState);
		method.placeLabel(finalState);
		method.inhibitOffsetListener(finalListener);		
	}
	
	@Override
	public void declare(Transpiler transpiler, String name, IType type) {
	    transpiler = transpiler.newChildTranspiler(null);
	    Identifier id = new Identifier("value");
	    transpiler.getContext().registerValue(new Variable(id, type));
	    this.expression.declare(transpiler);
	    this.transpileFunction = t -> this.transpileChecker(t, name, type);
	    transpiler.declare(this);
	}

	private boolean transpileChecker(Transpiler transpiler, String name, IType type) {
	    transpiler.append("function $check_").append(name).append("(value) {").indent();
	    transpiler = transpiler.newChildTranspiler(null);
	    Identifier id = new Identifier("value");
	    transpiler.getContext().registerValue(new Variable(id, type));
	    transpiler.append("if(");
	    this.expression.transpile(transpiler);
	    transpiler.append(")").indent();
	    transpiler.append("return value;").dedent();
	    transpiler.append("else").indent();
	    transpiler.append("throw new IllegalValueError((value == null ? 'null' : value.toString()) + ' does not match: \"").append(this.expression.toString()).append("\"');").dedent();
	    transpiler.dedent().append("}").newLine();
	    transpiler.flush();
	    return false;
	}

}
