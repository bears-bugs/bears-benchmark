package client.presenter.controller.messages;

import static client.presenter.SharedTestAttributes.*;
import static org.junit.jupiter.api.Assertions.*;

import client.presenter.controller.ViewMessageType;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

class LoginMessageTest{


  private static LoginMessage testMessage;

  @BeforeEach
  public void setup() throws NoSuchAlgorithmException {
    testMessage = new LoginMessage(TEST_LOGIN_EMAIL, TEST_LOGIN_PASSWORD);
  }

  @Test
  public void testType(){
    assertEquals(ViewMessageType.LOGIN, testMessage.messageType);
  }

  @Test
  public void testPassword(){
    assertEquals(TEST_HASHED_PASSWORD, testMessage.password);
  }

  @Test
  public void testEmail(){
    assertEquals(TEST_LOGIN_EMAIL, testMessage.email);
  }

  @Test
  public void testNotEqualNull(){
    assertNotEquals( testMessage, null);
  }

  @Test
  public void testHashCode() throws NoSuchAlgorithmException {
    LoginMessage testHash = new LoginMessage(TEST_LOGIN_EMAIL, TEST_LOGIN_PASSWORD);
    assertEquals(testMessage.hashCode(), testHash.hashCode());
  }

}