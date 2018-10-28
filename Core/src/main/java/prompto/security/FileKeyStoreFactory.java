package prompto.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

import prompto.config.IConfigurationReader;
import prompto.config.IKeyStoreFactoryConfiguration;
import prompto.utils.Logger;

public class FileKeyStoreFactory implements IKeyStoreFactory {

	static final Logger logger = new Logger();

	@Override
	public IKeyStoreFactoryConfiguration newConfiguration(IConfigurationReader reader) {
		return new Configuration(reader);
	}

	@Override
	public KeyStore newInstance(IKeyStoreFactoryConfiguration config) {
		String filePath = ((Configuration)config).getFile();
		if(filePath==null)
			return null;
		File file = new File(filePath);
		logger.info(()->"Loading certificate store: " + file.getAbsolutePath());
		try(InputStream input = new FileInputStream(filePath)) {
			KeyStore ks = KeyStore.getInstance("JKS");
			ks.load(input, null);
			return ks;
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	static class Configuration implements IKeyStoreFactoryConfiguration {

		IConfigurationReader reader;
		
		public Configuration(IConfigurationReader reader) {
			this.reader = reader;
		}

		public String getFile() {
			return reader.getString("file");
		}

		@Override
		public IKeyStoreFactory getKeyStoreFactory() {
			return new FileKeyStoreFactory();
		}
		
	}

}
