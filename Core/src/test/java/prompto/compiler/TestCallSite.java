package prompto.compiler;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import prompto.intrinsic.PromptoCallSite;
import prompto.runtime.utils.Out;

public class TestCallSite {

	@Before
	public void before() {
		Out.init();
	}
	
	@After
	public void after() {
		Out.restore();
	}
	
	public static interface Root {
		
	}

	public static interface Parent extends Root {
		
	}

	public static interface Child extends Parent {
		
	}

	public static void print(Root r) {
		System.out.print("/Root");
	}
	
	public static boolean checkParamsRoot(Object r) {
		return r instanceof Root;
	}

	public static void print(Parent r) {
		System.out.print("/Parent");
	}

	public static boolean checkParamsParent(Object r) {
		return r instanceof Parent;
	}

	public static void print(Child r) {
		System.out.print("/Child");
	}

	public static boolean checkParamsChild(Object r) {
		return r instanceof Child;
	}

	public static void print(Root r1, Root r2) {
		System.out.print("/Root-Root");
	}
	
	public static void print(Root r1, Parent p2) {
		System.out.print("/Root-Parent");
	}
	
	public static boolean checkParamsRootParent(Object r1, Object r2) {
		return r1 instanceof Root && r2 instanceof Parent;
	}

	public static void print(Root r1, Child c2) {
		System.out.print("/Root-Child");
	}
	
	public static boolean checkParamsRootChild(Object r1, Object r2) {
		return r1 instanceof Root && r2 instanceof Child;
	}

	public static void print(Parent p1, Root r2) {
		System.out.print("/Parent-Root");
	}

	public static boolean checkParamsParentRoot(Object r1, Object r2) {
		return r1 instanceof Parent && r2 instanceof Root;
	}

	public static void print(Parent p1, Parent p2) {
		System.out.print("/Parent-Parent");
	}

	public static boolean checkParamsParentParent(Object r1, Object r2) {
		return r1 instanceof Parent && r2 instanceof Parent;
	}

	public static void print(Parent p1, Child c2) {
		System.out.print("/Parent-Child");
	}

	public static boolean checkParamsParentChild(Object r1, Object r2) {
		return r1 instanceof Parent && r2 instanceof Child;
	}

	public static void print(Child c1, Root r2) {
		System.out.print("/Child-Root");
	}

	public static boolean checkParamsChildRoot(Object r1, Object r2) {
		return r1 instanceof Child && r2 instanceof Root;
	}

	public static void print(Child c1, Parent p2) {
		System.out.print("/Child-Parent");
	}

	public static boolean checkParamsChildParent(Object r1, Object r2) {
		return r1 instanceof Child && r2 instanceof Parent;
	}

	public static void print(Child c1, Child c2) {
		System.out.print("/Child-Child");
	}

	public static boolean checkParamsChildChild(Object r1, Object r2) {
		return r1 instanceof Child && r2 instanceof Child;
	}

	
	public static CallSite bootstrapRoot(MethodHandles.Lookup caller, String name, MethodType type) throws NoSuchMethodException, IllegalAccessException {
		MethodHandle mh = MethodHandles.lookup().findStatic(TestCallSite.class, "print", 
				MethodType.methodType(void.class, Root.class));
		return new ConstantCallSite(mh.asType(type));
	}
	
	@Test
	public void testRootMethodOnly() throws Throwable {
		CallSite site = bootstrapRoot(MethodHandles.lookup(), "dummy", MethodType.methodType(void.class, Root.class));
		site.dynamicInvoker().invoke(new Root(){});
		site.dynamicInvoker().invoke(new Parent(){});
		site.dynamicInvoker().invoke(new Child(){});
		assertEquals("/Root/Root/Root", Out.read());
	}

	public static CallSite bootstrapRootAndChild(MethodHandles.Lookup caller, String name, MethodType type) throws NoSuchMethodException, IllegalAccessException {
		MethodHandle mhRoot = MethodHandles.lookup().findStatic(TestCallSite.class, "print", 
				MethodType.methodType(void.class, Root.class)).asType(type);
		MethodHandle mhChild = MethodHandles.lookup().findStatic(TestCallSite.class, "print", 
				MethodType.methodType(void.class, Child.class)).asType(type);
		MethodType testType = type.changeReturnType(boolean.class);
		MethodHandle mhTest = MethodHandles.lookup().findStatic(TestCallSite.class, "checkParamsChild", 
				MethodType.methodType(boolean.class, Object.class)).asType(testType);
		MethodHandle mh = MethodHandles.guardWithTest(mhTest, mhChild, mhRoot);
		return new ConstantCallSite(mh.asType(type));
	}

