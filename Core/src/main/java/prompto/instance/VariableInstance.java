package prompto.instance;

import prompto.compiler.ClassConstant;
import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.IVerifierEntry.VerifierType;
import prompto.compiler.MethodInfo;
import prompto.compiler.ResultInfo;
import prompto.compiler.StackLocal;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.expression.IExpression;
import prompto.grammar.INamed;
import prompto.grammar.Identifier;
import prompto.runtime.Context;
import prompto.runtime.Context.InstanceContext;
import prompto.runtime.Variable;
import prompto.transpiler.Transpiler;
import prompto.type.CategoryType;
import prompto.type.CodeType;
import prompto.type.IType;
import prompto.utils.CodeWriter;
import prompto.value.IValue;

public class VariableInstance implements IAssignableInstance {
	
	Identifier id;
	
	public VariableInstance(Identifier id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return id.toString();
	}
	
	public Identifier getId() {
		return id;
	}
	
	public String getName() {
		return id.toString();
	}

	
	@Override
	public ResultInfo compileParent(Context context, MethodInfo method, Flags flags) {
		StackLocal local = method.getRegisteredLocal(id.toString());
		if(local instanceof StackLocal.ObjectLocal)
			return CompilerUtils.compileALOAD(method, local);
		else
			throw new UnsupportedOperationException();
	}
	
	@Override
	public ResultInfo compileAssign(Context context, MethodInfo method, Flags flags, IExpression expression) {
		Context actual = context.contextForValue(id);
		if(actual instanceof InstanceContext) {
			IType type = ((InstanceContext)actual).getInstanceType();
			if(type instanceof CategoryType) // could be a closure
				return ((CategoryType)type).compileSetMember(context, method, flags, null, expression, id);
		}
		return compileAssignVariable(context, method, flags, expression);
	}
	
	public ResultInfo compileAssignVariable(Context context, MethodInfo method, Flags flags, IExpression expression) {
		IType valueType = expression.check(context);
		// Code expressions need to be interpreted as part of compile
		if(valueType==CodeType.instance()) {
			assign(context, expression);
			return new ResultInfo(void.class);
		} else {
			checkAssignValue(context, valueType);
			ResultInfo info = expression.compile(context, method, flags);
			StackLocal local = method.registerLocal(id.toString(), VerifierType.ITEM_Object, new ClassConstant(info.getType()));
			CompilerUtils.compileASTORE(method, local);
			return new ResultInfo(void.class);
		}
	}
	
	@Override
	public void toDialect(CodeWriter writer, IExpression expression) {
		if(expression!=null) try {
			IType type = expression.check(writer.getContext());
			INamed actual = writer.getContext().getRegisteredValue(INamed.class,id);
			if(actual==null)
				writer.getContext().registerValue(new Variable(id, type));
		} catch(SyntaxError e) {
			// TODO warning
		}
		writer.append(id);
	}
	
	@Override
	public IType checkAssignValue(Context context, IType valueType) {
		// called for a=x
		INamed actual = context.getRegisteredValue(INamed.class,id);
		if(actual==null)
			context.registerValue(new Variable(id, valueType));
		else {
			// need to check type compatibility
			IType actualType = actual.getType(context);
			actualType.checkAssignableFrom(context, valueType);
			valueType = actualType;
		}
		return valueType;
	}
	
	@Override
	public IType checkAssignMember(Context context, Identifier memberName, IType valueType) {
		// called for a.x = y
		INamed actual = context.getRegisteredValue(INamed.class, id);
		if(actual==null) 
			throw new SyntaxError("Unknown variable:" + this.id);
		IType parentType = actual.getType(context);
		return parentType.checkMember(context, memberName);
	}
	
	@Override
	public IType checkAssignItem(Context context, IType itemType, IType valueType) {
		// called for a[x] = y
		INamed actual = context.getRegisteredValue(INamed.class, id);
		if(actual==null) 
			throw new SyntaxError("Unknown variable:" + this.id);
		IType parentType = actual.getType(context);
		return parentType.checkItem(context, itemType);
	}
	
	@Override
	public void assign(Context context, IExpression expression) throws PromptoError {
 		IValue value = expression.interpret(context);
		if(context.getRegisteredValue(INamed.class,id)==null) {
			IType type = expression.check(context);
			context.registerValue(new Variable(id, type)); 
		}
		context.setValue(id, value);
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		return context.getValue(id);
	}
	
	@Override
	public IType check(Context context) {
		INamed actual = context.getRegisteredValue(INamed.class, id);
		return actual.getType(context);
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		// nothing to do
	}
	
	@Override
	public void transpile(Transpiler transpiler) {
		transpiler.append(this.getName());
	}
	
	@Override
	public void declareAssign(Transpiler transpiler, IExpression expression) {
		Context context = transpiler.getContext();
	   if(context.getRegisteredValue(INamed.class, this.getId())==null) {
	        IType valueType = expression.check(context);
	        context.registerValue(new Variable(this.id, valueType));
	        // Code expressions need to be interpreted as part of full check
	        if (valueType == CodeType.instance()) {
	            context.setValue(this.id, expression.interpret(context));
	        }

	    }
	    expression.declare(transpiler);
	}
	
	@Override
	public void transpileAssign(Transpiler transpiler, IExpression expression) {
		Context context = transpiler.getContext();
	   if(context.getRegisteredValue(INamed.class, this.id)==null) {
	        IType type = expression.check(context);
	        context.registerValue(new Variable(this.id, type));
	        transpiler.append("var ");
	    }
	    context = context.contextForValue(this.id);
	    if(context instanceof InstanceContext) {
	        ((InstanceContext)context).getInstanceType().transpileInstance(transpiler);
	        transpiler.append(".setMember('").append(this.getName()).append("', ");
	        expression.transpile(transpiler);
	        transpiler.append(")");
	    } else {
	        transpiler.append(this.getName());
	        transpiler.append(" = ");
	        expression.transpile(transpiler);
	    }
    }
	
	@Override
	public void transpileAssignParent(Transpiler transpiler) {
		transpiler.append(this.getName());
	}
	
}
