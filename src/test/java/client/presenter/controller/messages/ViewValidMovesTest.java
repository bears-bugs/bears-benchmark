package client.presenter.controller.messages;

import client.presenter.controller.ViewMessageType;
import org.junit.jupiter.api.Test;

import static client.presenter.SharedTestAttributes.*;
import static org.junit.jupiter.api.Assertions.*;

class ViewValidMovesTest {


  private ViewValidMoves testValidMove = new ViewValidMoves(TO_COL, TO_ROW);

  @Test
  public void testCol(){
    assertEquals(TO_COL, testValidMove.location.getArrayCol());
  }

  @Test
  public void testRow(){
    assertEquals(TO_ROW, testValidMove.location.getArrayRow());
  }

  @Test
  public void testType() {assertEquals(ViewMessageType.SHOW_VALID_MOVES, testValidMove.messageType);}

  @Test
  public void testNotEqualNull(){
    assertNotEquals( testValidMove, null);
  }

  @Test
  public void testHashCode() {
    ViewValidMoves testHash = new ViewValidMoves(TO_COL, TO_ROW);
    assertEquals(testValidMove.hashCode(), testHash.hashCode());
  }

}