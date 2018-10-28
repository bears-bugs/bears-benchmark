package prompto.compiler;

import prompto.compiler.IVerifierEntry.VerifierType;


public class LongConstant implements IValueConstant {
	
	long value;
	int index;
	
	public LongConstant(long value) {
		this.value = value;
	}
	
	@Override
	public int getTag() {
		return Tags.CONSTANT_Long;
	}
	

	@Override
	public StackEntry toStackEntry() {
		return VerifierType.ITEM_Long.newStackEntry(null);
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	@Override
	public int size() {
		return 2;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof LongConstant 
				&& value==((LongConstant)obj).value;
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
		CONSTANT_Long_info {
		    u1 tag;
		    u4 high_bytes;
		    u4 low_bytes;
		}
		*/
		writer.writeU1(Tags.CONSTANT_Long);
		writer.writeU4((int)((value>>32)&0xFFFFFFFF));
		writer.writeU4((int)(value&0xFFFFFFFF));
	}

}
