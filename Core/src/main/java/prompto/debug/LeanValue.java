package prompto.debug;

public class LeanValue implements IValue {

	String typeName;
	String valueString;
	
	public LeanValue() {
	}
	
	public LeanValue(IValue value) {
		this.typeName = value.getTypeName();
		this.valueString = value.getValueString();
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	@Override
	public String getTypeName() {
		return typeName;
	}

	public void setValueString(String valueString) {
		this.valueString = valueString;
	}
	
	@Override
	public String getValueString() {
		return valueString;
	}

}
