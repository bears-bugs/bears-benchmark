package prompto.argument;

import prompto.grammar.Identifier;
import prompto.value.CodeValue;

public class ValuedCodeArgument extends CodeArgument {

	CodeValue value;
	
	public ValuedCodeArgument(Identifier id, CodeValue value) {
		super(id);
		this.value = value;
	}

	public CodeValue getValue() {
		return value;
	}

}
