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

@DisplayName("Test King")
class KingTest {

  private GameBoard board;

  @BeforeEach
  void setUp() {
    board = new GameBoard("RlARlBRkARhDRgErgJRaLRbLraKRiJkdDKhHQdAQcGqkHRbARaBqaARbBRcC");
  }

  @DisplayName("Equals")
  @ParameterizedTest
  @CsvSource({"aA, true, aA, true, true", "aA, true, aA, false, false", "aB, true, aA, true, false"})
  void testEquals(String p1, boolean c1, String p2, Boolean c2, boolean expected){
    King testKing1 = new King(new Point(p1), c1);
    King testKing2 = new King(new Point(p2), c2);
    boolean equal = testKing1.equals(testKing2);
    assertEquals(equal, expected, "King.equals did not give the expected result.");
  }

  @DisplayName("ValidMoves")
  @ParameterizedTest(name = "({0})")
  @CsvSource({"dD,false,eCeDeEdCdEcCcDcE", "hH, true, iHiIhIjI"})
  void getValidMoves(String point, boolean color, String expectedPoints) {
    King testKing = new King(new Point(point), color);
    Set<Point> points = new HashSet<>();
    for (int i = 0; i < expectedPoints.length(); i = i + 2) {
      points.add(new Point(expectedPoints.substring(i, i + 2)));
    }
    Point[] result = testKing.getValidMoves(board.getPieces());
    Set<Point> resSet = new HashSet<>(Arrays.asList(result));
    assertEquals(points, resSet, "Expected and Actual moves do not match.");
  }

  @DisplayName("Move")
  @ParameterizedTest(name = "({0} -> {2})")
  @CsvSource({"dD,false,dC,true", "dD,false,cC,true", "dD,false,fE,false", "hH, true, jI, true",
      "hH, true, iJ, false", "hH, true, hI, true", "hH, true, fI, false"})
  void testMove(String point, boolean color, String move, boolean expected) {
    King testKing = new King(new Point(point), color);

    boolean result = testKing.move(new Point(move), board.getPieces());

    assertEquals(expected, result, "The King moved or did not move when expected");

    if (expected) {
      assertEquals(testKing.boardLocation, new Point(move), "King should be in the new position");
    } else {
      assertEquals(testKing.boardLocation, new Point(point), "King should not have moved");
    }
  }
}