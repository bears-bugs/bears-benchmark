package prompto.transpiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import prompto.store.AttributeInfo;
import prompto.store.Family;
import prompto.store.IQuery;
import prompto.store.IQueryBuilder;
import prompto.store.IQueryBuilder.MatchOp;
import prompto.store.memory.MemStore;
import prompto.store.memory.Query;
import prompto.store.IStorable;
import prompto.store.IStored;
import prompto.store.IStoredIterable;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

@SuppressWarnings("restriction")
public class MemStoreMirror {
	
	MemStore store = new MemStore();
	ValueConverter converter;
	
	public MemStoreMirror(ScriptEngine nashorn) {
		this.converter = new ValueConverter(nashorn);
	}

	public StorableMirror newStorableDocument(List<String> categories) {
		IStorable storable = store.newStorable(categories, null);
		return new StorableMirror(storable);
	}
	
	public void store(JSSet toDelete, JSSet toStore) {
		Set<Object> del = toDelete==null ? null : toDelete.getSet();
		Set<IStorable> add = toStore==null ? null : toStore.getSet().stream().map(item->((StorableMirror)item).getStorable()).collect(Collectors.toSet());
		store.store(del, add);
	}
	
	public void flush() {
		store.flush();
	}
	
	public Object fetchOne(Query query) {
		IStored stored = store.fetchOne(query);
		return stored==null ? null : new StoredMirror(stored);
	}
	
	public Object fetchMany(Query query) {
		IStoredIterable iterable = store.fetchMany(query);
		return new StoredIterableMirror(iterable);
	}

	public MemQueryBuilderMirror newQueryBuilder() {
		return new MemQueryBuilderMirror();
	}
	
	public class MemQueryBuilderMirror {
		
		IQueryBuilder builder = store.newQueryBuilder();
		
		public void verify(ScriptObjectMirror fieldInfo, ScriptObjectMirror matchOp, Object value) {
			AttributeInfo info = AttributeInfoReader.read(fieldInfo);
			MatchOp match = MatchOpReader.read(matchOp);
			value = converter.fromJS(value);
			builder.verify(info, match, value);
		}
		
		public void and() {
			builder.and();
		}
		
		public void or() {
			builder.or();
		}

		public void not() {
			builder.not();
		}

		public IQuery build() {
			return builder.build();
		}
		
		public void addOrderByClause(ScriptObjectMirror fieldInfo, boolean descending) {
			AttributeInfo info = AttributeInfoReader.read(fieldInfo);
			builder.orderBy(info, descending);
		}
		
		public void setFirst(long first) {
			builder.first(first);
		}
		
		public void setLast(long last) {
			builder.last(last);
		}

	}
	
	class ValueConverter {

		ScriptEngine nashorn;
		
		public ValueConverter(ScriptEngine nashorn) {
			this.nashorn = nashorn;
		}


		@SuppressWarnings("unchecked")
		public Object fromJS(Object value) {
			if(value instanceof ScriptObjectMirror)
				value = fromScriptObjectMirror((ScriptObjectMirror)value);
			if(value==null || value instanceof Boolean || value instanceof Integer || value instanceof Double || value instanceof String)
				return value;
			else if(value instanceof StorableMirror)
				return ((StorableMirror)value).getStorable();
			else if(value instanceof StoredMirror)
				return ((StoredMirror)value).getStored();
			else if(value instanceof List)
				return ((List<Object>)value).stream().map(this::fromJS).collect(Collectors.toList());
			else if(value instanceof Map) {
				Map<String, Object> mapResult = new HashMap<>();
				((Map<String, Object>)value).forEach((k,v)->{
					if(!isFunction(v))
						mapResult.put(k, toJS(v));
				});
				return mapResult;
			} else
				throw new UnsupportedOperationException(value.getClass().getName());
		}
		
		public Object fromScriptObjectMirror(ScriptObjectMirror value) {
			if(value.isArray()) {
				return fromJSArray(value);
			} else if(value.getClassName().equals("Object")) {
				String className = getClassName(value);
				if("List".equals(className)) 
					return fromJSList(value);
				else if("Set".equals(className))
					return fromJSSet(value);
				else
					return fromJSObject(value);
			} else
				throw new UnsupportedOperationException(value.getClassName());
			
		}

	
		private Object fromJSArray(ScriptObjectMirror value) {
			List<Object> listResult = new ArrayList<Object>();
			value.forEach((k,v)->{
				if(!isFunction(v))
					listResult.add(fromJS(v));
			});
			return listResult;
		}


