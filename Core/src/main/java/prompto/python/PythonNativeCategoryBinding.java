package prompto.python;

import prompto.grammar.NativeCategoryBinding;
import prompto.utils.CodeWriter;


public class PythonNativeCategoryBinding extends NativeCategoryBinding {

	String identifier;
	PythonModule module;
	
	public PythonNativeCategoryBinding(String identifier, PythonModule module) {
		this.identifier = identifier;
		this.module = module;
	}

	public PythonNativeCategoryBinding(PythonNativeCategoryBinding mapping) {
		this.identifier = mapping.identifier;
		this.module = mapping.module;
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		writer.append(identifier);
		if(module!=null)
			module.toDialect(writer);
	}
}
