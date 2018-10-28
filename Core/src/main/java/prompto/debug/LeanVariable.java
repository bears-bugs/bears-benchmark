package prompto.debug;


public class LeanVariable implements IVariable {

	String name;
	String typeName;
	LeanValue value;
	
	public LeanVariable() {
	}
	
	public LeanVariable(String name, String typeName) {
		this.name = name;
		this.typeName = typeName;
	}
	
	public LeanVariable(IVariable variable) {
		this.name = variable.getName();
		this.typeName = variable.getTypeName();
		this.value = new LeanValue(variable.getValue());
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	@Override
	public String getTypeName() {
		return typeName;
	}
	
	public void setValue(LeanValue value) {
		this.value = value;
	}
	
	@Override
	public LeanValue getValue() {
		return value;
	}

}
