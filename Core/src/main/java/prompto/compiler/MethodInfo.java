package prompto.compiler;

import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import prompto.compiler.IVerifierEntry.VerifierType;

public class MethodInfo {
	
	int accessFlags = Modifier.PUBLIC;
	Utf8Constant name;
	Utf8Constant signature;
	List<IAttribute> attributes = new LinkedList<>();
	CodeAttribute codeAttribute = null;
	Descriptor descriptor;
	ClassFile classFile;
	
	MethodInfo(ClassFile classFile, String name, Descriptor.Method descriptor) {
		this.classFile = classFile;
		this.descriptor = descriptor;
		this.name = new Utf8Constant(name);
		this.signature = new Utf8Constant(descriptor.toString());
	}
	
	public ClassFile getClassFile() {
		return classFile;
	}

	public Utf8Constant getName() {
		return name;
	}
	
	public Utf8Constant getSignature() {
		return signature;
	}
	
	public CodeAttribute getCodeAttribute() {
		return codeAttribute;
	}

	public LocalVariableTableAttribute getLocals() {
		ensureCodeAttribute();
		return codeAttribute.getLocals();
	}

	@Override
	public String toString() {
		return name.toString() + '/' + signature.toString();
	}
	
	public void addModifier(int modifier) {
		accessFlags |= modifier;
	}
	
	public boolean isStatic() {
		return (accessFlags & Modifier.STATIC) != 0;
	}


	public IInstruction addInstruction(Opcode op, IOperand ... operands) {
		return addInstruction(new Instruction(op, operands));
	}
	
	public <T extends IInstruction> T addInstruction(T instruction) {
		ensureCodeAttribute();
		return codeAttribute.addInstruction(instruction);
	}

	public <T extends IInstructionListener> T addOffsetListener(T listener) {
		ensureCodeAttribute();
		return codeAttribute.addOffsetListener(listener);
	}
	
	public IInstruction activateOffsetListener(IInstructionListener listener) {
		return codeAttribute.activateOffsetListener(listener);
	}

	public IInstruction inhibitOffsetListener(IInstructionListener listener) {
		return codeAttribute.inhibitOffsetListener(listener);
	}

	public StackLocal registerLocal(String name, VerifierType type, ClassConstant className) {
		ensureCodeAttribute();
		return codeAttribute.registerLocal(type.newStackLocal(name, className));
	}
	
	/* a work-around to get a unique transient variable name */
	/* the real solution is to manage variable scope */
	public String nextTransientName(String core) {
		ensureCodeAttribute();
		return codeAttribute.nextTransientName(core);
	}

	public StackLocal getRegisteredLocal(String name) {
		ensureCodeAttribute();
		return codeAttribute.getRegisteredLocal(name);
	}
	
	public void unregisterLocal(StackLocal local) {
		codeAttribute.unregisterLocal(local);
	}

	public ExceptionHandler registerExceptionHandler(java.lang.reflect.Type type) {
		ensureCodeAttribute();
		return codeAttribute.registerExceptionHandler(type);
	}

	public void placeExceptionHandler(ExceptionHandler handler) {
		codeAttribute.placeExceptionHandler(handler);
	}

	void register(ConstantsPool pool) {
		if(DumpLevel.current().ordinal()>0)
			System.err.println("Registering method: " + this.toString());
		name.register(pool);
		signature.register(pool);
		attributes.forEach((a)->
			a.register(pool));
	}

	public CodeAttribute ensureCodeAttribute() {
		if(codeAttribute==null) {
			codeAttribute = new CodeAttribute();
			attributes.add(codeAttribute);
		}
		return codeAttribute;
	}

	public StackLocals captureStackLocals() {
		// register stack locals, both planned (StackState) and current (locals)
		StackLocals locals = new StackLocals();
		locals.stackState = captureStackState(); 
		locals.numLocals = getLocals().numLocals();
		return locals;
	}
	
	public void restoreStackLocals(StackLocals stackLocals) {
		// restore stack locals
		List<StackLocal> locals = getLocals().getEntries();
		ListIterator<StackLocal> iterLocals = locals.listIterator(locals.size());
		while(iterLocals.hasPrevious()) {
			StackLocal local = iterLocals.previous();
			if(local.getIndex()>=stackLocals.numLocals)
				unregisterLocal(local);
			else
				break;
		}
		restoreStackLocals(stackLocals.stackState);
	}



	public StackState captureStackState() {
		ensureCodeAttribute();
		return codeAttribute.captureStackState();
	}

	public void restoreFullStackState(StackState state) {
		codeAttribute.restoreFullStackState(state);
	}
	
	public void restoreStackLocals(StackState state) {
		codeAttribute.restoreStackLocals(state);
	}

	public StackLabel placeLabel(StackState state) {
		ensureCodeAttribute();
		return codeAttribute.placeLabel(state);
	}


	void writeTo(ByteWriter writer) {
		/*
		method_info {
		    u2             access_flags;
		    u2             name_index;
		    u2             descriptor_index;
		    u2             attributes_count;
		    attribute_info attributes[attributes_count];
		}
		*/
		writer.writeU2(accessFlags);
		writer.writeU2(name.getIndexInConstantPool());
		writer.writeU2(signature.getIndexInConstantPool());
		writer.writeU2(attributes.size());
		attributes.forEach((a)->
			a.writeTo(writer));
	}




}
