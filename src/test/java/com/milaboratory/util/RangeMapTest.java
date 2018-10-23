/*
 * Copyright 2016 MiLaboratory.com
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
package com.milaboratory.util;

import com.milaboratory.core.Range;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RangeMapTest {
    @Test(expected = RangeMap.IntersectingRangesException.class)
    public void test1() throws Exception {
        RangeMap<Integer> map = new RangeMap<>();
        map.put(new Range(10, 20), 1);
        map.put(new Range(19, 30), 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test2() throws Exception {
        RangeMap<Integer> map = new RangeMap<>();
        map.put(new Range(10, 20), 1);
        map.put(new Range(48, 35), 2);
    }

    @Test
    public void test3() throws Exception {
        RangeMap<Integer> map = new RangeMap<>();
        map.put(new Range(10, 20), 1);
        map.put(new Range(35, 48), 2);
        map.put(new Range(70, 80), 3);

        assertEquals(new Range(10, 80), map.enclosingRange());

        assertEquals((Object) 1, map.findContaining(new Range(10, 20)).getValue());
        assertEquals((Object) 1, map.findContaining(new Range(11, 19)).getValue());
        Assert.assertNull(map.findContaining(new Range(9, 19)));

        assertEquals((Object) 2, map.findContaining(new Range(35, 48)).getValue());
        assertEquals((Object) 2, map.findContaining(new Range(36, 48)).getValue());
        assertEquals((Object) 2, map.findContaining(new Range(35, 47)).getValue());
        Assert.assertNull(map.findContaining(new Range(34, 49)));

        assertEquals((Object) 3, map.findContaining(new Range(70, 80)).getValue());
        assertEquals((Object) 3, map.findContaining(new Range(71, 79)).getValue());
        Assert.assertNull(map.findContaining(new Range(71, 81)));
    }

    @Test(expected = RangeMap.IntersectingRangesException.class)
    public void test4() throws Exception {
        RangeMap<Integer> map = new RangeMap<>();
        map.put(new Range(10, 20), 1);
        map.put(new Range(35, 48), 2);
        map.put(new Range(60, 61), 4);
        map.put(new Range(70, 80), 5);

        map.put(new Range(79, 81), 5);
    }

    @Test(expected = RangeMap.IntersectingRangesException.class)
    public void test5() throws Exception {
        RangeMap<Integer> map = new RangeMap<>();
        map.put(new Range(10, 20), 1);
        map.put(new Range(35, 48), 2);
        map.put(new Range(59, 61), 4);
        map.put(new Range(70, 80), 5);

        map.put(new Range(60, 61), 5);
    }

    @Test(expected = RangeMap.IntersectingRangesException.class)
    public void test6() throws Exception {
        RangeMap<Integer> map = new RangeMap<>();
        map.put(new Range(10, 20), 1);
        map.put(new Range(35, 48), 2);
        map.put(new Range(59, 61), 4);
        map.put(new Range(70, 80), 5);

        map.put(new Range(5, 11), 5);
    }

    @Test
    public void test7() throws Exception {
        RangeMap<Integer> map = new RangeMap<>();
        map.put(new Range(10, 20), 1);
        map.put(new Range(35, 48), 2);
        map.put(new Range(59, 61), 4);
        map.put(new Range(70, 80), 5);

        assertEquals(new Range(10, 80), map.enclosingRange());

        assertRangeIntersectsWith(15, 15, map);
        assertRangeIntersectsWith(5, 7, map);
        assertRangeIntersectsWith(15, 35, map, 1);
        assertRangeIntersectsWith(15, 36, map, 1, 2);
        assertRangeIntersectsWith(15, 48, map, 1, 2);
        assertRangeIntersectsWith(15, 60, map, 1, 2, 4);
        assertRangeIntersectsWith(15, 75, map, 1, 2, 4, 5);
        assertRangeIntersectsWith(19, 75, map, 1, 2, 4, 5);
        assertRangeIntersectsWith(20, 75, map, 2, 4, 5);
        assertRangeIntersectsWith(88, 90, map);

        assertRangeIntersectsOrTouches(15, 15, map, 1);
        assertRangeIntersectsOrTouches(5, 7, map);
        assertRangeIntersectsOrTouches(15, 35, map, 1, 2);
        assertRangeIntersectsOrTouches(15, 36, map, 1, 2);
        assertRangeIntersectsOrTouches(15, 48, map, 1, 2);
        assertRangeIntersectsOrTouches(15, 60, map, 1, 2, 4);
        assertRangeIntersectsOrTouches(15, 75, map, 1, 2, 4, 5);
        assertRangeIntersectsOrTouches(19, 75, map, 1, 2, 4, 5);
        assertRangeIntersectsOrTouches(20, 75, map, 1, 2, 4, 5);
        assertRangeIntersectsOrTouches(88, 90, map);
    }

    public void assertRangeIntersectsWith(int from, int to, RangeMap<Integer> map, int... values) {
        List<Map.Entry<Range, Integer>> al = map.findAllIntersecting(new Range(from, to));
        assertEquals(values.length, al.size());
        for (int i = 0; i < al.size(); i++)
            assertEquals((Object) values[i], al.get(i).getValue());
    }

    public void assertRangeIntersectsOrTouches(int from, int to, RangeMap<Integer> map, int... values) {
        List<Map.Entry<Range, Integer>> al = map.findAllIntersectingOrTouching(new Range(from, to));
        assertEquals(values.length, al.size());
        for (int i = 0; i < al.size(); i++)
            assertEquals((Object) values[i], al.get(i).getValue());
    }
}