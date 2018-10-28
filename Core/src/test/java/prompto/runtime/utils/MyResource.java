package prompto.runtime.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import prompto.value.IResource;

public class MyResource implements IResource {

	static Map<String,String> contents = new ConcurrentHashMap<>();
	
	String path;
	BufferedReader reader;
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getContent() {
		return contents.get(path);
	}
	
	public void setContent(String content) {
		contents.put(path, content);
	}
	
	@Override
	public boolean isReadable() {
		return true;
	}
	
	@Override
	public boolean isWritable() {
		return true;
	}
	
	@Override
	public void close() {
		if(reader!=null) try {
			reader.close();
		} catch(IOException e) {
			// simply ignore
		} finally {
			reader = null;
		}
	}
	
	@Override
	public String readFully() {
		return getContent();
	}
	
	@Override
	public void writeFully(String data) {
		setContent(data);
	}
	
	@Override
	public String readLine() throws IOException {
		if(reader==null) {
			if(getContent()==null)
				return null;
			reader = new BufferedReader(new StringReader(getContent()));
		}
		return reader.readLine();
	}
	
	@Override
	public void writeLine(String data) throws IOException {
		String content = contents.getOrDefault(path, "");
		if(content.length()>0)
			content += "\n";
		setContent(content + data);
	}
}
