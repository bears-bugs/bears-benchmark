package prompto.expression;

import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.MethodInfo;
import prompto.compiler.ResultInfo;
import prompto.compiler.StackLocal;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.parser.Dialect;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.utils.CodeWriter;
import prompto.value.IValue;

public class ThisExpression implements IExpression {

	@Override
	public String toString() {
		return "this";
	}
	
	@Override
	public IType check(Context context) {
		if(context!=null && !(context instanceof Context.InstanceContext))
			context = context.getClosestInstanceContext();
		if( context instanceof Context.InstanceContext)
			return ((Context.InstanceContext)context).getInstanceType();
		else
			throw new SyntaxError("Not in an instance context!");
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		if(context!=null && !(context instanceof Context.InstanceContext))
			context = context.getClosestInstanceContext();
		if( context instanceof Context.InstanceContext)
			return ((Context.InstanceContext)context).getInstance();
		else
			throw new SyntaxError("Not in an instance context!");
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		StackLocal local = method.getRegisteredLocal("this");
		if(local==null)
			return null;
		CompilerUtils.compileALOAD(method, local);
		IType type = check(context);
		return new ResultInfo(type.getJavaType(context));	
	}
	
	
	@Override
	public void toDialect(CodeWriter writer) {
		if(writer.getDialect()==Dialect.O)
			writer.append("this");
		else
			writer.append("self");
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		// nothing to do
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
		transpiler.append("this");
		return false;
	}
}
