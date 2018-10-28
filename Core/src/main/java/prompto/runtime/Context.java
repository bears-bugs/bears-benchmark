package prompto.runtime;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import prompto.code.ICodeStore;
import prompto.debug.LocalDebugger;
import prompto.declaration.AttributeDeclaration;
import prompto.declaration.ConcreteCategoryDeclaration;
import prompto.declaration.IDeclaration;
import prompto.declaration.IMethodDeclaration;
import prompto.declaration.NativeCategoryDeclaration;
import prompto.declaration.SingletonCategoryDeclaration;
import prompto.declaration.TestMethodDeclaration;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.expression.Symbol;
import prompto.grammar.Annotation;
import prompto.grammar.INamed;
import prompto.grammar.Identifier;
import prompto.parser.Dialect;
import prompto.parser.ILocation;
import prompto.parser.ISection;
import prompto.problem.IProblemListener;
import prompto.problem.ProblemListener;
import prompto.statement.CommentStatement;
import prompto.statement.IStatement;
import prompto.type.CategoryType;
import prompto.type.DecimalType;
import prompto.type.IType;
import prompto.type.MethodType;
import prompto.type.NativeType;
import prompto.utils.CodeWriter;
import prompto.utils.ObjectUtils;
import prompto.utils.SectionLocator;
import prompto.value.ClosureValue;
import prompto.value.ConcreteInstance;
import prompto.value.Decimal;
import prompto.value.Document;
import prompto.value.ExpressionValue;
import prompto.value.IInstance;
import prompto.value.IValue;

/* a Context is the place where the Interpreter locates declarations and values */
public class Context implements IContext {
	
	public static Context newGlobalContext() {
		Context context = new Context();
		context.globals = context;
		context.calling = null;
		context.parent = null;
		context.debugger = null;
		context.problemListener = new ProblemListener();
		return context;
	}

	Context globals;
	Context calling;
	Context parent; // for inner methods
	LocalDebugger debugger; 
	IProblemListener problemListener;
	
	Map<Identifier,IDeclaration> declarations = new HashMap<>();
	Map<Identifier,TestMethodDeclaration> tests = new HashMap<>();
	Instances instances = new Instances();
	Map<Identifier,IValue> values = new HashMap<>();
	Map<Type, NativeCategoryDeclaration> nativeBindings = new HashMap<>();
	
	static class Instances {
		
		Map<Identifier,INamed> map = new HashMap<Identifier, INamed>();
		List<INamed> list = new ArrayList<>();
		
		
		@Override
		public String toString() {
			return list.toString();
		}
		
		public boolean isEmpty() {
			return map.isEmpty();
		}

		public Set<Identifier> keySet() {
			return map.keySet();
		}

		public void remove(Identifier id) {
			INamed named = map.remove(id);
			list.remove(named);
		}

		public INamed get(Identifier name) {
			return map.get(name);
		}

		public void put(Identifier id, INamed value) {
			INamed previous = map.put(id, value);
			if(previous!=null)
				list.remove(previous);
			list.add(value);
		}

		public Collection<INamed> values() {
			return list;
		}
	}
	
	protected Context() {
	}

	public Context getGlobalContext() {
		return globals;
	}

	public boolean isGlobalContext() {
		return this==globals;
	}
	
	public void setDebugger(LocalDebugger debugger) {
		this.debugger = debugger;
	}
	
	public LocalDebugger getDebugger() {
		return debugger;
	}
	
	public void setProblemListener(IProblemListener problemListener) {
		this.problemListener = problemListener;
	}
	
	public IProblemListener getProblemListener() {
		return problemListener;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		if(this!=globals) {
			sb.append("globals:");
			sb.append(globals);
		}
		sb.append(",calling:");
		sb.append(calling);
		sb.append(",parent:");
		sb.append(parent);
		sb.append(",declarations:");
		sb.append(declarations);
		sb.append(",instances:");
		sb.append(instances);
		sb.append(",values:");
		sb.append(values);
		sb.append("}");
		return sb.toString();
	}
	
	@Override
	public Context getCallingContext() {
		return calling;
	}

	public InstanceContext getClosestInstanceContext() {
		if(parent==null)
			return null;
		else if(parent instanceof InstanceContext)
			return (InstanceContext)parent;
		else
			return parent.getClosestInstanceContext();
	}
	
