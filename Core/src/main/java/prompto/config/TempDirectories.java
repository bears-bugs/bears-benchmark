package prompto.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import prompto.utils.Logger;

public abstract class TempDirectories {
	
	private static Logger logger = new Logger();

	static File promptoDir;
	static File transpiledDir;
	static File javaClassesDir;
	
	public static void setPromptoDir(File directory) {
		TempDirectories.promptoDir = directory;
		TempDirectories.transpiledDir = createTranspiledDir();
		TempDirectories.javaClassesDir = createJavaClassesDir();
	}
	
	public static File getPromptoDir() {
		return promptoDir;
	}

	public static void create() throws IOException {
		setPromptoDir(Files.createTempDirectory("prompto_").toFile());
	}

	private static File createTranspiledDir() {
		File jsDir = new File(promptoDir, "js");
		File transpiledDir = new File(jsDir, "transpiled");
		if(!transpiledDir.exists()) {
			logger.debug(()->"Storing transpiled files in " + transpiledDir.getAbsolutePath());
			transpiledDir.mkdirs();
			if(!transpiledDir.exists())
				throw new RuntimeException("Could not create prompto transpiled dir at " + transpiledDir.getAbsolutePath());
		}
		return transpiledDir;
	}

	public static File getTranspiledDir() {
		return transpiledDir;
	}

	private static File createJavaClassesDir() {
		File javaDir = new File(promptoDir, "java");
		File classesDir = new File(javaDir, "classes");
		if(!classesDir.exists()) {
			logger.debug(()->"Storing compiled classes in " + classesDir.getAbsolutePath());
			classesDir.mkdirs();
			if(!classesDir.exists())
				throw new RuntimeException("Could not create prompto class dir at " + classesDir.getAbsolutePath());
		}
		return classesDir;
	}

	public static File getJavaClassesDir() {
		return javaClassesDir;
	}


}