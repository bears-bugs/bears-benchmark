package prompto.debug;

import java.util.ArrayList;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
public class ClientStack extends ArrayList<ClientStackFrame> implements IStack<ClientStackFrame> {

	public ClientStack(IDebugger debugger, IThread thread, LeanStack stack) {
		addAll(stack.stream().map((f)->new ClientStackFrame(debugger, thread, f)).collect(Collectors.toList()));
	}

}
