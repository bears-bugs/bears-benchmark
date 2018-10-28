package prompto.utils;

import java.util.LinkedList;

public class ObjectList<T> extends LinkedList<T> {

	private static final long serialVersionUID = 1L;

	public ObjectList() {
	}
	
	public ObjectList(ObjectList<T> copyFrom) {
		super(copyFrom);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(Object o : this) {
			sb.append(o.toString());
			sb.append(", ");
		}
		if(sb.length()>=2)
			sb.setLength(sb.length()-2);
		return sb.toString();
	}


}
