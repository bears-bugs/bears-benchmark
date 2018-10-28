package prompto.compiler;

import java.lang.reflect.Type;

import prompto.compiler.CodeAttribute.CaptureStackState;
import prompto.compiler.IVerifierEntry.VerifierType;

public class ExceptionHandler implements IInstructionListener {

	boolean active = false;
	int startPC = -1;
	int endPC = -1;
	ClassConstant exception;
	StackState state;
	StackLabel label;
	
	public ExceptionHandler(Type type) {
		exception = type==null ? null : new ClassConstant(type);
	}

	public ClassConstant getException() {
		return exception;
	}
	
	public void setStackState(StackState state) {
		this.state = state;
	}
	
	public StackState getStackState() {
		return state;
	}

	public void setLabel(StackLabel label) {
		this.label = label;
	}
	
	public StackLabel getLabel() {
		return label;
	}

	public void register(ConstantsPool pool) {
		exception.register(pool);
	}

	@Override
	public void activate(Phase phase) {
		if(phase==Phase.WRITE)
			active = true;
	}
	
	@Override
	public void inhibit(Phase phase) {
		if(phase==Phase.WRITE)
			active = false;
	}
	
	@Override
	public void onAfterRehearse(IInstruction instruction) {
		if(instruction instanceof CaptureStackState)
			onAfterRehearse((CaptureStackState)instruction);
	}
	
	private void onAfterRehearse(CaptureStackState capture) {
		if(this.state==capture.getState()) {
			this.state.pushEntry(
					new StackEntry.ObjectEntry(
							VerifierType.ITEM_Object, 
								exception));
		}
	}
	
	@Override
	public void onBeforeWriteTo(ByteWriter writer, IInstruction instruction) {
		if(active) {
			if(startPC==-1)
				startPC = writer.length();
			endPC = writer.length();
		}
	}

	public void writeTo(ByteWriter writer) {
		/*
	    {   u2 start_pc;
	        u2 end_pc;
	        u2 handler_pc;
	        u2 catch_type;
	    } 
	    */
		if(startPC==-1)
			throw new UnsupportedOperationException();
		writer.writeU2(startPC);
		if(endPC==-1)
			throw new UnsupportedOperationException();
		writer.writeU2(endPC);
		if(label==null || label.getRealOffset()<0)
			throw new UnsupportedOperationException();
		writer.writeU2(label.getRealOffset());
		writer.writeU2(exception.getIndexInConstantPool());
	}

}
