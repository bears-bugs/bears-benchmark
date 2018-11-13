package client.presenter.controller.messages;

import client.presenter.controller.ViewMessageType;

public class ViewValidMovesResponse extends ViewMessage {

  /**
   * An array of locations that are valid moves
   */
  public final String[] locations;

  /**
   * Sets the valid move locations
   * @param locations the valid moves in response to the view valid moves request
   */
  public ViewValidMovesResponse(String[] locations) {
    super(ViewMessageType.SHOW_VALID_MOVES_RESPONSE);
    this.locations = locations;
  }
}
