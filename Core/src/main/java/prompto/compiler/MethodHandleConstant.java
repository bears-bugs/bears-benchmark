package prompto.compiler;

public class MethodHandleConstant implements ICodeConstant {

	MethodConstant method;
	int index;
	
	
	public MethodHandleConstant(MethodConstant method) {
		this.method = method;
	}
	
	@Override
	public int getTag() {
		return Tags.CONSTANT_MethodHandle;
	}
	

	@Override
	public void register(ConstantsPool pool) {
		method.register(pool);
		index = pool.registerConstant(this);
	}

	@Override
	public void writeTo(ByteWriter writer) {
		/*
		CONSTANT_MethodHandle_info {
		    u1 tag;
		    u1 reference_kind;
		    u2 reference_index;
		}
		*/
		writer.writeU1(Tags.CONSTANT_MethodHandle);
		writer.writeU1(Tags.REF_invokeStatic); // for now, only used in INVOKEDYNAMIC
		writer.writeU2(method.getIndexInConstantPool());
	}

	@Override
	public int getIndexInConstantPool() {
		if(index==-1)
			throw new UnsupportedOperationException();
		return index;
	}

}
