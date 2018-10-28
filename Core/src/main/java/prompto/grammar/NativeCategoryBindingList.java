package prompto.grammar;

import prompto.utils.CodeWriter;
import prompto.utils.ObjectList;

public class NativeCategoryBindingList extends ObjectList<NativeCategoryBinding> {

	private static final long serialVersionUID = 1L;

	public NativeCategoryBindingList() {
		
	}
	
	public NativeCategoryBindingList(NativeCategoryBinding mapping) {
		this.add(mapping);
	}

	public void toDialect(CodeWriter writer) {
		switch(writer.getDialect()) {
		case E:
			toEDialect(writer);
			break;
		case O:
			toODialect(writer);
			break;
		case M:
			toMDialect(writer);
			break;
		}
	}

	private void toEDialect(CodeWriter writer) {
		writer.append("define category bindings as:\n");
		writer.indent();
		for(NativeCategoryBinding m : this) {
			m.toDialect(writer);
			writer.newLine();
		}
		writer.dedent();	
	}

	private void toMDialect(CodeWriter writer) {
		writer.append("def category bindings:\n");
		writer.indent();
		for(NativeCategoryBinding m : this) {
			m.toDialect(writer);
			writer.newLine();
		}
		writer.dedent();	
	}

	private void toODialect(CodeWriter writer) {
		writer.append("category bindings {\n");
		writer.indent();
		for(NativeCategoryBinding m : this) {
			m.toDialect(writer);
			writer.append(';');
			writer.newLine();
		}
		writer.dedent();	
		writer.append("}");
	}
	

}
