package prompto.type;

import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.Map;

import prompto.runtime.Context;
import prompto.store.Family;
import prompto.transpiler.Transpiler;
import prompto.value.Boolean;
import prompto.value.IValue;

import com.fasterxml.jackson.databind.JsonNode;

public class BooleanType extends NativeType {
	
	static BooleanType instance = new BooleanType();
	
	public static BooleanType instance() {
		return instance;
	}
	
	private BooleanType() {
		super(Family.BOOLEAN);
	}

	@Override
	public Type getJavaType(Context context) {
		return java.lang.Boolean.class;
	}
	
	@Override
	public Comparator<? extends IValue> getComparator(boolean descending) {
		return descending ?
				new Comparator<Boolean>() {
					@Override
					public int compare(Boolean o1, Boolean o2) {
						return java.lang.Boolean.compare(o2.getValue(), o1.getValue());
					}
				} :
				new Comparator<Boolean>() {
					@Override
					public int compare(Boolean o1, Boolean o2) {
						return java.lang.Boolean.compare(o1.getValue(), o2.getValue());
					}
				};
	}
	
	@Override
	public IValue convertJavaValueToIValue(Context context, Object value) {
        if (value instanceof java.lang.Boolean)
            return Boolean.valueOf(((java.lang.Boolean)value).booleanValue());
        else
        	return super.convertJavaValueToIValue(context, value);
	}
	
	@Override
	public IValue readJSONValue(Context context, JsonNode value, Map<String, byte[]> parts) {
		return prompto.value.Boolean.valueOf(value.asBoolean());
	}
	
	@Override
	public void declare(Transpiler transpiler) {
		transpiler.require("Utils"); // equals etc...
	}
	
}
