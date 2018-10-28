package prompto.value;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Predicate;

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
import prompto.intrinsic.Filterable;
import prompto.intrinsic.IterableWithCounts;
import prompto.intrinsic.PromptoSet;
import prompto.runtime.Context;
import prompto.type.ContainerType;
import prompto.type.IType;
import prompto.type.SetType;

public class SetValue extends BaseValue implements IContainer<IValue>, IFilterable {

	PromptoSet<IValue> items = null;
	
	public SetValue(IType itemType) {
		super(new SetType(itemType));
		this.items = new PromptoSet<IValue>();
	}
	
	public SetValue(IType itemType, PromptoSet<IValue> items) {
		super(new SetType(itemType));
		this.items = items;
	}

	@Override
	public PromptoSet<IValue> getStorableData() {
		return items;
	}
	
	@Override
	public String toString() {
		return items.toString();
	}

	public IType getItemType() {
		return ((ContainerType)type).getItemType();
	}

	@Override
	public long getLength() {
		return items.size();
	}

	public PromptoSet<IValue> getItems() {
		return items;
	}
	
	@Override
	public boolean hasItem(Context context, IValue value) throws PromptoError {
		return items.contains(value);
	}

	@Override
	public IValue getItem(Context context, IValue index) throws PromptoError {
		if (index instanceof Integer) {
			int idx = (int)((Integer)index).longValue() - 1;
			return getNthItem(idx);
		} else
			throw new SyntaxError("No such item:" + index.toString());
	}
	
	public static ResultInfo compileItem(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression exp) {
		ResultInfo right = exp.compile(context, method, flags.withPrimitive(true));
		right = CompilerUtils.numberToint(method, right);
		// minus 1
		method.addInstruction(Opcode.ICONST_M1);
		method.addInstruction(Opcode.IADD);
		// create result
		IOperand oper = new MethodConstant(PromptoSet.class, "get", 
				int.class, Object.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, oper);
		return new ResultInfo(Object.class);
	}

	private IValue getNthItem(int idx) throws PromptoError {
		Iterator<? extends IValue> it = items.iterator();
		while(it.hasNext()) {
			IValue item = it.next();
			if(idx--==0)
				return item;
		}
		throw new IndexOutOfRangeError();
	}
	
	@Override
	public Filterable<IValue, IValue> getFilterable(Context context) {
		return new Filterable<IValue, IValue>() {
			@Override
			public IValue filter(Predicate<IValue> p) {
				PromptoSet<IValue> filtered = items.filter(p);
				return new SetValue(getItemType(), filtered);
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
	public boolean equals(Object obj) {
		if(!(obj instanceof SetValue))
			return false;
		return items.equals(((SetValue)obj).items);
	}

	public static ResultInfo compileEquals(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression exp) {
		exp.compile(context, method, flags);
		IOperand oper = new MethodConstant(
				PromptoSet.class, 
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
	
	@Override
	public IValue getMember(Context context, Identifier id, boolean autoCreate) {
		String name = id.toString();
		if ("count".equals(name))
			return new Integer(items.size());
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

	public static ResultInfo compilePlus(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression exp) {
		// TODO: return left if right is empty (or right if left is empty and is a set)
		// create result
		ResultInfo info = CompilerUtils.compileNewInstance(method, PromptoSet.class); 
		// add left, current stack is: left, result, we need: result, result, left
		method.addInstruction(Opcode.DUP_X1); // stack is: result, left, result
		method.addInstruction(Opcode.SWAP); // stack is: result, result, left
		IOperand oper = new MethodConstant(PromptoSet.class, "addAll", 
				Collection.class, boolean.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, oper);
		method.addInstruction(Opcode.POP); // consume returned boolean
		// add right, current stack is: result, we need: result, result, right
		method.addInstruction(Opcode.DUP); // stack is: result, result 
		exp.compile(context, method, flags); // stack is: result, result, right
		oper = new MethodConstant(PromptoSet.class, "addAll", 
				Collection.class, boolean.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, oper);
		method.addInstruction(Opcode.POP); // consume returned boolean
		return info;
	}
	
	public SetValue merge(Collection<? extends IValue> items) {
		PromptoSet<IValue> result = new PromptoSet<IValue>();
		result.addAll(this.items);
		result.addAll(items);
		IType itemType = ((SetType)getType()).getItemType();
		return new SetValue(itemType, result);
	}

}
