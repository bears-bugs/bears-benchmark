package prompto.value;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;

import prompto.error.PromptoError;
import prompto.error.ReadWriteError;
import prompto.grammar.Identifier;
import prompto.runtime.Context;
import prompto.type.NullType;


public class NullValue extends BaseValue {

	static NullValue instance = new NullValue();
	
	public static NullValue instance() {
		return instance;
	}
	
	private NullValue() {
		super(NullType.instance());
	}
	
	@Override
	public String toString() {
		return "null";
	}
	
	@Override
	public Object getStorableData() {
		return null; // YES! you read correctly
	}
	
	
	@Override
	public Object convertTo(Context context, Type type) {
		return null; // YES! you read correctly
	}
	
	@Override
	public void toJson(Context context, JsonGenerator generator, Object instanceId, Identifier fieldName, boolean withType, Map<String, byte[]> data) throws PromptoError {
		try {
			generator.writeNull();
		} catch(IOException e) {
			throw new ReadWriteError(e.getMessage());
		}
	}

}
