package prompto.java;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLClassLoader;

import prompto.compiler.ClassConstant;
import prompto.compiler.CompilerUtils;
import prompto.compiler.FieldConstant;
import prompto.compiler.IOperand;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.compiler.ResultInfo.Flag;
import prompto.compiler.StackLocal;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.grammar.INamed;
import prompto.grammar.Identifier;
import prompto.parser.Section;
import prompto.runtime.Context;
import prompto.type.IType;
import prompto.utils.CodeWriter;


public class JavaIdentifierExpression extends Section implements JavaExpression {

	
	public static JavaIdentifierExpression parse(String ids) {
		String[] parts = ids.split("\\.");
		JavaIdentifierExpression result = null;
		for(String part : parts)
			result = new JavaIdentifierExpression(result,part);
		return result;
	}
	
	JavaIdentifierExpression parent = null;
	String name;
	boolean isChildClass = false;
	
	public JavaIdentifierExpression(String name) {
		this.name = name;
	}

	public JavaIdentifierExpression(JavaIdentifierExpression parent, String name) {
		this.parent = parent;
		this.name = name;
	}
	
	public JavaIdentifierExpression(JavaIdentifierExpression parent, String name, boolean isChildClass) {
		this.parent = parent;
		this.name = name;
		this.isChildClass = isChildClass;
	}

	public JavaIdentifierExpression getParent() {
		return parent;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		if(parent!=null) {
			parent.toDialect(writer);
			writer.append(isChildClass ? '$' : '.');
		}
		writer.append(name);
	}
	
	@Override
	public String toString() {
		if(parent==null)
			return name;
		else 
			return parent.toString() + (isChildClass ? '$' : '.') + name;
	}
	
	@Override
	public ResultInfo compile(Context context, MethodInfo method) {
		if(parent==null)
			return compile_root(context, method);
		else
			return compile_child(context, method);
	}
	
	private ResultInfo compile_root(Context context, MethodInfo method) {
		ResultInfo info = compile_prompto(context, method);
		if(info!=null)
			return info;
		else
			info = compile_instance(context, method);
		if(info!=null)
			return info;
		else
			return compile_class(context, method);
	}

	private ResultInfo compile_prompto(Context context, MethodInfo method) {
		JavaIdentifierProcessor processor = JavaIdentifierProcessor.processors.get(name);
		if(processor==null)
			return null;
		else
			return processor.compile(context, method);
	}

