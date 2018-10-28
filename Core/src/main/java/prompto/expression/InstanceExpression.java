package prompto.expression;

import prompto.argument.IArgument;
import prompto.compiler.ClassConstant;
import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.compiler.StackLocal;
import prompto.declaration.AttributeDeclaration;
import prompto.declaration.CategoryDeclaration;
import prompto.declaration.ConcreteMethodDeclaration;
import prompto.declaration.IMethodDeclaration;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.grammar.INamed;
import prompto.grammar.Identifier;
import prompto.parser.Dialect;
import prompto.parser.Section;
import prompto.runtime.Context;
import prompto.runtime.Context.ClosureContext;
import prompto.runtime.Context.InstanceContext;
import prompto.runtime.Context.MethodDeclarationMap;
import prompto.runtime.LinkedVariable;
import prompto.runtime.Variable;
import prompto.transpiler.Transpiler;
import prompto.type.IType;
import prompto.type.MethodType;
import prompto.utils.CodeWriter;
import prompto.value.ClosureValue;
import prompto.value.IValue;

public class InstanceExpression extends Section implements IExpression {

	Identifier id;
	
	public InstanceExpression(Identifier name) {
		this.id = name;
	}

	public Identifier getId() {
		return id;
	}
	
	public String getName() {
		return id.toString();
	}
	
	@Override
	public String toString() {
		return id.toString();
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		toDialect(writer, true);
	}
	
	public void toDialect(CodeWriter writer, boolean requireMethod) {
		if(requireMethod && requiresMethod(writer))
			writer.append("Method: ");
		writer.append(id);
	}
	
	private boolean requiresMethod(CodeWriter writer) {
		if(writer.getDialect()!=Dialect.E)
			return false;
		Object o = writer.getContext().getRegistered(id);
		if(o instanceof MethodDeclarationMap)
			return true;
		return false;
	}

	@Override
	public IType check(Context context) {
		INamed named = context.getRegistered(id);
		if(named==null) {
			context.getProblemListener().reportUnknownIdentifier(id.toString(), id);
			return null;
		}
		else if(named instanceof Variable // local variable
				|| named instanceof LinkedVariable // local variable with downcast
				|| named instanceof IArgument // named argument
				|| named instanceof CategoryDeclaration // any p with x
				|| named instanceof AttributeDeclaration) // in category method
			return named.getType(context);
		else if(named instanceof MethodDeclarationMap) { // global method or closure
			IMethodDeclaration decl = ((MethodDeclarationMap)named).values().iterator().next();
			return new MethodType(decl);
		} else
			throw new SyntaxError(id + "  is not a value or method:" + named.getClass().getSimpleName()); // TODO use pb listener
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
				throw new SyntaxError("No value or method with name:" + id);
		}
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		ResultInfo info = compileLocal(context, method, flags);
		if(info!=null)
			return info;
		else
			info = compileRegistered(context, method, flags);
		if(info!=null)
			return info;
		else
			info = compileInstanceField(context, method, flags);
		if(info!=null)
			return info;
		else
			info = compileSingletonField(context, method, flags);
		if(info!=null)
			return info;
		else
			throw new SyntaxError("Unknown identifier: " + getName());
	}

	private ResultInfo compileRegistered(Context context, MethodInfo method, Flags flags) {
		INamed named = context.getRegistered(getId());
		if(named instanceof Context.MethodDeclarationMap) {
			ConcreteMethodDeclaration decl = (ConcreteMethodDeclaration)((MethodDeclarationMap)named).values().iterator().next();
			return decl.compileMethodInstance(context, method, flags);
		} else
			return null;
	}

	private ResultInfo compileSingletonField(Context context, MethodInfo method, Flags flags) {
		Context actual = context.contextForValue(getId());
		if(actual instanceof InstanceContext) {
			IType type = ((InstanceContext)actual).getInstanceType();
			return type.compileGetMember(context, method, flags, null, id);
		} else
			return null;
	}
	
	private ResultInfo compileInstanceField(Context context, MethodInfo method, Flags flags) {
		StackLocal local = method.getRegisteredLocal("this");
		if(local==null)
			return null;
		IExpression parent = new ThisExpression();
		if(context instanceof ClosureContext) {
			Context owner = context.contextForValue(id);
			if(owner instanceof InstanceContext)
				parent = new MemberSelector(parent, new Identifier("this$0"));
		}
		MemberSelector selector = new MemberSelector(parent, id);
		return selector.compile(context, method, flags);
	}

	private ResultInfo compileLocal(Context context, MethodInfo method, Flags flags) {
		StackLocal local = method.getRegisteredLocal(getName());
		if(local==null)
			return null;
		ClassConstant downcastTo = null;
		if(local instanceof StackLocal.ObjectLocal)
			downcastTo = ((StackLocal.ObjectLocal)local).getDowncastTo();
		ResultInfo info = CompilerUtils.compileALOAD(method, local);
		if(downcastTo==null)
			return info;
		else {
			method.addInstruction(Opcode.CHECKCAST, downcastTo);
			return new ResultInfo(downcastTo.getType());
		}
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		INamed named = transpiler.getContext().getRegistered(id);
		if(named instanceof MethodDeclarationMap) {
			IMethodDeclaration decl = ((MethodDeclarationMap)named).getFirst();
			// don't declare member methods
			if(decl.getMemberOf()!=null)
				return;
			// don't declare closures
			if(decl instanceof ConcreteMethodDeclaration && ((ConcreteMethodDeclaration)decl).getDeclarationOf()!=null)
				return;
			decl.declare(transpiler);
		}
	}
	

	@Override
	public boolean transpile(Transpiler transpiler) {
		Context context = transpiler.getContext().contextForValue(this.id);
	    if(context instanceof InstanceContext) {
	        ((InstanceContext)context).getInstanceType().transpileInstance(transpiler);
	        transpiler.append(".");
	    }
		INamed named = transpiler.getContext().getRegistered(id);
		if(named instanceof MethodDeclarationMap) {
			transpiler.append(((MethodDeclarationMap)named).getFirst().getTranspiledName(context));
			// need to bind instance methods
			if(context instanceof InstanceContext) {
		        transpiler.append(".bind(");
		        ((InstanceContext)context).getInstanceType().transpileInstance(transpiler);
		        transpiler.append(")");
		    }
		} else {
		    if(this.getName().equals(transpiler.getGetterName()))
		        transpiler.append("$");
		    transpiler.append(this.getName());
		}
		return false;
	}

}
