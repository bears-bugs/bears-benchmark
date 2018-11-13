package client.presenter.network.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ResignTest {
	private static Resign testResign1, testResign2;

	public final String testResignString = "13:123:testUser";

	@BeforeAll
	public static void setup() {
		testResign1 = new Resign(123, "testUser");
		testResign2 = new Resign("13:123:testUser");
	}

	@Test
	public void getDataString() {
		assertEquals(testResignString, testResign1.getDataString());
	}

	@Test
	public void getDataString2() {
		assertEquals(testResignString, testResign2.getDataString());
	}
}
