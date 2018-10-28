package prompto.code;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import prompto.declaration.AttributeDeclaration;
import prompto.declaration.DeclarationList;
import prompto.declaration.IDeclaration;
import prompto.declaration.IDeclaration.DeclarationType;
import prompto.declaration.IEnumeratedDeclaration;
import prompto.declaration.IMethodDeclaration;
import prompto.error.PromptoError;
import prompto.expression.AndExpression;
import prompto.expression.EqualsExpression;
import prompto.expression.IExpression;
import prompto.expression.IPredicateExpression;
import prompto.expression.Symbol;
import prompto.expression.UnresolvedIdentifier;
import prompto.grammar.EqOp;
import prompto.grammar.Identifier;
import prompto.grammar.OrderByClause;
import prompto.grammar.OrderByClauseList;
import prompto.intrinsic.PromptoBinary;
import prompto.intrinsic.PromptoVersion;
import prompto.literal.TextLiteral;
import prompto.literal.VersionLiteral;
import prompto.parser.Dialect;
import prompto.runtime.Context;
import prompto.runtime.Context.MethodDeclarationMap;
import prompto.store.AttributeInfo;
import prompto.store.IQueryBuilder;
import prompto.store.IQueryBuilder.MatchOp;
import prompto.store.IStorable;
import prompto.store.IStore;
import prompto.store.IStored;
import prompto.store.IStoredIterable;
import prompto.type.CategoryType;
import prompto.utils.CodeWriter;
import prompto.utils.IdentifierList;
import prompto.utils.Logger;
import prompto.utils.StringUtils;


public class QueryableCodeStore extends BaseCodeStore {

	static final Logger logger = new Logger();
	
	IStore store; // data store where to store/fetch the code
	String application;
	PromptoVersion version;
	// fetching and storing declarations requires a context holding code store attributes
	// some of these are code store specific and should not be looked for in the app context
	Context context; 
	// storing resource code is optional
	boolean storeExternals = false;
	
	public QueryableCodeStore(IStore store, ICodeStore runtime, String application, PromptoVersion version, URL[] addOns, URL ...resourceNames) throws PromptoError {
		super(null);
		this.store = store;
		this.application = application;
		this.version = version;
		this.context = CodeStoreBootstrapper.bootstrap(store, runtime);
		this.next = AppStoreBootstrapper.bootstrap(store, runtime, application, version, addOns, resourceNames);
	}
	
	public IStore getStore() {
		return store;
	}
	
	public void setStore(IStore store) {
		this.store = store;
	}
	
	public void collectStorables(List<IStorable> list, IDeclaration declaration, Dialect dialect, PromptoVersion version, Object moduleId) {
		if(declaration instanceof MethodDeclarationMap) {
			for(IDeclaration method : ((MethodDeclarationMap)declaration).values())
				collectStorables(list, method, dialect, version, moduleId);
		} else {
			String typeName = StringUtils.capitalizeFirst(declaration.getDeclarationType().name()) + "Declaration";
			List<String> categories = Arrays.asList("Stuff", "Declaration", typeName);
			IStorable storable = populateDeclarationStorable(categories, declaration, dialect, version, moduleId);
			list.add(storable);
		}
	}

	@Override
	public ModuleType getModuleType() {
		return ModuleType.WEBSITE;
	}
	
	@Override
	public Dialect getModuleDialect() {
		return null;
	}
	
	@Override
	public String getModuleName() {
		return application;
	}
	
	@Override
	public PromptoVersion getModuleVersion() {
		return version;
	}
	
	@Override
	public void storeResource(Resource resource, Object moduleId) {
		IStorable storable = resource.toStorable(store);
		if(moduleId!=null)
			storable.setData("module", moduleId);
		store.store(null, Collections.singletonList(storable));
	}
	
	@Override
	public void storeModule(Module module) throws PromptoError {
		Context context = Context.newGlobalContext();
		List<IStorable> storables = new ArrayList<>();
		module.toStorables(context, store, storables);
		store.store(null, storables);
	}
	
