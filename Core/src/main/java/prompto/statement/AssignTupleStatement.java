package prompto.statement;

import java.util.concurrent.atomic.AtomicInteger;

import prompto.compiler.ClassConstant;
import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.IVerifierEntry.VerifierType;
import prompto.compiler.IntConstant;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.compiler.StackLocal;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.expression.IExpression;
import prompto.grammar.INamed;
import prompto.grammar.Identifier;
import prompto.intrinsic.PromptoTuple;
import prompto.runtime.Context;
import prompto.runtime.Variable;
import prompto.transpiler.Transpiler;
import prompto.type.AnyType;
import prompto.type.IType;
import prompto.type.TupleType;
import prompto.type.VoidType;
import prompto.utils.CodeWriter;
import prompto.utils.IdentifierList;
import prompto.value.IValue;
import prompto.value.Integer;
import prompto.value.TupleValue;

public class AssignTupleStatement extends SimpleStatement {
	
	IdentifierList names;
	IExpression expression;
	
	public AssignTupleStatement(IdentifierList names, IExpression expression) {
		this.names = names;
		this.expression = expression;
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		names.toDialect(writer, false);
		writer.append(" = ");
		expression.toDialect(writer);
	}
	
	public void add(Identifier i1) {
		this.names.add(i1);
	}

	public IdentifierList getNames() {
		return names;
	}
	
	public void setExpression(IExpression expression) {
		this.expression = expression;
	}
	
	public IExpression getExpression() {
		return expression;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==this)
			return true;
		if(obj==null)
			return false;
		if(!(obj instanceof AssignTupleStatement))
			return false;
		AssignTupleStatement other = (AssignTupleStatement)obj;
		return this.getNames().equals(other.getNames())
				&& this.getExpression().equals(other.getExpression());
	}
	
	@Override
	public IType check(Context context) {
		IType type = expression.check(context);
		if(type!=TupleType.instance())
			throw new SyntaxError("Expecting a tuple expression, got " + type.getTypeName());
		for(Identifier name : names) {
			INamed actual = context.getRegistered(name);
			if(actual==null)
				context.registerValue(new Variable(name, AnyType.instance()));
			else {
				// need to check type compatibility
				IType actualType = actual.getType(context);
				actualType.checkAssignableFrom(context, AnyType.instance());
			}
		}
		return VoidType.instance();
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		Object object = expression.interpret(context);
		if(!(object instanceof TupleValue))
			throw new SyntaxError("Expecting a tuple expression, got " + object.getClass().getSimpleName());
		TupleValue tuple = (TupleValue)object;
		for(int i=0;i<names.size();i++) {
			Identifier name = names.get(i);
			IValue value = tuple.getItem(context, new Integer(i+1));
			if(context.getRegisteredValue(INamed.class, name)==null)
				context.registerValue(new Variable(name, value.getType()));
			context.setValue(name, value);
		}
		return null;
	}

	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		ResultInfo info = expression.compile(context, method, flags);
		if(PromptoTuple.class!=info.getType())
			throw new SyntaxError("Expecting a Tuple!");
		for(int i=0;i<names.size();i++)
			compileAssignTupleItem(context, method, flags, i);
		method.addInstruction(Opcode.POP);
		return new ResultInfo(void.class);
	}

	private void compileAssignTupleItem(Context context, MethodInfo method, Flags flags, int i) {
		method.addInstruction(Opcode.DUP);
		if(i<6) {
			Opcode opcode = Opcode.values()[i + Opcode.ICONST_0.ordinal()];
			method.addInstruction(opcode);
		} else
			method.addInstruction(Opcode.LDC, new IntConstant(i));
		MethodConstant m = new MethodConstant(PromptoTuple.class, "get", int.class, Object.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, m);
		Identifier name = names.get(i);
		StackLocal local = method.registerLocal(name.toString(), VerifierType.ITEM_Object, new ClassConstant(Object.class));
		CompilerUtils.compileASTORE(method, local);
	}
	
	@Override
	public void declare(Transpiler transpiler) {
	    this.expression.declare(transpiler);
	    this.names.forEach(name -> {
	        INamed actual = transpiler.getContext().getRegistered(name);
	        if(actual==null)
	            transpiler.getContext().registerValue(new Variable(name, AnyType.instance()));
	     });
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
		if(transpiler.getEngine().supportsDestructuring()) {
			transpiler.append("var [");
		    this.names.forEach(name -> {
		        transpiler.append(name.toString()).append(", ");
		        INamed actual = transpiler.getContext().getRegistered(name);
		        if(actual==null)
		            transpiler.getContext().registerValue(new Variable(name, AnyType.instance()));
		    });
		    transpiler.trimLast(2);
		    transpiler.append("] = ");
		    this.expression.transpile(transpiler);
		} else {
			transpiler.append("var $tuple = ");
		    this.expression.transpile(transpiler);
		    transpiler.append(";").newLine();	
		    AtomicInteger idx = new AtomicInteger();
		    this.names.forEach(name -> {
		    	transpiler.append("var ");
				transpiler.append(name.toString()).append(" = $tuple[").append(String.valueOf(idx.getAndIncrement())).append("];").newLine();
				INamed actual = transpiler.getContext().getRegistered(name);
		        if(actual==null)
		            transpiler.getContext().registerValue(new Variable(name, AnyType.instance()));
		    });
		}
	    return false;
	}

}
