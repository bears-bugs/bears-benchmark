package client.presenter.network.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class InboxRequestTest {
	private static InboxRequest testInboxRequest1, testInboxRequest2;

	public final String testGameRequestString = "15:1234";

	@BeforeAll
	public static void setup() {
		testInboxRequest1 = new InboxRequest(1234);
		testInboxRequest2 = new InboxRequest("15:1234");
	}

	@Test
	public void getDataString() {
		assertEquals(testGameRequestString, testInboxRequest1.getDataString());
	}

	@Test
	public void getDataString2() {
		assertEquals(testGameRequestString, testInboxRequest2.getDataString());
	}
}
