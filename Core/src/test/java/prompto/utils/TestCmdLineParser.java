package prompto.utils;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import prompto.utils.CmdLineParser;

public class TestCmdLineParser {

	Map<String,String> options;
	
	@Test
	public void testNull() throws Exception {
		options = CmdLineParser.parse((String)null);
		assertNotNull(options);
	}

	@Test
	public void testEmpty() throws Exception {
		options = CmdLineParser.parse("");
		assertNotNull(options);
	}

	@Test
	public void testKVP1() throws Exception {
		options = CmdLineParser.parse("a=b");
		assertEquals("b",options.get("a"));
	}

	@Test
	public void testKVP2() throws Exception {
		options = CmdLineParser.parse("a = b");
		assertEquals("b",options.get("a"));
	}
	
	@Test
	public void testKVP3() throws Exception {
		options = CmdLineParser.parse("-a=b");
		assertEquals("b",options.get("a"));
	}

	@Test
	public void testKVP4() throws Exception {
		options = CmdLineParser.parse("123=444");
		assertEquals("444",options.get("123"));
	}
	
	@Test
	public void testKVP5() throws Exception {
		options = CmdLineParser.parse("-a=b c=d e=f");
		assertEquals("b",options.get("a"));
		assertEquals("d",options.get("c"));
		assertEquals("f",options.get("e"));
	}

	@Test
	public void testKVP6() throws Exception {
		options = CmdLineParser.parse("123=\"444 -qlsdkj ==22\"");
		assertEquals("444 -qlsdkj ==22",options.get("123"));
	}


}
