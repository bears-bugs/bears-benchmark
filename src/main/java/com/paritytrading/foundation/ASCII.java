package com.paritytrading.foundation;

import static com.paritytrading.foundation.Longs.POWERS_OF_TEN;
import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * This class contains methods for manipulating ASCII strings.
 */
public class ASCII {

    private ASCII() {
    }

    /**
     * Get an ASCII string from a byte array.
     *
     * @param bytes a byte array
     * @return a string
     */
    public static String get(byte[] bytes) {
        return new String(bytes, US_ASCII);
    }

    /**
     * Get an ASCII string from a byte array.
     *
     * @param bytes a byte array
     * @param b a string builder
     */
    public static void get(byte[] bytes, StringBuilder b) {
        for (int i = 0; i < bytes.length; i++)
            b.append((char)bytes[i]);
    }

    /**
     * Put an ASCII string into a byte array.
     *
     * @param s a string
     * @return a byte array
     */
    public static byte[] put(String s) {
        return s.getBytes(US_ASCII);
    }

    /**
     * Put an ASCII string into a byte array.
     *
     * <p>If the length of the string is smaller than the length of the array,
     * the space character is used to fill the trailing bytes.</p>
     *
     * @param bytes a byte array
     * @param s a string
     * @throws IndexOutOfBoundsException if the length of the string is larger
     *   than the length of the array
     */
    public static void putLeft(byte[] bytes, CharSequence s) {
        int i = 0;

        for (; i < s.length(); i++)
            bytes[i] = (byte)s.charAt(i);

        for (; i < bytes.length; i++)
            bytes[i] = (byte)' ';
    }

    /**
     * Put an ASCII string into a byte array.
     *
     * <p>If the length of the string is smaller than the length of the array,
     * the space character is used to fill the leading bytes.</p>
     *
     * @param bytes a byte array
     * @param s a string
     * @throws IndexOutOfBoundsException if the length of the string is larger
     *   than the length of the array
     */
    public static void putRight(byte[] bytes, CharSequence s) {
        int i = 0;

        for (; i < bytes.length - s.length(); i++)
            bytes[i] = (byte)' ';

        for (int j = 0; j < s.length(); i++, j++)
            bytes[i] = (byte)s.charAt(j);
    }

    /**
     * Get an integer formatted as an ASCII string from a byte array.
     *
     * @param bytes a byte array
     * @return an integer
     */
    public static long getLong(byte[] bytes) {
        long sign = +1;

        int i = 0;

        while (bytes[i] == ' ')
            i++;

        if (bytes[i] == '-') {
            sign = -1;

            i++;
        }

        long l = 0;

        while (i < bytes.length && bytes[i] != ' ')
            l = 10 * l + bytes[i++] - '0';

        return sign * l;
    }

    /**
     * Put an integer formatted as an ASCII string into a byte array.
     *
     * <p>If the length of the string is smaller than the length of the array,
     * the space character is used to fill the trailing bytes.</p>
     *
     * @param bytes a byte array
     * @param l an integer
     * @throws IndexOutOfBoundsException if the length of the string is larger
     *   than the length of the array
     */
    public static void putLongLeft(byte[] bytes, long l) {
        long sign = l;

        if (sign < 0)
            l = -l;

        int i = 0;

        do {
            bytes[i++] = (byte)('0' + l % 10);

            l /= 10;
        } while (l > 0);

        if (sign < 0)
            bytes[i++] = '-';

        ByteArrays.reverse(bytes, 0, i);

        for (; i < bytes.length; i++)
            bytes[i] = ' ';
    }

    /**
     * Put an integer formatted as an ASCII string into a byte array.
     *
     * <p>If the length of the string is smaller than the length of the array,
     * the space character is used to fill the leading bytes.</p>
     *
     * @param bytes a byte array
     * @param l an integer
     * @throws IndexOutOfBoundsException if the length of the string is larger
     *   than the length of the array
     */
    public static void putLongRight(byte[] bytes, long l) {
        long sign = l;

        if (sign < 0)
            l = -l;

        int i = bytes.length - 1;

        do {
            bytes[i--] = (byte)('0' + l % 10);

            l /= 10;
        } while (l > 0);

        if (sign < 0)
            bytes[i--] = '-';

        for (; i >= 0; i--)
            bytes[i] = ' ';
    }

    /**
     * Get a decimal number formatted as an ASCII string from a byte array in
     * a fixed-point representation.
     *
     * @param bytes a byte array
     * @param decimals the number of decimal digits in the fixed-point
     *   representation
     * @return a decimal number
     */
    public static long getFixed(byte[] bytes, int decimals) {
        long sign = +1;

        int i = 0;

        while (bytes[i] == ' ')
            i++;

        if (bytes[i] == '-') {
            sign = -1;

            i++;
        }

        long f = 0;

        while (i < bytes.length && bytes[i] != '.' && bytes[i] != ' ')
            f = 10 * f + bytes[i++] - '0';

        if (i == bytes.length || bytes[i] != '.')
            return sign * f * POWERS_OF_TEN[decimals];

        int point = i++;

        while (i < bytes.length && bytes[i] != ' ')
            f = 10 * f + bytes[i++] - '0';

        int count = i - point - 1;

        if (count > decimals)
            return sign * f / POWERS_OF_TEN[count - decimals];
        else
            return sign * f * POWERS_OF_TEN[decimals - count];
    }

