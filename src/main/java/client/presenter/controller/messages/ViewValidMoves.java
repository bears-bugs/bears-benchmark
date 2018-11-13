package client.presenter.controller.messages;

import client.Point;
import client.presenter.controller.ViewMessageType;

/**
 * The message asks for the valid moves of a piece on a given row and column
 */
public class ViewValidMoves extends ViewMessage {

  public final Point location;

  /**
   * Set the row and column of the piece
   * @param col the column
   * @param row the row
   */
  public ViewValidMoves(int col, int row ){
    super(ViewMessageType.SHOW_VALID_MOVES);
    this.location = new Point(col, row);
  }

  @Override
  public boolean equals(Object o){
    if (o == null || !(o instanceof ViewValidMoves)) {
      return false;
    }
    ViewValidMoves other = (ViewValidMoves) o;
    return location.equals(other.location);
  }

  @Override
  public int hashCode() {

    return location.hashCode();
  }

}
