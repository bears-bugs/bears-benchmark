package prompto.code;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.StreamSupport;

import prompto.declaration.AttributeDeclaration;
import prompto.declaration.DeclarationList;
import prompto.declaration.IDeclaration;
import prompto.error.PromptoError;
import prompto.intrinsic.PromptoVersion;
import prompto.parser.AbstractParser;
import prompto.parser.Dialect;
import prompto.parser.ECleverParser;
import prompto.parser.ISection;
import prompto.parser.MCleverParser;
import prompto.parser.OCleverParser;
import prompto.runtime.Context;
import prompto.store.AttributeInfo;
import prompto.utils.ISingleton;

/* a code store is a place where a code consumer (interpreter, compiler...) can fetch code from */
public interface ICodeStore {
	
	static ISingleton<ICodeStore> instance = new ISingleton<ICodeStore>() {
		ICodeStore instance = null;
		@Override public void set(ICodeStore instance) { this.instance = instance; }
		@Override public ICodeStore get() { return instance; }
	};
	
	static void setInstance(ICodeStore store) {
		instance.set(store);
	}

	public static ICodeStore getInstance() {
		return instance.get();
	}
	
	public static DeclarationList parse(String sourceName, InputStream data) throws Exception {
		Dialect dialect = dialectFromResourceName(sourceName);
		return parse(dialect, sourceName, data);
	}
	
	public static DeclarationList parse(Dialect dialect, String sourceName, InputStream data) throws Exception {
		AbstractParser parser = parserForDialect(dialect, sourceName, data);
		return parser.parse_declaration_list();
	}

	static AbstractParser parserForDialect(Dialect dialect, String path, InputStream data) throws IOException {
		switch(dialect) {
		case E:
			return new ECleverParser(path, data);
		case O:
			return new OCleverParser(path, data);
		case M:
			return new MCleverParser(path, data);
		default:
			throw new RuntimeException("Unsupported extension: " + path);
		}
	}

	static Set<String> CODE_EXTENSIONS = new HashSet<>(Arrays.asList("pec", "poc", "pmc"));

	static Dialect dialectFromResourceName(String path) {
		int startExtension = path.lastIndexOf(".");
		String extension = path.substring(startExtension + 1).toLowerCase();
		if(!CODE_EXTENSIONS.contains(extension))
			return null;
		String s = "" + extension.charAt(1);
		return Dialect.valueOf(s.toUpperCase());
	}



	ModuleType getModuleType();
	Dialect getModuleDialect();
	String getModuleName();
	PromptoVersion getModuleVersion();

	void storeDeclarations(Iterable<IDeclaration> declarations, Dialect dialect, PromptoVersion version, Object moduleId) throws PromptoError;

	default Iterable<IDeclaration> fetchLatestDeclarations(String name) throws PromptoError {
		return fetchSpecificDeclarations(name, PromptoVersion.LATEST);
	}
	
	Iterable<IDeclaration> fetchSpecificDeclarations(String name, PromptoVersion version) throws PromptoError;

	default IDeclaration fetchLatestSymbol(String name) throws PromptoError {
		return fetchSpecificSymbol(name, PromptoVersion.LATEST);
	}

	IDeclaration fetchSpecificSymbol(String name, PromptoVersion version) throws PromptoError;

	default public Batch fetchBatch(String name, PromptoVersion version) throws PromptoError {
		return fetchModule(ModuleType.BATCH, name, version);
	}
	
	default public WebSite fetchApplication(String name, PromptoVersion version) throws PromptoError {
		return fetchModule(ModuleType.WEBSITE, name, version);
	}
	
	default public Script fetchScript(String name, PromptoVersion version) throws PromptoError {
		return fetchModule(ModuleType.SCRIPT, name, version);
	}
	
	default public Service fetchService(String name, PromptoVersion version) throws PromptoError {
		return fetchModule(ModuleType.SERVICE, name, version);
	}

	default public Library fetchLibrary(String name, PromptoVersion version) throws PromptoError {
		return fetchModule(ModuleType.LIBRARY, name, version);
	}
	
	default public Script fetchThesaurus(PromptoVersion version) throws PromptoError {
		return fetchModule(ModuleType.THESAURUS, ModuleType.THESAURUS.name(), version);
	}
	
	<T extends Module> T fetchModule(ModuleType type, String name, PromptoVersion version) throws PromptoError;
	void storeModule(Module module) throws PromptoError;

	void collectStorableAttributes(Map<String, AttributeDeclaration> columns) throws PromptoError;

	ISection findSection(ISection section);

    Collection<String> fetchDeclarationNames();

	void storeResource(Resource resource, Object moduleId);

	default public Resource fetchLatestResource(String path) throws PromptoError {
		return fetchSpecificResource(path, PromptoVersion.LATEST);
	}

	Resource fetchSpecificResource(String path, PromptoVersion version);
	
	default public AttributeInfo fetchAttributeInfo(Context context, String name) {
		if("category".equals(name))
			return AttributeInfo.CATEGORY;
		else {
			Iterable<IDeclaration> decls = fetchLatestDeclarations(name);
			if(decls==null)
				return null;
			else return StreamSupport.stream(decls.spliterator(), false)
					.filter(d->d instanceof AttributeDeclaration)
					.map(d->(AttributeDeclaration)d)
					.map(d->d.getAttributeInfo(context))
					.findFirst()
					.orElse(null);
		}
	}



}
