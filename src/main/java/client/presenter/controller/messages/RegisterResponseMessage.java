package client.presenter.controller.messages;

import client.presenter.controller.ViewMessageType;

public class RegisterResponseMessage extends ViewMessage {

  /**
   * A response of success for the register request
   */
  public final boolean success;
  /**
   * An array of messages that respond to the register request
   */
  public final String[] messages;

  /**
   * Sets if the request was successful and the responding messages
   * @param success success of the register request
   * @param messages response messages to the register request
   */
  public RegisterResponseMessage(boolean success, String[] messages) {
    super(ViewMessageType.REGISTER_RESPONSE);

    this.success = success;
    this.messages = messages;
  }
}
