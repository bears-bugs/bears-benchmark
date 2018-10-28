package prompto.intrinsic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class PromptoMethod {

	static Map<Integer, BiFunction<Method, Object, Object>> consumerProducers = getConsumerProducers();
	static Map<Integer, BiFunction<Method, Object, Object>> supplierProducers = getSupplierProducers();
	
	private static Map<Integer, BiFunction<Method, Object, Object>> getConsumerProducers() {
		Map<Integer, BiFunction<Method, Object, Object>> result = new HashMap<>();
		result.put(0, PromptoMethod::newRunnable);
		result.put(1, PromptoMethod::newConsumer);
		return result;
	}


	private static Map<Integer, BiFunction<Method, Object, Object>> getSupplierProducers() {
		Map<Integer, BiFunction<Method, Object, Object>> result = new HashMap<>();
		result.put(1, PromptoMethod::newFunction);
		return result;
	}

	// TODO use LambdaMetaFactory
	public static Object newMethodReference(Class<?> klass, String methodName, Object instance) {
		Method method = Arrays.asList(klass.getDeclaredMethods()).stream()
				.filter(m->methodName.equals(m.getName()))
				.findFirst()
				.orElse(null);
		if(method.getReturnType()==void.class)
			return newConsumerReference(method, instance);
		else
			return newSupplierReference(method, instance);
	}

	
	private static Object newSupplierReference(Method method, Object instance) {
		BiFunction<Method, Object, Object> supplier = supplierProducers.get(method.getParameterCount());
		if(supplier==null)	
			throw new UnsupportedOperationException("newSupplierReference with " + method.getParameterCount() + " parameter(s)");
		return supplier.apply(method, instance);
	}


	private static Object newConsumerReference(Method method, Object instance) {
		BiFunction<Method, Object, Object> consumer = consumerProducers.get(method.getParameterCount());
		if(consumer==null)	
			throw new UnsupportedOperationException("newConsumerReference with " + method.getParameterCount() + " parameter(s)");
		return consumer.apply(method, instance);
	}
	
	private static Object safeInvoke(Method method, Object instance, Object ... args) {
		try {
			return method.invoke(instance, args);
		} catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	private static Runnable newRunnable(Method method, Object instance) {
		return ()->safeInvoke(method, instance);
	}

	private static Consumer<Object> newConsumer(Method method, Object instance) {
		return (a)->safeInvoke(method, instance, a);
	}

	private static Function<Object, Object> newFunction(Method method, Object instance) {
		return (a)->safeInvoke(method, instance, a);
	}
}
