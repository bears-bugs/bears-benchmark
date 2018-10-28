package prompto.store;

import prompto.store.memory.MemStore;
import prompto.utils.ISingleton;

public abstract class DataStore {

	static ISingleton<IStore> globalInstance = new ISingleton<IStore>() {
		IStore instance = new MemStore();
		@Override public void set(IStore instance) { this.instance = instance; }
		@Override public IStore get() { return instance; }
	};
	
	static ThreadLocal<IStore> threadInstance = ThreadLocal.withInitial(()->globalInstance.get());
	
	public static void setGlobal(IStore store) {
		globalInstance.set(store);
	}
	
	public static void setInstance(IStore store) {
		threadInstance.set(store);
	}

	public static IStore getInstance() {
		return threadInstance.get();
	}

}
