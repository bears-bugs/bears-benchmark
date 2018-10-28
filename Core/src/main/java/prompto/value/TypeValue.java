package prompto.value;

import prompto.type.IType;

public class TypeValue extends BaseValue {

	IType value;
	
	public TypeValue(IType value) {
		super(null); // TODO
		this.value = value;
	}
	
	public IType getValue() {
		return value;
	}

	@Override
	public Object getStorableData() {
		throw new UnsupportedOperationException(); // can't be stored
	}
	
	@Override
	public String toString() {
		return value.toString();
	}

}
