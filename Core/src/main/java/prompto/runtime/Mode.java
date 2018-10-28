package prompto.runtime;

public enum Mode {
	PRODUCTION,
	VALIDATION,
	INTEGRATION,
	DEVELOPMENT,
	UNITTEST;

	static Mode instance = PRODUCTION;
	
	public static void set(Mode mode) {
		if(mode!=null)
			instance = mode;
	}
	
	public static Mode get() {
		return instance;
	}
}
