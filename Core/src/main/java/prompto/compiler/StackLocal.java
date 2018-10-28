package prompto.compiler;

public abstract class StackLocal implements IVerifierEntry {

	VerifierType type;
	String name;
	short index = -1;
	
	public StackLocal(VerifierType type, String name) {
		this.type = type;
		this.name = name;
	}
	
	@Override
	public VerifierType getType() {
		return type;
	}
	
	public String getName() {
		return name;
	}
	
	public void setIndex(short index) {
		this.index = index;
	}

	public short getIndex() {
		if(index==-1)
			throw new CompilerException("Local not registered: " + name); 
		return index;
	}

	public static class ObjectLocal extends StackLocal {

		ClassConstant className;
		ClassConstant downcastTo;
		
		public ObjectLocal(VerifierType type, String name, ClassConstant className) {
			super(type, name);
			this.className = className;
		}

		public ClassConstant getClassName() {
			return className;
		}
		
		public void markForAutodowncast(ClassConstant downcastTo) {
			this.downcastTo = downcastTo;
		}

		public void unmarkForAutodowncast() {
			this.downcastTo = null;
		}

		public ClassConstant getDowncastTo() {
			return downcastTo;
		}

		@Override
		public String toString() {
			return className.toString();
		}
		
		@Override
		public void register(ConstantsPool pool) {
			super.register(pool);
			className.register(pool);
		}
		
		@Override
		public int length() {
			/*
			Object_variable_info {
			    u1 tag = ITEM_Object; // 7
			    u2 cpool_index;
			}
			*/
			return 3;
		}

		@Override
		public void writeTo(ByteWriter writer) {
			/*
			Object_variable_info {
			    u1 tag = ITEM_Object; // 7
			    u2 cpool_index;
			}
			*/
			type.writeTo(writer);
			writer.writeU2(className.getIndexInConstantPool());
		}


	}
	
	static class ThisLocal extends ObjectLocal {

		public ThisLocal(ClassConstant className) {
			super(VerifierType.ITEM_UninitializedThis, "this", className);
		}
		
		@Override
		public int length() {
			return 1;
		}
		
		@Override
		public void writeTo(ByteWriter writer) {
			type.writeTo(writer);
		}
	}
	
	static class TopLocal extends ObjectLocal {

		public TopLocal(String name, ClassConstant className) {
			super(VerifierType.ITEM_Top, name, className);
		}
		
		@Override
		public int length() {
			return 1;
		}
		
		@Override
		public void writeTo(ByteWriter writer) {
			type.writeTo(writer);
		}
	}
	
	static class PrimitiveLocal extends StackLocal{

		public PrimitiveLocal(VerifierType type, String name) {
			super(type, name);
		}
		
		@Override
		public String toString() {
			return this.type.name();
		}
		
		@Override
		public int length() {
			/*
			union verification_type_info {
			    Top_variable_info;
			    Integer_variable_info;
			    Float_variable_info;
			    Long_variable_info;
			    Double_variable_info;
			    Null_variable_info;
			    UninitializedThis_variable_info; // EXCLUDED
			    Object_variable_info; // EXCLUDED
			    Uninitialized_variable_info; // EXCLUDED
			}
			*/
			return 1;
		}

		@Override
		public void writeTo(ByteWriter writer) {
			type.writeTo(writer);
		}
	}




	
}
