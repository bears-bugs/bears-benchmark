package prompto.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import prompto.store.IStore;
import prompto.store.IStored;
import prompto.utils.Logger;

public class StoredRecordConfigurationReader implements IConfigurationReader  {

	static final Logger logger = new Logger();

	private static IStored fetchStored(IStore store, Object dbId) {
		IStored stored = store.fetchUnique(store.convertToDbId(dbId));
		if(stored==null)
			logger.error(()->"No record found with dbId: " + dbId.toString());
		return stored;
	}
	
	
	IStore store;
	IStored stored;
	
	public StoredRecordConfigurationReader(IStore store, Object dbId) {
		this(store, fetchStored(store, dbId));
	}

	public StoredRecordConfigurationReader(IStore store, IStored stored) {
		this.store = store;
		this.stored = stored;
	}

	@Override
	public boolean hasKey(String key) {
		return stored.hasData(key);
	}

	@Override
	public Boolean getBoolean(String key) {
		Object value = stored.getRawData(key);
		return value instanceof Boolean ? (Boolean)value : null;
	}

	@Override
	public String getString(String key) {
		Object value = stored.getRawData(key);
		return value==null ? null : value.toString();
	}

	@Override
	public Integer getInteger(String key) {
		Object value = stored.getRawData(key);
		return value instanceof Number ? ((Number)value).intValue() : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Collection<T> getArray(String key) {
		Object value = stored.getRawData(key);
		return value instanceof Collection ? (Collection<T>)value : null;
	}

	@Override
	public StoredRecordConfigurationReader getObject(String key) {
		Object value = stored.getRawData(key);
		if(value==null)
			return null;
		if(!store.getDbIdClass().isInstance(value)) {
			logger.warn(()->"Not a valid dbId: " + value.toString());
			return null;
		}
		return new StoredRecordConfigurationReader(store, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<StoredRecordConfigurationReader> getObjectsArray(String key) {
		Object value = stored.getRawData(key);
		if(value==null)
			return null;
		if(!(value instanceof Collection)) {
			logger.warn(()->"Not a collection: " + value.toString());
			return null;
		}
		List<StoredRecordConfigurationReader> readers = new ArrayList<>();
		for(Object item : (Collection<Object>)value) {
			if(!store.getDbIdClass().isInstance(item)) {
				logger.warn(()->"Not a valid dbId: " + value.toString());
				return null;
			}
			readers.add(new StoredRecordConfigurationReader(store, item));
		}
		return readers;
	}

	public String readCategory() {
		Object value = stored.getRawData("category");
		if(!(value instanceof List)) {
			logger.warn(()->"Not a valid category list: " + value.toString());
			return null;
		}
		@SuppressWarnings("unchecked")
		List<String> list = (List<String>)value;
		return list.get(list.size()-1);
	}


}
