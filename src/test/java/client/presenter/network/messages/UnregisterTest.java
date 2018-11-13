package client.presenter.network.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UnregisterTest {
	private static Unregister testUnregister1, testUnregister2;

	public final String testUnregisterString = "5:me@example.com:testUser:231314";

	@BeforeAll
	public static void setup() {
		testUnregister1 = new Unregister("me@example.com", "testUser", "231314");
		testUnregister2 = new Unregister("5:me@example.com:testUser:231314");
	}

	@Test
	public void getDataString() {
		assertEquals(testUnregisterString, testUnregister1.getDataString());
	}

	@Test
	public void getDataString2() {
		assertEquals(testUnregisterString, testUnregister2.getDataString());
	}
}
