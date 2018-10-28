package prompto.runtime.utils;

public class MyClass {
	
	static public char characterValue() {
		return 'Z';
	}

	static public Character characterObject() {
		return 'Z';
	}

	String id;
	String name;
	String display;
	
	public void setId(String id) {
		this.id = id;
		computeDisplay();
	}
	
	public void setName(String name) {
		this.name = name;
		computeDisplay();
	}
	
	
	private void computeDisplay() {
		setDisplay("/id=" + id + "/name=" + name);
	}

	public void setDisplay(String display) {
		this.display = display;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDisplay() {
		return display;
	}
	
	public void printDisplay() {
		System.out.print(getDisplay());
	}
	
}
