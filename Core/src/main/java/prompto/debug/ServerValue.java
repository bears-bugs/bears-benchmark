package prompto.debug;

public class ServerValue implements IValue {
	
	prompto.value.IValue value;

	public ServerValue(prompto.value.IValue value) {
		this.value = value;
	}
	
	@Override
	public String getTypeName() {
		return this.value.getType().getTypeName();
	}
	
	@Override
	public String getValueString() {
		return this.value.toString();
	}

}