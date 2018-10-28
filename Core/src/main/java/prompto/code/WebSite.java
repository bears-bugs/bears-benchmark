package prompto.code;

import java.util.List;

import prompto.error.PromptoError;
import prompto.runtime.Context;
import prompto.store.IStorable;
import prompto.store.IStore;

public class WebSite extends Service {

	private String homePage;

	public String getHomePage() {
		return homePage;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}

	@Override
	public ModuleType getType() {
		return ModuleType.WEBSITE;
	}
	
	@Override
	public IStorable toStorables(Context context, IStore store, List<IStorable> storables) throws PromptoError {
		IStorable storable = super.toStorables(context, store, storables);
		storable.setData("homePage", homePage);
		return storable;
	}

}
