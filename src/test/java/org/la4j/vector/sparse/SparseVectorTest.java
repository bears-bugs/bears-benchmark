/*
 * Copyright 2011-2013, by Vladimir Kostyukov and Contributors.
 * 
 * This file is part of la4j project (http://la4j.org)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributor(s): -
 * 
 */

package org.la4j.vector.sparse;

import org.junit.Assert;
import org.junit.Test;
import org.la4j.vector.VectorFactory;
import org.la4j.vector.VectorTest;
import org.la4j.Vectors;
import org.la4j.vector.SparseVector;
import org.la4j.vector.functor.VectorAccumulator;

public abstract class SparseVectorTest<T extends SparseVector> extends VectorTest<T> {

    public SparseVectorTest(VectorFactory<T> factory) {
        super(factory);
    }

    @Test
    public void testCardinality() {
        SparseVector a = v(0.0, 0.0, 0.0, 0.0, 1.0);
        Assert.assertEquals(1, a.cardinality());
    }

    @Test
    public void testFoldNonZero_5() {
        SparseVector a = v(2.0, 0.0, 5.0, 0.0, 2.0);

        VectorAccumulator sum = Vectors.asSumAccumulator(0.0);
        VectorAccumulator product = Vectors.asProductAccumulator(1.0);

        Assert.assertEquals(9.0, a.foldNonZero(sum), Vectors.EPS);
        // check whether the accumulator were flushed
        Assert.assertEquals(9.0, a.foldNonZero(sum), Vectors.EPS);
        Assert.assertEquals(20.0, a.foldNonZero(product), Vectors.EPS);
        // check whether the accumulator were flushed
        Assert.assertEquals(20.0, a.foldNonZero(product), Vectors.EPS);
    }

    @Test
    public void testIsZeroAt_4() {
        SparseVector a = v(1.0, 0.0, 0.0, 4.0);
        Assert.assertTrue(a.isZeroAt(1));
        Assert.assertFalse(a.isZeroAt(3));
    }

    @Test
    public void testNonZeroAt_6() {
        SparseVector a = v(0.0, 5.0, 2.0, 0.0, 0.0, 0.0);
        Assert.assertTrue(a.nonZeroAt(1));
        Assert.assertFalse(a.nonZeroAt(3));
    }

    @Test
    public void testGetOrElse_5() {
        SparseVector a =v(0.0, 0.0, 1.0, 0.0, 0.0);
        Assert.assertEquals(0.0, a.getOrElse(1, 0.0), Vectors.EPS);
        Assert.assertEquals(1.0, a.getOrElse(2, 3.14), Vectors.EPS);
        Assert.assertEquals(4.2, a.getOrElse(3, 4.2), Vectors.EPS);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGet_IndexCheck_Negative() {
        SparseVector a = v(0.0, 0.0, 1.0, 0.0, 0.0);
        a.get(-1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGet_IndexCheck_TooLarge() {
        SparseVector a = v(0.0, 0.0, 1.0, 0.0, 0.0);
        a.get(a.length());
    }
    
    @Test
    public void testGet_IndexCheck_Valid() {
        SparseVector a = v(0.0, 0.0, 1.0, 0.0, 0.0);
        Assert.assertEquals(1.0, a.get(2), 0.0);
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void testGetOrElse_IndexCheck_Negative() {
        SparseVector a = v(0.0, 0.0, 1.0, 0.0, 0.0);
        a.getOrElse(-1, 0.0);
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void testGetOrElse_IndexCheck_TooLarge() {
        SparseVector a = v(0.0, 0.0, 1.0, 0.0, 0.0);
        a.getOrElse(a.length(), 0.0);
    }
    
    @Test
    public void testGetOrElse_IndexCheck_Valid() {
        SparseVector a = v(0.0, 0.0, 1.0, 0.0, 0.0);
        Assert.assertEquals(1.0, a.getOrElse(2, 0.0), 0.0);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSet_IndexCheck_Negative() {
        SparseVector a = v(0.0, 0.0, 1.0, 0.0, 0.0);
        a.set(-1, 1.0);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSet_IndexCheck_TooLarge() {
        SparseVector a = v(0.0, 0.0, 1.0, 0.0, 0.0);
        a.set(a.length(), 1.0);
    }
    
    @Test
    public void testSet_IndexCheck_Valid() {
        SparseVector a = v(0.0, 0.0, 1.0, 0.0, 0.0);
        a.set(0, 1.0);
        Assert.assertEquals(1.0, a.get(0), 0.0);
    }
}
