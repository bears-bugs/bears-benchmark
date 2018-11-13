package client.game;

import client.Point;
import client.game.pieces.King;
import client.game.pieces.Piece;
import client.game.pieces.Queen;
import client.game.pieces.Rook;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameBoard {

  private Piece[][] board;

  /**
   * Creates a GameBoard from a String representing a board.
   * @param pieces String representation of a GameBoard.
   * Form [[rRkKqQ][a-l][A-L]]*
   */
  public GameBoard(String pieces) {
    board = new Piece[12][12];
    for (int i = 0; i < pieces.length(); i = i + 3) {
      String type = pieces.substring(i, i + 1);
      Point pos = new Point(pieces.substring(i + 1, i + 3));
      board[pos.getArrayCol()][pos.getArrayRow()] = makePiece(type, pos);
    }
  }

  /**
   * Creates the correct type of Piece from a Point and a type.
   *
   * @param type [RrQqKk] representing the color and type of the Piece to be created.
   * @param pos Point of where the Piece exists on a GameBoard.
   * @return The new Piece.
   */
  private Piece makePiece(String type, Point pos) {
    boolean color = "RQK".contains(type);
    if ("Rr".contains(type)) {//Rook
      return new Rook(pos, color);
    }
    if ("Qq".contains(type)) {//Queen
      return new Queen(pos, color);
    }
    if ("Kk".contains(type)) {//King
      return new King(pos, color);
    }
    throw new IllegalArgumentException("Piece type must be one of: [RrQqKk]; got: " + type);
  }

  /**
   * Get a piece from (col, row) on this GameBoard.
   *
   * @param col Column to get the Piece from.
   * @param row Row to get the Piece from.
   * @return A Piece at (col, row) on this GameBoard, null if no Piece exists there.
   */
  public Piece getPieceAt(int col, int row) {
    return board[col][row];
  }

  /**
   * Get a Piece form the given Point on this GameBoard.
   *
   * @param tile The Point to get a piece from.
   * @return A Piece if there is a piece at the Point, null otherwise.
   */
  public Piece getPieceAt(Point tile) {
    return this.getPieceAt(tile.getArrayCol(), tile.getArrayRow());
  }

  /**
   * Generates the String representation of this GameBoard.
   *
   * @return String representing the location and type of each Piece on the board.
   */
  public String getBoard() {
    StringBuilder res = new StringBuilder();
    for (Piece[] row : board) {
      for (Piece p : row) {
        if (p == null) {
          continue;
        }
        res.append(pieceToLetter(p));
        res.append(p.getBoardLocation().toString());
      }
    }
    return res.toString();
  }

  /**
   * Gets the current Board
   * @return The current Board.
   */
  public Piece[][] getPieces() {
    return board;
  }

  /**
   * Get all valid moves for a Piece.
   *
   * @param p A Point on the board to find valid moves for.
   * @return String containing all valid moves from whatever Piece is at the Point. If there is no
   * Piece, returns an empty String.
   */
  public String getMoves(Point p, boolean turn) {
    if (this.getPieceAt(p) == null || this.getPieceAt(p).getColor() != turn) {
      return "";
    }
    Point[] result = board[p.getArrayCol()][p.getArrayRow()].getValidMoves(this.board);
    StringBuilder res = new StringBuilder();
    for (Point move : result) {
      res.append(move.toString());
    }
    return res.toString();
  }

  /**
   * Moves a Piece from one Point to another, replacing whatever was there before.
   *
   * @param from The Point to move from.
   * @param to The Point to move to.
   * @return True if the move was completed, False if the move is not a valid move for this Piece.
   */
  public boolean MovePiece(Point from, Point to, boolean turn) {
    Piece piece = this.getPieceAt(from);
    if (piece.getColor() == turn && piece.move(to, board)) {
      board[to.getArrayCol()][to.getArrayRow()] = board[from.getArrayCol()][from.getArrayRow()];
      board[from.getArrayCol()][from.getArrayRow()] = null;
      return true;
    }
    return false;
  }


  private static final Set<Point> WALLS = Stream.of(
      new Point(1, 2), new Point(1, 3), new Point(1, 4),
      new Point(2, 1), new Point(3, 1), new Point(4, 1),
      new Point(5, 2), new Point(5, 3), new Point(5, 4),
      new Point(2, 5), new Point(3, 5), new Point(4, 5),

      new Point(6, 7), new Point(6, 8), new Point(6, 9),
      new Point(10, 7), new Point(10, 8), new Point(10, 9),
      new Point(7, 6), new Point(8, 6), new Point(9, 6),
      new Point(7, 10), new Point(8, 10), new Point(9, 10)
  ).collect(Collectors.toSet());

  /**
   * Calculates if a Point is on a wall.
   *
   * @param point The Point to test.
   * @return True if the point is on a wall, false otherwise.
   */
  public static boolean isWall(Point point) {
    return (WALLS.contains(point));
  }

  private static final Set<Point> WHITE_CASTLE = Stream.of(
      new Point(2, 2), new Point(2, 3), new Point(2, 4),
      new Point(3, 2), new Point(3, 3), new Point(3, 4),
      new Point(4, 2), new Point(4, 3), new Point(4, 4)
  ).collect(Collectors.toSet());


  /**
   * Calculates if a Point is inside the White castle.
   *
   * @param point the Point to test.
   * @return True if the point is inside the white castle, false otherwise.
   */
  public static boolean isWhiteCastle(Point point) {
    return (WHITE_CASTLE.contains(point));
  }

  private static final Set<Point> BLACK_CASTLE = Stream.of(
      new Point(7, 7), new Point(7, 8), new Point(7, 9),
      new Point(8, 7), new Point(8, 8), new Point(8, 9),
      new Point(9, 7), new Point(9, 8), new Point(9, 9)
  ).collect(Collectors.toSet());

  /**
   * Calculates if a Point is inside the Black castle.
   *
   * @param point the Point to test.
   * @return True if the point is inside the black castle, false otherwise.
   */
  public static boolean isBlackCastle(Point point) {
    return (BLACK_CASTLE.contains(point));
  }

  /**
   * Get a letter representation of a Piece, for use in toString.
   *
   * @param p The Piece to stringify
   * @return One of [RrQqKk-], based on the Piece.
   */
  private String pieceToLetter(Piece p) {
    if (p == null) {
      return "-";
    }
    if (p.getClass() == Rook.class) {
      return p.getColor() ? "R" : "r";
    }
    if (p.getClass() == King.class) {
      return p.getColor() ? "K" : "k";
    }
    if (p.getClass() == Queen.class) {
      return p.getColor() ? "Q" : "q";
    }
    throw new IllegalArgumentException(
        "Piece must be a Rook, Queen, King, or null. Was a: " + p.getClass());

  }

  @Override
  public String toString() {
    StringBuilder res = new StringBuilder();
    for (int i = 11; i >= 0; --i) {
      Piece[] r = board[i];
      for (Piece p : r) {
        res.append(pieceToLetter(p)).append(" ");
      }
      res.append("\n");
    }
    return res.toString();
  }
}
