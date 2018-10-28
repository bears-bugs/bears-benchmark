package prompto.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CmdLineConfigurationReader implements IConfigurationReader {

	Map<String, String> argsMap;
	
	public CmdLineConfigurationReader(Map<String, String> argsMap) {
		this.argsMap = argsMap;
	}
	
	@Override
	public String toString() {
		return argsMap.toString();
	}
	
	@Override
	public boolean hasKey(String key) {
		return argsMap.containsKey(key);
	}

	@Override
	public Boolean getBoolean(String key) {
		String value = argsMap.get(key);
		return value==null ? null : Boolean.valueOf(value);
	}

	@Override
	public String getString(String key) {
		return argsMap.get(key);
	}

	@Override
	public Integer getInteger(String key) {
		String value = argsMap.get(key);
		return value==null ? null : Integer.parseInt(value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Collection<T> getArray(String key) {
		String value = argsMap.get(key);
		if(value==null)
			return null;
		return (List<T>)Arrays.asList(value.split(","));
	}

	@Override
	public IConfigurationReader getObject(String key) {
		String prefix = key + "-";
		Map<String, String> subMap = argsMap
				.entrySet()
				.stream()
				.filter((e)->e.getKey().startsWith(prefix))
				.collect(Collectors.toMap(e->e.getKey().substring(prefix.length()), e->e.getValue()));
		return subMap.isEmpty() ? null : new CmdLineConfigurationReader(subMap);
	}
	
	@Override
	public Collection<IConfigurationReader> getObjectsArray(String key) {
		throw new UnsupportedOperationException();
	}

}
