package prompto.compiler;

import java.lang.reflect.Type;

import prompto.compiler.IVerifierEntry.VerifierType;


public class NameAndTypeConstant implements ICodeConstant {

	Descriptor descriptor; 
	Utf8Constant name;
	Utf8Constant type;
	int index;
	
	public NameAndTypeConstant(String name, Descriptor descriptor) {
		this.descriptor = descriptor;
		this.name = new Utf8Constant(name);
		this.type = new Utf8Constant(descriptor.toString());
	}

	@Override
	public int getTag() {
		return Tags.CONSTANT_NameAndType;
	}
	

	@Override
	public String toString() {
		return name.toString() + '(' + type.toString() + ')';
	}
	
	public Descriptor getDescriptor() {
		return descriptor;
	}

	public Utf8Constant getName() {
		return name;
	}
		
	@Override
	public boolean equals(Object obj) {
		return obj instanceof NameAndTypeConstant
				&& name.equals(((NameAndTypeConstant)obj).name)
				&& type.equals(((NameAndTypeConstant)obj).type);
	}
	
	@Override
	public int getIndexInConstantPool() {
		if(index==-1)
			throw new UnsupportedOperationException();
		return index;
	}
	
	@Override
	public void register(ConstantsPool pool) {
		index = pool.registerConstant(this);
		name.register(pool);
		type.register(pool);
	}
	
	@Override
	public void writeTo(ByteWriter writer) {
		/*
		CONSTANT_NameAndType_info {
		    u1 tag;
		    u2 name_index;
		    u2 descriptor_index;
		}	
		*/	
		writer.writeU1(Tags.CONSTANT_NameAndType);
		writer.writeU2(name.getIndexInConstantPool());
		writer.writeU2(type.getIndexInConstantPool());
	}

	public short getArgumentsCount(boolean isStatic) {
		Type[] types = getDescriptor().getTypes();
		return (short)(types.length - (isStatic ? 1 : 0));
	}

	public StackEntry resultToStackEntry() {
		String descriptor = getDescriptor().getLastDescriptor();
		if("V".equals(descriptor))
			return null;
		VerifierType type = VerifierType.fromDescriptor(descriptor);
		StackEntry entry = type.newStackEntry(null);
		if(entry instanceof StackEntry.ObjectEntry)
			((StackEntry.ObjectEntry)entry).setClassName(new ClassConstant(getDescriptor().getLastType()));
		return entry;
	}
}