	public Context getParentMostContext() {
		if(parent==null)
			return this;
		else
			return parent.getParentMostContext();
	}

	public Context getParentContext() {
		return parent;
	}
	
	public void setParentContext(Context parent) {
		this.parent = parent;
	}
	
	public Context newResourceContext() {
		Context context = new ResourceContext();
		context.globals = this.globals;
		context.calling = this.calling;
		context.parent = this;
		context.debugger = this.debugger;
		context.problemListener = this.problemListener;
		return context;
	}
	
	public Context newLocalContext() {
		Context context = new Context();
		context.globals = this.globals;
		context.calling = this;
		context.parent = null;
		context.debugger = this.debugger;
		context.problemListener = this.problemListener;
		return context;
	}
	
	public Context newBuiltInContext(IValue value) {
		return initInstanceContext(new BuiltInContext(value), false);
	}

	public Context newBuiltInContext(NativeType type) {
		return initInstanceContext(new BuiltInContext(type), false);
	}

	public Context newInstanceContext(IInstance instance, boolean isChild) {
		return initInstanceContext(new InstanceContext(instance), isChild);
	}
	
	public Context newInstanceContext(CategoryType type, boolean isChild) {
		return initInstanceContext(new InstanceContext(type), isChild);
	}
	
	public Context newDocumentContext(Document document, boolean isChild) {
		return initInstanceContext(new DocumentContext(document), isChild);
	}

	public Context newClosureContext(MethodType type) {
		return initInstanceContext(new ClosureContext(type), true);
	}

	public Context newMemberContext(CategoryType type) {
		return newInstanceContext(type, false).newChildContext();
	}


	private Context initInstanceContext(Context context, boolean isChild) {
		context.globals = this.globals;
		context.calling = isChild ? this.calling : this;
		context.parent = isChild ? this : null;
		context.debugger = this.debugger;
		context.problemListener = this.problemListener;
		return context;
	}

	public Context newChildContext() {
		Context context = new Context();
		context.globals = this.globals;
		context.calling = this.calling;
		context.parent = this;
		context.debugger = this.debugger;
		context.problemListener = this.problemListener;
		return context;
	}

	public boolean isEmpty() {
		if(globals!=this)
			return globals.isEmpty();
		return declarations.isEmpty() 
				&& tests.isEmpty()
				&& instances.isEmpty()
				&& values.isEmpty();
	}
	
	public void unregister(String path) {
		unregisterDeclarations(path);
		unregisterValues(path);
		unregisterTests(path);
	}
	
	private void unregisterValues(String path) {
		List<Identifier> toRemove = new ArrayList<Identifier>();
		for(Identifier id : instances.keySet()) {
			if(path.equals(id.getFilePath()))
				toRemove.add(id);
		}
		for(Identifier id : toRemove) {
			instances.remove(id);
			values.remove(id);
		}
	}

	private void unregisterTests(String path) {
		List<TestMethodDeclaration> toRemove = new ArrayList<TestMethodDeclaration>();
		for(TestMethodDeclaration decl : tests.values()) {
			if(path.equals(decl.getFilePath()))
				toRemove.add(decl);
		}
		for(TestMethodDeclaration decl : toRemove)
			tests.remove(decl.getId());
	}

	private void unregisterDeclarations(String path) {
		List<IDeclaration> toRemove = new ArrayList<IDeclaration>();
		for(IDeclaration decl : declarations.values()) {
			if(path.equals(decl.getFilePath()))
				toRemove.add(decl);
			else if(decl instanceof MethodDeclarationMap)
				((MethodDeclarationMap)decl).unregister(path);
		}
		for(IDeclaration decl : toRemove) {
			declarations.remove(decl.getId());
			if(decl instanceof NativeCategoryDeclaration) {
				Class<?> klass = ((NativeCategoryDeclaration)decl).getBoundClass(false);
				if(klass!=null)
					nativeBindings.remove(klass);
			}
		}
	}
	
	public AttributeDeclaration findAttribute(String name) {
		return getRegisteredDeclaration(AttributeDeclaration.class, new Identifier(name));
	}
	
	public List<AttributeDeclaration> getAllAttributes() {
		if(globals!=this)
			return globals.getAllAttributes();
		List<AttributeDeclaration> list = new ArrayList<>();
		for(IDeclaration decl : declarations.values()) {
			if(decl instanceof AttributeDeclaration)
				list.add((AttributeDeclaration)decl);
		}
		return list;
	}

