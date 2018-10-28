package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestMutability extends BaseEParserTest {

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
		checkInterpretedOutput("mutability/immutable.pec");
	}

	@Test
	public void testCompiledImmutable() throws Exception {
		checkCompiledOutput("mutability/immutable.pec");
	}

	@Test
	public void testTranspiledImmutable() throws Exception {
		checkTranspiledOutput("mutability/immutable.pec");
	}

	@Test
	public void testInterpretedImmutableArgument() throws Exception {
		checkInterpretedOutput("mutability/immutableArgument.pec");
	}

	@Test
	public void testCompiledImmutableArgument() throws Exception {
		checkCompiledOutput("mutability/immutableArgument.pec");
	}

	@Test
	public void testTranspiledImmutableArgument() throws Exception {
		checkTranspiledOutput("mutability/immutableArgument.pec");
	}

	@Test
	public void testInterpretedImmutableDict() throws Exception {
		checkInterpretedOutput("mutability/immutableDict.pec");
	}

	@Test
	public void testCompiledImmutableDict() throws Exception {
		checkCompiledOutput("mutability/immutableDict.pec");
	}

	@Test
	public void testTranspiledImmutableDict() throws Exception {
		checkTranspiledOutput("mutability/immutableDict.pec");
	}

	@Test
	public void testInterpretedImmutableList() throws Exception {
		checkInterpretedOutput("mutability/immutableList.pec");
	}

	@Test
	public void testCompiledImmutableList() throws Exception {
		checkCompiledOutput("mutability/immutableList.pec");
	}

	@Test
	public void testTranspiledImmutableList() throws Exception {
		checkTranspiledOutput("mutability/immutableList.pec");
	}

	@Test
	public void testInterpretedImmutableMember() throws Exception {
		checkInterpretedOutput("mutability/immutableMember.pec");
	}

	@Test
	public void testCompiledImmutableMember() throws Exception {
		checkCompiledOutput("mutability/immutableMember.pec");
	}

	@Test
	public void testTranspiledImmutableMember() throws Exception {
		checkTranspiledOutput("mutability/immutableMember.pec");
	}

	@Test
	public void testInterpretedImmutableTuple() throws Exception {
		checkInterpretedOutput("mutability/immutableTuple.pec");
	}

	@Test
	public void testCompiledImmutableTuple() throws Exception {
		checkCompiledOutput("mutability/immutableTuple.pec");
	}

	@Test
	public void testTranspiledImmutableTuple() throws Exception {
		checkTranspiledOutput("mutability/immutableTuple.pec");
	}

	@Test
	public void testInterpretedMutable() throws Exception {
		checkInterpretedOutput("mutability/mutable.pec");
	}

	@Test
	public void testCompiledMutable() throws Exception {
		checkCompiledOutput("mutability/mutable.pec");
	}

	@Test
	public void testTranspiledMutable() throws Exception {
		checkTranspiledOutput("mutability/mutable.pec");
	}

	@Test
	public void testInterpretedMutableArgument() throws Exception {
		checkInterpretedOutput("mutability/mutableArgument.pec");
	}

	@Test
	public void testCompiledMutableArgument() throws Exception {
		checkCompiledOutput("mutability/mutableArgument.pec");
	}

	@Test
	public void testTranspiledMutableArgument() throws Exception {
		checkTranspiledOutput("mutability/mutableArgument.pec");
	}

	@Test
	public void testInterpretedMutableDict() throws Exception {
		checkInterpretedOutput("mutability/mutableDict.pec");
	}

	@Test
	public void testCompiledMutableDict() throws Exception {
		checkCompiledOutput("mutability/mutableDict.pec");
	}

	@Test
	public void testTranspiledMutableDict() throws Exception {
		checkTranspiledOutput("mutability/mutableDict.pec");
	}

	@Test
	public void testInterpretedMutableList() throws Exception {
		checkInterpretedOutput("mutability/mutableList.pec");
	}

	@Test
	public void testCompiledMutableList() throws Exception {
		checkCompiledOutput("mutability/mutableList.pec");
	}

	@Test
	public void testTranspiledMutableList() throws Exception {
		checkTranspiledOutput("mutability/mutableList.pec");
	}

	@Test
	public void testInterpretedMutableMember() throws Exception {
		checkInterpretedOutput("mutability/mutableMember.pec");
	}

	@Test
	public void testCompiledMutableMember() throws Exception {
		checkCompiledOutput("mutability/mutableMember.pec");
	}

	@Test
	public void testTranspiledMutableMember() throws Exception {
		checkTranspiledOutput("mutability/mutableMember.pec");
	}

	@Test
	public void testInterpretedMutableTuple() throws Exception {
		checkInterpretedOutput("mutability/mutableTuple.pec");
	}

	@Test
	public void testCompiledMutableTuple() throws Exception {
		checkCompiledOutput("mutability/mutableTuple.pec");
	}

	@Test
	public void testTranspiledMutableTuple() throws Exception {
		checkTranspiledOutput("mutability/mutableTuple.pec");
	}

}

