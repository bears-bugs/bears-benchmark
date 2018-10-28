package prompto.compiler;

public class BootstrapMethod {

	MethodHandleConstant methodHandle;
	int index;
	
	public BootstrapMethod(MethodHandleConstant methodHandle) {
		this.methodHandle = methodHandle;
	}
	
	public int getIndexInBootstrapList() {
		return index;
	}

	public void setIndexInBootstrapList(int index) {
		this.index = index;
	}

	public void register(ConstantsPool pool) {
		methodHandle.register(pool);
	}

	public void writeTo(ByteWriter writer) {
		writer.writeU2(methodHandle.getIndexInConstantPool());
		writer.writeU2(0); // no args for now
	}

	public int length() {
		/*
			u2 bootstrap_method_ref;
		    u2 num_bootstrap_arguments;
		    u2 bootstrap_arguments[num_bootstrap_arguments];
		 */
		return 2 + 2; // no args for now
	}

}
