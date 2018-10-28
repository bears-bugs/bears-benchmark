package prompto.compiler;

import prompto.compiler.IVerifierEntry.VerifierType;

public class ClassConstant implements ICodeConstant, IValueConstant {

	java.lang.reflect.Type type;
	Utf8Constant className;
	int index = -1;
	
	public ClassConstant(java.lang.reflect.Type type) {
		this.type = type;
		this.className = new Utf8Constant(CompilerUtils.makeClassName(type));
	}

	@Override
	public int getTag() {
		return Tags.CONSTANT_Class;
	}
	

	public ClassConstant clone() {
		return new ClassConstant(this.type);
	}
	
	public java.lang.reflect.Type getType() {
		return type;
	}
	
	@Override
	public StackEntry toStackEntry() {	
		return VerifierType.ITEM_Object.newStackEntry(this); // for dumping LDC_W <class> opcode
	}
	
	public ClassConstant toArray() {
		String className = this.className.toString().replace('/', '.') + "[]";
		if(type instanceof Class) try {
			Class<?> klass = Class.forName(className);
			return new ClassConstant(klass);
		} catch (Exception e) {
		}
		return new ClassConstant(new PromptoType(className));
	}
	
	public boolean isInterface() {
		return new ResultInfo(type).isInterface();
	}

	public Utf8Constant getClassName() {
		return className;
	}
	
	public String getSimpleName() {
		String fullName = className.getValue();
		return fullName.substring(fullName.lastIndexOf('/')+1);
	}

	@Override
	public String toString() {
		return this.className.toString();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof ClassConstant
				&& className.equals(((ClassConstant)obj).className);
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
		className.register(pool);
	}
	
	@Override
	public void writeTo(ByteWriter writer) {
		/*
		CONSTANT_Class_info {
		    u1 tag;
		    u2 name_index;
		}
		*/
		writer.writeU1(Tags.CONSTANT_Class);
		writer.writeU2(className.getIndexInConstantPool());
	}


}
