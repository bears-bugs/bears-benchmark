package client.presenter.network.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class GameInfoTest {
	private static GameInfo testGameInfo1, testGameInfo2;

	public final String testGameInfoString = "7:123:ra1Rb2kh7Kg7:true";

	@BeforeAll
	public static void setup() {
		testGameInfo1 = new GameInfo(123, "ra1Rb2kh7Kg7", true);
		testGameInfo2 = new GameInfo("7:123:ra1Rb2kh7Kg7:true");
	}

	@Test
	public void getDataString() {
		assertEquals(testGameInfoString, testGameInfo1.getDataString());
	}

	@Test
	public void getDataString2() {
		assertEquals(testGameInfoString, testGameInfo2.getDataString());
	}
}
