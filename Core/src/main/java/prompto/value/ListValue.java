package prompto.value;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.IOperand;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.error.IndexOutOfRangeError;
import prompto.error.PromptoError;
import prompto.error.ReadWriteError;
import prompto.error.SyntaxError;
import prompto.expression.IExpression;
import prompto.grammar.Identifier;
import prompto.intrinsic.Filterable;
import prompto.intrinsic.IterableWithCounts;
import prompto.intrinsic.PromptoList;
import prompto.java.JavaClassType;
import prompto.runtime.Context;
import prompto.store.IStorable;
import prompto.store.InvalidValueError;
import prompto.type.AnyType;
import prompto.type.ContainerType;
import prompto.type.IType;
import prompto.type.ListType;

import com.fasterxml.jackson.core.JsonGenerator;

public class ListValue extends BaseValue implements IContainer<IValue>, ISliceable<IValue>, IFilterable  {

	PromptoList<IValue> items;
	List<Object> storables;
	
	public ListValue(IType itemType) {
		super(new ListType(itemType));
		this.items = new PromptoList<>(false);
	}
	
	public ListValue(IType itemType, PromptoList<IValue> items) {
		super(new ListType(itemType));
		this.items = items;
	}

	public ListValue(IType itemType, Collection<? extends IValue> items) {
		super(new ListType(itemType));
		this.items = new PromptoList<>(items, false);
	}
	
	public ListValue(IType itemType, Collection<? extends IValue> items, boolean mutable) {
		super(new ListType(itemType));
		this.items = new PromptoList<>(items, mutable);
	}

	public ListValue(Context context, PromptoList<?> list) {
		super(new ListType(AnyType.instance()));
		List<IValue> items = list.stream()
				.map((item)->JavaClassType.convertJavaValueToPromptoValue(context, item, item.getClass(), AnyType.instance()))
				.collect(Collectors.toList());
		this.items = new PromptoList<>(items, false);
	}

	@Override
	public boolean isMutable() {
		return items.isMutable();
	}
	
	@Override
	public Object getStorableData() {
		if(storables==null) {
			storables = new ArrayList<>();
			for(IValue item : items)
				storables.add(item.getStorableData());
		}
		return storables;
	}
	
	@Override
	public void collectStorables(Consumer<IStorable> collector) {
		items.forEach((value)->
			value.collectStorables(collector));
	}
	
	@Override
	public String toString() {
		return items.toString();
	}

	public IType getItemType() {
		return ((ContainerType)type).getItemType();
	}

	public PromptoList<IValue> getItems() {
		return items;
	}
	
	public void addItem(IValue item) {
		items.add(item);
	}
	
	public IValue getItem(int index) {
		return items.get(index);
	}
	
	public void setItem(int index, IValue element) {
		items.set(index, element);
	}
	
	@Override
	public void setItem(Context context, IValue item, IValue value) {
		if(!(item instanceof Integer))
			throw new InvalidValueError("Expected an Integer, got:" + item.getClass().getName());
		int index = (int)((Integer)item).longValue();
		if(index<1 || index>this.getLength())
			throw new IndexOutOfRangeError();
		this.setItem(index-1, value);
	}
	
	@Override
	public IValue getItem(Context context, IValue index) throws PromptoError {
		if (index instanceof Integer) {
			try {
				int idx = (int)((Integer)index).longValue() - 1;
				return items.get(idx);
			} catch (IndexOutOfBoundsException e) {
				throw new IndexOutOfRangeError();
			}

		} else
			throw new SyntaxError("No such item:" + index.toString());
	}

	@Override
	public boolean hasItem(Context context, IValue lval) throws PromptoError {
		return this.items.contains(lval); // TODO interpret before
	}

	@Override
	public long getLength() {
		return items.size();
	}

	@Override
	public Filterable<IValue,IValue> getFilterable(Context context) {
		return new Filterable<IValue, IValue>() {
			@Override
			public IValue filter(Predicate<IValue> p) {
				PromptoList<IValue> filtered = items.filter(p);
				return new ListValue(getItemType(), filtered);
			}
		};
	}
	
	@Override
	public IterableWithCounts<IValue> getIterable(Context context) {
		return new IterableWithCounts<IValue>() {
			@Override
			public Long getCount() {
				return (long)items.size();
			}
			@Override
			public Long getTotalCount() {
				return (long)items.size();
			}
			@Override
			public Iterator<IValue> iterator() {
				return items.iterator();
			}
		};
	}
	
	@Override
	public IValue getMember(Context context, Identifier id, boolean autoCreate) {
		String name = id.toString();
		if ("count".equals(name))
			return new Integer(items.size());
		else if ("iterator".equals(name))
			return new IteratorValue(getItemType(), getIterable(context).iterator());
		else
			return super.getMember(context, id, autoCreate);
	}
	
