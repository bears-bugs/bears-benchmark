package client.presenter.network.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class GameRequestTest {
	private static GameRequest testGameRequest1, testGameRequest2;

	public final String testGameRequestString = "6:123";

	@BeforeAll
	public static void setup() {
		testGameRequest1 = new GameRequest(123);
		testGameRequest2 = new GameRequest("6:123");
	}

	@Test
	public void getDataString() {
		assertEquals(testGameRequestString, testGameRequest1.getDataString());
	}

	@Test
	public void getDataString2() {
		assertEquals(testGameRequestString, testGameRequest2.getDataString());
	}
}
