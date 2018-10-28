package prompto.value;

import prompto.intrinsic.PromptoDate;
import prompto.intrinsic.PromptoRange;
import prompto.type.DateType;



public class DateRange extends RangeBase<Date> {

	static class PromptoDateRange extends PromptoRange<Date> {

		public PromptoDateRange(prompto.value.Date low, prompto.value.Date high) {
			super(low, high);
		}
		
		@Override
		public prompto.value.Date getItem(long item) {
			PromptoDate result = low.value.plusDays(item-1);
			if(result.isAfter(high.value))
				throw new IndexOutOfBoundsException();
			return new prompto.value.Date(result);
		}
		
		@Override
		public PromptoDateRange slice(long first, long last) {
			last = adjustLastSliceIndex(last);
			return new PromptoDateRange(getItem(first), getItem(last));
		}

		@Override
		public long getNativeCount() {
			long h = high.value.toJavaTime();
			long l = low.value.toJavaTime();
			return 1 + ( (h-l)/(24*60*60*1000));
		}

		@Override
		public boolean contains(Object item) {
			if(!(item instanceof prompto.value.Date))
				return false;
			prompto.value.Date other = (prompto.value.Date)item;
			return other.compareTo(low)>=0 && high.compareTo(other)>=0;
		}

		
	}
	
	public DateRange(Date left, Date right) {
		this(new PromptoDateRange(left, right));
	}
	
	public DateRange(PromptoRange<Date> range) {
		super(DateType.instance(), range);
	}

	@Override
	public RangeBase<Date> newInstance(PromptoRange<Date> range) {
		return new DateRange(range);
	}

}
