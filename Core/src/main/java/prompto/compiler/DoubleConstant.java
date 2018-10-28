package prompto.compiler;

import prompto.compiler.IVerifierEntry.VerifierType;

public class DoubleConstant implements IValueConstant {
	
	double value;
	int index;
	
	public DoubleConstant(double value) {
		this.value = value;
	}

	@Override
	public int getTag() {
		return Tags.CONSTANT_Double;
	}
	

	@Override
	public StackEntry toStackEntry() {
		return VerifierType.ITEM_Double.newStackEntry(null);
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
		return obj instanceof DoubleConstant 
				&& value==((DoubleConstant)obj).value;
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
		CONSTANT_Double_info {
		    u1 tag;
		    u4 high_bytes;
		    u4 low_bytes;
		}
		*/
		writer.writeU1(Tags.CONSTANT_Double);
		long l = Double.doubleToLongBits(value);
		writer.writeU4((int)((l>>32)&0xFFFFFFFF));
		writer.writeU4((int)(l&0xFFFFFFFF));
	}

}
