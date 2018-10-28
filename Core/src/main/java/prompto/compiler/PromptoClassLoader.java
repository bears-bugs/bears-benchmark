package prompto.compiler;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;

import prompto.config.TempDirectories;
import prompto.error.SyntaxError;
import prompto.runtime.Context;
import prompto.runtime.Mode;
import prompto.utils.Logger;

/* a class loader which is able to create and store classes for prompto objects */
public class PromptoClassLoader extends URLClassLoader {
	
	static final Logger logger = new Logger();

	private static ClassLoader getParentClassLoader() {
		return PromptoClassLoader.class.getClassLoader();
	}

	private static URL[] makeClassesDirURLs() {
		File classesDir = TempDirectories.getJavaClassesDir();
		try {
			URL[] urls = { classesDir.toURI().toURL() };
			return urls;
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static PromptoClassLoader instance = null;
	
	/* during testing, mutiple threads may refer to different paths */
	private static Boolean testMode;
	private static ThreadLocal<PromptoClassLoader> testInstance;
	public static PromptoClassLoader getInstance() {
		return instance!=null ? instance : testInstance.get();
	}
	
	public static PromptoClassLoader initialize(Context context) {
		return initialize(context, Mode.get()==Mode.UNITTEST);
	}
	
	public static PromptoClassLoader initialize(Context context, boolean testMode) {
		synchronized(PromptoClassLoader.class) {
			if(PromptoClassLoader.testMode==null)
				PromptoClassLoader.testMode = testMode;
			boolean currentMode = PromptoClassLoader.testMode;
			if(currentMode!=testMode)
				throw new UnsupportedOperationException("Cannot run test mode and regular mode in parallel!");
			if(testMode) {
				if(testInstance==null)
					testInstance = new ThreadLocal<>();
				testInstance.set(new PromptoClassLoader(context));
				return testInstance.get();
			} else {
				if(instance!=null)
					throw new UnsupportedOperationException("Can only have one PromptoClassLoader!");
				instance = new PromptoClassLoader(context);
				return instance;
			}
		}
	}

	public static void uninitialize() {
		synchronized(PromptoClassLoader.class) {
			if(PromptoClassLoader.testMode==null)
				return;
			if(PromptoClassLoader.testMode)
				testInstance = null;
			else
				instance = null;
		}		
	}
	
	Context context;
	
	private PromptoClassLoader(Context context) {
		super(makeClassesDirURLs(), getParentClassLoader());
		this.context = context;
	}
	
	public Context getContext() {
		return context;
	}
	
	public void setContext(Context context) {
		this.context = context;
	}
	
	File getClassDir() throws Exception {
		return Paths.get(getURLs()[0].toURI()).toFile();
	}

	@Override
	protected Class<?> findClass(String fullName) throws ClassNotFoundException {
		try {
			return super.findClass(fullName);
		} catch (ClassNotFoundException e) {
			// is this a Prompto class ?
			if(fullName.charAt(0)=='π') { 
				try {
					createPromptoClass(fullName);
					return super.findClass(fullName);
				} catch (Throwable t) {
					t = extractSyntaxError(t);
					logger.error(()->"Compilation of " + fullName + " failed. Check: " +  TempDirectories.getJavaClassesDir().getAbsolutePath(), t);
					throw new ClassNotFoundException(fullName, t);
				}
			} else
				throw e;
		}
	}

	private void createPromptoClass(String fullName) throws Exception {
		File classDir = getClassDir();
		// logger.debug(()->"Compiling " + fullName + " @ " + classDir.toString());
		System.err.println("Compiling " + fullName + " @ " + classDir.toString());
		Compiler compiler = new Compiler(classDir); // where to store .class
		compiler.compileClass(context.getGlobalContext(), fullName);
	}

	private Throwable extractSyntaxError(Throwable t) {
		Throwable s = t;
		while(s!=null) {
			if(s instanceof SyntaxError)
				return s;
			if(s==s.getCause())
				break;
			s = s.getCause();
		} 
		return t;
	}
	

}
