package prompto.security;

import java.security.KeyStore;

import prompto.config.IConfigurationReader;
import prompto.config.IKeyStoreFactoryConfiguration;


public interface IKeyStoreFactory {

	IKeyStoreFactoryConfiguration newConfiguration(IConfigurationReader child);
	KeyStore newInstance(IKeyStoreFactoryConfiguration config);


}
