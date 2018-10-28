package prompto.store.memory;

import prompto.config.IStoreConfiguration;
import prompto.store.IStoreFactory;

public class MemStoreFactory implements IStoreFactory {

	@Override
	public MemStore newStore(IStoreConfiguration config) {
		return new MemStore();
	}

}
