package prompto.value;

import prompto.intrinsic.PromptoRange;
import prompto.intrinsic.PromptoTime;
import prompto.type.TimeType;



public class TimeRange extends RangeBase<Time> {

	static class PromptoTimeRange extends PromptoRange<Time> {

		public PromptoTimeRange(prompto.value.Time low, prompto.value.Time high) {
			super(low, high);
		}

		@Override
		public prompto.value.Time getItem(long item) {
			PromptoTime result = low.value.plusSeconds(item-1);
			if(result.isAfter(high.value))
				throw new IndexOutOfBoundsException();
			return new prompto.value.Time(result);
		}
		
		@Override
		public PromptoTimeRange slice(long first, long last) {
			last = adjustLastSliceIndex(last);
			return new PromptoTimeRange(getItem(first), getItem(last));
		}

		@Override
		public long getNativeCount() {
			return 1 + (high.getMillisOfDay() - low.getMillisOfDay())/1000;
		}
		
		@Override
		public boolean contains(Object item) {
			if(!(item instanceof prompto.value.Time))
				return false;
			prompto.value.Time other = (prompto.value.Time)item;
			return other.compareTo(low)>=0 && high.compareTo(other)>=0;
		}

	}
	
	public TimeRange(Time left, Time right) {
		this(new PromptoTimeRange(left, right));
	}
	
	
	public TimeRange(PromptoRange<Time> range) {
		super(TimeType.instance(), range);
	}


	@Override
	public RangeBase<Time> newInstance(PromptoRange<Time> range) {
		return new TimeRange(range);
	}


}
