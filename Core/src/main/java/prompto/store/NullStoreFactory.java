package prompto.store;

import prompto.config.IStoreConfiguration;

public class NullStoreFactory implements IStoreFactory {

	@Override
	public IStore newStore(IStoreConfiguration config) throws Exception {
		return null;
	}

}
