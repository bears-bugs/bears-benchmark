package prompto.code;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import prompto.declaration.AttributeDeclaration;
import prompto.declaration.DeclarationList;
import prompto.declaration.IDeclaration;
import prompto.declaration.IEnumeratedDeclaration;
import prompto.error.InvalidResourceError;
import prompto.error.PromptoError;
import prompto.error.ReadWriteError;
import prompto.intrinsic.PromptoVersion;
import prompto.parser.Dialect;
import prompto.parser.ISection;
import prompto.utils.Logger;
import prompto.utils.SectionLocator;

/* resource/file based code store used to bootstrap modules  */
public class ImmutableCodeStore extends BaseCodeStore {

	static final Logger logger = new Logger();

	public static ICodeStore bootstrapRuntime(Supplier<Collection<URL>> runtimeSupplier) {
		logger.info(()->"Connecting to prompto runtime libraries...");
		try {
			ICodeStore runtime = null;
			if(runtimeSupplier!=null) for(URL resource : runtimeSupplier.get()) {
				logger.info(()->"Connecting to library: " + resource.toExternalForm());
				runtime = new ImmutableCodeStore(runtime, ModuleType.LIBRARY, resource, PromptoVersion.parse("1.0.0"));
			}
			return runtime;
		} catch(RuntimeException e) {
			throw new ReadWriteError(e.getMessage());
		}
	}


	
	ModuleType type;
	URL resource;
	PromptoVersion version;
	Map<String, List<IDeclaration>> declarations = null;
	
	public ImmutableCodeStore(ICodeStore next, ModuleType type, URL resource, PromptoVersion version) {
		super(next);
		if(resource==null)
			throw new NullPointerException();
		this.type = type;
		this.resource = resource;
		this.version = version;
	}
	
	@Override
	public String toString() {
		return this.resource.toString();
	}

	@Override
	public ModuleType getModuleType() {
		return type;
	}
	
	@Override
	public Dialect getModuleDialect() {
		String external = resource.toExternalForm();
		char c = external.charAt(external.length()-2);
		return Dialect.valueOf(String.valueOf(c).toUpperCase());
	}
	
	@Override
	public String getModuleName() {
		String moduleName = resource.toExternalForm();
		int idx = moduleName.lastIndexOf('/');
		if(idx>=0)
			moduleName = moduleName.substring(idx+1);
		idx = moduleName.indexOf('.');
		if(idx>=0)
			moduleName = moduleName.substring(0, idx);
		return moduleName;
	}
	
	@Override
	public PromptoVersion getModuleVersion() {
		return version;
	}
	
