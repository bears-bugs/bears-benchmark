package prompto.intrinsic;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class PromptoCallSite {

	public static CallSite bootstrap(MethodHandles.Lookup lookup, Class<?> checkParamsClass, MethodHandle[] methods, MethodType type) throws Throwable {
		return bootstrap(lookup, checkParamsClass, Arrays.asList(methods), type);
	}

	public static CallSite bootstrap(Lookup lookup, Class<?> checkParamsClass, List<MethodHandle> methods, MethodType calling) throws Throwable {
		// get a list of methods compatible with type sorted by specificity (less specific first)
		methods = methods.stream()
			.filter((mh)->
				compatibleWith(calling, mh.type()))
			.sorted(methodTypesComparator(false))
			.collect(Collectors.toList());
		// get a method handle guarded with an instance check	
		MethodHandle method = guardWithTest(lookup, checkParamsClass, methods, calling);
		// done
		return new ConstantCallSite(method);
	}

	private static MethodHandle guardWithTest(Lookup lookup, Class<?> checkParamsClass, List<MethodHandle> methods, MethodType callingType) throws Throwable {
		MethodType testType = callingType.changeReturnType(boolean.class);
		Iterator<MethodHandle> iter = methods.iterator();
		MethodHandle fallback = iter.next(); 
		while(iter.hasNext()) {
			MethodHandle target = iter.next();
			MethodHandle test = findCheckParamsMethod(lookup, checkParamsClass, target.type());
			fallback = MethodHandles.guardWithTest(test.asType(testType), target.asType(callingType), fallback.asType(callingType));
		}
		return fallback.asType(callingType);
	}

	private static MethodHandle findCheckParamsMethod(Lookup lookup, Class<?> checkParamsClass, MethodType type) throws Throwable {
		String name = buildTestMethodName(type);
		MethodType proto = type.changeReturnType(boolean.class);
		for(int i=0;i<proto.parameterCount();i++)
			proto = proto.changeParameterType(i, Object.class);
		return lookup.findStatic(checkParamsClass, name, proto);
	}

	private static String buildTestMethodName(MethodType type) {
		StringBuilder sb = new StringBuilder();
		sb.append("checkParams");
		for(Class<?> klass : type.parameterList())
			sb.append(klass.getSimpleName());
		return sb.toString();
	}
	
	private static Comparator<MethodHandle> methodTypesComparator(boolean reverse) {
		return new Comparator<MethodHandle>() {
			@Override
			public int compare(MethodHandle mh1, MethodHandle mh2) {
				int result = compareTypes(mh1.type(), mh2.type());
				return reverse ? -result : result;
			}
		};
	}

	private static int compareTypes(MethodType t1, MethodType t2) {
		Iterator<Class<?>> it1 = t1.parameterList().iterator();
		Iterator<Class<?>> it2 = t2.parameterList().iterator();
		while(it1.hasNext()) {
			if(!it2.hasNext())
				return 1; // t1 is more specific than t2
			Class<?> k1 = it1.next();
			Class<?> k2 = it2.next();
			if(k1==k2 || k1.equals(k2))
				continue;
			boolean a1from2 = k1.isAssignableFrom(k2);
			boolean a2from1 = k2.isAssignableFrom(k1);
			if(a1from2==a2from1)
				continue;
			if(a1from2)
				return -1; // t2 is more specific than t1
			else
				return 1; // t1 is more specific than t2
		}
		if(it2.hasNext())
			return -1; // t2 is more specific than t1
		else
			return 0;
	}

	private static boolean compatibleWith(MethodType required, MethodType offered) {
		if(required.equals(offered))
			return true;
		if(!required.returnType().isAssignableFrom(offered.returnType()))
			return false;
		if(required.parameterCount()!=offered.parameterCount())
			return false;
		for(int i=0;i<required.parameterCount();i++) {
			if(!required.parameterType(i).isAssignableFrom(offered.parameterType(i)))
				return false;
		}
		return true;
	}
}
