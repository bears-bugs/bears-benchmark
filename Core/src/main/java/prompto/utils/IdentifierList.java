package prompto.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

import prompto.grammar.Identifier;


public class IdentifierList extends ObjectList<Identifier> {

	private static final long serialVersionUID = 1L;

	public static IdentifierList parse(String ids) {
		return new IdentifierList(ids.split(","));
	}
	
	public IdentifierList() {		
	}
	

	public IdentifierList(String item) {
		this.add(new Identifier(item));
	}

	
	public IdentifierList(Identifier item) {
		this.add(item);
	}


	public IdentifierList(String ... items) {
		this.addAll(Arrays.asList(items).stream().map(Identifier::new).collect(Collectors.toList()));
	}

	public IdentifierList(Identifier ... items) {
		this.addAll(Arrays.asList(items));
	}

	public void toDialect(CodeWriter writer, boolean finalAnd) {
		switch(writer.getDialect()) {
		case E:
			toEDialect(writer, finalAnd);
			break;
		case O:
			toODialect(writer);
			break;
		case M:
			toMDialect(writer);
			break;
		}
		
	}

	private void toEDialect(CodeWriter writer, boolean finalAnd) {
		switch(this.size()) {
		case 0:
			return;
		case 1:
			writer.append(this.getFirst());
			break;
		default:
			for(Identifier s : this) {
				if(finalAnd && s==this.getLast())
					break;
				writer.append(s);
				writer.append(", ");
			}
			writer.trimLast(2);
			if(finalAnd) {
				writer.append(" and ");
				writer.append(this.getLast());
			}
		}
	}

	private void toODialect(CodeWriter writer) {
		if(this.size()>0) {
			for(Identifier s : this) {
				writer.append(s);
				writer.append(", ");
			}
			writer.trimLast(2);
		}
	}

	private void toMDialect(CodeWriter writer) {
		toODialect(writer);
	}
}
