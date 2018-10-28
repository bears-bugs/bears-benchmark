package prompto.debug;

public class ClientVariable extends LeanVariable {

	public ClientVariable(IThread thread, IStackFrame frame, LeanVariable variable) {
		super(variable);
	}

}
