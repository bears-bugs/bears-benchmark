package prompto.runtime.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.o.BaseOParserTest;
import prompto.runtime.utils.Out;

public class TestResource extends BaseOParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedReadResource() throws Exception {
		checkInterpretedOutput("resource/readResource.poc");
	}

	@Test
	public void testCompiledReadResource() throws Exception {
		checkCompiledOutput("resource/readResource.poc");
	}

	@Test
	public void testTranspiledReadResource() throws Exception {
		checkTranspiledOutput("resource/readResource.poc");
	}

	@Test
	public void testInterpretedReadWithResource() throws Exception {
		checkInterpretedOutput("resource/readWithResource.poc");
	}

	@Test
	public void testCompiledReadWithResource() throws Exception {
		checkCompiledOutput("resource/readWithResource.poc");
	}

	@Test
	public void testTranspiledReadWithResource() throws Exception {
		checkTranspiledOutput("resource/readWithResource.poc");
	}

	@Test
	public void testInterpretedWriteResource() throws Exception {
		checkInterpretedOutput("resource/writeResource.poc");
	}

	@Test
	public void testCompiledWriteResource() throws Exception {
		checkCompiledOutput("resource/writeResource.poc");
	}

	@Test
	public void testTranspiledWriteResource() throws Exception {
		checkTranspiledOutput("resource/writeResource.poc");
	}

	@Test
	public void testInterpretedWriteWithResource() throws Exception {
		checkInterpretedOutput("resource/writeWithResource.poc");
	}

	@Test
	public void testCompiledWriteWithResource() throws Exception {
		checkCompiledOutput("resource/writeWithResource.poc");
	}

	@Test
	public void testTranspiledWriteWithResource() throws Exception {
		checkTranspiledOutput("resource/writeWithResource.poc");
	}

}

