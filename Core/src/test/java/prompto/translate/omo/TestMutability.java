package prompto.translate.omo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestMutability extends BaseOParserTest {

	@Test
	public void testImmutable() throws Exception {
		compareResourceOMO("mutability/immutable.poc");
	}

	@Test
	public void testImmutableArgument() throws Exception {
		compareResourceOMO("mutability/immutableArgument.poc");
	}

	@Test
	public void testImmutableDict() throws Exception {
		compareResourceOMO("mutability/immutableDict.poc");
	}

	@Test
	public void testImmutableList() throws Exception {
		compareResourceOMO("mutability/immutableList.poc");
	}

	@Test
	public void testImmutableMember() throws Exception {
		compareResourceOMO("mutability/immutableMember.poc");
	}

	@Test
	public void testImmutableTuple() throws Exception {
		compareResourceOMO("mutability/immutableTuple.poc");
	}

	@Test
	public void testMutable() throws Exception {
		compareResourceOMO("mutability/mutable.poc");
	}

	@Test
	public void testMutableArgument() throws Exception {
		compareResourceOMO("mutability/mutableArgument.poc");
	}

	@Test
	public void testMutableDict() throws Exception {
		compareResourceOMO("mutability/mutableDict.poc");
	}

	@Test
	public void testMutableList() throws Exception {
		compareResourceOMO("mutability/mutableList.poc");
	}

	@Test
	public void testMutableMember() throws Exception {
		compareResourceOMO("mutability/mutableMember.poc");
	}

	@Test
	public void testMutableTuple() throws Exception {
		compareResourceOMO("mutability/mutableTuple.poc");
	}

}

