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
import prompto.type.IType;
import prompto.utils.CodeWriter;
import prompto.value.IContainer;
import prompto.value.IValue;

public class MatchingCollectionConstraint extends MatchingConstraintBase {
	
	IExpression collection;
	
	public MatchingCollectionConstraint(IExpression collection) {
		this.collection = collection;
	}
	
	@Override
	public void checkValue(Context context, IValue value) throws PromptoError {
		Object container = collection.interpret(context);
		if(container instanceof IContainer) {
			if(!((IContainer<?>)container).hasItem(context, value))
				throw new InvalidValueError(String.valueOf(value) + collectionToString());
		} else
			throw new InvalidValueError("Not a collection: " + collection.toString());
	}
	
	private String collectionToString() {
		return " is not in collection: " + collection.toString();
	}

	@Override
	public void toDialect(CodeWriter writer) {
		writer.append(" in ");
		collection.toDialect(writer);
	}
	
	@Override
	public void compile(Context context, MethodInfo method, Flags flags) {
		ResultInfo info = collection.compile(context, method, flags.withPrimitive(true));
		CompilerUtils.compileALOAD(method, "value");
		MethodConstant m = new MethodConstant(info.getType(), "contains", Object.class, boolean.class);
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
		String message = " does not match:" + collectionToString();
		method.addInstruction(Opcode.LDC, new StringConstant(message));
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
	    this.collection.declare(transpiler);
	    this.transpileFunction = t -> this.transpileChecker(t, name, type);
	    transpiler.declare(this);
	    transpiler.require("StrictSet");
	}

	private boolean transpileChecker(Transpiler transpiler, String name, IType type) {
	    transpiler.append("function $check_").append(name).append("(value) {").indent();
	    transpiler = transpiler.newChildTranspiler(null);
	    Identifier id = new Identifier("value");
	    transpiler.getContext().registerValue(new Variable(id, type));
	    transpiler.append("if(");
	    this.collection.transpile(transpiler);
	    transpiler.append(".has(value))").indent();
	    transpiler.append("return value;").dedent();
	    transpiler.append("else").indent();
	    transpiler.append("throw new IllegalValueError((value == null ? 'null' : value.toString()) + ' is not in: \"").append(this.collection.toString()).append("\"');").dedent();
	    transpiler.dedent().append("}").newLine();
	    transpiler.flush();
	    return true;
	}
	
	
}
