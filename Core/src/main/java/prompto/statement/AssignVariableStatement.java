package prompto.statement;

import prompto.compiler.Flags;
import prompto.compiler.MethodInfo;
import prompto.compiler.ResultInfo;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.expression.IExpression;
import prompto.grammar.INamed;
import prompto.grammar.Identifier;
import prompto.instance.VariableInstance;
import prompto.runtime.Context;
import prompto.runtime.Variable;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.type.ResourceType;
import prompto.type.VoidType;
import prompto.utils.CodeWriter;
import prompto.value.IValue;


public class AssignVariableStatement extends SimpleStatement {
	
	VariableInstance variable;
	IExpression expression;
	
	public AssignVariableStatement(Identifier id, IExpression expression) {
		this.variable = new VariableInstance(id);
		this.expression = expression;
	}

	public Identifier getVariableId() {
		return variable.getId();
	}

	public String getVariableName() {
		return variable.getName();
	}

	public IExpression getExpression() {
		return expression;
	}
	
	public void setExpression(IExpression expression) {
		this.expression = expression;
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		writer.append(variable.getName());
		writer.append(" = ");
		expression.toDialect(writer);
	}

	public IType checkResource(Context context) {
		IType type = expression.check(context);
		if(!(type instanceof ResourceType))
			throw new SyntaxError("Not a resource!");
		INamed actual = context.getRegisteredValue(INamed.class, variable.getId());
		if(actual==null)
			context.registerValue(new Variable(variable.getId(), type));
		else {
			// need to check type compatibility
			IType actualType = actual.getType(context);
			actualType.checkAssignableFrom(context, type);
		}
		return VoidType.instance();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==this)
			return true;
		if(obj==null)
			return false;
		if(!(obj instanceof AssignVariableStatement))
			return false;
		AssignVariableStatement other = (AssignVariableStatement)obj;
		return this.variable.getId().equals(other.variable.getId())
				&& this.getExpression().equals(other.getExpression());
	}
	
	@Override
	public IType check(Context context) {
		INamed actual = context.getRegisteredValue(INamed.class, variable.getId());
		if(actual==null) {
			IType actualType = expression.check(context);
			context.registerValue(new Variable(variable.getId(), actualType));
		} else {
			// need to check type compatibility
			IType actualType = actual.getType(context);
			IType newType = expression.check(context);
			actualType.checkAssignableFrom(context, newType);
		}
		return VoidType.instance();
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		INamed actual = context.getRegisteredValue(INamed.class, variable.getId());
		if(actual==null) {
			IType actualType = expression.check(context);
			context.registerValue(new Variable(variable.getId(), actualType));
		}
		context.setValue(variable.getId(), expression.interpret(context));
		return null;
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		INamed actual = context.getRegisteredValue(INamed.class, variable.getId());
		if(actual==null) {
			IType actualType = expression.check(context);
			context.registerValue(new Variable(variable.getId(), actualType));
		}
		return variable.compileAssign(context, method, flags, expression);
	}

	
	@Override
	public void declare(Transpiler transpiler) {
	    INamed actual = transpiler.getContext().getRegisteredValue(INamed.class, variable.getId());
	    if(actual==null) {
	        IType actualType = this.expression.check(transpiler.getContext());
	        transpiler.getContext().registerValue(new Variable(variable.getId(), actualType));
	    }
	    this.expression.declare(transpiler);
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
	    INamed actual = transpiler.getContext().getRegisteredValue(INamed.class, variable.getId());
	    if(actual==null) {
	        IType actualType = this.expression.check(transpiler.getContext());
	        transpiler.getContext().registerValue(new Variable(variable.getId(), actualType));
	        transpiler.append("var ");
	    }
	    transpiler.append(variable.getName()).append(" = ");
	    this.expression.transpile(transpiler);
		return false;
	}

	public void transpileClose(Transpiler transpiler) {
		transpiler.append(variable.getName()).append(".close();").newLine();
	}


}
