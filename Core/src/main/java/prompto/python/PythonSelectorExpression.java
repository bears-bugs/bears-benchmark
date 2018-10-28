package prompto.python;


public abstract class PythonSelectorExpression implements PythonExpression {

	PythonExpression parent;
	
	public void setParent(PythonExpression parent) {
		this.parent = parent;
	}
	
}