	public INamed getRegistered(Identifier name) {
		// resolve upwards, since local names override global ones
		INamed actual = declarations.get(name);
		if(actual!=null)
			return actual;
		actual = instances.get(name);
		if(actual!=null)
			return actual;
		if(parent!=null)
			return parent.getRegistered(name);
		if(globals!=this)
			return globals.getRegistered(name);
		return null;	
	}
	
	public <T extends IDeclaration> T getLocalDeclaration(Class<T> klass, Identifier id) {
		IDeclaration actual = declarations.get(id);
		if(actual!=null)
			return ObjectUtils.downcast(klass, actual);
		else if(parent!=null)
			return parent.getLocalDeclaration(klass, id);
		else
			return null;
	}

	public <T extends IDeclaration> T getRegisteredDeclaration(Class<T> klass, Identifier id) {
		return getRegisteredDeclaration(klass, id, true);
	}
	
	public <T extends IDeclaration> T getRegisteredDeclaration(Class<T> klass, Identifier id, boolean lookInStore) {
		// resolve upwards, since local names override global ones
		IDeclaration actual = declarations.get(id);
		if(actual!=null)
			return ObjectUtils.downcast(klass, actual);
		else if(parent!=null)
			actual = parent.getRegisteredDeclaration(klass, id, lookInStore);
		if(actual!=null)
			return ObjectUtils.downcast(klass, actual);
		else if(globals!=this)
			actual = globals.getRegisteredDeclaration(klass, id, lookInStore);
		if(actual!=null)
			return ObjectUtils.downcast(klass, actual);
		else if(lookInStore && globals==this)
			actual = fetchAndRegisterDeclaration(id);
		if(actual!=null)
			return ObjectUtils.downcast(klass, actual);
		else
			return null;
	}
	
	public Symbol getRegisteredSymbol(Identifier id, boolean lookInStore) {
		Symbol symbol = getRegisteredValue(Symbol.class, id);
		if(symbol!=null || !lookInStore)
			return symbol;
		if(globals!=this)
			return globals.getRegisteredSymbol(id, lookInStore);
		else if(lookInStore)
			return fetchAndRegisterSymbol(id);
		else
			return null;
	}

	private Symbol fetchAndRegisterSymbol(Identifier id) {
		ICodeStore store = ICodeStore.getInstance();
		if(store==null)
			return null;
		// fetch and register atomically
		synchronized(this) {
			Symbol symbol = getRegisteredValue(Symbol.class, id); // may have happened in another thread
			if(symbol!=null)
				return symbol;
			try {
				IDeclaration decl = store.fetchLatestSymbol(id.toString());
				if(decl==null)
					return null;
				decl.register(this);
				return getRegisteredValue(Symbol.class, id);
			} catch(PromptoError e) {
				throw new RuntimeException(e); // TODO define a strategy
			}
		}
	}
	
	public void fetchAndRegisterAllDeclarations() {
		ICodeStore store = ICodeStore.getInstance();
		if(store==null)
			return;
		synchronized(this) {
			Collection<String> names = store.fetchDeclarationNames();
			names.stream()
				.map(Identifier::new)
				.forEach(this::fetchAndRegisterDeclaration);
		}		
	}

	private IDeclaration fetchAndRegisterDeclaration(Identifier name) {
		ICodeStore store = ICodeStore.getInstance();
		if(store==null)
			return null;
		// fetch and register atomically
		synchronized(this) {
			IDeclaration decl = declarations.get(name); // may have happened in another thread
			if(decl!=null)
				return decl;
			try {
				Iterable<IDeclaration> decls = store.fetchLatestDeclarations(name.toString());
				if(decls==null)
					return null;
				decls.forEach((d)-> {
					if(d instanceof MethodDeclarationMap) {
						MethodDeclarationMap map = (MethodDeclarationMap)d;
						for(Map.Entry<String, IMethodDeclaration> entry : map.entrySet())
							entry.getValue().register(this);
					} else
						d.register(this);
				});
				return declarations.get(name);
			} catch(PromptoError e) {
				throw new RuntimeException(e); // TODO define a strategy
			}
		}
	}

