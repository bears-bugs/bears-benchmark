package prompto.remoting;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.InvalidParameterException;
import java.util.Map;

import prompto.argument.CategoryArgument;
import prompto.argument.IArgument;
import prompto.error.PromptoError;
import prompto.grammar.ArgumentAssignment;
import prompto.grammar.Identifier;
import prompto.parser.ECleverParser;
import prompto.runtime.Context;
import prompto.store.DataStore;
import prompto.store.IStore;
import prompto.type.IType;
import prompto.utils.TypeUtils;
import prompto.value.ExpressionValue;
import prompto.value.IValue;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;

public class Parameter {

	public static Parameter read(Context context, JsonNode jsonParam, Map<String, byte[]> parts) throws Exception {
		if(!jsonParam.isObject())
			throw new InvalidParameterException("Expecting a JSON object!");
		Parameter param = new Parameter();
		JsonNode field = jsonParam.get("name");
		if(field==null)
			throw new InvalidParameterException("Expecting a 'name' field!");
		param.setName(field.asText());
		// dbId type resolves to Any category, when it's actually a value, need a hack for this one
		if(IStore.dbIdName.equals(param.getName()))
			param.setType(TypeUtils.typeToIType(DataStore.getInstance().getDbIdClass()));
		else {
			field = jsonParam.get("type");
			if(field==null)
				throw new InvalidParameterException("Expecting a 'type' field!");
			param.setType(getType(context, field.asText()));
		}
		field = jsonParam.get("value");
		if(field==null)
			throw new InvalidParameterException("Expecting a 'value' field!");
		param.setValue(new ExpressionValue(param.getType(), param.getType().readJSONValue(context, field, parts)));
		return param;
	}

	private static IType getType(Context context, String typeName) throws Exception {
		if(Character.isUpperCase(typeName.charAt(0)))
			return new ECleverParser(typeName).parse_standalone_type();
		else
			return context.findAttribute(typeName).getType();
	}


	private String name;
	private IType type;
	private IValue value;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public IType getType() {
		return type;
	}

	public void setType(IType type) {
		this.type = type;
	}

	public IValue getValue() {
		return value;
	}

	public void setValue(IValue value) {
		this.value = value;
	}

	public Class<?> toJavaType(Context context, ClassLoader classLoader) {
		Type type = this.type.getJavaType(context);
		if(type instanceof Class<?>)
			return (Class<?>)type;
		else try {
			return classLoader.loadClass(type.getTypeName().replace('.', '/'));
		} catch(ClassNotFoundException e) {
			throw new InternalError(e);
		}
	}
	
	public Object toJavaValue(Context context) {
		return value.convertTo(context, Object.class);
	}
	
	public ArgumentAssignment toAssignment(Context context) {
		IArgument argument = new CategoryArgument(type, new Identifier(name));
		return new ArgumentAssignment(argument, new ExpressionValue(type, value));
	}

	public void toJson(Context context, JsonGenerator generator) throws IOException, PromptoError {
		generator.writeStartObject();
		generator.writeStringField("name", name.toString());
		generator.writeStringField("type", type.toString());
		if(value==null)
			generator.writeNullField("value");
		else {
			generator.writeFieldName("value");
			value.toJson(context, generator, null, null, true, null);
		}
		generator.writeEndObject();
	}


}
