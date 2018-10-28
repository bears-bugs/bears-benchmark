package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestMutability extends BaseEParserTest {

	@Test
	public void testImmutable() throws Exception {
		compareResourceEME("mutability/immutable.pec");
	}

	@Test
	public void testImmutableArgument() throws Exception {
		compareResourceEME("mutability/immutableArgument.pec");
	}

	@Test
	public void testImmutableDict() throws Exception {
		compareResourceEME("mutability/immutableDict.pec");
	}

	@Test
	public void testImmutableList() throws Exception {
		compareResourceEME("mutability/immutableList.pec");
	}

	@Test
	public void testImmutableMember() throws Exception {
		compareResourceEME("mutability/immutableMember.pec");
	}

	@Test
	public void testImmutableTuple() throws Exception {
		compareResourceEME("mutability/immutableTuple.pec");
	}

	@Test
	public void testMutable() throws Exception {
		compareResourceEME("mutability/mutable.pec");
	}

	@Test
	public void testMutableArgument() throws Exception {
		compareResourceEME("mutability/mutableArgument.pec");
	}

	@Test
	public void testMutableDict() throws Exception {
		compareResourceEME("mutability/mutableDict.pec");
	}

	@Test
	public void testMutableList() throws Exception {
		compareResourceEME("mutability/mutableList.pec");
	}

	@Test
	public void testMutableMember() throws Exception {
		compareResourceEME("mutability/mutableMember.pec");
	}

	@Test
	public void testMutableTuple() throws Exception {
		compareResourceEME("mutability/mutableTuple.pec");
	}

}

