package prompto.value;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import prompto.error.PromptoError;
import prompto.error.ReadWriteError;
import prompto.error.SyntaxError;
import prompto.grammar.Identifier;
import prompto.intrinsic.Filterable;
import prompto.intrinsic.IterableWithCounts;
import prompto.intrinsic.PromptoList;
import prompto.runtime.Context;
import prompto.store.IStored;
import prompto.store.IStoredIterable;
import prompto.type.CategoryType;
import prompto.type.CursorType;
import prompto.type.IType;
import prompto.type.IterableType;
import prompto.type.ListType;

import com.fasterxml.jackson.core.JsonGenerator;

public class Cursor extends BaseValue implements IIterable<IValue>, IterableWithCounts<IValue>, IFilterable {

	Context context;
	IStoredIterable iterable;
	boolean mutable;
	
	public Cursor(Context context, IType itemType, IStoredIterable documents) {
		super(new CursorType(itemType));
		this.context = context;
		this.iterable = documents;
		this.mutable = itemType instanceof CategoryType ? ((CategoryType)itemType).isMutable() : false;
	}

	@Override
	public Object getStorableData() {
		throw new UnsupportedOperationException(); // can't be stored
	}
	
	
	public IType getItemType() {
		return ((CursorType)getType()).getItemType();
	}

	
	@Override
	public Long getCount() {
		return iterable.count();
	}
	
	@Override
	public Long getTotalCount() {
		return iterable.totalCount();
	}

	@Override
	public IterableWithCounts<IValue> getIterable(Context context) {
		return this;
	}
	
	@Override 
	public Iterator<IValue> iterator() {
		return new Iterator<IValue>() {
			
			Iterator<IStored> iterator = iterable.iterator();
			
			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}
			
			@Override
			public IValue next() {
				try {
					IStored stored = iterator.next();
					CategoryType itemType = readItemType(stored);
					return itemType.newInstance(context, stored);
				} catch (PromptoError e) {
					throw new RuntimeException(e);
				}
			}
		};
	}
	
	@SuppressWarnings("unchecked")
	private CategoryType readItemType(IStored stored) throws PromptoError {
		Object value = stored.getData("category");
		if(value instanceof List<?>) {
			List<String> categories = (List<String>)value;
			String category = categories.get(categories.size()-1);
			CategoryType type = new CategoryType(new Identifier(category));
			type.setMutable(this.mutable);
			return type;
		} else
			return (CategoryType) ((IterableType)type).getItemType();
	}

	@Override
	public IValue getMember(Context context, Identifier id, boolean autoCreate) {
		String name = id.toString();
		if ("count".equals(name))
			return new Integer(getCount());
		else if ("totalCount".equals(name))
			return new Integer(getTotalCount());
		else
			throw new SyntaxError("No such member:" + name);
	}

	@Override
	public void toJson(Context context, JsonGenerator generator, Object instanceId, Identifier fieldName, boolean withType, Map<String, byte[]> data) {
		try {
			if(withType) {
				generator.writeStartObject();
				generator.writeFieldName("type");
				// serialize Cursor as list
				IType type = new ListType(getItemType());
				generator.writeString(type.getTypeName());
				generator.writeFieldName("count");
				generator.writeNumber(iterable.count());
				generator.writeFieldName("totalCount");
				generator.writeNumber(iterable.totalCount());
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

	@Override
	public Filterable<IValue, IValue> getFilterable(Context context) {
		return new Filterable<IValue, IValue>() {
			@Override
			public IValue filter(Predicate<IValue> predicate) {
				PromptoList<IValue> filtered = new PromptoList<IValue>(false);
				for(IValue value : getIterable(context)) {
					if(predicate.test(value))
						filtered.add(value);
				}
				return new ListValue(getItemType(), filtered);
			}
		};
	}
	
	public IValue toListValue() {
		ListValue values = new ListValue(this.getItemType());
		for(IValue item : this)
			values.addItem(item);
		return values;
	}



}
