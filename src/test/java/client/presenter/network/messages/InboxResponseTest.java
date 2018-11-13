package client.presenter.network.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class InboxResponseTest {
	private static InboxResponse testResponse1, testResponse2;
	
	public final String testResponseString = "16:123:Mac:Dennis:01-01-18#1234:Charlie:Mac:02-14-18";
	
	  @BeforeAll
	  public static void setup() {
		  int[] inviteIDs = {123, 1234};
		  String[] senders = {"Mac","Charlie"};
		  String[] recipients = {"Dennis", "Mac"};
		  String[] dates = {"01-01-18", "02-14-18"};
		  testResponse1 = new InboxResponse(inviteIDs, senders, recipients, dates);
		  testResponse2 = new InboxResponse("16:123:Mac:Dennis:01-01-18#1234:Charlie:Mac:02-14-18");
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
