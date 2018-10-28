package prompto.argument;

import java.lang.reflect.Type;

import prompto.compiler.ClassConstant;
import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.IVerifierEntry.VerifierType;
import prompto.compiler.MethodInfo;
import prompto.compiler.ResultInfo;
import prompto.compiler.StackLocal;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.expression.DefaultExpression;
import prompto.expression.IExpression;
import prompto.grammar.ArgumentAssignment;
import prompto.grammar.ArgumentAssignmentList;
import prompto.grammar.Identifier;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.DecimalType;
import prompto.type.IntegerType;
import prompto.value.Decimal;
import prompto.value.IValue;
import prompto.value.Integer;


public abstract class BaseArgument implements IArgument {

	Identifier id;
	boolean mutable = false;
	DefaultExpression defaultExpression;
	
	protected BaseArgument(Identifier id) {
		this.id = id;
	}
	
	@Override
	public Identifier getId() {
		return id;
	}
	
	@Override
	public boolean setMutable(boolean set) {
		boolean result = mutable;
		mutable = set;
		return result;
	}
	
	@Override
	public boolean isMutable() {
		return mutable;
	}
	
	@Override
	public DefaultExpression getDefaultExpression() {
		return defaultExpression;
	}
	
	public void setDefaultExpression(IExpression expression) {
		this.defaultExpression = expression==null ? null : new DefaultExpression(expression);
	}
	
	@Override
	public IValue checkValue(Context context, IExpression expression) throws PromptoError {
		IValue value = expression.interpret(context);
		if(value instanceof Integer && getType(context)==DecimalType.instance())
			return new Decimal(((Integer)value).doubleValue()); 
		else if(value instanceof Decimal && getType(context)==IntegerType.instance())
			return new Integer(((Decimal)value).longValue()); 
		else
			return value;
	}
	
	@Override
	public Type getJavaType(Context context) {
		return getType(context).getJavaType(context);
	}
	
	@Override
	public StackLocal registerLocal(Context context, MethodInfo method, Flags flags) {
		String desc = CompilerUtils.getDescriptor(getJavaType(context));
		VerifierType type = VerifierType.fromDescriptor(desc);
		ClassConstant classConstant = new ClassConstant(getJavaType(context));
		return method.registerLocal(getName(), type, classConstant);
	}

	@Override
	public void compileAssignment(Context context, MethodInfo method, Flags flags, ArgumentAssignmentList assignments, boolean isFirst) {
		ArgumentAssignment assign = makeAssignment(assignments, isFirst);
		ResultInfo valueInfo = assign.getExpression().compile(context.getCallingContext(), method, flags);
		// cast if required
		Type type = this.getJavaType(context);
		if(type==Double.class)
			CompilerUtils.numberToDouble(method, valueInfo);
		else if(type==Long.class)
			CompilerUtils.numberToLong(method, valueInfo);
		
	}

	protected ArgumentAssignment makeAssignment(ArgumentAssignmentList assignments, boolean isFirst) {
		ArgumentAssignment assign = assignments.find(id);
		if(assign!=null)
			return assign;
		// first argument can be anonymous
		else if(isFirst && assignments.size()>0 && assignments.get(0).getArgument()==null)
			return assignments.get(0);
		else if(defaultExpression!=null)
			return new ArgumentAssignment(this, defaultExpression);
		else
			throw new SyntaxError("Missing assignment for argument " + getName());
	}
	
	@Override
	public void transpile(Transpiler transpiler) {
		transpiler.append(this.getName());
	}


	@Override
	public void transpileCall(Transpiler transpiler, IExpression expression) {
	    expression.transpile(transpiler);
	}
}
