package prompto.compiler;

public enum DumpLevel {
	NONE,
	OPCODE,
	STACK;
	
	public static DumpLevel current() {
		return NONE;
	}
}
