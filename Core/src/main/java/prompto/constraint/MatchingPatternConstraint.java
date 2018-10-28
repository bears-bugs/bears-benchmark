package prompto.constraint;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.IInstructionListener;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.OffsetListenerConstant;
import prompto.compiler.Opcode;
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
import prompto.type.IType;
import prompto.utils.CodeWriter;
import prompto.value.IValue;


public class MatchingPatternConstraint extends MatchingConstraintBase {

	IExpression expression;
	Pattern pattern;
	
	public MatchingPatternConstraint(IExpression expression) {
		this.expression = expression;
	}
	
	@Override
	public void checkValue(Context context, IValue value) throws PromptoError {
		if(pattern==null) {
			Object toMatch = expression.interpret(context);
			pattern = Pattern.compile(toMatch.toString());
		}
		if(!pattern.matcher(value.toString()).matches())
			throw new InvalidValueError(value.toString() + patternToString());
	}

	private String patternToString() {
		return " does not match:" + expression.toString();
	}

	@Override
	public void toDialect(CodeWriter writer) {
		writer.append(" matching ");
		expression.toDialect(writer);
	}
	
	@Override
	public void compile(Context context, MethodInfo method, Flags flags) {
		// compile pattern
		expression.compile(context, method, flags);
		MethodConstant m = new MethodConstant(String.class, "valueOf", Object.class, String.class);
		method.addInstruction(Opcode.INVOKESTATIC, m);
		m = new MethodConstant(Pattern.class, "compile", String.class, Pattern.class);
		method.addInstruction(Opcode.INVOKESTATIC, m);
		CompilerUtils.compileALOAD(method, "value");
		m = new MethodConstant(Pattern.class, "matcher", CharSequence.class, Matcher.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, m);
		m = new MethodConstant(Matcher.class, "matches", boolean.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, m);
		// 1 = success
		IInstructionListener finalListener = method.addOffsetListener(new OffsetListenerConstant());
		method.activateOffsetListener(finalListener);
		method.addInstruction(Opcode.IFNE, finalListener); 
		StackState finalState = method.captureStackState();
		// build failure message
		method.addInstruction(Opcode.LDC, new StringConstant("INVALID_VALUE"));
		CompilerUtils.compileALOAD(method, "value");
		m = new MethodConstant(String.class, "valueOf", Object.class, String.class);
		method.addInstruction(Opcode.INVOKESTATIC, m);
		method.addInstruction(Opcode.LDC, new StringConstant(patternToString()));
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
	    transpiler.append("if(new RegExp(");
	    this.expression.transpile(transpiler);
	    transpiler.append(").test(value))").indent();
	    transpiler.append("return value;").dedent();
	    transpiler.append("else").indent();
	    transpiler.append("throw new IllegalValueError((value == null ? 'null' : value.toString()) + ' does not match: ").append(this.expression.toString()).append("');").dedent();
	    transpiler.dedent().append("}").newLine();
	    transpiler.flush();
		return false;
	}
}
