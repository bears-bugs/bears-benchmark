package prompto.compiler;

public interface IVerifierEntry {
	
	VerifierType getType();
	int length();
	default void register(ConstantsPool pool) {} // nothing to do
	void writeTo(ByteWriter writer);

	public static enum VerifierType {
		ITEM_Top(TopFactory.instance),
		ITEM_Integer(PrimitiveFactory.instance),
		ITEM_Float(PrimitiveFactory.instance),
		ITEM_Double(2, PrimitiveFactory.instance),
		ITEM_Long(2, PrimitiveFactory.instance),
		ITEM_Null(PrimitiveFactory.instance),
		ITEM_UninitializedThis(ThisFactory.instance),
		ITEM_Object(ObjectFactory.instance),
		ITEM_Uninitialized(ObjectFactory.instance);
		
		static interface IFactory {
			StackEntry newStackEntry(VerifierType type, ClassConstant className);
			StackLocal newStackLocal(VerifierType type, String name, ClassConstant className);
		}
		
		static class PrimitiveFactory implements IFactory {
			
			static PrimitiveFactory instance = new PrimitiveFactory();
			
			@Override
			public StackEntry newStackEntry(VerifierType type, ClassConstant className) {
				return new StackEntry.PrimitiveEntry(type);
			}
			
			@Override
			public StackLocal newStackLocal(VerifierType type, String name, ClassConstant className) {
				return new StackLocal.PrimitiveLocal(type, name);
			}
		};

		static class TopFactory implements IFactory {
			
			static TopFactory instance = new TopFactory();
			
			@Override
			public StackEntry newStackEntry(VerifierType type, ClassConstant className) {
				return new StackEntry.TopEntry(type, className);
			}
			
			@Override
			public StackLocal newStackLocal(VerifierType type, String name, ClassConstant className) {
				return new StackLocal.TopLocal(name, className);
			}
		};

		static class ObjectFactory implements IFactory {
			
			static ObjectFactory instance = new ObjectFactory();
			
			@Override
			public StackEntry newStackEntry(VerifierType type, ClassConstant className) {
				return new StackEntry.ObjectEntry(type, className);
			}
			
			@Override
			public StackLocal newStackLocal(VerifierType type, String name, ClassConstant className) {
				return new StackLocal.ObjectLocal(type, name, className);
			}
		};
	
		static class ThisFactory extends ObjectFactory {
			
			static ThisFactory instance = new ThisFactory();
			
			@Override
			public StackLocal newStackLocal(VerifierType type, String name, ClassConstant className) {
				if(!"this".equals(name))
					throw new UnsupportedOperationException();
				return new StackLocal.ThisLocal(className);
			}
		};

		final short size;
		final IFactory entryFactory;
		
		private VerifierType() {
			size = 1;
			entryFactory = null;
		}
		
		private VerifierType(IFactory entryFactory) {
			size = 1;
			this.entryFactory = entryFactory;
		}

		private VerifierType(int size, IFactory entryFactory) {
			this.size = (short)size;
			this.entryFactory = entryFactory;
		}
	
		public void writeTo(ByteWriter writer) {
			writer.writeU1(ordinal());
		}
		
		public short size() {
			return size;
		}
	
		public StackEntry newStackEntry(ClassConstant className) {
			if(entryFactory==null)
				throw new UnsupportedOperationException();
			return entryFactory.newStackEntry(this, className);
		}

		public StackLocal newStackLocal(String name, ClassConstant className) {
			if(entryFactory==null)
				throw new UnsupportedOperationException();
			return entryFactory.newStackLocal(this, name, className);
		}

		public static VerifierType fromDescriptor(Descriptor descriptor) {
			return fromDescriptor(descriptor.toString());
		}

		public static VerifierType fromDescriptor(String desc) {
			switch(desc.charAt(0)) {
			case 'Z': // boolean
			case 'B': // byte
			case 'S': // short
			case 'I': // int
			case 'C': // char
				return ITEM_Integer;
			case 'F': // float
				return ITEM_Float;
			case 'D': // double
				return ITEM_Double;
			case 'J':
				return ITEM_Long;
			case '[': // array
			case 'L':
				return ITEM_Object;
			case 'V':
				return null;
			default:
				throw new UnsupportedOperationException(desc);
			}
		}



	}

}
