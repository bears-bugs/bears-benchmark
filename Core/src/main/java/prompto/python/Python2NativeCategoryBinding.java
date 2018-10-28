package prompto.python;

import prompto.utils.CodeWriter;


public class Python2NativeCategoryBinding extends PythonNativeCategoryBinding {

	public Python2NativeCategoryBinding(PythonNativeCategoryBinding mapping) {
		super(mapping);
	}

	@Override
	public void toDialect(CodeWriter writer) {
		writer.append("Python2: ");
		super.toDialect(writer);
	}
}
