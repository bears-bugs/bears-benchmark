package prompto.debug;

import java.util.ArrayDeque;

public class ServerStack extends ArrayDeque<ServerStackFrame> implements IStack<ServerStackFrame> {

	private static final long serialVersionUID = 1L;

	public ServerStackFrame find(IStackFrame frame) {
		for(ServerStackFrame f : this) {
			if(f.equals(frame))
				return f;
		}
		return null;
	}

	
}
