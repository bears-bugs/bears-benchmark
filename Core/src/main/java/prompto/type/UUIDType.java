package prompto.type;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;

import prompto.runtime.Context;
import prompto.store.Family;
import prompto.value.IValue;

import com.fasterxml.jackson.databind.JsonNode;

public class UUIDType extends NativeType {

	static UUIDType instance = new UUIDType();
	
	public static UUIDType instance() {
		return instance;
	}
	
	private UUIDType() {
		super(Family.UUID);
	}
	
	@Override
	public Type getJavaType(Context context) {
		return UUID.class;
	}
	
	@Override
	public IValue readJSONValue(Context context, JsonNode value, Map<String, byte[]> parts) {
		return new prompto.value.UUIDValue(value.asText());
	}
	
	@Override
	public IValue convertJavaValueToIValue(Context context, Object value) {
		return new prompto.value.UUIDValue((UUID)value);
	}
}