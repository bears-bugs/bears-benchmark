package prompto.python;

import prompto.utils.CodeWriter;


public class Python3NativeCategoryBinding extends PythonNativeCategoryBinding {

	public Python3NativeCategoryBinding(PythonNativeCategoryBinding mapping) {
		super(mapping);
	}

	@Override
	public void toDialect(CodeWriter writer) {
		writer.append("Python3: ");
		super.toDialect(writer);
	}

}