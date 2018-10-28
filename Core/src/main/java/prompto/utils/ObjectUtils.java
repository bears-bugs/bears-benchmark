package prompto.utils;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class ObjectUtils {

	@SuppressWarnings("unchecked")
	public static <T> T downcast(Class<T> klass, Object actual) {
		if(actual!=null && klass.isAssignableFrom(actual.getClass()))
			return (T)actual;
		else
			return null;
	}

	@SuppressWarnings("unchecked")
	public static Set<Class<?>> getClassesInCallStack() {
		return (Set<Class<?>>)(Object) Stream.of(new Throwable().getStackTrace())
				.map(StackTraceElement::getClassName)
				.filter(s->s.indexOf("$Lambda$")<0)
				.map(n->{try{ return Class.forName(n); } catch (ClassNotFoundException e) { throw new RuntimeException(e); }})
				.collect(Collectors.toSet());
	}


}
