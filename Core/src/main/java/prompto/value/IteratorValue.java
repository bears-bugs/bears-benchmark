package prompto.value;

import java.util.Iterator;

import prompto.type.IType;
import prompto.type.IteratorType;

/* exposes a native IValue iterator as an IValue */
public class IteratorValue extends BaseValue implements Iterator<IValue> {

	Iterator<IValue> source;
	
	public IteratorValue(IType itemType, Iterator<IValue> source) {
		super(new IteratorType(itemType));
		this.source = source;
	}

	@Override
	public boolean hasNext() {
		return source.hasNext();
	}
	
	@Override
	public IValue next() {
		return source.next();
	}
	
}
