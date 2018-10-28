package prompto.verifier;

import prompto.compiler.ClassFile;
import prompto.compiler.IConstantOperand;

public class Klass {

	ClassFile classFile;
	
	public Klass(ClassFile classFile) {
		this.classFile = classFile;
	}

	public String name() {
		return classFile.getThisClass().getClassName().getValue();
	}

	public boolean is_anonymous() {
		String[] names = name().split("$");
		return Character.isDigit(names[names.length-1].charAt(0));
	}

	public Klass host_klass() {
		throw new UnsupportedOperationException();
	}

	public IConstantOperand constantWithIndex(int index) {
		return classFile.getConstantsPool().constantWithIndex(index);
	}

	public boolean is_interface() {
		return classFile.isInterface();
	}

	public String superclass_name() {
		return classFile.getSuperClass().getClassName().getValue();
	}

	public int tag_at(short index) {
		IConstantOperand operand = constantWithIndex(index);
		return operand.getTag();
	}

}
