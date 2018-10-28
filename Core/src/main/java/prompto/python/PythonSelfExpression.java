package prompto.python;

import prompto.utils.CodeWriter;

public class PythonSelfExpression implements PythonExpression {
	
	@Override
	public void toDialect(CodeWriter writer) {
		writer.append("self");
	}
}
