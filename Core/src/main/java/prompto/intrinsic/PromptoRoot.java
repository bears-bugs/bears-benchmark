package prompto.intrinsic;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import prompto.compiler.CompilerUtils;
import prompto.compiler.PromptoClassLoader;
import prompto.error.NotStorableError;
import prompto.store.IStorable;
import prompto.store.IStorable.IDbIdListener;
import prompto.store.IStorable.IDbIdProvider;
import prompto.store.DataStore;
import prompto.store.IStored;
import prompto.store.IStoredIterable;
import prompto.store.InvalidValueError;

public abstract class PromptoRoot implements IDbIdProvider, IDbIdListener, IMutable {

	public static PromptoRoot newInstance(IStored stored) {
		if(stored==null) // happens on an unsuccessful fetchOne
			return null;
		else try {
			Object list = stored.getData("category");
			@SuppressWarnings("unchecked")
			String name = ((PromptoList<String>)list).getLast();
			String concreteName = CompilerUtils.getCategoryConcreteType(name).getTypeName();
			PromptoClassLoader loader = PromptoClassLoader.getInstance();
			if(loader==null)
				throw new UnsupportedOperationException("newPromptoRoot can only be used in compiled mode!");
			Class<?> klass = Class.forName(concreteName, true, loader);
			Constructor<?> cons = klass.getConstructor(IStored.class);
			Object instance = cons.newInstance(stored);
			return (PromptoRoot)instance;
		} catch (Exception e) {
			throw new RuntimeException(e); // TODO for now
		}
	}
	
	public static PromptoRoot newInstanceFromDbIdRef(Object value) {
		if(value instanceof PromptoRoot)
			return (PromptoRoot)value;
		if(DataStore.getInstance().getDbIdClass().isInstance(value))
			value = DataStore.getInstance().fetchUnique(value);
		if(value instanceof IStored)
			return newInstance((IStored)value);
		else
			return (PromptoRoot)value; // will eventually throw an InvalidCastException 
	}
	
	public static IterableWithCounts<PromptoRoot> newIterable(IStoredIterable iterable) {
		return new IterableWithCounts<PromptoRoot>() {
			
			@Override
			public Long getCount() {
				return iterable.count();
			}
			
			@Override
			public Long getTotalCount() {
				return iterable.totalCount();
			}

			@Override
			public Iterator<PromptoRoot> iterator() {
				return new Iterator<PromptoRoot>() {
					
					Iterator<IStored> iterator = iterable.iterator();
					
					@Override public boolean hasNext() { 
						return iterator.hasNext(); 
					}
					@Override public PromptoRoot next() { 
						return newInstance(iterator.next()); 
					}
				};
			}
		};
	}
	
	
	public static Object getStorableData(Object value) {
		if(value instanceof PromptoEnum)
			return ((PromptoEnum)value).getName();
		else if(value instanceof PromptoRoot)
			return ((PromptoRoot)value).getStorableData();
		else
			return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends PromptoRoot> T convertObjectToExact(Object o, Class<T> klass) {
		if(o==null)
			return null;
		else if(klass.isInstance(o))
			return (T)o;
		// TODO: convert Document to instance
		else
			throw new InvalidValueError("Expected a " + klass.getSimpleName() +", got " + o.getClass().getSimpleName());
	}

	protected Object dbId;
	protected IStorable storable;
	protected boolean mutable;
	
	protected PromptoRoot() {
	}
	
	protected PromptoRoot(IStored stored) {
		if(stored!=null)
			dbId = stored.getDbId();
	}
	
	public final Object getDbId() {
		return dbId;
	}
	
	@Override // of IDbIdProvider
	public Object get() {
		return dbId;
	}

	public final void setDbId(Object dbId) {
		this.dbId = dbId;
	}
	
	
	@Override // of IDbIdListener
	public final void accept(Object dbId) {
		this.dbId = dbId;
	}
	
	public IStorable getStorable() {
		return storable;
	}
	
	public final Object getStorableData() {
		// this is called when storing the instance as a field value, so we just return the dbId
		// the instance data itself will be collected as part of collectStorables
		if(this.storable==null)
			throw new NotStorableError();
		else
			return this.getOrCreateDbId();
	}
	
	private Object getOrCreateDbId() throws NotStorableError {
		Object dbId = getDbId();
		if(dbId==null) {
			dbId = this.storable.getOrCreateDbId();
			setDbId(dbId);
		}
		return dbId;
	}

	/* not a great name, but avoids collision with field setters */
	protected final void setStorable(String name, Object value) {
		if(storable!=null)
			storable.setData(name, value, this);
	}
	
	@Override
	public boolean isMutable() {
		return mutable;
	}
	
	@Override
	public void setMutable(boolean mutable) {
		this.mutable = mutable;
	}
	
	@Override
	public void checkMutable() {
		if(!this.mutable) 
			PromptoException.throwEnumeratedException("NOT_MUTABLE");
	}
	
	@Override
	public void checkImmutable() {
		if(this.mutable) 
			PromptoException.throwEnumeratedException("NOT_MUTABLE");
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		// sb.append("{id:");
		// sb.append(System.identityHashCode(this));
		// sb.append(", ");
		List<Field> fields = collectFields();
		fields.forEach((field)-> {
			sb.append(field.getName());
			sb.append(':');
			try {
				field.setAccessible(true);
				sb.append(String.valueOf(field.get(this)));
				field.setAccessible(false);
			} catch (Exception e) {
				sb.append("<unreadable>");
			} 
			sb.append(", ");
		});
		if(sb.length()>1)
			sb.setLength(sb.length()-", ".length()); // remove trailing ", "
		sb.append('}');
		return sb.toString();
	}

	private List<Field> collectFields() {
		List<Field> list = new ArrayList<>();
		collectFields(list, this.getClass());
		return list;
	}

	private static Set<String> hiddenFields = new HashSet<>(Arrays.asList("category", "dbId", "storable", "mutable", "hiddenFields"));
	
	private void collectFields(List<Field> list, Class<?> klass) {
		if(Object.class==klass)
			return;
		collectFields(list, klass.getSuperclass());
		list.addAll(
				Arrays.asList(klass.getDeclaredFields())
				.stream()
				.filter((f)->
					!hiddenFields.contains(f.getName()))
				.collect(Collectors.toList()));
	}

	public void collectStorables(Consumer<IStorable> collector) {
		if(storable!=null && storable.isDirty()) {
			getOrCreateDbId();
			collector.accept(storable);
		}
	}
	
}
