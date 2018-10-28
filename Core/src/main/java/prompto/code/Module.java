package prompto.code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import prompto.error.PromptoError;
import prompto.intrinsic.PromptoBinary;
import prompto.intrinsic.PromptoVersion;
import prompto.runtime.Context;
import prompto.store.IStorable;
import prompto.store.IStore;
import prompto.store.IStored;

public abstract class Module {
	
	private Object dbId;
	private String name;
	private PromptoVersion version;
	private String description;
	private PromptoBinary image;
	private List<Dependency> dependencies;
	
	public abstract ModuleType getType();

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
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public PromptoBinary getImage() {
		return image;
	}
	
	public void setImage(PromptoBinary image) {
		this.image = image;
	}

	public IStorable toStorables(Context context, IStore store, List<IStorable> storables) throws PromptoError {
		IStorable storable = store.newStorable(getCategories(), null); 
		storables.add(storable);
		setDbId(storable.getOrCreateDbId());
		storable.setData("name", name);
		storable.setData("version", version);
		if(description!=null)
			storable.setData("description", description);
		if(image!=null)
			storable.setData("image", image);
		if(dependencies!=null) {
			List<Object> dbIds = dependencies.stream()
					.map((d)->
						d.populate(context, store, storables)
						.getOrCreateDbId())
					.collect(Collectors.toList());
			storable.setData("dependencies", dbIds);
		}
		return storable;
	}
	
	public void fromStored(IStored stored) {
		setDbId(stored.getDbId());
		setName((String)stored.getData("name"));
		setVersion((PromptoVersion)stored.getData("version"));
		setDescription((String)stored.getData("description"));
	}

	private List<String> getCategories() {
		List<String> categories = new ArrayList<>();
		Class<?> klass = this.getClass();
		while(Module.class.isAssignableFrom(klass)) {
			categories.add(klass.getSimpleName());
			if(IApplication.class.isAssignableFrom(klass))
				categories.add("Application");
			klass = klass.getSuperclass();
		}
		Collections.reverse(categories);
		return categories;
	}

	public void setDependencies(List<Dependency> dependencies) {
		this.dependencies = dependencies;
	}
	
	public List<Dependency> getDependencies() {
		return dependencies;
	}


}
