package prompto.value;

import java.io.IOException;
import java.util.Map;

import prompto.error.PromptoError;
import prompto.error.ReadWriteError;
import prompto.grammar.Identifier;
import prompto.runtime.Context;
import prompto.type.UUIDType;

import com.fasterxml.jackson.core.JsonGenerator;

public class UUIDValue extends BaseValue {

	java.util.UUID value;
	
	public UUIDValue(String value) {
		super(UUIDType.instance());
		this.value = java.util.UUID.fromString(value);
	}

	public UUIDValue(java.util.UUID value) {
		super(UUIDType.instance());
		this.value = value;
	}

	public java.util.UUID getValue() {
		return value;
	}
	
	@Override
	public java.util.UUID getStorableData() {
		return value;
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
	
	@Override
	public void toJson(Context context, JsonGenerator generator, Object instanceId, Identifier fieldName, boolean withType, Map<String, byte[]> data) throws PromptoError {
		try {
			if(withType) {
				generator.writeStartObject();
				generator.writeFieldName("type");
				generator.writeString(UUIDType.instance().getTypeName());
				generator.writeFieldName("value");
				generator.writeString(this.toString());
				generator.writeEndObject();
			} else
				generator.writeString(this.toString());
		} catch(IOException e) {
			throw new ReadWriteError(e.getMessage());
		}
	}
	
}
