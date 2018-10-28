package prompto.compiler;

public class ByteOperand implements IInlineOperand {

	byte value;
	
	public ByteOperand(byte value) {
		this.value = value;
	}

	public byte value() {
		return value;
	}
	
	@Override
	public String toString() {
		return String.valueOf(value);
	}
}
