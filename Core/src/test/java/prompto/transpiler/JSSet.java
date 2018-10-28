package prompto.transpiler;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

@SuppressWarnings("restriction")
public class JSSet {

	Set<Object> set = new HashSet<>();
	
	public JSSet() {
	}
	
	public JSSet(Object values) {
		if(values instanceof ScriptObjectMirror) {
			List<Object> list = convert((ScriptObjectMirror)values);
			set.addAll(list);
		} else if(values instanceof JSSet) {
			set.addAll(((JSSet)values).set);
		} else if(values != null)
			throw new UnsupportedOperationException(values.getClass().getName());
	}
	
	public Set<Object> getSet() {
		return set;
	}
	
	private List<Object> convert(ScriptObjectMirror values) {
		return values.entrySet().stream()
				.map(Map.Entry::getValue)
				.collect(Collectors.toList());
	}

	public int getSize() {
		return set.size();
	}
	
	public boolean has(Object value) {
		return set.contains(value);
	}
	
	public void add(Object value) {
		set.add(value);
	}

	public Object values() {
		return new SetIterator();
	}
	
	public class SetIterator {

		Iterator<Object> iterator;
		
		public SetIterator() {
			this.iterator = set.iterator();
		}
		
		public JSSet JSSet() {
			return JSSet.this;
		}
	
		public int getLength() {
			return set.size();
		}

		public Object next() {
			SetIteration iter = new SetIteration();
			if(iterator.hasNext()) {
				iter.value = iterator.next();
				iter.done = false;
			} else
				iter.done = true;
			return iter;
		}
		
		public class SetIteration {
			public boolean done;
			public Object value;
		}
		
	}
	
}
