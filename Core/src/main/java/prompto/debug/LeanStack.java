package prompto.debug;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class LeanStack extends ArrayList<LeanStackFrame> implements IStack<LeanStackFrame> {

	public LeanStack() {
	}
	
	public LeanStack(IStack<?> stack) {
		stack.forEach((f)->add(new LeanStackFrame(f)));
	}
	
}

