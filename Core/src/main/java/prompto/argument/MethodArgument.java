package prompto.argument;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import prompto.compiler.ClassConstant;
import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.InterfaceType;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.StringConstant;
import prompto.declaration.IMethodDeclaration;
import prompto.error.SyntaxError;
import prompto.expression.IExpression;
import prompto.grammar.ArgumentAssignment;
import prompto.grammar.ArgumentAssignmentList;
import prompto.grammar.ArgumentList;
import prompto.grammar.INamed;
import prompto.grammar.Identifier;
import prompto.intrinsic.PromptoProxy;
import prompto.parser.Dialect;
import prompto.runtime.Context;
import prompto.runtime.Context.MethodDeclarationMap;
import prompto.transpiler.Transpiler;
import prompto.type.MethodType;
import prompto.utils.CodeWriter;

public class MethodArgument extends BaseArgument implements INamedArgument {
	
	public MethodArgument(Identifier id) {
		super(id);
	}
	
	@Override
	public String getSignature(Dialect dialect) {
		return id.toString();
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		writer.append(id);
	}

	@Override
	public String toString() {
		return id.toString();
	}
	
	@Override
	public String getProto() {
		return id.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==this)
			return true;
		if(obj==null)
			return false;
		if(!(obj instanceof MethodArgument))
			return false;
		MethodArgument other = (MethodArgument)obj;
		return Objects.equals(this.getId(),other.getId());
	}

	@Override
	public void register(Context context) {
		INamed actual = context.getRegisteredValue(INamed.class,id);
		if(actual!=null)
			throw new SyntaxError("Duplicate argument: \"" + id + "\"");
		context.registerValue(this);
	}
	
	@Override
	public void check(Context context) {
		IMethodDeclaration actual = getDeclaration(context);
		if(actual==null)
			throw new SyntaxError("Unknown method: \"" + id + "\"");
	}
	
	@Override
	public MethodType getType(Context context) {
		IMethodDeclaration actual = getDeclaration(context);
		return new MethodType(actual);
	}
	
	private IMethodDeclaration getDeclaration(Context context) {
		MethodDeclarationMap methods = context.getRegisteredDeclaration(MethodDeclarationMap.class, id);
		if(methods!=null)
			return (IMethodDeclaration)(methods.values().iterator().next());
		else
			return null;
	}
	
	@Override
	public void compileAssignment(Context context, MethodInfo method, Flags flags, ArgumentAssignmentList assignments, boolean isFirst) {
		MethodType target = getType(context);
		IMethodDeclaration decl = target.getMethod();
		ArgumentList args = decl.getArguments();
		InterfaceType intf = new InterfaceType(args, decl.getReturnType());
		// the JVM can only cast to declared types, so we need a proxy to convert the FunctionalInterface call into the concrete one
		// 1st parameter is method reference
		ArgumentAssignment assign = makeAssignment(assignments, isFirst);
		IExpression expression = assign.getExpression();
		expression.compile(context.getCallingContext(), method, flags); // this would return a lambda
		// what interface we are casting to
		ClassConstant dest = new ClassConstant(getJavaType(context));
		method.addInstruction(Opcode.LDC, dest);
		// method name
		String methodName = intf.getInterfaceMethodName(); 
		method.addInstruction(Opcode.LDC, new StringConstant(methodName));
		// method arg types
		List<Type> javaTypes = args.stream().map(arg->arg.getJavaType(context)).collect(Collectors.toList());
		CompilerUtils.compileClassConstantsArray(method, javaTypes);
		// create proxy
		MethodConstant m = new MethodConstant(PromptoProxy.class, "newProxy", Object.class, Class.class, String.class, Class[].class, Object.class);
		method.addInstruction(Opcode.INVOKESTATIC, m);
		method.addInstruction(Opcode.CHECKCAST, dest);
	}

	@Override
	public void declare(Transpiler transpiler) {
		// nothing to do ?
	}
	
	@Override
	public String getTranspiledName(Context context) {
		IMethodDeclaration method = this.getDeclaration(context);
	    return method.getTranspiledName(context);
	}

}
