package prompto.code;

import prompto.grammar.Identifier;
import prompto.type.CategoryType;

public enum ModuleType {
	THESAURUS(Thesaurus.class), // storable attributes and categories
	LIBRARY(Library.class), // reusable classes and methods
	WEBLIBRARY(WebLibrary.class), // reusable widgets
	BATCH(Batch.class), // which can be scheduled
	SCRIPT(Script.class), // which has no entry point
	SERVICE(Service.class), // back end only web service (must be hosted)
	WEBSITE(WebSite.class); // full fledged web app (must be hosted)
	
	Class<? extends Module> moduleClass;
	
	ModuleType(Class<? extends Module> klass) {
		moduleClass = klass;
	}
	
	public Class<? extends Module> getModuleClass() {
		return moduleClass;
	}
	
	public CategoryType getCategory() {
		return new CategoryType( new Identifier(moduleClass.getSimpleName()));
	}
}