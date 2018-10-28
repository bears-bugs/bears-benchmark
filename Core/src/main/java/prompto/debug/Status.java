package prompto.debug;

public enum Status {
	STARTING,
	RUNNING,
	SUSPENDED,
	TERMINATING,
	TERMINATED,
	UNREACHABLE;
	
	@Override
	public String toString() {
		return name().substring(0,1) + name().substring(1).toLowerCase();
	}
}