		private Object fromJSList(ScriptObjectMirror value) {
			List<Object> listResult = new ArrayList<Object>();
			value.forEach((k,v)->{
				if(!isFunction(v) && !"length".equals(k) && !"mutable".equals(k))
					listResult.add(fromJS(v));
			});
			return listResult;
		}


		private Object fromJSSet(ScriptObjectMirror value) {
			throw new UnsupportedOperationException();
		}


		private Object fromJSObject(ScriptObjectMirror value) {
			Map<String, Object> mapResult = new HashMap<>();
			value.forEach((k,v)->{
				if(!isFunction(v))
					mapResult.put(k, fromJS(v));
			});
			return mapResult;
		}


		private String getClassName(ScriptObjectMirror value) {
			value = (ScriptObjectMirror)value.getMember("constructor");
			return (String)value.getMember("name");
		}


		private boolean isFunction(Object v) {
			return v instanceof ScriptObjectMirror && ((ScriptObjectMirror)v).isFunction();
		}


		@SuppressWarnings("unchecked")
		public Object toJS(Object value) {
			if(value==null || value instanceof Boolean || value instanceof Integer || value instanceof Long || value instanceof Double || value instanceof String || value instanceof StoredMirror || value instanceof StorableMirror)
				return value;
			else if(value instanceof IStored)
				return new StoredMirror((IStored)value);
			else if(value instanceof IStorable)
				return new StorableMirror((IStorable)value);
			else if(value instanceof List) {
				ScriptObjectMirror array = newArray();
				AtomicInteger index = new AtomicInteger();
				((List<Object>)value).forEach(item->array.setSlot(index.getAndIncrement(), toJS(item)));
				return array;
			} else if(value instanceof Map) {
				ScriptObjectMirror object = newObject();
				((Map<String,Object>)value).forEach((k,v)->object.setMember(k, toJS(v)));
				return object;
			} else
				throw new UnsupportedOperationException(value.getClass().getName());
		}
		

		public ScriptObjectMirror newObject() {
			return safeEval("({})");
		}
		
		private ScriptObjectMirror newArray() {
			return safeEval("[]");
		}
		
		private ScriptObjectMirror safeEval(String script) {
			try {
				return (ScriptObjectMirror)nashorn.eval(script);
			} catch(ScriptException e) {
				throw new RuntimeException(e);
			}
		}
			
	}

	static abstract class MatchOpReader {

		public static MatchOp read(ScriptObjectMirror matchOp) {
			String name = (String)matchOp.getMember("name");
			return MatchOp.valueOf(name);
		}
	}
	
	static abstract class AttributeInfoReader {

		public static AttributeInfo read(ScriptObjectMirror fieldInfo) {
			String name = readName(fieldInfo);
			Family family = readFamily(fieldInfo);
			Boolean collection = readCollection(fieldInfo);
			return new AttributeInfo(name, family, collection, false, false, false);
		}

		private static String readName(ScriptObjectMirror fieldInfo) {
			return (String)fieldInfo.getMember("name");
		}

		private static Family readFamily(ScriptObjectMirror fieldInfo) {
			Object family = fieldInfo.getMember("family");
			if(family instanceof ScriptObjectMirror)
				family = ((ScriptObjectMirror)family).get("name");
			return Family.valueOf(family.toString());
		}
		
		private static Boolean readCollection(ScriptObjectMirror fieldInfo) {
			return (Boolean)fieldInfo.getMember("collection");
		}

	}
	
	public class StoredMirror {

		IStored stored;
		
		public StoredMirror(IStored stored) {
			this.stored = stored;
		}
		
		public IStored getStored() {
			return stored;
		}
		
		public Object getData(String name) {
			Object data = stored.getData(name);
			return converter.toJS(data);
		}
		
	}
	
	public class StorableMirror {
		
		IStorable storable;
		
		public StorableMirror(IStorable storable) {
			this.storable = storable;
		}

		public IStorable getStorable() {
			return storable;
		}
		
		public Object getOrCreateDbId() {
			return storable.getOrCreateDbId();
		}
		
		public void setData(String name, Object value) {
			storable.setData(name, converter.fromJS(value));
		}
		
	}
	
	public class StoredIterableMirror {

		IStoredIterable iterable;
		Iterator<IStored> iterator;
		
		public StoredIterableMirror(IStoredIterable iterable) {
			this.iterable = iterable;
			this.iterator = iterable.iterator();
		}
		
		public long count() {
			return iterable.count();
		}
		
		public long totalCount() {
			return iterable.totalCount();
		}

		public boolean hasNext() {
			return iterator.hasNext();
		}
		
		public Object next() {
			return converter.toJS(iterator.next());
		}
	}


}
