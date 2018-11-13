package client.presenter.controller.messages;

import client.presenter.controller.ViewMessageType;
import java.util.Objects;

public class MovePieceResponse extends ViewMessage {

  /**
   * A response of success for the move piece request
   */
  public final boolean success;

  public final String gameBoard;

  /**
   * Sets if the request was successful
   * @param success success of the move piece request
   */
  public MovePieceResponse(boolean success, String board) {
    super(ViewMessageType.MOVE_PIECE_RESPONSE);
    this.success = success;
    this.gameBoard = board;
  }

  @Override
  public boolean equals(Object o){
    if (o == null || !(o instanceof MovePieceResponse)) {
      return false;
    }
    MovePieceResponse other = (MovePieceResponse) o;
    return success == other.success && gameBoard.equals(other.gameBoard);
  }

  @Override
  public int hashCode() {

    return Objects.hash(success, gameBoard);
  }

}
