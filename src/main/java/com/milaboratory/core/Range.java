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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.milaboratory.core.io.binary.RangeSerializer;
import com.milaboratory.primitivio.annotations.Serializable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This class represents a range of positions in a sequence (e.g. sub-sequence). Range can be <b>reversed</b> ({@code
 * from > to}), to represent reverse complement sub-sequence of a nucleotide sequence.
 *
 * <p><b>Main contract:</b> upper limit (with biggest value) is always exclusive, and lower is always inclusive.</p>
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE)
@Serializable(by = RangeSerializer.class)
public final class Range implements java.io.Serializable, Comparable<Range> {
    static final long serialVersionUID = 1L;

    private final int lower;
    private final int upper;
    private final boolean reversed;

    public Range(int lower, int upper, boolean reversed) {
        if (lower > upper)
            throw new IllegalArgumentException();

        this.lower = lower;
        this.upper = upper;
        this.reversed = reversed;
    }

    @JsonCreator
    public Range(@JsonProperty("from") int from,
                 @JsonProperty("to") int to) {
        if (this.reversed = (from > to)) {
            this.upper = from;
            this.lower = to;
        } else {
            this.upper = to;
            this.lower = from;
        }
    }

    public Range expand(int offset) {
        return expand(offset, offset);
    }

    public Range expand(int leftOffset, int rightOffset) {
        return new Range(lower - leftOffset, upper + rightOffset, reversed);
    }

    /**
     * Returns {@literal true} if {@code length() == 0}.
     *
     * @return {@literal true} if {@code length() == 0}.
     */
    public boolean isEmpty() {
        return upper == lower;
    }

    /**
     * Returns the length of this range.
     *
     * @return length of this range
     */
    public int length() {
        return upper - lower;
    }

    /**
     * Returns true if this range is reversed.
     *
     * @return true if this range is reversed
     */
    public boolean isReverse() {
        return reversed;
    }

    /**
     * Return +1 if this range is normal and -1 if it is reversed
     *
     * @return +1 if this range is normal and -1 if it is reversed
     */
    public int sig() {
        return reversed ? -1 : +1;
    }

    /**
     * Return reversed range.
     *
     * @return reversed range
     */
    public Range reverse() {
        return new Range(lower, upper, !reversed);
    }

    /**
     * Returns true if two ranges has the same direction. Always return true if any of ranges are empty.
     *
     * @param other other range to compare with
     * @return true if two ranges has the same direction. Always return true if any of ranges are of zero length
     */
    public boolean hasSameDirection(Range other) {
        return this.isEmpty() || other.isEmpty() || this.isReverse() == other.isReverse();
    }

    /**
     * Returns from value. This bound may be exclusive of inclusive depending on the range orientation (see main
     * contract in the class description).
     *
     * @return from value (exclusive or inclusive)
     */
    @JsonProperty("from")
    public int getFrom() {
        return reversed ? upper : lower;
    }

    /**
     * Returns to value. This bound may be exclusive of inclusive depending on the range orientation (see main contract
     * in the class description).
     *
     * @return to value (exclusive or inclusive)
     */
    @JsonProperty("to")
    public int getTo() {
        return reversed ? lower : upper;
    }

    /**
     * Returns upper (with biggest value) bound of this range. This bound is always exclusive.
     *
     * @return upper limit of this range (exclusive)
     */
    public int getUpper() {
        return upper;
    }

    /**
     * Returns lower (with least value) bound of this range. This bound is always inclusive.
     *
     * @return lower limit of this range (inclusive)
     */
    public int getLower() {
        return lower;
    }

    /**
     * Returns reversed range.
     *
     * @return reversed range
     */
    public Range inverse() {
        return new Range(lower, upper, !reversed);
    }

    /**
     * Returns {@code true} if range contains provided {@code position}.
     *
     * @param position position
     * @return {@code true} if range contains provided {@code position}
     */
    public boolean contains(int position) {
        return position >= lower && position < upper;
    }

    public boolean containsBoundary(int position) {
        return position >= lower && position <= upper;
    }

    /**
     * Returns {@code true} if range contains {@code other} range.
     *
     * @param other other range
     * @return {@code true} if range contains {@code other} range
     */
    public boolean contains(Range other) {
        return lower <= other.lower && upper >= other.upper;
    }

    /**
     * Returns {@code true} if range intersects with {@code other} range.
     *
     * @param other other range
     * @return {@code true} if range intersects with {@code other} range
     */
    public boolean intersectsWith(Range other) {
        return !other.isEmpty() && !this.isEmpty() &&
                (this.contains(other.lower) || other.contains(this.lower)
                        || (other.upper > upper && other.lower < lower));
    }

    /**
     * Returns {@code true} if range intersects with or touches {@code other} range.
     *
     * @param other other range
     * @return {@code true} if range intersects with or touches {@code other} range
     */
    public boolean intersectsWithOrTouches(Range other) {
        return contains(other.lower) || contains(other.upper - 1) || (other.upper > upper && other.lower < lower) ||
                other.lower == upper || other.upper == lower;
    }


    /**
     * Returns intersection range with {@code other} range.
     *
     * @param other other range
     * @return intersection range with {@code other} range or null if ranges not intersects
     */
    public Range intersection(Range other) {
        if (!intersectsWith(other))
            return null;

        return new Range(Math.max(lower, other.lower), Math.min(upper, other.upper), reversed && other.reversed);
    }

    /**
     * Returns intersection range with {@code other} range.
     *
     * @param other other range
     * @return intersection range with {@code other} range or null if ranges not intersects
     */
    public Range intersectionWithTouch(Range other) {
        if (!intersectsWithOrTouches(other))
            return null;

        return new Range(Math.max(lower, other.lower), Math.min(upper, other.upper), reversed && other.reversed);
    }

