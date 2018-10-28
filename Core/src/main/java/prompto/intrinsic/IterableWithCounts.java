package prompto.intrinsic;

import java.util.Iterator;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public interface IterableWithCounts<T> extends Iterable<T> {

	Long getCount();
	Long getTotalCount();
	default Iterable<T> filter(Predicate<T> predicate) {
		Iterator<T> items = this.iterator();
		return new Iterable<T>() {
			@Override
			public Iterator<T> iterator() {
				return new Iterator<T>() {
					T current;
					@Override
					public boolean hasNext() {
						current = null;
						while(items.hasNext()) {
							current = items.next();
							if(predicate.test(current))
								return true;
						}
						current = null;
						return false;
					}
					
					@Override
					public T next() {
						return current;
					}
				};
			}
			@Override
			public String toString() {
				return StreamSupport.stream(this.spliterator(), false)
						.map(Object::toString)
						.collect(Collectors.toList())
						.toString();
			}
			
		};
	}
	default PromptoList<T> toList() {
		PromptoList<T> result = new PromptoList<T>(false);
		for(T item : this)
			result.add(item);
		return result;
	}
}
