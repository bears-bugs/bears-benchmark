package client.game.pieces;

import client.Point;
import java.util.ArrayList;
import java.util.Arrays;

public class Queen extends Rook {

  public Queen(Point boardLocation, boolean color) {
    super(boardLocation, color);
  }
  public Queen(String queen){super(queen);}
  /**
   * Finds all the valid moves for a Queen in chad chess. A Queen moves along vertical, horizontal,
   * and diagonal lines. A Queen can only capture a enemy non-King piece if the Queen is on an enemy
   * wall and the other piece is in its own castle, or the queen is in its own castle and the other
   * piece is on the wall. A Queen can always put a King in check if no other pieces are between the
   * Queen and the King. In all other cases the other piece simply blocks the Queen's movement.
   *
   * @param board A Piece[][] that contains this piece.
   * @return An array of Points containing all valid moves for this Queen.
   */
  @Override
  public Point[] getValidMoves(Piece[][] board) {
    ArrayList<Point> result = new ArrayList<>(Arrays.asList(super.getValidMoves(board)));
    result.addAll(this.search(board, 1, 1));   //NE
    result.addAll(this.search(board, 1, -1));  //NW
    result.addAll(this.search(board, -1, 1));  //SE
    result.addAll(this.search(board, -1, -1)); //SW
    return result.toArray(new Point[0]);
  }
}
