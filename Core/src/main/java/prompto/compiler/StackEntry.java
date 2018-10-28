package prompto.compiler;


public abstract class StackEntry implements IVerifierEntry {

	VerifierType type;
	
	protected StackEntry(VerifierType type) {
		this.type = type;
	}
	
	@Override
	public VerifierType getType() {
		return type;
	}
	
	static class ObjectEntry extends StackEntry {

		ClassConstant className;
		
		public ObjectEntry(VerifierType type, ClassConstant className) {
			super(type);
			this.className = className;
		}
		
		@Override
		public String toString() {
			return className.toString();
		}
		
		public void setClassName(ClassConstant className) {
			this.className = className;
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
	
	static class PrimitiveEntry extends StackEntry {

		public PrimitiveEntry(VerifierType type) {
			super(type);
		}
		
		@Override
		public String toString() {
			return type.name();
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
			    UninitializedThis_variable_info;
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
	
	static class TopEntry extends PrimitiveEntry {

		ClassConstant className;
		
		public TopEntry(VerifierType type, ClassConstant className) {
			super(type);
			this.className = className;
		}
		
		public ClassConstant getClassName() {
			return className;
		}
		
		@Override
		public String toString() {
			return className==null ? super.toString() : className.toString();
		}

		
	}

}
