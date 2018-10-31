package com.paritytrading.foundation;

import static org.junit.Assert.*;

import org.junit.Test;

public class ASCIITest {

    @Test
    public void get() {
        byte[] bytes = new byte[] { 'f', 'o', 'o', ' ', ' ' };

        assertEquals("foo  ", ASCII.get(bytes));
    }

    @Test
    public void getWithStringBuilder() {
        byte[] bytes = new byte[] { 'f', 'o', 'o', ' ', ' ' };

        StringBuilder b = new StringBuilder();

        ASCII.get(bytes, b);

        assertEquals("foo  ", b.toString());
    }

    @Test
    public void put() {
        assertArrayEquals(new byte[] { 'f', 'o', 'o' }, ASCII.put("foo"));
    }

    @Test
    public void putLeft() {
        byte[] bytes = new byte[5];

        ASCII.putLeft(bytes, "foo");

        assertArrayEquals(new byte[] { 'f', 'o', 'o', ' ', ' ' }, bytes);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void putTooLongLeft() {
        byte[] bytes = new byte[2];

        ASCII.putLeft(bytes, "foo");
    }

    @Test
    public void putRight() {
        byte[] bytes = new byte[5];

        ASCII.putRight(bytes, "foo");

        assertArrayEquals(new byte[] { ' ', ' ', 'f', 'o', 'o' }, bytes);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void putTooLongRight() {
        byte[] bytes = new byte[2];

        ASCII.putRight(bytes, "foo");
    }

    @Test
    public void getLongRight() {
        byte[] bytes = new byte[] { ' ', ' ', '1', '2', '3' };

        assertEquals(123, ASCII.getLong(bytes));
    }

    @Test
    public void getNegativeLongRight() {
        byte[] bytes = new byte[] { ' ', '-', '1', '2', '3' };

        assertEquals(-123, ASCII.getLong(bytes));
    }

    @Test
    public void getLongLeft() {
        byte[] bytes = new byte[] { '1', '2', '3', ' ', ' ' };

        assertEquals(123, ASCII.getLong(bytes));
    }

    @Test
    public void getNegativeLongLeft() {
        byte[] bytes = new byte[] { '-', '1', '2', '3', ' ' };

        assertEquals(-123, ASCII.getLong(bytes));
    }

    @Test
    public void putLongLeft() {
        byte[] bytes = new byte[5];

        ASCII.putLongLeft(bytes, 123);

        assertArrayEquals(new byte[] { '1', '2', '3', ' ', ' ' }, bytes);
    }

    @Test
    public void putNegativeLongLeft() {
        byte[] bytes = new byte[5];

        ASCII.putLongLeft(bytes, -123);

        assertArrayEquals(new byte[] { '-', '1', '2', '3', ' ' }, bytes);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void putTooLongLongLeft() {
        byte[] bytes = new byte[5];

        ASCII.putLongLeft(bytes, 123456);
    }

    @Test
    public void putLongRight() {
        byte[] bytes = new byte[5];

        ASCII.putLongRight(bytes, 123);

        assertArrayEquals(new byte[] { ' ', ' ', '1', '2', '3' }, bytes);
    }

    @Test
    public void putNegativeLongRight() {
        byte[] bytes = new byte[5];

        ASCII.putLongRight(bytes, -123);

        assertArrayEquals(new byte[] { ' ', '-', '1', '2', '3' }, bytes);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void putTooLongLongRight() {
        byte[] bytes = new byte[5];

        ASCII.putLongRight(bytes, 123456);
    }

    @Test
    public void getFixedLeft() {
        byte[] bytes = new byte[] { '1', '.', '2', '3', ' ' };

        assertEquals(123, ASCII.getFixed(bytes, 2));
    }

    @Test
    public void getFixedLeftWithoutDecimalDigits() {
        byte[] bytes = new byte[] { '1', '2', '3', ' ', ' ' };

        assertEquals(12300, ASCII.getFixed(bytes, 2));
    }

    @Test
    public void getFixedLeftWithFewerDecimalDigits() {
        byte[] bytes = new byte[] { '1', '2', '.', '3', ' ' };

        assertEquals(1230, ASCII.getFixed(bytes, 2));
    }

    @Test
    public void getFixedLeftWithMoreDecimalDigits() {
        byte[] bytes = new byte[] { '1', '.', '2', '3', ' ' };

        assertEquals(12, ASCII.getFixed(bytes, 1));
    }

    @Test
    public void getFixedRight() {
        byte[] bytes = new byte[] { ' ', '1', '.', '2', '3' };

        assertEquals(123, ASCII.getFixed(bytes, 2));
    }

    @Test
    public void getFixedRightWithoutDecimalDigits() {
        byte[] bytes = new byte[] { ' ', ' ', '1', '2', '3' };

        assertEquals(12300, ASCII.getFixed(bytes, 2));
    }

    @Test
    public void getFixedRightWithFewerDecimalDigits() {
        byte[] bytes = new byte[] { ' ', '1', '2', '.', '3' };

        assertEquals(1230, ASCII.getFixed(bytes, 2));
    }

    @Test
    public void getFixedRightWithMoreDecimalDigits() {
        byte[] bytes = new byte[] { ' ', '1', '.', '2', '3' };

        assertEquals(12, ASCII.getFixed(bytes, 1));
    }

    @Test
    public void getNegativeFixed() {
        byte[] bytes = new byte[] { '-', '1', '.', '2', '3' };

        assertEquals(-123, ASCII.getFixed(bytes, 2));
    }

    @Test
    public void getNegativeFixedWithoutDecimalDigits() {
        byte[] bytes = new byte[] { ' ', '-', '1', '2', '3' };

        assertEquals(-12300, ASCII.getFixed(bytes, 2));
    }

    @Test
    public void getNegativeFixedWithFewerDecimalDigits() {
        byte[] bytes = new byte[] { '-', '1', '2', '.', '3' };

        assertEquals(-1230, ASCII.getFixed(bytes, 2));
    }

    @Test
    public void getNegativeFixedWithMoreDecimalDigits() {
        byte[] bytes = new byte[] { '-', '1', '.', '2', '3' };

        assertEquals(-12, ASCII.getFixed(bytes, 1));
    }

    @Test
    public void putFixedLeft() {
        byte[] bytes = new byte[5];

        ASCII.putFixedLeft(bytes, 123, 2);

        assertArrayEquals(new byte[] { '1', '.', '2', '3', ' ' }, bytes);
    }

    @Test
    public void putSmallFixedLeft() {
        byte[] bytes = new byte[5];

        ASCII.putFixedLeft(bytes, 1, 2);

        assertArrayEquals(new byte[] { '0', '.', '0', '1', ' ' }, bytes);
    }

    @Test
    public void putNegativeFixedLeft() {
        byte[] bytes = new byte[5];

        ASCII.putFixedLeft(bytes, -123, 2);

        assertArrayEquals(new byte[] { '-', '1', '.', '2', '3' }, bytes);
    }

    @Test
    public void putSmallNegativeFixedLeft() {
        byte[] bytes = new byte[5];

        ASCII.putFixedLeft(bytes, -1, 2);

        assertArrayEquals(new byte[] { '-', '0', '.', '0', '1' }, bytes);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void putTooLongFixedLeft() {
        byte[] bytes = new byte[5];

        ASCII.putFixedLeft(bytes, 123456, 2);
    }

    @Test
    public void putFixedRight() {
        byte[] bytes = new byte[5];

        ASCII.putFixedRight(bytes, 123, 2);

        assertArrayEquals(new byte[] { ' ', '1', '.', '2', '3' }, bytes);
    }

    @Test
    public void putSmallFixedRight() {
        byte[] bytes = new byte[5];

        ASCII.putFixedRight(bytes, 1, 2);

        assertArrayEquals(new byte[] { ' ', '0', '.', '0', '1' }, bytes);
    }

    @Test
    public void putNegativeFixedRight() {
        byte[] bytes = new byte[5];

        ASCII.putFixedRight(bytes, -123, 2);

        assertArrayEquals(new byte[] { '-', '1', '.', '2', '3' }, bytes);
    }

    @Test
    public void putSmallNegativeFixedRight() {
        byte[] bytes = new byte[5];

        ASCII.putFixedRight(bytes, -1, 2);

        assertArrayEquals(new byte[] { '-', '0', '.', '0', '1' }, bytes);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void putTooLongFixedRight() {
        byte[] bytes = new byte[5];

        ASCII.putFixedRight(bytes, 123456, 2);
    }

    @Test
    public void packShort() {
        assertEquals(0x666f, ASCII.packShort("foo"));
    }

    @Test
    public void packInt() {
        assertEquals(0x666f6f20, ASCII.packInt("foo"));
    }

    @Test
    public void packLong() {
        assertEquals(0x666f6f2020202020L, ASCII.packLong("foo"));
    }

    @Test
    public void unpackShort() {
        assertEquals("fo", ASCII.unpackShort((short)0x666f));
    }

    @Test
    public void unpackShortWithStringBuilder() {
        StringBuilder b = new StringBuilder();

        ASCII.unpackShort((short)0x666f, b);

        assertEquals("fo", b.toString());
    }

    @Test
    public void unpackInt() {
        assertEquals("foo ", ASCII.unpackInt(0x666f6f20));
    }

    @Test
    public void unpackIntWithStringBuilder() {
        StringBuilder b = new StringBuilder();

        ASCII.unpackInt(0x666f6f20, b);

        assertEquals("foo ", b.toString());
    }

    @Test
    public void unpackLong() {
        assertEquals("foo     ", ASCII.unpackLong(0x666f6f2020202020L));
    }

    @Test
    public void unpackLongWithStringBuilder() {
        StringBuilder b = new StringBuilder();

        ASCII.unpackLong(0x666f6f2020202020L, b);

        assertEquals("foo     ", b.toString());
    }

}
