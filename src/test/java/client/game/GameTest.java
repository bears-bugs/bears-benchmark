package client.game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameTest {
  private Game testGame;
  private Game loadedGame;
  @BeforeEach
  void setUp() {
    testGame = new Game();
    loadedGame = new Game("RaAraBkeEKiIQiJqfE", true);
  }

  @Test
  void turn() {
    assertEquals(false, testGame.getTurn(), "Should be Whites getTurn");
    assertEquals(true, loadedGame.getTurn(), "Should be Blacks getTurn.");
  }

  @Test
  void move() {
    assertTrue(testGame.move("cC", "aC"));
    assertTrue(testGame.getBoard().contains("raC"));
  }

  @Test
  void validMoves() {
    assertEquals("", testGame.validMoves("dD"));
  }

  @Test
  void getBoard() {
    Set<String> expected = new HashSet<>();
    String expectedString = "RaAraBkeEKiIQiJqfE";
    for (int i = 0; i < expectedString.length(); i+=3){
      expected.add(expectedString.substring(i, i+3));
    }

    Set<String> actual = new HashSet<>();
    String actualString = loadedGame.getBoard();
    for (int i = 0; i < expectedString.length(); i+=3){
      actual.add(actualString.substring(i, i+3));
    }

    assertEquals(expected, actual, "GameBoard did not match expected board");
  }

}