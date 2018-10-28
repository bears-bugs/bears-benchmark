package prompto.code;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import prompto.store.IStorable;
import prompto.store.IStore;

public class URLResource extends Resource {
	
	URL url;
	
	
	public URLResource(URL url) {
		this.url = url;
	}
	
	@Override
	public IStorable toStorable(IStore store) {
		throw new UnsupportedOperationException("Should never get there!");
	}
	
	@Override
	public long length() {
		throw new UnsupportedOperationException("Should never get there!");
	}
	
	@Override
	public InputStream getInputStream() throws IOException {
		return url.openStream();
	}

}
