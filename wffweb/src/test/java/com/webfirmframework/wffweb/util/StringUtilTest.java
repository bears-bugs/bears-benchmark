/*
 * Copyright 2014-2018 Web Firm Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.webfirmframework.wffweb.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.logging.Logger;

import org.junit.Test;

/**
 * @author WFF
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public class StringUtilTest {

    private static final Logger LOGGER = Logger
            .getLogger(StringUtilTest.class.getName());

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.util.StringUtil#convertToSingleSpace(java.lang.String)}.
     */
    @Test
    public void testConvertToSingleSpace() {
        assertEquals("a b", StringUtil.convertToSingleSpace("a   b"));
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.util.StringUtil#getFirstSubstring(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetFirstSubstringStringStringString() {
        final String firstSubstring = StringUtil.getFirstSubstring(
                "dfsf rgb(7, 8, 9)rgb(3, 5, 6)", "rgb(", ")");
        assertEquals("rgb(7, 8, 9)", firstSubstring);
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.util.StringUtil#getFirstSubstring(java.lang.String, java.lang.String, java.lang.String, int)}.
     */
    @Test
    public void testGetFirstSubstringStringStringStringInt() {
        final String firstSubstring = StringUtil.getFirstSubstring(
                "dfsf rgb(7, 8, 9, 1)rgb(3, 5, 6)", "rgb(", ")", 19);
        assertEquals("rgb(3, 5, 6)", firstSubstring);
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.util.StringUtil#getAllSubstrings(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetAllSubstrings() {
        final String[] allSubstrings = StringUtil.getAllSubstrings(
                "dfsf rgb(7, 8, 9, 1)rgb(3, 5, 6)", "rgb(", ")");

        assertEquals("rgb(7, 8, 9, 1)", allSubstrings[0]);
        assertEquals("rgb(3, 5, 6)", allSubstrings[1]);
        assertEquals(2, allSubstrings.length);
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.util.StringUtil#startIndexOf(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testStartIndexOfStringStringString() {
        final int startIndexOf = StringUtil
                .startIndexOf("green rgb(7, 8, 9, 1)rgb(3, 5, 6)", "rgb(", ")");
        assertEquals(6, startIndexOf);
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.util.StringUtil#endIndexOf(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testEndIndexOfStringStringString() {
        final int endIndex = StringUtil
                .endIndexOf("green rgb(7, 8, 9, 1)rgb(3, 5, 6)", "rgb(", ")");
        assertEquals(20, endIndex);
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.util.StringUtil#startIndexOf(java.lang.String, java.lang.String, java.lang.String, int)}.
     */
    @Test
    public void testStartIndexOfStringStringStringInt() {
        final int startIndex = StringUtil.startIndexOf(
                "green rgb(7, 8, 9, 1) rgb(3, 5, 6)", "rgb(", ")", 20);
        assertEquals(22, startIndex);
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.util.StringUtil#endIndexOf(java.lang.String, java.lang.String, java.lang.String, int)}.
     */
    @Test
    public void testEndIndexOfStringStringStringInt() {
        final int endIndex = StringUtil.endIndexOf(
                "green rgb(7, 8, 9, 1) rgb(3, 5, 6)", "rgb(", ")", 20);
        assertEquals(33, endIndex);
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.util.StringUtil#startIndexAndEndIndexOf(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testStartIndexAndEndIndexOfStringStringString() {
        final int[] startIndexAndEndIndex = StringUtil.startIndexAndEndIndexOf(
                "green rgb(7, 8, 9, 1) rgb(3, 5, 6)", "rgb(", ")");
        assertEquals(6, startIndexAndEndIndex[0]);
        assertEquals(20, startIndexAndEndIndex[1]);
        assertEquals(2, startIndexAndEndIndex.length);
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.util.StringUtil#startIndexAndEndIndexOf(java.lang.String, java.lang.String, java.lang.String, int)}.
     */
    @Test
    public void testStartIndexAndEndIndexOfStringStringStringInt() {
        final int[] startIndexAndEndIndex = StringUtil.startIndexAndEndIndexOf(
                "green rgb(7, 8, 9, 1) rgb(3, 5, 6)", "rgb(", ")", 7);
        assertEquals(22, startIndexAndEndIndex[0]);
        assertEquals(33, startIndexAndEndIndex[1]);
        assertEquals(2, startIndexAndEndIndex.length);
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.util.StringUtil#startAndEndIndexesOf(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testStartAndEndIndexesOf() {
        final int[][] startAndEndIndexes = StringUtil.startAndEndIndexesOf(
                "green rgb(7, 8, 9, 1) rgb(3, 5, 6)", "rgb(", ")");
        assertEquals(2, startAndEndIndexes.length);

        int[] startIndexAndEndIndex = startAndEndIndexes[0];
        assertEquals(6, startIndexAndEndIndex[0]);
        assertEquals(20, startIndexAndEndIndex[1]);
        assertEquals(2, startIndexAndEndIndex.length);

        startIndexAndEndIndex = startAndEndIndexes[1];
        assertEquals(22, startIndexAndEndIndex[0]);
        assertEquals(33, startIndexAndEndIndex[1]);
        assertEquals(2, startIndexAndEndIndex.length);

        assertEquals(0,
                StringUtil.startAndEndIndexesOf("there is no combination",
                        "rgb(", ")").length);

    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.util.StringUtil#isEqual(java.lang.Object[])}.
     */
    @Test
    public void testIsEqualStringArray() {
        assertFalse(StringUtil.isEqual(null));
        assertFalse(StringUtil.isEqual(new String[] { null }));
        assertFalse(StringUtil.isEqual(new String[] { "d" }));
        assertFalse(StringUtil.isEqual("dfd"));
        assertTrue(StringUtil.isEqual(null, null, null));
        assertFalse(StringUtil.isEqual("d", null, null));
        assertFalse(StringUtil.isEqual(null, "dfd", null));
        assertFalse(StringUtil.isEqual(null, null, "dfd"));
        String obj1 = "hi";
        String obj2 = "hi";
        String obj3 = "hi";
        assertTrue(StringUtil.isEqual(obj1, obj2, obj3));
        obj2 = "hidfd";
        assertFalse(StringUtil.isEqual(obj1, obj2, obj3));
        obj2 = obj1;
        obj3 = "dfdf";
        assertFalse(StringUtil.isEqual(obj1, obj2, obj3));
        obj3 = obj1;
        obj1 = "dfdasdfsdf";
        assertFalse(StringUtil.isEqual(obj1, obj2, obj3));
        obj3 = "dfdfdfdfddddddddd";
        assertFalse(StringUtil.isEqual(obj1, obj2, obj3));
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.util.StringUtil#isEqual(java.lang.Object, java.lang.Object)}.
     */
    @Test
    public void testIsEqualStringObject() {
        assertTrue(StringUtil.isEqual(null, null));
        assertFalse(StringUtil.isEqual("df", null));
        assertFalse(StringUtil.isEqual(null, "df"));
        String obj1 = "hi";
        String obj2 = "hi";
        assertTrue(StringUtil.isEqual(obj1, obj2));
        assertTrue(StringUtil.isEqual("hi", "hi"));
    }

    @SuppressWarnings("unused")
    @Test
    public void testSplitBySpace() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 500; i++) {
            sb.append("word");
            sb.append(" ");
        }
        String string = sb.toString();
        long withSplit = 0;
        long withSplitBySpace = 0;
        {
            long before = System.nanoTime();
            final String[] split = StringUtil.splitBySpace(string);
            long after = System.nanoTime();
            withSplitBySpace = after - before;
        }
        {
            long before = System.nanoTime();
            final String[] split = string.split(" ");
            long after = System.nanoTime();
            withSplit = after - before;
        }
        LOGGER.info("ns of withSplit " + withSplit + ", withSplitBySpace "
                + withSplitBySpace);
        LOGGER.info("withSplit - withSplitBySpace = "
                + (withSplit - withSplitBySpace) + " ns");
        {
            String s = "";
            assertEquals(1, StringUtil.splitBySpace(s).length);
        }

    }

    @Test
    public void testEndsWithSpace() {
        assertFalse(StringUtil.endsWithSpace("something"));
        assertTrue(StringUtil.endsWithSpace("something "));
    }

    @Test
    public void testStartsWithSpace() {
        assertFalse(StringUtil.startsWithSpace("something"));
        assertTrue(StringUtil.startsWithSpace(" something"));
    }

    @Test
    public void testContainsSpace() throws Exception {
        assertFalse(StringUtil.containsSpace(""));
        assertTrue(StringUtil.containsSpace(" something"));
        assertTrue(StringUtil.containsSpace("something "));
        assertTrue(StringUtil.containsSpace("some thing"));
    }

    @Test
    public void testContainsMinus() throws Exception {
        assertFalse(StringUtil.containsMinus(""));
        assertTrue(StringUtil.containsMinus("-something"));
        assertTrue(StringUtil.containsMinus("something-"));
        assertTrue(StringUtil.containsMinus("some-thing"));
    }

    @Test
    public void testContainsPlus() throws Exception {
        assertFalse(StringUtil.containsPlus(""));
        assertTrue(StringUtil.containsPlus("+something"));
        assertTrue(StringUtil.containsPlus("something+"));
        assertTrue(StringUtil.containsPlus("some+thing"));
    }

    @Test
    public void testEndsWithColon() throws Exception {
        assertFalse(StringUtil.endsWithColon(""));
        assertFalse(StringUtil.endsWithColon(":something"));
        assertFalse(StringUtil.endsWithColon("sdfsf:something"));
        assertTrue(StringUtil.endsWithColon("something:"));
        assertTrue(StringUtil.endsWithColon("something :"));
        assertTrue(StringUtil.endsWithColon("some+thing :"));
    }

    @Test
    public void testJoin1() throws Exception {
        final String join = StringUtil.join(',', ':', ';', "one", "two",
                "three", "four");

        assertEquals(":one,two,three,four;", join);

    }

    @Test
    public void testJoin2() throws Exception {
        final String join = StringUtil.join(',', "one", "two", "three", "four");

        assertEquals("one,two,three,four", join);

    }

    @Test
    public void testJoin3() throws Exception {
        {
            final String join = StringUtil.join(',',
                    Arrays.asList("one", "two", "three", "four"));
            assertEquals("one,two,three,four", join);
        }
        {
            final String join = StringUtil.join(' ',
                    Arrays.asList("one", "two", "three", "four"));
            assertEquals("one two three four", join);
        }
    }
    
    @Test
    public void testIsBlank() throws Exception {
        assertTrue(StringUtil.isBlank("  \n  "));
        assertTrue(StringUtil.isBlank("\n"));
        assertTrue(StringUtil.isBlank("\r\n"));
        assertTrue(StringUtil.isBlank("\r\r\r"));
        assertTrue(StringUtil.isBlank(" "));
        assertTrue(StringUtil.isBlank("\t"));
        assertTrue(StringUtil.isBlank("      "));
        assertTrue(StringUtil.isBlank(""));
        assertFalse(StringUtil.isBlank("\na\n"));
        assertFalse(StringUtil.isBlank("abc"));
        assertFalse(StringUtil.isBlank("_"));
    }
    
    @Test
    public void testStrip() throws Exception {
        assertEquals("one", StringUtil.strip("    one      "));
        assertEquals("one", StringUtil.strip("\none\n"));
        assertEquals("one", StringUtil.strip("\r\none\r\n"));
        assertEquals("one", StringUtil.strip("\rone\r"));
        assertEquals("one", StringUtil.strip("\t\tone\t\t\t"));
        assertEquals("_", StringUtil.strip("\t\t_\t\t\t"));
        assertEquals("", StringUtil.strip(" \t\r\r\n  "));
    }

}
