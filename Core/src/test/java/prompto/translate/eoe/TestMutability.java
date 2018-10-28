package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestMutability extends BaseEParserTest {

	@Test
	public void testImmutable() throws Exception {
		compareResourceEOE("mutability/immutable.pec");
	}

	@Test
	public void testImmutableArgument() throws Exception {
		compareResourceEOE("mutability/immutableArgument.pec");
	}

	@Test
	public void testImmutableDict() throws Exception {
		compareResourceEOE("mutability/immutableDict.pec");
	}

	@Test
	public void testImmutableList() throws Exception {
		compareResourceEOE("mutability/immutableList.pec");
	}

	@Test
	public void testImmutableMember() throws Exception {
		compareResourceEOE("mutability/immutableMember.pec");
	}

	@Test
	public void testImmutableTuple() throws Exception {
		compareResourceEOE("mutability/immutableTuple.pec");
	}

	@Test
	public void testMutable() throws Exception {
		compareResourceEOE("mutability/mutable.pec");
	}

	@Test
	public void testMutableArgument() throws Exception {
		compareResourceEOE("mutability/mutableArgument.pec");
	}

	@Test
	public void testMutableDict() throws Exception {
		compareResourceEOE("mutability/mutableDict.pec");
	}

	@Test
	public void testMutableList() throws Exception {
		compareResourceEOE("mutability/mutableList.pec");
	}

	@Test
	public void testMutableMember() throws Exception {
		compareResourceEOE("mutability/mutableMember.pec");
	}

	@Test
	public void testMutableTuple() throws Exception {
		compareResourceEOE("mutability/mutableTuple.pec");
	}

}

