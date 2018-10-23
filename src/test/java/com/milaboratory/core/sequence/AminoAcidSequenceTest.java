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
package com.milaboratory.core.sequence;

import com.milaboratory.core.io.util.IOTestUtil;
import com.milaboratory.core.sequence.AminoAcidSequence.AminoAcidSequencePosition;
import com.milaboratory.test.TestUtil;
import org.junit.Assert;
import org.junit.Test;

import static com.milaboratory.core.sequence.TranslationParameters.*;

/**
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public class AminoAcidSequenceTest {
    @Test
    public void test1() throws Exception {
        Assert.assertEquals(new AminoAcidSequence("IR_"),
                AminoAcidSequence.translate(new NucleotideSequence("ATTAGACA"), FromLeftWithIncompleteCodon));
        Assert.assertEquals(new AminoAcidSequence("_*T"),
                AminoAcidSequence.translate(new NucleotideSequence("ATTAGACA"), FromRightWithIncompleteCodon));

        Assert.assertEquals(new AminoAcidSequence("I_T"),
                AminoAcidSequence.translate(new NucleotideSequence("ATTAGACA"), FromCenter));

        for (TranslationParameters tt : TranslationParameters.getPreDefinedParameters())
            Assert.assertEquals("TT=" + tt, new AminoAcidSequence("IR"),
                    AminoAcidSequence.translate(new NucleotideSequence("ATTAGA"), tt));
    }

    @Test
    public void test2() throws Exception {
        Assert.assertEquals(new AminoAcidSequence("IR"),
                AminoAcidSequence.translate(new NucleotideSequence("ATTAGACA"), FromLeftWithoutIncompleteCodon));
        Assert.assertEquals(new AminoAcidSequence("*T"),
                AminoAcidSequence.translate(new NucleotideSequence("ATTAGACA"), FromRightWithoutIncompleteCodon));
    }

    public static final TranslationParameters[] ALL_WITH_INCOMPLETE = {FromCenter, FromLeftWithIncompleteCodon, FromRightWithIncompleteCodon};
    public static final TranslationParameters[] ALL_WITHOUT_INCOMPLETE = {FromLeftWithoutIncompleteCodon, FromRightWithoutIncompleteCodon};

    @Test
    public void test3() throws Exception {
        for (TranslationParameters tt : ALL_WITH_INCOMPLETE)
            Assert.assertEquals("TT=" + tt, new AminoAcidSequence("_"),
                    AminoAcidSequence.translate(new NucleotideSequence("AT"), tt));
        for (TranslationParameters tt : ALL_WITHOUT_INCOMPLETE)
            Assert.assertEquals("TT=" + tt, new AminoAcidSequence(""),
                    AminoAcidSequence.translate(new NucleotideSequence("AT"), tt));
        for (TranslationParameters tt : TranslationParameters.getPreDefinedParameters())
            Assert.assertEquals("TT=" + tt, new AminoAcidSequence(""),
                    AminoAcidSequence.translate(new NucleotideSequence(""), tt));
    }

    @Test
    public void testConvertPositionLeft1() throws Exception {
        Assert.assertEquals(new AminoAcidSequencePosition(0, 1),
                AminoAcidSequence.convertNtPositionToAAFromLeft(1, 10));
        Assert.assertEquals(new AminoAcidSequencePosition(0, 0),
                AminoAcidSequence.convertNtPositionToAAFromLeft(0, 10));
        Assert.assertEquals(new AminoAcidSequencePosition(2, 1),
                AminoAcidSequence.convertNtPositionToAAFromLeft(7, 13));
    }

    @Test
    public void testConvertPositionRight1() throws Exception {
        Assert.assertEquals(new AminoAcidSequencePosition(1, 0),
                AminoAcidSequence.convertNtPositionToAAFromRight(1, 10));
        Assert.assertEquals(new AminoAcidSequencePosition(0, 2),
                AminoAcidSequence.convertNtPositionToAAFromRight(0, 10));
        Assert.assertEquals(new AminoAcidSequencePosition(3, 2),
                AminoAcidSequence.convertNtPositionToAAFromRight(9, 10));
    }

    @Test
    public void testConvertPositionRight2() throws Exception {
        Assert.assertEquals(new AminoAcidSequencePosition(0, 1),
                AminoAcidSequence.convertNtPositionToAAFromRight(1, 9));
        Assert.assertEquals(new AminoAcidSequencePosition(0, 0),
                AminoAcidSequence.convertNtPositionToAAFromRight(0, 9));
        Assert.assertEquals(new AminoAcidSequencePosition(2, 2),
                AminoAcidSequence.convertNtPositionToAAFromRight(8, 9));
    }

    @Test
    public void testConvertPositionCenter1() throws Exception {
        AminoAcidSequencePosition[] positions = {
                new AminoAcidSequencePosition(0, 0),
                new AminoAcidSequencePosition(0, 1),
                new AminoAcidSequencePosition(0, 2),
                new AminoAcidSequencePosition(1, 0),
                new AminoAcidSequencePosition(1, 1),
                new AminoAcidSequencePosition(1, 2),
                new AminoAcidSequencePosition(2, 0),
                new AminoAcidSequencePosition(3, 0),
                new AminoAcidSequencePosition(3, 1),
                new AminoAcidSequencePosition(3, 2),
        };

        for (int i = 0; i < positions.length; i++) {
            Assert.assertEquals(positions[i],
                    AminoAcidSequence.convertNtPositionToAAFromCenter(i, 10));
            Assert.assertEquals(i - positions[i].positionInTriplet,
                    AminoAcidSequence.convertAAPositionToNtFromCenter(positions[i].aminoAcidPosition, 10));
        }
    }

    @Test
    public void testConvertPositionCenter2() throws Exception {
        AminoAcidSequencePosition[] positions = {
                new AminoAcidSequencePosition(0, 0),
                new AminoAcidSequencePosition(0, 1),
                new AminoAcidSequencePosition(0, 2),
                new AminoAcidSequencePosition(1, 0),
                new AminoAcidSequencePosition(1, 1),
                new AminoAcidSequencePosition(1, 2),
                new AminoAcidSequencePosition(2, 0),
                new AminoAcidSequencePosition(2, 1),
                new AminoAcidSequencePosition(2, 2),
        };
        for (int i = 0; i < positions.length; i++) {
            Assert.assertEquals(positions[i],
                    AminoAcidSequence.convertNtPositionToAAFromCenter(i, 9));
            Assert.assertEquals(i - positions[i].positionInTriplet,
                    AminoAcidSequence.convertAAPositionToNtFromCenter(positions[i].aminoAcidPosition, 9));
        }
    }

    @Test
    public void testConvertPositionCenter3() throws Exception {
        AminoAcidSequencePosition[] positions = {
                new AminoAcidSequencePosition(0, 0),
                new AminoAcidSequencePosition(0, 1),
                new AminoAcidSequencePosition(0, 2),
                new AminoAcidSequencePosition(1, 0),
                new AminoAcidSequencePosition(1, 1),
                new AminoAcidSequencePosition(1, 2),
                new AminoAcidSequencePosition(2, 0),
                new AminoAcidSequencePosition(2, 1),
                new AminoAcidSequencePosition(3, 0),
                new AminoAcidSequencePosition(3, 1),
                new AminoAcidSequencePosition(3, 2),
        };
        for (int i = 0; i < positions.length; i++) {
            Assert.assertEquals(positions[i],
                    AminoAcidSequence.convertNtPositionToAAFromCenter(i, 11));
            Assert.assertEquals(i - positions[i].positionInTriplet,
                    AminoAcidSequence.convertAAPositionToNtFromCenter(positions[i].aminoAcidPosition, 11));
        }
    }

    @Test
    public void testConvertPositionSync1() throws Exception {
        for (TranslationParameters tt : TranslationParameters.getPreDefinedParameters()) {
            System.out.println(tt);
            for (int length = 2; length < 20; length++) {
                System.out.println(length);
                for (int i = 0; i < length; i++) {
                    AminoAcidSequencePosition pos = AminoAcidSequence.convertNtPositionToAA(i, length, tt);
                    int actual = pos != null ? AminoAcidSequence.convertAAPositionToNt(pos.aminoAcidPosition, length, tt) : -1;
                    //System.out.println(pos + "\t" + i + "\t" + actual);
                    if (pos == null) {
                        Assert.assertFalse(tt.includeIncomplete);
                        continue;
                    }
                    Assert.assertEquals("TT=" + tt + " i=" + i + " length=" + length, Math.max(i - pos.positionInTriplet, 0),
                            actual);
                }
            }
        }
    }

    @Test
    public void testName() throws Exception {
        //System.out.println(AminoAcidSequence.convertNtPositionToAA(1, 6, FromRightWithIncompleteCodon));
        System.out.println(AminoAcidSequence.convertAAPositionToNt(1, 6, FromRightWithIncompleteCodon));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnknownSymbol1() throws Exception {
        new AminoAcidSequence("ATTAGACANXOD");
    }

    @Test
    public void test4() throws Exception {
        AminoAcidSequence se = new AminoAcidSequence("ATTAGACAN");
        IOTestUtil.assertJavaSerialization(se);
    }

    @Test
    public void test5() throws Exception {
        AminoAcidSequence se = new AminoAcidSequence("ATTAGACAN");
        TestUtil.assertJson(se);
    }
}