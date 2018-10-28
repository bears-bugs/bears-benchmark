package prompto.literal;

import java.util.function.Supplier;

import prompto.error.PromptoError;
import prompto.expression.IExpression;
import prompto.runtime.Context;
import prompto.utils.CodeWriter;
import prompto.value.IValue;

public abstract class Literal<T extends IValue> implements IExpression {
	
	Supplier<String> text;
	protected T value;
	
	protected Literal(String text, T value) {
		this(()->text, value);
	}
	
	protected Literal(Supplier<String> text, T value) {
		this.text = text;
		this.value = value;
	}
	
	@Override
	public void toDialect(CodeWriter writer) {
		writer.append(text.get());
	}
	
	@Override
	public String toString() {
		return text.get();
	}
	
	public T getValue() {
		return value;
	}
	
	@Override
	public IValue interpret(Context context) throws PromptoError {
		return value;
	}
	
	@Override
	public int hashCode() {
		return value.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Literal)
			return value.equals(((Literal<?>)obj).value);
		else
			return value.equals(obj);
	}
	
}
