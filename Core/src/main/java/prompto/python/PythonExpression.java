package prompto.python;

import prompto.utils.CodeWriter;



public interface PythonExpression {

	void toDialect(CodeWriter writer);
	
}
