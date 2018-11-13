package client.game.pieces;

import static org.junit.jupiter.api.Assertions.*;

import client.game.GameBoard;
import client.Point;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("Test Rook")
class RookTest {

  private GameBoard board;

  @BeforeEach
  void setUp() {
    board = new GameBoard("RlARlBRkARhDRgErgJRaLRbLraKRiJkdDKhHQdAQcGQkHRbARaBqaARbBRcC");
  }

  @DisplayName("Equals")
  @ParameterizedTest
  @CsvSource({"aA, true, aA, true, true", "aA, true, aA, false, false", "aB, true, aA, true, false"})
  void testEquals(String p1, boolean c1, String p2, Boolean c2, boolean expected){
    Rook testRook1 = new Rook(new Point(p1), c1);
    Rook testRook2 = new Rook(new Point(p2), c2);
    boolean equal = testRook1.equals(testRook2);
    assertEquals(equal, expected, "Queen.equals did not give the expected result.");
  }

  @DisplayName("ValidMoves")
  @ParameterizedTest(name = "({0})")
  @CsvSource({"lA,true,''", "aL, true,''", "gE, true, aEbEcEdEeEfEhEiEjEkElEgAgBgCgDgFgGgHgI",
      "kA, true, eAfAgAhAiAjAkBkCkDkEkFkG", "gJ, false, aJbJcJdJeJfJhJiJgFgGgHgIgKgL",
      "iJ,true,jJkJlJhJgJiAiBiCiDiEiFiGiHiIiKiL", "hD, True, iDjDkDlDgDfDeDdDhAhBhChEhFhG"})
  void testGetValidMoves(String point, boolean color, String expectedPoints) {
    Rook testRook = new Rook(new Point(point), color);
    Set<Point> points = new HashSet<>();
    for (int i = 0; i < expectedPoints.length(); i = i + 2) {
      points.add(new Point(expectedPoints.substring(i, i + 2)));
    }
    Point[] result = testRook.getValidMoves(board.getPieces());
    Set<Point> resSet = new HashSet<>(Arrays.asList(result));
    assertEquals(points, resSet, "Expected and Actual moves do not match.");
  }

  @DisplayName("Move")
  @ParameterizedTest(name = "({0})")
  @CsvSource({"lA,true,kA, false", "aL, true,aK, false", "gE, true, gA, true",
      "kA, true, kG, true", "gJ, false, iJ, true", "iJ,true,gJ, true", "hD, True, dD, true"})
  void testMove(String point, boolean color, String move, boolean expected) {
    Rook testRook = new Rook(new Point(point), color);

    boolean result = testRook.move(new Point(move), board.getPieces());

    assertEquals(expected, result, "The Rook moved or did not move when expected");

    if (expected) {
      assertEquals(testRook.boardLocation, new Point(move), "Rook should be in the new position");
    } else {
      assertEquals(testRook.boardLocation, new Point(point), "Rook should not have moved");
    }
  }

}