package prompto.compiler;


public class CallSiteConstant implements ICodeConstant {

	int index;
	BootstrapMethod bootstrapMethod;
	NameAndTypeConstant methodNameAndType;
	
	public CallSiteConstant(BootstrapMethod bootstrapMethod, NameAndTypeConstant methodNameAndType) {
		this.bootstrapMethod = bootstrapMethod;
		this.methodNameAndType = methodNameAndType;
	}
	
	@Override
	public int getTag() {
		return Tags.CONSTANT_InvokeDynamic;
	}
	


	@Override
	public void register(ConstantsPool pool) {
		index = pool.registerConstant(this);
		methodNameAndType.register(pool);
	}

	@Override
	public void writeTo(ByteWriter writer) {
		/*
		CONSTANT_InvokeDynamic_info {
		    u1 tag;
		    u2 bootstrap_method_attr_index;
		    u2 name_and_type_index;
		}
		*/
		writer.writeU1(Tags.CONSTANT_InvokeDynamic);
		writer.writeU2(bootstrapMethod.getIndexInBootstrapList());
		writer.writeU2(methodNameAndType.getIndexInConstantPool());
		
	}

	@Override
	public int getIndexInConstantPool() {
		if(index==-1)
			throw new UnsupportedOperationException();
		return index;
	}
	
	public short getArgumentsCount(boolean isStatic) {
		return methodNameAndType.getArgumentsCount(isStatic);
	}

	public StackEntry resultToStackEntry() {
		return methodNameAndType.resultToStackEntry();
	}
}
