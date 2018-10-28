package prompto.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public abstract class ClassPathVisitor {
	
	public abstract void visitClass(Class<?> klass);

	public void visitClassesInClassPath() {
		try {
			Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources("");
			while(urls.hasMoreElements()) 
				visitClassesInURL(urls.nextElement());
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void visitClassesInURL(URL url) {
		String protocol = url.getProtocol();
		switch(protocol) {
		case "file":
			visitClassesInDirectory(url);
			break;
		case "jar":
			visitClassesInJar(url);
		default:
			throw new RuntimeException("Unknown protocol:" + protocol);
		}
	}
	
	private void visitClassesInDirectory( URL url) {
		File file = new File(url.getPath());
		visitClassesInDirectory(file,"");
	}
	
	private void visitClassesInDirectory( File dir,String packagePath) {
		File[] files = dir.listFiles();
		for(File file : files) {
			if(file.isDirectory())
				visitClassesInDirectory(file,packagePath + "/" + file.getName());
			else if(file.getName().endsWith(".class"))
				visitClassInFile(file,packagePath);
		}
	}
	
	private void visitClassInFile( File file,String packagePath) {
		packagePath = packagePath.substring(1).replace('/', '.');
		String simpleName = file.getName().substring(0,file.getName().indexOf(".class"));
		String className = packagePath + '.' + simpleName;
		visitClass(className);
	}
	
	private void visitClass( String className) {
		try {
			Class<?> klass = Class.forName(className);
			visitClass(klass);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void visitClassesInJar( URL url) {
		try (InputStream stream = url.openStream()) {
			visitClassesInJarInputStream(stream);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void visitClassesInJarInputStream( InputStream stream) {
		try (JarInputStream jar = new JarInputStream(stream)) {
			visitClassesInJarInputStream( jar);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void visitClassesInJarInputStream( JarInputStream jar) {
		try {
			JarEntry entry = jar.getNextJarEntry();
			while(entry!=null) {
				visitClassInJarEntry( entry);
				entry = jar.getNextJarEntry();
			}
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void visitClassInJarEntry( JarEntry entry) {
		String name = entry.getName();
		if(!name.endsWith(".class"))
			return;
		String className = name.substring(1).substring(0,name.indexOf(".class")-1).replace('/', '.');
		visitClass(className);
	}

}