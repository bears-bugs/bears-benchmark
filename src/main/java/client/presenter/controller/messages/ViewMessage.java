package client.presenter.controller.messages;

import client.presenter.controller.ViewMessageType;

/**
 * This is the base class for all message that the view is sending and receiving
 */
public abstract class ViewMessage {

  /**
   * The type of message
   */
  public final ViewMessageType messageType;

  /**
   * Sets the message type for the message
   * @param messageType the message type
   */
  protected ViewMessage(ViewMessageType messageType){
    this.messageType = messageType;
  }

}
