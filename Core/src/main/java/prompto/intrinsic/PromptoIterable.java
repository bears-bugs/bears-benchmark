package prompto.intrinsic;

import java.util.Iterator;

public abstract class PromptoIterable<S,R> implements IterableWithCounts<R> {

	Iterable<S> source;
	long length;
	
	public PromptoIterable(Iterable<S> source, long length) {
		this.source = source;
		this.length = length;
	}
	
	@Override
	public Iterator<R> iterator() {
		return new PromptoIterator();
	}
	
	class PromptoIterator implements Iterator<R> {
		
		Iterator<S> iter = source.iterator();
		
		@Override
		public boolean hasNext() { return iter.hasNext(); }
		
		@Override
		public R next() { 
			return apply(iter.next()); 
		}
	}
	
	protected abstract R apply(S s);
}
