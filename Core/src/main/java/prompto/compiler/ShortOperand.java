package prompto.compiler;

public class ShortOperand implements IInlineOperand {

	short value;
	
	public ShortOperand(short value) {
		this.value = value;
	}

	public short value() {
		return value;
	}
	
	@Override
	public String toString() {
		return String.valueOf(value);
	}

}
