package prompto.translate.oeo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestCast extends BaseOParserTest {

	@Test
	public void testAutoDowncast() throws Exception {
		compareResourceOEO("cast/autoDowncast.poc");
	}

	@Test
	public void testCastChild() throws Exception {
		compareResourceOEO("cast/castChild.poc");
	}

	@Test
	public void testCastMethod() throws Exception {
		compareResourceOEO("cast/castMethod.poc");
	}

	@Test
	public void testCastMissing() throws Exception {
		compareResourceOEO("cast/castMissing.poc");
	}

	@Test
	public void testCastNull() throws Exception {
		compareResourceOEO("cast/castNull.poc");
	}

	@Test
	public void testIsAChild() throws Exception {
		compareResourceOEO("cast/isAChild.poc");
	}

	@Test
	public void testIsAText() throws Exception {
		compareResourceOEO("cast/isAText.poc");
	}

}

