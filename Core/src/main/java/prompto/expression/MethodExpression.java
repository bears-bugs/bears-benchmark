package prompto.expression;

import prompto.compiler.Flags;
import prompto.compiler.MethodInfo;
import prompto.compiler.ResultInfo;
import prompto.declaration.ConcreteMethodDeclaration;
import prompto.declaration.IMethodDeclaration;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.grammar.INamed;
import prompto.grammar.Identifier;
import prompto.parser.Dialect;
import prompto.runtime.Context;
import prompto.runtime.Context.InstanceContext;
import prompto.runtime.Context.MethodDeclarationMap;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.type.MethodType;
import prompto.utils.CodeWriter;
import prompto.value.ClosureValue;
import prompto.value.IValue;

public class MethodExpression implements IExpression {

	Identifier id;
	
	public MethodExpression(Identifier id) {
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
	public void toDialect(CodeWriter writer) {
		if(writer.getDialect()==Dialect.E)
			writer.append("Method: ");
		writer.append(id);
	}
	
	@Override
	public IType check(Context context) {
		IMethodDeclaration declaration = getDeclaration(context);
		if(declaration!=null)
			return new MethodType(declaration);
		else
			throw new SyntaxError("No method with name:" + id);
	}
	
	private IMethodDeclaration getDeclaration(Context context) {
		MethodDeclarationMap methods = context.getRegisteredDeclaration(MethodDeclarationMap.class, id);
		if(methods!=null)
			return (IMethodDeclaration)(methods.values().iterator().next());
		else
			return null;
	}

	@Override
	public IValue interpret(Context context) throws PromptoError {
		if(context.hasValue(id))
			return context.getValue(id);
		else {
			INamed named = context.getRegistered(id);
			if(named instanceof Context.MethodDeclarationMap) {
				ConcreteMethodDeclaration decl = (ConcreteMethodDeclaration)((MethodDeclarationMap)named).values().iterator().next();
				MethodType type = new MethodType(decl);
				return new ClosureValue(context, type);
			} else
				throw new SyntaxError("No method with name:" + id);
		}
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		INamed named = context.getRegistered(id);
		if(named instanceof Context.MethodDeclarationMap) {
			ConcreteMethodDeclaration decl = (ConcreteMethodDeclaration)((MethodDeclarationMap)named).getFirst();
			return decl.compileMethodInstance(context, method, flags);
		} else
			throw new SyntaxError("No method with name:" + id);
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		INamed named = transpiler.getContext().getRegistered(id);
		if(named instanceof Context.MethodDeclarationMap) {
			IMethodDeclaration decl = ((MethodDeclarationMap)named).getFirst();
			// don't declare closures
			if(decl.getDeclarationOf()==null)
		        decl.declare(transpiler);
		}
	}
	
	@Override
	public boolean transpile(Transpiler transpiler) {
		INamed named = transpiler.getContext().getRegistered(id);
		if(named instanceof Context.MethodDeclarationMap) {
			Context context = transpiler.getContext().contextForValue(id);
			IMethodDeclaration decl = ((MethodDeclarationMap)named).getFirst();
		    if(context instanceof InstanceContext) {
		        ((InstanceContext)context).getInstanceType().transpileInstance(transpiler);
		        transpiler.append(".");
		    }
			transpiler.append(decl.getTranspiledName(transpiler.getContext()));
			// need to bind instance methods
			if(context instanceof InstanceContext) {
		        transpiler.append(".bind(");
		        ((InstanceContext)context).getInstanceType().transpileInstance(transpiler);
		        transpiler.append(")");
		    }
		}
		return false;
	}
	
}
