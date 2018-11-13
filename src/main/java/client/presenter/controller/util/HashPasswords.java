package client.presenter.controller.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Hashes passwords in "SHA1"
 */
public class HashPasswords {

  /**
   * Applies a Sha1 hash to a password
   * @param password the password to hash
   * @return the hash password
   * @throws NoSuchAlgorithmException
   */
  public static String SHA1FromString(String password) throws NoSuchAlgorithmException {

    MessageDigest digest = MessageDigest.getInstance("SHA1");
    byte[] hash = digest.digest(password.getBytes());
    BigInteger hashInt = new BigInteger(1, hash);
    return hashInt.toString(16);

  }

}