	@Override
	public void storeModule(Module module) throws PromptoError {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public <T extends Module> T fetchModule(ModuleType type, String name, PromptoVersion version) throws PromptoError {
		return null;
	}
	
	@Override
	public Resource fetchSpecificResource(String path, PromptoVersion version) {
		if(this.resource.toString().endsWith(path))
			return new URLResource(this.resource);
		else
			return null;
	}
	
	@Override
	public void storeDeclarations(Iterable<IDeclaration> declarations, Dialect dialect, PromptoVersion version, Object moduleId) throws PromptoError {
		throw new UnsupportedOperationException();
	}
	
	
	@Override
	public void storeResource(Resource resource, Object moduleId) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Iterable<IDeclaration> fetchLatestDeclarations(String name) throws PromptoError {
		Iterable<IDeclaration> decls = fetchInResource(name);
		if(decls!=null)
			return decls;
		else
			return super.fetchLatestDeclarations(name);
	}
	
	@Override
	public Iterable<IDeclaration> fetchSpecificDeclarations(String name,PromptoVersion version) throws PromptoError {
		Iterable<IDeclaration> decls = fetchInResource(name);
		if(decls!=null)
			return decls;
		else
			return super.fetchSpecificDeclarations(name, version);
	}
	
	@Override
	public IDeclaration fetchLatestSymbol(String name) throws PromptoError {
		IDeclaration decl = fetchSymbolInResource(name);
		if(decl!=null)
			return decl;
		else
			return super.fetchLatestSymbol(name);
	}
	
	@Override
	public IDeclaration fetchSpecificSymbol(String name, PromptoVersion version) throws PromptoError {
		IDeclaration decl = fetchSymbolInResource(name);
		if(decl!=null)
			return decl;
		else
			return super.fetchSpecificSymbol(name, version);
	}
	
	private IDeclaration fetchSymbolInResource(String name) {
		loadResource();
		return declarations.values().stream()
				.flatMap(Collection::stream)
				.filter((d)->d instanceof IEnumeratedDeclaration)
				.map((d)->(IEnumeratedDeclaration<?>)d)
				.filter((d)->d.hasSymbol(name))
				.findFirst()
				.orElse(null);
	}

	@Override
	public ISection findSection(ISection section) {
		ISection result = fetchInResource(section);
		if(result!=null)
			return result;
		else
			return super.findSection(section);
	}
	


	private ISection fetchInResource(ISection section) {
		if(!resource.toExternalForm().equals(section.getFilePath()))
			return null;
		loadResource();
		return SectionLocator.findSectionInLists(declarations.values(), section);
				
	}

	private Iterable<IDeclaration> fetchInResource(String name) throws PromptoError {
		loadResource();
		List<IDeclaration> decls = declarations.get(name);
		return decls==null ? null : decls;
	}

	private void loadResource() throws PromptoError {
		try {
			if(declarations==null)
				tryLoadResource();
			if(declarations==null)
				throw new InvalidResourceError(resource.toExternalForm());
		} catch(PromptoError error) {
			throw error;
		}
	}

	private void tryLoadResource() {
		try(InputStream input = resource.openStream()) {
			if(input!=null)
				parseResource(input);
		} catch(PromptoError error) {
			throw error;
		} catch(Exception e) {
			throw new InternalError(e);
		}
	}

	private void parseResource(InputStream input) throws Exception {
		String path = resource.toExternalForm();
		Dialect dialect = ICodeStore.dialectFromResourceName(path);
		if(dialect!=null) {
			DeclarationList decls = ICodeStore.parse(resource.toExternalForm(), input);
			declarations = new HashMap<String, List<IDeclaration>>();
			for(IDeclaration decl : decls) {
				decl.setOrigin(this);
				String name = decl.getId().toString();
				if(declarations.get(name)==null)
					declarations.put(name, new ArrayList<>());
				declarations.get(name).add(decl);
			}
		} else
			declarations = new HashMap<String, List<IDeclaration>>();
	}
	
	public Iterable<IDeclaration> getDeclarations() {
		loadResource();
		return ()->new Iterator<IDeclaration>() {
			
			Iterator<List<IDeclaration>> main = declarations.values().iterator();
			Iterator<IDeclaration> child = null;
			
			@Override 
			public boolean hasNext() {
				while(true) {
					if(child!=null) {
						if(child.hasNext())
							return true;
						else
							child = null;
					}
					if(child==null) {
						if(!main.hasNext())
							return false;
						else
							child = main.next().iterator();
					}
				}
			}
			
			@Override
			public IDeclaration next() {
				return child.next();
			}
		};
	}
	
	@Override
	public void collectStorableAttributes(Map<String, AttributeDeclaration> columns) throws PromptoError {
		super.collectStorableAttributes(columns);
		loadResource();
		declarations.values().forEach( (decls) -> {
			decls.stream().filter( (decl) -> decl instanceof AttributeDeclaration)
			.filter( (decl) -> ((AttributeDeclaration)decl).isStorable())
			.forEach( (decl) -> columns.put(decl.getName(), (AttributeDeclaration)decl));
		});
	}
	
	@Override
	public Collection<String> fetchDeclarationNames() {
		loadResource();
		if(next==null)
			return declarations.keySet();
		else {
			Set<String> names = new HashSet<>();
			names.addAll(declarations.keySet());
			names.addAll(next.fetchDeclarationNames());
			return names;
		}
	}
}
