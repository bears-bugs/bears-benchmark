package prompto.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestLogger {

	static Logger logger = new Logger();
	
	@Test
	public void testThatLoggedClassIsReadProperly() {
		String klass = logger.getLoggedClass();
		assertEquals(TestLogger.class.getName(), klass);
	}
}
