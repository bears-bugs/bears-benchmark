package prompto.grammar;

import java.io.IOException;
import java.util.Map;

import prompto.error.PromptoError;
import prompto.error.ReadWriteError;
import prompto.expression.Symbol;
import prompto.intrinsic.IterableWithCounts;
import prompto.runtime.Context;
import prompto.type.IType;
import prompto.utils.IValueIterable;
import prompto.utils.ObjectList;
import prompto.value.IIterable;
import prompto.value.ISliceable;
import prompto.value.IValue;

import com.fasterxml.jackson.core.JsonGenerator;

@SuppressWarnings("serial")
public abstract class SymbolList <T extends Symbol> extends ObjectList<T> implements IValue, IIterable<IValue> {
	
	IType type;
	
	protected SymbolList() {
	}

	protected SymbolList(T symbol) {
		this.add(symbol);
	}

	@Override
	public Object getStorableData() {
		throw new UnsupportedOperationException(); // can't be stored
	}

	@Override
	public boolean isMutable() {
		return false;
	}
	
	public void setType(IType type) {
		this.type = type;
	}
	
	@Override
	public IType getType() {
		return type;
	}
	
	@Override
	public IterableWithCounts<IValue> getIterable(Context context) {
		return new IValueIterable<>(context, this);
	}
	
	
	public IValue plus(Context context, IValue value) throws PromptoError {
		throw new UnsupportedOperationException("Add not supported by " + this.getClass().getSimpleName());
	}

	public IValue minus(Context context, IValue value) throws PromptoError {
		throw new UnsupportedOperationException("Subtract not supported by " + this.getClass().getSimpleName());
	}

	public IValue multiply(Context context, IValue value) throws PromptoError {
		throw new UnsupportedOperationException("Multiply not supported by " + this.getClass().getSimpleName());
	}

	public IValue divide(Context context, IValue value) throws PromptoError {
		throw new UnsupportedOperationException("Divide not supported by " + this.getClass().getSimpleName());
	}

	public IValue intDivide(Context context, IValue value) throws PromptoError {
		throw new UnsupportedOperationException("Integer divide not supported by " + this.getClass().getSimpleName());
	}

	public IValue modulo(Context context, IValue value) throws PromptoError {
		throw new UnsupportedOperationException("Integer divide not supported by " + this.getClass().getSimpleName());
	}

	public int compareTo(Context context, IValue value) throws PromptoError {
		throw new UnsupportedOperationException("Compare not supported by " + this.getClass().getSimpleName());
	}

	@Override
	public void setMember(Context context, Identifier name, IValue value) throws PromptoError {
		throw new UnsupportedOperationException("No member support for " + this.getClass().getSimpleName());
	}

	@Override
	public IValue getMember(Context context, Identifier name, boolean autoCreate) throws PromptoError {
		throw new UnsupportedOperationException("No member support for " + this.getClass().getSimpleName());
	}

	public Object convertTo(Class<?> type) {
		return this;
	}
	
	@Override
	public boolean roughly(Context context, IValue value) throws PromptoError {
		return this.equals(value);
	}
	
	@Override
	public ISliceable<IValue> asSliceable(Context context) throws PromptoError {
		return null;
	}
	
	@Override
	public void toJson(Context context, JsonGenerator generator, Object instanceId, Identifier fieldName, boolean withType, Map<String, byte[]> data) throws PromptoError {
		try {
			if(withType) {
				generator.writeStartObject();
				generator.writeFieldName("type");
				generator.writeString(this.getType().getTypeName());
				generator.writeFieldName("value");
			}
			generator.writeStartArray();
			for(Symbol symbol : this)
				symbol.toJson(context, generator, System.identityHashCode(this), null, withType, data);
			generator.writeEndArray();
			if(withType)
				generator.writeEndObject();
		} catch(IOException e) {
			throw new ReadWriteError(e.getMessage());
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(Symbol s : this) {
			sb.append(s.getName());
			sb.append(", ");
		}
		sb.setLength(sb.length() - ", ".length());
		sb.append("]");
		return sb.toString();
	}
	
	public boolean hasSymbol(String name) {
		for(T symbol : this) {
			if(name.equals(symbol.getName()))
				return true;
		}
		return false;
	}

}
