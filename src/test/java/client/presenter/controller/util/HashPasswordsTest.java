package client.presenter.controller.util;

import static org.junit.jupiter.api.Assertions.*;

import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.Test;

class HashPasswordsTest {

  private static final String loginPassword = "IamABadpassword";
  private static final String hashedPassword = "5f4f8b99d5cd5ccd665ae5d57296b16cad7aaadc";

  @Test
  public void testHash() throws NoSuchAlgorithmException {
    assertEquals(hashedPassword, HashPasswords.SHA1FromString(loginPassword));
  }

  /**
   * Makes sure the has is working correctly by giving it two very similar passwords
   * @throws NoSuchAlgorithmException could not find the SHA1 hash
   */
  @Test
  public void testHashFail() throws NoSuchAlgorithmException {
    assertNotEquals(hashedPassword, HashPasswords.SHA1FromString("IamABadPassword"));
  }

}