package client.presenter.network.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class RegisterResponseTest {
	private static RegisterResponse testRegisterResponse1, testRegisterResponse2;

	public final String testRegisterResponseString = "14:true:false";

	@BeforeAll
	public static void setup() {
		testRegisterResponse1 = new RegisterResponse(true, false);
		testRegisterResponse2 = new RegisterResponse("14:true:false");
	}

	@Test
	public void getDataString() {
		assertEquals(testRegisterResponseString, testRegisterResponse1.getDataString());
	}

	@Test
	public void getDataString2() {
		assertEquals(testRegisterResponseString, testRegisterResponse2.getDataString());
	}
}
