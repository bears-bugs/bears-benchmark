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
package com.milaboratory.util;

import com.milaboratory.core.Range;

import java.util.*;

import static java.util.Map.Entry;

public final class RangeMap<T> implements java.io.Serializable {
    private final TreeMap<Range, T> map = new TreeMap<>(rangeComparator);

    public RangeMap() {
    }

    public Entry<Range, T> findContaining(Range range) {
        Entry<Range, T> ret = map.floorEntry(range);
        if (ret != null && ret.getKey().contains(range))
            return ret;
        return null;
    }

    public void put(Range range, T value) {
        if (range.isReverse())
            throw new IllegalArgumentException("Don't support inverted ranges.");
        if (range.isEmpty())
            throw new IllegalArgumentException("Don't support empty ranges.");
        if (containIntersectingRanges(range))
            throw new IntersectingRangesException();
        map.put(range, value);
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * Return range enclosing all range-keys of this container.
     *
     * @return range enclosing all range-keys of this container
     */
    public Range enclosingRange() {
        return new Range(map.firstKey().getLower(), map.lastKey().getUpper());
    }

    public Set<Entry<Range, T>> entrySet() {
        return map.entrySet();
    }

    public T remove(Range range) {
        return map.remove(range);
    }

    public List<Entry<Range, T>> findAllIntersecting(Range range) {
        if (range.isEmpty())
            return Collections.EMPTY_LIST;

        List<Entry<Range, T>> result = new ArrayList<>();

        Entry<Range, T> tmp = map.floorEntry(range);
        if (tmp == null)
            tmp = map.firstEntry();
        while (tmp != null && tmp.getKey().getFrom() < range.getTo()) {
            if (tmp.getKey().intersectsWith(range))
                result.add(tmp);
            tmp = map.higherEntry(tmp.getKey());
        }

        return result;
    }

    public List<Entry<Range, T>> findAllIntersectingOrTouching(Range range) {
        List<Entry<Range, T>> result = new ArrayList<>();

        Entry<Range, T> tmp = map.floorEntry(range);
        if (tmp == null)
            tmp = map.firstEntry();
        while (tmp != null && tmp.getKey().getFrom() <= range.getTo()) {
            if (tmp.getKey().intersectsWithOrTouches(range))
                result.add(tmp);
            tmp = map.higherEntry(tmp.getKey());
        }

        return result;
    }

    public boolean containIntersectingRanges(Range range) {
        Entry<Range, T> tmp = map.floorEntry(range);
        if (tmp != null && tmp.getKey().intersectsWith(range))
            return true;
        tmp = map.higherEntry(range);
        return tmp != null && tmp.getKey().intersectsWith(range);
    }

    public Entry<Range, T> findSingleIntersection(Range range) {
        Entry<Range, T> ret = null, tmp = map.floorEntry(range);
        if (tmp != null && tmp.getKey().intersectsWith(range))
            ret = tmp;
        tmp = map.higherEntry(range);
        if (tmp != null && tmp.getKey().intersectsWith(range))
            if (ret != null)
                throw new IllegalArgumentException("Several intersection hits");
            else
                ret = tmp;
        return ret;
    }

    public boolean isOverFragmented() {
        Range prev = null;
        for (Range range : map.navigableKeySet()) {
            if (prev == null)
                prev = range;
            else if (prev.getTo() == range.getFrom())
                return true;
        }
        return false;
    }

    public static final class IntersectingRangesException extends IllegalArgumentException {
        public IntersectingRangesException() {
        }

        public IntersectingRangesException(String message) {
            super(message);
        }
    }

    private static final Comparator<Range> rangeComparator = new Comparator<Range>() {
        @Override
        public int compare(Range o1, Range o2) {
            int cmp;
            if ((cmp = Integer.compare(o1.getLower(), o2.getLower())) != 0)
                return cmp;

            return 0; //Integer.compare(o2.getUpper(), o1.getUpper());
        }
    };
}
