package prompto.expression;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

import prompto.compiler.ClassConstant;
import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.InterfaceType;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.compiler.StringConstant;
import prompto.declaration.IDeclaration;
import prompto.declaration.IMethodDeclaration;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.grammar.ArgumentList;
import prompto.intrinsic.PromptoProxy;
import prompto.runtime.Context;
import prompto.runtime.Context.MethodDeclarationMap;
import prompto.transpiler.Transpiler;
import prompto.type.AnyType;
import prompto.type.CategoryType;
import prompto.type.DecimalType;
import prompto.type.IType;
import prompto.type.IntegerType;
import prompto.type.IterableType;
import prompto.type.MethodType;
import prompto.type.NativeType;
import prompto.utils.CodeWriter;
import prompto.value.Decimal;
import prompto.value.IValue;
import prompto.value.Integer;

public class CastExpression implements IExpression {
	
	public static IType anyfy(IType type) {
		if(type instanceof CategoryType && "Any".equals(((CategoryType)type).getTypeName()))
			return AnyType.instance();	
		else
			return type;
	}

	IExpression expression;
	IType type;
	
	public CastExpression(IExpression expression, IType type) {
		this.expression = expression;
		this.type = anyfy(type);
	}
	
	@Override
	public String toString() {
		return expression.toString() + " as " + type.toString();
	}
	
	@Override
	public IType check(Context context) {
		IType actual = anyfy(expression.check(context));
		IType target = getTargetType(context);
		// check Any
		if(actual==AnyType.instance())
			return target;
		// check upcast
		if(target.isAssignableFrom(context, actual))
			return target;
		// check downcast
		if(actual.isAssignableFrom(context, target))
			return target;
		throw new SyntaxError("Cannot cast " + actual.toString() + " to " + target.toString());
	}

	private IType getTargetType(Context context) {
		return getTargetType(context, type);
	}
	
	private static IType getTargetType(Context context, IType type) {
		if(type instanceof IterableType) {
			IType itemType = getTargetType(context, ((IterableType)type).getItemType());
			return ((IterableType)type).withItemType(itemType);
		} else if(type instanceof NativeType)
			return type;
		else
			return getTargetAtomicType(context, type);
	}
	
	private static IType getTargetAtomicType(Context context, IType type) {
		IDeclaration decl = context.getRegisteredDeclaration(IDeclaration.class, type.getTypeNameId());
		if(decl==null) {
			context.getProblemListener().reportUnknownIdentifier(type.getTypeName(), type);
			return null;
		} else if(decl instanceof MethodDeclarationMap) {
			MethodDeclarationMap map = (MethodDeclarationMap)decl;
			if(map.size()==1)
				return new MethodType(map.getFirst());
			else {
				context.getProblemListener().reportAmbiguousIdentifier(type.getTypeName(), type);
				return null;
			}
		} else
			return decl.getType(context);
	}

	@Override
	public IValue interpret(Context context) throws PromptoError {
		IValue value = expression.interpret(context);
		if(value!=null) {
			IType target = getTargetType(context);
			if(target==DecimalType.instance() && value instanceof Integer)
				value = new Decimal(((Integer)value).doubleValue());
			else if(target==IntegerType.instance() && value instanceof Decimal)
				value = new Integer(((Decimal)value).longValue());
			else if(target.isMoreSpecificThan(context, value.getType()))
				value.setType(target);
		}
		return value;
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method, Flags flags) {
		IType target = getTargetType(context);
		if(target instanceof MethodType)
			return compileWithProxy(context, method, flags, (MethodType)target);
		else
			return compileCast(context, method, flags, target);
	}
	
	private ResultInfo compileWithProxy(Context context, MethodInfo method, Flags flags, MethodType target) {
		IMethodDeclaration decl = target.getMethod();
		ArgumentList args = decl.getArguments();
		// the JVM can only cast to declared types, so we need a proxy to convert the FunctionalInterface call into the concrete one
		// 1st parameter is method reference
		expression.compile(context, method, flags); // this would return a lambda
		// what interface we are casting to
		ClassConstant dest = new ClassConstant(target.getJavaType(context));
		method.addInstruction(Opcode.LDC, dest);
		// method name
		InterfaceType intf = new InterfaceType(args, decl.getReturnType());
		String methodName = intf.getInterfaceMethodName(); 
		method.addInstruction(Opcode.LDC, new StringConstant(methodName));
		// method arg types
		List<Type> javaTypes = args.stream().map(arg->arg.getJavaType(context)).collect(Collectors.toList());
		CompilerUtils.compileClassConstantsArray(method, javaTypes);
		// create the proxy
		MethodConstant m = new MethodConstant(PromptoProxy.class, "newProxy", Object.class, Class.class, String.class, Class[].class, Object.class);
		method.addInstruction(Opcode.INVOKESTATIC, m);
		method.addInstruction(Opcode.CHECKCAST, dest);
		return new ResultInfo(dest.getType());
	}


	public ResultInfo compileCast(Context context, MethodInfo method, Flags flags, IType target) {
		ResultInfo src = expression.compile(context, method, flags);
		Type dest = target.getJavaType(context);
		if(dest==Long.class)
			return CompilerUtils.numberToLong(method, src);
		else if(dest==Double.class)
			return CompilerUtils.numberToDouble(method, src);
		ClassConstant c = new ClassConstant(dest);
		method.addInstruction(Opcode.CHECKCAST, c);
		return new ResultInfo(dest);
	}

	@Override
	public void toDialect(CodeWriter writer) {
		switch(writer.getDialect()) {
		case E:
		case M:
			expression.toDialect(writer);
			writer.append(" as ");
			type.toDialect(writer);
			break;
		case O:
			writer.append("(");
			type.toDialect(writer);
			writer.append(")");
			expression.toDialect(writer);
			break;
		}
		
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		expression.declare(transpiler);
		IType target = getTargetType(transpiler.getContext());
		target.declare(transpiler);
	}
	
	
	@Override
	public boolean transpile(Transpiler transpiler) {
	    IType expType = this.expression.check(transpiler.getContext());
		IType target = getTargetType(transpiler.getContext());
	    if(expType==DecimalType.instance() && target==IntegerType.instance()) {
	        transpiler.append("Math.floor(");
	        this.expression.transpile(transpiler);
	        transpiler.append(")");
	    } else
	        this.expression.transpile(transpiler);
	    return false;
	}

}
