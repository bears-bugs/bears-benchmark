package prompto.literal;

import prompto.grammar.Identifier;

public class DictIdentifierKey extends DictKey {

	Identifier id;
	
	public DictIdentifierKey(Identifier id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return id.toString();
	}
	
	@Override
	protected String asKey() {
		return id.toString();
	}

}
