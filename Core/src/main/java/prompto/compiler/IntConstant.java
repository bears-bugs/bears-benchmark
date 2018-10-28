package prompto.compiler;

import prompto.compiler.IVerifierEntry.VerifierType;

public class IntConstant implements IValueConstant {
	
	int value;
	int index;
	
	public IntConstant(int value) {
		this.value = value;
	}

	@Override
	public int getTag() {
		return Tags.CONSTANT_Integer;
	}
	

	@Override
	public StackEntry toStackEntry() {
		return VerifierType.ITEM_Integer.newStackEntry(null);
	}
	
	@Override
	public String toString() {
		return String.valueOf(value);
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof IntConstant 
				&& value==((IntConstant)obj).value;
	}
	
	@Override
	public int getIndexInConstantPool() {
		return index;
	}
	
	@Override
	public void register(ConstantsPool pool) {
		index = pool.registerConstant(this);
	}
	
	@Override
	public void writeTo(ByteWriter writer) {
		/*
		CONSTANT_Integer_info {
		    u1 tag;
		    u4 bytes;
		}		
		*/
		writer.writeU1(Tags.CONSTANT_Integer);
		writer.writeU4(value);
	}

}
