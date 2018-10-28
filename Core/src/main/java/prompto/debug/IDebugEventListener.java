package prompto.debug;

public interface IDebugEventListener {
	void handleConnectedEvent(String host, int port);
	void handleResumedEvent(ResumeReason reason);
	void handleSuspendedEvent(SuspendReason reason);
	void handleTerminatedEvent();
}
