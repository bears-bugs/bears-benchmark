package prompto.literal;

import prompto.expression.IExpression;
import prompto.transpiler.Transpiler;
import prompto.utils.CodeWriter;

public class DictEntry {

	DictKey key;
	IExpression value;

	public DictEntry(DictKey key, IExpression value) {
		this.key = key;
		this.value = value;
	}

	public DictKey getKey() {
		return key;
	}

	public IExpression getValue() {
		return value;
	}

	@Override
	public String toString() {
		return key.toString() + ':' + value.toString();
	}

	public void toDialect(CodeWriter writer) {
		key.toDialect(writer);
		writer.append(':');
		value.toDialect(writer);
	}

	public void declare(Transpiler transpiler) {
		this.value.declare(transpiler);
	}

	public void transpile(Transpiler transpiler) {
		this.key.transpile(transpiler);
		transpiler.append(':');
		this.value.transpile(transpiler);
	}

}
