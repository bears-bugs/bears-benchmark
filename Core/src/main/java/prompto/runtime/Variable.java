package prompto.runtime;

import prompto.grammar.INamed;
import prompto.grammar.Identifier;
import prompto.type.IType;

public class Variable implements INamed {

	Identifier name;
	IType type;
	
	public Variable(Identifier name, IType type) {
		this.name = name;
		this.type = type;
	}

	@Override
	public String toString() {
		return name.toString();
	}
	
	@Override
	public Identifier getId() {
		return name;
	}

	@Override
	public IType getType(Context context) {
		return type;
	}
	
}
