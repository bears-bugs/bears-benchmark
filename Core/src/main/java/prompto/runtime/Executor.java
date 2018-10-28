package prompto.runtime;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import prompto.compiler.CompilerUtils;
import prompto.compiler.PromptoClassLoader;
import prompto.declaration.TestMethodDeclaration;
import prompto.error.InternalError;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.grammar.Identifier;
import prompto.intrinsic.PromptoDict;
import prompto.utils.CmdLineParser;

public abstract class Executor {

	public static void executeTests(Context context) {
		Collection<TestMethodDeclaration> tests = context.getTests();
		for(TestMethodDeclaration test : tests) {
			Context local = context.newLocalContext();
			executeTest(local, test.getName(), true);
		}
	}

	private static void executeTest(Context context, String testName, boolean testMode) {
		try(PromptoClassLoader loader = PromptoClassLoader.initialize(context, testMode)) {
			Type classType = CompilerUtils.getTestType(testName);
			Class<?> klass = loader.loadClass(classType.getTypeName());
			Method method = klass.getDeclaredMethod("run");
			method.invoke(null);
		} catch(ClassNotFoundException | NoSuchMethodException e) {
			throw new SyntaxError("Could not find a compatible \"" + testName + "\" test.");
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | IOException e) {
			throw new InternalError(e);
		} finally {
			context.notifyTerminated();
		}	
	}
	
	public static void executeTest(PromptoClassLoader loader, String testName) throws ClassNotFoundException, NoSuchMethodException, 
			SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Type classType = CompilerUtils.getTestType(testName);
		Class<?> klass = loader.loadClass(classType.getTypeName());
		Method method = klass.getDeclaredMethod("run");
		method.invoke(null);
	}

	public static void executeMainNoArgs(Context context) throws PromptoError {
		executeMainMethod(context, new Identifier("main"), "-testMode=true", null);
	}

	public static void executeMainNoArgs(Context context, File promptoDir) throws PromptoError {
		executeMainMethod(context, new Identifier("main"), "-testMode=true", promptoDir);
	}
	
	private static PromptoDict<String, String> parseCmdLineArgs(String cmdLineArgs) {
		PromptoDict<String, String> result = new PromptoDict<>(false);
		try {
			Map<String, String> args = CmdLineParser.parse(cmdLineArgs);
			result.putAll(args);
		} catch(Exception e) {
			e.printStackTrace(System.err); 
		}
		return result;
	}
	
	public static void executeMainMethod(Context context, Identifier methodName, String cmdLineArgs, File promptoDir) throws PromptoError {
		PromptoDict<String, String> options = parseCmdLineArgs(cmdLineArgs);
		boolean testMode = options.containsKey("testMode");
		Class<?>[] argTypes = cmdLineArgs==null ? new Class<?>[0] : new Class<?>[] { PromptoDict.class };
		Object[] args = cmdLineArgs==null ? new Object[0] : new Object[] { options };
		try(PromptoClassLoader loader = PromptoClassLoader.initialize(context, testMode)) {
			executeGlobalMethod(loader, methodName, argTypes, args);
		} catch(NoSuchMethodException e) {
			if(e.getCause() instanceof PromptoError)
				throw (PromptoError)e.getCause();
			else
				throw new InternalError(e.getCause());
		} catch(ClassNotFoundException e) {
			if(e.getException() instanceof PromptoError)
				throw (PromptoError)e.getCause();
			else
				throw new InternalError(e.getException());
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | IOException e) {
			e.printStackTrace(System.err);
			throw new InternalError(e);
		} finally {
			context.notifyTerminated();
		}
	}

	public static Object executeGlobalMethod(PromptoClassLoader loader, Identifier methodName, Class<?>[] argTypes, Object[] args) 
			throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, 
				IllegalArgumentException, InvocationTargetException {
		Type classType = CompilerUtils.getGlobalMethodType(methodName);
		Class<?> klass = loader.loadClass(classType.getTypeName());
		Method method = locateGlobalMethod(klass, argTypes);
		if(method.getParameterCount()==0)
			return method.invoke(null);
		else
			return method.invoke(null, args);
	}

	public static Method locateGlobalMethod(Class<?> klass, Class<?> ... argTypes) throws NoSuchMethodException {
		// try exact match first
		for(Method method : klass.getDeclaredMethods()) {
			if(identicalArguments(method.getParameterTypes(), argTypes))
				return method;
		}
		// match Text{} argument, will pass null argument
		if(argTypes.length==0) {
			argTypes = new Class<?>[] { Map.class };
			for(Method method : klass.getDeclaredMethods()) {
				if(identicalArguments(method.getParameterTypes(), argTypes))
					return method;
			}
		}
		// match no argument, will ignore options
		for(Method method : klass.getDeclaredMethods()) {
			if(method.getParameterTypes().length==0)
				return method;
		}
		throw new NoSuchMethodException(klass.getSimpleName() + argTypes.toString());
	}

	private static boolean identicalArguments(Class<?>[] expected, Class<?>[] provided) {
		if(expected.length!=provided.length)
			return false;
		for(int i=0;i<expected.length;i++) {
			if(expected[i]!=provided[i])
				return false;
		}
		return true;
	}



}
