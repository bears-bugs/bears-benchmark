package prompto.statement;

import prompto.compiler.Flags;
import prompto.compiler.ResultInfo;
import prompto.compiler.MethodInfo;
import prompto.error.PromptoError;
import prompto.expression.IExpression;
import prompto.instance.IAssignableInstance;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.CodeType;
import prompto.type.IType;
import prompto.type.VoidType;
import prompto.utils.CodeWriter;
import prompto.value.IValue;

public class AssignInstanceStatement extends SimpleStatement {
	
	IAssignableInstance instance;
	IExpression expression;
	
	public AssignInstanceStatement(IAssignableInstance instance, IExpression expression) {
		this.instance = instance;
		this.expression = expression;
	}

	@Override
	public String toString() {
		return instance.toString() + " = " + expression.toString();
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		instance.toDialect(writer, expression);
		writer.append(" = ");
		expression.toDialect(writer);
	}
	
	public IExpression getExpression() {
		return expression;
	}

	@Override
	public IType check(Context context) {
		IType valueType = expression.check(context);
		instance.checkAssignValue(context, valueType);
		// Code expressions need to be interpreted as part of full check
		if(valueType==CodeType.instance())
			instance.assign(context, expression);
		return VoidType.instance();
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		instance.assign(context, expression);
		return null;
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		return instance.compileAssign(context, method, flags, expression);
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		this.instance.declareAssign(transpiler, this.expression);
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
	    IType valueType = this.expression.check(transpiler.getContext());
	    // don't assign Code expressions
	    if (valueType == CodeType.instance()) {
	        this.instance.declareAssign(transpiler, this.expression);
	        return true;
	    } else {
	        this.instance.transpileAssign(transpiler, this.expression);
	        return false;
	    }
	}
}
