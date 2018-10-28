package prompto.type;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;

import prompto.error.PromptoError;
import prompto.expression.IExpression;
import prompto.grammar.Identifier;
import prompto.intrinsic.PromptoDocument;
import prompto.runtime.Context;
import prompto.store.Family;
import prompto.transpiler.Transpiler;
import prompto.value.Document;
import prompto.value.IValue;
import prompto.value.NullValue;

import com.fasterxml.jackson.databind.JsonNode;

public class DocumentType extends NativeType {
	
	static DocumentType instance = new DocumentType();
	
	public static DocumentType instance() {
		return instance;
	}
	
	private DocumentType() {
		super(Family.DOCUMENT);
	}

	@Override
	public boolean isMoreSpecificThan(Context context, IType other) {
		if(other instanceof NullType || other instanceof AnyType || other instanceof MissingType)
			return true;
		else
			return super.isMoreSpecificThan(context, other);
	}
	
	@Override
	public boolean isAssignableFrom(Context context, IType other) {
		return super.isAssignableFrom(context, other)
				|| other==AnyType.instance()
				|| (other instanceof CategoryType && "Any".equals(other.getTypeName()));
	}
	
	@Override
	public Type getJavaType(Context context) {
		return PromptoDocument.class;
	}
	
	@Override
	public IType checkItem(Context context, IType itemType) {
		if(itemType==TextType.instance())
			return AnyType.instance();
		else
			return super.checkItem(context, itemType);
	}

	@Override
	public IType checkMember(Context context, Identifier name) {
		return AnyType.instance();
	}
	
	@Override
	public IValue readJSONValue(Context context, JsonNode value, Map<String, byte[]> parts) {
		Document instance = new Document();
		Iterator<Map.Entry<String, JsonNode>> fields = value.fields();
		while(fields.hasNext()) {
			Map.Entry<String, JsonNode> field = fields.next();
			IValue item = readJSONField(context, field.getValue(), parts);
			instance.setMember(new Identifier(field.getKey()), item);
		}
		return instance;
	}

	private IValue readJSONField(Context context, JsonNode fieldData, Map<String, byte[]> parts) throws PromptoError {
		if(fieldData==null || fieldData.isNull())
			return NullValue.instance();
		else if(fieldData.isBoolean())
			return prompto.value.Boolean.valueOf(fieldData.asBoolean());
		else if(fieldData.isInt() || fieldData.isLong())
			return new prompto.value.Integer(fieldData.asLong());
		else if(fieldData.isFloat() || fieldData.isDouble())
			return new prompto.value.Decimal(fieldData.asDouble());
		else if(fieldData.isTextual())
			return new prompto.value.Text(fieldData.asText());
		else if(fieldData.isArray()) {
			throw new UnsupportedOperationException();
		} else if(fieldData.isObject()) {
			throw new UnsupportedOperationException();
		} else
			throw new UnsupportedOperationException();
	}
	
	@Override
	public IValue convertJavaValueToIValue(Context context, Object value) {
		if(value instanceof PromptoDocument)
			return new Document(context, (PromptoDocument<?,?>)value);
		else
			return super.convertJavaValueToIValue(context, value);
	}
	
	
	@Override
	public void declare(Transpiler transpiler) {
		transpiler.require("Document");
	}
	
	@Override
	public void declareMember(Transpiler transpiler, String name) {
		// nothing to do
	}
	
	
	@Override
	public void transpileMember(Transpiler transpiler, String name) {
	    if (!"text".equals(name)) {
	        transpiler.append(name);
	    } else {
	        transpiler.append("getText()");
	    }
	}
	
	@Override
	public void transpileAssignMember(Transpiler transpiler, String name) {
		transpiler.append(".getMember('").append(name).append("', true)");
	}
	
	
	@Override
	public void transpileAssignMemberValue(Transpiler transpiler, String name, IExpression expression) {
	    transpiler.append(".setMember('").append(name).append("', ");
	    expression.transpile(transpiler);
	    transpiler.append(")");
	}
	
	
	@Override
	public void transpileItem(Transpiler transpiler, IType itemType, IExpression item) {
	    transpiler.append(".item(");
	    item.transpile(transpiler);
	    transpiler.append(")");
	}
	
	@Override
	public void transpileAssignItemValue(Transpiler transpiler, IExpression item, IExpression expression) {
	    transpiler.append(".setItem(");
	    item.transpile(transpiler);
	    transpiler.append(", ");
	    expression.transpile(transpiler);
	    transpiler.append(")");
	}
		
}
