package prompto.transpiler;

import java.util.Collections;

public interface IJSEngine {

	default boolean supportsDestructuring() { return true; }
	default Iterable<String> getPolyfills() { return Collections.emptyList(); }
	default boolean supportsClass() { return true; }
	
	static IJSEngine forUserAgent(String userAgent) {
		// TODO detect exact engine
		return new DefaultJSEngine();
	}
	
	public class DefaultJSEngine implements IJSEngine {

	}

}
