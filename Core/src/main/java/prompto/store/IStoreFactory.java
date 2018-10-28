package prompto.store;

import prompto.config.IConfigurationReader;
import prompto.config.IStoreConfiguration;
import prompto.config.StoreConfiguration;
import prompto.store.memory.MemStore;

public interface IStoreFactory {
	static IStore newStoreFromConfig(IStoreConfiguration cfg) throws Throwable {
		if(cfg==null)
			return new MemStore();
		IStoreFactory factory = newStoreFactory(cfg.getFactory());
		return factory.newStore(cfg);
	}
	static IStoreFactory newStoreFactory(String factoryName) throws Throwable {
		Class<?> klass = Class.forName(factoryName, true, Thread.currentThread().getContextClassLoader());
		if(!(IStoreFactory.class.isAssignableFrom(klass)))
			throw new RuntimeException("Not a store factory: " + factoryName);
		return (IStoreFactory)klass.newInstance();
	}
	IStore newStore(IStoreConfiguration config) throws Exception;
	default IStoreConfiguration newConfiguration(IConfigurationReader reader) {
		return new StoreConfiguration(reader);
	}
}
