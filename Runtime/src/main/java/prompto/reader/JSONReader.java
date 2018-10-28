package prompto.reader;

import java.io.IOException;
import java.io.InputStream;

import prompto.intrinsic.PromptoConverter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class JSONReader {
	
	public static Object read(String json) throws IOException {
		try {
			JsonNode node = new ObjectMapper().readTree(json);
			return PromptoConverter.nodeToPrompto(node);
		} catch(RuntimeException e) {
			throw new IOException(e.getMessage());
		}
	}

	public static Object read(InputStream input) throws IOException {
		try {
			JsonNode node = new ObjectMapper().readTree(input);
			return PromptoConverter.nodeToPrompto(node);
		} catch(RuntimeException e) {
			throw new IOException(e.getMessage());
		}
	}

}
