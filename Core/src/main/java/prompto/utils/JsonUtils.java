package prompto.utils;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class JsonUtils {

	public static JsonNode readResource(String resource) throws IOException {
		try(InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource)) {
			return new ObjectMapper().readTree(input);
		}
	}

}
