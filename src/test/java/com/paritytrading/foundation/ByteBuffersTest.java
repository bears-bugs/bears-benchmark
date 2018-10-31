package com.paritytrading.foundation;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import org.junit.Test;

public class ByteBuffersTest {

    @Test
    public void gettingAbsoluteBulk() {
        ByteBuffer src = ByteBuffer.wrap(new byte[] { 0x01, 0x02, 0x03, 0x04 });
        byte[]     dst = new byte[2];

        ByteBuffers.get(src, dst, 1);

        assertArrayEquals(new byte[] { 0x02, 0x03 }, dst);
    }

    @Test
    public void gettingAbsoluteBulkWithOffsetAndLength() {
        ByteBuffer src = ByteBuffer.wrap(new byte[] { 0x01, 0x02, 0x03, 0x04 });
        byte[]     dst = new byte[4];

        ByteBuffers.get(src, dst, 1, 2, 2);

        assertArrayEquals(new byte[] { 0x00, 0x00, 0x02, 0x03 }, dst);
    }

    @Test
    public void puttingAbsoluteBulk() {
        byte[]     src = new byte[] { 0x01, 0x02 };
        ByteBuffer dst = ByteBuffer.wrap(new byte[4]);

        ByteBuffers.put(dst, src, 1);

        assertArrayEquals(new byte[] { 0x00, 0x01, 0x02, 0x00 }, dst.array());
    }

    @Test
    public void puttingAbsoluteBulkWithOffsetAndLength() {
        byte[]     src = new byte[] { 0x01, 0x02, 0x03, 0x04 };
        ByteBuffer dst = ByteBuffer.wrap(new byte[4]);

        ByteBuffers.put(dst, src, 1, 2, 2);

        assertArrayEquals(new byte[] { 0x00, 0x03, 0x04, 0x00 }, dst.array());
    }

    @Test
    public void gettingRelativeUnsignedByte() {
        ByteBuffer buffer = ByteBuffer.allocate(1);

        buffer.put((byte)0xff);
        buffer.flip();

        assertEquals(255, ByteBuffers.getUnsigned(buffer));
    }

    @Test
    public void gettingAbsoluteUnsignedByte() {
        ByteBuffer buffer = ByteBuffer.allocate(1);

        buffer.put((byte)0xff);
        buffer.flip();

        assertEquals(255, ByteBuffers.getUnsigned(buffer, 0));
    }

    @Test
    public void puttingRelativeUnsignedByte() {
        ByteBuffer buffer = ByteBuffer.allocate(1);

        ByteBuffers.putUnsigned(buffer, (short)255);
        buffer.flip();

        assertEquals((byte)0xff, buffer.get());
    }

    @Test
    public void puttingAbsoluteUnsignedByte() {
        ByteBuffer buffer = ByteBuffer.allocate(1);

        ByteBuffers.putUnsigned(buffer, 0, (short)255);

        assertEquals((byte)0xff, buffer.get());
    }

    @Test
    public void gettingRelativeUnsignedShort() {
        ByteBuffer buffer = ByteBuffer.allocate(2);

        buffer.putShort((short)0xffff);
        buffer.flip();

        assertEquals(65535, ByteBuffers.getUnsignedShort(buffer));
    }

    @Test
    public void gettingAbsoluteUnsignedShort() {
        ByteBuffer buffer = ByteBuffer.allocate(2);

        buffer.putShort((short)0xffff);
        buffer.flip();

        assertEquals(65535, ByteBuffers.getUnsignedShort(buffer, 0));
    }

    @Test
    public void puttingRelativeUnsignedShort() {
        ByteBuffer buffer = ByteBuffer.allocate(2);

        ByteBuffers.putUnsignedShort(buffer, 65535);
        buffer.flip();

        assertEquals((short)0xffff, buffer.getShort());
    }

    @Test
    public void puttingAbsoluteUnsignedShort() {
        ByteBuffer buffer = ByteBuffer.allocate(2);

        ByteBuffers.putUnsignedShort(buffer, 0, 65535);

        assertEquals((short)0xffff, buffer.getShort());
    }

    @Test
    public void gettingRelativeUnsignedInteger() {
        ByteBuffer buffer = ByteBuffer.allocate(4);

        buffer.putInt(0xffffffff);
        buffer.flip();

        assertEquals(4294967295L, ByteBuffers.getUnsignedInt(buffer));
    }

    @Test
    public void gettingAbsoluteUnsignedInteger() {
        ByteBuffer buffer = ByteBuffer.allocate(4);

        buffer.putInt(0xffffffff);
        buffer.flip();

        assertEquals(4294967295L, ByteBuffers.getUnsignedInt(buffer, 0));
    }

    @Test
    public void puttingRelativeUnsignedInteger() {
        ByteBuffer buffer = ByteBuffer.allocate(4);

        ByteBuffers.putUnsignedInt(buffer, 4294967295L);
        buffer.flip();

        assertEquals(0xffffffff, buffer.getInt());
    }

    @Test
    public void puttingAbsoluteUnsignedInteger() {
        ByteBuffer buffer = ByteBuffer.allocate(4);

        ByteBuffers.putUnsignedInt(buffer, 0, 4294967295L);

        assertEquals(0xffffffff, buffer.getInt());
    }

}