	@Override
	public IValue plus(Context context, IValue value) {
        if (value instanceof ListValue)
            return this.merge(((ListValue)value).getItems());
        else if (value instanceof SetValue)
            return this.merge(((SetValue)value).getItems());
        else
            throw new SyntaxError("Illegal: " + this.type.getTypeName() + " + " + value.getClass().getSimpleName());
    }
	
	protected ListValue merge(Collection<? extends IValue> items) {
		PromptoList<IValue> result = new PromptoList<IValue>(false);
		result.addAll(this.items);
		result.addAll(items);
		IType itemType = ((ListType)getType()).getItemType();
		return new ListValue(itemType, result);
	}
	

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ListValue))
			return false;
		return items.equals(((ListValue)obj).items);
	}
	
	public static ResultInfo compileEquals(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression exp) {
		exp.compile(context, method, flags);
		IOperand oper = new MethodConstant(
				PromptoList.class, 
				"equals",
				Object.class, boolean.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, oper);
		if(flags.isReverse()) 
			CompilerUtils.reverseBoolean(method);
		if(flags.toPrimitive())
			return new ResultInfo(boolean.class);
		else
			return CompilerUtils.booleanToBoolean(method);
	}
	
	public static ResultInfo compilePlus(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression exp) {
		// TODO: return left if right is empty (or right if left is empty and is a list)
		// create result
		ResultInfo info = CompilerUtils.compileNewRawInstance(method, PromptoList.class);
		method.addInstruction(Opcode.DUP);
		method.addInstruction(Opcode.ICONST_0); // not mutable
		CompilerUtils.compileCallConstructor(method, PromptoList.class, boolean.class);
		// add left, current stack is: left, result, we need: result, result, left
		method.addInstruction(Opcode.DUP_X1); // stack is: result, left, result
		method.addInstruction(Opcode.SWAP); // stack is: result, result, left
		IOperand oper = new MethodConstant(PromptoList.class, "addAll", 
				Collection.class, boolean.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, oper);
		method.addInstruction(Opcode.POP); // consume returned boolean
		// add right, current stack is: result, we need: result, result, right
		method.addInstruction(Opcode.DUP); // stack is: result, result 
		exp.compile(context, method, flags); // stack is: result, result, right
		oper = new MethodConstant(PromptoList.class, "addAll", 
				Collection.class, boolean.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, oper);
		method.addInstruction(Opcode.POP); // consume returned boolean
		return info;
	}
	
	@Override
	public IValue multiply(Context context, IValue value) throws PromptoError {
		if (value instanceof Integer) {
			IType itemType = ((ContainerType)this.type).getItemType();
			int count = (int) ((Integer) value).longValue();
			if (count < 0)
				throw new SyntaxError("Negative repeat count:" + count);
			return new ListValue(itemType, this.items.multiply(count));
		} else
			throw new SyntaxError("Illegal: List * " + value.getClass().getSimpleName());
	}
	
	@Override
	public ListValue slice(Integer fi, Integer li) throws IndexOutOfRangeError {
		long _fi = fi == null ? 1L : fi.longValue();
		if (_fi < 0)
			throw new IndexOutOfRangeError();
		long _li = li == null ? items.size() : li.longValue();
		if (_li > items.size())
			throw new IndexOutOfRangeError();
		PromptoList<IValue> sliced = items.slice(_fi, _li); // 1 based
		return new ListValue(this.getItemType(), sliced);
	}

	public static ResultInfo compileSlice(Context context, MethodInfo method, Flags flags, 
			ResultInfo parent, IExpression first, IExpression last) {
		compileSliceFirst(context, method, flags, first);
		compileSliceLast(context, method, flags, last);
		MethodConstant m = new MethodConstant(PromptoList.class, "slice", 
				long.class, long.class, PromptoList.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, m);
		return parent;
	}

	public static ResultInfo compileItem(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression exp) {
		ResultInfo right = exp.compile(context, method, flags.withPrimitive(true));
		right = CompilerUtils.numberToint(method, right);
		// minus 1
		method.addInstruction(Opcode.ICONST_M1);
		method.addInstruction(Opcode.IADD);
		// create result
		IOperand oper = new MethodConstant(PromptoList.class, "get", 
				int.class, Object.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, oper);
		return new ResultInfo(Object.class); // TODO refine
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
			for(IValue value : this.items)
				value.toJson(context, generator, System.identityHashCode(this), null, withType, data);
			generator.writeEndArray();
			if(withType)
				generator.writeEndObject();
		} catch(IOException e) {
			throw new ReadWriteError(e.getMessage());
		}
	}
	
	@Override
	public Object convertTo(Context context, Type type) {
		if(type==PromptoList.class) {
			Type itemType = this.getItemType().getJavaType(context);
			PromptoList<Object> result = new PromptoList<>(true);
			items.forEach((item)->result.add(item.convertTo(context, itemType)));
			return result;
		} else
			return super.convertTo(context, type);
	}

}
