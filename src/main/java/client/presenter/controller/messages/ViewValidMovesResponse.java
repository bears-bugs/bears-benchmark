package client.presenter.controller.messages;

import client.presenter.controller.ViewMessageType;
import java.util.Arrays;

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

  @Override
  public boolean equals(Object o){
    if (o == null || !(o instanceof ViewValidMovesResponse)) {
      return false;
    }
    ViewValidMovesResponse other = (ViewValidMovesResponse) o;
    return Arrays.equals(locations, other.locations);
  }
}
