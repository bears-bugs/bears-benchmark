package prompto.value;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import prompto.error.PromptoError;
import prompto.error.ReadWriteError;
import prompto.error.SyntaxError;
import prompto.expression.IExpression;
import prompto.grammar.Identifier;
import prompto.intrinsic.IterableWithCounts;
import prompto.runtime.Context;
import prompto.runtime.Variable;
import prompto.type.IType;
import prompto.type.IteratorType;
import prompto.type.ListType;

import com.fasterxml.jackson.core.JsonGenerator;

public class IterableValue extends BaseValue implements IIterable<IValue>, IterableWithCounts<IValue> {

	IType sourceType;
	Context context;
	Identifier name;
	IterableWithCounts<IValue> iterable;
	IExpression expression;
	
	public IterableValue(Context context, Identifier name, IType sourceType, 
			IterableWithCounts<IValue> iterable, IExpression expression, IType resultType) {
		super(new IteratorType(resultType));
		this.sourceType = sourceType;
		this.context = context;
		this.name = name;
		this.iterable = iterable;
		this.expression = expression;
	}

	@Override
	public Long getCount() {
		return iterable.getCount();
	}
	
	@Override
	public Long getTotalCount() {
		return iterable.getTotalCount();
	}

	@Override
	public IterableWithCounts<IValue> getIterable(Context context) {
		return this;
	}

	@Override 
	public Iterator<IValue> iterator() {
		return new Iterator<IValue>() {
			
			Iterator<IValue> iterator = iterable.iterator();
			
			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}
			
			@Override
			public IValue next() {
				try {
					Context child = context.newChildContext();
					child.registerValue(new Variable(name, sourceType));
					child.setValue(name, iterator.next());
					return expression.interpret(child);
				} catch (PromptoError e) {
					throw new RuntimeException(e);
				}
			}
		};
	}
	
	@Override
	public IValue getMember(Context context, Identifier id, boolean autoCreate) throws PromptoError {
		String name = id.toString();
		if ("count".equals(name))
			return new Integer(iterable.getCount());
		else
			throw new SyntaxError("No such member:" + name);
	}

	@Override
	public void toJson(Context context, JsonGenerator generator, Object instanceId, Identifier fieldName, boolean withType, Map<String, byte[]> data) throws PromptoError {
		try {
			if(withType) {
				generator.writeStartObject();
				generator.writeFieldName("type");
				// serialize Cursor as list
				IType type = new ListType(((IteratorType)getType()).getItemType());
				generator.writeString(type.getTypeName());
				generator.writeFieldName("value");
			}
			generator.writeStartArray();
			Iterator<IValue> iter = iterator();
			while(iter.hasNext())
				iter.next().toJson(context, generator, null, null, withType, data);
			generator.writeEndArray();
			if(withType)
				generator.writeEndObject();
		} catch(IOException e) {
			throw new ReadWriteError(e.getMessage());
		}
	}

	
}
