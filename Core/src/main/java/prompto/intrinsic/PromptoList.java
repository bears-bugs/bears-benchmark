package prompto.intrinsic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.Predicate;

import prompto.value.IMultiplyable;

@SuppressWarnings("serial")
public class PromptoList<V> extends ArrayList<V> implements Filterable<PromptoList<V>, V>, IMultiplyable {

	boolean mutable;
	
	public PromptoList(boolean mutable) {
		this.mutable = mutable;
	}

	public PromptoList(V[] items) {
		super(Arrays.asList(items));
		this.mutable = false;
	}

	public PromptoList(Collection<? extends V> items, boolean mutable) {
		super(items);
		this.mutable = mutable;
	}
	
	public boolean isMutable() {
		return mutable;
	}

	public Long getCount() {
		return (long)size();
	}
	
	public long getNativeCount() {
		return size();
	}
	
	public V getLast() {
		return get(this.size()-1);
	}

	public PromptoList<V> multiply(int count) {
		PromptoList<V> result = new PromptoList<>(false);
		while(count-->0)
			result.addAll(this);
		return result;
	}

	public PromptoList<V> slice(long first, long last) {
		if (last < 0)
			last = this.size() + 1 + last;
		return new PromptoList<>(this.subList((int)(first-1), (int)last), false);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PromptoList<V> sort(boolean descending) {
		PromptoList<V> sorted = new PromptoList<>(this, false);
		Comparator<V> cmp = descending ?
				new Comparator<V>() {

					@Override
					public int compare(V o1, V o2) {
						return ((Comparable)o2).compareTo(o1);
					}
			
				} : 
				new Comparator<V>() {
					
					@Override
					public int compare(V o1, V o2) {
						return ((Comparable)o1).compareTo(o2);
					}
				};
		sorted.sort(cmp);
		return sorted;
	}
	
	@SuppressWarnings("unchecked")
	public PromptoList<? extends V> sortUsing(Comparator<? extends V> cmp) {
		PromptoList<? extends V> sorted = new PromptoList<>(this, false);
		sorted.sort((Comparator<V>)cmp);
		return sorted;
	}

	public boolean containsAny(Collection<Object> items) {
		for(Object item : items) {
			if(contains(item))
				return true;
		}
		return false;
	}
	
	public PromptoList<V> filter(Predicate<V> p) {
		PromptoList<V> filtered = new PromptoList<>(false);
		this.forEach((v)->{
			if(p.test(v))
				filtered.add(v);
		});
		return filtered;
	}
	
	@Override
	public V set(int index, V element) {
		if(!mutable)
			PromptoException.throwEnumeratedException("NOT_MUTABLE");
		return super.set(index, element);
	}
		
}
