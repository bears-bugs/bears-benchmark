package prompto.compiler;

public interface IConstantOperand extends IOperand {

	int getTag();
	void register(ConstantsPool pool);
	void writeTo(ByteWriter writer);
	int getIndexInConstantPool();
	default int size() {
		return 1;
	}
}
