package prompto.code;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import prompto.declaration.AttributeDeclaration;
import prompto.declaration.IDeclaration;
import prompto.error.PromptoError;
import prompto.grammar.Identifier;
import prompto.intrinsic.PromptoVersion;
import prompto.runtime.Context;
import prompto.store.AttributeInfo;
import prompto.store.IStore;
import prompto.type.BlobType;
import prompto.type.BooleanType;
import prompto.type.DateTimeType;
import prompto.type.IType;
import prompto.type.IntegerType;
import prompto.type.ListType;
import prompto.type.TextType;
import prompto.utils.IdentifierList;
import prompto.utils.Logger;
import prompto.utils.TypeUtils;

// use a dedicated bootstrapper to ensure app and code store contexts do not spill
public class CodeStoreBootstrapper {

	static final Logger logger = new Logger();
	
	public static Context bootstrap(IStore store, ICodeStore runtime) throws PromptoError {
		logger.info(()->"Initializing code store...");
		CodeStoreBootstrapper bs = new CodeStoreBootstrapper(store, runtime);
		bs.bootstrap();
		return bs.context;
	}
	
	Context context = Context.newGlobalContext();
	ICodeStore next;
	IStore store;
	
	private CodeStoreBootstrapper(IStore store, ICodeStore runtime) {
		this.store = store;
		URL url = Thread.currentThread().getContextClassLoader().getResource("libraries/CodeStore.pec");
		this.next = new ImmutableCodeStore(runtime, ModuleType.LIBRARY, url, PromptoVersion.parse("1.0.0"));
	}

	private void bootstrap() throws PromptoError {
		Map<String, AttributeDeclaration> columns = getMinimalColumns(store);
		columns = fetchLatestDeclarations(columns);
		registerColumnAttributes(columns.values());
		if(store!=null) {
			List<AttributeInfo> infos = columns.values().stream().map((c)->c.getAttributeInfo(context)).collect(Collectors.toList());
			store.createOrUpdateAttributes(infos);
		}
	}

	private void registerColumnAttributes(Collection<AttributeDeclaration> columns) throws PromptoError {
		for(AttributeDeclaration column : columns)
			column.register(context);
	}

	private Map<String, AttributeDeclaration> fetchLatestDeclarations(Map<String, AttributeDeclaration> decls) throws PromptoError {
		try {
			Map<String, AttributeDeclaration> latest = new HashMap<>();
			for(Map.Entry<String, AttributeDeclaration> entry : decls.entrySet())
				latest.put(entry.getKey(), fetchLatestDeclaration(entry.getValue()));
			return latest;
		} catch (RuntimeException e) {
			if(e.getCause() instanceof PromptoError)
				throw (PromptoError)e.getCause();
			else
				throw e;
		}
	}

	static final Set<String> reserved = new HashSet<>(Arrays.asList(IStore.dbIdName, "category", "storable", "module"));
	
	private AttributeDeclaration fetchLatestDeclaration(AttributeDeclaration column) {
		try {
			// can't write a declaration for a column with a reserved name, so use the hard coded one
			if(reserved.contains(column.getName()))
				return column;
			Iterable<IDeclaration> decls = next.fetchLatestDeclarations(column.getName());
			if(decls==null || !decls.iterator().hasNext())
				throw new RuntimeException("Invalid column attribute: " + column.getName());
			IDeclaration decl = decls.iterator().next(); // can only get one attribute
			if(!(decl instanceof AttributeDeclaration))
				throw new RuntimeException("Invalid column attribute: " + column.getName());
			return (AttributeDeclaration)decl;
		} catch (PromptoError e) {
			throw new RuntimeException(e);
		}
	}

	private Map<String, AttributeDeclaration> getMinimalColumns(IStore store) {
		IType dbIdIType = store==null ? IntegerType.instance() : TypeUtils.typeToIType(store.getDbIdClass());
		Map<String, AttributeDeclaration> columns = new HashMap<>();
		// attributes with reserved names, the below declarations will be used
		columns.put(IStore.dbIdName, new AttributeDeclaration(new Identifier(IStore.dbIdName), dbIdIType));
		columns.put("storable", new AttributeDeclaration(new Identifier("storable"), BooleanType.instance()));
		columns.put("category", new AttributeDeclaration(new Identifier("category"), 
				new ListType(TextType.instance()), new IdentifierList(new Identifier("key"))));
		// also add 'module' to avoid dependency on DevCenter
		columns.put("module", new AttributeDeclaration(new Identifier("module"), dbIdIType));
		// more required attributes which will be overridden by a prompto declaration
		columns.put("author", new AttributeDeclaration(new Identifier("author"), TextType.instance()));
		columns.put("timeStamp", new AttributeDeclaration(new Identifier("timeStamp"), DateTimeType.instance()));
		columns.put("name", new AttributeDeclaration(new Identifier("name"), TextType.instance()));
		columns.put("description", new AttributeDeclaration(new Identifier("description"), TextType.instance()));
		columns.put("version", new AttributeDeclaration(new Identifier("version"), TextType.instance())); // TODO add VersionType ?
		columns.put("prototype", new AttributeDeclaration(new Identifier("prototype"), TextType.instance()));
		columns.put("dialect", new AttributeDeclaration(new Identifier("dialect"), TextType.instance()));
		columns.put("body", new AttributeDeclaration(new Identifier("body"), TextType.instance()));
		columns.put("data", new AttributeDeclaration(new Identifier("data"), BlobType.instance()));
		columns.put("mimeType", new AttributeDeclaration(new Identifier("mimeType"), TextType.instance()));
		return columns;
	}


}
