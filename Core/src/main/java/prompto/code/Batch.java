package prompto.code;

import java.util.List;

import prompto.error.PromptoError;
import prompto.runtime.Context;
import prompto.store.IStorable;
import prompto.store.IStore;
import prompto.store.IStored;

public class Batch extends Module implements IApplication {

	private String startMethod;

	public String getStartMethod() {
		return startMethod;
	}

	public void setStartMethod(String startMethod) {
		this.startMethod = startMethod;
	}

	@Override
	public ModuleType getType() {
		return ModuleType.BATCH;
	}
	
	@Override
	public IStorable toStorables(Context context, IStore store, List<IStorable> storables) throws PromptoError {
		IStorable storable = super.toStorables(context, store, storables);
		storable.setData("startMethod", startMethod);
		return storable;
	}
	
	@Override
	public void fromStored(IStored stored) {
		super.fromStored(stored);
		setStartMethod((String)stored.getData("startMethod"));
	}

}
