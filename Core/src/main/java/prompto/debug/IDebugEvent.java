package prompto.debug;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface IDebugEvent {

	@JsonIgnore
	Type getType();
	void execute(IDebugEventListener listener);

	static class ConnectedEvent implements IDebugEvent {
		
		String host;
		int port;
		
		public ConnectedEvent() {
		}
		
		public ConnectedEvent(String host, int port) {
			this.host = host;
			this.port = port;
		}

		public void setHost(String host) {
			this.host = host;
		}
		
		public void setPort(int port) {
			this.port = port;
		}
		
		public String getHost() {
			return host;
		}
		
		public int getPort() {
			return port;
		}
		
		@Override
		public Type getType() {
			return Type.CONNECTED;
		}
		
		@Override
		public void execute(IDebugEventListener listener) {
			listener.handleConnectedEvent(host, port);
		}
	}
	
	static class SuspendedEvent implements IDebugEvent {
		
		SuspendReason reason;
		
		public SuspendedEvent() {
		}
		
		
		public SuspendedEvent(SuspendReason reason) {
			setReason(reason);
		}

		
		public void setReason(SuspendReason reason) {
			this.reason = reason;
		}
		
		
		public SuspendReason getReason() {
			return reason;
		}
		
		@Override
		public Type getType() {
			return Type.SUSPENDED;
		}
		
		@Override
		public void execute(IDebugEventListener listener) {
			listener.handleSuspendedEvent(reason);
		}
	}
	
	static class ResumedEvent implements IDebugEvent {
		
		ResumeReason reason;
		
		
		public ResumedEvent() {
		}
		
		public ResumedEvent(ResumeReason reason) {
			setReason(reason);
		}
		
		
		public void setReason(ResumeReason reason) {
			this.reason = reason;
		}
		
		public ResumeReason getReason() {
			return reason;
		}
		
		
		@Override
		public Type getType() {
			return Type.RESUMED;
		}
		
		@Override
		public void execute(IDebugEventListener listener) {
			listener.handleResumedEvent(reason);
		}
	}

	static class TerminatedEvent implements IDebugEvent {

		@Override
		public Type getType() {
			return Type.TERMINATED;
		}
		
		@Override
		public void execute(IDebugEventListener listener) {
			listener.handleTerminatedEvent();
		}
	}

	public enum Type {
		CONNECTED(ConnectedEvent.class),
		SUSPENDED(SuspendedEvent.class),
		RESUMED(ResumedEvent.class),
		TERMINATED(TerminatedEvent.class);

		Class<? extends IDebugEvent> klass;
		
		Type(Class<? extends IDebugEvent> klass) {
			this.klass = klass;
		}
		
		public Class<? extends IDebugEvent> getKlass() {
			return klass;
		}
	}

}