    /**
     * Put a decimal number formatted as an ASCII string into a byte array
     * from a fixed-point representation.
     *
     * <p>If the length of the string is smaller than the length of the byte
     * array, the space character is used to fill the trailing bytes.</p>
     *
     * @param bytes a byte array
     * @param f a decimal number
     * @param decimals the number of decimal digits in the fixed-point
     *   representation
     * @throws IndexOutOfBoundsException if the length of the string is larger
     *   than the length of the array
     */
    public static void putFixedLeft(byte[] bytes, long f, int decimals) {
        long sign = f;

        if (sign < 0)
            f = -f;

        int i = 0;

        do {
            bytes[i++] = (byte)('0' + f % 10);

            f /= 10;
        } while (i < decimals);

        bytes[i++] = '.';

        do {
            bytes[i++] = (byte)('0' + f % 10);

            f /= 10;
        } while (f > 0);

        if (sign < 0)
            bytes[i++] = '-';

        ByteArrays.reverse(bytes, 0, i);

        for (; i < bytes.length; i++)
            bytes[i] = ' ';
    }

    /**
     * Put a decimal number formatted as an ASCII string into a byte array
     * from a fixed-point representation.
     *
     * <p>If the length of the string is smaller than the length of the byte
     * array, the space character is used to fill the leading bytes.</p>
     *
     * @param bytes a byte array
     * @param f a decimal number
     * @param decimals the number of decimal digits in the fixed-point
     *   representation
     * @throws IndexOutOfBoundsException if the length of the string is larger
     *   than the length of the array
     */
    public static void putFixedRight(byte[] bytes, long f, int decimals) {
        long sign = f;

        if (sign < 0)
            f = -f;

        int i = bytes.length - 1;

        do {
            bytes[i--] = (byte)('0' + f % 10);

            f /= 10;
        } while (bytes.length - 1 - i < decimals);

        bytes[i--] = '.';

        do {
            bytes[i--] = (byte)('0' + f % 10);

            f /= 10;
        } while (f > 0);

        if (sign < 0)
            bytes[i--] = '-';

        for (; i >= 0; i--)
            bytes[i] = ' ';
    }

    /**
     * Pack an ASCII string into a short.
     *
     * <p>If the length of the string is less than two characters, the space
     * character is used to fill the least significant bits.</p>
     *
     * <p>If the length of the string is more than two characters, only the
     * first two characters are packed.</p>
     *
     * @param s a string
     * @return a short
     */
    public static short packShort(CharSequence s) {
        return (short)pack(s, 2);
    }

    /**
     * Pack an ASCII string into an integer.
     *
     * <p>If the length of the string is less than four characters, the space
     * character is used to fill the least significant bits.</p>
     *
     * <p>If the length of the string is more than four characters, only the
     * first four characters are packed.</p>
     *
     * @param s a string
     * @return an integer
     */
    public static int packInt(CharSequence s) {
        return (int)pack(s, 4);
    }

    /**
     * Pack an ASCII string into a long.
     *
     * <p>If the length of the string is less than eight characters, the space
     * character is used to fill the least significant bits.</p>
     *
     * <p>If the length of the string is more than eight characters, only the
     * first eight characters are packed.</p>
     *
     * @param s a string
     * @return a long
     */
    public static long packLong(CharSequence s) {
        return pack(s, 8);
    }

    private static long pack(CharSequence s, int size) {
        long l = 0;
        int  i = 0;

        for (; i < Math.min(s.length(), size); i++)
            l = (l << 8) | (byte)s.charAt(i);

        for (; i < size; i++)
            l = (l << 8) | (byte)' ';

        return l;
    }

    /**
     * Unpack a short into an ASCII string.
     *
     * @param s a short
     * @param b a string builder
     */
    public static void unpackShort(short s, StringBuilder b) {
        unpack(s, 2, b);
    }

    /**
     * Unpack a short into an ASCII string.
     *
     * @param s a short
     * @return a string
     */
    public static String unpackShort(short s) {
        return unpack(s, 2);
    }

    /**
     * Unpack an integer into an ASCII string.
     *
     * @param i an integer
     * @param b a string builder
     */
    public static void unpackInt(int i, StringBuilder b) {
        unpack(i, 4, b);
    }

    /**
     * Unpack an integer into an ASCII string.
     *
     * @param i an integer
     * @return a string
     */
    public static String unpackInt(int i) {
        return unpack(i, 4);
    }

    /**
     * Unpack a long into an ASCII string.
     *
     * @param l a long
     * @param b a string builder
     */
    public static void unpackLong(long l, StringBuilder b) {
        unpack(l, 8, b);
    }

    /**
     * Unpack a long into an ASCII string.
     *
     * @param l a long
     * @return a string
     */
    public static String unpackLong(long l) {
        return unpack(l, 8);
    }

    private static void unpack(long l, int size, StringBuilder b) {
        for (int i = 0; i < size; i++)
            b.append((char)((l >> (8 * (size - 1 - i))) & 0xFF));
    }

    private static String unpack(long l, int size) {
        StringBuilder b = new StringBuilder(size);

        unpack(l, size, b);

        return b.toString();
    }

}
