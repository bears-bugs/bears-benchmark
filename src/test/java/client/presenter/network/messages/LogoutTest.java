package client.presenter.network.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class LogoutTest {

	private static Logout testLogout1, testLogout2;

	private final String testUserString = "3:TestUser";

	@BeforeAll
	public static void setup() {
		testLogout1 = new Logout("TestUser");
		testLogout2 = new Logout("3:TestUser", 0);
	}

	@Test
	public void getDataString() {
		assertEquals(testUserString, testLogout1.getDataString());
	}

	@Test
	public void getDataString2() {
		assertEquals(testUserString, testLogout2.getDataString());
	}
}
