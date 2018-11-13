package client.presenter.controller.messages;

import static client.presenter.SharedTestAttributes.*;
import static org.junit.jupiter.api.Assertions.*;

import client.presenter.controller.ViewMessageType;
import org.junit.jupiter.api.Test;

class MovePieceMessageTest {


  private MovePieceMessage testMove = new MovePieceMessage(FROM_COL, FROM_ROW, TO_COL, TO_ROW);

  @Test
  public void testFromCol(){
    assertEquals(FROM_COL, testMove.fromLocation.getArrayCol());
  }

  @Test
  public void testFromRow(){
    assertEquals(FROM_ROW, testMove.fromLocation.getArrayRow());
  }

  @Test
  public void testToCol(){
    assertEquals(TO_COL, testMove.toLocation.getArrayCol());
  }

  @Test
  public void testToRow(){
    assertEquals(TO_ROW, testMove.toLocation.getArrayRow());
  }

  @Test
  public void testType() {assertEquals(ViewMessageType.MOVE_PIECE, testMove.messageType);}

  @Test
  public void testNotEqualNull(){
    assertNotEquals( testMove, null);
  }

  @Test
  public void testHashCode() {
    MovePieceMessage testHash = new MovePieceMessage(FROM_COL, FROM_ROW, TO_COL, TO_ROW);
    assertEquals(testMove.hashCode(), testHash.hashCode());
  }

}