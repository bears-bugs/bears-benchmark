package prompto.code;

import java.util.List;

import prompto.error.PromptoError;
import prompto.runtime.Context;
import prompto.store.IStorable;
import prompto.store.IStore;
import prompto.store.IStored;

public class Service extends Module {

	private String serverAboutToStartMethod;

	public String getServerAboutToStartMethod() {
		return serverAboutToStartMethod;
	}

	public void setServerAboutToStartMethod(String serverAboutToStartMethod) {
		this.serverAboutToStartMethod = serverAboutToStartMethod;
	}

	@Override
	public ModuleType getType() {
		return ModuleType.SERVICE;
	}
	
	@Override
	public IStorable toStorables(Context context, IStore store, List<IStorable> storables) throws PromptoError {
		IStorable storable = super.toStorables(context, store, storables);
		storable.setData("serverAboutToStartMethod", serverAboutToStartMethod);
		return storable;
	}
	
	@Override
	public void fromStored(IStored stored) {
		super.fromStored(stored);
		setServerAboutToStartMethod((String)stored.getData("serverAboutToStartMethod"));
	}

}
