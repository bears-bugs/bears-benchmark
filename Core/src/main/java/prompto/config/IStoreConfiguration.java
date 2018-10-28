package prompto.config;

import java.util.function.Supplier;

import prompto.store.NullStoreFactory;
import prompto.store.memory.MemStoreFactory;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.document.YamlMapping;

public interface IStoreConfiguration {

	String getFactory();
	String getHost();
	Integer getPort();
	String getDbName();
	String getUser();
	ISecretKeyConfiguration getSecretKeyConfiguration();
	IStoreConfiguration withDbName(String dbName);
	
	default YamlMapping toYaml() throws YamlException {
		YamlMapping mapping = new YamlMapping();
		mapping.setEntry("factory", getFactory());
		mapping.setEntry("host", getHost());
		mapping.setEntry("port", getPort());
		mapping.setEntry("dbName", getDbName());
		mapping.setEntry("user", getUser());
		mapping.setEntry("secretKey", getSecretKeyConfiguration().toYaml());
		return mapping;
	}

	IStoreConfiguration NULL_STORE_CONFIG = new IStoreConfiguration() {
		@Override public String getFactory() { return NullStoreFactory.class.getName(); }
		@Override public String getHost() { return null; }
		@Override public Integer getPort() { return null; }
		@Override public String getDbName() { return null; }
		@Override public String getUser() { return null; }
		@Override public ISecretKeyConfiguration getSecretKeyConfiguration() { return null; }
		@Override public IStoreConfiguration withDbName(String dbName) { return this; }
	};
	
	IStoreConfiguration MEM_STORE_CONFIG = new IStoreConfiguration() {
		@Override public String getFactory() { return MemStoreFactory.class.getName(); }
		@Override public String getHost() { return null; }
		@Override public Integer getPort() { return null; }
		@Override public String getDbName() { return null; }
		@Override public String getUser() { return null; }
		@Override public ISecretKeyConfiguration getSecretKeyConfiguration() { return null; }
		@Override public IStoreConfiguration withDbName(String dbName) { return this; }
	};

	public class Inline implements IStoreConfiguration {

		Supplier<String> factory = ()->null;
		Supplier<String> host = ()->null;
		protected Supplier<Integer> port = ()->null;
		Supplier<String> dbName = ()->null;
		Supplier<String> user = ()->null;
		Supplier<ISecretKeyConfiguration> secretKey = ()->null;
		
		@Override public String getFactory() { return factory.get(); }
		@Override public String getHost() { return host.get(); }
		@Override public Integer getPort() { return port.get(); }
		@Override public String getDbName() { return dbName.get(); }
		@Override public String getUser() { return user.get(); }
		@Override public ISecretKeyConfiguration getSecretKeyConfiguration() { return secretKey.get(); }
		@Override public IStoreConfiguration withDbName(String dbName) {
			this.dbName = ()->dbName;
			return this;
		}
		
	}

}
