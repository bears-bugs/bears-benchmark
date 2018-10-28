package prompto.intrinsic;

import java.util.Collection;
import java.util.TreeSet;

@SuppressWarnings("serial")
public class PromptoSortedSet<V> extends TreeSet<V> {

	public PromptoSortedSet() {
	}
	
	public PromptoSortedSet(Collection<V> items) {
		super(items);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('<');
		forEach((i)->{
			sb.append(i.toString());
			sb.append(", ");
		});
		if(sb.length()>1)
			sb.setLength(sb.length()-2); // trim last ", "
		sb.append('>');
		return sb.toString();
	}
	
	public Long getCount() {
		return (long)size();
	}
	
	public long getNativeCount() {
		return size();
	}
	
	public PromptoSortedSet<V> sort() {
		return this; // since it's immutable
	}
	
	
}
