package client.presenter.network.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class InviteRequestTest {
	private static InviteRequest testInviteRequest1, testInviteRequest2;

	private final String testInviteReqString = "11:TestUser1:TestUser2";

	@BeforeAll
	public static void setup() {
		testInviteRequest1 = new InviteRequest("TestUser1", "TestUser2");
		testInviteRequest2 = new InviteRequest("11:TestUser1:TestUser2");
	}

	@Test
	public void getDataString() {
		assertEquals(testInviteReqString, testInviteRequest1.getDataString());
	}

	@Test
	public void getDataString2() {
		assertEquals(testInviteReqString, testInviteRequest2.getDataString());
	}
}
