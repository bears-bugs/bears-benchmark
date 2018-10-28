package prompto.intrinsic;

import java.util.Collection;
import java.util.Iterator;


public abstract class PromptoRange<T extends Object> implements IterableWithCounts<T> {
	
	protected T low;
	protected T high;
	
	public PromptoRange(T low, T high) {
		this.low = low;
		this.high = high;
	}
	
	public T getLow() {
		return low;
	}
	
	public T getHigh() {
		return high;
	}
	
	public java.lang.Long getCount() {
		return getNativeCount();
	}
	
	@Override
	public java.lang.Long getTotalCount() {
		return getNativeCount();
	}
	
	public abstract long getNativeCount();
	public abstract T getItem(long item);
	
	@Override
	public String toString() {
		return "[" + low.toString() + ".." + high.toString() + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof PromptoRange<?>
			&& low.equals(((PromptoRange<?>)obj).low) 
			&& high.equals(((PromptoRange<?>)obj).high);
	}

	public abstract PromptoRange<T> slice(long first, long last);
	
	protected long adjustLastSliceIndex(long last) {
		if(last>=0)
			return last;
		else
			return getNativeCount() + 1 + last;
	}
	
	public abstract boolean contains(Object item);

	public boolean containsAll(Collection<Object> items) {
		for(Object item : items) {
			if(!contains(item))
				return false;
		}
		return true;
	}
	
	public boolean containsAny(Collection<Object> items) {
		for(Object item : items) {
			if(contains(item))
				return true;
		}
		return false;
	}
	
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			
			long index = 0;
			long length = PromptoRange.this.getNativeCount();
			
			@Override public boolean hasNext() { return index<length; }
			@Override public T next() { return getItem(++index); }
			
		};
	}
	
	public static class Character extends PromptoRange<java.lang.Character> {
		
		public Character(java.lang.Character low, java.lang.Character high) {
			super(low, high);
		}
		
		@Override
		public java.lang.Character getItem(long item) {
			char result = (char)(low.charValue() + item - 1);
			if(result>high.charValue())
				throw new IndexOutOfBoundsException();
			return java.lang.Character.valueOf(result);
		}
		
		@Override
		public PromptoRange.Character slice(long first, long last) {
			last = adjustLastSliceIndex(last);
			return new PromptoRange.Character(getItem(first), getItem(last));
		}
		
		@Override
		public long getNativeCount() {
			return 1L + high.charValue() - low.charValue();
		}
		
		public boolean contains(Object item) {
			if(!(item instanceof java.lang.Character))
				return false;
			java.lang.Character other = (java.lang.Character)item;
			return other.compareTo(low)>=0 && high.compareTo(other)>=0;
		}

	}
	
	public static class Long extends PromptoRange<java.lang.Long> {
		
		public Long(java.lang.Long low, java.lang.Long high) {
			super(low, high);
		}
		
		@Override
		public java.lang.Long getItem(long item) {
			long result = low + item - 1;
			if(result>high)
				throw new IndexOutOfBoundsException();
			return java.lang.Long.valueOf(result);
		}
		
		@Override
		public PromptoRange.Long slice(long first, long last) {
			last = adjustLastSliceIndex(last);
			return new PromptoRange.Long(getItem(first), getItem(last));
		}
		
		@Override
		public long getNativeCount() {
			return 1L + high.longValue() - low.longValue();
		}

		public boolean contains(Object item) {
			if(!(item instanceof java.lang.Long))
				return false;
			java.lang.Long other = (java.lang.Long)item;
			return other.compareTo(low)>=0 && high.compareTo(other)>=0;
		}


	}
	
	public static class Date extends PromptoRange<PromptoDate> {
		
		public Date(PromptoDate low, PromptoDate high) {
			super(low, high);
		}

		@Override
		public PromptoDate getItem(long item) {
			PromptoDate result = low.plusDays(item-1);
			if(result.isAfter(high))
				throw new IndexOutOfBoundsException();
			return result;
		}
		
		@Override
		public PromptoRange.Date slice(long first, long last) {
			last = adjustLastSliceIndex(last);
			return new PromptoRange.Date(getItem(first), getItem(last));
		}

		@Override
		public long getNativeCount() {
			long h = high.toJavaTime();
			long l = low.toJavaTime();
			return 1 + ( (h-l)/(24*60*60*1000));
		}

		public boolean contains(Object item) {
			if(!(item instanceof PromptoDate))
				return false;
			PromptoDate other = (PromptoDate)item;
			return other.compareTo(low)>=0 && high.compareTo(other)>=0;
		}

	}
	
	public static class Time extends PromptoRange<PromptoTime> {
		
		public Time(PromptoTime low, PromptoTime high) {
			super(low, high);
		}
		
		@Override
		public PromptoTime getItem(long item) {
			PromptoTime result = low.plusSeconds(item-1);
			if(result.isAfter(high))
				throw new IndexOutOfBoundsException();
			return result;
		}
		
		@Override
		public PromptoRange.Time slice(long first, long last) {
			last = adjustLastSliceIndex(last);
			return new PromptoRange.Time(getItem(first), getItem(last));
		}
		
		@Override
		public long getNativeCount() {
			return 1 + (high.getNativeMillisOfDay() - low.getNativeMillisOfDay())/1000;
		}

		public boolean contains(Object item) {
			if(!(item instanceof PromptoTime))
				return false;
			PromptoTime other = (PromptoTime)item;
			return other.compareTo(low)>=0 && high.compareTo(other)>=0;
		}

	}
}
