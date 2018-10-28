package prompto.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

public class BaseTest {

	public String getResourceAsString(String resourceName) throws Exception {
		char[] data = new char[4096];
		InputStream stream = getResourceAsStream(resourceName);
		Reader reader = new InputStreamReader(stream);
		StringBuffer buffer = new StringBuffer();
		for(;;) {
			int read = reader.read(data);
			if(read==-1)
				break;
			buffer.append(data, 0, read);
		}
		stream.close();
		return buffer.toString();
	}

	public InputStream getResourceAsStream(String resourceName) throws Exception {
		InputStream stream = tryLoadCoreResource(resourceName);
		if(stream==null)
			stream = tryLoadTestResource(resourceName);
		if(stream==null)
			stream = tryLoadRuntimeResource(resourceName);
		return stream;
	}

	public URL getResourceAsURL(String resourceName) throws Exception {
		URL url = tryGetCoreResource(resourceName);
		if(url==null)
			url = tryGetTestResource(resourceName);
		if(url==null)
			url = tryGetRuntimeResource(resourceName);
		return url;
	}

	
	private URL tryGetRuntimeResource(String resourceName) throws Exception {
		File file = tryLocateRuntimeFile(resourceName);
		if(file!=null)
			return file.toURI().toURL();
		else
			return null;
	}

	private URL tryGetTestResource(String resourceName) throws Exception {
		File file = tryLocateTestFile(resourceName);
		if(file!=null)
			return file.toURI().toURL();
		else
			return null;
	}

	public URL tryGetCoreResource(String resourceName) throws Exception {
		return Thread.currentThread().getContextClassLoader().getResource(resourceName);
	}

	private InputStream tryLoadRuntimeResource(String resourceName) throws FileNotFoundException {
		File file = tryLocateRuntimeFile(resourceName);
		if(file!=null)
			return new FileInputStream(file);
		else
			return null;
	}
	
	public File tryLocateRuntimeFile(String resourceName) throws FileNotFoundException {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		String dirPath = loader.getResource("").getFile();
		int idx = dirPath.lastIndexOf("/Runtime/");
		if(idx<0)
			return null;
		String libsPath = dirPath.substring(0, idx) + "/prompto-libraries/";
		File file = new File(libsPath + resourceName);
		if(file.exists())
			return file;
		else
			return null;
	}
	
	public File[] listRuntimeFiles(String libraryName) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		String dirPath = loader.getResource("").getFile();
		int idx = dirPath.lastIndexOf("/Runtime/");
		if(idx<0)
			return null;
		String libsPath = dirPath.substring(0, idx) + "/prompto-libraries/";
		File file = new File(libsPath + libraryName);
		if(file.exists())
			return file.listFiles(new FilenameFilter() {
				@Override public boolean accept(File dir, String name) {
					return name.endsWith(".pec")
							|| name.endsWith(".poc")
							|| name.endsWith(".psc");
				}
			});
		else
			return null;
	}

	private InputStream tryLoadTestResource(String resourceName) throws FileNotFoundException {
		File file = tryLocateTestFile(resourceName);
		if(file!=null)
			return new FileInputStream(file);
		else
			return null;
	}
	
	public File tryLocateTestFile(String resourceName) throws FileNotFoundException {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		String dirPath = loader.getResource("").getFile();
		int idx = dirPath.lastIndexOf("/Core/");
		if(idx<0)
			idx = dirPath.lastIndexOf("/Standalone/");
		if(idx<0)
			idx = dirPath.lastIndexOf("/CodeStore/");
		if(idx<0)
			return null;
		String testsPath = dirPath.substring(0, idx) + "/prompto-tests/Tests/resources/";
		File file = new File(testsPath + resourceName);
		if(file.exists())
			return file;
		else
			return null;
	}

	private InputStream tryLoadCoreResource(String resourceName) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		return loader.getResourceAsStream(resourceName);
	}

}
