package prompto.intrinsic;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import prompto.compiler.PromptoClassLoader;

public abstract class PromptoException {

	public static String getExceptionTypeName(Object e) {
		Class<?> type = e.getClass();
		return getExceptionTypeName(type);
	}
	
	public static String getExceptionTypeName(Class<?> type) {
		if(ArithmeticException.class==type)
			return "DIVIDE_BY_ZERO";
		else if(IndexOutOfBoundsException.class==type)
			return "INDEX_OUT_OF_RANGE";
		else if(NullPointerException.class==type)
			return "NULL_REFERENCE";
		else
			return type.getSimpleName();
	}

	public static Type getExceptionType(String name) {
		switch(name) {
		case "DIVIDE_BY_ZERO":
			return ArithmeticException.class;
		case "INDEX_OUT_OF_RANGE":
			return IndexOutOfBoundsException.class;
		case "NULL_REFERENCE":
			return NullPointerException.class;
		default:
			return null;
		}
	}
	
	public static void throwEnumeratedException(String name) {
		try {
			String exceptionName = "π.ε.Error$%Error";
			PromptoClassLoader loader = PromptoClassLoader.getInstance();
			if(loader==null)
				throw new UnsupportedOperationException("throwEnumeratedException can only be used in compiled mode!");
			Class<?> klass = Class.forName(exceptionName, true, loader);
			Field field = klass.getDeclaredField(name);
			RuntimeException instance = (RuntimeException)(field.get(null));
			throw instance;
		} catch(ClassNotFoundException | NoSuchFieldException | IllegalAccessException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void throwEnumeratedException(String name, String message) {
		try {
			String exceptionName = "π.ε.Error$%Error$" + name;
			PromptoClassLoader loader = PromptoClassLoader.getInstance();
			if(loader==null)
				throw new UnsupportedOperationException("throwEnumeratedException can only be used in compiled mode!");
			Class<RuntimeException> klass = (Class<RuntimeException>) Class.forName(exceptionName, true, loader);
			Constructor<RuntimeException> ctor = klass.getDeclaredConstructor(String.class);
			RuntimeException instance = ctor.newInstance(message);
			throw instance;
		} catch(ClassNotFoundException | InstantiationException | IllegalAccessException | SecurityException | NoSuchMethodException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
}
