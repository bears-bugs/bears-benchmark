package prompto.literal;

import java.util.LinkedList;

import prompto.transpiler.Transpiler;
import prompto.utils.CodeWriter;

public class DocEntryList extends LinkedList<DictEntry> {

	private static final long serialVersionUID = 1L;

	public DocEntryList() {
	}
	
	
	public DocEntryList(LinkedList<DictEntry> entries) {
		super(entries);
	}
	

	public DocEntryList(DictEntry entry) {
		this.add(entry);
	}
	
	@Override
	public String toString() {
		String text = super.toString();
		text = text.substring(1,text.length()-1);
		return '{' + text + '}';
	}

	public void toDialect(CodeWriter writer) {
		writer.append('{');
		if(this.size()>0) {
			for(DictEntry entry : this) {
				entry.toDialect(writer);
				writer.append(", ");
			}
			writer.trimLast(2);
		}
		writer.append('}');
	}

	public void declare(Transpiler transpiler) {
		this.forEach(entry->entry.declare(transpiler));
	}

	public void transpile(Transpiler transpiler) {
	   transpiler.append('{');
	    if(this.size()>0) {
	        this.forEach(item -> {
	            item.transpile(transpiler);
	            transpiler.append(",");
	        });
	        transpiler.trimLast(1);
	    }
	    transpiler.append('}');
	}
	
}
