package prompto.compiler;

import java.util.Stack;

public class StackState {
	
	private Stack<StackEntry> entries = new Stack<>();
	private Stack<StackLocal> locals = new Stack<>();
	private short currentSize = 0;

	@Override
	public String toString() {
		return "locals: " + locals.toString() + ", stack: " + entries.toString();
	}
	public Stack<StackEntry> getEntries() {
		return entries;
	}
	
	public Stack<StackLocal> getLocals() {
		return locals;
	}
	
	public short getCurrentSize() {
		return currentSize;
	}
	
	public void capture(StackState state, boolean entries, boolean locals) {
		if(entries) {
			this.entries = new Stack<>();
			this.entries.addAll(state.entries);
			this.currentSize = state.currentSize;
		}
		if(locals) {
			this.locals = new Stack<>();
			this.locals.addAll(state.locals);
		}
	}
	

	public int stackLength() {
		return length(entries);
	}

	public int localsLength() {
		return length(locals);
	}

	private int length(Stack<? extends IVerifierEntry> stack) {
		return (int)stack.stream().mapToInt((e)->
		e.length())
			.sum();
	}

	public StackEntry pushEntry(StackEntry entry) {
		if(DumpLevel.current()==DumpLevel.STACK)
			System.err.print("currentStackSize " + currentSize);
		StackEntry result = entries.push(entry);
		currentSize += entry.getType().size();
		if(DumpLevel.current()==DumpLevel.STACK)
			System.err.println(" -> " + currentSize);
		return result;
	}

	public StackEntry popEntry() {
		if(DumpLevel.current()==DumpLevel.STACK)
			System.err.print("currentStackSize " + currentSize);
		StackEntry result = entries.pop();
		currentSize -= result.getType().size();
		if(DumpLevel.current()==DumpLevel.STACK)
			System.err.println(" -> " + currentSize);
		return result;
	}

	public StackLocal pushLocal(StackLocal item) {
		return locals.push(item);
	}
	
	public StackLocal peekLocal() {
		return locals.peek();
	}

	public StackLocal popLocal() {
		return locals.pop();
	}

	public void register(ConstantsPool pool) {
		entries.forEach((e)->
			e.register(pool));
		locals.forEach((l)->
			l.register(pool));
	}



}
