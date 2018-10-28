package prompto.remoting;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import prompto.error.PromptoError;
import prompto.grammar.ArgumentAssignmentList;
import prompto.runtime.Context;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("serial")
public class ParameterList extends ArrayList<Parameter> {

	public ArgumentAssignmentList toAssignments(Context context) {
		return new ArgumentAssignmentList(
				this.stream()
				.map((param)->
					param.toAssignment(context))
				.collect(Collectors.toList()));
	}

	public Class<?>[] toJavaTypes(Context context, ClassLoader classLoader) {
		List<Class<?>> list = this.stream()
				.map((param)->
					param.toJavaType(context, classLoader))
				.collect(Collectors.toList());
		return list.toArray(new Class<?>[list.size()]);
	}

	public Object[] toJavaValues(Context context) {
		List<Object> list = this.stream()
				.map((param)->
					param.toJavaValue(context))
				.collect(Collectors.toList());
		return list.toArray(new Object[list.size()]);
	}

	public String toURLEncodedString(Context context) throws IOException, PromptoError {
		String jsonString = toJsonString(context);
		return URLEncoder.encode(jsonString, "UTF-8");
	}
	
	private String toJsonString(Context context) throws IOException, PromptoError {
		Writer writer = new StringWriter();
		JsonGenerator generator = new JsonFactory().createGenerator(writer);
		generator.writeStartArray();
		for(Parameter param : this)
			param.toJson(context, generator);
		generator.writeEndArray();
		generator.flush();
		generator.close();
		return writer.toString();
	}

	public static ParameterList read(Context context, String jsonParams, Map<String, byte[]> parts) throws Exception {
		JsonNode params = parseParams(jsonParams);
		return read(context, params, parts);
	}
	
	private static JsonNode parseParams(String jsonParams) throws Exception {
		if(jsonParams==null || jsonParams.isEmpty())
			return null;
		JsonParser parser = new ObjectMapper().getFactory().createParser(jsonParams);
		return parser.readValueAsTree();
	}

	public static ParameterList read(Context context, JsonNode jsonParams, Map<String, byte[]> parts) throws Exception {
		ParameterList params = new ParameterList();
		if(jsonParams==null)
			return params;
		if(!jsonParams.isArray())
			throw new InvalidParameterException("Expecting a JSON array!");
		for(JsonNode node : jsonParams)
			params.add(Parameter.read(context, node, parts));
		return params;
	}



}
