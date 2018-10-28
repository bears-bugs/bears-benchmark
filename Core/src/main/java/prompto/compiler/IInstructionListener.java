package prompto.compiler;

public interface IInstructionListener extends IOperand {

	public static enum Phase {
		REHEARSE,
		REGISTER,
		WRITE
	}
	
	default void activate(Phase phase) {};
	default void inhibit(Phase phase) {};
	default void onBeforeRehearse(IInstruction instruction) {};
	default void onAfterRehearse(IInstruction instruction) {};
	default void onBeforeRegister(IInstruction instruction) {};
	default void onAfterRegister(IInstruction instruction) {};
	default void onBeforeWriteTo(ByteWriter writer, IInstruction instruction) {};
	default void onAfterWriteTo(ByteWriter writer, IInstruction instruction) {};
}
