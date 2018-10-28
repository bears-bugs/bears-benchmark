package prompto.value;

import prompto.intrinsic.PromptoRange;
import prompto.type.IntegerType;


public class IntegerRange extends RangeBase<Integer> {

	static class PromptoIntegerRange extends PromptoRange<Integer> {

		public PromptoIntegerRange(Integer low, Integer high) {
			super(low, high);
		}
		
		@Override
		public Integer getItem(long item) {
			java.lang.Long result = low.longValue() + item - 1;
			if(result>high.longValue())
				throw new IndexOutOfBoundsException();
			return new Integer(result);
		}
		
		@Override
		public long getNativeCount() {
			return 1L + high.longValue() - low.longValue();
		}
			
		@Override
		public PromptoIntegerRange slice(long first, long last) {
			last = adjustLastSliceIndex(last);
			return new PromptoIntegerRange(getItem(first), getItem(last));
		}

		@Override
		public boolean contains(Object item) {
			if(!(item instanceof prompto.value.Integer))
				return false;
			prompto.value.Integer other = (prompto.value.Integer)item;
			return other.compareTo(low)>=0 && high.compareTo(other)>=0;
		}

	}
	
	public IntegerRange(Integer left, Integer right) {
		this(new PromptoIntegerRange(left, right));
	}

	public IntegerRange(PromptoRange<Integer> range) {
		super(IntegerType.instance(), range);
	}

	@Override
	public long getLength() {
		return 1 + getHigh().longValue() - getLow().longValue();
	}

	@Override
	public RangeBase<Integer> newInstance(PromptoRange<Integer> range) {
		return new IntegerRange(range);
	}
	

}
