package prompto.python;

import prompto.utils.CodeWriter;


public class PythonStatement {

	PythonExpression expression;
	PythonModule module;
	boolean isReturn;
	
	public PythonStatement(PythonExpression expression,boolean isReturn) {
		this.expression = expression;
		this.isReturn = isReturn;
	}
	
	public void setModule(PythonModule module) {
		this.module = module;
	}

	@Override
	public String toString() {
		return "" + (isReturn ? "return " : "") + expression.toString() + ";";
	}

	public void toDialect(CodeWriter writer) {
		if(isReturn)
			writer.append("return ");
		expression.toDialect(writer);
		if(module!=null)
			module.toDialect(writer);
	}
}
