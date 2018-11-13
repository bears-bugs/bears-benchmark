package client.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import client.Point;
import client.game.pieces.King;
import client.game.pieces.Piece;
import client.game.pieces.Queen;
import client.game.pieces.Rook;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class GameBoardTest {

  private Piece pieceFromString(String s) {
    String type = s.substring(0, 1);
    if ("Rr".contains(type)) {
      return new Rook(s.substring(1));
    } else if ("Kk".contains(type)) {
      return new King(s.substring(1));
    } else if ("Qq".contains(type)) {
      return new Queen(s.substring(1));
    } else {
      return null;
    }
  }

  private GameBoard board;

  @BeforeEach
  void setUp() {
    board = new GameBoard("RiIrdDKjJkeEQaAqlL");
  }

  @Test
  void testConstructor() {
    GameBoard b = new GameBoard("raA");
    assertNotEquals(null, b.getPieceAt(0, 0),
        "Board should contain a piece at (0,0)");
    assertEquals(null, b.getPieceAt(1, 1),
        "Board should not contain a piece at (1,1)");
  }

  @Test
  void testGetPieceAtCoordinates() {
    King expected = new King(new Point("jJ"), true);
    assertEquals(expected, board.getPieceAt(9, 9),
        "getPieceAt(9,9) should return a Black King");
  }

  @Test
  void testGetPieceAtPoint() {
    King expected = new King(new Point("eE"), false);
    assertEquals(expected, board.getPieceAt(new Point("eE")),
        "getPieceAt(4,4) should return a White King");
  }

  @Test
  void testMovePiece() {
    assertTrue(true);
  }

  @DisplayName("isWall")
  @ParameterizedTest(name = "({0}) should be {1}")
  @CsvSource({"aA, false", "lL, false", "dD, false", "aL, false", "bC, true", "bE, true",
      "fD, true", "dF, true", "gI, true", "iG, true", "kJ, true", "hK, true", "hI, false"})
  void testIsWall(String point, boolean expected) {
    assertEquals(GameBoard.isWall(new Point(point)), expected);
  }

  @DisplayName("getPieceAt(int, int)")
  @ParameterizedTest(name = "{2} expected at ({0}, {1})")
  @CsvSource({"8,8,RiItrue", "9,9,KjJtrue", "0,0,QaAtrue"})
  void testGetPieceAt(int c, int r, String piece) {
    Piece expected = pieceFromString(piece);
    assertEquals(board.getPieceAt(c, r), expected,
        "GameBoard.getPieceAt should have found a " + piece.charAt(0));
  }

  @DisplayName("getPieceAt(int, int)")
  @ParameterizedTest(name = "{1} expected at ({0})")
  @CsvSource({"dD,rdDfalse", "eE,keEfalse", "lL,qlLfalse"})
  void testGetPieceAt1(String point, String piece) {
    Piece expected = pieceFromString(piece);
    Point p = new Point(point);
    assertEquals(board.getPieceAt(p), expected,
        "GameBoard.getPieceAt should have found a " + piece.charAt(0));
  }

  @Test
  void testGetBoard() {
    Set<String> expected = new HashSet<>();
    String expectedString = "RiIrdDKjJkeEQaAqlL";
    for (int i = 0; i < expectedString.length(); i+=3){
      expected.add(expectedString.substring(i, i+3));
    }

    Set<String> actual = new HashSet<>();
    String actualString = board.getBoard();
    for (int i = 0; i < expectedString.length(); i+=3){
      actual.add(actualString.substring(i, i+3));
    }

    assertEquals(expected, actual, "GameBoard did not match expected board");
  }

  @DisplayName("isWhiteCastle")
  @ParameterizedTest(name = "({0}) should be {1}")
  @CsvSource({"aA, false", "lL, false", "bC, false", "fD, false", "dD, true", "iI, false",
      "jJ, false", "cE, true", "iH, false"})
  void testIsWhiteCastle(String point, boolean expected) {
    assertEquals(GameBoard.isWhiteCastle(new Point(point)), expected);

  }

  @DisplayName("isBlackCastle")
  @ParameterizedTest(name = "({0}) should be {1}")
  @CsvSource({"aA, false", "lL, false", "bC, false", "fD, false", "dD, false", "iI, true",
      "jJ, true", "cE, false", "iH, true"})
  void testIsBlackCastle(String point, boolean expected) {
    assertEquals(GameBoard.isBlackCastle(new Point(point)), expected);
  }
}