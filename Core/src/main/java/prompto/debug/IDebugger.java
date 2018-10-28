package prompto.debug;

import java.util.Collection;

import prompto.parser.ISection;

public interface IDebugger {

	void setListener(IDebugEventListener listener);
	void installBreakpoint(ISection section);
	boolean isTerminated();
	boolean canTerminate();
	void terminate();
	void notifyTerminated();
	Status getStatus(IThread thread);
	IStack<?> getStack(IThread thread);
	int getLine(IThread thread);
	boolean isStepping(IThread thread);
	boolean isSuspended(IThread thread);
	boolean canResume(IThread thread);
	boolean canSuspend(IThread thread);
	boolean canStepInto(IThread thread);
	boolean canStepOver(IThread thread);
	boolean canStepOut(IThread thread);
	void suspend(IThread thread);
	void resume(IThread thread);
	void stepInto(IThread thread);
	void stepOut(IThread thread);
	void stepOver(IThread thread);
	Collection<? extends IVariable> getVariables(IThread thread, IStackFrame frame);
}
