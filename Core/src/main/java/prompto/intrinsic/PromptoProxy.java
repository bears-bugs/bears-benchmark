package prompto.intrinsic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public abstract class PromptoProxy {

	/* for anonymous parameters (any x with y, z), we have to create a local combining interface */
	/* on which to call x.y or x.z. However the parameter does not implement this interface, so we */
	/* use a proxy class for conversion purpose only. This is slower than a direct call, so we might */
	/* want to rely on interface injection instead when it becomes available */
	@SuppressWarnings("unchecked")
	public static <T> T newProxy(Object o, Class<T> klass) {
		return (T)Proxy.newProxyInstance(
				klass.getClassLoader(), 
				new Class<?>[] { klass }, 
				new InvocationHandler() {

					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						return method.invoke(o, args);
					}
					
				});
		
	}
	
	
	/* for method references, since the FunctionalInterface class is not known in advance by the concrete implementation */
	/* we need a proxy to bridge the FunctionalInterface method (get, apply, accept...) with the actual method */
	@SuppressWarnings("unchecked")
	public static <T> T newProxy(Object o, Class<T> klass, String methodName, Class<?>[] parameterTypes) {
		
		Class<?>[] paramTypes = adjustParameterTypesForLambda(o.getClass(), parameterTypes);
		
		return (T)Proxy.newProxyInstance(
				klass.getClassLoader(), 
				new Class<?>[] { klass }, 
				new InvocationHandler() {

					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						method = o.getClass().getMethod(methodName, paramTypes);
						return method.invoke(o, args);
					}
					
				});
		
	}


	private static Class<?>[] adjustParameterTypesForLambda(Class<? extends Object> klass, Class<?>[] parameterTypes) {
		// if the method reference is a lambda, then parameterTypes are all Object.class 
		if(klass.getName().startsWith(PromptoMethod.class.getName() + "$$Lambda$")) {
			Class<?>[] objectTypes = new Class<?>[parameterTypes.length];
			Arrays.fill(objectTypes, Object.class);
			return objectTypes;
		} else
			return parameterTypes;
	}
}
