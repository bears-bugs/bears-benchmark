package prompto.debug;

import java.util.Collection;

public class ClientStackFrame extends LeanStackFrame {

	IDebugger debugger;
	IThread thread;
	
	public ClientStackFrame(IDebugger debugger, IThread thread, LeanStackFrame frame) {
		super(frame);
		this.debugger = debugger;
		this.thread = thread;
	}
	
	@Override
	public Collection<? extends IVariable> getVariables() {
		return debugger.getVariables(thread, this);
	}
}