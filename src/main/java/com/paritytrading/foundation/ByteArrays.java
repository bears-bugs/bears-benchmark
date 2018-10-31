package com.paritytrading.foundation;

/**
 * This class contains methods for manipulating byte arrays.
 */
public class ByteArrays {

    private ByteArrays() {
    }

    /**
     * Reverse a byte array.
     *
     * @param a a byte array
     */
    public static void reverse(byte[] a) {
        reverse(a, 0, a.length);
    }

    /**
     * Reverse the specified range in a byte array.
     *
     * @param a a byte array
     * @param from the index of the first element (inclusive)
     * @param to the index of the last element (exclusive)
     */
    public static void reverse(byte[] a, int from, int to) {
        byte tmp;

        for (int i = 0; i < (to - from) / 2; i++) {
            tmp             = a[from + i    ];
            a[from + i]     = a[to   - i - 1];
            a[to   - i - 1] = tmp;
        }
    }

    /**
     * Pack a byte array into a short.
     *
     * <p>If the length of the byte array is less than two bytes, the pad byte
     * is used to fill the less significant bits.</p>
     *
     * <p>If the length of the byte array is more than two bytes, only the
     * first two bytes are packed.</p>
     *
     * @param a a byte array
     * @param pad the pad byte
     * @return a short
     */
    public static short packShort(byte[] a, byte pad) {
        return (short)pack(a, pad, 2);
    }

    /**
     * Pack a byte array into an integer.
     *
     * <p>If the length of the byte array is less than four bytes, the
     * pad byte is used to fill the less significant bits.</p>
     *
     * <p>If the length of the byte array is more than four bytes, only
     * the first four bytes are packed.</p>
     *
     * @param a a byte array
     * @param pad the pad byte
     * @return an integer
     */
    public static int packInt(byte[] a, byte pad) {
        return (int)pack(a, pad, 4);
    }

    /**
     * Pack a byte array into a long.
     *
     * <p>If the length of the byte array is less than eight bytes, the pad
     * byte is used to fill the less significant bits.</p>
     *
     * <p>If the length of the byte array is more than eight bytes, only the
     * first eight bytes are packed.</p>
     *
     * @param a a byte array
     * @param pad the bad byte
     * @return a long
     */
    public static long packLong(byte[] a, byte pad) {
        return pack(a, pad, 8);
    }

    private static long pack(byte[] a, byte pad, int size) {
        long l = 0;
        int  i = 0;

        for (; i < Math.min(a.length, size); i++)
            l = (l << 8) | a[i];

        for (; i < size; i++)
            l = (l << 8) | pad;

        return l;
    }

    /**
     * Unpack a short into a byte array.
     *
     * <p>If the length of the array is less than two bytes, only the first
     * bytes that fit into the array are unpacked.</p>
     *
     * @param a a byte array
     * @param s a short
     */
    public static void unpackShort(byte[] a, short s) {
        unpack(a, s, 2);
    }

    /**
     * Unpack a short into a byte array.
     *
     * @param s a short
     * @return an array of two bytes
     */
    public static byte[] unpackShort(short s) {
        return unpack(s, 2);
    }

    /**
     * Unpack an integer into a byte array.
     *
     * <p>If the length of the array is less than four bytes, only the first
     * bytes that fit into the array are unpacked.</p>
     *
     * @param a a byte array
     * @param i an integer
     */
    public static void unpackInt(byte[] a, int i) {
        unpack(a, i, 4);
    }

    /**
     * Unpack an integer into a byte array.
     *
     * @param i an integer
     * @return an array of four bytes
     */
    public static byte[] unpackInt(int i) {
        return unpack(i, 4);
    }

    /**
     * Unpack a long into a byte array.
     *
     * <p>If the length of the array is less than eight bytes, only the first
     * bytes that fit into the array are unpacked.</p>
     *
     * @param a a byte array
     * @param l a long
     */
    public static void unpackLong(byte[] a, long l) {
        unpack(a, l, 8);
    }

    /**
     * Unpack a long into a byte array.
     *
     * @param l a long
     * @return an array of eight bytes
     */
    public static byte[] unpackLong(long l) {
        return unpack(l, 8);
    }

    private static void unpack(byte[] a, long l, int size) {
        for (int i = 0; i < Math.min(a.length, size); i++)
            a[i] = (byte)((l >> (8 * (size - 1 - i))) & 0xFF);
    }

    private static byte[] unpack(long l, int size) {
        byte[] a = new byte[size];

        unpack(a, l, size);

        return a;
    }

}