	@Test
	public void testRootAndChildMethods() throws Throwable {
		CallSite site = bootstrapRootAndChild(MethodHandles.lookup(), "dummy", MethodType.methodType(void.class, Root.class));
		site.dynamicInvoker().invoke(new Root(){});
		site.dynamicInvoker().invoke(new Parent(){});
		site.dynamicInvoker().invoke(new Child(){});
		assertEquals("/Root/Root/Child", Out.read());
	}

	public static CallSite bootstrapRootAndChildMulti(MethodHandles.Lookup caller, String name, MethodType type) throws Throwable {
		MethodType testType = type.changeReturnType(boolean.class);
		List<MethodHandle> methods = collectMethods();
		Iterator<MethodHandle> iter = methods.iterator();
		MethodHandle fallback = iter.next(); 
		while(iter.hasNext()) {
			MethodHandle target = iter.next();
			MethodHandle test = findTest(target.type());
			fallback = MethodHandles.guardWithTest(test.asType(testType), target.asType(type), fallback.asType(type));
		}
		return new ConstantCallSite(fallback.asType(type));
	}
	
	private static MethodHandle findTest(MethodType type) throws Throwable {
		String name = buildTestMethodName(type);
		MethodType testType = type.changeReturnType(boolean.class);
		for(int i=0;i<type.parameterCount();i++)
			testType = testType.changeParameterType(i, Object.class);
		return MethodHandles.lookup().findStatic(TestCallSite.class, name, testType);
	}

	private static String buildTestMethodName(MethodType type) {
		StringBuilder sb = new StringBuilder();
		sb.append("checkParams");
		for(Class<?> klass : type.parameterList())
			sb.append(klass.getSimpleName());
		return sb.toString();
	}

	private static List<MethodHandle> collectMethods() throws Throwable {
		// from less specific to most specific
		Class<?>[] klasses = new Class<?>[] { Root.class, Parent.class, Child.class };
		List<MethodHandle> list = new ArrayList<>();
		for(Class<?> k1 : klasses) {
			for(Class<?> k2 : klasses) {
				list.add(MethodHandles.lookup().findStatic(
						TestCallSite.class, "print", 
						MethodType.methodType(void.class, k1, k2)));
			}
		}
		return list;
	}
	
	private static MethodHandle[] collectAllMethods() throws Throwable {
		Class<?>[] klasses = new Class<?>[] { Child.class, Parent.class, Root.class };
		List<MethodHandle> list = new ArrayList<>();
		for(Class<?> k1 : klasses) {
			list.add(MethodHandles.lookup().findStatic(
					TestCallSite.class, "print", 
					MethodType.methodType(void.class, k1)));
			for(Class<?> k2 : klasses) {
				list.add(MethodHandles.lookup().findStatic(
						TestCallSite.class, "print", 
						MethodType.methodType(void.class, k1, k2)));
			}
		}
		return list.toArray(new MethodHandle[list.size()]);
	}


	@Test
	public void testRootAndChildMultiMethods() throws Throwable {
		CallSite site = bootstrapRootAndChildMulti(MethodHandles.lookup(), "dummy", MethodType.methodType(void.class, Root.class, Root.class));
		site.dynamicInvoker().invoke(new Root(){}, new Root(){});
		site.dynamicInvoker().invoke(new Parent(){}, new Root(){});
		site.dynamicInvoker().invoke(new Child(){}, new Child(){});
		assertEquals("/Root-Root/Parent-Root/Child-Child", Out.read());
	}
	
	
	public static CallSite bootstrap(Lookup lookup, String name, MethodType type) throws Throwable {
		MethodHandle[] methods = collectAllMethods();
		return PromptoCallSite.bootstrap(lookup, TestCallSite.class, methods, type);
	}
	
	@Test
	public void testPromptoCallSite1() throws Throwable {
		CallSite site = bootstrap(MethodHandles.lookup(), "dummy", MethodType.methodType(void.class, Root.class, Root.class));
		site.dynamicInvoker().invoke(new Root(){}, new Root(){});
		site.dynamicInvoker().invoke(new Parent(){}, new Root(){});
		site.dynamicInvoker().invoke(new Child(){}, new Child(){});
		assertEquals("/Root-Root/Parent-Root/Child-Child", Out.read());
	}
	
	@Test
	public void testPromptoCallSite2() throws Throwable {
		CallSite site = bootstrap(MethodHandles.lookup(), "dummy", MethodType.methodType(void.class, Root.class));
		site.dynamicInvoker().invoke(new Root(){});
		site.dynamicInvoker().invoke(new Parent(){});
		site.dynamicInvoker().invoke(new Child(){});
		assertEquals("/Root/Parent/Child", Out.read());
	}


}
