package client.presenter.network.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ActiveGameRequestTest {
	private static ActiveGameRequest testActiveGameReq1, testActiveGameReq2;

	private final String testActiveGamReqString = "9:TestUser";

	@BeforeAll
	public static void setup() {
		testActiveGameReq1 = new ActiveGameRequest("TestUser");
		testActiveGameReq2 = new ActiveGameRequest("9:TestUser", 0);
	}

	@Test
	public void getDataString() {
		assertEquals(testActiveGamReqString, testActiveGameReq1.getDataString());
	}

	@Test
	public void getDataString2() {
		assertEquals(testActiveGamReqString, testActiveGameReq2.getDataString());
	}
}
