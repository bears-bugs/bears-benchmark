package prompto.store.memory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import prompto.error.PromptoError;
import prompto.intrinsic.PromptoBinary;
import prompto.intrinsic.PromptoList;
import prompto.intrinsic.PromptoTuple;
import prompto.store.AttributeInfo;
import prompto.store.IQuery;
import prompto.store.IQueryBuilder;
import prompto.store.IStorable;
import prompto.store.IStorable.IDbIdListener;
import prompto.store.IStore;
import prompto.store.IStored;
import prompto.store.IStoredIterable;

/* a utility class for running unit tests only */
public final class MemStore implements IStore {

	private Map<Long, StorableDocument> documents = new HashMap<>();
	private AtomicLong lastDbId = new AtomicLong(0);
	private Map<String, AttributeInfo> attributes = new HashMap<>();
	
	@Override
	public boolean checkConnection() {
		return true;
	}
	
	@Override
	public Class<?> getDbIdClass() {
		return Long.class;
	}
	
	@Override
	public Object newDbId() {
		return Long.valueOf(lastDbId.incrementAndGet());
	}
	
	@Override
	public Long convertToDbId(Object dbId) {
		if(dbId instanceof Long)
			return (Long)dbId;
		else
			return Long.decode(String.valueOf(dbId));
	}
	
	@Override
	public AttributeInfo getAttributeInfo(String name) {
		return attributes.get(name);
	}
	
	@Override
	public void createOrUpdateAttributes(Collection<AttributeInfo> infos) {
		infos.forEach(i->attributes.put(i.getName(), i));
	}
	
	@Override
	public void store(Collection<?> dbIds, Collection<IStorable> storables) throws PromptoError {
		if(dbIds!=null) 
			delete(dbIds);
		if(storables!=null)
			store(storables);
	}

	public void store(Collection<IStorable> storables) throws PromptoError {
		for(IStorable storable : storables) {
			if(!(storable instanceof StorableDocument))
				throw new IllegalStateException("Expecting a StorableDocument");
			store((StorableDocument)storable);
		}
	}

	public void store(StorableDocument storable) throws PromptoError {
		// ensure db id
		Object dbId = storable.getData(dbIdName);
		if(!(dbId instanceof Long)) {
			dbId = Long.valueOf(lastDbId.incrementAndGet());
			storable.setData(dbIdName, dbId);
		}
		documents.put((Long)dbId, storable);
	}
	
	@Override
	public void delete(Collection<?> dbIds) throws PromptoError {
		for(Object dbId : dbIds)
			documents.remove(dbId);
	}
	
	@Override
	public void deleteAll() throws PromptoError {
		documents = new HashMap<>();
	}
	
	@Override
	public PromptoBinary fetchBinary(Object dbId, String attr) {
		if(!(dbId instanceof Long))
			dbId = Long.decode(dbId.toString());
		StorableDocument doc = documents.get(dbId);
		if(doc==null)
			return null;
		else
			return (PromptoBinary)doc.getData(attr);
	}
	
	@Override
	public IStored fetchUnique(Object dbId) throws PromptoError {
		return documents.get(dbId);
	}
	
	
	@Override
	public IQueryBuilder newQueryBuilder() {
		return new QueryBuilder();
	}
	
	@Override
	public IStored fetchOne(IQuery query) throws PromptoError {
		Query q = (Query)query;
		for(StorableDocument doc : documents.values()) {
			if(doc.matches(q.getPredicate()))
				return doc;
		}
		return null;
	}
	
	@Override
	public IStoredIterable fetchMany(IQuery query) throws PromptoError {
		Query q = (Query)query;
		final List<StorableDocument> allDocs = fetchManyDocs(q);
		final List<StorableDocument> slicedDocs = slice(q, allDocs);
		return new IStoredIterable() {
			@Override
			public long count() {
				return (long)slicedDocs.size();
			}
			@Override
			public long totalCount() {
				return (long)allDocs.size(); 
			}
			@SuppressWarnings("unchecked")
			@Override
			public Iterator<IStored> iterator() {
				return (Iterator<IStored>)(Object)slicedDocs.iterator();
			};
		};
	}
	
	private List<StorableDocument> fetchManyDocs(Query query) throws PromptoError {
		List<StorableDocument> docs = filterDocs((query).getPredicate());
		docs = sort(query.getOrdering(), docs);
		return docs;
	}

	private List<StorableDocument> filterDocs(IPredicate predicate) throws PromptoError {
		// create list of filtered docs
		List<StorableDocument> docs = new ArrayList<StorableDocument>();
		List<StorableDocument> all = new ArrayList<>(documents.values()); // need a copy to avoid concurrent modification
		for(StorableDocument doc : all) {
			if(doc.matches(predicate))
				docs.add(doc);
		}
		return docs;
	}
	
