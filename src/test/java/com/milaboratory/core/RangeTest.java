/*
 * Copyright 2015 MiLaboratory.com
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
package com.milaboratory.core;

import com.milaboratory.core.io.util.IOTestUtil;
import com.milaboratory.test.TestUtil;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class RangeTest {
    private Range r(int from, int to) {
        return new Range(from, to);
    }

    @Test
    public void testValues() throws Exception {
        assertEquals(20, r(20, 10).getUpper());
        assertEquals(10, r(20, 10).getLower());
        assertEquals(20, r(20, 10).getFrom());
        assertEquals(10, r(20, 10).getTo());

        assertEquals(20, r(10, 20).getUpper());
        assertEquals(10, r(10, 20).getLower());
        assertEquals(10, r(10, 20).getFrom());
        assertEquals(20, r(10, 20).getTo());
    }

    @Test
    public void testContains() throws Exception {
        assertFalse(r(20, 10).contains(20));
        assertTrue(r(20, 10).contains(19));

        assertFalse(r(20, 10).contains(r(21, 10)));
        assertTrue(r(20, 10).contains(r(20, 10)));
        assertTrue(r(20, 10).contains(r(19, 11)));
        assertFalse(r(20, 10).contains(r(21, 9)));
    }

    @Test
    public void testIntersection1() throws Exception {
        assertEquals(r(10, 20).intersection(r(14, 40)), r(14, 20));
        assertEquals(r(10, 20).intersection(r(21, 40)), null);
    }

    @Test
    public void testMerge() throws Exception {
        assertEquals(r(10, 20).tryMerge(r(14, 40)), r(10, 40));
        assertEquals(r(10, 20).tryMerge(r(21, 40)), null);
    }

    @Test
    public void testIntersection2() throws Exception {
        assertTrue(r(10, 20).intersectsWith(r(20, 10)));
        assertTrue(r(10, 20).intersectsWith(r(19, 11)));
        assertTrue(r(19, 11).intersectsWith(r(20, 10)));
    }

    @Test
    public void testCheckConvert1() throws Exception {
        checkConvert(r(10, 20), 10);
        checkConvert(r(10, 20), 13);
        checkConvert(r(20, 10), 19);
        checkConvert(r(20, 10), 18);
    }

    @Test
    public void test23e14() throws Exception {
        System.out.println(Integer.toBinaryString(((byte) 'A')));
        System.out.println(Integer.toBinaryString(((byte) 'T')));
        System.out.println(Integer.toBinaryString(((byte) 'G')));
        System.out.println(Integer.toBinaryString(((byte) 'C')));
        System.out.println();
        System.out.println(Integer.toBinaryString(((byte) 'B')));
        //System.out.println(Integer.toBinaryString(~(((byte) 'A') | ((byte) 'T') | ((byte) 'G') | ((byte) 'C'))));
    }

    private void checkConvert(Range range, int position) {
        int relativePosition = range.convertPointToRelativePosition(position);
        assertEquals(position, range.convertPointToAbsolutePosition(relativePosition));

        relativePosition = range.convertBoundaryToRelativePosition(position);
        assertEquals(position, range.convertBoundaryToAbsolutePosition(relativePosition));
    }

    @Test
    public void testRelativeRange() throws Exception {
        assertRelativeRange(r(20, 40), r(0, 100), r(20, 40));
        assertRelativeRange(r(20, 40), r(50, 150), r(70, 90));
        assertRelativeRange(r(80, 90), r(100, 0), r(20, 10));
        assertRelativeRange(r(90, 80), r(100, 0), r(10, 20));
    }

    @Test
    public void testRelativeRangeBoundaryCases() throws Exception {
        assertRelativeRange(r(70, 70), r(30, 100), r(100, 100));
        assertRelativeRange(r(0, 0), r(30, 100), r(30, 30));
        assertRelativeRange(r(0, 0), r(100, 30), r(100, 100));
        assertRelativeRange(r(70, 70), r(100, 30), r(30, 30));
    }

    public void assertRelativeRange(Range expectedRelativeRange, Range baseRange, Range range) {
        Range rel = baseRange.getRelativeRangeOf(range);
        assertEquals(expectedRelativeRange, rel);
        assertEquals(range, baseRange.getAbsoluteRangeFor(rel));
    }

    @Test
    public void test4() throws Exception {
        Range se = new Range(3, 5);
        IOTestUtil.assertJavaSerialization(se);
    }

    @Test
    public void test5() throws Exception {
        Range se = new Range(3, 5);
        TestUtil.assertJson(se);
    }

    @Test
    public void testIntersection3() throws Exception {
        Range se = new Range(3, 5);
        Range s3 = new Range(3, 3);
        Range s5 = new Range(5, 5);

        assertNotIntersects(se, s3);
        assertNotIntersects(se, s5);

        assertIntersectsOrTouches(se, s3);
        assertIntersectsOrTouches(se, s5);
    }

    @Test
    public void testIntersection4() throws Exception {
        Range se = new Range(30, 50);
        for (int i = 29; i <= 31; i++)
            for (int j = 49; j <= 51; j++) {
                assertIntersects(se, new Range(i, j));
                assertIntersectsOrTouches(se, new Range(i, j));
            }

        assertNotIntersects(se, new Range(50, 60));
        assertNotIntersects(se, new Range(20, 30));

        assertIntersectsOrTouches(se, new Range(50, 60));
        assertIntersectsOrTouches(se, new Range(20, 30));
    }

    @Test
    public void testIntersection5() throws Exception {
        Range r0 = new Range(30, 30);
        Range r1 = new Range(40, 40);
        Range r2 = new Range(30, 50);
        Range r4 = new Range(29, 29);

        assertFalse(r1.intersectsWith(r2));
        assertFalse(r2.intersectsWith(r1));

        assertTrue(r1.intersectsWithOrTouches(r2));
        assertTrue(r2.intersectsWithOrTouches(r1));

        assertTrue(r0.intersectsWithOrTouches(r2));
        assertTrue(r2.intersectsWithOrTouches(r0));

        assertFalse(r4.intersectsWithOrTouches(r2));
        assertFalse(r2.intersectsWithOrTouches(r4));

        assertNull(r1.intersection(r2));
        assertNull(r2.intersection(r1));

        assertTrue(r1.intersectionWithTouch(r2).isEmpty());
        assertTrue(r2.intersectionWithTouch(r1).isEmpty());
    }

    @Test
    public void subtractionTest1() throws Exception {
        Range rr = r(100, 200);

        assertEquals(Arrays.asList(r(150, 200)), rr.without(r(50, 150)));
        assertEquals(Arrays.asList(), rr.without(r(50, 200)));
        assertEquals(Arrays.asList(), rr.without(r(50, 250)));
        assertEquals(Arrays.asList(r(100, 150)), rr.without(r(150, 250)));
        assertEquals(Arrays.asList(r(100, 130), r(150, 200)), rr.without(r(130, 150)));
    }

    public static void assertIntersects(Range r1, Range r2) {
        assertEquals(r1.intersectsWith(r2), r2.intersectsWith(r1));
        assertTrue(r1.intersectsWith(r2));
    }

    public static void assertNotIntersects(Range r1, Range r2) {
        assertEquals(r1.intersectsWith(r2), r2.intersectsWith(r1));
        assertFalse(r1.intersectsWith(r2));
    }

    public static void assertIntersectsOrTouches(Range r1, Range r2) {
        assertEquals(r1.intersectsWithOrTouches(r2), r2.intersectsWithOrTouches(r1));
        assertTrue(r1.intersectsWithOrTouches(r2));
    }

    public static void assertNotIntersectsOrTouches(Range r1, Range r2) {
        assertEquals(r1.intersectsWithOrTouches(r2), r2.intersectsWithOrTouches(r1));
        assertFalse(r1.intersectsWithOrTouches(r2));
    }
}
