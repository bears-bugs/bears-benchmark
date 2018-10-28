package prompto.java;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import prompto.compiler.CompilerException;
import prompto.compiler.Descriptor;
import prompto.compiler.IConstantOperand;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.PromptoClassLoader;
import prompto.compiler.PromptoType;
import prompto.compiler.ResultInfo;
import prompto.declaration.IDeclaration;
import prompto.declaration.IMethodDeclaration;
import prompto.declaration.NativeCategoryDeclaration;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.expression.IExpression;
import prompto.runtime.Context;
import prompto.type.CategoryType;
import prompto.type.IType;
import prompto.type.MethodType;
import prompto.type.VoidType;
import prompto.utils.CodeWriter;
import prompto.value.IValue;
import prompto.value.NativeInstance;


public class JavaMethodExpression extends JavaSelectorExpression {

	String name;
	JavaExpressionList arguments;
	
	public JavaMethodExpression(String name, JavaExpressionList arguments) {
		this.name = name;
		this.arguments = arguments!=null ? arguments : new JavaExpressionList();
	}

	@Override
	public void toDialect(CodeWriter writer) {
		parent.toDialect(writer);
		writer.append('.');
		writer.append(name);
		writer.append('(');
		arguments.toDialect(writer);
		writer.append(')');
	}
	
	@Override
	public String toString() {
		return parent.toString() + "." + name + "(" + arguments.toString() + ")";
	}
	
	@Override
	public IType check(Context context) {
		try {
			Method method = findMethod(context);
			if(method==null) {
				context.getProblemListener().reportUnknownMethod(name, this);
				return VoidType.instance();
			} else
				return new JavaClassType(method.getGenericReturnType());
		} catch(ClassNotFoundException e) {
			throw new SyntaxError(e.getMessage());
		}
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method) {
		// push instance if any
		ResultInfo parentType = parent.compile(context, method); 
		// push arguments if any
		for(JavaExpression arg : arguments)
			arg.compile(context, method);
		// write method call
		try {
			Method m = findMethod(context, parentType.getType());
			Descriptor.Method dm = new Descriptor.Method(m.getParameterTypes(), m.getReturnType());
			IConstantOperand operand = new MethodConstant(parentType.getType(), m.getName(), dm);
			if(parentType.isStatic())
				method.addInstruction(Opcode.INVOKESTATIC, operand);
			else
				method.addInstruction(Opcode.INVOKEVIRTUAL, operand);
			return new ResultInfo(m.getReturnType());
		} catch(ClassNotFoundException e) {
			throw new CompilerException(e);
		}
	}
	
	@Override
	public Object interpret(Context context) throws PromptoError {
		Object instance = parent.interpret(context);
		if(instance==null)
			throw new SyntaxError("Could not locate: " + parent.toString());
		if(instance instanceof NativeInstance)
			instance = ((NativeInstance)instance).getInstance();
		try {
			Method method = findMethod(context, instance);
			if(method==null)
				throw new SyntaxError("Could not locate: " + this.toString());
			Object[] args = evaluate_arguments(context, method);
			Class<?> klass = instance instanceof Class<?> ? (Class<?>)instance : instance.getClass(); 
			if(klass==instance)
				instance = null;
			try {
				return method.invoke(instance, args);
			} catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		} catch(ClassNotFoundException e) {
			throw new InternalError(e);
		}
	}
	
	Object[] evaluate_arguments(Context context, Method method) throws PromptoError {
		Object[] args = new Object[arguments.size()];
		Class<?>[] types = method.getParameterTypes();
		for(int i=0;i<args.length;i++) {
			JavaExpression exp = arguments.get(i);
			args[i] = evaluate_argument(context, exp, types[i]);
		}
		return args;
	}

	Object evaluate_argument(Context context, JavaExpression expression, Class<?> type) throws PromptoError {
        Object value = expression.interpret(context);
        if (value instanceof IExpression)
            value = ((IExpression)value).interpret(context);
        if (value instanceof IValue)
            value = ((IValue)value).convertTo(context, type);
        return value;
    }

	public Method findMethod(Context context) throws ClassNotFoundException {
		IType type = parent.check(context);
		if(type==null) {
			context.getProblemListener().reportUnknownIdentifier(parent.toString(), parent);
			return null;
		} else {
			Type klass = findClass(context, type);
			return findMethod(context, klass);
		}
	}
	
	private Type findClass(Context context, IType type) {
		if(type instanceof CategoryType) {
			IDeclaration named = context.getRegisteredDeclaration(IDeclaration.class, type.getTypeNameId());
			if(named instanceof NativeCategoryDeclaration) 
				return ((NativeCategoryDeclaration)named).getBoundClass(true);
		}
		return type.getJavaType(context);
	}

	public Method findMethod(Context context, Object instance) throws ClassNotFoundException {
		if(instance instanceof PromptoType)
			instance = Class.forName(((PromptoType)instance).getTypeName(), true, PromptoClassLoader.getInstance());
		if(instance instanceof Class<?>)
			return findMethod(context, (Class<?>)instance);
		else
			return findMethod(context, instance.getClass());
	}
	
	public Method findMethod(Context context, Class<?> klass) {
		if(klass==null)
			return null;
		Method method = findExactMethod(context, klass);
		if(method!=null)
			return method;
		else
			return findCompatibleMethod(context, klass);
	}
	
	private Method findExactMethod(Context context, Class<?> klass) {
		Class<?>[] types = new Class<?>[arguments.size()];
		int i = 0;
		try {
			for(JavaExpression exp  : arguments) {
				Type argType = exp.check(context).getJavaType(context);
				if(argType instanceof PromptoType)
					argType = Class.forName(argType.getTypeName());
				types[i++] = (Class<?>)argType;
			}
			return klass.getDeclaredMethod(name, types);
		} catch (NoSuchMethodException | ClassNotFoundException e) {
			return null;
		}
	}

	private Method findCompatibleMethod(Context context, Class<?> klass) {
		Method[] methods = klass.getMethods();
		for(Method m : methods) {
			if(!name.equals(m.getName())) 
				continue;
			if(validPrototype(context, m))
				return m;
		}
		return null; 
	}

	boolean validPrototype(Context context,Method method) {
		Class<?>[] types = method.getParameterTypes();
		if(types.length!=arguments.size())
			return false;
		for(int i=0;i<types.length;i++) {
			if(!validArgument(context, types[i], arguments.get(i)))
				return false;
		}
		return true;
	}
	
	boolean validArgument(Context context, Class<?> klass, JavaExpression argument) {
		IType argIType = argument.check(context);
		if(argIType instanceof MethodType && klass==IMethodDeclaration.class) {
			return true;
		} else {
			Type argType = argIType.getJavaType(context);
			if(argType instanceof PromptoType) try {
				argType = Class.forName(argType.getTypeName());
			} catch (ClassNotFoundException e) {
				return false;
			}
			return klass.isAssignableFrom((Class<?>)argType);
		}
	}
}
