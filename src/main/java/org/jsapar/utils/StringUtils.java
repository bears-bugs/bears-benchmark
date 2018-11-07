package org.jsapar.utils;

@SuppressWarnings("WeakerAccess")
public class StringUtils {

    /**
     * Removes all characters of specified type from the string
     *
     * @param s          The string to remove from
     * @param chToRemove The character to remove
     * @return The new string where all character of type chToRemove has been
     * removed.
     */
    public static String removeAll(String s, char chToRemove) {
        return s.codePoints().filter(it -> it != (int) chToRemove)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }


    /**
     * Removes all characters that are regarded as white-space according to
     * Character.isWhitespace(int codePoint) function.
     *
     * @param s The string to remove all white-spaces from
     * @return A string without any white-space characters.
     */
    public static String removeAllWhitespaces(String s) {
        return removeAll(s, Character::isWhitespace);
    }

    /**
     * Removes all characters that are regarded as space according to
     * Character.isSpaceChar(int codePoint) function.
     *
     * @param s The string to remove all spaces from
     * @return A string without any space characters.
     */
    public static String removeAllSpaces(String s) {
        return removeAll(s, Character::isSpaceChar);
    }

    private interface CheckCharacterType{
        boolean check(int codePoint);
    }

    /**
     * Removes all characters that are regarded as space according to
     * provided check
     *
     * @param s        String to remove from.
     * @param check    Check lambda, if returns true, character will be removed.
     * @return A string without any space characters.
     */
    private static String removeAll(String s, CheckCharacterType check) {
        return s.codePoints().filter(it -> !check.check(it))
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }

    /**
     * Counts number of whole occurrences of subString within s
     * @param s The string to count occurrences within
     * @param subString The sub string to count
     * @return The number of whole occurrences of subString within s.
     */
    public static int countMatches(final String s, final String subString) {
        if (s == null || s.isEmpty() ) {
            return 0;
        }
        int count = 0;
        int index = 0;
        while ((index = s.indexOf(subString, index)) >= 0 ) {
            count++;
            index += subString.length();
        }
        return count;
    }

}
