package prompto.value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import prompto.compiler.CompilerUtils;
import prompto.compiler.Flags;
import prompto.compiler.IOperand;
import prompto.compiler.MethodConstant;
import prompto.compiler.MethodInfo;
import prompto.compiler.Opcode;
import prompto.compiler.ResultInfo;
import prompto.error.IndexOutOfRangeError;
import prompto.error.PromptoError;
import prompto.error.SyntaxError;
import prompto.expression.IExpression;
import prompto.grammar.Identifier;
import prompto.intrinsic.IterableWithCounts;
import prompto.intrinsic.PromptoTuple;
import prompto.literal.Literal;
import prompto.runtime.Context;
import prompto.store.InvalidValueError;
import prompto.type.TupleType;
import prompto.utils.CodeWriter;

public class TupleValue extends BaseValue implements IContainer<IValue>, ISliceable<IValue>  {

	protected PromptoTuple<IValue> items;
	
	public TupleValue(boolean mutable) {
		super(TupleType.instance());
		this.items = new PromptoTuple<>(mutable);
	}
	
	public TupleValue(PromptoTuple<IValue> items) {
		super(TupleType.instance());
		this.items = items;
	}
	
	public TupleValue(Collection<IValue> items, boolean mutable) {
		super(TupleType.instance());
		this.items = new PromptoTuple<IValue>(items, mutable);
	}

	
	@Override
	public boolean isMutable() {
		return items.isMutable();
	}
	
	@Override
	public PromptoTuple<IValue> getStorableData() {
		return items;
	}
	
	@Override
	public String toString() {
		String result = items.toString();
		return "(" + result.substring(1,result.length()-1) + ")";
	}
	
	public void addItem(IValue item) {
		items.add(item);
	}
	
	public PromptoTuple<IValue> getItems() {
		return items;
	}
	
	public IValue getItem(int index) {
		return items.get(index);
	}
	
	public void setItem(int index, IValue element) {
		items.set(index, element);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof TupleValue))
			return false;
		return items.equals(((TupleValue)obj).items);
	}

	public void toDialect(CodeWriter writer) {
		writer.append('(');
		if(items.size()>0) {
			for(Object o : items) {
				if(o instanceof Literal<?>)
					((Literal<?>)o).toDialect(writer);
				else
					writer.append(o.toString());
				writer.append(", ");
			}
			writer.trimLast(2);
		}
		writer.append(')');
	}
	
	@Override
	public TupleValue slice(Integer fi, Integer li) throws IndexOutOfRangeError {
		long _fi = fi == null ? 1L : fi.longValue();
		if (_fi < 0)
			throw new IndexOutOfRangeError();
		long _li = li == null ? items.size() : li.longValue();
		if (_li > items.size())
			throw new IndexOutOfRangeError();
		PromptoTuple<IValue> sliced = items.slice(_fi, _li); // 1 based
		return new TupleValue(sliced, false);
	}
	
	@Override
	public long getLength() {
		return items.size();
	}

	@Override
	public boolean hasItem(Context context, IValue lval) throws PromptoError {
		return this.items.contains(lval); // TODO interpret before
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
	public IValue plus(Context context, IValue value) throws PromptoError {
        if (value instanceof ListValue)
            return this.merge(((ListValue)value).getItems());
        else if (value instanceof TupleValue)
            return this.merge(((TupleValue)value).getItems());
        else if (value instanceof SetValue)
            return this.merge(((SetValue)value).getItems());
        else
            throw new SyntaxError("Illegal: " + this.type.getTypeName() + " + " + value.getClass().getSimpleName());
    }
	
	protected TupleValue merge(Collection<? extends IValue> items) {
		PromptoTuple<IValue> result = new PromptoTuple<IValue>(false);
		result.addAll(this.items);
		result.addAll(items);
		return new TupleValue(result);
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
	public IValue getMember(Context context, Identifier id, boolean autoCreate) throws PromptoError {
		String name = id.toString();
		if ("count".equals(name))
			return new Integer(items.size());
		else
			return super.getMember(context, id, autoCreate);
	}
	
	
	public static ResultInfo compileSlice(Context context, MethodInfo method, Flags flags, 
			ResultInfo parent, IExpression first, IExpression last) {
		compileSliceFirst(context, method, flags, first);
		compileSliceLast(context, method, flags, last);
		MethodConstant m = new MethodConstant(PromptoTuple.class, "slice", 
				long.class, long.class, PromptoTuple.class);
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
		IOperand oper = new MethodConstant(PromptoTuple.class, "get", 
				int.class, Object.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, oper);
		return new ResultInfo(Object.class);
	}

	@Override
	public int compareTo(Context context, IValue value) throws PromptoError {
		if(!(value instanceof TupleValue))
			return super.compareTo(context, value);
		return compareTo(context, (TupleValue)value, new ArrayList<java.lang.Boolean>());
	}

	public int compareTo(Context context, TupleValue other, Collection<java.lang.Boolean> directions) throws PromptoError {
		Iterator<java.lang.Boolean> iterDirs = directions.iterator();
		Iterator<IValue> iterThis = this.items.iterator();
		Iterator<IValue> iterOther = other.items.iterator();
		while(iterThis.hasNext() && iterOther.hasNext()) {
			boolean descending = iterDirs.hasNext() ? iterDirs.next() : false;
			// compare items
			IValue thisVal = iterThis.next();
			IValue otherVal = iterOther.next();
			if(thisVal==null && otherVal==null)
				continue;
			else if(thisVal==null)
				return descending ? 1 : -1;
			else if(otherVal==null)
				return descending ? -1 : 1;
			int cmp = thisVal.compareTo(context, otherVal);
			// if not equal, done
			if(cmp!=0) {
				return descending ? -cmp : cmp;
			}
		}
		boolean descending = iterDirs.hasNext() ? iterDirs.next() : false;
		if(iterThis.hasNext())
			return descending ? -1 : 1;
		else if(iterOther.hasNext())
			return descending ? 1 : -1;
		else
			return 0;
	}

	public static ResultInfo compilePlus(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression exp) {
		// TODO: return left if right is empty (or right if left is empty and is a list)
		// create result
		ResultInfo info = CompilerUtils.compileNewRawInstance(method, PromptoTuple.class);
		method.addInstruction(Opcode.DUP);
		method.addInstruction(Opcode.ICONST_0); // not mutable
		CompilerUtils.compileCallConstructor(method, PromptoTuple.class, boolean.class);
		// add left, current stack is: left, result, we need: result, result, left
		method.addInstruction(Opcode.DUP_X1); // stack is: result, left, result
		method.addInstruction(Opcode.SWAP); // stack is: result, result, left
		IOperand oper = new MethodConstant(PromptoTuple.class, "addAll", 
				Collection.class, boolean.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, oper);
		method.addInstruction(Opcode.POP); // consume returned boolean
		// add right, current stack is: result, we need: result, result, right
		method.addInstruction(Opcode.DUP); // stack is: result, result 
		exp.compile(context, method, flags); // stack is: result, result, right
		oper = new MethodConstant(PromptoTuple.class, "addAll", 
				Collection.class, boolean.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, oper);
		method.addInstruction(Opcode.POP); // consume returned boolean
		return info;
	}
}
