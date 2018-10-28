package prompto.value;

import java.util.Set;
import java.util.TreeSet;

import prompto.type.IType;

public class SortedSetValue extends SetValue {

	public SortedSetValue(IType itemType) {
		super(itemType);
	}
	
	protected Set<IValue> newSet() {
		return new TreeSet<IValue>();
	}
	
	
}
