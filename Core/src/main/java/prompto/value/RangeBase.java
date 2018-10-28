package prompto.value;

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
import prompto.intrinsic.IterableWithCounts;
import prompto.intrinsic.PromptoRange;
import prompto.runtime.Context;
import prompto.type.IType;
import prompto.type.RangeType;

public abstract class RangeBase<T extends IValue> extends BaseValue implements IContainer<T>, IRange<T> {
	
	PromptoRange<T> range;
	
	protected RangeBase(IType type, PromptoRange<T> range) {
		super(new RangeType(type));
		this.range = range;
	}
	
	@Override
	public Object getStorableData() {
		return range;
	}
	
	@Override
	public String toString() {
		return range.toString();
	}
	
	public T getLow() {
		return range.getLow();
	}
	
	public T getHigh() {
		return range.getHigh();
	}

	@Override
	public long getLength() {
		return range.getNativeCount();
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof RangeBase && range.equals(((RangeBase<?>)obj).range);
	}
		
	public static ResultInfo compileEquals(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression exp) {
		exp.compile(context, method, flags);
		IOperand oper = new MethodConstant(
				PromptoRange.class, 
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

	public boolean hasItem(Context context, IValue lval) {
		return range.contains(lval);
	}
	
	public T getItem(Context context, IValue index) throws PromptoError {
		if (index instanceof Integer) {
			try {
				return range.getItem(((Integer) index).longValue());
			} catch (IndexOutOfBoundsException e) {
				throw new IndexOutOfRangeError();
			}
		} else
			throw new SyntaxError("No such item:" + index.toString());
		  			
	}
	
	public static ResultInfo compileItem(Context context, MethodInfo method, Flags flags, 
			ResultInfo left, IExpression exp) {
		ResultInfo right = exp.compile(context, method, flags.withPrimitive(true));
		right = CompilerUtils.numberTolong(method, right);
		// create result
		IOperand oper = new MethodConstant(PromptoRange.class, "getItem", 
				long.class, Object.class);
		method.addInstruction(Opcode.INVOKEVIRTUAL, oper);
		return new ResultInfo(Object.class);
	}

	public RangeBase<T> slice(Integer fi, Integer li) throws PromptoError {
		try {
			long _fi = fi==null ? 1L : fi.longValue();
			long _li = li==null ? -1L : li.longValue();
			PromptoRange<T> sliced = range.slice(_fi, _li);
			return newInstance(sliced);
		} catch (IndexOutOfBoundsException e) {
			throw new IndexOutOfRangeError();
		}
	}

	public static ResultInfo compileSlice(Context context, MethodInfo method, Flags flags, 
			ResultInfo parent, IExpression first, IExpression last) {
		compileSliceFirst(context, method, flags, first);
		compileSliceLast(context, method, flags, last);
		MethodConstant m = new MethodConstant(parent.getType(), "slice", 
				long.class, long.class, parent.getType());
		method.addInstruction(Opcode.INVOKEVIRTUAL, m);
		return parent;
	}

	@Override
	public IterableWithCounts<T> getIterable(Context context) {
		return new RangeIterable(context);
	}	
	
	public abstract RangeBase<T> newInstance(PromptoRange<T> range);

	class RangeIterable implements IterableWithCounts<T> {
		
		Context context;
		
		public RangeIterable(Context context) {
			this.context = context;
		}

		@Override
		public Long getCount() {
			return RangeBase.this.getLength();
		}
		
		@Override
		public Long getTotalCount() {
			return RangeBase.this.getLength();
		}
		
		@Override
		public Iterator<T> iterator() {
			return new Iterator<T>() {
		
				long index = 0L;
				long length = RangeBase.this.getLength();
				
				@Override
				public boolean hasNext() {
					return index<length;
				}
		
				@Override
				public T next() {
					try {
						return getItem(context, new Integer(++index));
					} catch(Throwable t) {
						throw new InternalError(t.getMessage());
					}
				}
		
				@Override
				public void remove() {
					throw new RuntimeException("Shold never get there!");
				}
			};
			
		}
	}
	


}