    /**
     * Returns union range with {@code other} range.
     *
     * @param other other range
     * @return union range with {@code other} range or null if ranges not intersects ot touches
     */
    public Range tryMerge(Range other) {
        if (!intersectsWithOrTouches(other))
            return null;

        return new Range(Math.min(lower, other.lower), Math.max(upper, other.upper), reversed && other.reversed);
    }

    /**
     * Returns range moved using provided offset (e.g. [lower + offset, upper + offset, reversed])
     *
     * @param offset offset, can be negative
     * @return range moved using provided offset
     */
    public Range move(int offset) {
        if (offset == 0)
            return this;
        return new Range(lower + offset, upper + offset, reversed);
    }

    /**
     * Returns relative point position inside this range.
     *
     * @param absolutePosition absolute point position (in the same coordinates as this range boundaries)
     * @return relative point position inside this range
     */
    public int convertPointToRelativePosition(int absolutePosition) {
        if (absolutePosition < lower || absolutePosition >= upper)
            throw new IllegalArgumentException("Position outside this range (" + absolutePosition + ").");

        if (reversed)
            return upper - 1 - absolutePosition;
        else
            return absolutePosition - lower;
    }

    /**
     * Returns relative boundary position inside this range.
     *
     * @param absolutePosition absolute boundary position (in the same coordinates as this range boundaries)
     * @return relative boundary position inside this range
     */
    public int convertBoundaryToRelativePosition(int absolutePosition) {
        if (absolutePosition < lower || absolutePosition > upper)
            throw new IllegalArgumentException("Position outside this range (" + absolutePosition + ") this=" + this + ".");

        if (reversed)
            return upper - absolutePosition;
        else
            return absolutePosition - lower;
    }

    /**
     * Subtract provided range and return list of ranges contained in current range and not intersecting with other
     * range.
     *
     * @param range range to subtract
     * @return list of ranges contained in current range and not intersecting with other range
     */
    @SuppressWarnings("unchecked")
    public List<Range> without(Range range) {
        if (!intersectsWith(range))
            return Collections.singletonList(this);

        if (upper <= range.upper)
            return range.lower <= lower ? Collections.EMPTY_LIST : Collections.singletonList(new Range(lower, range.lower, reversed));

        if (range.lower <= lower)
            return Collections.singletonList(new Range(range.upper, upper, reversed));

        return Arrays.asList(new Range(lower, range.lower, reversed), new Range(range.upper, upper, reversed));
    }

    public Range getRelativeRangeOf(Range absoluteRange) {
        int from = convertBoundaryToRelativePosition(absoluteRange.getFrom()),
                to = convertBoundaryToRelativePosition(absoluteRange.getTo());
        if (from == -1 || to == -1)
            return null;
        return new Range(from, to);
    }

    public int[] convertBoundariesToRelativePosition(int... absolutePositions) {
        int[] result = new int[absolutePositions.length];

        for (int i = 0; i < absolutePositions.length; ++i)
            result[i] = convertBoundaryToRelativePosition(absolutePositions[i]);

        return result;
    }

    public int[] convertPointsToRelativePosition(int... absolutePositions) {
        int[] result = new int[absolutePositions.length];

        for (int i = 0; i < absolutePositions.length; ++i)
            result[i] = convertPointToRelativePosition(absolutePositions[i]);

        return result;
    }

    /**
     * Converts relative point position to absolute position
     *
     * @param relativePosition relative point position
     * @return absolute point position
     */
    public int convertPointToAbsolutePosition(int relativePosition) {
        if (relativePosition < 0 || relativePosition >= length())
            throw new IllegalArgumentException("Relative position outside this range (" + relativePosition + ").");

        if (reversed)
            return upper - 1 - relativePosition;
        else
            return relativePosition + lower;
    }

    /**
     * Converts relative boundary position to absolute position
     *
     * @param relativePosition relative boundary position
     * @return absolute point position
     */
    public int convertBoundaryToAbsolutePosition(int relativePosition) {
        if (relativePosition < 0 || relativePosition > length())
            throw new IllegalArgumentException("Relative position outside this range (" + relativePosition + ").");

        if (reversed)
            return upper - relativePosition;
        else
            return relativePosition + lower;
    }

    /**
     * Reverse operation for {@link #getRelativeRangeOf(Range)}.
     *
     * A.getAbsoluteRangeFor(A.getRelativeRangeOf(B)) == B
     *
     * @param relativeRange range defined relative to this range
     * @return absolute range
     */
    public Range getAbsoluteRangeFor(Range relativeRange) {
        int from = convertBoundaryToAbsolutePosition(relativeRange.getFrom()),
                to = convertBoundaryToAbsolutePosition(relativeRange.getTo());
        return new Range(from, to);
    }

    @Override
    public int compareTo(Range o) {
        int cmp;
        if ((cmp = Integer.compare(getLower(), o.getLower())) != 0)
            return cmp;

        if ((cmp = Integer.compare(getUpper(), o.getUpper())) != 0)
            return cmp;

        return Boolean.compare(isReverse(), o.isReverse());
    }

    @Override
    public String toString() {
        return "(" + lower + (reversed ? "<-" : "->") + upper + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Range range = (Range) o;

        return lower == range.lower && reversed == range.reversed && upper == range.upper;
    }

    @Override
    public int hashCode() {
        int result = lower;
        result = 31 * result + upper;
        result = 31 * result + (reversed ? 1 : 0);
        return result;
    }

    public static final Comparator<Range> COMPARATOR_BY_FROM = new Comparator<Range>() {
        @Override
        public int compare(Range o1, Range o2) {
            return Integer.compare(o1.getFrom(), o2.getTo());
        }
    };
}
