package prompto.code;

import java.util.List;

import prompto.error.PromptoError;
import prompto.runtime.Context;
import prompto.store.IStorable;
import prompto.store.IStore;
import prompto.store.IStored;

public class WebLibrary extends Library {

	String widgetLibrary;
	String htmlEngine;
	String uiFramework;
	
	@Override
	public ModuleType getType() {
		return ModuleType.WEBLIBRARY;
	}

	public String getWidgetLibrary() {
		return widgetLibrary;
	}

	public void setWidgetLibrary(String widgetLibrary) {
		this.widgetLibrary = widgetLibrary;
	}

	public String getHtmlEngine() {
		return htmlEngine;
	}

	public void setHtmlEngine(String htmlEngine) {
		this.htmlEngine = htmlEngine;
	}

	public String getUIFramework() {
		return uiFramework;
	}
	
	public void setUIFramework(String uiFramework) {
		this.uiFramework = uiFramework;
	}

	@Override
	public IStorable toStorables(Context context, IStore store, List<IStorable> storables) throws PromptoError {
		IStorable storable = super.toStorables(context, store, storables);
		storable.setData("widgetLibrary", widgetLibrary);
		storable.setData("htmlEngine", htmlEngine);
		storable.setData("uiFramework", uiFramework);
		return storable;
	}

	@Override
	public void fromStored(IStored stored) {
		super.fromStored(stored);
		setWidgetLibrary((String)stored.getData("widgetLibrary"));
		setHtmlEngine((String)stored.getData("htmlEngine"));
		setUIFramework((String)stored.getData("uiFramework"));
	}

}

