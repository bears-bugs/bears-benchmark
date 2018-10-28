package prompto.csharp;

import prompto.utils.CodeWriter;



public class CSharpIdentifierExpression implements CSharpExpression {

	public static CSharpIdentifierExpression parse(String ids) {
		String[] parts = ids.split("\\.");
		CSharpIdentifierExpression result = null;
		for(String part : parts)
			result = new CSharpIdentifierExpression(result,part);
		return result;
	}
	
	CSharpIdentifierExpression parent;
	String identifier;
	
	public CSharpIdentifierExpression(String identifier) {
		this.identifier = identifier;
	}

	public CSharpIdentifierExpression(CSharpIdentifierExpression parent, String identifier) {
		this.parent = parent;
		this.identifier = identifier;
	}
	
	public CSharpIdentifierExpression getParent() {
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
