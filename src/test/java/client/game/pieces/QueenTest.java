package client.game.pieces;

import static org.junit.jupiter.api.Assertions.assertEquals;

import client.game.GameBoard;
import client.Point;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("Test Queen")
class QueenTest {

  private GameBoard board;

  @BeforeEach
  void setUp() {
    board = new GameBoard("RlARlBRkARhDRgErgJRaLRbLraKRiJkdDKhHQdAQcGqkHRbARaBqaARbBRcC");
  }

  @DisplayName("Equals")
  @ParameterizedTest
  @CsvSource({"aA, true, aA, true, true", "aA, true, aA, false, false", "aB, true, aA, true, false"})
  void testEquals(String p1, boolean c1, String p2, Boolean c2, boolean expected){
    Queen testQueen1 = new Queen(new Point(p1), c1);
    Queen testQueen2 = new Queen(new Point(p2), c2);
    boolean equal = testQueen1.equals(testQueen2);
    assertEquals(equal, expected, "Queen.equals did not give the expected result.");
  }

  @DisplayName("ValidMoves")
  @ParameterizedTest(name = "({0})")
  @CsvSource({"aA,false,''", "dA, true, cAeAfAgAhAiAjAdBdCdDcBbCaDeBfCgDhEiFjG",
      "kH, false, kBkCkDkEkFkGkIkJkKkLlGlHlIjHiHhHjIiJjGiFhEgDfCeB"})
  void getValidMoves(String point, boolean color, String expectedPoints) {
    Queen testQueen = new Queen(new Point(point), color);
    Set<Point> points = new HashSet<>();
    for (int i = 0; i < expectedPoints.length(); i = i + 2) {
      points.add(new Point(expectedPoints.substring(i, i + 2)));
    }
    Point[] result = testQueen.getValidMoves(board.getPieces());
    Set<Point> resSet = new HashSet<>(Arrays.asList(result));
    assertEquals(points, resSet, "Expected and Actual moves do not match.");
  }

  @DisplayName("Move")
  @ParameterizedTest(name = "({0})")
  @CsvSource({"aA,false,bA,false", "dA, true,dD, true", "dA, true,gD, true", "dA, true,dH, false",
      "kH, false,iJ, true", "kH, false,hH, true"})
  void testMove(String point, boolean color, String move, boolean expected) {
    Queen testQueen = new Queen(new Point(point), color);

    boolean result = testQueen.move(new Point(move), board.getPieces());

    assertEquals(expected, result, "The Queen moved or did not move when expected");

    if (expected) {
      assertEquals(testQueen.boardLocation, new Point(move), "Queen should be in the new position");
    } else {
      assertEquals(testQueen.boardLocation, new Point(point), "Queen should not have moved");
    }
  }
}