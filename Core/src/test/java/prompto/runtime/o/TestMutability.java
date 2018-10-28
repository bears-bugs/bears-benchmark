package prompto.runtime.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.o.BaseOParserTest;
import prompto.runtime.utils.Out;

public class TestMutability extends BaseOParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedImmutable() throws Exception {
		checkInterpretedOutput("mutability/immutable.poc");
	}

	@Test
	public void testCompiledImmutable() throws Exception {
		checkCompiledOutput("mutability/immutable.poc");
	}

	@Test
	public void testTranspiledImmutable() throws Exception {
		checkTranspiledOutput("mutability/immutable.poc");
	}

	@Test
	public void testInterpretedImmutableArgument() throws Exception {
		checkInterpretedOutput("mutability/immutableArgument.poc");
	}

	@Test
	public void testCompiledImmutableArgument() throws Exception {
		checkCompiledOutput("mutability/immutableArgument.poc");
	}

	@Test
	public void testTranspiledImmutableArgument() throws Exception {
		checkTranspiledOutput("mutability/immutableArgument.poc");
	}

	@Test
	public void testInterpretedImmutableDict() throws Exception {
		checkInterpretedOutput("mutability/immutableDict.poc");
	}

	@Test
	public void testCompiledImmutableDict() throws Exception {
		checkCompiledOutput("mutability/immutableDict.poc");
	}

	@Test
	public void testTranspiledImmutableDict() throws Exception {
		checkTranspiledOutput("mutability/immutableDict.poc");
	}

	@Test
	public void testInterpretedImmutableList() throws Exception {
		checkInterpretedOutput("mutability/immutableList.poc");
	}

	@Test
	public void testCompiledImmutableList() throws Exception {
		checkCompiledOutput("mutability/immutableList.poc");
	}

	@Test
	public void testTranspiledImmutableList() throws Exception {
		checkTranspiledOutput("mutability/immutableList.poc");
	}

	@Test
	public void testInterpretedImmutableMember() throws Exception {
		checkInterpretedOutput("mutability/immutableMember.poc");
	}

	@Test
	public void testCompiledImmutableMember() throws Exception {
		checkCompiledOutput("mutability/immutableMember.poc");
	}

	@Test
	public void testTranspiledImmutableMember() throws Exception {
		checkTranspiledOutput("mutability/immutableMember.poc");
	}

	@Test
	public void testInterpretedImmutableTuple() throws Exception {
		checkInterpretedOutput("mutability/immutableTuple.poc");
	}

	@Test
	public void testCompiledImmutableTuple() throws Exception {
		checkCompiledOutput("mutability/immutableTuple.poc");
	}

	@Test
	public void testTranspiledImmutableTuple() throws Exception {
		checkTranspiledOutput("mutability/immutableTuple.poc");
	}

	@Test
	public void testInterpretedMutable() throws Exception {
		checkInterpretedOutput("mutability/mutable.poc");
	}

	@Test
	public void testCompiledMutable() throws Exception {
		checkCompiledOutput("mutability/mutable.poc");
	}

	@Test
	public void testTranspiledMutable() throws Exception {
		checkTranspiledOutput("mutability/mutable.poc");
	}

	@Test
	public void testInterpretedMutableArgument() throws Exception {
		checkInterpretedOutput("mutability/mutableArgument.poc");
	}

	@Test
	public void testCompiledMutableArgument() throws Exception {
		checkCompiledOutput("mutability/mutableArgument.poc");
	}

	@Test
	public void testTranspiledMutableArgument() throws Exception {
		checkTranspiledOutput("mutability/mutableArgument.poc");
	}

	@Test
	public void testInterpretedMutableDict() throws Exception {
		checkInterpretedOutput("mutability/mutableDict.poc");
	}

	@Test
	public void testCompiledMutableDict() throws Exception {
		checkCompiledOutput("mutability/mutableDict.poc");
	}

	@Test
	public void testTranspiledMutableDict() throws Exception {
		checkTranspiledOutput("mutability/mutableDict.poc");
	}

	@Test
	public void testInterpretedMutableList() throws Exception {
		checkInterpretedOutput("mutability/mutableList.poc");
	}

	@Test
	public void testCompiledMutableList() throws Exception {
		checkCompiledOutput("mutability/mutableList.poc");
	}

	@Test
	public void testTranspiledMutableList() throws Exception {
		checkTranspiledOutput("mutability/mutableList.poc");
	}

	@Test
	public void testInterpretedMutableMember() throws Exception {
		checkInterpretedOutput("mutability/mutableMember.poc");
	}

	@Test
	public void testCompiledMutableMember() throws Exception {
		checkCompiledOutput("mutability/mutableMember.poc");
	}

	@Test
	public void testTranspiledMutableMember() throws Exception {
		checkTranspiledOutput("mutability/mutableMember.poc");
	}

	@Test
	public void testInterpretedMutableTuple() throws Exception {
		checkInterpretedOutput("mutability/mutableTuple.poc");
	}

	@Test
	public void testCompiledMutableTuple() throws Exception {
		checkCompiledOutput("mutability/mutableTuple.poc");
	}

	@Test
	public void testTranspiledMutableTuple() throws Exception {
		checkTranspiledOutput("mutability/mutableTuple.poc");
	}

}

