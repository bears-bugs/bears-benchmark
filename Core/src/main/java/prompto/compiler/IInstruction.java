package prompto.compiler;

public interface IInstruction {

	default void rehearse(CodeAttribute code) {};
	default void register(ConstantsPool pool) {};
	default void writeTo(ByteWriter writer) {};

}
