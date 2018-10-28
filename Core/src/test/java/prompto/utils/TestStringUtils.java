package prompto.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestStringUtils {
	
	@Test
	public void doubleQuotesAreEscaped() {
		String source = "some \" quotes";
		String escaped = StringUtils.escape(source);
		assertEquals("some \\\" quotes", escaped);
	}

}