	private ResultInfo compile_instance(Context context, MethodInfo method) {
		INamed named = context.getRegisteredValue(INamed.class, new Identifier(name));
		if(named==null)
			return null;
		StackLocal local = method.getRegisteredLocal(name);
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

	private ResultInfo compile_child(Context context, MethodInfo method) {
		ResultInfo info = parent.compile(context, method);
		if(info!=null)
			return compile_field(context, method, info);
		else
			return compile_class(context, method);
	}

	private ResultInfo compile_field(Context context, MethodInfo method, ResultInfo info) {
		try {
			Class<?> klass = (Class<?>)info.getType();
			Field field = klass.getField(name);
			IOperand oper = new FieldConstant(info.getType(), name, field.getType());
			if(info.isStatic())
				method.addInstruction(Opcode.GETSTATIC, oper);
			else
				method.addInstruction(Opcode.GETFIELD, oper);
			return new ResultInfo(field.getType());
		} catch (NoSuchFieldException e) { 
			return null;
		}
	}

	private ResultInfo compile_class(Context context, MethodInfo method) {
		String fullName = this.toString();
		try {
			Class<?> klass = addOnsClassLoader==null ? Class.forName(fullName) : addOnsClassLoader.loadClass(fullName);
			return new ResultInfo(klass, Flag.STATIC);
		} catch (ClassNotFoundException e1) {
			// package prefix not required for classes in java.lang package
			if(parent==null) try {
				fullName = "java.lang." + name;
				return new ResultInfo(Class.forName(fullName), Flag.STATIC);
			} catch (ClassNotFoundException e2) {
			}	
		}
		return null;
	}

	@Override
	public Object interpret(Context context) throws PromptoError {
		if(parent==null)
			return interpret_root(context);
		else
			return interpret_child(context);
	}
	
	Object interpret_root(Context context) throws PromptoError {
		Object o = interpret_prompto(context);
		if(o!=null)
			return o;
		o = interpret_instance(context);
		if(o!=null)
			return o;
		else
			return interpret_class(); // as an instance for static field/method
	}

	private Object interpret_prompto(Context context) {
		JavaIdentifierProcessor processor = JavaIdentifierProcessor.processors.get(name);
		if(processor==null)
			return null;
		else
			return processor.interpret(context);
	}

	Object interpret_instance(Context context) throws PromptoError {
		try {
			return context.getValue(new Identifier(name)); 
		} catch (PromptoError e) {
			return null;
		}
	}

	public Class<?> interpret_class() {
		String fullName = this.toString();
		try {
			return addOnsClassLoader==null ? Class.forName(fullName) : addOnsClassLoader.loadClass(fullName);
		} catch (ClassNotFoundException e1) {
			// package prefix not required for classes in java.lang package
			if(parent==null) try {
				fullName = "java.lang." + name;
				return Class.forName(fullName);
			} catch (ClassNotFoundException e2) {
			}	
		}
		return null;
	}

	Object interpret_child(Context context) throws PromptoError {
		Object o = parent.interpret(context); 
		if(o!=null)
			return interpret_field(o);
		else
			return interpret_class();
	}
	
	Object interpret_field(Object o) {
		Class<?> klass = null;
		if(o instanceof Class<?>) {
			klass = (Class<?>)o;
			o = null;
		} else
			klass = o.getClass();
		try {
			Field field = klass.getField(name);
			return field.get(o);
		} catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException e) { 
			return null;
		}
	}

	@Override
	public IType check(Context context) {
		if(parent==null)
			return check_root(context);
		else
			return check_child(context);
	}
	
	IType check_root(Context context) {
		IType t = check_prompto(context);
		if(t!=null)
			return t;
		t = check_instance(context);
		if(t!=null)
			return t;
		else
			return check_class(); // as an instance for accessing static field/method
	}

	private IType check_prompto(Context context) {
		JavaIdentifierProcessor processor = JavaIdentifierProcessor.processors.get(name);
		if(processor==null)
			return null;
		else
			return processor.check(context);
	}

	IType check_instance(Context context) {
		INamed named = context.getRegisteredValue(INamed.class, new Identifier(name)); 
		if(named==null)
			return null;
		try {
			return named.getType(context);
		} catch (SyntaxError e) {
			return null;
		}
	}

	private static ClassLoader addOnsClassLoader; 

	public static void registerAddOns(URL[] addOnURLs, ClassLoader parent) {
		if(addOnURLs!=null && addOnURLs.length>0)
			addOnsClassLoader = new URLClassLoader(addOnURLs, parent);
	}

	IType check_class() {
		String fullName = this.toString();
		try {
			Class<?> klass = addOnsClassLoader==null ? Class.forName(fullName) : addOnsClassLoader.loadClass(fullName);
			return new JavaClassType(klass);
		} catch (ClassNotFoundException e1) {
			// package prefix not required for classes in java.lang package
			if(parent==null) try {
				fullName = "java.lang." + name;
				Class<?> klass = Class.forName(fullName);
				return new JavaClassType(klass);
			} catch (ClassNotFoundException e2) {
			}	
		}
		return null;
	}
	
	IType check_child(Context context) {
		IType t = parent.check(context); 
		if(t!=null)
			return check_field(context, t);
		else
			return check_class();
	}
	
	IType check_field(Context context, IType t) {
		if(!(t instanceof JavaClassType))
			return null;
		Type klass = t.getJavaType(context);
		if(klass instanceof Class) try {
			Field field = ((Class<?>)klass).getField(name);
			return new JavaClassType(field.getType());
		} catch (NoSuchFieldException e) { 
			return null;
		} else {
			throw new UnsupportedOperationException();
		}
	}


}
