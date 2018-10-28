package prompto.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

import com.esotericsoftware.yamlbeans.YamlReader;

public abstract class YamlUtils {

	static final Logger logger = new Logger();

	public static Map<String, Object> readResource(String resource) throws IOException {
		return readResource(()->Thread.currentThread().getContextClassLoader().getResourceAsStream(resource));
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> readResource(InputStreamSupplier supplier) throws IOException {
		try(InputStream input = supplier.getInputStream()) {
			try(Reader reader = new InputStreamReader(input)) {
				YamlReader yaml = new YamlReader(reader);
				Object read = yaml.read();
				if(read instanceof Map)
					return (Map<String, Object>)read;
				else {
					logger.error(()->"Expected Map<String, Object> got: " + read.getClass().getName());
					return null;
				}
			}
		}
	}

}
