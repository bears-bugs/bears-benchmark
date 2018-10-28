package prompto.grammar;

import prompto.argument.IArgument;
import prompto.declaration.IMethodDeclaration;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.expression.IExpression;
import prompto.expression.InstanceExpression;
import prompto.expression.MemberSelector;
import prompto.runtime.Context;
import prompto.runtime.Variable;
import prompto.transpiler.Transpiler;
import prompto.type.CategoryType;
import prompto.type.IType;
import prompto.type.VoidType;
import prompto.utils.CodeWriter;
import prompto.value.ContextualExpression;
import prompto.value.IInstance;

public class ArgumentAssignment {
	
	IArgument argument;
	IExpression expression;
	
	public ArgumentAssignment(IArgument argument, IExpression expression) {
		this.argument = argument;
		this.expression = expression;
	}
	
	
	public void setArgument(IArgument argument) {
		this.argument = argument;
	}
	
	
	public IArgument getArgument() {
		return argument;
	}

	public Identifier getArgumentId() {
		return argument==null ? null : argument.getId();
	} 
	
	public IExpression getExpression() {
		return expression!=null ? expression : new InstanceExpression(argument.getId());
	}

	public void setExpression(IExpression expression) {
		this.expression = expression;
	}
	
	@Override
	public String toString() {
		if(argument==null)
			return expression.toString();
		else if(expression==null)
			return argument.getId().toString();
		else
			return argument.getId() + " = " + expression.toString();
	}
	
	public void toDialect(CodeWriter writer) {
		switch(writer.getDialect()) {
		case E:
			toEDialect(writer);
			break;
		case O:
			toODialect(writer);
			break;
		case M:
			toMDialect(writer);
			break;
		}
	}
	
	private void toODialect(CodeWriter writer) {
		toMDialect(writer);
	}

	private void toMDialect(CodeWriter writer) {
		if(expression==null)
			writer.append(argument.getId());
		else {
			if(argument!=null) {
				writer.append(argument.getId());
				writer.append(" = ");
			}
			expression.toDialect(writer);
		}
	}

	private void toEDialect(CodeWriter writer) {
		if(expression==null)
			writer.append(argument.getId());
		else {
			expression.toDialect(writer);
			if(argument!=null) {
				writer.append(" as ");
				writer.append(argument.getId());
			}
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==this)
			return true;
		if(obj==null)
			return false;
		if(!(obj instanceof ArgumentAssignment))
			return false;
		ArgumentAssignment other = (ArgumentAssignment)obj;
		return this.getArgument().equals(other.getArgument())
				&& this.getExpression().equals(other.getExpression());
	}
	
	public IType check(Context context) {
		IExpression expression = getExpression();
		INamed actual = context.getRegisteredValue(INamed.class, argument.getId());
		if(actual==null) {
			IType actualType = expression.check(context);
			context.registerValue(new Variable(argument.getId(), actualType));
		} else {
			// need to check type compatibility
			IType actualType = actual.getType(context);
			IType newType = expression.check(context);
			actualType.checkAssignableFrom(context, newType);
		}
		return VoidType.instance();
	}
	
	public IExpression resolve(Context context, IMethodDeclaration methodDeclaration, boolean checkInstance, boolean allowDerived) throws PromptoError {
		// since we support implicit members, it's time to resolve them
		Identifier name = argument.getId();
		IExpression expression = getExpression();
		IArgument argument = methodDeclaration.getArguments().find(name);
		IType required = argument.getType(context);
		IType actual = expression.check(context.getCallingContext());
		if(checkInstance && actual instanceof CategoryType) {
			Object value = expression.interpret(context.getCallingContext());
			if(value instanceof IInstance)
				actual = ((IInstance)value).getType();
		}
		boolean assignable = required.isAssignableFrom(context, actual);
		// when in dispatch, allow derived
		if(!assignable && allowDerived)
	        assignable = actual.isAssignableFrom(context, required);
		// try passing member
		if(!assignable && (actual instanceof CategoryType)) 
			expression = new MemberSelector(expression,name);
		return expression; 
	}

	public ArgumentAssignment resolveAndCheck(Context context, ArgumentList argumentList) {
		IArgument argument = this.argument;
		// when 1st argument, can be unnamed
		if(argument==null) {
			if(argumentList.size()==0)
				throw new SyntaxError("Method has no argument");
			argument = argumentList.get(0);
		} else
			argument = argumentList.find(this.getArgumentId());
		if(argument==null)
			throw new SyntaxError("Method has no argument:" + this.getArgumentId());
		IExpression expression = new ContextualExpression(context, getExpression());
		return new ArgumentAssignment(argument,expression);
	}


	public void declare(Transpiler transpiler) {
		if(this.expression!=null)
			this.expression.declare(transpiler);
	}


	public void transpile(Transpiler transpiler) {
		this.expression.transpile(transpiler);
	}
}
