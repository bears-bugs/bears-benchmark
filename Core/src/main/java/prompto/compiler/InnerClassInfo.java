package prompto.compiler;

import java.lang.reflect.Modifier;

public class InnerClassInfo {
	
	ClassFile classFile;
	ClassConstant innerClass;
	ClassConstant outerClass;
	Utf8Constant simpleName;
	int accessFlags = Tags.ACC_SUPER | Modifier.PUBLIC | Modifier.STATIC;

	public InnerClassInfo(ClassFile classFile, ClassConstant outerClass) {
		this.classFile = classFile;
		this.innerClass = classFile.getThisClass().clone();
		this.outerClass = outerClass;
		this.simpleName = new Utf8Constant(this.innerClass.getSimpleName());
	}
	
	public InnerClassInfo(ClassFile classFile, MethodInfo method) {
		this.classFile = classFile;
		this.innerClass = classFile.getThisClass().clone();
		this.outerClass = method.getClassFile().getThisClass();
		this.simpleName = new Utf8Constant(this.innerClass.getSimpleName());
	}

	public ClassFile getClassFile() {
		return classFile;
	}

	public void register(ConstantsPool pool) {
		innerClass.register(pool);
		outerClass.register(pool);
		simpleName.register(pool);
	}

	public void writeTo(ByteWriter writer) {
		/*
	    u2 inner_class_info_index;
        u2 outer_class_info_index;
        u2 inner_name_index;
        u2 inner_class_access_flags;
        */
		writer.writeU2(innerClass.getIndexInConstantPool());
		writer.writeU2(outerClass.getIndexInConstantPool());
		writer.writeU2(simpleName.getIndexInConstantPool());
		writer.writeU2(accessFlags);
	}


}
