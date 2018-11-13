package client.game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import client.Point;
import org.junit.jupiter.api.Test;

class PointTest {

  @Test
  void testConstructor1(){
    Point p = new Point(0,0);
    assertEquals(0, p.getArrayCol(), "Column was set wrong");
    assertEquals(0, p.getArrayRow(), "Row was set wrong");
  }

  @Test
  void testConstructor2(){
    Point p = new Point('a',0);
    assertEquals(0, p.getArrayCol(), "Column was set wrong");
    assertEquals(0, p.getArrayRow(), "Row was set wrong");
  }

  @Test
  void testConstructor3(){
    Point p = new Point("aA");
    assertEquals(0, p.getArrayCol(), "Column was set wrong");
    assertEquals(0, p.getArrayRow(), "Row was set wrong");
  }

  @Test
  void testConstructor99(){
    Point p = new Point("iI");
    assertEquals(8, p.getArrayCol(), "Column was set wrong");
    assertEquals(8, p.getArrayRow(), "Row was set wrong");
  }

  @Test
  void testToString() {
    Point p = new Point(0,0);
    assertEquals("aA", p.toString());
  }
}