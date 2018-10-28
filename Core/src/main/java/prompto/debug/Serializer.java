package prompto.debug;

import java.io.InputStream;
import java.io.OutputStream;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Serializer {

	static ObjectMapper mapper = initMapper();
	
	private static ObjectMapper initMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
		mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
		return mapper;
	}

	static class DebugRequestMessage {
		IDebugRequest.Type type;
		IDebugRequest object;
		
		public IDebugRequest.Type getType() {
			return type;
		}
		
		public IDebugRequest getObject() {
			return object;
		}
	}
	
	public static void writeDebugRequest(OutputStream output, IDebugRequest request) throws Exception {
		DebugRequestMessage message = new DebugRequestMessage();
		message.type = request.getType();
		message.object = request;
		mapper.writeValue(output, message);
		output.flush();
	}
	
	public static IDebugRequest readDebugRequest(InputStream input) throws Exception {
		JsonNode content = mapper.readTree(input);
		String typeName = content.get("type").asText();
		IDebugRequest.Type type = IDebugRequest.Type.valueOf(typeName);
		return mapper.convertValue(content.get("object"), type.getKlass());
	}

	static class DebugResponseMessage {
		IDebugResponse.Type type;
		IDebugResponse object;
		
		public IDebugResponse.Type getType() {
			return type;
		}
		
		public IDebugResponse getObject() {
			return object;
		}
	}
	

	public static void writeDebugResponse(OutputStream output, IDebugResponse response) throws Exception {
		DebugResponseMessage message = new DebugResponseMessage();
		message.type = response.getType();
		message.object = response;
		mapper.writeValue(output, message);
	}

	public static IDebugResponse readDebugResponse(InputStream input) throws Exception {
		JsonNode content = mapper.readTree(input);
		String typeName = content.get("type").asText();
		IDebugResponse.Type type = IDebugResponse.Type.valueOf(typeName);
		return mapper.convertValue(content.get("object"), type.getKlass());
	}


	static class DebugEventMessage {
		IDebugEvent.Type type;
		IDebugEvent object;
		
		public IDebugEvent.Type getType() {
			return type;
		}
		
		public IDebugEvent getObject() {
			return object;
		}
	}
	
	public static void writeDebugEvent(OutputStream output, IDebugEvent event) throws Exception {
		DebugEventMessage message = new DebugEventMessage();
		message.type = event.getType();
		message.object = event;
		mapper.writeValue(output, message);
	}

	public static IDebugEvent readDebugEvent(InputStream input) throws Exception {
		JsonNode content = mapper.readTree(input);
		String typeName = content.get("type").asText();
		IDebugEvent.Type type = IDebugEvent.Type.valueOf(typeName);
		return mapper.convertValue(content.get("object"), type.getKlass());
	}
	
	static class AcknowledgementMessage {
		IAcknowledgement.Type type;
		
		public IAcknowledgement.Type getType() {
			return type;
		}
	}
	
	
	public static void writeAcknowledgement(OutputStream output, IAcknowledgement ack) throws Exception {
		AcknowledgementMessage message = new AcknowledgementMessage();
		message.type = ack.getType();
		mapper.writeValue(output, message);
	}

	public static IAcknowledgement readAcknowledgement(InputStream input) throws Exception {
		JsonNode content = mapper.readTree(input);
		String typeName = content.get("type").asText();
		IAcknowledgement.Type type = IAcknowledgement.Type.valueOf(typeName);
		return type.getKlass().newInstance();
	}

}
