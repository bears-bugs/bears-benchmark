package prompto.code;

import java.util.Arrays;
import java.util.List;

import prompto.intrinsic.PromptoVersion;
import prompto.runtime.Context;
import prompto.store.IStorable;
import prompto.store.IStore;

public class Dependency {
	
	private Object dbId;
	private String name;
	private PromptoVersion version;

	public Object getDbId() {
		return dbId;
	}
	
	public void setDbId(Object dbId) {
		this.dbId = dbId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public PromptoVersion getVersion() {
		return version;
	}
	
	public void setVersion(PromptoVersion version) {
		this.version = version;
	}

	public IStorable populate(Context context, IStore store, List<IStorable> storables) {
		List<String> categories = Arrays.asList("Dependency");
		IStorable storable = store.newStorable(categories, null); 
		storables.add(storable);
		setDbId(storable.getOrCreateDbId());
		storable.setData("name", name);
		storable.setData("version", version);
		return storable;
	}
}
