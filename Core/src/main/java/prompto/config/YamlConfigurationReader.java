package prompto.config;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import prompto.store.IStore;
import prompto.store.IStoreFactory;
import prompto.utils.Logger;

import com.esotericsoftware.yamlbeans.YamlReader;

public class YamlConfigurationReader implements IConfigurationReader {

	static Logger logger = new Logger();
	
	@SuppressWarnings("unchecked")
	static Map<String, Object> parseYaml(InputStream input) {
		try {
			YamlReader reader = new YamlReader(new InputStreamReader(input));
			return reader.read(Map.class);
		} catch(Throwable t) {
			throw new RuntimeException(t);
		}
	}

	Map<String, Object> data;
	
	public YamlConfigurationReader(InputStream input) {
		this(parseYaml(input));
	}
	
	@SuppressWarnings("unchecked")
	public YamlConfigurationReader(Map<String, Object> data) {
		/// TODO remove once yamlbeans is upgraded
		if(data.containsKey("<<")) {
			this.data = new HashMap<String, Object>();
			this.data.putAll((Map<String, Object>)data.get("<<"));
			this.data.putAll(data);
			this.data.remove("<<");
		} else
			this.data = data;
	}
	
	@Override
	public String toString() {
		return data.toString();
	}
	
	@Override
	public boolean hasKey(String key) {
		return data.containsKey(key);
	}
	
	@Override
	public String getString(String key) {
		Object value = data.get(key);
		return value==null ? null : value.toString();
	}
	
	@Override
	public Boolean getBoolean(String key) {
		Object value = data.get(key);
		return value==null ? null : Boolean.parseBoolean(value.toString());
	}
	
	@Override
	public Integer getInteger(String key) {
		Object value = data.get(key);
		return value==null ? null : Integer.parseInt(value.toString());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> Collection<T> getArray(String key) {
		Object value = data.get(key);
		if(value instanceof List)
			return (List<T>) value;
		else
			return null;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Collection<IConfigurationReader> getObjectsArray(String key) {
		Object value = data.get(key);
		if(value instanceof List) {
			return (Collection<IConfigurationReader>)((List)value).stream()
					.filter(o -> o instanceof Map)
					.map(o -> (Map<String, Object>)o)
					.map(m -> new YamlConfigurationReader((Map<String, Object>)m))
					.collect(Collectors.toList());
		} else
			return null;
	}
		
	@SuppressWarnings("unchecked")
	@Override
	public IConfigurationReader getObject(String key) {
		Object value = data.get(key);
		return value instanceof Map ? new YamlConfigurationReader((Map<String, Object>)value): null;
	}
	
	public static boolean checkStoreConnection(String yamlConfig) {
		try {
			logger.info(()->yamlConfig);
			IConfigurationReader reader = new YamlConfigurationReader(new ByteArrayInputStream(yamlConfig.getBytes()));
			IStoreConfiguration storeConfig = reader.readStoreConfiguration();
			IStore store = IStoreFactory.newStoreFromConfig(storeConfig);
			return store.checkConnection();
		} catch(Throwable t) {
			logger.error(()->"Failed to connect!", t);
			return false;
		}
	}
	
}
