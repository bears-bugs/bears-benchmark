package prompto.translate.oeo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestMutability extends BaseOParserTest {

	@Test
	public void testImmutable() throws Exception {
		compareResourceOEO("mutability/immutable.poc");
	}

	@Test
	public void testImmutableArgument() throws Exception {
		compareResourceOEO("mutability/immutableArgument.poc");
	}

	@Test
	public void testImmutableDict() throws Exception {
		compareResourceOEO("mutability/immutableDict.poc");
	}

	@Test
	public void testImmutableList() throws Exception {
		compareResourceOEO("mutability/immutableList.poc");
	}

	@Test
	public void testImmutableMember() throws Exception {
		compareResourceOEO("mutability/immutableMember.poc");
	}

	@Test
	public void testImmutableTuple() throws Exception {
		compareResourceOEO("mutability/immutableTuple.poc");
	}

	@Test
	public void testMutable() throws Exception {
		compareResourceOEO("mutability/mutable.poc");
	}

	@Test
	public void testMutableArgument() throws Exception {
		compareResourceOEO("mutability/mutableArgument.poc");
	}

	@Test
	public void testMutableDict() throws Exception {
		compareResourceOEO("mutability/mutableDict.poc");
	}

	@Test
	public void testMutableList() throws Exception {
		compareResourceOEO("mutability/mutableList.poc");
	}

	@Test
	public void testMutableMember() throws Exception {
		compareResourceOEO("mutability/mutableMember.poc");
	}

	@Test
	public void testMutableTuple() throws Exception {
		compareResourceOEO("mutability/mutableTuple.poc");
	}

}