	private List<StorableDocument> slice(Query query, List<StorableDocument> docs) {
		if(docs==null || docs.isEmpty())
			return docs;
		Long first = query.getFirst();
		Long last = query.getLast();
		if(first==null && last==null)
			return docs;
		if(first==null || first<1)
			first = 1L;
		if(last==null || last>docs.size())
			last = new Long(docs.size());
		if(first > last)
			return new ArrayList<StorableDocument>();
		return docs.subList(first.intValue() - 1, last.intValue());
	}

	private List<StorableDocument> sort(Collection<IOrderBy> orderBy, List<StorableDocument> docs) {
		if(orderBy==null || orderBy.isEmpty() || docs.size()<2)
			return docs;
		List<java.lang.Boolean> directions = orderBy.stream().map((o)->
				o.isDescending()).collect(Collectors.toList());
		docs.sort(new Comparator<StorableDocument>() {

			@Override
			public int compare(StorableDocument o1, StorableDocument o2) {
				try {
					PromptoTuple<Comparable<?>> v1 = readTuple(o1, orderBy);
					PromptoTuple<Comparable<?>> v2 = readTuple(o2, orderBy);
					return v1.compareTo(v2, directions);
				} catch (PromptoError e) {
					throw new RuntimeException(e);
				}
			}

		});
		return docs;
	}
	
	private PromptoTuple<Comparable<?>> readTuple(StorableDocument doc, Collection<IOrderBy> orderBy) throws PromptoError {
		PromptoTuple<Comparable<?>> tuple = new PromptoTuple<>(false);
		orderBy.forEach((o)->
			tuple.add((Comparable<?>)doc.getData(o.getAttributeInfo().getName())));
		return tuple;
	}

	
	@Override
	public IStorable newStorable(List<String> categories, IDbIdListener listener) {
		return new StorableDocument(categories, listener);
	}
	
	
	@Override
	public void flush() throws PromptoError {
		// nothing to do
	}
	
	class StorableDocument implements IStored, IStorable {

		Map<String, Object> document = null;
		IDbIdListener listener;
		List<String> categories;
		
		public StorableDocument(List<String> categories, IDbIdListener listener) {
			this.categories = categories;
			this.listener = listener;
		}

		public boolean matches(IPredicate predicate) {
			if(predicate==null)
				return true;
			else
				return predicate.matches(document);
		}
		
		@Override
		public void setCategories(String[] categories) throws PromptoError {
			this.categories = Arrays.asList(categories);
		}
		
		@Override
		public List<String> getCategories() {
			return categories;
		}

		
		@Override
		public void setDbId(Object dbId) {
			setDirty(true);
			document.put(dbIdName, dbId);
		}
		
		@Override
		public Object getDbId() {
			return getData(dbIdName);
		}
		
		@Override
		public Object getOrCreateDbId() {
			Object dbId = getData(dbIdName);
			if(dbId==null) {
				setDirty(true);
				dbId = Long.valueOf(lastDbId.incrementAndGet());
				document.put(dbIdName, dbId);
			}
			return dbId;
		}
		
		@Override
		public void setDirty(boolean set) {
			if(!set)
				document = null;
			else if(document==null)
				document = newDocument(null);
		}

		private Map<String, Object> newDocument(Object dbId) {
			Map<String, Object> doc = new HashMap<>();
			if(categories!=null) {
				PromptoList<String> value = new PromptoList<>(false);
				for(String name : categories)
					value.add(name);
				doc.put("category", value);
			}
			if(dbId==null)
				dbId = Long.valueOf(lastDbId.incrementAndGet());
			doc.put(dbIdName, dbId);
			if(listener!=null)
				listener.accept(dbId);
			return doc;
		}

		@Override
		public boolean isDirty() {
			return document!=null;
		}
		
		
		@Override
		public boolean hasData(String name) {
			if(document==null)
				return false;
			else
				return document.containsKey(name);
		}

		@Override
		public Object getRawData(String fieldName) {
			return getData(fieldName);
		}
		
		@Override
		public Object getData(String fieldName) {
			if(document==null)
				return null;
			else
				return document.get(fieldName);
		}
		
		@Override
		public void setData(String fieldName, Object value, IDbIdProvider provider) {
			if(document==null) {
				Object dbId = provider==null ? null : provider.get();
				document = newDocument(dbId);
			}
			document.put(fieldName, value);
		}
		
		@Override
		public Set<String> getNames() throws PromptoError {
			if(document==null)
				return Collections.emptySet();
			else {
				Set<String> names = document.keySet();
				names.remove("category");
				names.remove("dbId");
				return names;
			}
		}
		
	}

	
}
