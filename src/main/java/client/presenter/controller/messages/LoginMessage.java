package client.presenter.controller.messages;

import client.presenter.controller.ViewMessageType;
import client.presenter.controller.util.HashPasswords;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * A message from the view with the email and password for a login attempt
 */
public class LoginMessage extends ViewMessage{

  /**
   * The email address
   */
  public final String email;
  /**
   * The password
   */
  public final String password;

  /**
   * Sets the email and password information for the message
   * @param email the email
   * @param password the password
   * @throws NoSuchAlgorithmException could not find the SHA1 hash
   */
  public LoginMessage(String email, String password) throws NoSuchAlgorithmException {
    super(ViewMessageType.LOGIN);

    this.email = email;
    this.password = HashPasswords.SHA1FromString(password);
  }

  @Override
  public boolean equals(Object o){
    if (o == null || !(o instanceof LoginMessage)) {
      return false;
    }
    LoginMessage other = (LoginMessage) o;
    return email.equals(other.email) && password.equals(other.password);
  }

  @Override
  public int hashCode() {

    return Objects.hash(email, password);
  }
}
