package client.presenter.network.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class InviteResponseTest {
	private static InviteResponse testResponse1, testResponse2;
	
	public final String testResponseString = "12:441:true";
	
	  @BeforeAll
	  public static void setup() {
		  testResponse1 = new InviteResponse(441, true);
		  testResponse2 = new InviteResponse("12:441:true");
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
