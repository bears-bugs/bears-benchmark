package prompto.internet;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import prompto.value.IResource;

public class Url implements IResource {
	
	Object dbId;
	URL url;
	BufferedReader reader;
	String encoding = "utf-8";
	
	public void setDbId(Object dbId) {
		this.dbId = dbId;
	}
	
	public Object getDbId() {
		return dbId;
	}
	
	public void setPath(String path) throws MalformedURLException {
		url = new URL(path);
	}
	
	public String getPath() {
		return url!=null ? url.toExternalForm() : "";
	}
	
	public void setEncoding(String encoding) {
		if(encoding!=null)
			this.encoding = encoding;
	}
	
	public String getEncoding() {
		return encoding;
	}
	
	@Override
	public boolean isReadable() {
		return url!=null;
	}
	
	@Override
	public boolean isWritable() {
		return url!=null;
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
	public String readFully() throws IOException {
		try( InputStream input = url.openStream() ) {
			return readFully(input);
		}
	}
	
	private String readFully(InputStream input) throws IOException {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		for(;;) {
			int read = input.read(buffer);
			if(read==-1)
				break;
			data.write(buffer, 0, read);
		}
		return data.toString(encoding);
	}

	@Override
	public void writeFully(String data) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String readLine() throws IOException {
		if(reader==null)
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
		return reader.readLine();
	}
	
	@Override
	public void writeLine(String data) throws IOException {
		throw new UnsupportedOperationException();
	}
	
}
