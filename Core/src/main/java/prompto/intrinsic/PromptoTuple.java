package prompto.intrinsic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("serial")
public class PromptoTuple<V> extends ArrayList<V> {

	boolean mutable;

	public PromptoTuple(boolean mutable) {
		this.mutable = mutable;
	}

	public PromptoTuple(Collection<? extends V> items, boolean mutable) {
		super(items);
		this.mutable = mutable;
	}
	
	
	public boolean isMutable() {
		return mutable;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		forEach((i)->{
			sb.append(i.toString());
			sb.append(", ");
		});
		if(sb.length()>1)
			sb.setLength(sb.length()-2); // trim last ", "
		sb.append(')');
		return sb.toString();
	}
	
	public Long getCount() {
		return (long)size();
	}
	
	public long getNativeCount() {
		return size();
	}

	public PromptoTuple<V> slice(long first, long last) {
		if (last < 0)
			last = this.size() + 1 + last;
		return new PromptoTuple<>(this.subList((int)(first-1), (int)last), false);
	}
	
	public boolean containsAny(Collection<Object> items) {
		for(Object item : items) {
			if(contains(item))
				return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public int compareTo(PromptoTuple<?> other, List<Boolean> directions) {
		Iterator<java.lang.Boolean> iterDirs = directions.iterator();
		Iterator<V> iterThis = this.iterator();
		Iterator<?> iterOther = other.iterator();
		while(iterThis.hasNext() && iterOther.hasNext()) {
			boolean descending = iterDirs.hasNext() ? iterDirs.next() : false;
			// compare items
			V thisVal = iterThis.next();
			Object otherVal = iterOther.next();
			if(thisVal==null && otherVal==null)
				continue;
			else if(thisVal==null)
				return descending ? 1 : -1;
			else if(otherVal==null)
				return descending ? -1 : 1;
			if(!(thisVal instanceof Comparable<?> && thisVal.getClass().isInstance(otherVal)))
				return 0;
			int cmp = ((Comparable<V>)thisVal).compareTo((V)otherVal);
			// if not equal, done
			if(cmp!=0)
				return descending ? -cmp : cmp;
		}
		boolean descending = iterDirs.hasNext() ? iterDirs.next() : false;
		if(iterThis.hasNext())
			return descending ? -1 : 1;
		else if(iterOther.hasNext())
			return descending ? 1 : -1;
		else
			return 0;
	}
	
	@Override
	public V set(int index, V element) {
		if(!mutable)
			PromptoException.throwEnumeratedException("NOT_MUTABLE");
		return super.set(index, element);
	}

}
