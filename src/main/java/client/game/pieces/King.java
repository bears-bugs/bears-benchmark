package client.game.pieces;

import client.Point;
import java.util.ArrayList;

public class King extends Piece {

  public King(Point boardLocation, boolean color) {
    super(boardLocation, color);
  }
  public King(String king){super(king);}
  /**
   * Finds the valid moves for a King in chad chess. The King cannot leave its castle, and moves as
   * a combination of a King and Knight in normal chess. The King can capture any opponent piece
   * that it can land on.
   *
   * @param board A Piece[][] that contains this piece.
   * @return An array of Points containing all valid moves for this King.
   */
  @Override
  public Point[] getValidMoves(Piece[][] board) {
    ArrayList<Point> result = new ArrayList<>();
    int deltaCol[] = {1, 2, 2, 1, -1, -2, -2, -1, 1, 1, 1, 0, 0, -1, -1, -1};
    int deltaRow[] = {2, 1, -1, -2, -2, -1, 1, 2, 1, 0, -1, 1, -1, 1, 0, -1};
    for (int i = 0; i < deltaCol.length; ++i) {
      int col = this.boardLocation.getArrayCol() + deltaCol[i];
      int row = this.boardLocation.getArrayRow() + deltaRow[i];
      Point possible = new Point(col, row);
      if (this.inOwnCastle(possible) && canMove(board[col][row])) {
        result.add(possible);
      }
    }
    return result.toArray(new Point[0]);
  }

  /**
   * Determines if a King can move to a space.
   *
   * @param p A Piece to attempt to move to. Can be null if no Piece is in a space.
   * @return True if Piece is null or can be captured, false otherwise.
   */
  private boolean canMove(Piece p) {
    return p == null || notSameColor(p);
  }
}
