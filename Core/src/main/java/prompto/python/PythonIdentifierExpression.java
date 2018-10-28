package prompto.python;

import prompto.utils.CodeWriter;



public class PythonIdentifierExpression implements PythonExpression {

	public static PythonIdentifierExpression parse(String ids) {
		String[] parts = ids.split("\\.");
		PythonIdentifierExpression result = null;
		for(String part : parts)
			result = new PythonIdentifierExpression(result,part);
		return result;
	}
	
	PythonIdentifierExpression parent;
	String identifier;
	
	public PythonIdentifierExpression(String identifier) {
		this.identifier = identifier;
	}

	public PythonIdentifierExpression(PythonIdentifierExpression parent, String identifier) {
		this.parent = parent;
		this.identifier = identifier;
	}
	
	public PythonIdentifierExpression getParent() {
		return parent;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	@Override
	public String toString() {
		if(parent==null)
			return identifier;
		else 
			return parent.toString() + "." + identifier;
	}
		
	@Override
	public void toDialect(CodeWriter writer) {
		if(parent!=null) {
			parent.toDialect(writer);
			writer.append('.');
		}
		writer.append(identifier);
	}

}
