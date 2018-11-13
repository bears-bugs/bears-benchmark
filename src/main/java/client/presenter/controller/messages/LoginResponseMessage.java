package client.presenter.controller.messages;

import client.presenter.controller.ViewMessageType;

public class LoginResponseMessage extends ViewMessage {

  /**
   * A response of success for the login request
   */
  public final boolean success;
  /**
   * The nickname of the user
   */
  public final String nickname;

  /**
   *  Sets if the request was successful and the nickname of the user trying to login
   * @param success success of the login request
   * @param nickname nickname of the user logging in
   */
  public LoginResponseMessage(boolean success, String nickname) {
    super(ViewMessageType.LOGIN_RESPONSE);

    this.success = success;
    this.nickname = nickname;
  }
}