	public void registerDeclaration(IDeclaration declaration) {
		if(checkDuplicateDeclaration(declaration))
			declarations.put(declaration.getId(), declaration);
	}

	private boolean checkDuplicateDeclaration(IDeclaration declaration) {
		IDeclaration current = getRegisteredDeclaration(IDeclaration.class, declaration.getId(), false);
		if(current!=null && current!=declaration)
			problemListener.reportDuplicate(declaration.getId().toString(), declaration, current.getId());
		return current==null;
	}

	public void registerDeclaration(IMethodDeclaration declaration) {
		MethodDeclarationMap current = checkDuplicate(declaration);
		if(current==null) {
			current = new MethodDeclarationMap(declaration.getId());
			declarations.put(declaration.getId(), (MethodDeclarationMap)current);
		}
		current.register(declaration,this);
	}
	
	public void registerDeclarationIfMissing(IMethodDeclaration declaration) {
		MethodDeclarationMap current = getRegisteredDeclaration(MethodDeclarationMap.class, declaration.getId());
		if(current==null) {
			current = new MethodDeclarationMap(declaration.getId());
			declarations.put(declaration.getId(), (MethodDeclarationMap)current);
		}
		current.registerIfMissing(declaration,this);
	}

	private MethodDeclarationMap checkDuplicate(IMethodDeclaration declaration) {
		INamed current = getRegistered(declaration.getId());
		if(current!=null && !(current instanceof MethodDeclarationMap))
			problemListener.reportDuplicate(declaration.getId().toString(), declaration, (ISection)current);
		return (MethodDeclarationMap)current;
	}

	public void registerDeclaration(TestMethodDeclaration declaration) {
		if(checkDuplicate(declaration))
			tests.put(declaration.getId(), declaration);
	}
	
	private boolean checkDuplicate(TestMethodDeclaration declaration) {
		TestMethodDeclaration current = tests.get(declaration.getId());
		if(current!=null)
			problemListener.reportDuplicate(declaration.getId().toString(), declaration, (ISection)current);
		return current==null;
	}
	
	public static class MethodDeclarationMap extends HashMap<String,IMethodDeclaration> implements IDeclaration {

		private static final long serialVersionUID = 1L;
		
		Identifier id;
		ICodeStore origin;
		
		public MethodDeclarationMap(Identifier id) {
			this.id = id;
		}
		
