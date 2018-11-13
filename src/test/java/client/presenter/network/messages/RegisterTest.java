package client.presenter.network.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class RegisterTest {
	private static Register testRegister1, testRegister2;

	public final String testRegisterString = "4:me@example.com:testUser:231314";

	@BeforeAll
	public static void setup() {
		testRegister1 = new Register("me@example.com", "testUser", "231314");
		testRegister2 = new Register("4:me@example.com:testUser:231314");
	}

	@Test
	public void getDataString() {
		assertEquals(testRegisterString, testRegister1.getDataString());
	}

	@Test
	public void getDataString2() {
		assertEquals(testRegisterString, testRegister2.getDataString());
	}
}
