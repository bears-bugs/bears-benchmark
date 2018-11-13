package client.game.pieces;

import client.Point;
import java.util.ArrayList;

public class Rook extends Piece {

  public Rook(Point boardLocation, boolean color) {
    super(boardLocation, color);
  }
  public Rook(String rook){super(rook);}
  /**
   * Finds all the valid moves for a Rook in chad chess. A Rook moves along vertical and horizontal
   * lines. A Rook can only capture a enemy non-King piece if the Rook is on an enemy wall and the
   * other piece is in its own castle, or the Rook is in its own castle and the other piece is on
   * the wall. A Rook can always put a King in check if no other pieces are between the Rook and the
   * King. In all other cases the other piece simply blocks the Rook's movement.
   *
   * @param board A Piece[][] that contains this piece.
   * @return An array of Points containing all valid moves for this Rook.
   */
  @Override
  public Point[] getValidMoves(Piece[][] board) {
    ArrayList<Point> result = this.search(board, 1, 0);   //Right
    result.addAll(this.search(board, -1, 0));             //Left
    result.addAll(this.search(board, 0, 1));              //Up
    result.addAll(this.search(board, 0, -1));             //Down

    return result.toArray(new Point[0]);
    //return result.size() > 0 ? result.toArray(new Point[0]) : new Point[0];
  }

  /**
   * Searches the Board for valid move places in a straight line determined by deltaCol and
   * deltaRow.
   *
   * @param board A Piece[][] that contains this piece.
   * @param deltaCol How much to move the column each step.
   * @param deltaRow How much to move the row each step.
   * @return An ArrayList of points with every valid move in the direction searched.
   */
  ArrayList<Point> search(Piece[][] board, int deltaCol, int deltaRow) {
    ArrayList<Point> result = new ArrayList<>();
    int col = this.boardLocation.getArrayCol() + deltaCol;
    int row = this.boardLocation.getArrayRow() + deltaRow;
    while (row >= 0 && row < 12 && col >= 0 && col < 12 && board[col][row] == null) {
      result.add(new Point(col, row));
      row += deltaRow;
      col += deltaCol;
    }
    if (row >= 0 && row < 12 && col >= 0 && col < 12 && canCapture(board[col][row])) {
      result.add(new Point(col, row));
    }
    return result;
  }
}
