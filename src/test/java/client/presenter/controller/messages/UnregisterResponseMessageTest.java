package client.presenter.controller.messages;

import static org.junit.jupiter.api.Assertions.*;

import client.presenter.controller.ViewMessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UnregisterResponseMessageTest {

  private static final boolean success = true;
  private static final String[] messages = {"Successfully Unregistered User"};
  private static UnregisterResponseMessage testMessage;

  @BeforeEach
  public void setup() {
    testMessage = new UnregisterResponseMessage(success, messages);
  }

  @Test
  public void testType() {
    assertEquals(ViewMessageType.UNREGISTER_RESPONSE, testMessage.messageType);
  }

  @Test
  public void testSuccess() {
    assertEquals(success, testMessage.success);
  }

  @Test
  public void testMessages() {
    assertEquals(messages, testMessage.messages);
  }
}