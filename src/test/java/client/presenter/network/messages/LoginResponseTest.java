package client.presenter.network.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class LoginResponseTest {

	private static LoginResponse testResponse1, testResponse2;
	
	public final String testResponseString = "2:true:testUser";
	
	  @BeforeAll
	  public static void setup() {
		  testResponse1 = new LoginResponse(true, "testUser");
		  testResponse2 = new LoginResponse("2:true:testUser");
	  }

	  @Test
	  public void getDataString() {
	    assertEquals(testResponseString, testResponse1.getDataString());
	  }
	  
	  @Test
	  public void getDataString2() {
	    assertEquals(testResponseString, testResponse2.getDataString());
	  }
	
}