		@Override
		public Collection<CommentStatement> getComments() {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public void setComments(Collection<CommentStatement> stmts) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public Collection<Annotation> getAnnotations() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAnnotations(Collection<Annotation> annotations) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean hasLocalAnnotation(String name) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean hasInheritedAnnotation(Context context, String name) {
			throw new UnsupportedOperationException();
		}

		@Override
		public DeclarationType getDeclarationType() {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public ICodeStore getOrigin() {
			return origin;
		}
		
		@Override
		public void setOrigin(ICodeStore origin) {
			this.origin = origin;
		}
		
		public void unregister(String path) {
			List<IMethodDeclaration> toRemove = new ArrayList<IMethodDeclaration>();
			for(IMethodDeclaration decl : this.values()) {
				if(path.equals(decl.getFilePath()))
					toRemove.add(decl);
			}
			for(IMethodDeclaration decl : toRemove)
				this.remove(decl.getProto());
		}

		@Override
		public void toDialect(CodeWriter writer) {
			throw new RuntimeException("Should never get there!");
		}
		
		@Override
		public Identifier getId() {
			return id;
		}
		
		@Override
		public IType check(Context context, boolean isStart) {
			throw new RuntimeException("Should never get there!");
		}
		
		@Override
		public void register(Context context) {
			throw new RuntimeException("Should never get there!");
		}
		
		public void register(IMethodDeclaration declaration, Context context) {
			String proto = declaration.getProto();
			if(this.containsKey(proto))
				context.getProblemListener().reportDuplicate(declaration.getId().toString(), declaration, this.get(proto));
			else
				this.put(proto, declaration);
		}
		
		public void registerIfMissing(IMethodDeclaration declaration,Context context) {
			String proto = declaration.getProto();
			if(!this.containsKey(proto))
				this.put(proto, declaration);
		}
		
		@Override
		public IType getType(Context context) {
			throw new SyntaxError("Should never get there!");
		}

		@Override
		public String getFilePath() {
			return "__INTERNAL__"; // avoid crash in unregister
		}

		@Override
		public ILocation getStart() {
			throw new RuntimeException("Should never get there!");
		}

		@Override
		public ILocation getEnd() {
			throw new RuntimeException("Should never get there!");
		}
		
		@Override
		public Dialect getDialect() {
			throw new RuntimeException("Should never get there!");
		}
		
		@Override
		public void setAsBreakpoint(boolean set) {
			throw new RuntimeException("Should never get there!");
		}
		
		@Override
		public boolean isBreakpoint() {
			throw new RuntimeException("Should never get there!");
		}
		
		@Override
		public boolean isOrContains(ISection section) {
			throw new RuntimeException("Should never get there!");
		}

		public Collection<IMethodDeclaration> globalConcreteMethods() {
			return values().stream()
					.filter((m)->
						!m.isAbstract())
					.filter((m)->
						m.getMemberOf()==null)
					.collect(Collectors.toList());
		}

		public IMethodDeclaration getFirst() {
			return values().iterator().next();
		}

	}
	
	public <T extends INamed> T getRegisteredValue(Class<T> klass, Identifier name) {
		Context context = contextForValue(name);
		if(context==null)
			return null;
		else
			return context.readRegisteredValue(klass, name);
	}
	
	protected <T extends INamed> T readRegisteredValue(Class<T> klass, Identifier name) {
		INamed actual = instances.get(name);
		if(actual!=null)
			return ObjectUtils.downcast(klass,actual);
		else
			return null;
	}
	
	
	public void registerValue(INamed value) {
		registerValue(value, true);
	}
	
	public void registerValue(INamed value, boolean checkDuplicate) {
		if(checkDuplicate) {
			// only explore current context
			if(instances.get(value.getId())!=null)
				throw new SyntaxError("Duplicate name: \"" + value.getId() + "\"");
		}
		instances.put(value.getId(), value);
	}
	
	public Collection<INamed> getInstances() {
		return instances.values();
	}
	

	public boolean hasValue(Identifier id) {
		return contextForValue(id)!=null;
	}


	public IValue getValue(Identifier id) throws PromptoError {
		return getValue(id, ()->null);
	}
	
	
	public IValue getValue(Identifier id, Supplier<IValue> supplier) throws PromptoError {
		Context context = contextForValue(id);
		if(context==null)
			throw new SyntaxError(id + " is not defined");
		return context.readValue(id, supplier);
	}
	
	
	protected IValue readValue(Identifier id, Supplier<IValue> supplier) throws PromptoError {
		IValue value = values.get(id);
		if(value==null) {
			value = supplier.get();
			if(value!=null)
				values.put(id, value);
		}
		if(value==null)
			throw new SyntaxError(id + " has no value");
		if(value instanceof LinkedValue)
			return ((LinkedValue)value).getContext().getValue(id);
		else
			return value;
	}
	
	public void setValue(Identifier name, IValue value) throws PromptoError {
		Context context = contextForValue(name);
		if(context==null)
			throw new SyntaxError(name + " is not defined");
		context.writeValue(name,value);
	}
	
	protected void writeValue(Identifier name, IValue value) throws PromptoError {
		value = autocast(name, value);
		IValue current = values.get(name);
		if(current instanceof LinkedValue)
			((LinkedValue)current).getContext().setValue(name, value);
		else
			values.put(name, value);
	}

	private IValue autocast(Identifier name, IValue value) {
		if(value!=null) {
			if(value instanceof ExpressionValue)
				value = ((ExpressionValue)value).getValue();
			if(value instanceof prompto.value.Integer) {
				INamed actual = instances.get(name);
				if(actual.getType(this)==DecimalType.instance())
					value = new Decimal(((prompto.value.Integer)value).doubleValue());
			}
		}
		return value;
	}

	public Context contextForValue(Identifier name) {
		// resolve upwards, since local names override global ones
		INamed actual = instances.get(name);
		if(actual!=null)
			return this;
		if(parent!=null)
			return parent.contextForValue(name);
		if(globals!=this)
			return globals.contextForValue(name);
		return null;
	}
	

	public void enterMethod(IDeclaration method) throws PromptoError {
		if(debugger!=null)
			debugger.enterMethod(this, method);
	}

	
	public void leaveMethod(IDeclaration method) throws PromptoError {
		if(debugger!=null)
			debugger.leaveMethod(this, method);
	}

	
	public void enterStatement(IStatement statement) throws PromptoError {
		if(debugger!=null)
			debugger.enterStatement(this, statement);
	}

	
	public void leaveStatement(IStatement statement) throws PromptoError {
		if(debugger!=null)
			debugger.leaveStatement(this, statement);
	}

	
	public void notifyTerminated() {
		if(debugger!=null)
			debugger.notifyTerminated();
	}

	
	public ConcreteInstance loadSingleton(Context context, CategoryType type) throws PromptoError {
		if(this==globals) {
			IValue value = values.get(type.getTypeNameId());
			if(value==null) {
				IDeclaration decl = declarations.get(type.getTypeNameId());
				if(decl==null)
					decl = fetchAndRegisterDeclaration(type.getTypeNameId());
				if(!(decl instanceof SingletonCategoryDeclaration))
					throw new InternalError("No such singleton:" + type.getTypeName());
				value = new ConcreteInstance(context, (ConcreteCategoryDeclaration)decl);
				((IInstance)value).setMutable(true); // a singleton is protected by "with x do", so always mutable in that context
				values.put(type.getTypeNameId(), value);
			}
			if(value instanceof ConcreteInstance)
				return (ConcreteInstance)value;
			else
				throw new InternalError("Not a concrete instance:" + value.getClass().getSimpleName());
		} else
			return this.globals.loadSingleton(context, type);
	}

	public boolean hasTests() {
		return tests.size()>0;
	}

	public Collection<TestMethodDeclaration> getTests() {
		if(globals!=this)
			return globals.getTests();
		else
			return tests.values();
	}

	public TestMethodDeclaration getTest(Identifier name, boolean lookInStore) {
		if(globals!=this)
			return globals.getTest(name, lookInStore);
		else {
			IDeclaration test = tests.get(name);
			if(test==null && lookInStore)
				test = fetchAndRegisterTest(name);
			if(test instanceof TestMethodDeclaration)
				return (TestMethodDeclaration)test;
			else
				return null;
		}
	}
	
	private IDeclaration fetchAndRegisterTest(Identifier name) {
		ICodeStore store = ICodeStore.getInstance();
		if(store==null)
			return null;
		// fetch and register atomically
		synchronized(this) {
			IDeclaration decl = tests.get(name); // may have happened in another thread
			if(decl!=null)
				return decl;
			try {
				Iterable<IDeclaration> decls = store.fetchLatestDeclarations(name.toString());
				if(decls==null)
					return null;
				decls.forEach((d) -> {
					if(d instanceof MethodDeclarationMap) {
						MethodDeclarationMap map = (MethodDeclarationMap)d;
						for(Map.Entry<String, IMethodDeclaration> entry : map.entrySet())
							entry.getValue().register(this);
					} else
						d.register(this);
				});
				return tests.get(name);
			} catch(PromptoError e) {
				throw new RuntimeException(e); // TODO define a strategy
			}
		}
	}
	
	
	@Override
	public ISection findSectionFor(String path, int lineNumber) {
		if(globals!=this)
			return globals.findSectionFor(path, lineNumber);
		else
			return SectionLocator.findSection(declarations.values(), path, lineNumber);
	}
	
	public ISection findSection(ISection section) {
		if(globals!=this)
			return globals.findSection(section);
		else {
			ISection result = SectionLocator.findSection(declarations.values(), section);
			if(result!=null) 
				return result;
			ICodeStore store = ICodeStore.getInstance();
			if(store!=null)
				return store.findSection(section);
				else
					return null;
		}
	}


	public void registerNativeBinding(Type type, NativeCategoryDeclaration declaration) {
		if(this==globals)
			nativeBindings.put(type, declaration);
		else
			globals.registerNativeBinding(type, declaration);
	}
	
	public NativeCategoryDeclaration getNativeBinding(Type type) {
		if(this==globals)
			return nativeBindings.get(type);
		else
			return globals.getNativeBinding(type);
	}

	public static class ResourceContext extends Context {
		
		ResourceContext() {
		}

	}

	public static class DocumentContext extends Context {
		
		Document document;
		
		DocumentContext(Document document) {
			this.document = document;
		}
		
		@Override
		public Context contextForValue(Identifier name) {
			// params and variables have precedence over members
			// so first look in context values
			Context context = super.contextForValue(name);
			if(context!=null)
				return context;
			// since any name is valid in the context of a document
			// simply return this document context
			else 
				return this;
		}

		@Override
		protected IValue readValue(Identifier name, Supplier<IValue> supplier) throws PromptoError {
			return document.getMember(calling, name, false);
		}
		
		@Override
		protected void writeValue(Identifier name, IValue value) throws PromptoError {
			document.setMember(calling, name, value);
		}

	}
	
	public static class BuiltInContext extends Context {
		
		IValue value;
		NativeType type;
		
		public BuiltInContext(IValue value) {
			this.value = value;
			this.type = (NativeType)value.getType();
		}
		
		public BuiltInContext(NativeType type) {
			this.type = type;
		}

		public IValue getValue() {
			return value;
		}
	}
	
	public static class InstanceContext extends Context {
		
		IInstance instance;
		IType type;
		
		InstanceContext(IInstance instance) {
			this.instance = instance;
			this.type = instance.getType();
		}
		
		InstanceContext(IType type) {
			this.type = type;
		}

		public IInstance getInstance() {
			return instance;
		}
		
		public IType getInstanceType() {
			return type;
		}
		
		@Override
		public INamed getRegistered(Identifier id) {
			INamed actual = super.getRegistered(id);
			if(actual!=null) 
				return actual;
			ConcreteCategoryDeclaration decl = getDeclaration();
			MethodDeclarationMap methods = decl.getMemberMethods(this, id);
			return methods.isEmpty() ? null : methods;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public <T extends IDeclaration> T getRegisteredDeclaration(Class<T> klass, Identifier id, boolean lookInStore) {
			if(klass==MethodDeclarationMap.class) {
				ConcreteCategoryDeclaration decl = getDeclaration();
				if(decl!=null) {
					MethodDeclarationMap methods = decl.getMemberMethods(this, id);
					if(methods!=null && !methods.isEmpty())
						return (T)methods;
				}
			}
			return super.getRegisteredDeclaration(klass, id, lookInStore);
		}
		
		
		protected <T extends INamed> T readRegisteredValue(Class<T> klass, Identifier name) {
			INamed actual = instances.get(name);
			// not very pure, but avoids a lot of complexity when registering a value
			if(actual==null) {
				AttributeDeclaration attr = getRegisteredDeclaration(AttributeDeclaration.class, name);
				if(attr!=null) {
					IType type = attr.getType();
					actual = new Variable(name, type);
					instances.put(name, actual);
				}
			}
			return ObjectUtils.downcast(klass,actual);
		}
		
		@Override
		public Context contextForValue(Identifier name) {
			// params and variables have precedence over members
			// so first look in context values
			Context context = super.contextForValue(name);
			if(context!=null)
				return context;
			ConcreteCategoryDeclaration decl = getDeclaration();
			if(decl.hasAttribute(this, name) || decl.hasMethod(this, name))
				return this;
			else
				return null;
		}
		
		Context superContextForValue(Identifier name) {
			return super.contextForValue(name);
		}
		
		private ConcreteCategoryDeclaration getDeclaration() {
			if(instance!=null)
				return instance.getDeclaration();
			else
				return getRegisteredDeclaration(ConcreteCategoryDeclaration.class, type.getTypeNameId());
		}

		@Override
		protected IValue readValue(Identifier name, Supplier<IValue> supplier) throws PromptoError {
			ConcreteCategoryDeclaration decl = getDeclaration();
			if(decl.hasAttribute(this, name)) {
				IValue value = instance.getMember(calling, name, false);
				return value!=null ? value : supplier.get();
			} else if (decl.hasMethod(this, name)) {
				IMethodDeclaration method = decl.getMemberMethods(this, name).getFirst();
				MethodType type = new MethodType(method);
				return new ClosureValue(this, type);
		
			} else
				return supplier.get();
		}
		
		@Override
		protected void writeValue(Identifier name, IValue value) throws PromptoError {
			instance.setMember(calling, name, value);
		}
	}

	public static class ClosureContext extends InstanceContext {

		public ClosureContext(MethodType type) {
			super(type);
		}
		
		@Override
		public Context contextForValue(Identifier name) {
			return superContextForValue(name);
		}
		
	}




}