	private Object storeDeclarationModule(IDeclaration decl) throws PromptoError {
		ICodeStore origin = decl.getOrigin();
		List<String> categories = Arrays.asList("Module", origin.getModuleType().getCategory().getTypeName());
		IStorable storable = store.newStorable(categories, null);
		storable.setData("name", origin.getModuleName());
		storable.setData("version", origin.getModuleVersion());
		store.store(storable);
		return storable.getOrCreateDbId();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Module> T fetchModule(ModuleType type, String name, PromptoVersion version) throws PromptoError {
		try {
			IStored stored = fetchOneNamedInStore(type.getCategory(), version, name);
			if(stored==null)
				return null;
			Module module = type.getModuleClass().newInstance();
			module.fromStored(stored);
			return (T)module;
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public Resource fetchSpecificResource(String name, PromptoVersion version) {
		try {
			IStored stored = fetchOneInStore(new CategoryType(new Identifier("Resource")), version, "name", name);
			if(stored==null)
				return null;
			return readResource(stored);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private Resource readResource(IStored stored) {
		Resource resource = null;
		String mimeType = (String)stored.getData("mimeType");
		if(mimeType.startsWith("text/")) {
			resource = new TextResource();
			((TextResource)resource).setBody((String)stored.getData("body"));
		} else {
			resource = new BinaryResource();
			((BinaryResource)resource).setData((PromptoBinary)stored.getData("data"));
		}
		resource.setMimeType(mimeType);
		resource.setName((String)stored.getData("name"));
		resource.setVersion((PromptoVersion)stored.getData("version"));
		Long value = (Long)stored.getData("timeStamp");
		if(value!=null)
			resource.setLastModified(OffsetDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneOffset.UTC));
		return resource;
	}

	static ThreadLocal<Map<String, Iterable<IDeclaration>>> registering = new ThreadLocal<Map<String, Iterable<IDeclaration>>>() {
		@Override protected Map<String, Iterable<IDeclaration>> initialValue() {
	        return new HashMap<>();
	    }
	};
	
	
	@SuppressWarnings("unchecked")
	private IDeclaration getRegisteringSymbol(String name) {
		return registering.get().values().stream()
				.map(i->StreamSupport.stream(i.spliterator(), false))
				.flatMap(Function.identity())
				.filter(d->d instanceof IEnumeratedDeclaration)
				.map(d->(IEnumeratedDeclaration<Symbol>)d)
				.filter(e->e.hasSymbol(name))
				.findFirst()
				.orElse(null);
	}


	private Iterable<IDeclaration> getRegisteringDeclarations(String name) {
		return registering.get().get(name);
	}
	
	private void setRegisteringDeclarations(String name, Iterable<IDeclaration> decl) {
		registering.get().put(name, decl);
	}

	private void clearRegisteringDeclarations(String name) {
		registering.get().remove(name);
	}

	@Override
	public Collection<String> fetchDeclarationNames() {
		// TODO Auto-generated method stub
		return super.fetchDeclarationNames();
	}
	
	@Override
	public Iterable<IDeclaration> fetchSpecificDeclarations(String name, PromptoVersion version) throws PromptoError {
		Iterable<IDeclaration> decls = fetchDeclarationsInStore(name, version);
		if(decls!=null)
			return decls;
		if(storeExternals)
			return fetchAndStoreExternalSpecificDeclarations(name, version);
		else
			return super.fetchSpecificDeclarations(name, version);
	}
	
	private synchronized Iterable<IDeclaration> fetchAndStoreExternalSpecificDeclarations(String name, PromptoVersion version2) {
		// when called from the AppServer, multiple threads may be attempting to do this
		// TODO: need to deal with multiple cloud nodes doing this
		synchronized(this) {
			Iterable<IDeclaration> decls = fetchDeclarationsInStore(name, version);
			if(decls!=null)
				return decls;
			decls = getRegisteringDeclarations(name);
			if(decls!=null)
				return decls;
			decls = super.fetchSpecificDeclarations(name, version);
			if(store!=null && decls!=null && decls.iterator().hasNext()) {
				// avoid infinite reentrance loop
				setRegisteringDeclarations(name, decls);
				decls = storeDeclarations(decls);
				clearRegisteringDeclarations(name);
				store.flush();
			}
			return decls;
		}
	}
	
	@Override
	public IDeclaration fetchSpecificSymbol(String name, PromptoVersion version) throws PromptoError {
		Iterable<IDeclaration> decls = fetchSymbolsInStore(name, version);
		if(decls!=null && decls.iterator().hasNext())
			return decls.iterator().next();
		if(storeExternals)
			return fetchAndStoreExternalSpecificSymbol(name, version);
		else
			return super.fetchSpecificSymbol(name, version);
	}
	
	
	private synchronized IDeclaration fetchAndStoreExternalSpecificSymbol(String name, PromptoVersion version2) {
		// when called from the AppServer, multiple threads may be attempting to do this
		// TODO: need to deal with multiple cloud nodes doing this
		synchronized(this) {
			Iterable<IDeclaration> decls = fetchSymbolsInStore(name, version);
			if(decls!=null && decls.iterator().hasNext())
				return decls.iterator().next();
			IDeclaration decl = getRegisteringSymbol(name);
			if(decl==null) {
				decl = super.fetchSpecificSymbol(name, version);
				if(store!=null && decl!=null) {
					setRegisteringDeclarations(decl.getName(), Collections.singletonList(decl));
					decls = storeDeclarations(Collections.singletonList(decl));
					clearRegisteringDeclarations(decl.getName());
					store.flush();
					decl = decls.iterator().next();
				}
			}
			return decl;
		}
	}

	private Iterable<IDeclaration> fetchSymbolsInStore(String name, PromptoVersion version) {
		IStoredIterable iterable = fetchStoredDeclarationsBySymbol(name, version);
		if(iterable.iterator().hasNext()) {
			Iterator<IStored> iterator = iterable.iterator();
			return () -> new Iterator<IDeclaration>() {
				@Override public boolean hasNext() { return iterator.hasNext(); }
				@Override public IDeclaration next() { return parseDeclaration(iterator.next()); }
			};
		} else
			return null;
	}

	private IStoredIterable fetchStoredDeclarationsBySymbol(String name, PromptoVersion version) {
		IQueryBuilder builder = store.newQueryBuilder();
		builder.verify(AttributeInfo.CATEGORY, MatchOp.CONTAINS, "EnumeratedDeclaration");
		builder.verify(AttributeInfo.SYMBOLS, MatchOp.CONTAINS, name);
		builder.and();
		if(PromptoVersion.LATEST.equals(version)) {
			IdentifierList names = IdentifierList.parse("prototype,version");
			OrderByClauseList orderBy = new OrderByClauseList( new OrderByClause(names, true) );
			orderBy.interpretQuery(context, builder);
			IStoredIterable stored = store.fetchMany(builder.build());
			return fetchDistinct(stored);
		} else
			return store.fetchMany(builder.build()); 
	}

	private Iterable<IDeclaration> storeDeclarations(Iterable<IDeclaration> decls) throws PromptoError {
		Iterator<IDeclaration> iter = decls.iterator();
		if(!iter.hasNext())
			return null;
		// need module id
		IDeclaration decl = iter.next();
		ICodeStore origin = decl.getOrigin();
		if(origin==null)
			throw new InternalError("Cannot store declaration with no origin!");
		Object moduleId = fetchDeclarationModuleDbId(decl);
		if(moduleId==null)
			moduleId = storeDeclarationModule(decl);
		storeDeclarations(decls, origin.getModuleDialect(), origin.getModuleVersion(), moduleId);
		return decls;
	}


	@Override
	public void storeDeclarations(Iterable<IDeclaration> declarations, Dialect dialect, PromptoVersion version, Object moduleId) throws PromptoError {
		List<IStorable> list = new ArrayList<>();
		declarations.forEach((decl)->
			collectStorables(list, decl, dialect, version, moduleId));
		store.store(null, list);
	}
	
	private Object fetchDeclarationModuleDbId(IDeclaration decl) throws PromptoError {
		ICodeStore origin = decl.getOrigin();
		IStored stored = fetchOneNamedInStore(origin.getModuleType().getCategory(), origin.getModuleVersion(), origin.getModuleName());
		if(stored==null)
			return null;
		else
			return stored.getDbId();
	}

	private IStorable populateDeclarationStorable(List<String> categories, IDeclaration decl, Dialect dialect, PromptoVersion version, Object moduleId) {
		IStorable storable = store.newStorable(categories, null); 
		try {
			storable.setData("name", decl.getId().toString());
			storable.setData("version", version);
			if(decl instanceof IMethodDeclaration) {
				String proto = ((IMethodDeclaration)decl).getProto();
				storable.setData("prototype", proto);
			}
			storable.setData("dialect", dialect.name());
			CodeWriter writer = new CodeWriter(dialect, context);
			decl.toDialect(writer);
			String content = writer.toString();
			storable.setData("body", content);
			storable.setData("module",  moduleId);
			if(decl instanceof IEnumeratedDeclaration) {
				@SuppressWarnings("unchecked")
				List<String> symbols = ((IEnumeratedDeclaration<Symbol>)decl).getSymbolsList().stream()
						.map(Symbol::getName)
						.collect(Collectors.toList());
				storable.setData("symbols",  symbols);
			}
			if(decl.isStorable())
				storable.setData("storable", true);
			return storable;
		} catch(PromptoError e) {
			throw new RuntimeException(e);
		}
	}

	
	private Iterable<IDeclaration> fetchDeclarationsInStore(String name, PromptoVersion version) {
		if(store==null)
			return null;
		else try {
			CategoryType category = new CategoryType(new Identifier("Declaration"));
			IStoredIterable iterable = fetchManyNamedInStore(name, category, version);
			if(iterable.iterator().hasNext()) {
				Iterator<IStored> iterator = iterable.iterator();
				return () -> new Iterator<IDeclaration>() {
					@Override public boolean hasNext() { return iterator.hasNext(); }
					@Override public IDeclaration next() { return parseDeclaration(iterator.next()); }
				};
			} else
				return null;
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static Set<String> uniqueDecls = new HashSet<>(
			Arrays.asList(DeclarationType.ATTRIBUTE.name(), DeclarationType.CATEGORY.name(), DeclarationType.TEST.name()));
	
	private IStoredIterable fetchManyNamedInStore(String name, CategoryType type, PromptoVersion version) throws PromptoError {
		return fetchManyInStore(type, version, "name", name);
	}

	private IStoredIterable fetchManyInStore(CategoryType type, PromptoVersion version, String attribute, String value) throws PromptoError {
		IQueryBuilder builder = store.newQueryBuilder();
		if(uniqueDecls.contains(type.toString().toUpperCase())) {
			builder.first(1L).last(1L);
		}
		AttributeInfo info = AttributeInfo.CATEGORY;
		builder.verify(info, MatchOp.CONTAINS, type.getTypeName());
		IPredicateExpression filter = buildFilter(version, attribute, value);
		filter.interpretQuery(context, builder);
		builder.and();
		if(PromptoVersion.LATEST.equals(version)) {
			IdentifierList names = new IdentifierList("prototype", "version");
			OrderByClauseList orderBy = new OrderByClauseList( new OrderByClause(names, true) );
			orderBy.interpretQuery(context, builder);
			IStoredIterable stored = store.fetchMany(builder.build());
			return fetchDistinct(stored);
		} else
			return store.fetchMany(builder.build()); 
	}
	
	private IStored fetchOneNamedInStore(CategoryType type, PromptoVersion version, String name) throws PromptoError {
		return fetchOneInStore(type, version, "name", name);
	}
	
	private IStored fetchOneInStore(CategoryType type, PromptoVersion version, String attribute, String value) throws PromptoError {
		IQueryBuilder builder = store.newQueryBuilder();
		AttributeInfo info = AttributeInfo.CATEGORY;
		builder.verify(info, MatchOp.CONTAINS, type.getTypeName());
		IPredicateExpression filter = buildFilter(version, attribute, value);
		filter.interpretQuery(context, builder);
		builder.and();
		if(PromptoVersion.LATEST.equals(version)) {
			IdentifierList names = new IdentifierList("version");
			OrderByClauseList orderBy = new OrderByClauseList( new OrderByClause(names, true) );
			orderBy.interpretQuery(context, builder);
			builder.first(1L).last(1L);
			IStoredIterable iterable = store.fetchMany(builder.build());
			Iterator<IStored> stored = iterable.iterator();
			return stored.hasNext() ? stored.next() : null;
		} else
			return store.fetchOne(builder.build()); 
	}

	@Override
	public void collectStorableAttributes(Map<String, AttributeDeclaration> map) throws PromptoError {
		super.collectStorableAttributes(map);
		if(store!=null) {
			IQueryBuilder builder = store.newQueryBuilder();
			AttributeInfo info = AttributeInfo.CATEGORY;
			builder.verify(info, MatchOp.CONTAINS, "AttributeDeclaration");
			info = AttributeInfo.STORABLE;
			builder.verify(info, MatchOp.EQUALS, true);
			builder.and();
			IStoredIterable iterable = store.fetchMany(builder.build());
			Iterator<IStored> stored = iterable.iterator();
			while(stored.hasNext()) {
				AttributeDeclaration attr = parseDeclaration(stored.next());
				map.put(attr.getName(), attr);		
			}
		}
	}


	private IStoredIterable fetchDistinct(IStoredIterable iterable) {
		/* we don't support distinct/group by yet, so need to do it "by hand" */
		List<IStored> distinct = new ArrayList<>();
		Object lastName = null;
		Object lastProto = null;
		for(IStored stored : iterable) {
			Object thisName = stored.getData("name");
			Object thisProto = stored.getData("prototype");
			if(!Objects.equals(thisName, lastName) || !Objects.equals(thisProto, lastProto)) {
				distinct.add(stored);
				lastName = thisName;
				lastProto = thisProto;
			}
		}
		return new IStoredIterable() {
			@Override public Iterator<IStored> iterator() { return distinct.iterator(); }
			@Override public long count() { return distinct.size(); }
			@Override public long totalCount() { return distinct.size(); }
		};
	}

	private IPredicateExpression buildFilter(PromptoVersion version, String attribute, String value) {
		IExpression left = new UnresolvedIdentifier(new Identifier(attribute));
		IExpression right = new TextLiteral("'" + value + "'");
		IPredicateExpression filter = new EqualsExpression(left, EqOp.EQUALS, right);
		if(!PromptoVersion.LATEST.equals(version)) {
			left = new UnresolvedIdentifier(new Identifier("version"));
			right = new VersionLiteral("'v" + version.toString() + "'");
			IExpression condition = new EqualsExpression(left, EqOp.EQUALS, right);
			filter = new AndExpression(filter, condition);
		} 
		return filter;
	}

	@SuppressWarnings("unchecked")
	private <T extends IDeclaration> T parseDeclaration(IStored stored) {
		if(stored==null)
			return null;
		try {
			Dialect dialect = Dialect.valueOf((String)stored.getData("dialect"));
			String body = (String)stored.getData("body");
			InputStream input = new ByteArrayInputStream(body.getBytes());
			DeclarationList decls = ICodeStore.parse(dialect, "__store__", input);
			return decls.isEmpty() ? null : (T)decls.get(0);
		} catch (Exception e) {
			throw new RuntimeException(e); // TODO
		}
	}

}
