package com.paritytrading.foundation;

import static org.junit.Assert.*;

import org.junit.Test;

public class ByteArraysTest {

    @Test
    public void reverseEvenLength() {
        byte[] a = new byte[] { 1, 2, 3, 4 };

        ByteArrays.reverse(a);

        assertArrayEquals(new byte[] { 4, 3, 2, 1 }, a);
    }

    @Test
    public void reverseOddLength() {
        byte[] a = new byte[] { 1, 2, 3, 4, 5 };

        ByteArrays.reverse(a);

        assertArrayEquals(new byte[] { 5, 4, 3, 2, 1 }, a);
    }

    @Test
    public void reverseEvenLengthRange() {
        byte[] a = new byte[] { 1, 2, 3, 4 };

        ByteArrays.reverse(a, 1, 3);

        assertArrayEquals(new byte[] { 1, 3, 2, 4 }, a);
    }

    @Test
    public void reverseOddLengthRange() {
        byte[] a = new byte[] { 1, 2, 3, 4, 5 };

        ByteArrays.reverse(a, 1, 4);

        assertArrayEquals(new byte[] { 1, 4, 3, 2, 5 }, a);
    }

    @Test
    public void packShort() {
        short s = ByteArrays.packShort(new byte[] { 0x01, 0x02, 0x03 }, (byte)0x00);

        assertEquals(0x0102, s);
    }

    @Test
    public void packInt() {
        int i = ByteArrays.packInt(new byte[] { 0x01, 0x02, 0x03 }, (byte)0x00);

        assertEquals(0x01020300, i);
    }

    @Test
    public void packLong() {
        long l = ByteArrays.packLong(new byte[] { 0x01, 0x02, 0x03, }, (byte)0x00);

        assertEquals(0x0102030000000000L, l);
    }

    @Test
    public void unpackShort() {
        byte[] a = new byte[2];

        ByteArrays.unpackShort(a, (short)0x0102);

        assertArrayEquals(new byte[] { 0x01, 0x02 }, a);
    }

    @Test
    public void unpackShortWithAllocation() {
        byte[] a = ByteArrays.unpackShort((short)0x0102);

        assertArrayEquals(new byte[] { 0x01, 0x02 }, a);
    }

    @Test
    public void unpackInt() {
        byte[] a = new byte[4];

        ByteArrays.unpackInt(a, 0x01020300);

        assertArrayEquals(new byte[] { 0x01, 0x02, 0x03, 0x00 }, a);
    }

    @Test
    public void unpackIntWithAllocation() {
        byte[] a = ByteArrays.unpackInt(0x01020300);

        assertArrayEquals(new byte[] { 0x01, 0x02, 0x03, 0x00 }, a);
    }

    @Test
    public void unpackLong() {
        byte[] a = new byte[8];

        ByteArrays.unpackLong(a, 0x0102030000000000L);

        assertArrayEquals(new byte[] { 0x01, 0x02, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00 }, a);
    }

    @Test
    public void unpackLongWithAllocation() {
        byte[] a = ByteArrays.unpackLong(0x0102030000000000L);

        assertArrayEquals(new byte[] { 0x01, 0x02, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00 }, a);
    }

    @Test
    public void shortRoundtrip() {
        short s = 0x01FF;

        assertEquals(s, ByteArrays.packShort(ByteArrays.unpackShort(s), (byte)0x00));
    }

    @Test
    public void intRoundtrip() {
        int i = 0x017F81FF;

        assertEquals(i, ByteArrays.packInt(ByteArrays.unpackInt(i), (byte)0x00));
    }

    @Test
    public void longRoundtrip() {
        long l = 0x01407F81C0FFL;

        assertEquals(l, ByteArrays.packLong(ByteArrays.unpackLong(l), (byte)0x00));
    }

}
