package prompto.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import prompto.runtime.utils.Out;

public class TestLimits {

	@Test
	public void testParseMaxLong() {
		Long l1 = Long.MAX_VALUE;
		String s = l1.toString();
		long l2 = Long.parseLong(s);
		assertEquals(Long.MAX_VALUE, l2);
	}

	@Test
	public void testParseMinLong() {
		Long l1 = Long.MIN_VALUE;
		String s = l1.toString();
		long l2 = Long.parseLong(s);
		assertEquals(Long.MIN_VALUE, l2);
	}

	@Test
	public void testPrintLong() {
		Out.init();
		long l = 9876543210L;
		Object o = Long.valueOf(l);
		System.out.print(o);
		String s = Out.read();
		Out.restore();
		assertEquals(s, o.toString());
	}
	
}
