package prompto.code;

import static org.junit.Assert.*;

import org.junit.Test;

import prompto.intrinsic.PromptoVersion;

public class TestVersion {

	@Test
	public void test() {
		String s = "1.2.3";
		PromptoVersion v = PromptoVersion.parse(s);
		assertEquals(s, v.toString());
	}

}
